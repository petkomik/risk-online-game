package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import game.gui.HostServerMessengerController;
import general.AppController;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageServerCloseConnection;

public class Server {

	private static ServerSocket serverSocket;
	private static ClientHandler clientHandler;

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void startServer() {
		System.out.println("The Server has been started ");

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!serverSocket.isClosed()) {
						Socket socket = serverSocket.accept();

						clientHandler = new ClientHandler(socket);

						Thread player = new Thread(clientHandler);
						player.start();

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	public static  void closeServerSocket() {
	
		try {
	        if (serverSocket != null) {
	            serverSocket.close();
	            System.out.println("Server socket closed");

	            if (clientHandler != null) {
	                clientHandler.closeEverything();
	                System.out.println("Client socket closed");
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error closing server socket: " + e.getMessage());
	    }
		
	}

	public static Server createServer(int port) throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		Server server = new Server(serverSocket);
		server.startServer();

		System.out.println(serverSocket.getLocalSocketAddress());
		return server;

	}

	/**
	 * main for specific testing public static void main(String[] args) throws
	 * IOException { ServerSocket serverSocket = new ServerSocket(1234); Server
	 * server = new Server(serverSocket); server.startServer(); }
	 */
}
