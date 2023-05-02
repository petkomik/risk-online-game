package network.messages;

import game.Lobby;
import game.gui.LobbyGUI;

public class MessageCreateLobby extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int playerID;
	private Lobby lobby;
	private Message message;

	public MessageCreateLobby(int playerID, Lobby aLobby) {
		super(MessageType.MessageCreateLobby);
		this.playerID = playerID;
		this.lobby = aLobby;
	}

	public MessageCreateLobby(Message message) {
		super(MessageType.MessageCreateLobby);
		this.message = message;
	}

	public MessageCreateLobby() {
		super(MessageType.MessageCreateLobby);
	}

	public int getPlayerID() {
		return playerID;
	}

	public Lobby getLobby() {
		return lobby;
	}

	public Message getMessage() {
		return message;
	}

}
