package network.messages;

import game.Profile;

public class MessageConnect extends Message {
	private String playername;

	public MessageConnect(Profile profile) {
		super(MessageType.Connect);
		playername = profile.getUserName();

	}

	public String getPlayername() {
		return playername;
	}

}
