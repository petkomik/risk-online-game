package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import database.Profile;
import game.gui.HostServerMessengerController;
import game.gui.JoinClientMessengerController;
import game.models.Player;
import game.models.PlayerMP;
import general.AppController;
import javafx.scene.layout.VBox;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageDisconnect;
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
	public static ArrayList<Profile> profiles = new ArrayList<>();

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

	public void listenForMessage(VBox vBoxMessages) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message;
				while (socket.isConnected()) {
					try {
						message = (Message) inputStream.readObject();
						switch (message.getMessageType()) {
						case MessageSend:
							System.out.println("case MessageSend in Clinet Success 0 ");
							HostServerMessengerController.addLabel(((MessageSend) message).getMessage(), vBoxMessages);
							// System.out.println(((MessageSend) message).getMessage());

							break;
						case Connect:
							System.out.println("case MessageConnect Success 1 ");
							Profile profilee = ((MessageConnect) message).getProfile();
							if (AppController.dbH.getProfileByID(profilee.getId()) == null) {
								AppController.dbH.createProfileData(profilee);
							}
							if (!profiles.contains(profilee)) {
								profiles.add(profilee);
							}
							sendMessage(new MessageProfile(profile));
							HostServerMessengerController.addLabel(
									"Player " + ((MessageConnect) message).getPlayername() + " has been connected",
									vBoxMessages);
							// Imporatant for different pcs communication
							// sendMessage(new MessageConnect(((MessageConnect)message).getProfile()));
							break;
						case Disconnect:
							System.out.println("case MessageConnect Success 2 ");
							HostServerMessengerController.addLabel(
									"Player " + ((MessageDisconnect) message).getPlayername() + " has disconnected",
									vBoxMessages);
							closeEverything();
							break;
						case MessageServerCloseConnection:
							System.out.println("case MessageServerDisconnect in Clients Server Success 3 ");
//							JoinClientMessengerController
//									.addLabel(((MessageServerCloseConnection) message).getMessage(), vBoxMessages);	
							JoinClientMessengerController.addLabel("Host has disconnected, please reconnect",
									vBoxMessages);
							closeEverything(socket, inputStream, outputStream);
							Server.closeServerSocket();
							break;
						case MessageToPerson:
							System.out.println("case 4 in Handler");
							JoinClientMessengerController.addLabel(
									((MessageToPerson) message).getTo() + ": "
											+ ((MessageToPerson) message).getMsg()
													.substring(((MessageToPerson) message).getMsg().indexOf(':')),
									vBoxMessages);
							break;
						case MessageProfile:
							if (!profiles.contains(((MessageProfile) message).getProfile())) {
								profiles.add(((MessageProfile) message).getProfile());
							}
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

		}).start();
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

}
