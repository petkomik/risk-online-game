package network;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferReader;
	private BufferedWriter bufferWirter;
	private String clientUsername;

	public ClientHandler(Socket socket)
			 {
		this.socket = socket;

		try {
			this.bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferWirter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.clientUsername = bufferReader.readLine();
			clientHandlers.add(this);
			broadcastMessage("SERVER: " + clientUsername + " has entered the chat");
		} catch (IOException e) {
			closeEverything(socket, bufferReader, bufferWirter);
			e.printStackTrace();
		}

	}

	private void broadcastMessage(String message) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
				if (!clientHandler.clientUsername.equals(clientUsername)) {
					clientHandler.bufferWirter.write(message);
					clientHandler.bufferWirter.newLine();
					clientHandler.bufferWirter.flush();
				}
			} catch (IOException e) {
				closeEverything(socket, bufferReader, bufferWirter);
				e.printStackTrace();
			}
		}

	}

	public void removeClientHandler() {

		clientHandlers.remove(this);
		broadcastMessage("Server:  " + this.clientUsername + " has left the chat");

	}

	@Override
	public void run() {
		String messageFromClient;
		while (socket.isConnected()) {
			try {

				messageFromClient = bufferReader.readLine();
				broadcastMessage(messageFromClient);
			} catch (Exception e) {
				closeEverything(socket, bufferReader, bufferWirter);
			}
		}
	}

	private void closeEverything(Socket socket2, BufferedReader bufferReader2, BufferedWriter bufferWirter2) {
		removeClientHandler();
		try {
			if (socket2 != null) {
				
				socket2.close();
			}
			if (bufferWirter2 != null) {
				
				socket2.close();
			}
			if (bufferReader2 != null) {
				
				socket2.close();
			}

			bufferReader2.close();
			bufferWirter2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
