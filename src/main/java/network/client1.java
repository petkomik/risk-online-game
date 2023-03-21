package network;
import java.io.*;
import java.net.*;

public class client1 {
	private static BufferedReader br;

	public static void main(String[] args) throws IOException {
		Socket server = new Socket("localhost", 4998);
		PrintWriter pr = new PrintWriter(server.getOutputStream());
		pr.println("Clients message to the Server");
		pr.flush();
		InputStreamReader in = new InputStreamReader(server.getInputStream());
		br = new BufferedReader(in);
		String message = br.readLine();
		System.out.println("Received message from server: " + message);
	}
}
