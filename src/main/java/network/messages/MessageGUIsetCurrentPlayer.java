package network.messages;

import gameState.GameState;

public class MessageGUIsetCurrentPlayer extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; 
	private int troopsLeft;
	private GameState gameState;
	
	public GameState getGameState() {
		return gameState;
	}

	public MessageGUIsetCurrentPlayer(GameState gameState,int id, int troopsLeft) {
		super(MessageType.MessageGUIsetCurrentPlayer );
	this.id = id;
	this.gameState = gameState;
	this.troopsLeft = troopsLeft;
	
	}

	public int getId() {
		return id;
	}

	public int getTroopsLeft() {
		return troopsLeft;
	}

}
