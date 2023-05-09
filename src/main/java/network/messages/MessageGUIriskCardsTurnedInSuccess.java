package network.messages;

import java.util.ArrayList;

import game.models.Card;
import gameState.GameState;

public class MessageGUIriskCardsTurnedInSuccess extends Message {

	private static final long serialVersionUID = 1L;

	GameState gamestate;
	ArrayList<Card> card;
	int idOfPlayer;
	int bonusTroops;
	
	public MessageGUIriskCardsTurnedInSuccess(GameState gamestate, ArrayList<Card> card, int idOfPlayer, int bonusTroops) {
		super(MessageType.MessageGUIriskCardsTurnedInSuccess);
		this.gamestate = gamestate;
		this.card = card;
		this.idOfPlayer = idOfPlayer;
		this.bonusTroops = bonusTroops;
	}

	public GameState getGamestate() {
		return gamestate;
	}

	public ArrayList<Card> getCard() {
		return card;
	}

	public int getIdOfPlayer() {
		return idOfPlayer;
	}

	public int getBonusTroops() {
		return bonusTroops;
	}
	
}
