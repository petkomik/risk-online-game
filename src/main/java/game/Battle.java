package game;

import java.io.Serializable;

import game.logic.GameType;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import gameState.SinglePlayerHandler;

/**
 * class for modelling a battle and saving it in the state
 * @author pmikov
 *
 */
public class Battle implements Serializable {

	private static final long serialVersionUID = 1L;
	private Continent continentAt;
	private CountryName countryNameAt;
	private	Continent continentDf;
	private CountryName countryNameDf;
	private String attackingPNG;
	private String defendingPNG;
	private int troopsInAttackAt;
	private int troopsInAttackDf;
	private String attackingAvatar;
	private String defendingAvatar;
	private String attackerColor;
	private String defenderColor;
	private int maxDiceToThrow;
	private int defendingDice;
	private int[] dicesAttacker;
	private int[] dicesDefender;
	private GameType gameType; 
	private int attackerID;
	
	public Battle(Continent continentAt, CountryName countryNameAt, Continent continentDf, CountryName countryNameDf,
			String attackingPNG, String defendingPNG, int troopsInAttackAt, int troopsInAttackDf,
			String attackingAvatar, String defendingAvatar, String attackerColor, String defenderColor, 
			int maxDiceToThrow, int defendingDice, GameType gameType, int attackerID) {
		super();
		this.continentAt = continentAt;
		this.countryNameAt = countryNameAt;
		this.continentDf = continentDf;
		this.countryNameDf = countryNameDf;
		this.attackingPNG = attackingPNG;
		this.defendingPNG = defendingPNG;
		this.troopsInAttackAt = troopsInAttackAt;
		this.troopsInAttackDf = troopsInAttackDf;
		this.attackingAvatar = attackingAvatar;
		this.defendingAvatar = defendingAvatar;
		this.attackerColor = attackerColor;
		this.defenderColor = defenderColor;
		this.maxDiceToThrow = maxDiceToThrow;
		this.defendingDice = defendingDice;
		this.dicesAttacker = dicesAttacker;
		this.dicesDefender = dicesDefender;
		this.gameType = gameType;
		this.attackerID = attackerID;
	}
	
	public int getAttackerID() {
		return attackerID;
	}

	public void setAttackerID(int attackerID) {
		this.attackerID = attackerID;
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

	public String getAttackingPNG() {
		return attackingPNG;
	}

	public void setAttackingPNG(String attackingPNG) {
		this.attackingPNG = attackingPNG;
	}

	public String getDefendingPNG() {
		return defendingPNG;
	}

	public void setDefendingPNG(String defendingPNG) {
		this.defendingPNG = defendingPNG;
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

	public int[] getDicesAttacker() {
		return dicesAttacker;
	}

	public void setDicesAttacker(int[] dicesAttacker) {
		this.dicesAttacker = dicesAttacker;
	}

	public int[] getDicesDefender() {
		return dicesDefender;
	}

	public void setDicesDefender(int[] dicesDefender) {
		this.dicesDefender = dicesDefender;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	
	
}
