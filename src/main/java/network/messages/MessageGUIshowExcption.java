package network.messages;

import gameState.GameState;

public class MessageGUIshowExcption extends Message {

  /**
   * 
   */
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

  public MessageGUIshowExcption(GameState gameState, Exception e) {
    super(MessageType.MessageGUIshowExcption);
    this.e = e;
    this.gameState = gameState;

  }

}
