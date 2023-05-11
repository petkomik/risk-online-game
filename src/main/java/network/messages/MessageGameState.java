package network.messages;

import gameState.GameState;

public class MessageGameState extends Message {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameState gameState;
	public MessageGameState( ) {
		super(MessageType.MessageGameState);
	}
	public MessageGameState( GameState gameState) {
		super(MessageType.MessageGameState);
		this.setGameState(gameState);
	}
	public GameState getGameState() {
		return gameState;
	}
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

}
