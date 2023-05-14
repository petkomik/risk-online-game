package gameState;

import game.models.Battle;
import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import game.models.Territory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Modells the State of game. All information about the player, territories are saved. Consists
 * mostly of getter and setter methods.
 *
 * @author pmikov
 */

public class GameState implements Serializable {

  private static final long serialVersionUID = 1L;
  private HashMap<CountryName, Territory> territories;
  private HashMap<Continent, ArrayList<CountryName>> continents;
  private HashMap<Integer, Player> players;
  private int numberOfCardsTurnedIn;
  private boolean gameIsOver;
  private ArrayList<Player> alivePlayers;
  private Player currentPlayer;
  private Phase currentTurnPhase;
  private Period currentGamePeriod;
  private HashMap<Integer, Integer> playersDiceThrown;
  private HashMap<Integer, Integer> playerTroopsLeft;
  private HashMap<Integer, ArrayList<Card>> riskCardsInPlayers;
  private ArrayList<Card> cards;
  private CountryName lastAttackingCountry;
  private CountryName lastFortifyingCounty;
  private Battle battle;
  private boolean lastTurnWonterritory;
  private ArrayList<Player> deadPlayers;
  private int gameStateVersion;
  private boolean playerForitfied;

  /**
   * Constructor for the class. Initializes all the variables and sets values to default for
   * beginning of the game.
   *
   * @param lobby Lobby instance of the game played
   */

  public GameState(Lobby lobby) {
    continents = new HashMap<Continent, ArrayList<CountryName>>();
    cards = new ArrayList<Card>();
    territories = new HashMap<CountryName, Territory>();
    players = new HashMap<Integer, Player>();
    alivePlayers = new ArrayList<Player>();
    playerTroopsLeft = new HashMap<Integer, Integer>();
    playersDiceThrown = new HashMap<Integer, Integer>();
    riskCardsInPlayers = new HashMap<Integer, ArrayList<Card>>();
    deadPlayers = new ArrayList<Player>();
    numberOfCardsTurnedIn = 0;
    currentTurnPhase = null;
    currentGamePeriod = Period.DICETHROW;
    gameIsOver = false;
    lastAttackingCountry = null;
    lastFortifyingCounty = null;
    setPlayerForitfied(false);
    gameStateVersion = 0;

    SetTerritories.createTerritories(territories);
    SetTerritories.setNeighboringCountrys(territories);
    SetTerritories.createContinents(continents, territories);
    SetTerritories.createCardDeck(cards, territories);

    for (Player player : lobby.getPlayerList()) {
      this.players.put(player.getId(), player);
      this.alivePlayers.add(player);
      this.riskCardsInPlayers.put(player.getId(), new ArrayList<Card>());
    }

  }

  public void addTroopsToPlayer(int idPlayer, int numberOfTroops) {
    this.getPlayerTroopsLeft().replace(idPlayer,
        this.getPlayerTroopsLeft().get(idPlayer) + numberOfTroops);
  }

  /**
   * Removes number of troops left to deploy from player.
   *
   * @param idPlayer whose troops are getting changed
   * @param numberOfTroops substracted from avaiable
   */

  public void subtractTroopsToPlayer(Integer idPlayer, int numberOfTroops) {
    if (this.playerTroopsLeft.get(idPlayer) >= numberOfTroops) {
      this.getPlayerTroopsLeft().put(idPlayer,
          this.getPlayerTroopsLeft().get(idPlayer) - numberOfTroops);
    } else {
      System.out.println("Tried to remove more troops than available");
    }

  }

  /**
   * Player turns in a correct set of risk cards. The bonus troops are calculated.
   *
   * @return int number of bonus troops the player receives
   */

  public int playerTurnsInCard() {
    this.numberOfCardsTurnedIn++;
    switch (this.numberOfCardsTurnedIn) {
      case 1:
        return 4;
      case 2:
        return 6;
      case 3:
        return 8;
      case 4:
        return 10;
      case 5:
        return 12;
      case 6:
        return 15;
      default:
        return (this.numberOfCardsTurnedIn - 6) * 5 + 15;
    }

  }

  /** Method to update the List of Risk Cards owned by player. */

  public void editRiskCardsInPlayers(ArrayList<Card> cardsNew, int idOfPlayer) {
    this.cards.addAll(this.riskCardsInPlayers.get(idOfPlayer));
    this.riskCardsInPlayers.replace(idOfPlayer, cardsNew);
    this.cards.removeAll(cardsNew);
  }

  /**
   * Method to receive a random Risk card for player wih id in param.
   *
   * @param idOfPlayer to receive the risk card.
   */

  public void receiveRandomRiskCard(int idOfPlayer) {
    Random generator = new Random();
    this.cards.stream().forEach(x -> System.out.println(x.toString()));
    Card card = this.cards.remove(generator.nextInt(this.cards.size()));
    this.riskCardsInPlayers.get(idOfPlayer).add(card);


  }

  /**
   * Sets the initial troops for each player. Called after Dice Throw period.
   *
   * @param troops number of troops to be set
   */

  public void setInitialTroops(int troops) {
    for (Integer playerId : this.players.keySet()) {
      this.playerTroopsLeft.put(playerId, 0);
    }
    for (Integer playerId : this.players.keySet()) {
      this.addTroopsToPlayer(playerId, troops);
    }
  }

  /** Adds a Player with id to list with dead players. */

  public void addDeadPlayer(int id) {
    Player dead = this.players.get(id);
    this.deadPlayers.add(dead);

  }

  public HashMap<Integer, Integer> getPlayersDiceThrown() {
    return playersDiceThrown;
  }

  public void setPlayersDiceThrown(HashMap<Integer, Integer> playersDiceThrown) {
    this.playersDiceThrown = playersDiceThrown;
  }

  public void updateTroopsOnTerritory(CountryName countryName, int numberTroops) {
    this.territories.get(countryName).setNumberOfTroops(numberTroops);
  }

  public void setOwnedByTerritory(CountryName country, Player player) {
    this.territories.get(country).setOwnedByPlayer(player);
  }

  public HashMap<CountryName, Territory> getTerritories() {
    return territories;
  }

  public HashMap<Continent, ArrayList<CountryName>> getContinents() {
    return continents;
  }

  public HashMap<Integer, Player> getPlayers() {
    return players;
  }

  public ArrayList<Card> getCards() {
    return cards;
  }

  public int getNumberOfCardsTurnedIn() {
    return numberOfCardsTurnedIn;
  }

  public boolean isGameIsOver() {
    return gameIsOver;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public ArrayList<Player> getAlivePlayers() {
    return alivePlayers;
  }

  public void removeDeadPlayer(int id) {
    this.alivePlayers.removeIf(x -> x.getId() == id);
  }

  public Phase getCurrentTurnPhase() {
    return currentTurnPhase;
  }

  public void setCurrentTurnPhase(Phase currentTurnPhase) {
    this.currentTurnPhase = currentTurnPhase;
  }

  public void setCurrentPlayer(int id) {
    this.currentPlayer = this.players.get(id);
  }

  public Period getCurrentGamePeriod() {
    return currentGamePeriod;
  }

  public void setCurrentGamePeriod(Period currentGamePeriod) {
    this.currentGamePeriod = currentGamePeriod;
  }

  public HashMap<Integer, Integer> getPlayerTroopsLeft() {
    return playerTroopsLeft;
  }

  public void setNextPlayer() {
    this.currentPlayer = this.alivePlayers
        .get((this.alivePlayers.indexOf(currentPlayer) + 1) % this.alivePlayers.size());
  }

  public void setPlayerTroopsLeft(HashMap<Integer, Integer> playerTroopsLeft) {
    this.playerTroopsLeft = playerTroopsLeft;
  }

  public HashMap<Integer, ArrayList<Card>> getRiskCardsInPlayers() {
    return riskCardsInPlayers;
  }

  public CountryName getLastAttackingCountry() {
    return lastAttackingCountry;
  }

  public void setLastAttackingCountry(CountryName lastAttackingCountry) {
    this.lastAttackingCountry = lastAttackingCountry;
  }

  public CountryName getLastFortifyingCounty() {
    return lastFortifyingCounty;
  }

  public void setLastFortifyingCounty(CountryName lastFortifyingCounty) {
    this.lastFortifyingCounty = lastFortifyingCounty;
  }

  public int getTroopsLeftForCurrent() {
    return this.playerTroopsLeft.get(this.currentPlayer.getId());

  }

  public Battle getBattle() {
    return battle;
  }

  public void setBattle(Battle battle) {
    this.battle = battle;
  }

  public boolean getLastTurnWonterritory() {
    return lastTurnWonterritory;
  }

  public void setLastTurnWonterritory(boolean lastTurnWonterritory) {
    this.lastTurnWonterritory = lastTurnWonterritory;
  }

  public ArrayList<Player> getDeadPlayers() {
    return deadPlayers;
  }

  public int getGameStateVersion() {
    return gameStateVersion;
  }

  public void setGameStateVersion(int gameStateVersion) {
    this.gameStateVersion = gameStateVersion;
  }

  public boolean isPlayerForitfied() {
    return playerForitfied;
  }

  public void setPlayerForitfied(boolean playerForitfied) {
    this.playerForitfied = playerForitfied;
  }

}
