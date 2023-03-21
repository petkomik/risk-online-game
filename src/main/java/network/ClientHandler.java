package network;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;

class ClientHandler extends Thread {
    private Socket client;
    private PrintWriter pw;
    private BufferedReader br;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        pw = new PrintWriter(client.getOutputStream());
        br = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    @Override
    public void run() {
      
    	try {
            String message = br.readLine();
            System.out.println("Received message from client: " + message);
            String result = additionFromServer(message);
            pw.println(result);
            pw.flush();
        } catch (IOException e) {
            System.out.println("Error while communicating with client");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Error while closing client connection");
            }
        }
       
    }

    public String additionFromServer(String message) {
        // Perform addition calculation here and return the result
        // In this example, we are simply adding a string to the end of the original message
        return message + " addition from server";
    }
}