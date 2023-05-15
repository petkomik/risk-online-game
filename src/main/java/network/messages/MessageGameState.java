package network.messages;

import game.state.GameState;

/**
 * This Message is used to send a Game State instance.
 *
 * @author dignatov
 */

public class MessageGameState extends Message {


  private static final long serialVersionUID = 1L;
  private GameState gameState;

  /**
   * Notify message.
   */
  public MessageGameState() {
    super(MessageType.MessageGameState);
  }

  /**
   * Constructs a MessageGameState.
   *
   */
  public MessageGameState(GameState gameState) {
    super(MessageType.MessageGameState);
    this.setGameState(gameState);
  }

  public GameState getGameState() {
    return gameState;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

}
