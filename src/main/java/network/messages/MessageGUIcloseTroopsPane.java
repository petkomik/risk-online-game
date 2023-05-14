package network.messages;

import gameState.GameState;
/*
 * @author dignatov
 */
public class MessageGUIcloseTroopsPane extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;

  /**
   * Constructs a MessageGUIcloseTroopsPane object with the specified game state.
   *
   * @param gameState The game state associated with the message
   */
  public MessageGUIcloseTroopsPane(GameState gameState) {
    super(MessageType.MessageGUIcloseTroopsPane);
    this.gameState = gameState;
  }

  public GameState getGameState() {
    return gameState;
  }

}
