package network.messages;

import gameState.ChoosePane;
import gameState.GameState;

/**
 * This class is a message for the number of troops.
 *
 * @author dignatov
 * 
 */

public class MessageGuichooseNumberOfTroops extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private int min;
  private int max;
  private ChoosePane choosePane;

  /**
   * Constructs a MessageGUIchooseNumberOfTroops object with the specified game state, minimum and
   * maximum number of troops, and ChoosePane.
   *
   * @param gameState The game state associated with the message
   * @param min The minimum number of troops that can be chosen
   * @param max The maximum number of troops that can be chosen
   * @param choosePane The ChoosePane used for selecting the number of troops
   */
  public MessageGuichooseNumberOfTroops(GameState gameState, int min, int max,
      ChoosePane choosePane) {
    super(MessageType.MessageGUIchooseNumberOfTroops);
    this.gameState = gameState;
    this.min = min;
    this.max = max;
    this.choosePane = choosePane;
  }

  public GameState getGameState() {
    return gameState;
  }

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  public ChoosePane getChoosePane() {
    return choosePane;
  }

}
