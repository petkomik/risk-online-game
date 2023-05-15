package game.logic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.models.Card;
import game.models.CountryName;
import game.models.Difficulty;
import game.models.Lobby;
import game.models.Player;
import game.models.PlayerAi;
import game.models.PlayerSingle;
import game.models.Territory;
import game.state.GameState;
import game.state.Period;
import general.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class includes tests for the AI Logic.
 *
 * @author majda
 *
 */

class AiLogicTest {

  private GameState gameState;
  private PlayerSingle player1;
  private PlayerAi player2;
  private HashMap<CountryName, Territory> territories;

  @BeforeEach
  void setUp() throws Exception {
    // Set up a new GameState and Player object before each test
    player1 = new PlayerSingle("Player 1", 0);
    player2 = new PlayerAi("Player 2", 1, Difficulty.HARD);
    Lobby lobby = new Lobby();
    ArrayList<Player> playerList = new ArrayList<>();
    playerList.add(player1);
    playerList.add(player2);
    lobby.setPlayersJoined(playerList);
    gameState = new GameState(lobby);
    gameState.setCurrentPlayer(1);
    gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
    gameState.setInitialTroops(10);
    territories = gameState.getTerritories();
  }


  @Test
  void testChooseTerritoryToInitialClaim() {
    gameState.getTerritories().get(CountryName.Alaska).setOwnedByPlayer(player2);
    CountryName tmp = AiLogic.chooseTerritoryToInitialClaim(gameState, player2);
    assertTrue(tmp == CountryName.Kamchatka || tmp == CountryName.NorthwestTerritory
        || tmp == CountryName.Alberta);

    territories.get(CountryName.Alaska).setOwnedByPlayer(null);
    territories.get(CountryName.Ontario).setOwnedByPlayer(player2);
    territories.get(CountryName.Alberta).setOwnedByPlayer(player1);
    territories.get(CountryName.NorthwestTerritory).setOwnedByPlayer(player1);
    territories.get(CountryName.Greenland).setOwnedByPlayer(player1);
    territories.get(CountryName.Quebec).setOwnedByPlayer(player1);
    territories.get(CountryName.EasternUnitedStates).setOwnedByPlayer(player1);
    territories.get(CountryName.WesternUnitedStates).setOwnedByPlayer(player1);
    tmp = AiLogic.chooseTerritoryToInitialClaim(gameState, player2);
    assertTrue(tmp == CountryName.Alaska || tmp == CountryName.CentralAmerica
        || tmp == CountryName.Iceland);
  }

  @Test
  void testChooseTerritoryToInitialReinforce() {
    for (Territory t : territories.values()) {
      if (!(t.getCountryName() == CountryName.Ukraine || t.getCountryName() == CountryName.Siberia
          || t.getCountryName() == CountryName.Brazil
          || t.getCountryName() == CountryName.SouthAfrica
          || t.getCountryName() == CountryName.Argentina)) {
        t.setOwnedByPlayer(player1);
      }
    }

    territories.get(CountryName.Ukraine).setOwnedByPlayer(player2);
    territories.get(CountryName.Siberia).setOwnedByPlayer(player2);
    territories.get(CountryName.Brazil).setOwnedByPlayer(player2);
    territories.get(CountryName.SouthAfrica).setOwnedByPlayer(player2);
    territories.get(CountryName.Argentina).setOwnedByPlayer(player2);


    CountryName tmp = AiLogic.chooseTerritoryToInitialReinforce(gameState, player2);
    assertTrue(
        tmp == CountryName.Ukraine || tmp == CountryName.Siberia || tmp == CountryName.Brazil);
  }

  @Test
  void testChooseTerritoryToReinforce() {
    for (Territory t : territories.values()) {
      if (!(t.getCountryName() == CountryName.Ukraine || t.getCountryName() == CountryName.Siberia
          || t.getCountryName() == CountryName.Brazil
          || t.getCountryName() == CountryName.SouthAfrica)) {
        t.setOwnedByPlayer(player1);
      }
    }
    territories.get(CountryName.Ukraine).setOwnedByPlayer(player2);
    territories.get(CountryName.Siberia).setOwnedByPlayer(player2);
    territories.get(CountryName.Brazil).setOwnedByPlayer(player2);
    territories.get(CountryName.SouthAfrica).setOwnedByPlayer(player2);



    territories.get(CountryName.Ukraine).setNumberOfTroops(10);
    territories.get(CountryName.Siberia).setNumberOfTroops(7);
    territories.get(CountryName.Brazil).setNumberOfTroops(2);

    gameState.getPlayerTroopsLeft().replace(player2.getId(), 5);

    Pair<CountryName, Integer> tmp = AiLogic.chooseTerritoryToReinforce(gameState, player2);
    assertTrue(tmp.getKey() == CountryName.Brazil && tmp.getValue().equals(5));
  }

  @Test
  void testChooseTerritoryPairAttack() {
    for (Territory t : territories.values()) {
      if (!(t.getCountryName() == CountryName.Ukraine
          || t.getCountryName() == CountryName.SouthAfrica
          || t.getCountryName() == CountryName.EasternAustralia
          || t.getCountryName() == CountryName.EasternUnitedStates
          || t.getCountryName() == CountryName.Brazil)) {
        t.setOwnedByPlayer(player1);
        t.setNumberOfTroops(Integer.MAX_VALUE);
      }
    }

    territories.get(CountryName.Ukraine).setOwnedByPlayer(player2);

    territories.get(CountryName.SouthAfrica).setOwnedByPlayer(player2);
    territories.get(CountryName.EasternAustralia).setOwnedByPlayer(player2);
    territories.get(CountryName.EasternUnitedStates).setOwnedByPlayer(player2);
    territories.get(CountryName.Brazil).setOwnedByPlayer(player2);

    territories.get(CountryName.Ukraine).setNumberOfTroops(10);
    territories.get(CountryName.EasternUnitedStates).setNumberOfTroops(7);
    territories.get(CountryName.Brazil).setNumberOfTroops(2);
    // territories.get(CountryName.SouthAfrica).setNumberOfTroops(2);
    // territories.get(CountryName.EasternAustralia).setOwnedByPlayer(player2);

    territories.get(CountryName.Ural).setNumberOfTroops(4);
    territories.get(CountryName.Afghanistan).setNumberOfTroops(7);
    territories.get(CountryName.MiddleEast).setNumberOfTroops(2);

    Pair tmp = AiLogic.chooseTerritoryPairAttack(gameState, player2);
    assertTrue(tmp.getKey() == CountryName.Ukraine && tmp.getValue() == CountryName.MiddleEast);
  }

  @Test
  void testChooseTroopsToSendToConqueredTerritory() {
    for (Territory t : territories.values()) {
      t.setOwnedByPlayer(player1);
    }
    territories.get(CountryName.Ukraine).setOwnedByPlayer(player2);
    territories.get(CountryName.Afghanistan).setOwnedByPlayer(player2);
    territories.get(CountryName.MiddleEast).setOwnedByPlayer(player2);
    territories.get(CountryName.SouthernEurope).setOwnedByPlayer(player2);
    territories.get(CountryName.NorthernEurope).setOwnedByPlayer(player2);
    territories.get(CountryName.Scandinavia).setOwnedByPlayer(player2);

    territories.get(CountryName.Ural).setOwnedByPlayer(player2);

    territories.get(CountryName.Ukraine).setNumberOfTroops(15);

    int tmp = AiLogic.chooseTroopsToSendToConqueredTerritory(territories.get(CountryName.Ukraine),
        territories.get(CountryName.Ural), player2);
    assertTrue(tmp == 14);

    for (Territory t : territories.values()) {
      t.setOwnedByPlayer(player1);
    }
    territories.get(CountryName.Ukraine).setOwnedByPlayer(player2);
    territories.get(CountryName.Afghanistan).setOwnedByPlayer(player2);
    territories.get(CountryName.China).setOwnedByPlayer(player2);
    territories.get(CountryName.Siberia).setOwnedByPlayer(player2);

    territories.get(CountryName.Ural).setOwnedByPlayer(player2);

    tmp = AiLogic.chooseTroopsToSendToConqueredTerritory(territories.get(CountryName.Ukraine),
        territories.get(CountryName.Ural), player2);
    assertTrue(tmp == 1);

    for (Territory t : territories.values()) {
      t.setOwnedByPlayer(player1);
    }

    territories.get(CountryName.Ukraine).setOwnedByPlayer(player2);
    territories.get(CountryName.Ural).setOwnedByPlayer(player2);

    territories.get(CountryName.Siberia).setNumberOfTroops(20);;
    territories.get(CountryName.Scandinavia).setNumberOfTroops(18);;

    tmp = AiLogic.chooseTroopsToSendToConqueredTerritory(territories.get(CountryName.Ukraine),
        territories.get(CountryName.Ural), player2);
    assertTrue(tmp == 7);
  }

  void testChooseTerritoriesPairFortify() {
    for (Territory t : territories.values()) {
      t.setOwnedByPlayer(player1);
    }
    territories.get(CountryName.Ukraine).setOwnedByPlayer(player2);
    territories.get(CountryName.Ural).setOwnedByPlayer(player2);
    territories.get(CountryName.Afghanistan).setOwnedByPlayer(player2);
    territories.get(CountryName.MiddleEast).setOwnedByPlayer(player2);
    territories.get(CountryName.Scandinavia).setOwnedByPlayer(player2);
    territories.get(CountryName.NorthernEurope).setOwnedByPlayer(player2);
    territories.get(CountryName.SouthernEurope).setOwnedByPlayer(player2);

    territories.get(CountryName.Ontario).setOwnedByPlayer(player2);
    territories.get(CountryName.Greenland).setOwnedByPlayer(player2);
    territories.get(CountryName.Quebec).setOwnedByPlayer(player2);
    territories.get(CountryName.EasternUnitedStates).setOwnedByPlayer(player2);
    territories.get(CountryName.WesternUnitedStates).setOwnedByPlayer(player2);
    territories.get(CountryName.Alberta).setOwnedByPlayer(player2);
    territories.get(CountryName.NorthwestTerritory).setOwnedByPlayer(player2);

    territories.get(CountryName.Brazil).setOwnedByPlayer(player2);
    territories.get(CountryName.Argentina).setOwnedByPlayer(player2);
    territories.get(CountryName.Peru).setOwnedByPlayer(player2);
    territories.get(CountryName.Venezuela).setOwnedByPlayer(player2);

    territories.get(CountryName.Mongolia).setOwnedByPlayer(player2);

    territories.get(CountryName.Ukraine).setNumberOfTroops(7);
    territories.get(CountryName.Ontario).setNumberOfTroops(5);
    territories.get(CountryName.Brazil).setNumberOfTroops(14);


    Pair tmp = AiLogic.chooseTerritoriesPairFortify(gameState, player2);
    assertTrue(tmp.getKey() == CountryName.Brazil && tmp.getValue() == CountryName.Mongolia);
  }

  @Test
  void testGetRiskCardsTurnIn() {
    ArrayList<Card> cards = new ArrayList<>();
    Card card1 = new Card(true, 0);
    Card card2 = new Card(CountryName.Congo, 1, Parameter.territoryPNGdir + "africa-congo.png");
    Card card3 = new Card(CountryName.Egypt, 2, Parameter.territoryPNGdir + "africa-egypt.png");
    Card card4 =
        new Card(CountryName.Alaska, 3, Parameter.territoryPNGdir + "northamerica-alaska.png");
    Card card5 = new Card(CountryName.CentralAmerica, 4,
        Parameter.territoryPNGdir + "northamerica-centralamerica.png");
    Card card7 =
        new Card(CountryName.Brazil, 5, Parameter.territoryPNGdir + "southamerica-brazil.png");

    cards.add(card1);
    cards.add(card2);
    cards.add(card3);
    cards.add(card4);
    cards.add(card5);
    gameState.getRiskCardsInPlayers().put(player2.getId(), cards);

    List<String> tmp = AiLogic.getRiskCardsTurnIn(gameState, player2.getId());
    assertNotNull(tmp);

    ArrayList<Card> list = new ArrayList<>();
    int i = 0;
    for (String s : tmp) {
      if (s.equals("Joker")) {
        list.add(new Card(true, i));
      } else {
        list.add(
            new Card(CountryName.valueOf(s), i, Parameter.territoryPNGdir + "africa-congo.png"));
      }
      i++;
    }

    assertTrue((list.stream().allMatch(o -> o.getCardSymbol() == list.get(0).getCardSymbol()))
        || (list.stream().map(Card::getCardSymbol).distinct().collect(Collectors.toSet())
            .equals(Set.of(1, 5, 10))
            || (list.stream().map(Card::getCardSymbol).reduce(0, (a, b) -> a + b) < 0)));


  }
}
