package network.messages;

import game.state.GameState;

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
   */
  public MessageGuicloseTroopsPane(GameState gameState) {
    super(MessageType.MessageGUIcloseTroopsPane);
    this.gameState = gameState;
  }

  public GameState getGameState() {
    return gameState;
  }

}
