package game.state;

import game.logic.AiLogic;
import game.logic.Logic;
import game.models.Battle;
import game.models.Card;
import game.models.CountryName;
import game.models.Lobby;
import game.models.Player;
import game.models.PlayerAi;
import game.models.Territory;
import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;
import network.Client;

/**
 * This class manages player action, checks validity, updates GameState and repaint the Game Pane
 * GUI. To communicate back to the GUI this class calls either a SinglePlayerHandler or a Client
 * depending on GameType.
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
    gameState.setCurrentPlayer(lobby.getPlayerList().get(0).getId());
  }

  public void initTutorial(SinglePlayerHandler singlePlayerHandler) {
    this.singlePlayerHandler = singlePlayerHandler;
    this.gameType = GameType.Tutorial;
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
   * @param idOfPlayer id of the player that clicked throw
   */

  public void playerThrowsInitialDice(int idOfPlayer) {
    if (Logic.canThrowInitialDice(idOfPlayer, this.gameState)) {
      int i = this.gameState.getPlayersDiceThrown().get(idOfPlayer);
      switch (this.gameType) {
        case SinglePlayer:
          this.singlePlayerHandler.rollInitialDiceOnGui(idOfPlayer, i);
          break;
        case Multiplayer:
          client.rollInitialDiceOnGui(idOfPlayer, i);
          break;
        case Tutorial:
          this.singlePlayerHandler.rollInitialDiceOnGui(idOfPlayer, i);
          break;
        default:
          break;
      }
    }
  }

  /** Shows the initial hint for the tutorial. */

  public void showInitialHint() {
    Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
    timer.play();
    timer.setOnFinished(x -> {
      this.singlePlayerHandler.showTutorialPaneOnGui("Introduction", Hint.INTRODUCTION);
    });
  }

  /**
   * Called from SinglePlayerHandler. Player closed tutorial pane.
   *
   * @param hint Hint enum of the closed pane
   */

  public void tutorialCancelClick(Hint hint) {
    Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
    switch (hint) {
      case INTRODUCTION:
        timer.play();
        timer.setOnFinished(x -> {
          this.singlePlayerHandler.showTutorialPaneOnGui("Game Elements", Hint.GUIELEMENTS);
        });
        break;
      case GUIELEMENTS:
        timer.play();
        timer.setOnFinished(x -> {
          this.singlePlayerHandler.showTutorialPaneOnGui("Dice Throw", Hint.DICETHROW);
        });
        break;
      case DICETHROW:
        break;
      case COUNTRYPOSESSION:
        break;
      case INITIALDEPLOY:
        break;
      case PHASES:
        break;
      case BATTLE:
        break;
      case OVER:
        this.singlePlayerHandler.endTutorialOnGui();
        break;
      default:
        break;
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
   * Called from GUI. Player clicks on Country. This method checks if it is a valid move and decides
   * what to do with it.
   *
   * @param idOfPlayer that clicked on a Territory
   * @param country CountryName enum of Territroy clicked
   */

  public void clickCountry(int idOfPlayer, CountryName country) {
    Player player = this.gameState.getPlayers().get(idOfPlayer);
    switch (this.gameState.getCurrentGamePeriod()) {
      case DICETHROW:
        break;
      case COUNTRYPOSESSION:
        if (Logic.claimTerritory(player, this.gameState, country)) {
          this.gameState.setOwnedByTerritory(country, player);
          this.gameState.updateTroopsOnTerritory(country, 1);
          int numTroopsPlayer = this.gameState.getPlayerTroopsLeft().get(idOfPlayer) - 1;
          this.gameState.getPlayerTroopsLeft().replace(idOfPlayer, numTroopsPlayer);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.possesCountryOnGui(country, idOfPlayer,
                  this.gameState.getTroopsLeftForCurrent());
              break;
            case Multiplayer:
              this.client.possesCountryOnGui(country, idOfPlayer,
                  this.gameState.getTroopsLeftForCurrent());
              break;
            case Tutorial:
              this.singlePlayerHandler.possesCountryOnGui(country, idOfPlayer,
                  this.gameState.getTroopsLeftForCurrent());
              break;
            default:
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
              this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGui(country,
                  gameState.getTerritories().get(country).getNumberOfTroops(), numTroopsPlayer);
              break;
            case Multiplayer:
              this.client.setTroopsOnTerritoryAndLeftOnGui(country,
                  gameState.getTerritories().get(country).getNumberOfTroops(), numTroopsPlayer);
              break;
            case Tutorial:
              this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGui(country,
                  gameState.getTerritories().get(country).getNumberOfTroops(), numTroopsPlayer);
              break;
            default:
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
                  if (!this.gameState.getCurrentPlayer().isAi()) {
                    this.singlePlayerHandler.chooseNumberOfTroopsOnGui(country, 1,
                        this.gameState.getPlayerTroopsLeft().get(idOfPlayer), ChoosePane.REINFORCE);
                  }
                  break;
                case Multiplayer:
                  if (!this.gameState.getCurrentPlayer().isAi()) {
                    this.client.chooseNumberOfTroopsOnGui(country, 1,
                        this.gameState.getPlayerTroopsLeft().get(idOfPlayer), ChoosePane.REINFORCE);
                  }
                  break;
                case Tutorial:
                  if (!this.gameState.getCurrentPlayer().isAi()) {
                    this.singlePlayerHandler.chooseNumberOfTroopsOnGui(country, 1,
                        this.gameState.getPlayerTroopsLeft().get(idOfPlayer), ChoosePane.REINFORCE);
                  }
                  break;
                default:
                  break;
              }
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
                default:
                  break;
              }
            } else if (Logic.playerAttackingCountry(country, idOfPlayer, this.gameState)) {
              switch (this.gameType) {
                case SinglePlayer:
                  if (!this.gameState.getCurrentPlayer().isAi()) {
                    this.singlePlayerHandler.chooseNumberOfTroopsOnGui(country, 1,
                        this.gameState.getTerritories()
                            .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops() - 1,
                        ChoosePane.ATTACK_ATTACK);
                  }
                  break;
                case Multiplayer:
                  if (!this.gameState.getCurrentPlayer().isAi()) {
                    this.client.chooseNumberOfTroopsOnGui(country, 1,
                        this.gameState.getTerritories()
                            .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops() - 1,
                        ChoosePane.ATTACK_ATTACK);
                  }

                  break;
                case Tutorial:
                  if (!this.gameState.getCurrentPlayer().isAi()) {
                    this.singlePlayerHandler.chooseNumberOfTroopsOnGui(country, 1,
                        this.gameState.getTerritories()
                            .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops() - 1,
                        ChoosePane.ATTACK_ATTACK);
                    Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
                    timer.play();
                    timer.setOnFinished(x -> {
                      this.singlePlayerHandler.showTutorialPaneOnGui("Attacking", Hint.BATTLE);
                    });
                  }
                  break;
                default:
                  break;
              }
            }
            break;
          case FORTIFY:
            if (Logic.playerFortifyingPosition(country, idOfPlayer, this.gameState)
                && !this.gameState.isPlayerForitfied()) {
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
                    default:
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
                  default:
                    break;
                }
              } else {
                if (Logic.twoTerritoriesAreFortifiable(this.gameState.getLastFortifyingCounty(),
                    country, this.gameState, new ArrayList<CountryName>(), idOfPlayer)) {
                  switch (this.gameType) {
                    case SinglePlayer:
                      if (!this.gameState.getCurrentPlayer().isAi()) {
                        this.singlePlayerHandler.chooseNumberOfTroopsOnGui(country, 1,
                            this.gameState.getTerritories()
                                .get(this.gameState.getLastFortifyingCounty()).getNumberOfTroops()
                                - 1,
                            ChoosePane.FORTIFY);
                      }

                      break;
                    case Multiplayer:
                      if (!this.gameState.getCurrentPlayer().isAi()) {
                        this.client.chooseNumberOfTroopsOnGui(country, 1,
                            this.gameState.getTerritories()
                                .get(this.gameState.getLastFortifyingCounty()).getNumberOfTroops()
                                - 1,
                            ChoosePane.FORTIFY);
                      }

                      break;
                    case Tutorial:
                      if (!this.gameState.getCurrentPlayer().isAi()) {
                        this.singlePlayerHandler.chooseNumberOfTroopsOnGui(country, 1,
                            this.gameState.getTerritories()
                                .get(this.gameState.getLastFortifyingCounty()).getNumberOfTroops()
                                - 1,
                            ChoosePane.FORTIFY);
                      }
                      break;
                    default:
                      break;
                  }
                }
              }
            }
            break;
          default:
            break;
        }
        break;
      default:
        break;
    }
  }

  /**
   * Called from GUI. Player confirms number of troops in the choose pane.
   *
   * @param country CountryName to send troops to / attack
   * @param troops Number of troops chosen
   * @param choosePane ChoosePane enum for the type of confirmation
   * @param idOfPlayer ID of player that confirms
   */

  public void confirmTroopsToCountry(CountryName country, int troops, ChoosePane choosePane,
      int idOfPlayer) {
    switch (choosePane) {
      case REINFORCE:
        if (Logic.playerReinforceConfirmedIsOk(this.gameState, idOfPlayer, country, troops)) {
          this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
          this.gameState.subtractTroopsToPlayer(this.gameState.getCurrentPlayer().getId(), troops);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGui(country,
                  this.gameState.getTerritories().get(country).getNumberOfTroops(), this.gameState
                      .getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer().getId()));
              break;
            case Multiplayer:
              this.client.setTroopsOnTerritoryAndLeftOnGui(country,
                  this.gameState.getTerritories().get(country).getNumberOfTroops(), this.gameState
                      .getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer().getId()));
              break;
            case Tutorial:
              this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGui(country,
                  this.gameState.getTerritories().get(country).getNumberOfTroops(), this.gameState
                      .getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer().getId()));
              break;
            default:
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
              dfTer.getContinent(), dfTer.getCountryName(), atTer.getAddressToPng(),
              dfTer.getAddressToPng(), troops, dfTer.getNumberOfTroops(), atPly.getAvatar(),
              dfPly.getAvatar(), atPly.getColor(), dfPly.getColor(), Math.min(3, troops),
              Math.min(2, dfTer.getNumberOfTroops()), this.gameType, atPly.getId(), dfPly.getId());
          this.gameState.setBattle(battle);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.openBattleFrameOnGui(battle);
              break;
            case Multiplayer:
              this.client.openBattleFrameOnGui(battle);
              break;
            case Tutorial:
              this.singlePlayerHandler.openBattleFrameOnGui(battle);
              break;
            default:
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
                  .moveTroopsFromTerritoryToOtherOnGui(this.gameState.getLastAttackingCountry(),
                      country, this.gameState.getTerritories()
                          .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops(),
                      troops);
              break;
            case Multiplayer:
              this.client
                  .moveTroopsFromTerritoryToOtherOnGui(this.gameState.getLastAttackingCountry(),
                      country, this.gameState.getTerritories()
                          .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops(),
                      troops);
              break;
            case Tutorial:
              this.singlePlayerHandler
                  .moveTroopsFromTerritoryToOtherOnGui(this.gameState.getLastAttackingCountry(),
                      country, this.gameState.getTerritories()
                          .get(this.gameState.getLastAttackingCountry()).getNumberOfTroops(),
                      troops);
              break;
            default:
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
          this.gameState.setPlayerForitfied(true);
          switch (this.gameType) {
            case SinglePlayer:
              this.singlePlayerHandler.moveTroopsFromTerritoryToOtherOnGui(
                  this.gameState.getLastFortifyingCounty(), country,
                  this.gameState.getTerritories().get(this.gameState.getLastFortifyingCounty())
                      .getNumberOfTroops(),
                  this.gameState.getTerritories().get(country).getNumberOfTroops());
              break;
            case Multiplayer:
              this.client.moveTroopsFromTerritoryToOtherOnGui(
                  this.gameState.getLastFortifyingCounty(), country,
                  this.gameState.getTerritories().get(this.gameState.getLastFortifyingCounty())
                      .getNumberOfTroops(),
                  this.gameState.getTerritories().get(country).getNumberOfTroops());
              break;
            case Tutorial:
              this.singlePlayerHandler.moveTroopsFromTerritoryToOtherOnGui(
                  this.gameState.getLastFortifyingCounty(), country,
                  this.gameState.getTerritories().get(this.gameState.getLastFortifyingCounty())
                      .getNumberOfTroops(),
                  this.gameState.getTerritories().get(country).getNumberOfTroops());
              break;
            default:
              break;
          }
        }
        this.gameState.setLastFortifyingCounty(null);
        break;
      default:
        break;
    }
  }

  /**
   * Called from GUI. Player clicks cancel in Choosinf Troops Pane. Attacked / Fortify / Deploy
   * Called off.
   *
   * @param country The CountryName from the Choose Pane
   * @param choosePane Enum for the type of choose pane
   * @param idOfPlayer ID of player interacting
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
      default:
        break;
    }
  }

  /**
   * Player wants to end turn.
   *
   * @param period Current Period as Enum
   * @param phase Current Phase as Enum
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
                  this.singlePlayerHandler.setPhaseOnGui(Phase.ATTACK);
                  break;
                case Multiplayer:
                  this.client.setPhaseOnGui(Phase.ATTACK);
                  break;
                case Tutorial:
                  this.singlePlayerHandler.setPhaseOnGui(Phase.ATTACK);
                  break;
                default:
                  break;
              }
              if (this.gameState.getCurrentPlayer().isAi()) {
                this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
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
                this.singlePlayerHandler.setPhaseOnGui(Phase.FORTIFY);
                break;
              case Multiplayer:
                this.client.setPhaseOnGui(Phase.FORTIFY);
                break;
              case Tutorial:
                this.singlePlayerHandler.setPhaseOnGui(Phase.FORTIFY);
                break;
              default:
                break;
            }
            if (this.gameState.getCurrentPlayer().isAi()) {
              this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
            }
            break;
          case FORTIFY:
            if (this.gameType.equals(GameType.Tutorial)) {
              if (!this.gameState.getCurrentPlayer().isAi()) {
                Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
                timer.play();
                timer.setOnFinished(x -> {
                  this.singlePlayerHandler.showTutorialPaneOnGui("You're Ready", Hint.OVER);
                });
              }
            }
            this.gameState.setCurrentTurnPhase(Phase.REINFORCE);
            this.gameState.setNextPlayer();
            this.gameState.setPlayerTroopsLeft(Logic.getTroopsReinforce(this.gameState));
            this.gameState.setLastTurnWonterritory(false);
            this.gameState.setPlayerForitfied(false);
            this.updateInGameLeaderBoard();
            switch (this.gameType) {
              case SinglePlayer:
                System.out.println("Fortify Phase ended for " + idOfPlayer);
                this.singlePlayerHandler.setPhaseOnGui(Phase.REINFORCE);
                this.singlePlayerHandler.setCurrentPlayerOnGui(
                    this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler
                      .chnagePlayerOnGui(this.gameState.getCurrentPlayer().getId(), this.gameState
                          .getRiskCardsInPlayers().get(this.gameState.getCurrentPlayer().getId()));
                }
                break;
              case Multiplayer:
                this.client.setPhaseOnGui(Phase.REINFORCE);
                this.client.setCurrentPlayerOnGui(this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                }
                break;
              case Tutorial:
                this.singlePlayerHandler.setPhaseOnGui(Phase.REINFORCE);
                this.singlePlayerHandler.setCurrentPlayerOnGui(
                    this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler
                      .chnagePlayerOnGui(this.gameState.getCurrentPlayer().getId(), this.gameState
                          .getRiskCardsInPlayers().get(this.gameState.getCurrentPlayer().getId()));
                }
                break;
              default:
                break;
            }
            break;
          default:
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
                  System.out.println("dice throw is over single");
                  this.singlePlayerHandler.setCurrentPlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getTroopsLeftForCurrent());
                  this.singlePlayerHandler.setPeriodOnGui(Period.COUNTRYPOSESSION);
                  if (this.gameState.getCurrentPlayer().isAi()) {
                    // calls the AI
                    this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                  } else {
                    this.singlePlayerHandler.chnagePlayerOnGui(
                        this.gameState.getCurrentPlayer().getId(),
                        this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                  }
                  break;
                case Multiplayer:
                  this.client.setCurrentPlayerOnGui(this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getTroopsLeftForCurrent());
                  this.client.setPeriodOnGui(Period.COUNTRYPOSESSION);
                  if (this.gameState.getCurrentPlayer().isAi()) {
                    this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                  }
                  break;
                case Tutorial:
                  Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
                  timer.play();
                  timer.setOnFinished(x -> {
                    singlePlayerHandler.showTutorialPaneOnGui("Country Possession",
                        Hint.COUNTRYPOSESSION);
                  });

                  this.singlePlayerHandler.setCurrentPlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getTroopsLeftForCurrent());
                  this.singlePlayerHandler.setPeriodOnGui(Period.COUNTRYPOSESSION);
                  if (this.gameState.getCurrentPlayer().isAi()) {
                    // calls the AI
                    this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                  } else {
                    this.singlePlayerHandler.chnagePlayerOnGui(
                        this.gameState.getCurrentPlayer().getId(),
                        this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                  }
                  break;
                default:
                  break;
              }
            } else {
              this.gameState.setNextPlayer();
              switch (this.gameType) {
                case SinglePlayer:
                  this.singlePlayerHandler.setCurrentPlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getTroopsLeftForCurrent());
                  if (this.gameState.getCurrentPlayer().isAi()) {
                    // calls the AI
                    this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                  } else {
                    this.singlePlayerHandler.chnagePlayerOnGui(
                        this.gameState.getCurrentPlayer().getId(),
                        this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                  }
                  break;
                case Multiplayer:
                  this.client.setCurrentPlayerOnGui(this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getTroopsLeftForCurrent());
                  if (this.gameState.getCurrentPlayer().isAi()) {
                    this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                  }
                  break;
                case Tutorial:
                  this.singlePlayerHandler.setCurrentPlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getTroopsLeftForCurrent());
                  if (this.gameState.getCurrentPlayer().isAi()) {
                    this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                  } else {
                    this.singlePlayerHandler.chnagePlayerOnGui(
                        this.gameState.getCurrentPlayer().getId(),
                        this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                  }
                  break;
                default:
                  break;
              }
            }
            break;
          case COUNTRYPOSESSION:
            if (Logic.allTerritoriesClaimed(gameState)) {
              gameState.setCurrentGamePeriod(Period.INITIALDEPLOY);
              switch (this.gameType) {
                case SinglePlayer:
                  this.singlePlayerHandler.setPeriodOnGui(Period.INITIALDEPLOY);
                  break;
                case Multiplayer:
                  this.client.setPeriodOnGui(Period.INITIALDEPLOY);
                  break;
                case Tutorial:
                  Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
                  timer.play();
                  timer.setOnFinished(x -> {
                    this.singlePlayerHandler.showTutorialPaneOnGui("Initial Deployment",
                        Hint.INITIALDEPLOY);
                  });
                  this.singlePlayerHandler.setPeriodOnGui(Period.INITIALDEPLOY);
                  break;
                default:
                  break;
              }
            }
            gameState.setNextPlayer();
            switch (this.gameType) {
              case SinglePlayer:
                this.singlePlayerHandler.setCurrentPlayerOnGui(
                    this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler.chnagePlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                }
                break;
              case Multiplayer:
                this.client.setCurrentPlayerOnGui(this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                }
                break;
              case Tutorial:
                this.singlePlayerHandler.setCurrentPlayerOnGui(
                    this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler.chnagePlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                }
                break;
              default:
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
                  this.singlePlayerHandler.setPeriodOnGui(Period.MAINPERIOD);
                  this.singlePlayerHandler.setPhaseOnGui(Phase.REINFORCE);
                  break;
                case Multiplayer:
                  this.client.setPeriodOnGui(Period.MAINPERIOD);
                  this.client.setPhaseOnGui(Phase.REINFORCE);
                  break;
                case Tutorial:
                  if (!this.gameState.getCurrentPlayer().isAi()) {
                    Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1)));
                    timer.play();
                    timer.setOnFinished(x -> {
                      this.singlePlayerHandler.showTutorialPaneOnGui("Turn Phases", Hint.PHASES);
                    });
                  }
                  this.singlePlayerHandler.setPeriodOnGui(Period.MAINPERIOD);
                  this.singlePlayerHandler.setPhaseOnGui(Phase.REINFORCE);
                  break;
                default:
                  break;
              }
            }
            gameState.setNextPlayer();
            switch (this.gameType) {
              case SinglePlayer:
                this.singlePlayerHandler.setCurrentPlayerOnGui(
                    this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler.chnagePlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                }
                break;
              case Multiplayer:
                this.client.setCurrentPlayerOnGui(this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                }
                break;
              case Tutorial:
                this.singlePlayerHandler.setCurrentPlayerOnGui(
                    this.gameState.getCurrentPlayer().getId(),
                    this.gameState.getTroopsLeftForCurrent());
                if (this.gameState.getCurrentPlayer().isAi()) {
                  this.simulateAi(gameState, ((PlayerAi) this.gameState.getCurrentPlayer()));
                } else {
                  this.singlePlayerHandler.chnagePlayerOnGui(
                      this.gameState.getCurrentPlayer().getId(),
                      this.gameState.getRiskCardsInPlayers().get(idOfPlayer));
                }
                break;
              default:
                break;
            }
            break;
          case MAINPERIOD:
            break;
          default:
            break;
        }
      }
    }

  }

  /** Updated the In-Game Ranking based on current game.state. */
  public void updateInGameLeaderBoard() {
    int[] ranks = Logic.getInGameRanks(this.gameState, this.lobby);
    switch (this.gameType) {
      case Tutorial:
        this.singlePlayerHandler.updateRanksOnGui(ranks);
        break;
      case SinglePlayer:
        this.singlePlayerHandler.updateRanksOnGui(ranks);
        break;
      case Multiplayer:
        this.client.updateRanksOnGui(ranks);
        break;
      default:
        break;
    }
  }

  /**
   * Called from GUI. Player turns in a set of risk cards.
   *
   * @param cards as their String that the player wants to turn in
   * @param idOfPlayer turning in the cards
   */

  public void turnInRiskCards(ArrayList<String> cards, int idOfPlayer) {
    ArrayList<Card> cardsCards =
        Logic.arrayListFromStringsToCard(cards, this.gameState, idOfPlayer);

    if (Logic.turnInRiskCards(cardsCards, this.gameState.getPlayers().get(idOfPlayer),
        this.gameState)) {
      ArrayList<Card> newCards =
          (ArrayList<Card>) this.gameState.getRiskCardsInPlayers().get(idOfPlayer).clone();
      for (Card c : cardsCards) {
        if (!c.isJoker()) {
          if (this.gameState.getTerritories().get(c.getName()).getOwnedByPlayer()
              .getId() == idOfPlayer) {
            System.out.println("Bonus troops added in gamehandler");
            this.gameState.getTerritories().get(c.getName()).addNumberOfTroops(2);
          }
        }
      }
      newCards.removeAll(cardsCards);
      this.gameState.editRiskCardsInPlayers(newCards, idOfPlayer);
      int bonusTroops = this.gameState.playerTurnsInCard();
      this.gameState.addTroopsToPlayer(idOfPlayer, bonusTroops);
      System.out.println("Risk Cards turned in success troops left now = "
          + this.gameState.getPlayerTroopsLeft().get(idOfPlayer));
      switch (this.gameType) {
        case SinglePlayer:
          if (!this.gameState.getCurrentPlayer().isAi()) {
            this.singlePlayerHandler.riskCardsTurnedInSuccessOnGui(newCards, idOfPlayer,
                this.gameState.getPlayerTroopsLeft().get(idOfPlayer));
          }
          for (Card c : cardsCards) {
            if (!c.isJoker()) {
              if (this.gameState.getTerritories().get(c.getName()).getOwnedByPlayer()
                  .getId() == idOfPlayer) {
                System.out.println("Bonus troops added in singleplayer");
                this.singlePlayerHandler.setTroopsOnTerritoryOnGui(c.getName(),
                    this.gameState.getTerritories().get(c.getName()).getNumberOfTroops());
              }
            }
          }
          break;
        case Multiplayer:
          this.client.riskCardsTurnedInSuccessOnGui(newCards, idOfPlayer,
              this.gameState.getPlayerTroopsLeft().get(idOfPlayer));
          for (Card c : cardsCards) {
            if (!c.isJoker()) {
              if (this.gameState.getTerritories().get(c.getName()).getOwnedByPlayer()
                  .getId() == idOfPlayer) {
                this.client.setTroopsOnTerritory(c.getName(),
                    this.gameState.getTerritories().get(c.getName()).getNumberOfTroops());
              }
            }
          }
          break;
        case Tutorial:
          if (!this.gameState.getCurrentPlayer().isAi()) {
            this.singlePlayerHandler.riskCardsTurnedInSuccessOnGui(newCards, idOfPlayer,
                this.gameState.getPlayerTroopsLeft().get(idOfPlayer));
          }
          for (Card c : cardsCards) {
            if (!c.isJoker()) {
              if (this.gameState.getTerritories().get(c.getName()).getOwnedByPlayer()
                  .getId() == idOfPlayer) {
                this.singlePlayerHandler.setTroopsOnTerritoryOnGui(c.getName(),
                    this.gameState.getTerritories().get(c.getName()).getNumberOfTroops());
              }
            }
          }
          break;
        default:
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
            this.gameState.addDeadPlayer(this.gameState.getCurrentPlayer().getId());
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
            this.singlePlayerHandler.rollDiceBattleOnGui(diceValuesAt, diceValuesDf,
                changed.getTroopsInAttackAt(), changed.getTroopsInAttackDf(), numberOfDices);
            timer.play();
            if (overAt || overDf) {
              timer.setOnFinished(x -> {
                this.singlePlayerHandler.endBattleOnGui();
                this.singlePlayerHandler.setTroopsOnTerritoryOnGui(changed.getCountryNameAt(),
                    this.gameState.getTerritories().get(changed.getCountryNameAt())
                        .getNumberOfTroops());
                this.singlePlayerHandler.setTroopsOnTerritoryOnGui(changed.getCountryNameDf(),
                    this.gameState.getTerritories().get(changed.getCountryNameDf())
                        .getNumberOfTroops());
              });
            }
            if (overDf) {
              this.singlePlayerHandler.conquerCountryOnGui(changed.getCountryNameDf(),
                  this.gameState.getCurrentPlayer().getId(), 0);
              if (!this.gameState.getCurrentPlayer().isAi()) {
                this.singlePlayerHandler.chooseNumberOfTroopsOnGui(changed.getCountryNameDf(),
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
                this.singlePlayerHandler.gameIsOverOnGui(podium);
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        case Multiplayer:
          try {
            this.client.rollDiceBattleOnGui(diceValuesAt, diceValuesDf,
                changed.getTroopsInAttackAt(), changed.getTroopsInAttackDf(), numberOfDices);
            if (overAt || overDf) {
              timer.play();
              timer.setOnFinished(x -> {
                this.client.endBattleOnGui();
                this.client.setTroopsOnTerritory(changed.getCountryNameAt(), this.gameState
                    .getTerritories().get(changed.getCountryNameAt()).getNumberOfTroops());
                this.client.setTroopsOnTerritory(changed.getCountryNameDf(), this.gameState
                    .getTerritories().get(changed.getCountryNameDf()).getNumberOfTroops());
              });
            }
            if (overDf) {
              this.client.conquerCountryOnGui(changed.getCountryNameDf(),
                  this.gameState.getCurrentPlayer().getId(), 0);
              if (!this.gameState.getCurrentPlayer().isAi()) {
                this.client.chooseNumberOfTroopsOnGui(changed.getCountryNameDf(),
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
                this.client.gameIsOverOnGui(podium);

              }
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        case Tutorial:
          try {
            this.singlePlayerHandler.rollDiceBattleOnGui(diceValuesAt, diceValuesDf,
                changed.getTroopsInAttackAt(), changed.getTroopsInAttackDf(), numberOfDices);
            timer.play();
            if (overAt || overDf) {
              timer.setOnFinished(x -> {
                this.singlePlayerHandler.endBattleOnGui();
                this.singlePlayerHandler.setTroopsOnTerritoryOnGui(changed.getCountryNameAt(),
                    this.gameState.getTerritories().get(changed.getCountryNameAt())
                        .getNumberOfTroops());
                this.singlePlayerHandler.setTroopsOnTerritoryOnGui(changed.getCountryNameDf(),
                    this.gameState.getTerritories().get(changed.getCountryNameDf())
                        .getNumberOfTroops());
              });
            }
            if (overDf) {
              this.singlePlayerHandler.conquerCountryOnGui(changed.getCountryNameDf(),
                  this.gameState.getCurrentPlayer().getId(), 0);
              if (!this.gameState.getCurrentPlayer().isAi()) {
                this.singlePlayerHandler.chooseNumberOfTroopsOnGui(changed.getCountryNameDf(),
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
                this.singlePlayerHandler.gameIsOverOnGui(podium);
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        default:
          break;
      }
    }
  }

  /**
   * Method to simulate AI behaviour. Called from GameHandler when a player end turn and next on is
   * AI.
   *
   * @param player PlayerAi instance of the player that is to be simulated.
   */

  public void simulateAi(GameState gameState, PlayerAi player) {
    CountryName country = null;
    switch (gameState.getCurrentGamePeriod()) {
      case DICETHROW:
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(3)));
        this.playerThrowsInitialDice(player.getId());
        timer.play();
        timer.setOnFinished(x -> {
          this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
              this.gameState.getCurrentTurnPhase(), this.gameState.getCurrentPlayer().getId());
        });
        break;
      case COUNTRYPOSESSION:
        Timeline timer2 = new Timeline(new KeyFrame(Duration.seconds(1)));
        Timeline timer3 = new Timeline(new KeyFrame(Duration.seconds(1)));
        country = AiLogic.chooseTerritoryToInitialClaim(gameState, player);
        final CountryName countryNameCopy = country;
        timer2.play();
        timer2.setOnFinished(x -> {
          this.clickCountry(player.getId(), countryNameCopy);
          timer3.play();
        });
        timer3.setOnFinished(x -> {
          this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
              this.gameState.getCurrentTurnPhase(), player.getId());
        });
        break;
      case INITIALDEPLOY:
        Timeline timer4 = new Timeline(new KeyFrame(Duration.seconds(1)));
        Timeline timer5 = new Timeline(new KeyFrame(Duration.seconds(1)));
        country = AiLogic.chooseTerritoryToInitialReinforce(gameState, player);
        final CountryName countryNameCopy2 = country;
        timer4.play();
        timer4.setOnFinished(x -> {
          this.clickCountry(player.getId(), countryNameCopy2);
          timer5.play();
        });
        timer5.setOnFinished(x -> {
          this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
              this.gameState.getCurrentTurnPhase(), player.getId());
        });
        break;
      case MAINPERIOD:
        switch (gameState.getCurrentTurnPhase()) {
          case REINFORCE:
            Timeline timer6 = new Timeline(new KeyFrame(Duration.seconds(1)));
            Timeline timer7 = new Timeline(new KeyFrame(Duration.seconds(1)));
            System.out.println("Reinforce Called I");
            timer6.play();
            timer6.setOnFinished(x -> {
              System.out.println("Reinforce Called II");
              while (this.gameState.getRiskCardsInPlayers().get(player.getId()).size() >= 5) {
                System.out.println("While loop AI PLAYER SHOULD TURN IN CARDS");
                ArrayList<String> cardSet =
                    (ArrayList<String>) AiLogic.getRiskCardsTurnIn(gameState, player.getId());
                if (cardSet != null) {
                  for (String s : cardSet) {
                    System.out.println("One of Cards to turn in is " + s);
                  }
                  this.turnInRiskCards(cardSet, player.getId());
                }
              }
              System.out.println("Reinforce Called III");
              Pair<CountryName, Integer> pairReinforce =
                  AiLogic.chooseTerritoryToReinforce(gameState, player);
              this.clickCountry(player.getId(), pairReinforce.getKey());
              this.confirmTroopsToCountry(pairReinforce.getKey(), pairReinforce.getValue(),
                  ChoosePane.REINFORCE, player.getId());
              timer7.play();
            });

            Timeline timer8 = new Timeline(new KeyFrame(Duration.seconds(3)));
            timer7.setOnFinished(x -> {
              System.out.println("Reinforce Called IV");
              if (this.gameState.getPlayerTroopsLeft().get(player.getId()) <= 0) {
                timer6.stop();
                timer8.play();
              } else {
                timer6.play();
              }
            });

            timer8.setOnFinished(x -> {
              System.out.println("Reinforce Called V");
              this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
                  this.gameState.getCurrentTurnPhase(), player.getId());
            });
            break;
          case ATTACK:
            Timeline timer9 = new Timeline(new KeyFrame(Duration.seconds(1)));
            Timeline timer11 = new Timeline(new KeyFrame(Duration.seconds(1)));
            System.out.println("Attack Called I");

            timer9.play();
            timer9.setOnFinished(x -> {
              System.out.println("Attack Called II");
              Pair<CountryName, CountryName> pairAttack =
                  AiLogic.chooseTerritoryPairAttack(gameState, player);
              if (pairAttack.getValue() != null) {
                int troops = AiLogic.chooseTroopsToAttackWith(
                    this.gameState.getTerritories().get(pairAttack.getKey()), player, gameState);
                this.clickCountry(player.getId(), pairAttack.getKey());
                this.clickCountry(player.getId(), pairAttack.getValue());
                this.confirmTroopsToCountry(pairAttack.getValue(), troops, ChoosePane.ATTACK_ATTACK,
                    player.getId());
                System.out.println("Attack Called III");

                while (this.gameState.getBattle() != null) {
                  this.battleDiceThrow();
                }
                System.out.println("Attack Called IV");

                int troopsColonise = AiLogic.chooseTroopsToSendToConqueredTerritory(
                    gameState.getTerritories().get(pairAttack.getKey()),
                    gameState.getTerritories().get(pairAttack.getValue()), player);
                this.confirmTroopsToCountry(pairAttack.getValue(), troopsColonise,
                    ChoosePane.ATTACK_COLONISE, player.getId());
                timer11.play();
              }
            });

            Timeline timer10 = new Timeline(new KeyFrame(Duration.seconds(1)));
            timer11.setOnFinished(x -> {
              System.out.println("Attack Called V");
              if (AiLogic.willAttack(gameState, player)) {
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
                  this.gameState.getCurrentTurnPhase(), this.gameState.getCurrentPlayer().getId());
            });
            break;
          case FORTIFY:
            Timeline timer12 = new Timeline(new KeyFrame(Duration.seconds(2)));
            Timeline timer13 = new Timeline(new KeyFrame(Duration.seconds(1)));
            System.out.println("Fortify Called I");
            timer12.play();
            timer12.setOnFinished(x -> {
              if (AiLogic.willFortify(gameState, player)) {
                System.out.println("Fortify Called II");

                Pair<CountryName, CountryName> pairFortify =
                    AiLogic.chooseTerritoriesPairFortify(gameState, player);
                int troops = AiLogic.chooseTroopsToSendFortify(
                    this.gameState.getTerritories().get(pairFortify.getKey()), player);
                this.clickCountry(player.getId(), pairFortify.getKey());
                this.clickCountry(player.getId(), pairFortify.getValue());
                this.confirmTroopsToCountry(pairFortify.getValue(), troops, ChoosePane.FORTIFY,
                    player.getId());
              }
              timer13.play();
            });

            timer13.setOnFinished(x -> {
              System.out.println("Fortify Called III");

              this.endPhaseTurn(this.gameState.getCurrentGamePeriod(),
                  this.gameState.getCurrentTurnPhase(), this.gameState.getCurrentPlayer().getId());
            });

            break;
          default:
            break;
        }
        break;
      default:
        break;
    }
  }

}
