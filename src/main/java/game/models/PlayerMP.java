package game.models;

import game.Profile;
import network.ClientHandler;
import network.messages.MessageType;

public class PlayerMP extends Player {
	
	private Profile profile;
	private ClientHandler clientHandler;
	private volatile boolean messageAchieved = false;
	private volatile MessageType awaitingMessageType;
	
	public PlayerMP(Profile profile, ClientHandler clientHandler) {
		super(profile.getUserName(), profile.getId());
		this.clientHandler = clientHandler;
		this.profile = profile;
	}
	
	public void messageAchieved(MessageType messageType) {
		if (this.awaitingMessageType == messageType) {
			messageAchieved = true;
		}
	}
	
	public boolean awaitMessage(int milliseconds, MessageType messageType) throws InterruptedException {
		//TODO
		messageAchieved = false;
		this.awaitingMessageType = messageType;
		long startTime = System.currentTimeMillis();
        while (!messageAchieved && System.currentTimeMillis() - startTime < milliseconds) {
            Thread.sleep(100); //Thread sleeps for 0.1 seconds to avoid busy waiting
        }
		return messageAchieved;
	}
	

	
//	public int getRank() {
//		int wins = profile.getWins();
//		int losses = profile.getLoses();
//		int rank = wins / (wins + losses);
//		return rank;
//	}
	
}
