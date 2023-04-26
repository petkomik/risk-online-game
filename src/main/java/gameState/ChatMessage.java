package gameState;

import database.Profile;

public class ChatMessage {
	// 
	private String message;
	private String sender;
	public String getMessage() {
		return message;
	}
	public String getSender() {
		return sender;
	}
	public ChatMessage(String message, String sender) {
		
		this.message = message;
		this.sender = sender;
	}
	
	
}
