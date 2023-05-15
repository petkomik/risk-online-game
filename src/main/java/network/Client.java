package network;

import database.Profile;
import game.gui.CreateProfilePaneController;
import game.gui.GUISupportClasses;
import game.gui.GamePaneController;
import game.gui.LobbyGui;
import game.gui.ServerMainWindowController;
import game.models.Battle;
import game.models.Card;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import game.state.ChoosePane;
import game.state.GameHandler;
import game.state.Period;
import game.state.Phase;
import general.AppController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageCreateLobby;
import network.messages.MessageDisconnect;
import network.messages.MessageGuiOpenBattleFrame;
import network.messages.MessageGuiRollDiceBattle;
import network.messages.MessageGuiRollInitalDice;
import network.messages.MessageGuiconquerCountry;
import network.messages.MessageGuiendBattle;
import network.messages.MessageGuimoveTroopsFromTerritoryToOther;
import network.messages.MessageGuipossessCountry;
import network.messages.MessageGuisetCurrentPlayer;
import network.messages.MessageGuisetPeriod;
import network.messages.MessageGuisetPhase;
import network.messages.MessageGuisetTroopsOnTerritory;
import network.messages.MessageGuisetTroopsOnTerritoryAndLeft;
import network.messages.MessageGuiupdateRanks;
import network.messages.MessageJoinLobby;
import network.messages.MessageProfile;
import network.messages.MessageReadyToPlay;
import network.messages.MessageSend;
import network.messages.MessageSendInGame;
import network.messages.MessageServerCloseConnection;
import network.messages.MessageToPerson;
import network.messages.MessageUpdateLobby;
import network.messages.MessageUpdateLobbyList;
import network.messages.MessageuigameIsOver;

/**
 * Client class represents a client that connects to the server. It has properties such as socket,
 * input/output stream, user profile, username, player, client thread, lobby, chat window, host
 * status, and game status. It also has static ArrayList profiles and HashMap lobbies that keep
 * track of all the profiles and lobbies respectively. It also handles the listening communication
 * from the side of the client.
 *
 * @author [dignatov]
 */
public class Client {
  private Socket socket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;
  private Profile profile;
  private String userName;
  private Player player;
  private Thread clientThread;
  public static ArrayList<Profile> profiles = new ArrayList<>();
  private HashMap<String, Lobby> lobbies = new HashMap<>();
  private Lobby clientsLobby;
  GUISupportClasses.ChatWindow chat;
  private boolean host;
  private boolean isInaLobby;
  private boolean isInaGame;
  private GamePaneController gamePane;
  private GameHandler gameHandler;
  private volatile boolean stopFlag = false;

  /**
   * Constructor for the Client class. Initializes the client with a socket and profile, sets up
   * input/output streams and sends an initial profile message.
   *
   * @param socket The socket for communication.
   * @param profile The profile associated with the client.
   */
  public Client(Socket socket, Profile profile) {
    this.profile = profile;
    this.userName = profile.getUserName();
    this.socket = socket;
    isInaLobby = false;
    isInaGame = false;
    try {
      this.outputStream = new ObjectOutputStream(socket.getOutputStream());
      this.inputStream = new ObjectInputStream(socket.getInputStream());
      outputStream.writeObject(new MessageProfile(profile));
      outputStream.flush();
    } catch (Exception e) {
      if (host) {
        sendMessage(new MessageServerCloseConnection());
        closeEverything(socket, inputStream, outputStream);
        host = false;
        Server.closeServerSocket();
      } else {
        MessageDisconnect disconnectMessage = new MessageDisconnect(profile);
        sendMessage(disconnectMessage);
        closeEverything(socket, inputStream, outputStream);
        e.printStackTrace();
      }
    }

  }

  /**
   * Creates a client and connects it to the specified host and port.
   *
   * @param host The host to connect to.
   * @param port The port to connect to.
   * @return The created client.
   * @throws IOException If an I/O error occurs when creating the socket.
   */

  public static Client createClient(String host, int port) throws IOException {
    Profile profile = AppController.getProfile();
    Socket socket;
    Client client;
    socket = new Socket(host, port);
    client = new Client(socket, profile);
    return client;
  }

  /**
   * Sets the chat window for the client.
   *
   * @param serverMainWindowController The main window controller for the server.
   */

  public void setChat(GUISupportClasses.ChatWindow serverMainWindowController) {
    chat = serverMainWindowController;
  }

  /**
   * Closes all the resources associated with the client.
   */
  public void closeEverything() {

    closeEverything(socket, inputStream, outputStream);
  }

  /**
   * Closes all the resources associated with the client.
   *
   * @param socket2 The socket to close.
   * @param inputStream2 The input stream to close.
   * @param outputStream2 The output stream to close.
   */

  private void closeEverything(Socket socket2, ObjectInputStream inputStream2,
      ObjectOutputStream outputStream2) {
    try {
      if (socket2 != null) {
        socket2.close();
      }
      if (outputStream2 != null) {
        outputStream2.close();
      }
      if (inputStream2 != null) {
        inputStream2.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    clientThread.interrupt();

  }

  /**
   * Removes a profile from the static list of profiles.
   *
   * @param profilee The profile to remove.
   */

  public static void removeProfile(Profile profilee) {
    for (Profile profile : profiles) {
      if (profile.equals(profilee)) {
        profiles.remove(profile);
      }
    }
  }


  /**
   * Sends a message.
   *
   * @param message The message to send.
   */

  public void sendMessage(Message message) {
    try {
      outputStream.flush();
      outputStream.writeObject(message);
      outputStream.flush();
    } catch (IOException e) {
      closeEverything(socket, inputStream, outputStream);
      e.printStackTrace();
    }
  }

  /**
   * Listens for incoming messages from the client. This method creates a new thread to continuously
   * listen for messages from the client. It retrieves messages from the input stream and processes
   * them based on their type. The method handles various types of messages including regular chat
   * messages, in-game chat messages, client connection and disconnection messages, and server close
   * connection message.
   *
   * @throws Exception if an error occurs while reading messages from the input stream.
   */

  public void listenForMessage() {
    clientThread = new Thread(new Runnable() {
      @Override
      public void run() {
        Message message;
        BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
        while (socket.isConnected() && !stopFlag) {
          try {

            if (!messageQueue.isEmpty()) {
              message = messageQueue.take();
            } else {
              // Otherwise, wait for a message from the client
              message = (Message) inputStream.readObject();
            }

            switch (message.getMessageType()) {

              case MessageSend:
                Profile profileFrom = ((MessageSend) message).getProfileFrom();
                boolean isInLobby = ((MessageSend) message).isForLobby();
                boolean isSenderInSameLobby = lobbies.values().stream()
                    .anyMatch(lobby -> lobby.getHumanPlayerList().stream()
                        .anyMatch(player -> player.getId() == profileFrom.getId())
                        && lobby.getHumanPlayerList().stream()
                            .anyMatch(player -> player.getId() == profile.getId()));
                if (isInLobby) {
                  if (isSenderInSameLobby) {
                    chat.addLabel(
                        profileFrom.getUserName() + ": " + ((MessageSend) message).getMessage());
                  }
                } else {
                  if (!isInaLobby) {
                    chat.addLabel(
                        profileFrom.getUserName() + ": " + ((MessageSend) message).getMessage());
                  }
                }

                break;

              case MessageSendInGame:
                MessageSendInGame mesChat = ((MessageSendInGame) message);
                Platform.runLater(() -> {
                  chat.addLabel(mesChat.getProfile().getUserName() + ": " + mesChat.getMessage());
                });
                break;

              case Connect:
                Profile profilee = ((MessageConnect) message).getProfile();
                if (AppController.dbH.getProfileById(profilee.getId()) == null) {
                  AppController.dbH.createProfileData(profilee);
                }
                AppController.dbH.deleteProfile(profilee.getId());
                AppController.dbH.createProfileData(profilee);

                profiles.add(profilee);
                chat.addItemsInComboBox(profilee);
                chat.addLabel(((MessageConnect) message).getProfile().getUserName()
                    + " has joined the server");
                sendMessage(new MessageConnect(getProfile(), profilee.getId()));

                if (!lobbies.isEmpty()) {

                  sendMessage(new MessageUpdateLobbyList(lobbies,
                      ((MessageConnect) message).getProfile().getId()));
                }
                break;

              case Disconnect:
                chat.addLabel(
                    ((MessageDisconnect) message).getProfile().getUserName() + " has left ");
                chat.removeItemsInComboBox(((MessageDisconnect) message).getProfile());
                break;

              case MessageServerCloseConnection:
                chat.addLabel("Host has left, please reconnect to a new server ");
                try {
                  if (host) {
                    host = false;
                    setStopFlag(true);
                    closeEverything(socket, inputStream, outputStream);
                    Server.closeServerSocket();
                  } else {
                    setStopFlag(true);
                    closeEverything(socket, inputStream, outputStream);
                  }
                } catch (Exception e2) {
                  e2.printStackTrace();
                }
                break;
              case MessageToPerson:

                String textMessage1 = ((MessageToPerson) message).getStringMessage();
                Profile profileFrom1 = ((MessageToPerson) message).getFromProfile();
                boolean isInLobby1 = ((MessageToPerson) message).isInaLobby();
                boolean isSenderInSameLobby1 = lobbies.values().stream()
                    .anyMatch(lobby -> lobby.getHumanPlayerList().stream()
                        .anyMatch(player -> player.getId() == profileFrom1.getId())
                        && lobby.getHumanPlayerList().stream()
                            .anyMatch(player -> player.getId() == profile.getId()));

                if (isInLobby1) {
                  if (isSenderInSameLobby1) {
                    chat.addLabel(((MessageToPerson) message).getStringMessage(),
                        "(private) " + ((MessageToPerson) message).getFromProfile().getUserName());
                  }

                } else {
                  if (!isInaLobby) {
                    chat.addLabel(textMessage1, "(private) " + profileFrom1.getUserName());
                  }

                }

                break;

              case MessageProfile:
                if (!profiles.contains(((MessageProfile) message).getProfile())) {
                  profiles.add(((MessageProfile) message).getProfile());
                }
                Profile profilee1 = ((MessageProfile) message).getProfile();
                if (AppController.dbH.getProfileById(profilee1.getId()) == null) {

                  AppController.dbH.createProfileData(profilee1);
                } else {
                  AppController.dbH.deleteProfile(profilee1.getId());
                  AppController.dbH.createProfileData(profilee1);
                }
                chat.addItemsInComboBox(profilee1);
                break;

              case MessageCreateLobby:

                MessageCreateLobby mcL = (MessageCreateLobby) message;
                LobbyGui lobbyGui = new LobbyGui(mcL.getLobby());

                lobbyGui.setOnAction(new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    AppController.getGameSound().buttonClickBackwardSound();
                    ServerMainWindowController.selectedLobby = lobbyGui.getLobby();
                    for (LobbyGui lobbyEnt : ServerMainWindowController.lobbyGUIList.values()) {
                      lobbyEnt.setSelected(false);
                    }

                    lobbyGui.setSelected(true);

                  }
                });
                ServerMainWindowController.lobbyGUIList.put(mcL.getLobby().getLobbyName(),
                    lobbyGui);
                ServerMainWindowController.drawLobbies(true);
                ServerMainWindowController.getSearchButton().fire();
                lobbies.put(mcL.getLobby().getLobbyName(), mcL.getLobby());
                break;

              case MessageJoinLobby:
                MessageJoinLobby mjL = (MessageJoinLobby) message;
                lobbies.replace(mjL.getLobby().getLobbyName(), mjL.getLobby());
                ServerMainWindowController.lobbyGUIList.replace(mjL.getLobby().getLobbyName(),
                    new LobbyGui(mjL.getLobby()));

                for (Player player : mjL.getLobby().getPlayersJoined()) {
                  if (profile.getId() == player.getId()) {
                    ServerMainWindowController
                        .drawLobbyMenu(lobbies.get(mjL.getLobby().getLobbyName()));
                  }
                }
                ServerMainWindowController.drawLobbies(true);
                ServerMainWindowController.getSearchButton().fire();
                break;

              case MessageUpdateLobby:
                MessageUpdateLobby messageUpdateLobby = (MessageUpdateLobby) message;
                lobbies.replace(messageUpdateLobby.getLobby().getLobbyName(),
                    messageUpdateLobby.getLobby());
                ServerMainWindowController.lobbyGUIList.replace(
                    messageUpdateLobby.getLobby().getLobbyName(),
                    new LobbyGui(messageUpdateLobby.getLobby()));
                for (Player player : messageUpdateLobby.getLobby().getPlayersJoined()) {
                  if (profile.getId() == player.getId()) {
                    ServerMainWindowController
                        .drawLobbyMenu(lobbies.get(messageUpdateLobby.getLobby().getLobbyName()));
                  }
                }
                if (messageUpdateLobby.getLobby().getHumanPlayerList().isEmpty()) {
                  ServerMainWindowController.lobbyGUIList
                      .remove(messageUpdateLobby.getLobby().getLobbyName());
                  lobbies.remove(messageUpdateLobby.getLobby().getLobbyName());
                }
                ServerMainWindowController.drawLobbies(true);
                ServerMainWindowController.getSearchButton().fire();
                break;

              case MessageUpdateLobbyList:

                for (Map.Entry<String, Lobby> entry : ((MessageUpdateLobbyList) message)
                    .getLobbyList().entrySet()) {
                  String key = entry.getKey();
                  Lobby lobby = entry.getValue();
                  lobbies.putIfAbsent(key, lobby);

                }
                lobbies.forEach((key, value) -> {

                  if (!ServerMainWindowController.lobbyGUIList.containsKey(key)) {
                    ServerMainWindowController.lobbyGUIList.put(key, new LobbyGui(value));
                  }
                });
                ServerMainWindowController.drawLobbies(true);
                ServerMainWindowController.getSearchButton().fire();
                break;

              case MessageReadyToPlay:
                MessageReadyToPlay messageReadyToPlay = ((MessageReadyToPlay) message);
                Lobby lobbyWithAvatars = messageReadyToPlay.getLobby();
                lobbyWithAvatars.updateAvatarDir();
                for (Player player : lobbyWithAvatars.getPlayersJoined()) {
                  if (profile.getId() == player.getId()) {
                    Platform.runLater(() -> {

                      gameHandler = new GameHandler(lobbyWithAvatars);
                      gameHandler.initMultiplayer(returnClient());
                      clientsLobby = lobbyWithAvatars;
                      ServerMainWindowController.startMultyplayerGame(lobbyWithAvatars);
                      isInaGame = true;
                      setInaLobby(false);
                      chat = gamePane.getChatWindow();
                    });
                  }
                }
                lobbies.remove(lobbyWithAvatars.getLobbyName());
                ServerMainWindowController.lobbyGUIList.remove(lobbyWithAvatars.getLobbyName());
                ServerMainWindowController.drawLobbies(true);
                break;

              case MessageGUIRollInitalDice:
                final MessageGuiRollInitalDice me = ((MessageGuiRollInitalDice) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(me.getGameState());
                  gamePane.rollInitialDice(me.getId(), me.getValue());
                });
                break;

              case MessageGUIRollDiceBattle:
                final MessageGuiRollDiceBattle mes = ((MessageGuiRollDiceBattle) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuiRollDiceBattle) mes).getGameState());
                  try {
                    gamePane.rollDiceBattle(mes.getAttackerDiceValues(),
                        mes.getDefenderDiceValues(), mes.getTroopsInAttackAt(),
                        mes.getTroopsInAttackDf(), mes.getNumberOfDice());
                  } catch (FileNotFoundException e) {
                    e.printStackTrace();
                  }
                });
                break;

              case MessageGUIsetPeriod:
                MessageGuisetPeriod mesP = ((MessageGuisetPeriod) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuisetPeriod) mesP).getGameState());
                  gamePane.setPeriod(mesP.getPeriod());
                });
                break;
              case MessageGUIsetPhase:
                MessageGuisetPhase mesPh = ((MessageGuisetPhase) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuisetPhase) mesPh).getGameState());
                  gamePane.setPhase(mesPh.getPhase());
                });
                break;

              case MessageGUIpossessCountry:
                MessageGuipossessCountry mesCo = ((MessageGuipossessCountry) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuipossessCountry) mesCo).getGameState());
                  gamePane.claimCountry(mesCo.getCountry(), mesCo.getId());
                  gamePane.setAmountOfTroopsLeftToDeploy(
                      ((MessageGuipossessCountry) mesCo).getTroopsLeft());
                });
                break;

              case MessageGUIconquerCountry:

                MessageGuiconquerCountry mesCoCo = ((MessageGuiconquerCountry) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuiconquerCountry) mesCoCo).getGameState());
                  gamePane.conquerCountry(((MessageGuiconquerCountry) mesCoCo).getCountry(),
                      ((MessageGuiconquerCountry) mesCoCo).getId(),
                      ((MessageGuiconquerCountry) mesCoCo).getTroops());
                });
                break;

              case MessageGUIsetCurrentPlayer:
                final MessageGuisetCurrentPlayer mesCur = ((MessageGuisetCurrentPlayer) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(mesCur.getGameState());
                  gamePane.setCurrentPlayer(mesCur.getId());
                  gamePane.setAmountOfTroopsLeftToDeploy(mesCur.getTroopsLeft());
                  gamePane.setPlayerOnGUI(AppController.getProfile().getId(), mesCur.getGameState()
                      .getRiskCardsInPlayers().get(AppController.getProfile().getId()));
                });
                break;

              case MessageGUIsetTroopsOnTerritory:

                MessageGuisetTroopsOnTerritory mesTrOnTe =
                    ((MessageGuisetTroopsOnTerritory) message);
                Platform.runLater(() -> {
                  gameHandler
                      .setGameState(((MessageGuisetTroopsOnTerritory) mesTrOnTe).getGameState());
                  gamePane.setNumTroops(
                      ((MessageGuisetTroopsOnTerritory) mesTrOnTe).getCountryName(),
                      ((MessageGuisetTroopsOnTerritory) mesTrOnTe).getNumTroopsOfCountry());
                });
                break;

              case MessageGUIsetTroopsOnTerritoryAndLeft:
                MessageGuisetTroopsOnTerritoryAndLeft mesTrOnTeAndLe =
                    ((MessageGuisetTroopsOnTerritoryAndLeft) message);

                Platform.runLater(() -> {
                  gameHandler.setGameState(
                      ((MessageGuisetTroopsOnTerritoryAndLeft) mesTrOnTeAndLe).getGameState());
                  gamePane.setNumTroops(
                      ((MessageGuisetTroopsOnTerritoryAndLeft) mesTrOnTeAndLe).getCountryName(),
                      ((MessageGuisetTroopsOnTerritoryAndLeft) mesTrOnTeAndLe)
                          .getNumTroopsOfCountry());
                  gamePane.setAmountOfTroopsLeftToDeploy(
                      ((MessageGuisetTroopsOnTerritoryAndLeft) mesTrOnTeAndLe)
                          .getNumTroopsOfPlayer());
                });
                break;

              case MessageGUImoveTroopsFromTerritoryToOther:

                MessageGuimoveTroopsFromTerritoryToOther mesTrFromTeToO =
                    ((MessageGuimoveTroopsFromTerritoryToOther) message);

                Platform.runLater(() -> {
                  gameHandler.setGameState(
                      ((MessageGuimoveTroopsFromTerritoryToOther) mesTrFromTeToO).getGameState());
                  gamePane.setNumTroops(
                      ((MessageGuimoveTroopsFromTerritoryToOther) mesTrFromTeToO).getFrom(),
                      ((MessageGuimoveTroopsFromTerritoryToOther) mesTrFromTeToO).getNumberFrom());
                  gamePane.setNumTroops(
                      ((MessageGuimoveTroopsFromTerritoryToOther) mesTrFromTeToO).getTo(),
                      ((MessageGuimoveTroopsFromTerritoryToOther) mesTrFromTeToO).getNumberTo());
                });
                break;

              case MessageGUIOpenBattleFrame:

                MessageGuiOpenBattleFrame mesobF = ((MessageGuiOpenBattleFrame) message);

                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuiOpenBattleFrame) mesobF).getGameState());
                  gamePane.openBattleFrame(((MessageGuiOpenBattleFrame) mesobF).getBattle());
                });
                break;

              case MessageGUIendBattle:

                MessageGuiendBattle meseB = ((MessageGuiendBattle) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuiendBattle) meseB).getGameState());
                  gamePane.closeBattleFrame();
                });

                break;

              case MessageGUIupdateRanks:

                MessageGuiupdateRanks mesUpRa = ((MessageGuiupdateRanks) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageGuiupdateRanks) mesUpRa).getGameState());
                  gamePane.setPlayersRanking(((MessageGuiupdateRanks) mesUpRa).getRanks());
                });

                break;
              case MessageGUIgameIsOver:

                MessageuigameIsOver mesGaOv = ((MessageuigameIsOver) message);
                Platform.runLater(() -> {
                  gameHandler.setGameState(((MessageuigameIsOver) mesGaOv).getGameState());
                  gamePane.endGame(((MessageuigameIsOver) mesGaOv).getPodium());
                });

                if (!mesGaOv.getPodium().get(0).isAi()) {
                  AppController.dbH.updateProfileInfo(
                      AppController.dbH.getProfileById(mesGaOv.getPodium().get(0).getId()).getWins()
                          + 1,
                      "Wins", mesGaOv.getPodium().get(0).getId());
                }
                if (mesGaOv.getPodium().get(0).getId() == AppController.getProfile().getId()) {
                  AppController.getProfile().setWins(AppController.getProfile().getWins() + 1);
                }
                for (Player p : mesGaOv.getLobby().getHumanPlayerList()) {
                  if (p.getId() != (mesGaOv.getPodium().get(0).getId()) && !p.isAi()) {
                    AppController.dbH.updateProfileInfo(
                        AppController.dbH.getProfileById(p.getId()).getLoses() + 1, "Loses",
                        p.getId());
                  }
                  if (p.getId() == AppController.getProfile().getId()
                      && p.getId() != (mesGaOv.getPodium().get(0).getId())) {
                    AppController.getProfile().setLoses(AppController.getProfile().getLoses() + 1);
                  }
                }

                break;
              default:
                break;
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }
    });

    clientThread.start();
  }

  /**
   * Returns the current client object.
   *
   * @return The current client object.
   */
  public Client returnClient() {
    return this;
  }

  /**
   * Gets the profile associated with the client.
   *
   * @return The profile of the client.
   */
  public Profile getProfile() {
    return profile;
  }

  /**
   * Gets the lobby that the client is currently in.
   *
   * @return The lobby that the client is in.
   */
  public Lobby getClientsLobby() {
    return clientsLobby;
  }

  /**
   * Checks if the client is the host of the lobby.
   *
   * @return True if the client is the host, false otherwise.
   */
  public boolean isHost() {
    return host;
  }

  /**
   * Sets the client's host status.
   *
   * @param host The new host status of the client.
   */
  public void setHost(boolean host) {
    this.host = host;
  }

  /**
   * Gets a hashmap of all lobbies.
   *
   * @return A hashmap of all lobbies.
   */
  public HashMap<String, Lobby> getLobbies() {
    return lobbies;
  }

  /**
   * Sets the hashmap of all lobbies.
   *
   * @param lobbies The new hashmap of lobbies.
   */
  public void setLobbies(HashMap<String, Lobby> lobbies) {
    this.lobbies = lobbies;
  }

  /**
   * Checks if the client is currently in a lobby.
   *
   * @return True if the client is in a lobby, false otherwise.
   */
  public boolean isInaLobby() {
    return isInaLobby;
  }

  /**
   * Sets the client's status of being in a lobby.
   *
   * @param isInaLobby The new lobby status of the client.
   */
  public void setInaLobby(boolean isInaLobby) {
    this.isInaLobby = isInaLobby;
  }

  /**
   * Load a FXML file.
   *
   * @param fxml The name of the FXML file to load.
   * @return The root node of the loaded FXML file.
   * @throws IOException If an error occurs during loading.
   */
  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /**
   * Gets the GamePaneController associated with the client.
   *
   * @return The GamePaneController of the client.
   */
  public GamePaneController getGamePane() {
    return gamePane;
  }

  /**
   * Sets the GamePaneController associated with the client.
   *
   * @param gamePane The new GamePaneController of the client.
   */
  public void setGamePane(GamePaneController gamePane) {
    this.gamePane = gamePane;
  }

  // The rest of the methods in your class can be annotated similarly.
  // It would look something like this:

  /**
   * Method description goes here.
   *
   * @param id Description of parameter goes here.
   */
  public void playerThrowsInitalDice(int id) {
    Platform.runLater(() -> {
      this.gameHandler.playerThrowsInitialDice(id);
    });
  }

  /**
   * Handles a click on a country.
   *
   * @param id The ID of the player.
   * @param country The country being clicked.
   */
  public void clickCountry(int id, CountryName country) {
    Platform.runLater(() -> {
      this.gameHandler.clickCountry(id, country);
    });
  }

  /**
   * Cancels the number of troops in a country.
   *
   * @param country The country where the troops are being canceled.
   * @param choosePane The pane where the troops are being chosen.
   * @param idOfPlayer The ID of the player.
   */
  public void cancelNumberOfTroops(CountryName country, ChoosePane choosePane, int idOfPlayer) {
    Platform.runLater(() -> {
      this.gameHandler.cancelNumberOfTroops(country, choosePane, idOfPlayer);
    });
  }

  /**
   * Confirms the number of troops in a country.
   *
   * @param country The country where the troops are being confirmed.
   * @param troops The number of troops being confirmed.
   * @param choosePane The pane where the troops are being chosen.
   * @param idOfPlayer The ID of the player.
   */
  public void confirmNumberOfTroops(CountryName country, int troops, ChoosePane choosePane,
      int idOfPlayer) {
    Platform.runLater(() -> {
      this.gameHandler.confirmTroopsToCountry(country, troops, choosePane, idOfPlayer);
    });
  }

  /**
   * Handles a player turning in risk cards.
   *
   * @param cards The risk cards being turned in.
   * @param idOfPlayer The ID of the player.
   */
  public void turnInRiskCards(ArrayList<String> cards, int idOfPlayer) {
    Platform.runLater(() -> {
      this.gameHandler.turnInRiskCards(cards, idOfPlayer);
    });
  }

  /**
   * Ends the phase of the turn.
   *
   * @param period The period of the game.
   * @param phase The phase of the turn.
   * @param idOfPlayer The ID of the player.
   */
  public void endPhaseTurn(Period period, Phase phase, int idOfPlayer) {
    Platform.runLater(() -> {

      this.gameHandler.endPhaseTurn(period, phase, idOfPlayer);
    });
  }

  /**
   * Rolls dice for a battle.
   */
  public void battleDiceThrow() {
    Platform.runLater(() -> {
      this.gameHandler.battleDiceThrow();
    });
  }

  /**
   * Rolls dice for initial dice on the GUI and sends a 'MessageGUIRollInitalDice' message to all
   * other clients.
   *
   * @param idOfPlayer The ID of the player.
   * @param i The value of the dice.
   */
  public void rollInitialDiceOnGui(int idOfPlayer, int i) {
    Platform.runLater(() -> {
      sendMessage(
          new MessageGuiRollInitalDice(gameHandler.getGameState(), idOfPlayer, i, clientsLobby));
    });
  }

  /**
   * Rolls dice for a battle on the GUI and sends a 'MessageGUIRollDiceBattle' message to all other
   * clients.
   *
   * @param attackerDiceValues The values of the attacker's dice.
   * @param defenderDiceValues The values of the defender's dice.
   * @param troopsInAttackAt The number of troops in the attacking territory.
   * @param troopsInAttackDf The number of troops in the defending territory.
   * @param numberOfDice The number of dice.
   * @throws FileNotFoundException If an error occurs when accessing the file.
   */
  public void rollDiceBattleOnGui(int[] attackerDiceValues, int[] defenderDiceValues,
      int troopsInAttackAt, int troopsInAttackDf, int[] numberOfDice) throws FileNotFoundException {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuiRollDiceBattle(gameHandler.getGameState(), attackerDiceValues,
          defenderDiceValues, troopsInAttackAt, troopsInAttackDf, numberOfDice, clientsLobby));
    });
  }

  /**
   * Shows an exception on the GUI.
   *
   * @param e The exception to be shown.
   */
  public void showExeceptionOnGui(Exception e) {
    Platform.runLater(() -> {
      this.gamePane.showException(e.toString());
    });
  }

  /**
   * Sets the period on the GUI and sends a 'MessageGUIsetPeriod' message to all other clients.
   *
   * @param period The period to be set.
   */
  public void setPeriodOnGui(Period period) {
    Platform.runLater(() -> {
      sendMessage(new MessageGuisetPeriod(gameHandler.getGameState(), period, clientsLobby));
    });
  }

  /**
   * Sets the phase on the GUI and sends a 'MessageGUIsetPhase' message to all other clients.
   *
   * @param phase The phase to be set.
   */
  public void setPhaseOnGui(Phase phase) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuisetPhase(gameHandler.getGameState(), phase, clientsLobby));
    });
  }

  /**
   * Possesses a country on the GUI and sends a 'MessageGUIpossessCountry' message to all other
   * clients.
   *
   * @param country The country to be possessed.
   * @param id The ID of the player.
   * @param troopsLeft The number of troops left.
   */
  public void possesCountryOnGui(CountryName country, int id, int troopsLeft) {
    Platform.runLater(() -> {
      sendMessage(new MessageGuipossessCountry(gameHandler.getGameState(), country, id, troopsLeft,
          clientsLobby));
    });
  }

  /**
   * Conquers a country on the GUI and sends a 'MessageGUIconquerCountry' message to all other
   * clients.
   *
   * @param country The country to be conquered.
   * @param id The ID of the player.
   * @param troops The number of troops.
   */
  public void conquerCountryOnGui(CountryName country, int id, int troops) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuiconquerCountry(gameHandler.getGameState(), country, id, troops,
          clientsLobby));
    });
  }

  /**
   * Sets the current player on the GUI and sends a 'MessageGUIsetCurrentPlayer' message to all
   * other clients.
   *
   * @param id The ID of the player.
   * @param troopsLeft The number of troops left.
   */
  public void setCurrentPlayerOnGui(int id, int troopsLeft) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(
          new MessageGuisetCurrentPlayer(gameHandler.getGameState(), id, troopsLeft, clientsLobby));
    });
  }

  /**
   * Changes the player on the GUI.
   *
   * @param id The ID of the player.
   * @param cards The list of cards of the player.
   */
  public void chnagePlayerOnGui(int id, ArrayList<Card> cards) {
    Platform.runLater(() -> {
      this.gamePane.setPlayerOnGUI(id, cards);
    });
  }

  /**
   * Chooses the number of troops on the GUI.
   *
   * @param country The country to place the troops.
   * @param min The minimum number of troops.
   * @param max The maximum number of troops.
   * @param choosePane The choosePane to be used.
   */
  public void chooseNumberOfTroopsOnGui(CountryName country, int min, int max,
      ChoosePane choosePane) {
    Platform.runLater(() -> {
      this.gamePane.showChoosingTroopsPane(country, min, max, choosePane);
    });
  }

  /**
   * Closes the troops pane on GUI.
   */
  public void closeTroopsPaneOnGui() {
    Platform.runLater(() -> {
      this.gamePane.closeChoosingTroopsPane();
    });
  }

  /**
   * Sets the troops on a territory and sends a 'MessageGUIsetTroopsOnTerritory' message to all
   * other clients.
   *
   * @param countryName The name of the country where the troops are set.
   * @param numTroopsOfCountry The number of troops in the country.
   */
  public void setTroopsOnTerritory(CountryName countryName, int numTroopsOfCountry) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuisetTroopsOnTerritory(gameHandler.getGameState(), countryName,
          numTroopsOfCountry, clientsLobby));
    });
  }

  /**
   * Sets the troops on a territory and the remaining troops of the player, and sends a
   * 'MessageGUIsetTroopsOnTerritoryAndLeft' message to all other clients.
   *
   * @param countryName The name of the country where the troops are set.
   * @param numTroopsOfCountry The number of troops in the country.
   * @param numTroopsOfPlayer The number of troops left for the player.
   */
  public void setTroopsOnTerritoryAndLeftOnGui(CountryName countryName, int numTroopsOfCountry,
      int numTroopsOfPlayer) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuisetTroopsOnTerritoryAndLeft(gameHandler.getGameState(), countryName,
          numTroopsOfCountry, numTroopsOfPlayer, clientsLobby));
    });
  }

  /**
   * Moves troops from one territory to another on the GUI, and sends a
   * 'MessageGUImoveTroopsFromTerritoryToOther' message to all other clients.
   *
   * @param from The name of the country from where the troops are moved.
   * @param to The name of the country to where the troops are moved.
   * @param numberFrom The number of troops left in the 'from' country.
   * @param numberTo The number of troops in the 'to' country after the move.
   */
  public void moveTroopsFromTerritoryToOtherOnGui(CountryName from, CountryName to, int numberFrom,
      int numberTo) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuimoveTroopsFromTerritoryToOther(gameHandler.getGameState(), from, to,
          numberFrom, numberTo, clientsLobby));
    });
  }

  /**
   * Opens the battle frame on the GUI and sends a 'MessageGUIOpenBattleFrame' message to all other
   * clients.
   *
   * @param battle The battle to be displayed.
   */
  public void openBattleFrameOnGui(Battle battle) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuiOpenBattleFrame(gameHandler.getGameState(), battle, clientsLobby));
    });
  }

  /**
   * Ends the battle on the GUI and sends a 'MessageGUIendBattle' message to all other clients.
   */
  public void endBattleOnGui() {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageGuiendBattle(gameHandler.getGameState(), clientsLobby));
    });
  }

  /**
   * Shows the risk cards turned in successfully on the GUI.
   *
   * @param card The cards that were turned in.
   * @param idOfPlayer The ID of the player who turned in the cards.
   * @param bonusTroops The number of bonus troops the player receives for turning in the cards.
   */

  public void riskCardsTurnedInSuccessOnGui(ArrayList<Card> card, int idOfPlayer, int bonusTroops) {
    Platform.runLater(() -> {
      this.gamePane.setAmountOfTroopsLeftToDeploy(bonusTroops);
      this.gamePane.setPlayerOnGUI(idOfPlayer, card);
      sendMessage(new MessageGuisetCurrentPlayer(gameHandler.getGameState(), idOfPlayer,
          bonusTroops, clientsLobby));
    });
  }

  /**
   * Updates the ranks on the GUI and sends a 'MessageGUIupdateRanks' message to all other clients.
   *
   * @param ranks The updated ranks.
   */
  public void updateRanksOnGui(int[] ranks) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());

      sendMessage(new MessageGuiupdateRanks(gameHandler.getGameState(), ranks, clientsLobby));
    });
  }

  /**
   * Notifies that the game is over on the GUI and sends a 'MessageGUIgameIsOver' message to all
   * other clients.
   *
   * @param podium The list of players in the podium.
   */
  public void gameIsOverOnGui(ArrayList<Player> podium) {
    Platform.runLater(() -> {
      gameHandler.getGameState()
          .setGameStateVersion(1 + gameHandler.getGameState().getGameStateVersion());
      sendMessage(new MessageuigameIsOver(gameHandler.getGameState(), podium, clientsLobby));
    });
  }

  /**
   * Checks if the client is currently in a game.
   *
   * @return True if the client is in a game, false otherwise.
   */
  public boolean isInaGame() {
    return isInaGame;
  }

  public void setInaGame(boolean isInaGame) {
    this.isInaGame = isInaGame;
  }

  public boolean isStopFlag() {
    return stopFlag;
  }

  public void setStopFlag(boolean stopFlag) {
    this.stopFlag = stopFlag;
  }

  public String getUserName() {
    return userName;
  }

  public Player getPlayer() {
    return player;
  }

  /**
   * Sets the client's lobby.
   *
   * @param clientsLobby The lobby to set for the client.
   */


  public void setClientsLobby(Lobby clientsLobby) {
    this.clientsLobby = clientsLobby;
  }
}
