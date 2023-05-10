package network.messages;

import game.models.Battle;
import game.models.Lobby;
import gameState.GameState;

public class MessageGUIOpenBattleFrame extends Message {

	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private Battle battle;
	private Lobby lobby;

	public MessageGUIOpenBattleFrame(GameState gameState, Battle battle, Lobby clientsLobby) {
		super(MessageType.MessageGUIOpenBattleFrame);
		this.gameState = gameState;
		this.battle = battle;
		this.lobby = clientsLobby;
	}

	public GameState getGameState() {
		return gameState;
	}

	public Battle getBattle() {
		return battle;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
