package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

import database.Profile;
import game.Lobby;
import game.gui.HostServerMessengerController;
import game.gui.MainApp;
import game.models.Player;
import gameState.GameHandler;
import network.messages.Message;
import network.messages.MessageAttack;
import network.messages.MessageConnect;
import network.messages.MessageCreateLobby;
import network.messages.MessageDiceThrow;
import network.messages.MessageDisconnect;
import network.messages.MessageFortifyTroops;
import network.messages.MessageJoinLobby;
import network.messages.MessagePlaceTroops;
import network.messages.MessagePlayerTurn;
import network.messages.MessagePossessCountry;
import network.messages.MessageProfile;
import network.messages.MessageSend;
import network.messages.MessageToPerson;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	public static ArrayList<Profile> clients = new ArrayList<>();
	private HashMap<String, Lobby> lobbies = new HashMap<>();
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Profile profile;
	private String clientUsername;

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
// methode for one to one  player

	public void removeClientHandler() {

		clientHandlers.remove(this);

	}

	@Override
	public void run() {
		Message messageFromClient;

		while (socket.isConnected()) {
			try {

				messageFromClient = (Message) objectInputStream.readObject();
				switch (messageFromClient.getMessageType()) {
				case MessageSend:
					System.out.println("case MessageSend in Handler Success 0");
					broadcastMessage(messageFromClient);
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
					System.out.println("case MessageDisconnect Server Success 3 ");
					broadcastMessage(messageFromClient);
					removeClient(((MessageDisconnect) messageFromClient).getProfile());

					break;
				case MessageServerCloseConnection:
					System.out.println("case MessageDisconnect Server Success 3 ");
					broadcastMessage(messageFromClient);
					closeEverything(socket, objectInputStream, objectOutputStream);

//					JoinClientMessengerController
//							.addLabel(((MessageServerCloseConnection) message).getMessage(), vBoxMessages);
					break;
				case MessageToPerson:
					System.out.println("case 4 in Handler");
					personalTextMessage((MessageToPerson) messageFromClient);
					// theoretisch ein Thread
					break;
				case MessageProfile:
					broadcastMessage(new MessageProfile(((MessageProfile) messageFromClient).getProfile()));
					break;
				case MessageMove:
					// Handle the message move message
					break;
				case MessagePlayerTurn:
					broadcastMessage(((MessagePlayerTurn) messageFromClient));
					break;
				case MessagePlacingTroops:
					broadcastMessage(((MessagePlaceTroops) messageFromClient));
					break;
				case MessageAttack:
					broadcastMessage(((MessageAttack) messageFromClient));
					break;
				case MessageDiceThrow:
					broadcastMessage(((MessageDiceThrow) messageFromClient));

					break;
				case MessagePossessCountry:
					broadcastMessage(((MessagePossessCountry) messageFromClient));
					break;
				case MessageChooseCountry:
					// Handle the message choose country message
					break;
				case MessagePlaceTroops:
					broadcastMessage(((MessagePlaceTroops) messageFromClient));
					break;
				case MessageDiceThrowRequest:
					// Handle the message dice throw request message
					break;
				case MessageFortifyTroops:
					broadcastMessage(((MessageFortifyTroops) messageFromClient));

					break;
				case MessageCreateLobby:

					Lobby aLobby = new Lobby();
					BiConsumer<String, Lobby> addLobby = (clientUsername, lobby) -> {
						int i = 1;
						String newUsername = clientUsername;
						while (lobbies.containsKey(newUsername)) {
							newUsername = clientUsername + i;
							i++;
						}
						lobbies.put(newUsername, lobby);
						lobby.setLobbyName(newUsername);
					};
					addLobby.accept(clientUsername, aLobby);
					broadcastMessage(new MessageCreateLobby(this.getProfile().getId(), aLobby));

					break;

				case MessageJoinLobby:

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

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void removeClient(Profile profile) {
		for (ClientHandler clientHandler : clientHandlers) {
			if (clientHandler.getProfile().equals(profile)) {
				clientHandler.removeClient(profile);
			}
		}

	}

}
