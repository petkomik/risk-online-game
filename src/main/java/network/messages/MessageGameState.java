package network.messages;

import gameState.GameState;

public class MessageGameState extends Message {


  /**
   * @author Danail
   */
  private static final long serialVersionUID = 1L;
  private GameState gameState;
  /**
   * Notify message
   */
  public MessageGameState() {
    super(MessageType.MessageGameState);
  }
  /**
   * Constructs a MessageGameState 
   *
   * @param new gameState is send 
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
