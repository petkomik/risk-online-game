package network.messages;

import game.Lobby;
import gameState.GameState;

public class MessageGUIupdateRanks extends Message {

	private static final long serialVersionUID = 1L;
	
	private GameState gameState;
	int[] ranks;

	private Lobby lobby;
	
	public MessageGUIupdateRanks(GameState gameState, int[] ranks, Lobby clientsLobby) {
		super(MessageType.MessageGUIupdateRanks);
		this.gameState = gameState;
		this.ranks = ranks;
		this.lobby = clientsLobby;
	}

	public GameState getGameState() {
		return gameState;
	}

	public int[] getRanks() {
		return ranks;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
