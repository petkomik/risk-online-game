package gameState;

import java.util.ArrayList;
import java.util.List;

import game.Lobby;
import game.exceptions.WrongCardsException;
import game.exceptions.WrongCardsSetException;
import game.exceptions.WrongCountryException;
import game.exceptions.WrongPeriodException;
import game.exceptions.WrongPhaseException;
import game.exceptions.WrongTroopsCountException;
import game.gui.GamePaneController;
import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import javafx.scene.paint.Color;

public class SinglePlayerHandler {
	
	private GameHandler gameHandler;
	private Lobby lobby;
	private GamePaneController gamePaneController;

	public SinglePlayerHandler(Lobby lobby, GamePaneController gamePaneController) {
		this.gameHandler = new GameHandler(lobby);
		this.gameHandler.initSingleplayer(this);
		this.gamePaneController = gamePaneController;
		this.lobby = lobby;
		
	}
	
	/*
	 * COUNTRYPOSESSION  Period
	 * INITIALREINFORCEMENT Period
	 * MAINPERIOD Period
	 */
	
	public void clickCountry(int id, CountryName country) {
		this.gameHandler.clickCountry(id, country);
	}
	

	/*
	 * Called in the action event of confirm numbe ChoosingTroopsPane
	 * REINFORCE, ATTACK, FORTIFY
	 */
	
	public void confirmNumberOfTroops(CountryName country, int troops, ChoosePane choosePane, int idOfPlayer) {
		this.gameHandler.confirmTroopsToCountry(country, troops, choosePane, idOfPlayer);
	}
	
	/*
	 * Called when player turns in risk cards, player's turn
	 * REINFORCE Phase
	 */

	public void turnInRiskCards(ArrayList<Card> cards, int idOfPlayer) {
		try {
			this.gameHandler.turnInRiskCards(cards, idOfPlayer);
		} catch (Exception e) {
			this.showExeceptionOnGUI(e);
		}
	}
	
	/*
	 * Called to end any of the three phase from player with idOfPlayer
	 * during his turn, calling during Fortify ends turn
	 * REINFORCE, ATTACK, FORTIFY
	 */
	
	public void endPhaseTurn(Period period, Phase phase, int idOfPlayer) {
		this.gameHandler.endPhaseTurn(period, phase, idOfPlayer);
	}
	
	/*
	 * Called from attacking player during his attack
	 * Dice animation starts for him
	 * Tell everyone to start animatio
	 * Decide dice values and transmit them to everyone
	 * ATTACK Phase
	 */
	
	public void battleDiceThrow(int[] numberOfDices) {
		// TODO
	}
	
	
	/*
	 * gets called from GUI when player throws initial dice to 
	 * decide who gets to be first 
	 * returns the value of the dice for the player
	 * 
	 * Input DICE_THROW Period 
	 * 
	 * INPUT + OUTPUT to GUI
	 */
	
	public void playerThrowsInitialDice(int idOfPlayer) {
		this.gameHandler.playerThrowsInitialDice(idOfPlayer);
	}
	
	public void rollInitialDiceOnGUI(int idOfPlayer, int i) {
		this.gamePaneController.rollInitialDice(idOfPlayer, i);
	}

	public void showExeceptionOnGUI(Exception e) {
		this.gamePaneController.showException(e.toString());
	}
	
	/*
	 * Sets the the given period on the GUI. Based on the period
	 * the bottom pane displays different info / buttons
	 * 
	 * @param period the period to be set
	 */
	
	public void setPeriodOnGUI(Period period) {
		this.gamePaneController.setPeriod(period);
	}
	
	/*
	 * Sets the the given phase on the GUI. Phases are relevant only during
	 * MAINPERIOD, follow a specific order
	 * 
	 * @param phase the phase to be set
	 */
	
	public void setPhaseOnGUI(Phase phase) {
		this.gamePaneController.setPhase(phase);
	}
	
	/*
	 * Paint the country in the color of player with id and places 
	 * one troop on it. 
	 * 
	 * @param country the CountryName enum of the territory to be claimed
	 * @param id 	  the id of player that has claimed territory
	 */
	
	public void possesCountryOnGUI(CountryName country, int id) {
		this.gamePaneController.claimCountry(country, id);
	}
	
	/*
	 * Changes the current player on the GUI by swapping to the correct
	 * color and avatar, and setting the corrct number of troops
	 * 
	 * @param id id of the player
	 * @param troopsLeft the amount of troops the player has to deploy
	 */
	public void setCurrentPlayerOnGUI(int id, int troopsLeft) {
		this.gamePaneController.setCurrentPlayer(id);
		this.gamePaneController.setAmountOfTroopsLeftToDeploy(troopsLeft);
	}
	
	public void chnagePlayerOnGUI(int id, ArrayList<Card> cards) {
		this.gamePaneController.setPlayerOnGUI(id, cards);
	}

	/*
	 * Opens a menu where the player chooses how many troops to deploy
	 * 
	 * @param 
	 */
	

	public void chooseNumberOfTroopsOnGUI(CountryName country,int min, int max, ChoosePane choosePane) {
		this.gamePaneController.showChoosingTroopsPane(country, min, max, choosePane);
	}
	
	public void setTroopsOnTerritoryAndLeftOnGUI(CountryName countryName, int numTroopsOfCountry, int numTroopsOfPlayer) {
		this.gamePaneController.setNumTroops(countryName, numTroopsOfCountry);
		this.gamePaneController.setAmountOfTroopsLeftToDeploy(numTroopsOfPlayer);
	}
	
	public void moveTroopsFromTerritoryToOtherOnGUI(CountryName from, CountryName to, int number) {
		//player should own both already
		//simply move from one to other
	}
	
	public void openBattleFrameOnGUI(Continent continentAt, CountryName countryNameAt, 
								Continent continentDf, CountryName countryNameDf,
								Player playerAt, Player playerDf, boolean attackerGui,
								int troopsAt, int troopsDf) {
		// sets the battle visible and initiates it with these parameters
	}
	
	public void endBattleOnGUI() {
		//makes battle screne invisible
	}
	
	public void riskCardsTurnedInSuccessOnGUI(ArrayList<Card> card, int idOfPlayer, int bonusTroops) {
	}
	
	public void receiveRiskCardOnGUI(Card card, int idOfPlayer) {
		
	}
	
	public void selectTerritoryAndSetDisabledTerritoriesOnGUI(CountryName countryName, 
			ArrayList<CountryName> unreachableCountries) {
		// for attack and fortify phase when player clicks on the "from" territory
	}
	
	public void resetPhaseOnGUI() {
		//the player clicked cancel in confirm number of troops
		//for fortify disable all enemy territory, enable all owned, remove selecte
		//attack other way around
	}


}
