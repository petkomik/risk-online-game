package network.messages;

import java.util.ArrayList;

import game.models.Card;
import gameState.GameState;

public class MessageGUIchnagePlayer extends Message {
  /**
   * @author Danail
   */
  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private int id;
  private ArrayList<Card> cards;

  /**
   * Constructs a MessageGUIChangePlayer object with the specified game state, player ID, and cards.
   *
   * @param gameState The game state associated with the message
   * @param id The ID of the player being changed
   * @param cards The list of cards associated with the player
   */
  public MessageGUIchnagePlayer(GameState gameState, int id, ArrayList<Card> cards) {
    super(MessageType.MessageGUIchnagePlayer);
    this.gameState = gameState;
    this.id = id;
    this.cards = cards;
  }

  public GameState getGameState() {
    return gameState;
  }

  public int getId() {
    return id;
  }

  public ArrayList<Card> getCards() {
    return cards;
  }

}
