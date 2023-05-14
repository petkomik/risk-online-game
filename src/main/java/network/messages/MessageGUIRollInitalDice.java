package network.messages;

import game.models.Lobby;
import gameState.GameState;
/**
 * The MessageGUIRollInitalDice class represents a message used to indicate the result of rolling the initial dice in the game GUI.
 * It includes the game state, the ID of the player, the rolled value, and the associated lobby.
 * 
 * author dignatov
 */
public class MessageGUIRollInitalDice extends Message {

  private static final long serialVersionUID = 1L;
  GameState gameState;

  private int Id;
  private int value;
  private Lobby lobby;
	/**
	 * Constructs a MessageGUIRollInitalDice object with the specified game state, player ID, rolled value, and associated lobby.
	 *
	 * @param gameState The game state associated with the message
	 * @param id The ID of the player
	 * @param value The rolled value
	 * @param lobby The associated lobby
	 */
  public MessageGUIRollInitalDice(GameState gameState, int Id, int value, Lobby lobby) {
    super(MessageType.MessageGUIRollInitalDice);
    this.gameState = gameState;
    this.Id = Id;
    this.value = value;
    this.lobby = lobby;
  }

  public int getId() {
    return Id;
  }

  public GameState getGameState() {
    return gameState;
  }

  public int getValue() {
    return value;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
