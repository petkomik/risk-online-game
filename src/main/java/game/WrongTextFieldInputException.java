package game;

/**
 * Exception class for wrong text input
 * @author srogalsk
 *
 */

public class WrongTextFieldInputException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
	
	public WrongTextFieldInputException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
