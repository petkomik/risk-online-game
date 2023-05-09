package network;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Platform;

import database.Profile;
import game.Battle;
import game.GameStatistic;
import game.Lobby;
import game.gui.CreateProfilePaneController;
import game.gui.GUISupportClasses;
import game.gui.GamePaneController;
import game.gui.LobbyGUI;
import game.gui.ServerMainWindowController;
import game.models.Card;
import game.models.CountryName;
import game.models.Player;
import gameState.ChoosePane;
import gameState.GameHandler;
import gameState.Period;
import gameState.Phase;
import gameState.SinglePlayerHandler;
import general.AppController;
import general.GameSound;
import general.Parameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageCreateLobby;
import network.messages.MessageDisconnect;
import network.messages.MessageGUIOpenBattleFrame;
import network.messages.MessageGUIRollDiceBattle;
import network.messages.MessageGUIRollInitalDice;
import network.messages.MessageGUIchnagePlayer;
import network.messages.MessageGUIconquerCountry;
import network.messages.MessageGUIendBattle;
import network.messages.MessageGUIgameIsOver;
import network.messages.MessageGUImoveTroopsFromTerritoryToOther;
import network.messages.MessageGUIpossessCountry;
import network.messages.MessageGUIsetCurrentPlayer;
import network.messages.MessageGUIsetPeriod;
import network.messages.MessageGUIsetPhase;
import network.messages.MessageGUIsetTroopsOnTerritory;
import network.messages.MessageGUIsetTroopsOnTerritoryAndLeft;
import network.messages.MessageGUIupdateRanks;
import network.messages.MessageJoinLobby;
import network.messages.MessageProfile;
import network.messages.MessageReadyToPlay;
import network.messages.MessageSend;
import network.messages.MessageServerCloseConnection;
import network.messages.MessageToPerson;
import network.messages.MessageUpdateLobby;
import network.messages.MessageUpdateLobbyList;

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
	private boolean isInALobby = false;
	private GamePaneController gamePane;
	private GameHandler gameHandler;

	public void setClientsLobby(Lobby clientsLobby) {
		this.clientsLobby = clientsLobby;
	}

	public Client(Socket socket, Profile profile) {
		this.profile = profile;
		this.userName = profile.getUserName();
		this.socket = socket;
		try {
			// update
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream.writeObject(new MessageProfile(profile));
			outputStream.flush();

			// this.sendMessage(new MessageConnect(profile));
			// newlineofCode
		} catch (Exception e) {
			System.out.println("DSICONNECT");
			// System.out.println(
			// "Player " + (((MessageConnect) message).getPlayername()) + " has been
			// connected ");
			MessageDisconnect disconnectMessage = new MessageDisconnect(profile);
			sendMessage(disconnectMessage);
			closeEverything(socket, inputStream, outputStream);
			e.printStackTrace();
		}

	}

	public void setChat(GUISupportClasses.ChatWindow serverMainWindowController) {
		chat = serverMainWindowController;
	}

	public Client(Socket socket, Player player) {

		this.player = player;
		this.userName = player.getName();
		this.socket = socket;
		try {

			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream.writeObject(new MessageProfile(profile));
			outputStream.flush();
			// this.sendMessage(new MessageConnect(profile));
			// newlineofCode
		} catch (IOException e) {
			System.out.println("DSICONNECT");
			// System.out.println(
			// "Player " + (((MessageConnect) message).getPlayername()) + " has been
			// connected ");
			MessageDisconnect disconnectMessage = new MessageDisconnect(profile);
			sendMessage(disconnectMessage);
			closeEverything(socket, inputStream, outputStream);
			e.printStackTrace();
		}

	}

	public void closeEverything() {

		closeEverything(socket, inputStream, outputStream);
		System.out.println("Closing  works");
	}

	private void closeEverything(Socket socket2, ObjectInputStream inputStream2, ObjectOutputStream outputStream2) {
		System.out.println("Close everything 2");
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

//	public void sendMessageViaConsole() {
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//
//				try {
//					Scanner scanner = new Scanner(System.in);
//					while (socket.isConnected()) {
//					//	outputStream.writeObject(new MessageSend(userName + ": " + scanner.nextLine()));
//						outputStream.flush();
//					}
//
//				} catch (Exception e) {
//					closeEverything(socket, inputStream, outputStream);
//				}
//			}
//		}).start();
//	}

//	public void sendMessage(String message) {
//		try {
//			outputStream.writeObject(new MessageSend(userName + ": " + message));
//			outputStream.flush();
//		} catch (IOException e) {
//			closeEverything(socket, inputStream, outputStream);
//			e.printStackTrace();
//		}
//	}

	public static Client createClient(String host, int port) throws IOException {
		AppController.getInstance();
		Profile profile = AppController.getProfile();
		Socket socket;
		Client client;
		socket = new Socket(host, port);
		client = new Client(socket, profile);

		return client;
	}

	public static void removeProfile(Profile profilee) {
		for (Profile profile : profiles) {
			if (profile.equals(profilee)) {
				profiles.remove(profile);
			}
		}
	}

	public void sendDirectMessage(Message message, int id) {
		try {
			outputStream.flush();
			outputStream.writeObject(message);
			outputStream.flush();
		} catch (IOException e) {
			closeEverything(socket, inputStream, outputStream);
			e.printStackTrace();
		}

	}

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

	public void listenForMessage() {
		clientThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message message;
				BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
				while (socket.isConnected()) {
					try {

						if (!messageQueue.isEmpty()) {
							message = messageQueue.take();
						} else {
							// Otherwise, wait for a message from the client
							message = (Message) inputStream.readObject();
						}

						switch (message.getMessageType()) {
						case MessageSend:

							System.out.println("case MessageSend in Clinet Success 0 ");
							System.out.println(((MessageSend) message).getMessage());

							String textMessage = ((MessageSend) message).getMessage();
							Profile profileFrom = ((MessageSend) message).getProfileFrom();
							boolean isInLobby = ((MessageSend) message).isForLobby();
							// Assume that each Lobby object has a List<HumanPlayer> called humanPlayerList,
							// and that this.profile refers to the profile of the program
							boolean isSenderInSameLobby = lobbies.values().stream()
									.anyMatch(lobby -> lobby.getHumanPlayerList().stream()
											.anyMatch(player -> player.getID() == profileFrom.getId())
											&& lobby.getHumanPlayerList().stream()
													.anyMatch(player -> player.getID() == profile.getId()));

							if (isInLobby) {
								if (isSenderInSameLobby) {
									chat.addLabel(((MessageSend) message).getMessage());
								}

							} else {
								if (!isInALobby) {
									chat.addLabel(((MessageSend) message).getMessage());

								}

							}

							// HostServerMessengerController.addLabel(((MessageSend) message).getMessage(),
							// vBoxMessages);
							// System.out.println(((MessageSend) message).getMessage());

							break;
						case Connect:
							// all clients profile of New connected client and send their profile only to
							// this one clinet
							System.out.println("case MessageConnect Success 1 ");
							Profile profilee = ((MessageConnect) message).getProfile();
							// Add Label that this client connected to the server
							if (AppController.dbH.getProfileByID(profilee.getId()) == null) {
								AppController.dbH.createProfileData(profilee);
							}
							profiles.add(profilee);
							chat.addItemsInComboBox(profilee);
							// The client who received the new Connected Client sends own profile
							chat.addLabel(
									((MessageConnect) message).getProfile().getUserName() + " has joined the server");
							sendMessage(new MessageConnect(getProfile(), profilee.getId()));

							if (!lobbies.isEmpty()) {

								sendMessage(new MessageUpdateLobbyList(lobbies,
										((MessageConnect) message).getProfile().getId()));
							}
							break;
						case Disconnect:
							System.out.println("case MessageConnect Success 2 ");

							chat.addLabel(((MessageDisconnect) message).getProfile().getUserName() + " has left ");
//							HostServerMessengerController.addLabel(
//									"Player " + ((MessageDisconnect) message).getPlayername() + " has disconnected",
//									vBoxMessages);
							chat.removeItemsInComboBox(((MessageDisconnect) message).getProfile());
							break;
						case MessageServerCloseConnection:
							System.out.println("case MessageServerDisconnect in Clients Server Success 3 ");
							chat.addLabel("Host has left, please reconnect to a new server ");

							closeEverything(socket, inputStream, outputStream);
							Server.closeServerSocket();
							break;
						case MessageToPerson:
							System.out.println("case 4 in Client");

							String textMessage1 = ((MessageToPerson) message).getStringMessage();
							Profile profileFrom1 = ((MessageToPerson) message).getFromProfile();
							boolean isInLobby1 = ((MessageToPerson) message).isInALobby();
							// Assume that each Lobby object has a List<HumanPlayer> called humanPlayerList,
							// and that this.profile refers to the profile of the program
							boolean isSenderInSameLobby1 = lobbies.values().stream()
									.anyMatch(lobby -> lobby.getHumanPlayerList().stream()
											.anyMatch(player -> player.getID() == profileFrom1.getId())
											&& lobby.getHumanPlayerList().stream()
													.anyMatch(player -> player.getID() == profile.getId()));

							if (isInLobby1) {
								if (isSenderInSameLobby1) {
									chat.addLabel(((MessageToPerson) message).getStringMessage(),
											((MessageToPerson) message).getFromProfile().getUserName());
								}

							} else {
								if (!isInALobby) {
									chat.addLabel(textMessage1, profileFrom1.getUserName());
								}

							}

							System.out.println();
							// chat.addLabel(((MessageToPerson)message).getFromProfile().getUserName()+" : "
							// + ((MessageToPerson)message).getStringMessage());
							break;
						case MessageProfile:
							// ithe new client adds the other clients in his list and db
							if (!profiles.contains(((MessageProfile) message).getProfile())) {
								profiles.add(((MessageProfile) message).getProfile());
							}
							Profile profilee1 = ((MessageProfile) message).getProfile();
							if (AppController.dbH.getProfileByID(profilee1.getId()) == null) {
								AppController.dbH.createProfileData(profilee1);
								System.out.println("profileadding works");
							}
							chat.addItemsInComboBox(profilee1);
							System.out.println("MessageProfile");
							break;
						case MessageCreateLobby:

							MessageCreateLobby mCL = (MessageCreateLobby) message;

							LobbyGUI lobbyGUI = new LobbyGUI(mCL.getLobby());

							lobbyGUI.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									AppController.getGameSound().buttonClickBackwardSound();
									ServerMainWindowController.selectedLobby = lobbyGUI.getLobby();
									for (LobbyGUI lobbyEnt : ServerMainWindowController.lobbyGUIList.values()) {
										lobbyEnt.setSelected(false);
									}

									lobbyGUI.setSelected(true);

								}
							});

							ServerMainWindowController.lobbyGUIList.put(mCL.getLobby().getLobbyName(), lobbyGUI);
							ServerMainWindowController.drawLobbies(true);
							ServerMainWindowController.getSearchButton().fire();
							lobbies.put(mCL.getLobby().getLobbyName(), mCL.getLobby());

							lobbies.put(mCL.getLobby().getLobbyName(), mCL.getLobby());

							break;
						case MessageJoinLobby:
							MessageJoinLobby mJL = (MessageJoinLobby) message;
							lobbies.replace(mJL.getLobby().getLobbyName(), mJL.getLobby());
							ServerMainWindowController.lobbyGUIList.replace(mJL.getLobby().getLobbyName(),
									new LobbyGUI(mJL.getLobby()));

							for (Player player : mJL.getLobby().getPlayersJoined()) {
								if (profile.getId() == player.getID()) {
									ServerMainWindowController
											.drawLobbyMenu(lobbies.get(mJL.getLobby().getLobbyName()));
								}
							}
							ServerMainWindowController.drawLobbies(true);
							ServerMainWindowController.getSearchButton().fire();
							System.out.println(mJL.getLobby().getLobbyName());
							break;
						case MessageUpdateLobby:
							MessageUpdateLobby messageUpdateLobby = (MessageUpdateLobby) message;
							// update all lobbies in Lobby
							lobbies.replace(messageUpdateLobby.getLobby().getLobbyName(),
									messageUpdateLobby.getLobby());
							// update Pane before joining a Lobby
							ServerMainWindowController.lobbyGUIList.replace(
									messageUpdateLobby.getLobby().getLobbyName(),
									new LobbyGUI(messageUpdateLobby.getLobby()));
							// draws in the lobby
							for (Player player : messageUpdateLobby.getLobby().getPlayersJoined()) {
								if (profile.getId() == player.getID()) {
									ServerMainWindowController
											.drawLobbyMenu(lobbies.get(messageUpdateLobby.getLobby().getLobbyName()));
								}
							}
							// remove lobbies in Server (Pane) if empty
							if (messageUpdateLobby.getLobby().getHumanPlayerList().isEmpty()) {
								ServerMainWindowController.lobbyGUIList
										.remove(messageUpdateLobby.getLobby().getLobbyName());
								lobbies.remove(messageUpdateLobby.getLobby().getLobbyName());
							}
							// draws Server pane
							ServerMainWindowController.drawLobbies(true);
							ServerMainWindowController.getSearchButton().fire();

							break;
						// Its a Message that sends all lobbies to everyone
						case MessageUpdateLobbyList:

							for (Map.Entry<String, Lobby> entry : ((MessageUpdateLobbyList) message).getLobbyList()
									.entrySet()) {
								String key = entry.getKey();
								Lobby lobby = entry.getValue();
								lobbies.putIfAbsent(key, lobby);

							}

							lobbies.forEach((key, value) -> {
								if (!ServerMainWindowController.lobbyGUIList.containsKey(key)) {
									ServerMainWindowController.lobbyGUIList.put(key, new LobbyGUI(value));
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
								if (profile.getId() == player.getID()) {
									gameHandler = new GameHandler(lobbyWithAvatars);
									gameHandler.initMultiplayer(returnClient());
									clientsLobby = lobbyWithAvatars;
									ServerMainWindowController.startMultyplayerGame(lobbyWithAvatars);
								}
							}

							lobbies.remove(lobbyWithAvatars.getLobbyName());
							ServerMainWindowController.drawLobbies(true);

							break;
						case MessageGUIRollInitalDice:
							
							gameHandler.setGameState(((MessageGUIRollInitalDice) message).getGameState());
							gamePane.rollInitialDice(((MessageGUIRollInitalDice) message).getId(),
									((MessageGUIRollInitalDice) message).getValue());
							
							break;
						case MessageGUIRollDiceBattle:
							
							MessageGUIRollDiceBattle mes = ((MessageGUIRollDiceBattle) message);
							gameHandler.setGameState(((MessageGUIRollDiceBattle) message).getGameState());
							gamePane.rollDiceBattle(mes.getAttackerDiceValues(), mes.getDefenderDiceValues(), 
									mes.getTroopsInAttackAt(), mes.getTroopsInAttackDf(), mes.getNumberOfDice());
							break;
						case MessageGUIsetPeriod:
							MessageGUIsetPeriod mesP = ((MessageGUIsetPeriod) message);
							gameHandler.setGameState(((MessageGUIsetPeriod) message).getGameState());
							gamePane.setPeriod(mesP.getPeriod());
							break;
						case MessageGUIsetPhase:
							MessageGUIsetPhase  mesPh = ((MessageGUIsetPhase)message);
							gameHandler.setGameState(((MessageGUIsetPhase) message).getGameState());
							gamePane.setPhase(mesPh.getPhase());
							break;
						case MessageGUIpossessCountry:
							MessageGUIpossessCountry  mesCo =  ((MessageGUIpossessCountry)message);
							gameHandler.setGameState(((MessageGUIpossessCountry) message).getGameState());
							gamePane.claimCountry(mesCo.getCountry(), mesCo.getId());
							gamePane
							.setAmountOfTroopsLeftToDeploy(((MessageGUIpossessCountry)message).getTroopsLeft());
							break;
						case MessageGUIconquerCountry:
							gameHandler.setGameState(((MessageGUIconquerCountry) message).getGameState());
							gamePane
							.conquerCountry(((MessageGUIconquerCountry)message).getCountry(), ((MessageGUIconquerCountry)message).getId(),
									((MessageGUIconquerCountry)message).getTroops());
							
							break;
						case MessageGUIsetCurrentPlayer:
							gameHandler.setGameState(((MessageGUIsetCurrentPlayer) message).getGameState());
							gamePane.setCurrentPlayer(((MessageGUIsetCurrentPlayer)message).getId());
							gamePane.setAmountOfTroopsLeftToDeploy(((MessageGUIsetCurrentPlayer)message).getTroopsLeft());
						
							break;
						case MessageGUIsetTroopsOnTerritory:
							gameHandler.setGameState(((MessageGUIsetTroopsOnTerritory) message).getGameState());
							gamePane.setNumTroops(((MessageGUIsetTroopsOnTerritory)message)
									.getCountryName(), ((MessageGUIsetTroopsOnTerritory)message)
									.getNumTroopsOfCountry());
							break;
						case MessageGUIsetTroopsOnTerritoryAndLeft:
							gameHandler.setGameState(((MessageGUIsetTroopsOnTerritoryAndLeft) message).getGameState());
							gamePane.setNumTroops(((MessageGUIsetTroopsOnTerritoryAndLeft)message).getCountryName(),
									((MessageGUIsetTroopsOnTerritoryAndLeft)message).getNumTroopsOfCountry());
							gamePane.setAmountOfTroopsLeftToDeploy(((MessageGUIsetTroopsOnTerritoryAndLeft)message)
									.getNumTroopsOfPlayer());
							break;
							
						case MessageGUImoveTroopsFromTerritoryToOther:
							gameHandler.setGameState(((MessageGUImoveTroopsFromTerritoryToOther) message).getGameState());
							gamePane.setNumTroops(((MessageGUImoveTroopsFromTerritoryToOther)message).getFrom(), 
									((MessageGUImoveTroopsFromTerritoryToOther)message).getNumberFrom());		
							gamePane.setNumTroops(
									((MessageGUImoveTroopsFromTerritoryToOther)message).getTo(), 
									((MessageGUImoveTroopsFromTerritoryToOther)message).getNumberTo()
									);
							break;
							
						case MessageGUIOpenBattleFrame:
							gameHandler.setGameState(((MessageGUIOpenBattleFrame) message).getGameState());
							gamePane.openBattleFrame(((MessageGUIOpenBattleFrame)message).getBattle());
							break;
						case MessageGUIendBattle:
							gameHandler.setGameState(((MessageGUIendBattle) message).getGameState());
							gamePane.closeBattleFrame();

							break;
						case MessageGUIupdateRanks:
							gameHandler.setGameState(((MessageGUIupdateRanks) message).getGameState());
							gamePane.setPlayersRanking(((MessageGUIupdateRanks)message).getRanks());

							break;
						case MessageGUIgameIsOver:
							gameHandler.setGameState(((MessageGUIgameIsOver) message).getGameState());
							gamePane.endGame(((MessageGUIgameIsOver)message).getPodium());

							break;

						default:
							break;
						}
					} catch (Exception e) {
						closeEverything(socket, inputStream, outputStream);
						e.printStackTrace();
						break;
					}
				}
			}

		});
		clientThread.start();
	}

	/**
	 * main for explicit testing public static void main(String[] args) { Scanner sc
	 * = new Scanner(System.in); System.out.println(" Enter your user name for the
	 * group chat "); String username = sc.nextLine(); Profile profile = null;
	 * Socket socket; try { socket = new Socket("localhost", 1234); Client client =
	 * new Client(socket, profile); client.listenForMessage();
	 * client.sendMessageViaConsole(); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 */
	public Client returnClient() {

		return this;
	}

	public Profile getProfile() {
		return profile;
	}

	public Lobby getClientsLobby() {
		return clientsLobby;
	}

	public boolean isHost() {
		return host;
	}

	public void setHost(boolean host) {
		this.host = host;
	}

	public HashMap<String, Lobby> getLobbies() {
		return lobbies;
	}

	public void setLobbies(HashMap<String, Lobby> lobbies) {
		this.lobbies = lobbies;
	}

	private void updateInLobbyVisual(Message message) {
	}

	public boolean isInALobby() {
		return isInALobby;
	}

	public void setInALobby(boolean isInALobby) {
		this.isInALobby = isInALobby;
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public GamePaneController getGamePane() {
		return gamePane;
	}

	public void setGamePane(GamePaneController gamePane) {
		this.gamePane = gamePane;
	}

	public void playerThrowsInitalDice(int iD) {
		this.gameHandler.playerThrowsInitialDice(iD);
	}

	public void clickCountry(int id, CountryName country) {
		this.gameHandler.clickCountry(id, country);
	}

	public void cancelNumberOfTroops(CountryName country, ChoosePane choosePane, int idOfPlayer) {
		this.gameHandler.cancelNumberOfTroops(country, choosePane, idOfPlayer);
	}

	public void confirmNumberOfTroops(CountryName country, int troops, ChoosePane choosePane, int idOfPlayer) {
		this.gameHandler.confirmTroopsToCountry(country, troops, choosePane, idOfPlayer);
	}

	public void turnInRiskCards(ArrayList<String> cards, int idOfPlayer) {
		this.gameHandler.turnInRiskCards(cards, idOfPlayer);
	}

	public void endPhaseTurn(Period period, Phase phase, int idOfPlayer) {
		this.gameHandler.endPhaseTurn(period, phase, idOfPlayer);
	}

	public void battleDiceThrow() {
		this.gameHandler.battleDiceThrow();

	}

	// ot tuk
	public void rollInitialDiceOnGUI(int idOfPlayer, int i) {
		sendMessage(new MessageGUIRollInitalDice(gameHandler.getGameState(), idOfPlayer, i, clientsLobby));
		this.gamePane.rollInitialDice(idOfPlayer, i);
	}

	public void rollDiceBattleOnGUI(int[] attackerDiceValues, int[] defenderDiceValues, int troopsInAttackAt,
			int troopsInAttackDf, int[] numberOfDice) throws FileNotFoundException {

		sendMessage(new MessageGUIRollDiceBattle(gameHandler.getGameState(), attackerDiceValues, defenderDiceValues,
				troopsInAttackAt, troopsInAttackDf, numberOfDice, clientsLobby));
		this.gamePane.rollDiceBattle(attackerDiceValues, defenderDiceValues, troopsInAttackAt, troopsInAttackDf,
				numberOfDice);
	}

	public void showExeceptionOnGUI(Exception e) {
		this.gamePane.showException(e.toString());
	}

	public void setPeriodOnGUI(Period period) {
		sendMessage(new MessageGUIsetPeriod(gameHandler.getGameState(), period, clientsLobby));
		this.gamePane.setPeriod(period);
	}

	public void setPhaseOnGUI(Phase phase) {
		sendMessage(new MessageGUIsetPhase(gameHandler.getGameState(), phase, clientsLobby));
		this.gamePane.setPhase(phase);
	}

	public void possesCountryOnGUI(CountryName country, int id, int troopsLeft) {
		sendMessage(new MessageGUIpossessCountry(gameHandler.getGameState(), country, id, troopsLeft, clientsLobby));
		this.gamePane.claimCountry(country, id);
		this.gamePane.setAmountOfTroopsLeftToDeploy(troopsLeft);
	}

	public void conquerCountryOnGUI(CountryName country, int id, int troops) {
		sendMessage(new MessageGUIconquerCountry(gameHandler.getGameState(), country, id, troops, clientsLobby));
		this.gamePane.conquerCountry(country, id, troops);
	}

	public void setCurrentPlayerOnGUI(int id, int troopsLeft) {
		sendMessage(new MessageGUIsetCurrentPlayer(gameHandler.getGameState(), id, troopsLeft, clientsLobby));
		this.gamePane.setCurrentPlayer(id);
		this.gamePane.setAmountOfTroopsLeftToDeploy(troopsLeft);
	}

	public void chnagePlayerOnGUI(int id, ArrayList<Card> cards) {
		this.gamePane.setPlayerOnGUI(id, cards);
	}

	public void chooseNumberOfTroopsOnGUI(CountryName country, int min, int max, ChoosePane choosePane) {
		System.out.println("Opening choose troops with " + country.toString() + " " + choosePane.toString());
		this.gamePane.showChoosingTroopsPane(country, min, max, choosePane);
	}

	public void closeTroopsPaneOnGUI() {
		this.gamePane.closeChoosingTroopsPane();
	}

	public void setTroopsOnTerritory(CountryName countryName, int numTroopsOfCountry) {
		sendMessage(new MessageGUIsetTroopsOnTerritory(gameHandler.getGameState(), countryName, numTroopsOfCountry,
				clientsLobby));
		this.gamePane.setNumTroops(countryName, numTroopsOfCountry);
	}

	public void setTroopsOnTerritoryAndLeftOnGUI(CountryName countryName, int numTroopsOfCountry,
			int numTroopsOfPlayer) {
		sendMessage(new MessageGUIsetTroopsOnTerritoryAndLeft(gameHandler.getGameState(), countryName,
				numTroopsOfCountry, numTroopsOfPlayer, clientsLobby));
		this.gamePane.setNumTroops(countryName, numTroopsOfCountry);
		this.gamePane.setAmountOfTroopsLeftToDeploy(numTroopsOfPlayer);
	}

	public void moveTroopsFromTerritoryToOtherOnGUI(CountryName from, CountryName to, int numberFrom, int numberTo) {
		sendMessage(new MessageGUImoveTroopsFromTerritoryToOther(gameHandler.getGameState(), from, to, numberFrom,
				numberTo, clientsLobby));
		this.gamePane.setNumTroops(from, numberFrom);
		this.gamePane.setNumTroops(to, numberTo);
	}

	public void openBattleFrameOnGUI(Battle battle) {
		sendMessage(new MessageGUIOpenBattleFrame(gameHandler.getGameState(), battle, clientsLobby));
		this.gamePane.openBattleFrame(battle);
	}

	public void endBattleOnGUI() {
		sendMessage(new MessageGUIendBattle(gameHandler.getGameState(), clientsLobby));
		this.gamePane.closeBattleFrame();
	}

	public void riskCardsTurnedInSuccessOnGUI(ArrayList<Card> card, int idOfPlayer, int bonusTroops) {
		this.gamePane.setAmountOfTroopsLeftToDeploy(bonusTroops);
		this.gamePane.setPlayerOnGUI(idOfPlayer, card);
		for (Card c : card) {
			System.out.println(c.toString());
		}
	}

//	public void selectTerritoryAndSetDisabledTerritoriesOnGUI(CountryName countryName, 
//			ArrayList<CountryName> unreachableCountries) {
//		this.gamePane.pointUpCountry(countryName);
//		for(CountryName country : unreachableCountries) {
//			this.gamePane.deactivateCountry(country);
//		}
//		// for attack and fortify phase when player clicks on the "from" territory
//	}

//	public void resetAllOnGUI() {
//	}

	public void updateRanksOnGUI(int[] ranks) {
		sendMessage(new MessageGUIupdateRanks(gameHandler.getGameState(), ranks, clientsLobby));
		this.gamePane.setPlayersRanking(ranks);
	}

	public void gameIsOverOnGUI(ArrayList<Player> podium) {
		sendMessage(new MessageGUIgameIsOver(gameHandler.getGameState(), podium, clientsLobby));
		this.gamePane.endGame(podium);
	}

}
