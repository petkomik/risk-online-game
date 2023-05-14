package gameState;

import game.gui.GamePaneController;
import game.models.Battle;
import game.models.Card;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This class connects the GUI and GameHandler for Offline Games. Takes user input from GUI. Takes
 * decision from GameHandler and changes GUI.
 *
 * @author pmikov
 *
 */

public class SinglePlayerHandler {

  private GameHandler gameHandler;
  private Lobby lobby;
  private GamePaneController gamePaneController;

  /**
   * Constructor for the class.
   *
   * @param lobby Lobby instance for the game
   * @param gamePaneController GamePane
   */

  public SinglePlayerHandler(Lobby lobby, GamePaneController gamePaneController,
      GameType gameType) {
    this.gameHandler = new GameHandler(lobby);
    switch (gameType) {
      case Tutorial:
        this.gameHandler.initTutorial(this);
        break;
      case SinglePlayer:
        this.gameHandler.initSingleplayer(this);
        break;
      case Multiplayer:
        break;
      default:
        break;
    }
    this.gamePaneController = gamePaneController;
    this.lobby = lobby;

  }

  /**
   * Displays a hint on the game pane GUI.
   * 
   * @param title of the tutorial pane
   * @param hint enum for the type of hint
   */

  public void showInitialHint() {
    this.gameHandler.showInitialHint();
  }

  /**
   * Called From GUI during Initial Dice Period. Player wants to throw dice.
   *
   * @param idOfPlayer that interacts with GUI
   */

  public void playerThrowsInitialDice(int idOfPlayer) {
    this.gameHandler.playerThrowsInitialDice(idOfPlayer);
  }

  /**
   * Called from GUI at any point in Game.
   *
   * @param id of player interacting
   * @param country CountryName of territory clicked
   */

  public void clickCountry(int id, CountryName country) {
    this.gameHandler.clickCountry(id, country);
  }

  /**
   * Called from GUI. Player decides to close choose number of troops pane. Allowed for any other
   * than ATTACK_COLONISE.
   *
   * @param country CountryName of territory attacked or sending troops to
   * @param choosePane ChoosePane enum for the pane
   * @param idOfPlayer player interacting
   */

  public void cancelNumberOfTroops(CountryName country, ChoosePane choosePane, int idOfPlayer) {
    this.gameHandler.cancelNumberOfTroops(country, choosePane, idOfPlayer);
  }

  /**
   * Called from GUI. Player confirms choice for number of troops in choosing number of troops pane.
   *
   * @param country CountryName of territory attacked or sending troops to
   * @param choosePane ChoosePane enum for the pane
   * @param troops Amount of troops chosen
   * @param idOfPlayer player interacting
   */

  public void confirmNumberOfTroops(CountryName country, int troops, ChoosePane choosePane,
      int idOfPlayer) {
    this.gameHandler.confirmTroopsToCountry(country, troops, choosePane, idOfPlayer);
  }

  /**
   * Called from GUI. Player wants to turn in a set of risk cards.
   *
   * @param cards List with cards getting turned in
   * @param idOfPlayer Player turning in cards
   */

  public void turnInRiskCards(ArrayList<String> cards, int idOfPlayer) {
    this.gameHandler.turnInRiskCards(cards, idOfPlayer);
  }

  /**
   * Called from GUI. Player clicks on end turn / phase.
   *
   * @param period Current Period on GUI
   * @param phase Current Phase on GUI
   * @param idOfPlayer Player interacting
   */

  public void endPhaseTurn(Period period, Phase phase, int idOfPlayer) {
    this.gameHandler.endPhaseTurn(period, phase, idOfPlayer);
  }

  /**
   * Called from GUI. Attacker clicks on Throw dice in the battle frame.
   */

  public void battleDiceThrow() {
    this.gameHandler.battleDiceThrow();
  }

  public void tutorialCancelClick(Hint hint) {
    this.gameHandler.tutorialCancelClick(hint);
  }

  /**
   * Called from GameHandler. Throwing Battle dice is confirmed. Sending new Dice Values and updated
   * number of troops.
   *
   * @param attackerDiceValues Dices thrwon by attacker
   * @param defenderDiceValues Dices throw by defender
   * @param troopsInAttackAt Number of troops in Attack Attacking
   * @param troopsInAttackDf Number of troops in Attack Defending
   * @param numberOfDice Updated number of dice to throw for both players
   * @throws FileNotFoundException For dice images
   */

  public void rollDiceBattleOnGui(int[] attackerDiceValues, int[] defenderDiceValues,
      int troopsInAttackAt, int troopsInAttackDf, int[] numberOfDice) throws FileNotFoundException {
    this.gamePaneController.rollDiceBattle(attackerDiceValues, defenderDiceValues, troopsInAttackAt,
        troopsInAttackDf, numberOfDice);
  }

  /**
   * Called from GameHandler. Confirms roll intiial dice and gives the value of the dice.
   *
   * @param idOfPlayer whose dice is thrown
   * @param i final value of the dice
   */

  public void rollInitialDiceOnGui(int idOfPlayer, int i) {
    this.gamePaneController.rollInitialDice(idOfPlayer, i);
  }

  /**
   * Called from GameHandler. Displays an Exception Alert in GUI
   *
   * @param e Exception to be shown
   */

  public void showExeceptionOnGui(Exception e) {
    this.gamePaneController.showException(e.toString());
  }

  /**
   * Called from GameHandler. Sets the the given period on the GUI. Based on the period the bottom
   * pane displays different info / buttons.
   *
   * @param period to be set
   */

  public void setPeriodOnGui(Period period) {
    this.gamePaneController.setPeriod(period);
  }

  /**
   * Called from GameHandler. Sets the the given phase on the GUI. Based on the phase the bottom
   * pane displays different info / buttons.
   *
   * @param phase the phase to be set
   */

  public void setPhaseOnGui(Phase phase) {
    this.gamePaneController.setPhase(phase);
  }

  /**
   * Called from GameHandler. Paint the country in the color of player with id and places one troop
   * on it.
   *
   * @param country the CountryName enum of the territory to be claimed
   * @param id the id of player that has claimed territory
   */

  public void possesCountryOnGui(CountryName country, int id, int troopsLeft) {
    this.gamePaneController.claimCountry(country, id);
    this.gamePaneController.setAmountOfTroopsLeftToDeploy(troopsLeft);
  }

  /**
   * Called from GameHandler. Paint the country in the color of player with id.
   *
   * @param country the CountryName enum of the territory to be claimed
   * @param id the id of player that has claimed territory
   */

  public void conquerCountryOnGui(CountryName country, int id, int troops) {
    this.gamePaneController.conquerCountry(country, id, troops);
  }

  /**
   * Called from GameHandler. Changes the current player on the GUI by swapping to the correct color
   * and avatar, and setting the corrct number of troops
   *
   * @param id id of the player
   * @param troopsLeft the amount of troops the player has to deploy
   */

  public void setCurrentPlayerOnGui(int id, int troopsLeft) {
    this.gamePaneController.setCurrentPlayer(id);
    this.gamePaneController.setAmountOfTroopsLeftToDeploy(troopsLeft);
  }

  /**
   * Called from GameHandler. Changes the player which is playing from the instance.
   *
   * @param id ID of player set
   * @param cards List with cards currently possesed by player
   */

  public void chnagePlayerOnGui(int id, ArrayList<Card> cards) {
    this.gamePaneController.setPlayerOnGUI(id, cards);
  }

  /**
   * Called from GameHandler. Opens Chooosing Troops Pane, where the player decides on how many
   * troops to deploy / move / attack with.
   *
   * @param country Getting attacked / foritfied / reinforced
   * @param min minimum troops to choose
   * @param max maximum troops to choose
   * @param choosePane ChoosePane enum for the type of Pane
   */

  public void chooseNumberOfTroopsOnGui(CountryName country, int min, int max,
      ChoosePane choosePane) {
    System.out
        .println("Opening choose troops with " + country.toString() + " " + choosePane.toString());
    this.gamePaneController.showChoosingTroopsPane(country, min, max, choosePane);
  }

  /** Called from GameHandler. Close choosing troops Pane. */

  public void closeTroopsPaneOnGui() {
    this.gamePaneController.closeChoosingTroopsPane();
  }

  /**
   * Called from GameHandler. Sets a number of troops on the specified territory.
   *
   * @param countryName enum of the Territory.
   * @param numTroopsOfCountry number of troops to be set
   */

  public void setTroopsOnTerritoryOnGui(CountryName countryName, int numTroopsOfCountry) {
    this.gamePaneController.setNumTroops(countryName, numTroopsOfCountry);
  }

  /**
   * Called from GameHandler. Sets a number of troops on the specified territory. Sets troops left
   * to deploy for the player.
   *
   * @param countryName enum of the Territory
   * @param numTroopsOfCountry number of troops to be set on territory
   * @param numTroopsOfPlayer number of troops left to deploy
   */

  public void setTroopsOnTerritoryAndLeftOnGui(CountryName countryName, int numTroopsOfCountry,
      int numTroopsOfPlayer) {
    this.gamePaneController.setNumTroops(countryName, numTroopsOfCountry);
    this.gamePaneController.setAmountOfTroopsLeftToDeploy(numTroopsOfPlayer);
  }

  /**
   * Called from GameHandler. Moves troops from one territory toanother on GUI and updates their
   * number of troops.
   *
   * @param from CountryName of Territory from
   * @param to CountryName of Territory to
   * @param numberFrom updated troops in from Territory
   * @param numberTo updated troops in to Territory
   */

  public void moveTroopsFromTerritoryToOtherOnGui(CountryName from, CountryName to, int numberFrom,
      int numberTo) {
    this.gamePaneController.setNumTroops(from, numberFrom);
    this.gamePaneController.setNumTroops(to, numberTo);
  }

  /**
   * Opens the battle frame in GUI.
   *
   * @param battle Battle instance to initialize the Frame with.
   */

  public void openBattleFrameOnGui(Battle battle) {
    this.gamePaneController.openBattleFrame(battle);
  }

  /** Closes the battle frame. */

  public void endBattleOnGui() {
    this.gamePaneController.closeBattleFrame();
  }

  /**
   * Risk cards have been turned in scuccessfully. Update Cards avaible for player on GUI and troops
   * left to deploy.
   *
   * @param card Updated list with cards avaiable to the player
   * @param idOfPlayer ID of player that turned in cards
   * @param bonusTroops Bonus troops received
   */

  public void riskCardsTurnedInSuccessOnGui(ArrayList<Card> card, int idOfPlayer, int bonusTroops) {
    this.gamePaneController.setAmountOfTroopsLeftToDeploy(bonusTroops);
    this.gamePaneController.setPlayerOnGUI(idOfPlayer, card);
    for (Card c : card) {
      System.out.println(c.toString());
    }
  }

  /**
   * Sets Territories disabled. And "Points up" a country. Used during fortifying and attacking.
   *
   * @param countryName Country to point up
   * @param unreachableCountries List with unreachable Territories to disable
   */

  public void selectTerritoryAndSetDisabledTerritoriesOnGui(CountryName countryName,
      ArrayList<CountryName> unreachableCountries) {
    this.gamePaneController.pointUpCountry(countryName);
    for (CountryName country : unreachableCountries) {
      this.gamePaneController.deactivateCountry(country);
    }
  }

  /**
   * Updates the in game ranks of all player.
   *
   * @param ranks Array with the ranks of player in the order of Players List in the Lobby.
   */

  public void updateRanksOnGui(int[] ranks) {
    this.gamePaneController.setPlayersRanking(ranks);
  }

  /**
   * Ends the game and displays the End Game Podium.
   *
   * @param podium List with the Player. Startting from Winner.
   */

  public void gameIsOverOnGui(ArrayList<Player> podium) {
    this.gamePaneController.endGame(podium);
  }

  /**
   * Displays a hint on the game pane GUI.
   * 
   * @param title of the tutorial pane
   * @param hint enum for the type of hint
   */

  public void showTutorialPaneOnGui(String title, Hint hint) {
    System.out.println("tut calle in singleplayer handler " + title);
    this.gamePaneController.showTutorialsPane(title, hint);
  }

  /** Ends the tutorial and sends the player to main menu. */

  public void endTutorialOnGui() {
    this.gamePaneController.endTutorial();
  }

}
