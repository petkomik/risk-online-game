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
import javafx.application.Platform;
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

/*
 * @author dignatov
 * 
 */
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
			System.out.println(profile.getWins() + " CH 66");
			broadcastMessage(new MessageConnect(profile));
		} catch (IOException | ClassNotFoundException e) {
			MessageDisconnect disconnect = new MessageDisconnect(profile);
			broadcastMessage(disconnect);
			closeEverything(socket, objectInputStream, objectOutputStream);
			e.printStackTrace();
		}

	}

	/**
	 * Broadcasts a message to all connected client handlers, except the current
	 * one.
	 *
	 * @param message The message to be broadcasted
	 */

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

	/**
	 * Broadcasts a message to all connected client handlers, except the current one
	 * and the one specified by the profile ID in the message.
	 *
	 * @param message The message to be broadcasted
	 */

	public void broadcastMessageDisconnectWithoutProfile(Message message) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
				if (clientHandler.getProfile().getId() != ((MessageDisconnect) message).getProfile().getId()) {
					clientHandler.objectOutputStream.writeObject(message);
					clientHandler.objectOutputStream.flush();
				}
			} catch (IOException e) {
				closeEverything(socket, objectInputStream, objectOutputStream);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Broadcasts a message to all client handlers within a specific lobby.
	 *
	 * @param message The message to be broadcasted
	 * @param lobby   The lobby to broadcast the message to
	 */

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

	/**
	 * Broadcasts a message to all client handlers within a specific lobby, except
	 * the current one checked by the Id.
	 *
	 * @param message The message to be broadcasted
	 * @param lobby   The lobby
	 */
	public void broadcastMessageWithinLobbyWithoutMeId(Message message, Lobby lobby) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
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

	/**
	 * 
	 * Broadcasts a message to all connected client handlers, including the current
	 * one.
	 * 
	 * @param message The message to be broadcasted
	 */

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

	/**
	 * Sends a personal text message to the specified client.
	 * 
	 * @param message The message to be sent
	 */
	public void personalTextMessage(Message message) {

		for (ClientHandler clientHandler : clientHandlers) {
			try {

				if (clientHandler.clientUsername
						.equalsIgnoreCase(((MessageToPerson) message).getToProfile().getUserName())) {

					clientHandler.objectOutputStream.writeObject((MessageToPerson) message);
					clientHandler.objectOutputStream.flush();

				}
			} catch (IOException e) {
				closeEverything(socket, objectInputStream, objectOutputStream);
				e.printStackTrace();

			}
		}

	}

	/**
	 * 
	 * Sends a personal message to the client with the specified player ID.
	 *
	 * @param playerId The ID of the recipient player
	 * @param message  The message to be sent
	 */
	public void personalMessage(int playerId, Message message) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {

				if (clientHandler.getProfile().getId() == playerId) {

					clientHandler.objectOutputStream.writeObject(message);
					clientHandler.objectOutputStream.flush();

				}
			} catch (IOException e) {
				closeEverything(socket, objectInputStream, objectOutputStream);
				e.printStackTrace();

			}
		}

	}

	/**
	 *
	 * Sends a personal message to the client with the specified player ID.
	 *
	 * @param playerId The ID of the recipient player
	 * @param message  The message to be sent
	 */
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
					messageFromClient = (Message) objectInputStream.readObject();
				}
				switch (messageFromClient.getMessageType()) {
				case MessageSend:
					broadcastMessage(messageFromClient);
					break;

				case MessageSendInGame:
					broadcastMessageWithinLobbyWithoutMeId(messageFromClient,
							((MessageSendInGame) messageFromClient).getLobby());
					break;

				case Connect:
					personalMessage(((MessageConnect) messageFromClient).getIdTo(),
							new MessageProfile(((MessageConnect) messageFromClient).getProfile()));
					break;

				case Disconnect:
					broadcastMessageDisconnectWithoutProfile(messageFromClient);
					break;

				case MessageServerCloseConnection:
					broadcastMessageToAllIncludingMe(messageFromClient);
					break;
				case MessageToPerson:
					personalTextMessage((MessageToPerson) messageFromClient);
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
					broadcastMessageWithinLobby(messageFromClient, ((MessageGUIsetPhase) messageFromClient).getLobby());
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
					break;
				}

			} catch (Exception e) {
				closeEverything(socket, objectInputStream, objectOutputStream);
				break;
			}
		}
	}

	/**
	 *
	 * Closes the socket, input stream, and output stream associated with this
	 * client handler.
	 */
	public void closeEverything() {
		closeEverything(socket, objectInputStream, objectOutputStream);

	}

	/**
	 * 
	 * Closes the specified socket, input stream, and output stream.
	 * 
	 * @param socket             The socket to be closed
	 * @param objectInputStream  The input stream to be closed
	 * @param objectOutputStream The output stream to be closed
	 */
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
			clieantHandlerThread.interrupt();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * Removes the specified profile from the list of connected clients and client
	 * handlers.
	 * 
	 * @param profile The profile to be removed
	 */
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
