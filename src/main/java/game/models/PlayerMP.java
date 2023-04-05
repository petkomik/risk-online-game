package game.models;

import game.Profile;
import network.ClientHandler;
import network.messages.Message;
import network.messages.MessageType;

public class PlayerMP extends Player {
	
	private Profile profile;
	private ClientHandler clientHandler;
	private volatile boolean messageAchieved = false;
	private volatile boolean messageRead = false;
	private volatile MessageType awaitingMessageType;
	private volatile Message nextMessage;
	
	public PlayerMP(Profile profile, ClientHandler clientHandler) {
		super(profile.getUserName(), profile.getId());
		this.clientHandler = clientHandler;
		this.profile = profile;
	}
	
	public synchronized void messageAchieved(Message message) {
		MessageType messageType = message.getMessageType();
		if ((this.awaitingMessageType == messageType && messageRead)|| nextMessage.getMessageType() == messageType) {
			nextMessage = message;
			messageAchieved = true;
		}
	}
	
	public Message awaitMessage(int milliseconds, MessageType messageType) throws InterruptedException {
		//TODO
		messageAchieved = false;
		this.awaitingMessageType = messageType;
		long startTime = System.currentTimeMillis();
        while (!messageAchieved && System.currentTimeMillis() - startTime < milliseconds) {
            Thread.sleep(100); //Thread sleeps for 0.1 seconds to avoid busy waiting
        }
        synchronized (nextMessage) {
			messageRead = true;
			return nextMessage;
		}
	}

	public ClientHandler getClientHandler() {
		return clientHandler;
	}
	

	
//	public int getRank() {
//		int wins = profile.getWins();
//		int losses = profile.getLoses();
//		int rank = wins / (wins + losses);
//		return rank;
//	}
	
}
