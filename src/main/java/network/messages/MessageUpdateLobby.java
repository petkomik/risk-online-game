package network.messages;

import game.models.Lobby;

public class MessageUpdateLobby  extends Message {

	/**
	 * 
	 */
	private Lobby lobby;
	
	public Lobby getLobby() {
		return lobby;
	}


	private static final long serialVersionUID = 1L;
	
	public MessageUpdateLobby(Lobby lobby) {
		super(MessageType.MessageUpdateLobby);
		this.lobby = lobby;
	}


	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
	
	

}
