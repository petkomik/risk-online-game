package game.models;

import game.state.GameType;
import java.io.Serializable;

/**
 * Modells a batle during the game contains all the needed information to paint the GUI and keeping
 * record of the current battle state.
 *
 * @author pmikov
 *
 */

public class Battle implements Serializable {

  private static final long serialVersionUID = 1L;
  private final int troopsInAttackAtFinal;
  private Continent continentAt;
  private CountryName countryNameAt;
  private Continent continentDf;
  private CountryName countryNameDf;
  private String attackingPng;
  private String defendingPng;
  private int troopsInAttackAt;
  private int troopsInAttackDf;
  private String attackingAvatar;
  private String defendingAvatar;
  private String attackerColor;
  private String defenderColor;
  private int maxDiceToThrow;
  private int defendingDice;
  private GameType gameType;
  private int attackerId;
  private int defenderId;

  /**
   * Consturctor for the class. Takes all the needed values as parameters.
   *
   * @author pmikov
   * @param continentAt Continent enum of the attacker
   * @param countryNameAt CountryName enum of the attacker
   * @param continentDf Continent enum of the defender
   * @param countryNameDf CountryName enum of the defender
   * @param attackingPng Address of the PNG of attacker territory
   * @param defendingPng Address of the PNG of defender territory
   * @param troopsInAttackAt Number of attacking troops
   * @param troopsInAttackDf Number of defending troops
   * @param attackingAvatar Address of the avatar of the attacker in resources
   * @param defendingAvatar Address of the avatar of the defender in resources
   * @param attackerColor Color of attacker as hex code
   * @param defenderColor Color of defender as hex code
   * @param maxDiceToThrow Max number of dice that the attacker can throw (<= 3)
   * @param defendingDice Max number of dice that the defender can throw (<= 2)
   * @param gameType GameType Enum value
   * @param attackerId ID of attacker profile
   * @param defenderId ID of defender profile
   */

  public Battle(Continent continentAt, CountryName countryNameAt, Continent continentDf,
      CountryName countryNameDf, String attackingPng, String defendingPng, int troopsInAttackAt,
      int troopsInAttackDf, String attackingAvatar, String defendingAvatar, String attackerColor,
      String defenderColor, int maxDiceToThrow, int defendingDice, GameType gameType,
      int attackerId, int defenderId) {
    super();
    this.continentAt = continentAt;
    this.countryNameAt = countryNameAt;
    this.continentDf = continentDf;
    this.countryNameDf = countryNameDf;
    this.attackingPng = attackingPng;
    this.defendingPng = defendingPng;
    this.attackingAvatar = attackingAvatar;
    this.defendingAvatar = defendingAvatar;
    this.attackerColor = attackerColor;
    this.defenderColor = defenderColor;
    this.troopsInAttackAtFinal = troopsInAttackAt;
    this.troopsInAttackAt = troopsInAttackAt;
    this.troopsInAttackDf = troopsInAttackDf;
    this.maxDiceToThrow = maxDiceToThrow;
    this.defendingDice = defendingDice;

    this.gameType = gameType;
    this.attackerId = attackerId;
    this.defenderId = defenderId;
  }

  public int getDefenderId() {
    return defenderId;
  }

  public int getAttackerId() {
    return attackerId;
  }

  public void setAttackerId(int attackerId) {
    this.attackerId = attackerId;
  }

  public Continent getContinentAt() {
    return continentAt;
  }

  public void setContinentAt(Continent continentAt) {
    this.continentAt = continentAt;
  }

  public CountryName getCountryNameAt() {
    return countryNameAt;
  }

  public void setCountryNameAt(CountryName countryNameAt) {
    this.countryNameAt = countryNameAt;
  }

  public Continent getContinentDf() {
    return continentDf;
  }

  public void setContinentDf(Continent continentDf) {
    this.continentDf = continentDf;
  }

  public CountryName getCountryNameDf() {
    return countryNameDf;
  }

  public void setCountryNameDf(CountryName countryNameDf) {
    this.countryNameDf = countryNameDf;
  }

  public String getAttackingPng() {
    return attackingPng;
  }

  public void setAttackingPng(String attackingPng) {
    this.attackingPng = attackingPng;
  }

  public String getDefendingPng() {
    return defendingPng;
  }

  public void setDefendingPng(String defendingPng) {
    this.defendingPng = defendingPng;
  }

  public int getTroopsInAttackAt() {
    return troopsInAttackAt;
  }

  public void setTroopsInAttackAt(int troopsInAttackAt) {
    this.troopsInAttackAt = troopsInAttackAt;
  }

  public int getTroopsInAttackDf() {
    return troopsInAttackDf;
  }

  public void setTroopsInAttackDf(int troopsInAttackDf) {
    this.troopsInAttackDf = troopsInAttackDf;
  }

  public String getAttackingAvatar() {
    return attackingAvatar;
  }

  public void setAttackingAvatar(String attackingAvatar) {
    this.attackingAvatar = attackingAvatar;
  }

  public String getDefendingAvatar() {
    return defendingAvatar;
  }

  public void setDefendingAvatar(String defendingAvatar) {
    this.defendingAvatar = defendingAvatar;
  }

  public String getAttackerColor() {
    return attackerColor;
  }

  public void setAttackerColor(String attackerColor) {
    this.attackerColor = attackerColor;
  }

  public String getDefenderColor() {
    return defenderColor;
  }

  public void setDefenderColor(String defenderColor) {
    this.defenderColor = defenderColor;
  }


  public int getMaxDiceToThrow() {
    return maxDiceToThrow;
  }

  public void setMaxDiceToThrow(int maxDiceToThrow) {
    this.maxDiceToThrow = maxDiceToThrow;
  }

  public int getDefendingDice() {
    return defendingDice;
  }

  public void setDefendingDice(int defendingDice) {
    this.defendingDice = defendingDice;
  }


  public GameType getGameType() {
    return gameType;
  }

  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  public int getTroopsInAttackAtFinal() {
    return troopsInAttackAtFinal;
  }
}
