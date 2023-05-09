package network.messages;

import gameState.GameState;

public class MessageGUIendBattle extends Message {

	private static final long serialVersionUID = 1L;
	
	GameState gameState;

	public MessageGUIendBattle(GameState gameState) {
		super(MessageType.MessageGUIendBattle);
	}

	public GameState getGameState() {
		return gameState;
	}
	

}
