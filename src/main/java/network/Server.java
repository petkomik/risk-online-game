package network;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	
	private ServerSocket serverSocket;

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	public void startServer() {
		
		try {
			while(!serverSocket.isClosed()) {
			Socket socket =	serverSocket.accept();
			System.out.println("A new Client has connected ");
			ClientHandler clientHandler = new ClientHandler(socket);
			
			
			Thread player = new Thread(clientHandler);
			player.start();
			
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void closeServerSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(1234);
		Server server = new Server(serverSocket);
		server.startServer(); 
	}
}
