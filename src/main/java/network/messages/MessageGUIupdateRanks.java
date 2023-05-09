package network.messages;

import gameState.GameState;

public class MessageGUIupdateRanks extends Message {

	private static final long serialVersionUID = 1L;
	
	GameState gameState;
	int[] ranks;
	
	public MessageGUIupdateRanks(GameState gameState, int[] ranks) {
		super(MessageType.MessageGUIupdateRanks);
		this.gameState = gameState;
		this.ranks = ranks;
	}

}
