package game.exceptions;

/**
 * Exception class for wrong phase
 * @author srogalsk
 *
 */

public class WrongPhaseException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public WrongPhaseException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
