package game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.logic.Logic;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import game.models.PlayerSingle;
import game.models.Territory;
import gameState.GameState;
import gameState.Period;
import gameState.Phase;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing the game logic.
 *
 * @author srogalsk
 *
 */

public class GameLogicTest {

  private GameState gameState;
  private Player player1;
  private Player player3;
  private CountryName testTerritory;

  @BeforeEach
  void setUp() throws Exception {
    // Set up a new GameState and Player object before each test
    player1 = new PlayerSingle("Player 1", 0);
    player3 = new PlayerSingle("Player 3", 2);
    Lobby lobby = new Lobby();
    ArrayList<Player> playerList = new ArrayList<>();
    playerList.add(player3);
    playerList.add(player1);
    lobby.setPlayersJoined(playerList);
    gameState = new GameState(lobby);
    gameState.setCurrentPlayer(0);
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    testTerritory = CountryName.Argentina; // choose a test territory
    gameState.setInitialTroops(10);
    gameState.getTerritories().get(testTerritory).setOwnedByPlayer(player1);

  }

  /**
   * Testing the claim territory method
   */
  @Test
  void testClaimTerritory() {
    gameState.getTerritories().get(testTerritory).setOwnedByPlayer(null);
    // Test claiming a territory when it is unclaimed
    boolean result = Logic.claimTerritory(player1, gameState, testTerritory);
    assertTrue(result);

    // Test claiming a territory when it is already owned by another player
    Player player2 = new PlayerSingle("Player 2", 1);
    gameState.getTerritories().get(testTerritory).setOwnedByPlayer(player2);
    result = Logic.claimTerritory(player1, gameState, testTerritory);
    assertFalse(result);

    // Test claiming a territory when it is owned by the same player
    testTerritory = CountryName.Afghanistan;
    gameState.getTerritories().get(testTerritory).setOwnedByPlayer(player1);
    result = Logic.claimTerritory(player1, gameState, testTerritory);
    assertFalse(result);

    // Test claiming a territory during the wrong game period
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    result = Logic.claimTerritory(player1, gameState, testTerritory);
    assertFalse(result);

    // Test claiming a territory when it is not the current player's turn
    Player player3 = gameState.getAlivePlayers().get(0);
    gameState.setCurrentPlayer(player3.getId());
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    result = Logic.claimTerritory(player1, gameState, testTerritory);
    assertFalse(result);
  }

  /**
   * Testing the initial deploy method
   */
  @Test
  void testCanInitialDeployTroopsToTerritory() {
    gameState.setCurrentGamePeriod(Period.INITIALDEPLOY);
    // Assert that player can initially deploy troops to territory
    assertTrue(Logic.canInitialDeployTroopsToTerritory(gameState, player1, testTerritory));

    // Assert that player cannot deploy troops if they have no troops left
    gameState.getPlayerTroopsLeft().put(player1.getId(), 0);
    assertFalse(Logic.canInitialDeployTroopsToTerritory(gameState, player1, testTerritory));
    gameState.getPlayerTroopsLeft().put(player1.getId(), 10);
    // Assert that player cannot deploy troops to a territory they don't own
    gameState.getTerritories().get(testTerritory).setOwnedByPlayer(new PlayerSingle("Player 2", 1));
    assertFalse(Logic.canInitialDeployTroopsToTerritory(gameState, player1, testTerritory));

    // Assert that player cannot deploy troops during the wrong game period
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    assertFalse(Logic.canInitialDeployTroopsToTerritory(gameState, player1, testTerritory));

    // Assert that a different player cannot deploy troops to the same territory
    gameState.setCurrentGamePeriod(Period.INITIALDEPLOY);
    assertFalse(Logic.canInitialDeployTroopsToTerritory(gameState, new PlayerSingle("Player 2", 1),
        testTerritory));
  }

  /**
   * Testing the reinforcement method
   */
  @Test
  void testCanReinforceTroopsToTerritory() {
    // Test valid reinforcement
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.REINFORCE);
    assertTrue(Logic.canReinforceTroopsToTerritory(gameState, player1, testTerritory));

    // Test invalid reinforcement due to different player
    assertFalse(Logic.canReinforceTroopsToTerritory(gameState, new PlayerSingle("Player 2", 1),
        testTerritory));

    // Test invalid reinforcement due to wrong game period
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    assertFalse(Logic.canReinforceTroopsToTerritory(gameState, player1, testTerritory));

    // Test invalid reinforcement due to territory not owned by player
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.getTerritories().get(testTerritory).setOwnedByPlayer(new PlayerSingle("Player 2", 1));
    assertFalse(Logic.canReinforceTroopsToTerritory(gameState, player1, testTerritory));

    // Test invalid reinforcement due to no troops left to deploy
    gameState.getTerritories().get(testTerritory).setOwnedByPlayer(player1);
    gameState.getPlayerTroopsLeft().put(player1.getId(), 0);
    assertFalse(Logic.canReinforceTroopsToTerritory(gameState, player1, testTerritory));

    // Test invalid reinforcement due to wrong turn phase
    gameState.getPlayerTroopsLeft().put(player1.getId(), 1);
    gameState.setCurrentTurnPhase(Phase.ATTACK);
    assertFalse(Logic.canReinforceTroopsToTerritory(gameState, player1, testTerritory));

  }

  /**
   * Testing the attack method
   */
  @Test
  void testCanAttack() {
    // Set up test data
    Territory attacker = gameState.getTerritories().get(testTerritory);
    Territory defender = gameState.getTerritories().get(CountryName.India);
    attacker.addNumberOfTroops(10);
    defender.addNumberOfTroops(5);
    gameState.getTerritories().put(attacker.getCountryName(), attacker);
    gameState.getTerritories().put(defender.getCountryName(), defender);
    attacker.getNeighboringTerritories().add(defender);
    defender.getNeighboringTerritories().add(attacker);

    // Test cases
    assertFalse(Logic.canAttack(gameState, attacker, attacker)); // Cannot attack itself
    assertTrue(Logic.canAttack(gameState, attacker, defender)); // Valid Attack
    attacker.setNumberOfTroops(1);
    assertFalse(Logic.canAttack(gameState, attacker, defender)); // Not enough troops to attack
    attacker.setNumberOfTroops(2);
    assertTrue(Logic.canAttack(gameState, attacker, defender)); // Valid attack
    defender.setOwnedByPlayer(player1);
    assertFalse(Logic.canAttack(gameState, attacker, defender)); // Cannot attack own territory
  }

  /**
   * Testing the fortification method
   */
  @Test
  void testPlayerForitfyConfirmedIsOk() {
    // Set Up
    CountryName from = CountryName.Argentina;
    CountryName to = CountryName.Brazil;
    int troopsNumber = 2;
    gameState.setCurrentPlayer(player1.getId());
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.FORTIFY);
    Territory fromTerritory = gameState.getTerritories().get(from);
    fromTerritory.setOwnedByPlayer(player1);
    fromTerritory.setNumberOfTroops(4);
    Territory toTerritory = gameState.getTerritories().get(to);
    toTerritory.setOwnedByPlayer(player1);
    // Test Cases
    // Assert that player can fortify from Argentina to Brazil with 2 troops
    assertTrue(Logic.playerForitfyConfirmedIsOk(gameState, player1, from, to, troopsNumber));

    // Assert that player cannot fortify during other periods or phases
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    assertFalse(Logic.playerForitfyConfirmedIsOk(gameState, player1, from, to, troopsNumber));
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.ATTACK);
    assertFalse(Logic.playerForitfyConfirmedIsOk(gameState, player1, from, to, troopsNumber));
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.FORTIFY);

    // Assert that player cannot fortify to a territory not owned by them
    toTerritory.setOwnedByPlayer(new PlayerSingle("Other Player", 1));
    assertFalse(Logic.playerForitfyConfirmedIsOk(gameState, player1, from, to, troopsNumber));
    toTerritory.setOwnedByPlayer(player1);

    // Assert that player cannot fortify from a territory they do not own
    fromTerritory.setOwnedByPlayer(new PlayerSingle("Other Player", 1));
    assertFalse(Logic.playerForitfyConfirmedIsOk(gameState, player1, from, to, troopsNumber));
    fromTerritory.setOwnedByPlayer(player1);

    // Assert that player cannot fortify with more troops than available in the from
    // territory
    fromTerritory.setOwnedByPlayer(player1);
    fromTerritory.setNumberOfTroops(1);
    assertFalse(Logic.playerForitfyConfirmedIsOk(gameState, player1, from, to, troopsNumber));
  }

  /**
   * Testing the attack confirmation method
   */
  @Test
  void testPlayerAttackAttackConfirmedIsOK() {
    // Test case where all conditions are satisfied and attack is confirmed
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.ATTACK);
    gameState.setCurrentPlayer(player1.getId());
    gameState.setLastAttackingCountry(CountryName.Alaska);
    gameState.getTerritories().get(CountryName.Alaska)
        .setOwnedByPlayer(gameState.getCurrentPlayer());
    gameState.getTerritories().get(CountryName.NorthwestTerritory).setOwnedByPlayer(player3);
    gameState.getTerritories().get(CountryName.Alaska).setNumberOfTroops(4);
    gameState.getTerritories().get(CountryName.NorthwestTerritory).setNumberOfTroops(1);
    assertTrue(Logic.playerAttackAttackConfirmedIsOk(gameState, player1.getId(),
        CountryName.NorthwestTerritory, 2));

    // Test case where it is not the main period
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    assertFalse(
        Logic.playerAttackAttackConfirmedIsOk(gameState, 1, CountryName.NorthwestTerritory, 2));
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.ATTACK);

    // Test case where it is not the attack phase
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.REINFORCE);
    assertFalse(
        Logic.playerAttackAttackConfirmedIsOk(gameState, 1, CountryName.NorthwestTerritory, 2));
    gameState.setCurrentGamePeriod(Period.MAINPERIOD);
    gameState.setCurrentTurnPhase(Phase.ATTACK);

    // Test case where the player ID is different from the current player's ID
    gameState.setCurrentTurnPhase(Phase.ATTACK);
    assertFalse(
        Logic.playerAttackAttackConfirmedIsOk(gameState, 2, CountryName.NorthwestTerritory, 2));
  }
}
