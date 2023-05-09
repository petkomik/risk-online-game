package network.messages;

import gameState.GameState;

public class MessageGUIRollInitalDice extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GameState gameState;

	private int Id;
	private int value;
	
	public MessageGUIRollInitalDice(GameState gameState, int Id, int value ) {
		super(MessageType.MessageGUIRollInitalDice);
	}

	public int getId() {
		return Id;
	}
	public GameState getGameState() {
		return gameState;
	}

	public int getValue() {
		return value;
	}

}
