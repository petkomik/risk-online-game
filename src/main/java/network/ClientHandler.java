package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import game.Profile;
import network.messages.Message;
import network.messages.MessageProfile;
import network.messages.MessageSend;

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
            this.profile = ((MessageProfile)clientIdentifierMessage).getProfile();
            this.clientUsername = profile.getUserName();
            clientHandlers.add(this);
            broadcastMessage(new MessageSend("SERVER: " + clientUsername + " has entered the chat"));
        } catch (IOException | ClassNotFoundException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }

    }

    private void broadcastMessage(Message message) {
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

    public void removeClientHandler() {

        clientHandlers.remove(this);
        broadcastMessage(new MessageSend("Server:  " + this.clientUsername + " has left the chat"));

    }

    @Override
    public void run() {
        Message messageFromClient;
        while (socket.isConnected()) {
            try {

                messageFromClient = (Message) objectInputStream.readObject();
                switch(messageFromClient.getMessageType()) {
				case MessageSend:
	                broadcastMessage(messageFromClient);
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

    private void closeEverything(Socket socket2, ObjectInputStream objectInputStream2,
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
