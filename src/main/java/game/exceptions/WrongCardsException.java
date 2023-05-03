package game.exceptions;

import java.util.ArrayList;
import game.models.Card;

/**
 * Exception class for wrong cards
 * @author srogalsk
 *
 */

public class WrongCardsException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public WrongCardsException(String message, ArrayList<Card> cards) {
		this.message = message;
	}

	public WrongCardsException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
