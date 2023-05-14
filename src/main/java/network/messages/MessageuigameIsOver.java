package network.messages;

import game.models.Lobby;
import game.models.Player;
import gameState.GameState;
import java.util.ArrayList;

/**
 * The MessageGUIgameIsOver class represents a message used to indicate that the game is over in the
 * game GUI. It includes the game state, the podium (list of players in finishing order), and the
 * associated lobby.
 *
 * @author: dignatov
 */


public class MessageuigameIsOver extends Message {

  private static final long serialVersionUID = 1L;

  private GameState gameState;
  private ArrayList<Player> podium;
  private Lobby lobby;

  /**
   * Constructs a MessageGUIgameIsOver object with the specified game state, podium, and lobby.
   *
   * @param gameState The game state associated with the message
   * @param podium The list of players in the finishing order (podium)
   * @param clientsLobby The associated lobby
   *
   */
  public MessageuigameIsOver(GameState gameState, ArrayList<Player> podium, Lobby clientsLobby) {
    super(MessageType.MessageGUIgameIsOver);
    this.gameState = gameState;
    this.podium = podium;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public ArrayList<Player> getPodium() {
    return podium;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
