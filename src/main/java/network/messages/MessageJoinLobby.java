package network.messages;

import game.Lobby;

public class MessageJoinLobby extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Lobby lobby;

	public MessageJoinLobby(Lobby lobby) {
		super(MessageType.MessageJoinLobby);
		this.lobby = lobby;
	}

	public Lobby getLobby() {
		return lobby;
	}

	

}
