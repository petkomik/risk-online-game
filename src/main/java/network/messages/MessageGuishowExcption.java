package network.messages;

import game.state.GameState;

/**
 * The MessageGUIshowExcption class represents a message used to show an exception in the game GUI.
 * It includes the game state and the exception object.
 *
 * @author dignatov
 */
public class MessageGuishowExcption extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public GameState getGameState() {
    return gameState;
  }

  public Exception getE() {
    return exception;
  }

  private Exception exception;

  /**
   * Constructs a MessageGUIshowExcption object with the specified game state and exception.
   *
   * @param e The exception object to be shown
   */
  public MessageGuishowExcption(GameState gameState, Exception e) {
    super(MessageType.MessageGUIshowExcption);
    this.exception = e;
    this.gameState = gameState;

  }

}
