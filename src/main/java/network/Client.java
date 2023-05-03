package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import database.Profile;
import game.Lobby;
import game.gui.GUISupportClasses;
import game.gui.GameSound;
import game.gui.LobbyGUI;
import game.gui.ServerMainWindowController;
import game.models.Player;
import general.AppController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageCreateLobby;
import network.messages.MessageDisconnect;

import network.messages.MessageJoinLobby;
import network.messages.MessageProfile;
import network.messages.MessageSend;
import network.messages.MessageServerCloseConnection;
import network.messages.MessageToPerson;

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

	public Client(Socket socket, Profile profile) {
		this.profile = profile;
		this.userName = profile.getUserName();
		this.socket = socket;
		try {

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

	public void sendMessageViaConsole() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Scanner scanner = new Scanner(System.in);
					while (socket.isConnected()) {
						outputStream.writeObject(new MessageSend(userName + ": " + scanner.nextLine()));
						outputStream.flush();
					}

				} catch (Exception e) {
					closeEverything(socket, inputStream, outputStream);
				}
			}
		}).start();
	}

	public void sendMessage(String message) {
		try {
			outputStream.writeObject(new MessageSend(userName + ": " + message));
			outputStream.flush();
		} catch (IOException e) {
			closeEverything(socket, inputStream, outputStream);
			e.printStackTrace();
		}
	}

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
				while (socket.isConnected()) {
					try {
						message = (Message) inputStream.readObject();
						switch (message.getMessageType()) {
						case MessageSend:

							System.out.println("case MessageSend in Clinet Success 0 ");
							System.out.println(((MessageSend) message).getMessage());
							chat.addLabel(((MessageSend) message).getMessage());
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

//							JoinClientMessengerController
//									.addLabel(((MessageServerCloseConnection) message).getMessage(), vBoxMessages);	
//							JoinClientMessengerController.addLabel("Host has disconnected, please reconnect",
//									vBoxMessages);
							closeEverything(socket, inputStream, outputStream);
							Server.closeServerSocket();
							break;
						case MessageToPerson:
							System.out.println("case 4 in Client");

							chat.addLabel(((MessageToPerson) message).getStringMessage(),
									((MessageToPerson) message).getFromProfile().getUserName());
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
							System.out.println("CLIENT " + profile.getUserName());

							MessageCreateLobby mCL = (MessageCreateLobby) message;
							
							LobbyGUI lobbyGUI = new LobbyGUI(mCL.getLobby());
							
							lobbyGUI.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									(new GameSound()).buttonClickBackwardSound();
									ServerMainWindowController.selectedLobby = lobbyGUI.getLobby();
									for (LobbyGUI lobbyEnt : ServerMainWindowController.lobbyGUIList.values()) {
										lobbyEnt.setSelected(false);
									}

									lobbyGUI.setSelected(true);

								}
							});
							
							ServerMainWindowController.lobbyGUIList.put(mCL.getLobby().getLobbyName(), lobbyGUI);
							ServerMainWindowController.drawLobbies();
							lobbies.put(mCL.getLobby().getLobbyName(), mCL.getLobby());
							break;
						case MessageJoinLobby:
							MessageJoinLobby mJL = (MessageJoinLobby) message;
							lobbies.replace(mJL.getLobby().getLobbyName(),mJL.getLobby());
							
							// update
							ServerMainWindowController.drawLobbyMenu(lobbies.get(mJL.getLobby().getLobbyName()));
							
							System.out.println(mJL.getLobby().getLobbyName());
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

}
