package game.exceptions;

import game.models.CountryName;

/**
 * Exception class for wrong country
 * 
 * @author srogalsk
 *
 */

public class WrongCountryException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String message;

  public WrongCountryException(String message, CountryName country) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
