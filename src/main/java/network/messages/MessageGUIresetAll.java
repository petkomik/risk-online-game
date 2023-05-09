package network.messages;

import gameState.GameState;

public class MessageGUIresetAll extends Message {
	
	private static final long serialVersionUID = 1L;
	
	GameState gameState;

	public MessageGUIresetAll(GameState gameState) {
		super(MessageType.MessageGUIresetAll);
		this.gameState = gameState;
	}

}
