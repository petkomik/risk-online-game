package network.messages;

import gameState.GameState;
import gameState.Phase;

public class MessageGUIsetPhase extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameState gameState;
	

	private Phase phase;
	
	public GameState getGameState() {
		return gameState;
	}

	public Phase getPhase() {
		return phase;
	}

	public MessageGUIsetPhase( GameState gameState, Phase phase ) {
		super(MessageType.MessageGUIsetPhase);
		this.gameState = gameState;
		this.phase = phase;
	}

}
