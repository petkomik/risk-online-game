package network.messages;

import database.Profile;

public class MessageDisconnect extends Message {
	/**
	 * @author dignatov
	 */
	private static final long serialVersionUID = 1L;
	private String playername;
	private String message;
	private Profile profile;

	/**
	 * Constructs a {@link MessageDisconnect} object with the given profile.
	 *
	 * @param profile of user
	 */
	public MessageDisconnect(Profile profile) {
		super(MessageType.Disconnect);
		playername = profile.getUserName();
		this.profile = profile;

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
