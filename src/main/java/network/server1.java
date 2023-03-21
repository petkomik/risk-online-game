package network;
//Server Code
import java.io.*;
import java.net.*;

public class server1 {
 private static ServerSocket serverSocket;
 private static int i = 0;

 public static void main(String[] args) throws IOException {
     try {
         serverSocket = new ServerSocket(4998);
         serverSocket.setReuseAddress(true);
         System.out.println("Waiting for connection...");
     } catch (Exception e) {
         System.out.println("Unable to connect");
         return;
     }

     while (true) {
         Socket socket = serverSocket.accept();
         i++;

         ClientHandler newClient = new ClientHandler(socket);
         newClient.start();
         System.out.println("Client " + i + " connected");
     }
 }
}
