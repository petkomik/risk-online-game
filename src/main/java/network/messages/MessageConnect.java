package network.messages;

import database.Profile;

public class MessageConnect extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String playername;
	private Profile profile;
	public MessageConnect(Profile profile) {
		super(MessageType.Connect);
		playername = profile.getUserName();
		this.profile = profile;

	}

	public String getPlayername() {
		return playername;
	}

	public Profile getProfile() {
		return profile;
	}

}
