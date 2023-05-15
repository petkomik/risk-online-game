package game.exceptions;

/**
 * Exception class for wrong period.
 *
 * @author srogalsk
 *
 */

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
