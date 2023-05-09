package network.messages;

import java.util.ArrayList;

import game.models.Player;
import gameState.GameState;

public class MessageGUIgameIsOver extends Message {

	private static final long serialVersionUID = 1L;
	
	GameState gameState;
	ArrayList<Player> podium;

	public MessageGUIgameIsOver(GameState gameState, ArrayList<Player> podium) {
		super(MessageType.MessageGUIgameIsOver);
		this.gameState = gameState;
		this.podium = podium;
	}

	public GameState getGameState() {
		return gameState;
	}

	public ArrayList<Player> getPodium() {
		return podium;
	}

}
