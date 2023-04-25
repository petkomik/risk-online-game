package network.messages;

import database.Profile;

public class MessageDisconnect extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String playername;
	private String message;
	private Profile profile;
	public MessageDisconnect(Profile profile) {
		super(MessageType.Disconnect);
		playername = profile.getUserName();
		this.profile = profile;

	}

	public MessageDisconnect(String message) {
		super(MessageType.Disconnect);
		this.message = message;

	}

	public String getPlayername() {
		return playername;
	}

	public String getMessage() {
		return message;
	}

	public Profile getProfile() {
		return profile;
	}
}
