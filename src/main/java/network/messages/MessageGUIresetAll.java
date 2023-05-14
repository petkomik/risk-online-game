package network.messages;

import gameState.GameState;
/**
 * The MessageGUIresetAll class represents a message used to reset all game state in the game GUI.
 * It includes the game state.
 * 
 * author dignatov
 */
public class MessageGUIresetAll extends Message {

  private static final long serialVersionUID = 1L;

  GameState gameState;
  /**
   * Constructs a MessageGUIresetAll object with the specified game state.
   *
   * @param gameState The game state associated with the message
   */
  public MessageGUIresetAll(GameState gameState) {
    super(MessageType.MessageGUIresetAll);
    this.gameState = gameState;
  }

  public GameState getGameState() {
    return gameState;
  }
}
