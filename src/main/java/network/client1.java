package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class client1 {
	private Socket socket;
	private BufferedReader bufferReader;
	private BufferedWriter bufferWirter;
	private String userName;

	public client1(Socket socket, String userName) {
		this.userName = userName;
		this.socket = socket;
		try {
			this.bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferWirter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			closeEverything(socket, bufferReader, bufferWirter);
			e.printStackTrace();
		}

	}

	private void closeEverything(Socket socket2, BufferedReader bufferReader2, BufferedWriter bufferWirter2) {

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
			closeEverything(socket2, bufferReader2, bufferWirter2);
			e.printStackTrace();
		}

	}

	public void sendMessage() {

		try {
			bufferWirter.write(userName);
			bufferWirter.newLine();
			bufferWirter.flush();

			Scanner scanner = new Scanner(System.in);
			while (socket.isConnected()) {
				bufferWirter.write(userName + "; " + scanner.nextLine());
				bufferWirter.newLine();
				bufferWirter.flush();
			}

		} catch (Exception e) {
			closeEverything(socket, bufferReader, bufferWirter);
		}
	}

	public void listenForMessage() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				String msg;
				while (socket.isConnected()) {
					try {
						msg = bufferReader.readLine();
						System.out.println(msg);
					} catch (IOException e) {
						closeEverything(socket, bufferReader, bufferWirter);
						e.printStackTrace();
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
		client1 client = new client1(socket , username);
		client.listenForMessage();
		client.sendMessage();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	
}
}
