package network.messages;

import game.models.Lobby;

public class MessageReadyToPlay  extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Lobby lobby;
	public MessageReadyToPlay(Lobby lobby) {
		super(MessageType.MessageReadyToPlay);
		this.lobby = lobby;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
	

}
