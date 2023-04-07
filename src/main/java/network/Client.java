package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import game.Profile;
import game.gui.HostServerMessengerController;
import general.AppController;
import javafx.scene.layout.VBox;
import network.messages.Message;
import network.messages.MessageProfile;
import network.messages.MessageSend;

public class Client {
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Profile profile;
	private String userName;

	public Client(Socket socket, Profile profile) {
		this.profile = profile;
		this.userName = profile.getUserName();
		this.socket = socket;
		try {
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream.writeObject(new MessageProfile(profile));
			outputStream.flush();
		} catch (IOException e) {
			closeEverything(socket, inputStream, outputStream);
			e.printStackTrace();
		}

	}

	private void closeEverything(Socket socket2, ObjectInputStream inputStream2, ObjectOutputStream outputStream2) {

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
							HostServerMessengerController.addLabel(((MessageSend) message).getMessage(), vBoxMessages);
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

	public void listenForMessage() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg;
				while (socket.isConnected()) {
					try {
						msg = (Message) inputStream.readObject();
						switch (msg.getMessageType()) {
						case MessageSend:
							System.out.println(((MessageSend) msg).getMessage());
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

	public static Client createClient(String host, int port) throws IOException {
		AppController.getInstance();
		Profile profile = AppController.getProfile();
		Socket socket;
		Client client;
		socket = new Socket(host, port);
		client = new Client(socket, profile);
		return client;
	}

	/** main for explicit testing
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println(" Enter your user name for the group chat ");
		String username = sc.nextLine();
		Profile profile = null;
		Socket socket;
		try {
			socket = new Socket("localhost", 1234);
			Client client = new Client(socket, profile);
			client.listenForMessage();
			client.sendMessageViaConsole();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	*/

}
