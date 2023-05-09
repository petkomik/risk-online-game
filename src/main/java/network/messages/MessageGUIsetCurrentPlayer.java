package network.messages;

import game.Lobby;
import gameState.GameState;

public class MessageGUIsetCurrentPlayer extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; 
	private int troopsLeft;
	private GameState gameState;
	private Lobby lobby;
	
	public GameState getGameState() {
		return gameState;
	}

	public MessageGUIsetCurrentPlayer(GameState gameState,int id, int troopsLeft, Lobby clientsLobby) {
		super(MessageType.MessageGUIsetCurrentPlayer );
	this.id = id;
	this.gameState = gameState;
	this.troopsLeft = troopsLeft;
	this.lobby = clientsLobby;
	}

	public int getId() {
		return id;
	}

	public int getTroopsLeft() {
		return troopsLeft;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
