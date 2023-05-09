package network.messages;

import java.util.ArrayList;

import game.Lobby;
import game.models.Player;
import gameState.GameState;

public class MessageGUIgameIsOver extends Message {

	private static final long serialVersionUID = 1L;
	
	private GameState gameState;
	private ArrayList<Player> podium;
	private Lobby lobby;

	public MessageGUIgameIsOver(GameState gameState, ArrayList<Player> podium, Lobby clientsLobby) {
		super(MessageType.MessageGUIgameIsOver);
		this.gameState = gameState;
		this.podium = podium;
		this.lobby = clientsLobby;
	}

	public GameState getGameState() {
		return gameState;
	}

	public ArrayList<Player> getPodium() {
		return podium;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
