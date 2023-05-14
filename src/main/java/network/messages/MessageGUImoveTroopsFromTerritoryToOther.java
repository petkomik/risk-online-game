package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;
/**
 * The MessageGUImoveTroopsFromTerritoryToOther class represents a message used to indicate the movement of troops from one territory to another in the game GUI.
 * It includes the game state, the source territory, the destination territory, the number of troops to move from the source, the number of troops to move to the destination, and the associated lobby.
 * 
 * @author dignatov
 */
public class MessageGUImoveTroopsFromTerritoryToOther extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private CountryName from;
  private CountryName to;
  private int numberFrom;
  private int numberTo;
  private Lobby lobby;

  /**
   * Constructs a MessageGUImoveTroopsFromTerritoryToOther object with the specified game state, source territory,
   * destination territory, number of troops to move from the source, number of troops to move to the destination,
   * and lobby.
   *
   * @param gameState The game state associated with the message
   * @param from The source territory from which troops are being moved
   * @param to The destination territory to which troops are being moved
   * @param numberFrom The number of troops to move from the source territory
   * @param numberTo The number of troops to move to the destination territory
   * @param clientsLobby The associated lobby
   */
  public MessageGUImoveTroopsFromTerritoryToOther(GameState gameState, CountryName from,
      CountryName to, int numberFrom, int numberTo, Lobby clientsLobby) {
    super(MessageType.MessageGUImoveTroopsFromTerritoryToOther);
    this.gameState = gameState;
    this.from = from;
    this.to = to;
    this.numberFrom = numberFrom;
    this.numberTo = numberTo;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public CountryName getFrom() {
    return from;
  }

  public CountryName getTo() {
    return to;
  }

  public int getNumberFrom() {
    return numberFrom;
  }

  public int getNumberTo() {
    return numberTo;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
