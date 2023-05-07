package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Platform;

import database.Profile;
import game.Lobby;
import game.gui.GUISupportClasses;
import game.gui.LobbyGUI;
import game.gui.ServerMainWindowController;
import game.models.Player;
import general.AppController;
import general.GameSound;
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
							
							
							String textMessage =((MessageSend) message).getMessage();
							Profile profileFrom =((MessageSend) message).getProfileFrom();
							boolean isInLobby = ((MessageSend) message).isForLobby();
							// Assume that each Lobby object has a List<HumanPlayer> called humanPlayerList, and that this.profile refers to the profile of the program
							boolean isSenderInSameLobby = lobbies.values().stream()
							    .anyMatch(lobby -> lobby.getHumanPlayerList().stream()
							        .anyMatch(player -> player.getID() == profileFrom.getId()) && lobby.getHumanPlayerList().stream()
							        .anyMatch(player -> player.getID() == profile.getId()));

						

							if(isInLobby){
								if(isSenderInSameLobby){
									chat.addLabel(((MessageSend) message).getMessage());
								}
								
							}else{
								if(!isInALobby){
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

							String textMessage1 =((MessageToPerson) message).getStringMessage();
							Profile profileFrom1 =((MessageToPerson) message).getFromProfile();
							boolean isInLobby1 = ((MessageToPerson) message).isInALobby();
							// Assume that each Lobby object has a List<HumanPlayer> called humanPlayerList, and that this.profile refers to the profile of the program
							boolean isSenderInSameLobby1 = lobbies.values().stream()
							    .anyMatch(lobby -> lobby.getHumanPlayerList().stream()
							        .anyMatch(player -> player.getID() == profileFrom1.getId()) && lobby.getHumanPlayerList().stream()
							        .anyMatch(player -> player.getID() == profile.getId()));

							if(isInLobby1){
								if(isSenderInSameLobby1){
									chat.addLabel(((MessageToPerson) message).getStringMessage(),
											((MessageToPerson) message).getFromProfile().getUserName());
								}
								
							}else{
								if(!isInALobby){
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
							ServerMainWindowController.drawLobbies();
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
							ServerMainWindowController.drawLobbies();
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
							ServerMainWindowController.drawLobbies();

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

							ServerMainWindowController.drawLobbies();
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

	private void updateInLobbyVisual(Message message) {
	}
	public boolean isInALobby() {
		return isInALobby;
	}

	public void setInALobby(boolean isInALobby) {
		this.isInALobby = isInALobby;
	}
}
