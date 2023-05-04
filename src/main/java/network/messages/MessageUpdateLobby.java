package network.messages;

import game.Lobby;

public class MessageUpdateLobby  extends Message {

	/**
	 * 
	 */
	private Lobby lobby;
	
	private static final long serialVersionUID = 1L;
	
	public MessageUpdateLobby(Lobby lobby) {
		super(MessageType.MessageUpdateLobby);
		this.lobby = lobby;
	}

	public Lobby getLobby() {
		return lobby;
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
	
	

}
