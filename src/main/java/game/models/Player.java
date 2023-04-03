package game.models;

import network.ClientHandler;

/**
 * Player class to model the player entity
 *
 * @author jorohr
 */

public class Player {
	
	private String name;
	private String color;
	private int id;
	private int rank;
	private ClientHandler clientHandler;
	private volatile boolean messageAchieved = false;
	
	public Player(String name, int id, ClientHandler clientHandler) {
		this.name = name;
		this.id = id;
		this.clientHandler = clientHandler;
	}
	
	// To be called in the ClientHandler Protocoll
	public void messageAchieved() {
		messageAchieved = true;
	}
	
	public boolean awaitMessage(int milliseconds, String message) throws InterruptedException {
		//TODO
		messageAchieved = false;
		long startTime = System.currentTimeMillis();
        while (!messageAchieved && System.currentTimeMillis() - startTime < milliseconds) {
            Thread.sleep(100); //Thread sleeps for 0.1 seconds to avoid busy waiting
        }
		return messageAchieved;
	}
	
	public void incrementRank() {
		rank+=1;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ClientHandler getClientHandler() {
		return this.clientHandler;
	}
	
}
