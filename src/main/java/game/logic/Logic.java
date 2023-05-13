package game.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import game.models.Battle;
import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import game.models.Territory;
import gameState.GameState;
import gameState.Period;
import gameState.Phase;

/**
 * This class contains the Rule Book and other game Logic. Consist of static methods that confirm
 * that an action can be performed at the current GameState
 *
 * @author pmikov
 *
 */

public class Logic {

  /**
   * Returns the correct initial number of troops for each player.
   *
   * @param gamestate the current gamestate
   * @return int the number of troops each player receives
   */

  public static int setInitialTroopsSize(GameState gamestate) {
    int troopsSize = 0;
    switch (gamestate.getPlayers().size()) {
      case 2:
        troopsSize = 40;
        break;
      case 3:
        troopsSize = 35;
        break;
      case 4:
        troopsSize = 30;
        break;
      case 5:
        troopsSize = 25;
        break;
      case 6:
        troopsSize = 20;
        break;
      default:
        troopsSize = -1;
        break;
    }

    return troopsSize;
  }

  /**
   * Generates dice values for every player.
   *
   * @param gameState current gameState for the game
   * @return Hashmap maps player id to dice value
   * 
   */

  public static HashMap<Integer, Integer> diceThrowToDetermineTheBeginner(GameState gameState) {
    HashMap<Integer, Integer> playersDiceThrown = new HashMap<Integer, Integer>();
    for (int idPlayer : gameState.getPlayers().keySet()) {
      int diceNumber = getRandomDiceNumber();
      playersDiceThrown.put(idPlayer, diceNumber);
    }
    return playersDiceThrown;
  }

  /**
   * Return the first player for country possession, based on dices thrown.
   *
   * @param gameState current gameState
   * @return Id of the first player
   */

  public static Integer getFirstPlayer(GameState gameState) {
    HashMap<Integer, Integer> playerDice = gameState.getPlayersDiceThrown();
    Integer firstPlayer = null;
    int maxDice = 0;
    for (Player player : gameState.getAlivePlayers()) {
      int playerId = player.getId();
      if (playerDice.get(playerId) > maxDice) {
        firstPlayer = playerId;
        maxDice = playerDice.get(playerId);
      }
    }
    return firstPlayer;
  }

  /**
   * Checks whether or not the give player can throw a dice.
   *
   * @param idOfPlayer that wants to throw a dice
   * @param gamestate current gameState
   * @return boolean
   * 
   */

  public static boolean canThrowInitialDice(int idOfPlayer, GameState gamestate) {
    if (gamestate.getCurrentPlayer().getId() == idOfPlayer) {
      if (gamestate.getCurrentGamePeriod().equals(Period.DICETHROW)) {
        return true;
      }
    }
    return false;
  }

  public static int getRandomDiceNumber() {
    return (int) (Math.floor(Math.random() * 5) + 1);
  }

  /**
   * Checks if a player can currently claim a given territor.
   *
   * @param player Player instance that wants to claim the territory
   * @param gameState current gameState
   * @param territory the CountryName enum of the territory to be claimed
   * @return boolena with descision
   * 
   */

  public static boolean claimTerritory(Player player, GameState gameState, CountryName territory) {
    if (gameState.getCurrentPlayer().equals(player)) {
      if (gameState.getCurrentGamePeriod().equals(Period.COUNTRYPOSESSION)) {
        if (gameState.getTerritories().get(territory).getOwnedByPlayer() == null) {
          return true;
          // if(gameState.getAlivePlayers().get((gameState.getAlivePlayers().indexOf(player) +
          // gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
          // == Logic.getFirstPlayer(gameState)) {
          // if(gameState.getPlayerTroopsLeft().get(player.getID()) >
          // gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
          // (gameState.getAlivePlayers().indexOf(player) +
          // gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
          // )) {
          // return true;
          // }
          // } else {
          // if(gameState.getPlayerTroopsLeft().get(player.getID()) ==
          // gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
          // (gameState.getAlivePlayers().indexOf(player) +
          // gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
          // )) {
          // return true;
          // }
          // }
        }
      }
    }
    return false;
  }

  /**
   * Checks if all territories have been claimed.
   *
   * @param gameState curent gameState of game
   * @return boolean with decsision
   */

  public static boolean allTerritoriesClaimed(GameState gameState) {

    for (Territory territory : gameState.getTerritories().values()) {
      if (territory.getOwnedByPlayer() == null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if Player can initial deploy to Territory.
   *
   * @param gameState current GameState
   * @param player Player instance that wants to deploy
   * @param territory CountryName enum of the Territory
   * @return boolean with descision
   */


  public static boolean canInitialDeployTroopsToTerritory(GameState gameState, Player player,
      CountryName territory) {
    if (gameState.getCurrentPlayer().getId() == player.getId()) {
      if (gameState.getCurrentGamePeriod().equals(Period.INITIALDEPLOY)) {
        if (gameState.getTerritories().get(territory).getOwnedByPlayer().getId() == player
            .getId()) {
          if (gameState.getPlayerTroopsLeft().get(player.getId()) >= 1) {
            return true;
            // if(gameState.getAlivePlayers().get((gameState.getAlivePlayers().indexOf(player) +
            // gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
            // == Logic.getFirstPlayer(gameState)) {
            // if(gameState.getPlayerTroopsLeft().get(player.getID()) >
            // gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
            // (gameState.getAlivePlayers().indexOf(player) +
            // gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
            // )) {
            // return true;
            // }
            // } else {
            // if(gameState.getPlayerTroopsLeft().get(player.getID()) ==
            // gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
            // (gameState.getAlivePlayers().indexOf(player) +
            // gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
            // )) {
            // return true;
            // }
            // }
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks if Player can reinforce to Territory.
   *
   * @param gameState current GameState
   * @param player Player instance that wants to deploy
   * @param territory CountryName enum of the Territory
   * @return boolean with descision
   */

  public static boolean canReinforceTroopsToTerritory(GameState gameState, Player player,
      CountryName territory) {
    if (gameState.getCurrentPlayer().equals(player)) {
      if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
        if (gameState.getTerritories().get(territory).getOwnedByPlayer().getId() == player
            .getId()) {
          if (gameState.getPlayerTroopsLeft().get(player.getId()) > 0) {
            if (gameState.getCurrentTurnPhase().equals(Phase.REINFORCE)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks if Player can currently attack a given Territory from given Territory.
   *
   * @param gameState current GameState
   * @param attacker Territory to attack from
   * @param defender Territory to attack
   * @return booelan with descision
   */

  public static boolean canAttack(GameState gameState, Territory attacker, Territory defender) {
    if (gameState.getTerritories().get(attacker.getCountryName()).getNeighboringTerritories()
        .contains(defender)) {
      if (!gameState.getTerritories().get(attacker.getCountryName()).getOwnedByPlayer()
          .equals(gameState.getTerritories().get(defender.getCountryName()).getOwnedByPlayer())) {
        if (gameState.getTerritories().get(attacker.getCountryName()).getNumberOfTroops() > 1) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if a player can turn in given set of Risk Cards.
   *
   * @param cards List with instances of the cards
   * @param player Player instance that wants to turn in
   * @param gameState current gameState
   * @return boolean with descision
   */

  public static boolean turnInRiskCards(List<Card> cards, Player player, GameState gameState) {
    if (gameState.getCurrentPlayer().equals(player)) {
      if (cards != null && cards.size() == 3) {
        if (gameState.getCurrentTurnPhase().equals(Phase.REINFORCE)) {
          if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
            if (gameState.getRiskCardsInPlayers().get(player.getId()).containsAll(cards)) {
              if ((cards.stream().allMatch(o -> o.getCardSymbol() == cards.get(0).getCardSymbol()))
                  || (cards.stream().map(Card::getCardSymbol).distinct().collect(Collectors.toSet())
                      .equals(Set.of(1, 5, 10))
                      || (cards.stream().map(Card::getCardSymbol).reduce(0,
                          (a, b) -> a + b) < 0))) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /** Checks if the Deploy period is Over. */

  public static boolean isDeployPeriodOver(GameState gameState) {
    for (int leftOver : gameState.getPlayerTroopsLeft().values()) {
      if (leftOver != 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Calculates the amount of troops players receive at the beginning off their turn.
   *
   * @param gameState current gameState
   * @return Hashmap that maps id of player to number of troops
   */

  public static HashMap<Integer, Integer> getTroopsReinforce(GameState gameState) {
    HashMap<Integer, Integer> troops = gameState.getPlayerTroopsLeft();

    for (Integer idPlayer : troops.keySet()) {
      troops.replace(idPlayer, 0);
    }

    for (Territory territory : gameState.getTerritories().values()) {
      troops.put(territory.getOwnedByPlayer().getId(),
          troops.get(territory.getOwnedByPlayer().getId()) + 1);
    }

    for (Integer idPlayer : troops.keySet()) {
      troops.replace(idPlayer, Math.max((troops.get(idPlayer) - troops.get(idPlayer) % 3) / 3, 3));
    }

    for (Continent continent : gameState.getContinents().keySet()) {
      int continentOwner = gameState.getTerritories()
          .get(gameState.getContinents().get(continent).get(0)).getOwnedByPlayer().getId();
      boolean success = true;
      for (CountryName countryName : gameState.getContinents().get(continent)) {
        Territory territory = gameState.getTerritories().get(countryName);
        if (continentOwner != territory.getOwnedByPlayer().getId()) {
          success = false;
          break;
        }
      }
      if (success) {
        switch (continent) {
          case Africa:
            troops.replace(continentOwner, troops.get(continentOwner) + 3);
            break;
          case Asia:
            troops.replace(continentOwner, troops.get(continentOwner) + 7);
            break;
          case Australia:
            troops.replace(continentOwner, troops.get(continentOwner) + 2);
            break;
          case Europe:
            troops.replace(continentOwner, troops.get(continentOwner) + 5);
            break;
          case NorthAmerica:
            troops.replace(continentOwner, troops.get(continentOwner) + 5);
            break;
          case SouthAmerica:
            troops.replace(continentOwner, troops.get(continentOwner) + 2);
            break;
          default:
            break;
        }
      }
    }
    return troops;
  }

  /** Checks if a given player can currently attack from a given country. */

  public static boolean playerAttackingFromCountry(CountryName country, int idOfPlayer,
      GameState gameState) {
    if (gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
      if (gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
        if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
          if (gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
            if (gameState.getTerritories().get(country).getOwnedByPlayer()
                .equals(gameState.getCurrentPlayer())) {
              if (gameState.getTerritories().get(country).getNumberOfTroops() > 1) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /** Checks if a given player can currently attack a given country. */

  public static boolean playerAttackingCountry(CountryName country, int idOfPlayer,
      GameState gameState) {
    if (gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
      if (gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
        if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
          if (gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
            if (!gameState.getTerritories().get(country).getOwnedByPlayer()
                .equals(gameState.getCurrentPlayer())) {
              if (gameState.getLastAttackingCountry() != null) {
                if (gameState.getTerritories().get(gameState.getLastAttackingCountry())
                    .getNeighboringTerritories()
                    .contains(gameState.getTerritories().get(country))) {
                  return true;
                }
              }
            }
          }
        }
      }
    }
    return false;
  }

  /** Calculates a List with unreachable Territories for a Player attacking from country. */

  public static ArrayList<CountryName> getUnreachableTerritories(CountryName country,
      int idOfPlayer, GameState gameState) {
    ArrayList<CountryName> unreachableCountries = new ArrayList<CountryName>();
    unreachableCountries.addAll(gameState.getTerritories().keySet());
    unreachableCountries.removeIf(x -> gameState.getTerritories().get(x).getOwnedByPlayer()
        .equals(gameState.getPlayers().get(idOfPlayer))
        || !gameState.getTerritories().get(x).getNeighboringTerritories()
            .contains(gameState.getTerritories().get(country)));
    return unreachableCountries;
  }

  /** Checks if a player can fortify to or from a country. */

  public static boolean playerFortifyingPosition(CountryName country, int idOfPlayer,
      GameState gameState) {
    if (gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
      if (gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
        if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
          if (gameState.getCurrentTurnPhase().equals(Phase.FORTIFY)) {
            if (gameState.getTerritories().get(country).getOwnedByPlayer()
                .equals(gameState.getCurrentPlayer())) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  /** Checks if a player can currently end a given phase. */

  public static boolean playerEndsPhase(Phase phase, int idOfPlayer, GameState gameState) {
    if (gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
      if (gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
        if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
          if (gameState.getCurrentTurnPhase().equals(phase)) {
            if (phase.equals(Phase.REINFORCE)) {
              return gameState.getPlayerTroopsLeft().get(idOfPlayer) == 0
                  && gameState.getRiskCardsInPlayers().get(idOfPlayer).size() <= 5;
            }
            return true;
          }
        }
      }
    }

    return false;
  }

  /** Checks if a player can currently end a own turn. */

  public static boolean playerEndsTurn(Period period, int idOfPlayer, GameState gameState) {
    if (gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
      if (gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
        if (!gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
          return true;
        }
      }
    }

    return false;
  }

  /** Checks if the Dice Throw Period is Over. */

  public static boolean isDiceThrowPeriodOver(GameState gameState, int idOfPlayer) {
    if (gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
      if (gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
        if (gameState.getCurrentGamePeriod().equals(Period.DICETHROW)) {
          if (gameState.getAlivePlayers().get(gameState.getAlivePlayers().size() - 1)
              .getId() == idOfPlayer) {
            return true;

          }
        }
      }
    }

    return false;
  }

  /**
   * Checks if the a player can currently Fortify from CountryName to CountryName with a
   * troopsNumber amount of troops.
   *
   * @param gameState current gameState
   * @param player Player that is fortifying
   * @param from CountryName fortify from
   * @param to CountryName fortify to
   * @param troopsNumber number of troops to fortify with
   * @return boolean with descision
   */

  public static boolean playerForitfyConfirmedIsOk(GameState gameState, Player player,
      CountryName from, CountryName to, int troopsNumber) {
    if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
      if (gameState.getCurrentTurnPhase().equals(Phase.FORTIFY)) {
        if (gameState.getCurrentPlayer().getId() == player.getId()) {
          if (gameState.getTerritories().get(from) != null && gameState.getTerritories().get(from)
              .getOwnedByPlayer().equals(gameState.getCurrentPlayer())) {
            if (gameState.getTerritories().get(to).getOwnedByPlayer()
                .equals(gameState.getCurrentPlayer())) {
              if (gameState.getTerritories().get(from).getNumberOfTroops() >= troopsNumber) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /** Checks if player can currently attack a Territoy with a number of Troops. */

  public static boolean playerAttackAttackConfirmedIsOk(GameState gameState, int idOfPlayer,
      CountryName attacked, int numTroops) {
    if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
      if (gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
        if (gameState.getCurrentPlayer().getId() == idOfPlayer) {
          if (gameState.getTerritories().get(gameState.getLastAttackingCountry()).getOwnedByPlayer()
              .equals(gameState.getCurrentPlayer())) {
            if (!gameState.getTerritories().get(attacked).getOwnedByPlayer()
                .equals(gameState.getCurrentPlayer())) {
              if (gameState.getTerritories().get(gameState.getLastAttackingCountry())
                  .getNumberOfTroops() >= numTroops) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /** Checks if player can currently colonise a Territoy with a number of Troops. */

  public static boolean playerAttackColoniseConfirmedIsOk(GameState gameState, int idOfPlayer,
      CountryName attacking, CountryName attacked, int numTroops) {
    if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
      if (gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
        if (gameState.getCurrentPlayer().getId() == idOfPlayer) {
          if (gameState.getTerritories().get(attacking).getOwnedByPlayer()
              .equals(gameState.getCurrentPlayer())) {
            if (gameState.getTerritories().get(attacked).getOwnedByPlayer()
                .equals(gameState.getCurrentPlayer())) {
              if (gameState.getTerritories().get(attacking).getNumberOfTroops() >= numTroops) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /** Checks if player can currently reinforce a Territoy with a number of Troops. */

  public static boolean playerReinforceConfirmedIsOk(GameState gameState, int idPlayer,
      CountryName country, int numTroops) {
    if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
      if (gameState.getCurrentTurnPhase().equals(Phase.REINFORCE)) {
        if (gameState.getCurrentPlayer().getId() == idPlayer) {
          if (gameState.getTerritories().get(country).getOwnedByPlayer()
              .equals(gameState.getCurrentPlayer())) {
            if (gameState.getPlayerTroopsLeft().get(idPlayer) >= numTroops) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /** Checks if a Battle Dice Throw can currently be done. */

  public static boolean battleDiceThrowIsOk(GameState gameState) {
    if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
      if (gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
        if (gameState.getBattle() != null
            && gameState.getCurrentPlayer().getId() == gameState.getBattle().getAttackerId()) {
          if (gameState.getBattle().getTroopsInAttackAt() > 0
              && gameState.getBattle().getTroopsInAttackDf() > 0) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Updates the Battle instance of GameState with given new dice values appropriately changing
   * other info in the battle instance.
   *
   * @param gameState current GameState
   * @param diceValuesAt new dice values of Attacker
   * @param diceValuesDf new dice values of Defender
   * @return updated Battle instance
   */


  public static Battle battleDiceRollConfirmed(GameState gameState, int[] diceValuesAt,
      int[] diceValuesDf) {
    Battle battle = gameState.getBattle();
    int lastThrow = 0;
    int maxDf = Arrays.stream(diceValuesDf).max().getAsInt();
    int maxAt = Arrays.stream(diceValuesAt).max().getAsInt();

    if (maxAt > maxDf) {
      lastThrow++;
    } else {
      lastThrow--;
    }

    if (diceValuesDf.length == 2 && diceValuesAt.length > 1) {
      int nextDf = Arrays.stream(diceValuesDf).min().getAsInt();

      int minAt = Arrays.stream(diceValuesAt).min().getAsInt();
      int nextAt = Arrays.stream(diceValuesAt).sum() - minAt - maxAt;

      if (nextAt > nextDf) {
        lastThrow++;
      } else {
        lastThrow--;
      }
    }

    if (lastThrow < 0) {
      battle.setTroopsInAttackAt(battle.getTroopsInAttackAt() + lastThrow);
    } else if (lastThrow > 0) {
      battle.setTroopsInAttackDf(battle.getTroopsInAttackDf() - lastThrow);
    } else {
      battle.setTroopsInAttackAt(battle.getTroopsInAttackAt() - 1);
      battle.setTroopsInAttackDf(battle.getTroopsInAttackDf() - 1);
    }

    battle.setMaxDiceToThrow(Math.min(3, battle.getTroopsInAttackAt()));
    battle.setDefendingDice(Math.min(2, battle.getTroopsInAttackDf()));

    return battle;
  }

  /** Generates an array with random dice values. */

  public static int[] getBattleDiceValues(GameState gameState, boolean attacker) {
    int[] values;
    Random random = new Random();

    if (attacker) {
      values = new int[gameState.getBattle().getMaxDiceToThrow()];
    } else {
      values = new int[gameState.getBattle().getDefendingDice()];
    }

    for (int i = 0; i < values.length; i++) {
      int k = random.nextInt(5) + 1;
      values[i] = k;
    }
    return values;
  }

  public static boolean playerIsAlive(GameState gameState, int defenderId) {
    return gameState.getTerritories().values().stream()
        .anyMatch(x -> x.getOwnedByPlayer().getId() == defenderId);
  }

  public static boolean isGameOver(GameState gameState) {
    return gameState.getAlivePlayers().size() == 1;
  }

  /** Calculates the in-game ranks for all players and returns them as an array. */

  public static int[] getInGameRanks(GameState gameState, Lobby lobby) {
    HashMap<Integer, Integer> ranks = new HashMap<Integer, Integer>();
    ArrayList<Integer> ranksList = new ArrayList<Integer>();
    for (int plyId : gameState.getPlayers().keySet()) {
      int troopN = 0;
      int terrN = 0;
      for (CountryName country : gameState.getTerritories().keySet()) {
        if (gameState.getTerritories().get(country).getOwnedByPlayer().getId() == plyId) {
          terrN++;
          troopN += gameState.getTerritories().get(country).getNumberOfTroops();
        }
      }
      troopN += gameState.getPlayerTroopsLeft().get(plyId);
      ranks.put(plyId, troopN * terrN);
      ranksList.add((troopN * terrN) == 0 ? -1 : troopN * terrN);
    }

    Collections.sort(ranksList, Collections.reverseOrder());
    int[] rankArray = new int[lobby.getPlayerList().size()];

    for (int i = 0; i < lobby.getPlayerList().size(); i++) {
      rankArray[i] = ranksList.indexOf(ranks.get(lobby.getPlayerList().get(i).getId())) + 1;
    }

    return rankArray;
  }

  /** Gets an array of the countrynames as String and turns them into Card instances. */

  public static ArrayList<Card> arrayListFromStringsToCard(ArrayList<String> cards,
      GameState gameState, int idOfPlayer) {
    ArrayList<Card> returnList = new ArrayList<Card>();
    for (String s : cards) {
      if (s.equalsIgnoreCase("Joker")) {
        returnList.add(gameState.getRiskCardsInPlayers().get(idOfPlayer).stream()
            .filter(x -> x.isJoker()).collect(Collectors.toList()).get(0));
      } else {
        returnList.add(
            gameState.getRiskCardsInPlayers().get(idOfPlayer).stream().filter(x -> !x.isJoker())
                .filter(x -> x.getName().equals(CountryName.valueOf(s.replaceAll("[\n\r]", ""))))
                .collect(Collectors.toList()).get(0));
      }
    }

    return returnList;
  }

}
