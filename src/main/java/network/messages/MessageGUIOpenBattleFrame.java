package network.messages;

import game.Battle;
import gameState.GameState;

public class MessageGUIOpenBattleFrame extends Message {

	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private Battle battle;

	public MessageGUIOpenBattleFrame(GameState gameState, Battle battle) {
		super(MessageType.MessageGUIOpenBattleFrame);
		this.gameState = gameState;
		this.battle = battle;
	}

	public GameState getGameState() {
		return gameState;
	}

	public Battle getBattle() {
		return battle;
	}

}
