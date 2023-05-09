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

}
