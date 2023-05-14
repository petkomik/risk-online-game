package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import general.AppController;
import network.messages.Message;
import network.messages.MessageConnect;
import network.messages.MessageServerCloseConnection;

/**
 * @author dignatov
 *
 *         The Server class represents the server-side of the network
 *         application. It listens for incoming client connections, creates
 *         ClientHandler instances for each client, and manages the server
 *         socket.
 */
public class Server {

	private static ServerSocket serverSocket;
	private static ClientHandler clientHandler;
	private static Thread server;

	/**
	 * Constructs a Server object with the specified ServerSocket.
	 *
	 * @param serverSocket The ServerSocket used by the server
	 */
	public Server(ServerSocket serverSocket) {
		Server.serverSocket = serverSocket;
	}

	/**
	 * Starts the server by creating a new thread to accept incoming client
	 * connections.
	 */
	public void startServer() {
		System.out.println("The Server has been started ");

		server = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!serverSocket.isClosed()) {
						Socket socket = serverSocket.accept();

						clientHandler = new ClientHandler(socket);

						Thread player = new Thread(clientHandler);
						clientHandler.setClieantHandlerThread(player);
						player.start();

					}
					closeServerSocket();

				} catch (Exception e) {
					closeServerSocket();
					e.printStackTrace();
				}
			}

		});
		server.start();

	}

	/**
	 * Closes the server socket and any associated client sockets.
	 */
	public static void closeServerSocket() {
		System.out.println("we reach server closer");
		server.interrupt();
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

	/**
	 * Creates and starts a Server object with the specified port number.
	 *
	 * @param port The port number for the server
	 * @return The Server object that was created
	 * @throws IOException if an I/O error occurs when opening the server socket
	 */
	public static Server createServer(int port) throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		Server server = new Server(serverSocket);
		server.startServer();

		System.out.println(serverSocket.getLocalSocketAddress());
		return server;

	}

}
