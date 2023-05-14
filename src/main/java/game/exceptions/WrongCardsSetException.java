package game.exceptions;

/**
 * Exception class for wrong cards
 * 
 * @author srogalsk
 *
 */


public class WrongCardsSetException extends Exception {
  private static final long serialVersionUID = 1L;
  private String message;

  public WrongCardsSetException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

