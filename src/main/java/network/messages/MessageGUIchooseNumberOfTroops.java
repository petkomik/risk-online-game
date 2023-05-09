package network.messages;

import gameState.ChoosePane;
import gameState.GameState;

public class MessageGUIchooseNumberOfTroops extends Message {
	
	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private int min;
	private int max;
	private ChoosePane choosePane;

	public MessageGUIchooseNumberOfTroops(GameState gameState,
			int min, int max, ChoosePane choosePane) {
		super(MessageType.MessageGUIchooseNumberOfTroops);
		this.gameState = gameState;
		this.min = min;
		this.max = max;
		this.choosePane = choosePane;
	}

	public GameState getGameState() {
		return gameState;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public ChoosePane getChoosePane() {
		return choosePane;
	}

}
