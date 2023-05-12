package gameState;

import java.util.ArrayList;
import java.util.Collections;
import game.logic.AILogic;
import game.logic.Logic;
import game.models.Battle;
import game.models.Card;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import game.models.PlayerAI;
import game.models.Territory;
import general.AppController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;
import network.Client;

/**
 * This class manages player action, checks validity, 
 * updates GameState and repaint the Game Pane GUI.
 * To communicate back to the GUI this class calls
 * either a SinglePlayerHandler or a Client depending on GameType.
 * 
 * @author pmikov
 *
 */

public class GameHandler {

  private SinglePlayerHandler singlePlayerHandler;
  private GameState gameState;
  private Lobby lobby;
  private GameType gameType;
  private Client client;

  /**
   * Default Constructor. Initializes a new Handler for the Lobby.
   *
   * @param lobby Lobby instance for the game
   */
  
  public GameHandler(Lobby lobby) {
    this.gameState = new GameState(lobby);
    this.lobby = lobby;
    this.singlePlayerHandler = null;
    determineInitialDice();
    gameState.setInitialTroops(Logic.setInitialTroopsSize(this.gameState));
    gameState.setCurrentPlayer(lobby.getPlayerList().get(0).getID());
  }
  
  public void initSingleplayer(SinglePlayerHandler singlePlayerHandler) {
    this.singlePlayerHandler = singlePlayerHandler;
    this.gameType = GameType.SinglePlayer;
  }

  public void initMultiplayer(Client client) {
    this.client = client;
    this.gameType = GameType.Multiplayer;
  }

  public void determineInitialDice() {
    this.gameState.setPlayersDiceThrown(Logic.diceThrowToDetermineTheBeginner(gameState));
  }


  /**
   * Called from SinglePlayerHandler. Player Wants to throw dice.
   *
   * @param idOfPlayer
   */

  public void playerThrowsInitialDice(int idOfPlayer) {
    if (Logic.canThrowInitialDice(idOfPlayer, this.gameState)) {
      int i = this.gameState.getPlayersDiceThrown().get(idOfPlayer);
      switch (this.gameType) {
        case SinglePlayer:
          this.singlePlayerHandler.rollInitialDiceOnGUI(idOfPlayer, i);
          break;
        case Multiplayer:
          client.rollInitialDiceOnGUI(idOfPlayer, i);
          break;
        case Tutorial:
          break;
      }
    }
  }

  /*
   * gets called from GamePane, everytime a country is clicked decides what to do with the click,
   * depending on period and phase
   */

  public void setGameState(GameState gameStaten) {
    this.gameState = gameStaten;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  /**
   * Called from GUI.
   * Player clicks on Country. This method checks if it 
   * is a valid move and decides what to do with it. 
   *
   * @param idOfPlayer 	that clicked on a Territory
   * @param country	CountryName enum of Territroy clicked	
   */
  
  public void clickCountry(int idOfPlayer, CountryName country) {
    Player player = this.gameState.getPlayers().get(idOfPlayer);
    switch (this.gameState.getCurrentGamePeriod()) {
      case DICETHROW: // Map should be disabled during DICETHROW
        break;
      case COUNTRYPOSESSION:
        if (Logic.claimTerritory(player, this.gameState, country)) {
          this.gameState.setOwnedByTerritory(country, player);
          this.gameState.updateTroopsOnTerritory(country, 1);
          int numTroopsPlayer = this.gameState.getPlayerTroopsLeft().get(idOfPlayer) - 1;
          this.gameState.getPlayerTroopsLeft().replace(idOfPlayer, numTroopsPlayer);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.possesCountryOnGUI(country, idOfPlayer,
                  this.gameState.getTroopsLeftForCurrent());
              break;
            case Multiplayer:
              this.client.possesCountryOnGUI(country, idOfPlayer,
                  this.gameState.getTroopsLeftForCurrent());
              break;
            case Tutorial:
              break;
          }
        }
        break;
      case INITIALDEPLOY:
        if (Logic.canInitialDeployTroopsToTerritory(this.gameState, player, country)) {
          this.gameState.getTerritories().get(country).addNumberOfTroops(1);
          int numTroopsPlayer = this.gameState.getPlayerTroopsLeft().get(idOfPlayer) - 1;
          this.gameState.getPlayerTroopsLeft().replace(idOfPlayer, numTroopsPlayer);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGUI(country,
                  gameState.getTerritories().get(country).getNumberOfTroops(), numTroopsPlayer);
              break;
            case Multiplayer:
              this.client.setTroopsOnTerritoryAndLeftOnGUI(country,
                  gameState.getTerritories().get(country).getNumberOfTroops(), numTroopsPlayer);
              break;
            case Tutorial:
              break;
          }
        }
        break;
      case MAINPERIOD:
        switch (this.gameState.getCurrentTurnPhase()) {
          case REINFORCE:
            System.out.println("Reinforce Phase Country Clicked in GameHandler");
            if (Logic.canReinforceTroopsToTerritory(this.gameState, player, country)) {
              switch (this.gameType) {
                case SinglePlayer:
                    if(!this.gameState.getCurrentPlayer().isAI()) {
                	this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(country, 1,                	
                		this.gameState.getPlayerTroopsLeft().get(idOfPlayer), ChoosePane.REINFORCE);
                    }
                  break;
                case Multiplayer:
                  if(!this.gameState.getCurrentPlayer().isAI()) {
                      this.client.chooseNumberOfTroopsOnGUI(country, 1,
                              this.gameState.getPlayerTroopsLeft().get(idOfPlayer), ChoosePane.REINFORCE);
                  }
                  break;
                case Tutorial:
                  break;
              }
            } else {

            }
            break;
          case ATTACK:
            System.out.println("Attack Phase Country Clicked in GameHandler");
            if (Logic.playerAttackingFromCountry(country, idOfPlayer, this.gameState)) {
              ArrayList<CountryName> unreachableCountries =
                  Logic.getUnreachableTerritories(country, idOfPlayer, this.gameState);
              this.gameState.setLastAttackingCountry(country);
              System.out.println("Setting last attacking to " + country.toString());
              switch (this.gameType) {
                case SinglePlayer:
                  // this.singlePlayerHandler.selectTerritoryAndSetDisabledTerritoriesOnGUI(country,
                  // unreachableCountries);
                  break;
                case Multiplayer:
                  break;
                case Tutorial:
                  break;
              }
            } else if (Logic.playerAttackingCountry(country, idOfPlayer, this.gameState)) {
              switch (this.gameType) {
                case SinglePlayer:
                    if(!this.gameState.getCurrentPlayer().isAI()) {
                	this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(
                		country, 1, this.gameState.getTerritories()
                		.get(this.gameState.getLastAttackingCountry()).getNumberOfTroops() - 1,
                		ChoosePane.ATTACK_ATTACK);                	
                    }
                  break;
                case Multiplayer:
                    if(!this.gameState.getCurrentPlayer().isAI()) {
                	this.client.chooseNumberOfTroopsOnGUI(
                		country, 1, this.gameState.getTerritories()
                		.get(this.gameState.getLastAttackingCountry()).getNumberOfTroops() - 1,
                		ChoosePane.ATTACK_ATTACK);                	
                    }

                  break;
                case Tutorial:
                  break;
              }
            }
            break;
          case FORTIFY:
            if (Logic.playerFortifyingPosition(country, idOfPlayer, this.gameState)) {
              if (this.gameState.getLastFortifyingCounty() == null) {
                System.out.println("setting fortifying form " + country.toString());
                if (this.gameState.getTerritories().get(country).getNumberOfTroops() > 1) {
                  this.gameState.setLastFortifyingCounty(country);
                  ArrayList<CountryName> enemyCountries = new ArrayList<CountryName>();
                  enemyCountries.addAll(this.gameState.getTerritories().keySet());
                  enemyCountries.removeIf(x -> this.gameState.getTerritories().get(x)
                      .getOwnedByPlayer().equals(player));
                  switch (this.gameType) {
                    case SinglePlayer:
                      break;
                    case Multiplayer:
                      break;
                    case Tutorial:
                      break;
                  }
                }
              } else if (this.gameState.getLastFortifyingCounty() == country) {
                this.gameState.setLastFortifyingCounty(null);
                System.out.println("setting fortifying form null");
                switch (this.gameType) {
                  case SinglePlayer:
                    // this.singlePlayerHandler.resetAllOnGUI();
                    break;
                  case Multiplayer:
                    break;
                  case Tutorial:
                    break;
                }
              } else {
                switch (this.gameType) {
                  case SinglePlayer:
                      if(!this.gameState.getCurrentPlayer().isAI()) {
                	  this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(country, 1,
                		  this.gameState.getTerritories()
                		  .get(this.gameState.getLastFortifyingCounty()).getNumberOfTroops() - 1,
                		  ChoosePane.FORTIFY);                	  
                      }

                    break;
                  case Multiplayer:
                      if(!this.gameState.getCurrentPlayer().isAI()) {
                	  this.client.chooseNumberOfTroopsOnGUI(country, 1,
                		  this.gameState.getTerritories()
                		  .get(this.gameState.getLastFortifyingCounty()).getNumberOfTroops() - 1,
                		  ChoosePane.FORTIFY);                	  
                      }

                    break;
                  case Tutorial:
                    break;
                }
              }
            }
            break;
        }
        break;
    }
  }

  /**
   * Called from GUI.
   * Player confirms number of troops in the choose pane.
   *
   * @param country 	CountryName to send troops to / attack
   * @param troops 	Number of troops chosen
   * @param choosePane	ChoosePane enum for the type of confirmation
   * @param idOfPlayer	ID of player that confirms
   */
  public void confirmTroopsToCountry(CountryName country, int troops, ChoosePane choosePane,
      int idOfPlayer) {
    switch (choosePane) {
      case REINFORCE:
        if (Logic.playerReinforceConfirmedIsOk(this.gameState, idOfPlayer, country, troops)) {
          this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
          this.gameState.subtractTroopsToPlayer(this.gameState.getCurrentPlayer().getID(), troops);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGUI(country,
                  this.gameState.getTerritories().get(country).getNumberOfTroops(), this.gameState
                      .getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer().getID()));
              break;
            case Multiplayer:
              this.client.setTroopsOnTerritoryAndLeftOnGUI(country,
                  this.gameState.getTerritories().get(country).getNumberOfTroops(), this.gameState
                      .getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer().getID()));
              break;
            case Tutorial:
              break;
          }
        }
        break;
      case ATTACK_ATTACK: // open battle frame
        if (Logic.playerAttackAttackConfirmedIsOk(this.gameState, idOfPlayer, country, troops)) {
          Territory atTer =
              this.gameState.getTerritories().get(this.gameState.getLastAttackingCountry());
          Territory dfTer = this.gameState.getTerritories().get(country);
          Player atPly = atTer.getOwnedByPlayer();
          Player dfPly = dfTer.getOwnedByPlayer();
          Battle battle = new Battle(atTer.getContinent(), atTer.getCountryName(),
              dfTer.getContinent(), dfTer.getCountryName(), atTer.getAddressToPNG(),
              dfTer.getAddressToPNG(), troops, dfTer.getNumberOfTroops(), atPly.getAvatar(),
              dfPly.getAvatar(), atPly.getColor(), dfPly.getColor(), Math.min(3, troops),
              Math.min(2, dfTer.getNumberOfTroops()), this.gameType, atPly.getID(), dfPly.getID());
          this.gameState.setBattle(battle);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.openBattleFrameOnGUI(battle);
              break;
            case Multiplayer:
              this.client.openBattleFrameOnGUI(battle);
              break;
            case Tutorial:
              break;
          }
        }
        break;
      case ATTACK_COLONISE: // move troops form x to y
        if (Logic.playerAttackColoniseConfirmedIsOk(this.gameState, idOfPlayer,
            this.gameState.getLastAttackingCountry(), country, troops)) {
          this.gameState.getTerritories().get(this.gameState.getLastAttackingCountry())
              .removeNumberOfTroops(troops);
          this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler
                  .moveTroopsFromTerritoryToOtherOnGUI(this.gameState.getLastAttackingCountry(),
                      country, this.gameState.getTerritories()
                          .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops(),
                      troops);
              break;
            case Multiplayer:
              this.client
                  .moveTroopsFromTerritoryToOtherOnGUI(this.gameState.getLastAttackingCountry(),
                      country, this.gameState.getTerritories()
                          .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops(),
                      troops);
              break;
            case Tutorial:
              break;
          }
          this.gameState.setLastAttackingCountry(null);
          System.out.println("setting last attacking null");
        }

        break;
      case FORTIFY: // move troops from own territory x to own y
        if (Logic.playerForitfyConfirmedIsOk(gameState, gameState.getPlayers().get(idOfPlayer),
            this.gameState.getLastFortifyingCounty(), country, troops)) {
          this.gameState.getTerritories().get(this.gameState.getLastFortifyingCounty())
              .removeNumberOfTroops(troops);
          this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.moveTroopsFromTerritoryToOtherOnGUI(
                  this.gameState.getLastFortifyingCounty(), country,
                  this.gameState.getTerritories().get(this.gameState.getLastFortifyingCounty())
                      .getNumberOfTroops(),
                  this.gameState.getTerritories().get(country).getNumberOfTroops());
              break;
            case Multiplayer:
              this.client.moveTroopsFromTerritoryToOtherOnGUI(
                  this.gameState.getLastFortifyingCounty(), country,
                  this.gameState.getTerritories().get(this.gameState.getLastFortifyingCounty())
                      .getNumberOfTroops(),
                  this.gameState.getTerritories().get(country).getNumberOfTroops());
              break;
            case Tutorial:
              break;
          }
        }
        this.gameState.setLastFortifyingCounty(null);
        break;
    }
  }
  
  /**
   * Called from GUI.
   * Player clicks cancel in Choosinf Troops Pane. Attacked / Fortify / Deploy Called off.
   * 
   * @param country 	The CountryName from the Choose Pane
   * @param choosePane 	Enum for the type of choose pane
   * @param idOfPlayer	ID of player interacting
   */

  public void cancelNumberOfTroops(CountryName country, ChoosePane choosePane, int idOfPlayer) {
    switch (choosePane) {
      case REINFORCE:
        // hide the window
        break;
      case ATTACK_ATTACK:
        this.gameState.setLastAttackingCountry(null);
        System.out.println("setting last attacking null");

        // remove pinup country, reset activate countries
        break;
      case ATTACK_COLONISE:
        // not possible player has to deploy troops to new territory
        break;
      case FORTIFY:
        this.gameState.setLastFortifyingCounty(null);
        // remove pinup country, reset activate countries
        break;
    }
  }

  /**
   * Player wants to end turn.
   *
   * @param period Current Period as Enum 
   * @param phase  Current Phase as Enum
   * @param idOfPlayer ID of player
   */
  
  public void endPhaseTurn(Period period, Phase phase, int idOfPlayer) {
    System.out.println("End Phase Turn Called in the Game Handler by " + idOfPlayer);
    if (period.equals(Period.MAINPERIOD)) {
      if (Logic.playerEndsPhase(phase, idOfPlayer, this.gameState)) {
        switch (phase) {
          case REINFORCE:
            if (this.gameState.getRiskCardsInPlayers().get(idOfPlayer).size() < 5) {
              this.gameState.setCurrentTurnPhase(Phase.ATTACK);
              switch (this.gameType) {
                case SinglePlayer:
                  System.out.println("Reinforce Phase ended for " + idOfPlayer);
                  this.singlePlayerHandler.setPhaseOnGUI(Phase.ATTACK);
                  break;
                case Multiplayer:
                  this.client.setPhaseOnGUI(Phase.ATTACK);
                  break;
                case Tutorial:
                  break;
              }
              if (this.gameState.getCurrentPlayer().isAI()) {
                this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
              }
            }
            break;
          case ATTACK:
            this.gameState.setCurrentTurnPhase(Phase.FORTIFY);
            if (this.gameState.getLastTurnWonterritory()) {
              this.gameState.receiveRandomRiskCard(idOfPlayer);
            }
            switch (this.gameType) {
              case SinglePlayer:
                System.out.println("Attack Phase ended for " + idOfPlayer);
                this.singlePlayerHandler.setPhaseOnGUI(Phase.FORTIFY);
                break;
              case Multiplayer:
                this.client.setPhaseOnGUI(Phase.FORTIFY);
                break;
              case Tutorial:
                break;
            }
            if (this.gameState.getCurrentPlayer().isAI()) {
              this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
            }
            break;
          case FORTIFY:
            this.gameState.setCurrentTurnPhase(Phase.REINFORCE);
            this.gameState.setNextPlayer();
            this.gameState.setPlayerTroopsLeft(Logic.getTroopsReinforce(this.gameState));
            this.gameState.setLastTurnWonterritory(false);
            this.updateInGameLeaderBoard();

            switch (this.gameType) {
              case SinglePlayer:
                System.out.println("Fortify Phase ended for " + idOfPlayer);
                this.singlePlayerHandler.setPhaseOnGUI(Phase.REINFORCE);
                this.singlePlayerHandler.setCurrentPlayerOnGUI(
                    this.gameState.getCurrentPlayer().getID(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAI()) {
                  this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler
                      .chnagePlayerOnGUI(this.gameState.getCurrentPlayer().getID(), this.gameState
                          .getRiskCardsInPlayers().get(this.gameState.getCurrentPlayer().getID()));
                }

                break;
              case Multiplayer:
                this.client.setPhaseOnGUI(Phase.REINFORCE);
                this.client.setCurrentPlayerOnGUI(this.gameState.getCurrentPlayer().getID(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAI()) {
                  this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                }
                break;
              case Tutorial:
                break;
            }
            break;
        }
      } else {
        System.out.println("Can't end turn yet " + idOfPlayer);
      }

    } else {
      if (Logic.playerEndsTurn(period, idOfPlayer, this.gameState)) {
        switch (period) {
          case DICETHROW:
            if (Logic.isDiceThrowPeriodOver(this.gameState, idOfPlayer)) {
              this.gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
              this.gameState.setCurrentPlayer(Logic.getFirstPlayer(gameState));
              switch (this.gameType) {
                case SinglePlayer:
                  System.out.println("dice throw is over");
                  this.singlePlayerHandler.setCurrentPlayerOnGUI(
                      this.gameState.getCurrentPlayer().getID(),
                      this.gameState.getTroopsLeftForCurrent());
                  this.singlePlayerHandler.setPeriodOnGUI(Period.COUNTRYPOSESSION);
                  if (this.gameState.getCurrentPlayer().isAI()) {
                    // calls the AI
                    this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                  } else {
                    this.singlePlayerHandler.chnagePlayerOnGUI(
                        this.gameState.getCurrentPlayer().getID(),
                        this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                  }
                  break;
                case Multiplayer:
                  this.client.setCurrentPlayerOnGUI(this.gameState.getCurrentPlayer().getID(),
                      this.gameState.getTroopsLeftForCurrent());
                  this.client.setPeriodOnGUI(Period.COUNTRYPOSESSION);
                  if (this.gameState.getCurrentPlayer().isAI()) {
                    this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                  }
                  break;
                case Tutorial:
                  break;
              }
            } else {
              this.gameState.setNextPlayer();
              switch (this.gameType) {
                case SinglePlayer:
                  this.singlePlayerHandler.setCurrentPlayerOnGUI(
                      this.gameState.getCurrentPlayer().getID(),
                      this.gameState.getTroopsLeftForCurrent());
                  if (this.gameState.getCurrentPlayer().isAI()) {
                    // calls the AI
                    this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                  } else {
                    this.singlePlayerHandler.chnagePlayerOnGUI(
                        this.gameState.getCurrentPlayer().getID(),
                        this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                  }
                  break;
                case Multiplayer:
                  this.client.setCurrentPlayerOnGUI(this.gameState.getCurrentPlayer().getID(),
                      this.gameState.getTroopsLeftForCurrent());
                  if (this.gameState.getCurrentPlayer().isAI()) {
                    // calls the AI
                    this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                  }
                  break;
                case Tutorial:
                  break;
              }
            }
            break;
          case COUNTRYPOSESSION:
            if (Logic.allTerritoriesClaimed(gameState)) {
              gameState.setCurrentGamePeriod(Period.INITIALDEPLOY);
              switch (this.gameType) {
                case SinglePlayer:
                  this.singlePlayerHandler.setPeriodOnGUI(Period.INITIALDEPLOY);
                  break;
                case Multiplayer:
                  this.client.setPeriodOnGUI(Period.INITIALDEPLOY);
                  break;
                case Tutorial:
                  break;
              }
            }
            gameState.setNextPlayer();
            switch (this.gameType) {
              case SinglePlayer:
                this.singlePlayerHandler.setCurrentPlayerOnGUI(
                    this.gameState.getCurrentPlayer().getID(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAI()) {
                  this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler.chnagePlayerOnGUI(
                      this.gameState.getCurrentPlayer().getID(),
                      this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                }
                break;
              case Multiplayer:
                this.client.setCurrentPlayerOnGUI(this.gameState.getCurrentPlayer().getID(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAI()) {
                  this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                }
                break;
              case Tutorial:
                break;
            }
            break;
          case INITIALDEPLOY:
            if (Logic.isDeployPeriodOver(this.gameState)) {
              gameState.setCurrentGamePeriod(Period.MAINPERIOD);
              gameState.setCurrentTurnPhase(Phase.REINFORCE);
              this.gameState.setPlayerTroopsLeft(Logic.getTroopsReinforce(this.gameState));
              this.updateInGameLeaderBoard();
              switch (this.gameType) {
                case SinglePlayer:
                  this.singlePlayerHandler.setPeriodOnGUI(Period.MAINPERIOD);
                  this.singlePlayerHandler.setPhaseOnGUI(Phase.REINFORCE);
                  break;
                case Multiplayer:
                  this.client.setPeriodOnGUI(Period.MAINPERIOD);
                  this.client.setPhaseOnGUI(Phase.REINFORCE);
                  break;
                case Tutorial:
                  break;
              }
            }
            gameState.setNextPlayer();
            switch (this.gameType) {
              case SinglePlayer:
                this.singlePlayerHandler.setCurrentPlayerOnGUI(
                    this.gameState.getCurrentPlayer().getID(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAI()) {
                  // calls the AI
                  this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler.chnagePlayerOnGUI(
                      this.gameState.getCurrentPlayer().getID(),
                      this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                }
                break;
              case Multiplayer:
                this.client.setCurrentPlayerOnGUI(this.gameState.getCurrentPlayer().getID(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAI()) {
                  this.simulateAI(gameState, ((PlayerAI) this.gameState.getCurrentPlayer()));
                }
                break;
              case Tutorial:
                break;
            }
            break;
          case MAINPERIOD: // not here
            break;
          default: // no such case
            break;
        }
      }
    }

  }

  /** Updated the In-Game Ranking based on current gameState. */
  public void updateInGameLeaderBoard() {
    int[] ranks = Logic.getInGameRanks(this.gameState, this.lobby);
    switch (this.gameType) {
      case Tutorial:
        break;
      case SinglePlayer:
        this.singlePlayerHandler.updateRanksOnGUI(ranks);
        break;
      case Multiplayer:
        this.client.updateRanksOnGUI(ranks);
        break;
    }
  }
  
  /**
   * Called from GUI.
   * Player turns in a set of risk cards. 
   *
   * @param cards
   * @param idOfPlayer
   */

  public void turnInRiskCards(ArrayList<String> cards, int idOfPlayer) {
    ArrayList<Card> cardsCards =
        Logic.arrayListFromStringsToCard(cards, this.gameState, idOfPlayer);
    for (Card s : cardsCards) {
      System.out.println(s.toString());
    }
    System.out.println("Reinforce Called Test");

    if (Logic.turnInRiskCards(cardsCards, this.gameState.getPlayers().get(idOfPlayer),
        this.gameState)) {
      ArrayList<Card> newCards = (ArrayList<Card>) this.gameState.getRiskCardsInPlayers().get(idOfPlayer).clone();
      newCards.removeAll(cardsCards);
      this.gameState.editRiskCardsInPlayers(newCards, idOfPlayer);
      int bonusTroops = this.gameState.playerTurnsInCard();
      this.gameState.addTroopsToPlayer(idOfPlayer, bonusTroops);
      System.out.println("Risk Cards turned in success troops left now = "
          + this.gameState.getPlayerTroopsLeft().get(idOfPlayer));
      switch (this.gameType) {
        case SinglePlayer:
            if(!this.gameState.getCurrentPlayer().isAI()) {
        	this.singlePlayerHandler.riskCardsTurnedInSuccessOnGUI(newCards, idOfPlayer,
        		this.gameState.getPlayerTroopsLeft().get(idOfPlayer));        	
            }
          break;
        case Multiplayer:
          this.client.riskCardsTurnedInSuccessOnGUI(newCards, idOfPlayer,
              this.gameState.getPlayerTroopsLeft().get(idOfPlayer));
          break;
        case Tutorial:
          break;
      }
    }
  }
  
  /** Player throws dice during battle. Updates the battle instance. */

  public void battleDiceThrow() {
    if (Logic.battleDiceThrowIsOk(gameState)) {
      int[] diceValuesAt = Logic.getBattleDiceValues(gameState, true);
      int[] diceValuesDf = Logic.getBattleDiceValues(gameState, false);
      Battle changed = Logic.battleDiceRollConfirmed(gameState, diceValuesAt, diceValuesDf);
      int[] numberOfDices = new int[] {changed.getMaxDiceToThrow(), changed.getDefendingDice()};
      boolean overAt = changed.getTroopsInAttackAt() == 0;
      boolean overDf = changed.getTroopsInAttackDf() == 0;
      Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));

      if (overDf) {
        this.gameState.setLastTurnWonterritory(true);
        this.gameState.getTerritories().get(changed.getCountryNameDf())
            .setOwnedByPlayer(this.gameState.getPlayers().get(changed.getAttackerId()));
        if (!Logic.playerIsAlive(gameState, changed.getDefenderId())) {
          if (this.gameState.getAlivePlayers().size() <= 3) {
            this.gameState.addDeadPlayer(changed.getDefenderId());
          }
          this.gameState.getRiskCardsInPlayers().get(changed.getAttackerId())
              .addAll(this.gameState.getRiskCardsInPlayers().get(changed.getDefenderId()));
          this.gameState.getRiskCardsInPlayers().get(changed.getDefenderId()).removeIf(x -> true);
          this.gameState.removeDeadPlayer(changed.getDefenderId());
          System.out.println("player died left are " + gameState.getAlivePlayers().size());
          if (Logic.isGameOver(gameState)) {
            this.gameState.addDeadPlayer(this.gameState.getCurrentPlayer().getID());
          }
        }
      }
      if (overAt || overDf) {
        this.gameState.setBattle(null);
        this.gameState.getTerritories().get(changed.getCountryNameAt()).removeNumberOfTroops(
            changed.getTroopsInAttackAtFinal() - changed.getTroopsInAttackAt());
        this.gameState.getTerritories().get(changed.getCountryNameDf())
            .setNumberOfTroops(changed.getTroopsInAttackDf());
      } else {
        this.gameState.setBattle(changed);
      }

      switch (this.gameType) {
        case SinglePlayer:
          try {
            this.singlePlayerHandler.rollDiceBattleOnGUI(diceValuesAt, diceValuesDf,
                changed.getTroopsInAttackAt(), changed.getTroopsInAttackDf(), numberOfDices);
            timer.play();
            if (overAt || overDf) {
              timer.setOnFinished(x -> {
                this.singlePlayerHandler.endBattleOnGUI();
                this.singlePlayerHandler.setTroopsOnTerritory(changed.getCountryNameAt(),
                    this.gameState.getTerritories().get(changed.getCountryNameAt())
                        .getNumberOfTroops());
                this.singlePlayerHandler.setTroopsOnTerritory(changed.getCountryNameDf(),
                    this.gameState.getTerritories().get(changed.getCountryNameDf())
                        .getNumberOfTroops());
              });
            }
            if (overDf) {
              this.singlePlayerHandler.conquerCountryOnGUI(changed.getCountryNameDf(),
                  this.gameState.getCurrentPlayer().getID(), 0);
              if(!this.gameState.getCurrentPlayer().isAI()) {
        	  this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(changed.getCountryNameDf(),
        		  Math.min(Math.min(changed.getTroopsInAttackAtFinal(), 3),
        			  this.gameState.getTerritories().get(changed.getCountryNameAt())
        			  .getNumberOfTroops() - 1),
        		  this.gameState.getTerritories().get(changed.getCountryNameAt())
        		  .getNumberOfTroops() - 1,
        		  ChoosePane.ATTACK_COLONISE);        	  
              }

              if (Logic.isGameOver(gameState)) {
                ArrayList<Player> podium = this.gameState.getDeadPlayers();
                Collections.reverse(podium);
                this.singlePlayerHandler.gameIsOverOnGUI(podium);
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        case Multiplayer:
          try {
            this.client.rollDiceBattleOnGUI(diceValuesAt, diceValuesDf,
                changed.getTroopsInAttackAt(), changed.getTroopsInAttackDf(), numberOfDices);
            if (overAt || overDf) {
              timer.play();
              timer.setOnFinished(x -> {
                this.client.endBattleOnGUI();
                this.client.setTroopsOnTerritory(changed.getCountryNameAt(), this.gameState
                    .getTerritories().get(changed.getCountryNameAt()).getNumberOfTroops());
                this.client.setTroopsOnTerritory(changed.getCountryNameDf(), this.gameState
                    .getTerritories().get(changed.getCountryNameDf()).getNumberOfTroops());
              });
            }
            if (overDf) {
              this.client.conquerCountryOnGUI(changed.getCountryNameDf(),
                  this.gameState.getCurrentPlayer().getID(), 0);
              if(!this.gameState.getCurrentPlayer().isAI()) {
        	  this.client.chooseNumberOfTroopsOnGUI(changed.getCountryNameDf(),
        		  Math.min(Math.min(changed.getTroopsInAttackAtFinal(), 3),
        			  this.gameState.getTerritories().get(changed.getCountryNameAt())
        			  .getNumberOfTroops() - 1),
        		  this.gameState.getTerritories().get(changed.getCountryNameAt())
        		  .getNumberOfTroops() - 1,
        		  ChoosePane.ATTACK_COLONISE);        	  
              }

              if (Logic.isGameOver(gameState)) {
                ArrayList<Player> podium = this.gameState.getDeadPlayers();
                if (!podium.get(0).isAI()) {
                  AppController.dbH.updateProfileInfo(
                      AppController.dbH.getProfileByID(podium.get(0).getID()).getWins() + 1, "Wins",
                      podium.get(0).getID());
                  if (podium.get(0).getID() == AppController.getProfile().getId()) {
                      AppController.getProfile().setWins(podium.get(0).getID() + 1);
                    }

                }
                Collections.reverse(podium);
                this.client.gameIsOverOnGUI(podium);
                for (Player p : this.lobby.getHumanPlayerList()) {
                  if (p.getID() != podium.get(0).getID() && !p.isAI()) {
                    AppController.dbH.updateProfileInfo(
                        AppController.dbH.getProfileByID(p.getID()).getLoses() + 1, "Loses",
                        p.getID());
                    if (p.getID() == AppController.getProfile().getId()) {
                        AppController.getProfile().setLoses(p.getID() + 1);
                      }

                  }
                }
              }
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        case Tutorial:
          break;
      }
    }
  }
  
  /**
   * Method to simulate AI behaviour. 
   * Called from GameHandler when a player end turn and next on is AI.
   *
   * @param gameState Current GameState
   * @param player PlayerAI instance of the player that is to be simulated.
   */

  public void simulateAI(GameState gameState, PlayerAI player) {
    CountryName country = null;
    switch (gameState.getCurrentGamePeriod()) {
      case DICETHROW:
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(3)));
        this.playerThrowsInitialDice(player.getID());
        timer.play();
        timer.setOnFinished(x -> {
          this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
              this.gameState.getCurrentTurnPhase(), this.gameState.getCurrentPlayer().getID());
        });
        break;
      case COUNTRYPOSESSION:
        Timeline timer2 = new Timeline(new KeyFrame(Duration.seconds(1)));
        Timeline timer3 = new Timeline(new KeyFrame(Duration.seconds(1)));
        country = AILogic.chooseTerritoryToInitialClaim(gameState, player);
        final CountryName countryNameCopy = country;
        timer2.play();
        timer2.setOnFinished(x -> {
          this.clickCountry(player.getID(), countryNameCopy);
          timer3.play();
        });
        timer3.setOnFinished(x -> {
          this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
              this.gameState.getCurrentTurnPhase(), player.getID());
        });
        break;
      case INITIALDEPLOY:
        Timeline timer4 = new Timeline(new KeyFrame(Duration.seconds(1)));
        Timeline timer5 = new Timeline(new KeyFrame(Duration.seconds(1)));
        country = AILogic.chooseTerritoryToInitialReinforce(gameState, player);
        final CountryName countryNameCopy2 = country;
        timer4.play();
        timer4.setOnFinished(x -> {
          this.clickCountry(player.getID(), countryNameCopy2);
          timer5.play();
        });
        timer5.setOnFinished(x -> {
          this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
              this.gameState.getCurrentTurnPhase(), player.getID());
        });
        break;
      case MAINPERIOD:
        switch (gameState.getCurrentTurnPhase()) {
          case REINFORCE:
            Timeline timer6 = new Timeline(new KeyFrame(Duration.seconds(1)));
            Timeline timer7 = new Timeline(new KeyFrame(Duration.seconds(1)));
            Timeline timer8 = new Timeline(new KeyFrame(Duration.seconds(3)));
            System.out.println("Reinforce Called I");
            timer6.play();
            timer6.setOnFinished(x -> {
              System.out.println("Reinforce Called II");
              while (this.gameState.getRiskCardsInPlayers().get(player.getID()).size() >= 5) {
        	System.out.println("While loop AI PLAYER SHOULD TURN IN CARDS");
                ArrayList<String> cardSet =
                    (ArrayList<String>) AILogic.getRiskCardsTurnIn(gameState, player.getID());
                if (cardSet != null) {
                  for (String s : cardSet) {
                    System.out.println("One of Cards to turn in is " + s);
                  }
                  this.turnInRiskCards(cardSet, player.getID());
                }
              }
              System.out.println("Reinforce Called III");
              Pair<CountryName, Integer> pairReinforce =
                  AILogic.chooseTerritoryToReinforce(gameState, player);
              this.clickCountry(player.getID(), pairReinforce.getKey());
              this.confirmTroopsToCountry(pairReinforce.getKey(), pairReinforce.getValue(),
                  ChoosePane.REINFORCE, player.getID());
              timer7.play();
            });

            timer7.setOnFinished(x -> {
              System.out.println("Reinforce Called IV");
              if (this.gameState.getPlayerTroopsLeft().get(player.getID()) <= 0) {
                timer6.stop();
                timer8.play();
              } else {
                timer6.play();
              }
            });

            timer8.setOnFinished(x -> {
              System.out.println("Reinforce Called V");
              this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
                  this.gameState.getCurrentTurnPhase(), player.getID());
            });
            break;
          case ATTACK:
            Timeline timer9 = new Timeline(new KeyFrame(Duration.seconds(1)));
            Timeline timer10 = new Timeline(new KeyFrame(Duration.seconds(1)));
            Timeline timer11 = new Timeline(new KeyFrame(Duration.seconds(1)));
            System.out.println("Attack Called I");

            timer9.play();
            timer9.setOnFinished(x -> {
              System.out.println("Attack Called II");
              Pair<CountryName, CountryName> pairAttack =
                  AILogic.chooseTerritoryPairAttack(gameState, player);
              int troops = AILogic.chooseTroopsToAttackWith(
                  this.gameState.getTerritories().get(pairAttack.getKey()), player, gameState);
              this.clickCountry(player.getID(), pairAttack.getKey());
              this.clickCountry(player.getID(), pairAttack.getValue());
              this.confirmTroopsToCountry(pairAttack.getValue(), troops, ChoosePane.ATTACK_ATTACK,
                  player.getID());
              System.out.println("Attack Called III");

              while (this.gameState.getBattle() != null) {
                this.battleDiceThrow();
              }

              System.out.println("Attack Called IV");

              int troopsColonise = AILogic.chooseTroopsToSendToConqueredTerritory(
                  gameState.getTerritories().get(pairAttack.getKey()),
                  gameState.getTerritories().get(pairAttack.getValue()), player);
              this.confirmTroopsToCountry(pairAttack.getValue(), troopsColonise,
                  ChoosePane.ATTACK_COLONISE, player.getID());
              timer11.play();
            });

            timer11.setOnFinished(x -> {
              System.out.println("Attack Called V");
              if (AILogic.willAttack(gameState, player)) {
                timer9.play();
              } else {
                timer10.play();
              }
            });

            timer10.setOnFinished(x -> {
              timer9.stop();
              timer11.stop();
              System.out.println("Attack Called VI");
              this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
                  this.gameState.getCurrentTurnPhase(), this.gameState.getCurrentPlayer().getID());
            });
            break;
          case FORTIFY:
            Timeline timer12 = new Timeline(new KeyFrame(Duration.seconds(2)));
            Timeline timer13 = new Timeline(new KeyFrame(Duration.seconds(1)));
            System.out.println("Fortify Called I");
            timer12.play();
            timer12.setOnFinished(x -> {
              if (AILogic.willFortify(gameState, player)) {
                System.out.println("Fortify Called II");

                Pair<CountryName, CountryName> pairFortify =
                    AILogic.chooseTerritoriesPairFortify(gameState, player);
                int troops = AILogic.chooseTroopsToSendFortify(
                    this.gameState.getTerritories().get(pairFortify.getKey()), player);
                this.clickCountry(player.getID(), pairFortify.getKey());
                this.clickCountry(player.getID(), pairFortify.getValue());
                this.confirmTroopsToCountry(pairFortify.getValue(), troops, ChoosePane.FORTIFY,
                    player.getID());
              }
              timer13.play();
            });

            timer13.setOnFinished(x -> {
              System.out.println("Fortify Called III");

              this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
                  this.gameState.getCurrentTurnPhase(), this.gameState.getCurrentPlayer().getID());
            });

            break;
        }

    }
  }

}
