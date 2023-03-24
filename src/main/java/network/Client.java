package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import network.messages.Message;
import network.messages.MessageSend;

public class Client {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String userName;

    public Client(Socket socket, String userName) {
        this.userName = userName;
        this.socket = socket;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(new MessageSend(userName));
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
            closeEverything(socket2, inputStream2, outputStream2);
            e.printStackTrace();
        }

    }

    public void sendMessage() {

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

    public void listenForMessage() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg;
                while (socket.isConnected()) {
                    try {
                        msg = (Message) inputStream.readObject();
                        switch(msg.getMessageType()) {
						case MessageSend:
	                        System.out.println(((MessageSend)msg).getMessage());
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(" Enter your user name for the group chat ");
        String username = sc.nextLine();
        Socket socket;
        try {
            socket = new Socket("localhost", 1234);
            Client client = new Client(socket, username);
            client.listenForMessage();
            client.sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
