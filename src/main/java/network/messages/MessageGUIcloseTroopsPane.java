package network.messages;

import gameState.GameState;

public class MessageGUIcloseTroopsPane extends Message {

	private static final long serialVersionUID = 1L;
	private GameState gameState;


	public MessageGUIcloseTroopsPane(GameState gameState) {
		super(MessageType.MessageGUIcloseTroopsPane);
		this.gameState = gameState;
	}

	public GameState getGameState() {
		return gameState;
	}
	
}
