package game.exceptions;

/**
 * Exception class for wrong troops count.
 *
 * @author srogalsk
 *
 */

public class WrongTroopsCountException extends Exception {

  private static final long serialVersionUID = 1L;
  private String message;

  public WrongTroopsCountException(String message, int troops) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
