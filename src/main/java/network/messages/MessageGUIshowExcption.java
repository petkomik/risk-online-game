package network.messages;

import gameState.GameState;
/**
 * The MessageGUIshowExcption class represents a message used to show an exception in the game GUI.
 * It includes the game state and the exception object.
 *
 * author dignatov
 */
public class MessageGUIshowExcption extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public GameState getGameState() {
    return gameState;
  }

  public Exception getE() {
    return e;
  }

  private Exception e;
  /**
   * Constructs a MessageGUIshowExcption object with the specified game state and exception.
   *
   * @param gameState The game state associated with the message
   * @param e The exception object to be shown
   */
  public MessageGUIshowExcption(GameState gameState, Exception e) {
    super(MessageType.MessageGUIshowExcption);
    this.e = e;
    this.gameState = gameState;

  }

}
