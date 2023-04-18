package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import database.Profile;
import game.gui.HostServerMessengerController;
import game.gui.MainApp;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageDisconnect;
import network.messages.MessageProfile;
import network.messages.MessageSend;
import network.messages.MessageToPerson;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
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
			broadcastMessage(new MessageConnect(profile) );
			
		} catch (IOException | ClassNotFoundException e) {
			MessageDisconnect disconnect = new MessageDisconnect(profile);
			broadcastMessage(disconnect);
			System.out.println("case CLientHandler Ecxeptions ");
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
		public void personalMessage(Message message, String to) {
			System.out.println("messanger works");
			for (ClientHandler clientHandler : clientHandlers) {
				try {
					
					if (clientHandler.clientUsername.equalsIgnoreCase(to)) {
						System.out.println("that is what TO is: ");
					
						clientHandler.objectOutputStream.writeObject((MessageToPerson)message);
						clientHandler.objectOutputStream.flush();
						
					}
				}
				 catch (IOException e) {
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
					broadcastMessage(messageFromClient);
					System.out.println("case MessageSend in Handler Success 0");
					break;
				case Connect:
					MessageConnect connectionConfirmed = new MessageConnect(profile);
					broadcastMessage(connectionConfirmed);
					System.out.println("case MessageConnect in Handler Succes 1 ");
					break;
				case Disconnect:
					System.out.println("case MessageDisconnect Server Success 3 ");
					broadcastMessage(messageFromClient);
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
					personalMessage((MessageToPerson)messageFromClient, ((MessageToPerson) messageFromClient).getTo());
					
					break;
				case MessageProfile:
					// Handle the message profile message
					break;
				case MessageMove:
					// Handle the message move message
					break;
				case MessagePlacingTroops:
					// Handle the message placing troops message
					break;
				case MessageAttack:
					// Handle the message attack message
					break;
				case MessageDiceThrow:
					// Handle the message dice throw message
					// Zahlen zusenden von Würfeln 
					break;
				case MessagePlayerTurn:
					// Handle the message player turn message
					break;
				case MessagePossessCountry:
					// Handle the message possess country message
					break;
				case MessageChooseCountry:
					// Handle the message choose country message
					break;
				case MessagePlaceTroops:
					// Handle the message place troops message
					break;
				case MessageDiceThrowRequest:
					// Handle the message dice throw request message
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
	public void closeEverything(){
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
}
