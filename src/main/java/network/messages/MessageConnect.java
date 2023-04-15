package network.messages;

import database.Profile;

public class MessageConnect extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String playername;

	public MessageConnect(Profile profile) {
		super(MessageType.Connect);
		playername = profile.getUserName();

	}

	public String getPlayername() {
		return playername;
	}

}
