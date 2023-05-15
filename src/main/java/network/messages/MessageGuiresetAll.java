package network.messages;

import game.state.GameState;

/**
 * The MessageGUIresetAll class represents a message used to reset all game state in the game GUI.
 * It includes the game state.
 *
 * @author dignatov
 */
public class MessageGuiresetAll extends Message {

  private static final long serialVersionUID = 1L;

  GameState gameState;

  /**
   * Constructs a MessageGUIresetAll object with the specified game state.
   *
   */
  public MessageGuiresetAll(GameState gameState) {
    super(MessageType.MessageGUIresetAll);
    this.gameState = gameState;
  }

  public GameState getGameState() {
    return gameState;
  }
}
