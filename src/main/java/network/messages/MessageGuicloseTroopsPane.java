package network.messages;

import gameState.GameState;

/**
 * Message for the closer of toops Pane.
 *
 * @author dignatov
 */
public class MessageGuicloseTroopsPane extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;

  /**
   * Constructs a MessageGUIcloseTroopsPane object with the specified game state.
   *
   * @param gameState The game state associated with the message
   */
  public MessageGuicloseTroopsPane(GameState gameState) {
    super(MessageType.MessageGUIcloseTroopsPane);
    this.gameState = gameState;
  }

  public GameState getGameState() {
    return gameState;
  }

}
