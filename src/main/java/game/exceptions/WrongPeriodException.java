package game.exceptions;

import java.util.ArrayList;

import game.models.Card;

public class WrongPeriodException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public WrongPeriodException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
