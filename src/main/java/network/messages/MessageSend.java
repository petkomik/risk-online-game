package network.messages;

import database.Profile;
import game.models.Player;


public class MessageSend extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Player playerFrom;
	private Player playerTo;
	private String playerToS;
	private Profile profileFrom;
	private boolean forLobby;
	

	public MessageSend(String message, Player playerFrom, Player playerTo) {
		super(MessageType.MessageSend);
		this.message = message;
		this.playerFrom = playerFrom;
		this.playerTo = playerTo;
	}
	public MessageSend(String message,  String playerToS) {
		super(MessageType.MessageSend);
		this.message = message;
		
		this.playerToS = playerToS;
	}
	
	public MessageSend(String message, Profile profileFrom, boolean forLobby) {
		super(MessageType.MessageSend);
		this.message = message;
		this.profileFrom = profileFrom;
		this.forLobby = forLobby;
	}
	
	
	public Player getPlayerFrom() {
		return playerFrom;
	}
	
	public Player getPlayerTo() {
		return playerTo;
	}
	
	public String getMessage() {
		return message;
	}
	public Profile getProfileFrom() {
		return profileFrom;
	}
	public boolean isForLobby() {
		return forLobby;
	}

}
