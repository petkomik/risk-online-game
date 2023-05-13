package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import database.Profile;
import game.models.Lobby;
import game.models.Player;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageDisconnect;
import network.messages.MessageGUIOpenBattleFrame;
import network.messages.MessageGUIRollDiceBattle;
import network.messages.MessageGUIRollInitalDice;
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
import network.messages.MessageSendInGame;
import network.messages.MessageToPerson;
import network.messages.MessageUpdateLobby;

public class ClientHandler implements Runnable {

  public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  public static ArrayList<Profile> clients = new ArrayList<>();
  private HashMap<String, Lobby> lobbies = new HashMap<>();
  private Socket socket;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private Profile profile;
  private String clientUsername;
  private Thread clieantHandlerThread;
  private Lobby lobby;

  public ClientHandler(Socket socket) {
    this.socket = socket;

    try {
      this.objectInputStream = new ObjectInputStream(socket.getInputStream());
      this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      Message clientIdentifierMessage = ((Message) objectInputStream.readObject());
      this.profile = ((MessageProfile) clientIdentifierMessage).getProfile();
      this.clientUsername = profile.getUserName();
      clientHandlers.add(this);
      clients.add(profile);
      broadcastMessage(new MessageConnect(profile));
    } catch (IOException | ClassNotFoundException e) {
      MessageDisconnect disconnect = new MessageDisconnect(profile);
      broadcastMessage(disconnect);
      closeEverything(socket, objectInputStream, objectOutputStream);
      e.printStackTrace();
    }

  }

  public void broadcastMessage(Message message) {
    for (ClientHandler clientHandler : clientHandlers) {
      try {
        if (!clientHandler.clientUsername.equals(clientUsername)) {
          clientHandler.objectOutputStream.writeObject(message);
          clientHandler.objectOutputStream.flush();
        }
      } catch (IOException e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        e.printStackTrace();
      }
    }
  }

  public void broadcastMessageWithinLobby(Message message, Lobby lobby) {
    for (ClientHandler clientHandler : clientHandlers) {
      try {
        // &&this.getProfile().getId() != player.getID()
        for (Player player : lobby.getHumanPlayerList()) {
          if (clientHandler.getProfile().getId() == player.getId()) {
            clientHandler.objectOutputStream.writeObject(message);
            clientHandler.objectOutputStream.flush();
          }
        }
      } catch (IOException e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        e.printStackTrace();
      }
    }
  }

  public void broadcastMessageWithinLobbyWithoutMeId(Message message, Lobby lobby) {
    for (ClientHandler clientHandler : clientHandlers) {
      try {
        // &&this.getProfile().getId() != player.getID()
        for (Player player : lobby.getHumanPlayerList()) {
          if (clientHandler.getProfile().getId() == player.getId()
              && this.profile.getId() != player.getId()) {
            clientHandler.objectOutputStream.writeObject(message);
            clientHandler.objectOutputStream.flush();
          }
        }
      } catch (IOException e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        e.printStackTrace();
      }
    }
  }

  public void broadcastMessageWithinLobbyWithoutMe(Message message, Lobby lobby) {
    for (ClientHandler clientHandler : clientHandlers) {
      try {
        for (Player player : lobby.getHumanPlayerList()) {
          if (clientHandler.getProfile().getId() == player.getId()
              && !clientHandler.clientUsername.equals(clientUsername)) {
            clientHandler.objectOutputStream.writeObject(message);
            clientHandler.objectOutputStream.flush();
          }
        }
      } catch (IOException e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        e.printStackTrace();
      }
    }
  }

  public void broadcastMessageToAllIncludingMe(Message message) {
    for (ClientHandler clientHandler : clientHandlers) {
      try {

        clientHandler.objectOutputStream.writeObject(message);
        clientHandler.objectOutputStream.flush();

      } catch (IOException e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        e.printStackTrace();
      }
    }
  }

  public Profile getProfile() {
    return profile;
  }

  public String getClientUsername() {
    return clientUsername;
  }

  public void personalTextMessage(Message message) {
    System.out.println("messanger works");
    for (ClientHandler clientHandler : clientHandlers) {
      try {

        if (clientHandler.clientUsername
            .equalsIgnoreCase(((MessageToPerson) message).getToProfile().getUserName())) {
          System.out.println("that is what TO is: ");

          clientHandler.objectOutputStream.writeObject((MessageToPerson) message);
          clientHandler.objectOutputStream.flush();

        }
      } catch (IOException e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        e.printStackTrace();

      }
    }

  }

  public void personalMessage(int playerId, Message message) {
    System.out.println("messanger works");
    for (ClientHandler clientHandler : clientHandlers) {
      try {

        if (clientHandler.getProfile().getId() == playerId) {
          System.out.println("that is what TO is: ");

          clientHandler.objectOutputStream.writeObject(message);
          clientHandler.objectOutputStream.flush();

        }
      } catch (IOException e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        e.printStackTrace();

      }
    }

  }
  // methode for one to one player

  public void removeClientHandler() {

    clientHandlers.remove(this);

  }

  @Override
  public void run() {
    Message messageFromClient;

    BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

    while (socket.isConnected()) {
      try {
        if (!messageQueue.isEmpty()) {
          messageFromClient = messageQueue.take();
        } else {
          // Otherwise, wait for a message from the client
          messageFromClient = (Message) objectInputStream.readObject();
        }
        switch (messageFromClient.getMessageType()) {
          case MessageSend:
            System.out.println("case MessageSend in Handler Success 0");
            broadcastMessage(messageFromClient);
            break;
          case MessageSendInGame:
            broadcastMessageWithinLobbyWithoutMe(messageFromClient,
                ((MessageSendInGame) messageFromClient).getLobby());
            break;
          case Connect:
            // all clients send their profile to the new Client
            System.out.println("MessageConnect on Handler works)");
            // personal message with (iDTO , Profile of the sender with MessageProfile )
            personalMessage(((MessageConnect) messageFromClient).getIdTo(),
                new MessageProfile(((MessageConnect) messageFromClient).getProfile()));
            // change connect to case
            break;
          case Disconnect:

            System.out.println("case MessageDisconnect in Handler Success 3 ");
            broadcastMessage(messageFromClient);
            // try {
            // Thread.sleep(1000); // wait for the broadcast to finish
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            // removeClient(((MessageDisconnect) messageFromClient).getProfile());
            // closeEverything();
            break;

          case MessageServerCloseConnection:
            System.out.println("case MessageDisconnect Server Success 3 ");
            broadcastMessage(messageFromClient);
            closeEverything(socket, objectInputStream, objectOutputStream);
            Server.closeServerSocket();
            // JoinClientMessengerController
            // .addLabel(((MessageServerCloseConnection) message).getMessage(), vBoxMessages);
            break;
          case MessageToPerson:
            System.out.println("case 4 in Handler");
            personalTextMessage((MessageToPerson) messageFromClient);
            // theoretisch ein Thread
            break;
          case MessageProfile:
            broadcastMessage(new MessageProfile(((MessageProfile) messageFromClient).getProfile()));
            break;

          case MessageCreateLobby:
            broadcastMessageToAllIncludingMe(messageFromClient);
            break;

          case MessageJoinLobby:
            broadcastMessageToAllIncludingMe(((MessageJoinLobby) messageFromClient));
            break;
          case MessageUpdateLobby:

            broadcastMessageToAllIncludingMe(((MessageUpdateLobby) messageFromClient));
            break;
          case MessageUpdateLobbyList:

            broadcastMessageToAllIncludingMe(messageFromClient);
            break;
          case MessageReadyToPlay:
            broadcastMessageToAllIncludingMe(messageFromClient);
            break;
          case MessageGUIRollInitalDice:
            // broadcastMessageToAllIncludingMe(messageFromClient);
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIRollInitalDice) messageFromClient).getLobby());
            break;
          case MessageGUIRollDiceBattle:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIRollDiceBattle) messageFromClient).getLobby());
            break;
          case MessageGUIsetPeriod:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIsetPeriod) messageFromClient).getLobby());
            break;
          case MessageGUIsetPhase:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIsetPhase) messageFromClient).getLobby());
            break;
          case MessageGUIpossessCountry:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIpossessCountry) messageFromClient).getLobby());
            break;
          case MessageGUIconquerCountry:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIconquerCountry) messageFromClient).getLobby());

            break;
          case MessageGUIsetCurrentPlayer:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIsetCurrentPlayer) messageFromClient).getLobby());
            break;
          case MessageGUIsetTroopsOnTerritory:

            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIsetTroopsOnTerritory) messageFromClient).getLobby());
            break;
          case MessageGUImoveTroopsFromTerritoryToOther:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUImoveTroopsFromTerritoryToOther) messageFromClient).getLobby());
            break;
          case MessageGUIsetTroopsOnTerritoryAndLeft:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIsetTroopsOnTerritoryAndLeft) messageFromClient).getLobby());
            break;
          case MessageGUIOpenBattleFrame:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIOpenBattleFrame) messageFromClient).getLobby());
            break;
          case MessageGUIendBattle:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIendBattle) messageFromClient).getLobby());
            break;
          case MessageGUIupdateRanks:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIupdateRanks) messageFromClient).getLobby());
            break;
          case MessageGUIgameIsOver:
            broadcastMessageWithinLobby(messageFromClient,
                ((MessageGUIgameIsOver) messageFromClient).getLobby());
            break;
          default:
            // Handle unknown message types, if necessary
            break;
        }

      } catch (Exception e) {
        closeEverything(socket, objectInputStream, objectOutputStream);
        break;
      }
    }
  }

  public void closeEverything() {
    closeEverything(socket, objectInputStream, objectOutputStream);

  }

  public void closeEverything(Socket socket2, ObjectInputStream objectInputStream2,
      ObjectOutputStream objectOutputStream2) {
    removeClientHandler();
    try {
      if (socket2 != null) {
        socket2.close();
      }
      if (objectOutputStream2 != null) {
        objectOutputStream2.close();
      }
      if (objectInputStream2 != null) {
        objectInputStream2.close();
      }
      System.out.println("Pepi pita stiga li do tam");
      clieantHandlerThread.interrupt();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void removeClient(Profile profile) {

    for (ClientHandler clientHandler : clientHandlers) {

      if (clientHandler.getProfile().equals(profile)) {
        clientHandlers.remove(clientHandler);
        clieantHandlerThread.interrupt();

      }
    }
    for (Profile profile2 : clients) {
      if (profile2.equals(profile)) {
        clients.remove(profile2);

      }
    }

  }

  public Thread getClieantHandlerThread() {
    return clieantHandlerThread;
  }

  public void setClieantHandlerThread(Thread clieantHandlerThread) {
    this.clieantHandlerThread = clieantHandlerThread;
  }

  public Lobby getLobby() {
    return lobby;
  }

  public void setLobby(Lobby lobby) {
    this.lobby = lobby;
  }

}
