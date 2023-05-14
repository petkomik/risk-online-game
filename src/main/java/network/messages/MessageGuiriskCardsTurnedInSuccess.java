package network.messages;

import game.models.Card;
import gameState.GameState;
import java.util.ArrayList;

/**
 * The MessageGUIriskCardsTurnedInSuccess class represents a message used to indicate the successful
 * turning in of Risk cards in the game GUI. It includes the game state, the list of turned in
 * cards, the ID of the player, and the bonus troops earned.
 *
 *
 * @author dignatov
 */
public class MessageGuiriskCardsTurnedInSuccess extends Message {

  private static final long serialVersionUID = 1L;

  GameState gamestate;
  ArrayList<Card> card;
  int idOfPlayer;
  int bonusTroops;

  /**
   * Constructs a MessageGUIriskCardsTurnedInSuccess object with the specified game state, list of
   * turned in cards, player ID, and bonus troops earned.
   *
   * @param gamestate The game state associated with the message
   * @param card The list of turned in cards
   * @param idOfPlayer The ID of the player turning in the cards
   * @param bonusTroops The bonus troops earned from turning in the cards
   */
  public MessageGuiriskCardsTurnedInSuccess(GameState gamestate, ArrayList<Card> card,
      int idOfPlayer, int bonusTroops) {
    super(MessageType.MessageGUIriskCardsTurnedInSuccess);
    this.gamestate = gamestate;
    this.card = card;
    this.idOfPlayer = idOfPlayer;
    this.bonusTroops = bonusTroops;
  }

  public GameState getGamestate() {
    return gamestate;
  }

  public ArrayList<Card> getCard() {
    return card;
  }

  public int getIdOfPlayer() {
    return idOfPlayer;
  }

  public int getBonusTroops() {
    return bonusTroops;
  }

}
