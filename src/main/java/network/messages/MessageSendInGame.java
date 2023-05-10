package network.messages;

import database.Profile;
import game.models.Lobby;

public class MessageSendInGame extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Lobby lobby;
	private Profile profile;
	
	public MessageSendInGame(String message, Lobby lobby, Profile profile) {
		super(MessageType.MessageSendInGame);
		this.message = message;
		this.lobby = lobby;
		this.profile = profile;
	}

	public Lobby getLobby() {
		return lobby;
	}

	public String getMessage() {
		return message;
	}

	public Profile getProfile() {
		return profile;
	}

}
