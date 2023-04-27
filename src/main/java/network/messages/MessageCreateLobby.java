package network.messages;

import game.Lobby;

public class MessageCreateLobby extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int playerID;
	private Lobby lobby;
	public MessageCreateLobby(int playerID,Lobby lobby) {
		super(MessageType.MessageCreateLobby);
		this.playerID = playerID;
		this.lobby = lobby;
	}
	public int getPlayerID() {
		return playerID;
	}
	
	public Lobby getLobby() {
		return lobby;
	}

}
