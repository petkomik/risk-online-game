package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;

public class MessageGUIsetTroopsOnTerritory extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private CountryName countryName;
  private int numTroopsOfCountry;
  private Lobby lobby;


  public MessageGUIsetTroopsOnTerritory(GameState gameState, CountryName countryName,
      int numTroopsOfCountry, Lobby clientsLobby) {
    super(MessageType.MessageGUIsetTroopsOnTerritory);
    this.gameState = gameState;
    this.countryName = countryName;
    this.numTroopsOfCountry = numTroopsOfCountry;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }


  public CountryName getCountryName() {
    return countryName;
  }


  public int getNumTroopsOfCountry() {
    return numTroopsOfCountry;
  }

  public Lobby getLobby() {
    return lobby;
  }


}
