package gameState;

import java.util.ArrayList;
import java.util.List;

import game.Lobby;
import game.gui.GamePaneController;
import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import javafx.scene.paint.Color;

public class SinglePlayerHandler {
	
	private GameHandler gameHandler;
	private Lobby lobby;
	GamePaneController gamePaneController;

	public SinglePlayerHandler(Lobby lobby, GamePaneController gamePaneController) {
		this.gameHandler = new GameHandler(lobby);
		this.gamePaneController = gamePaneController;
		this.lobby = lobby;
		
	}
	
	public GameHandler getGameHandler() {
		return gameHandler;
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
	
	public void confirmNumberOfTroops(CountryName country, int troops, ChoosePane choosePane) {
		//this.gameHandler.addTroopsToCountry(country, troops, choosePane);
	}
	
	/*
	 * Called when player turns in risk cards, player's turn
	 * REINFORCE Phase
	 */

	public void turnInRiskCards(List<Card> cards, int idOfPlayer) {
		//this.gameHandler.turnInRiskCards(cards, idOfPlayer);
	}
	
	/*
	 * Called to end any of the three phase from player with idOfPlayer
	 * during his turn, calling during Fortify ends turn
	 * REINFORCE, ATTACK, FORTIFY
	 */
	
	public void endPhase(Phase phase, int idOfPlayer) {
		//this.gameHandler.endPhase(phase, idOfPlayer);
	}
	
	/*
	 * Called from attacking player during his attack
	 * Dice animation starts for him
	 * Tell everyone to start animatio
	 * Decide dice values and transmit them to everyone
	 * ATTACK Phase
	 */
	
	public void battleDiceThrow(int idOfPlayer) {
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
	
	public int getInitialThrowDice(Player player) {
		return this.gameHandler.getInitialThrowDice(player);
	}
	
	/*
	 * 
	 */
	
	public void setPeriodOnGUI(Period period) {
		this.gamePaneController.setPeriod(period);
	}
	
	public void setPhaseOnGUI(Phase phase) {
		this.gamePaneController.setPhase(phase);
	}
	
	public void possesCountryOnGUI(CountryName country, int id) {
		this.gamePaneController.claimCountry(country, id);
	}
	
	/*
	 * gives the GUI the current Player
	 */
	public void setCurrentPlayerOnGUI(int id, int troopsLeft) {
		this.gamePaneController.setCurrentPlayer(id);
		this.gamePaneController.setAmountOfTroopsLeftToDeploy(troopsLeft);
	}


	public void chooseNumberOfTroopsOnGUI(CountryName country,int min, int max, ChoosePane choosePane) {
		this.gamePaneController.showChoosingTroopsPane(country, min, max, choosePane);
	}
	
	public void initialDeployOnGUI(CountryName countryName, int numTroopsOfCountry, int numTroopsOfPlayer) {
		this.gamePaneController.setNumTroops(countryName, numTroopsOfCountry);
		this.gamePaneController.setAmountOfTroopsLeftToDeploy(numTroopsOfPlayer);
	}
	
	public void reinforceOnGUI(CountryName countryName, int numTroopsOfCountry, int numTroopsOfPlayer) {
		this.gamePaneController.setNumTroops(countryName, numTroopsOfCountry);
		this.gamePaneController.setAmountOfTroopsLeftToDeploy(numTroopsOfPlayer);
	}
	
	public void riskCardsTurnedInSuccessOnGUI(ArrayList<Card> card, int idOfPlayer, int bonusTroops) {
		// remove risk cards from player, update troops to deploy
	}
	
	public void playerCanAttackFromCountryOnGUI(CountryName countryName, 
			ArrayList<CountryName> attackableCountries) {
		
	}
	
	public void playerCanAttackSelectedCountryOnGUI(CountryName country,int min, int max, ChoosePane choosePane) {
		this.gamePaneController.showChoosingTroopsPane(country, min, max, choosePane);
	}
	
	public void openBattleFrameOnGUI(Continent continentAt, CountryName countryNameAt, 
								Continent continentDf, CountryName countryNameDf,
								Player playerAt, Player playerDf, boolean attackerGui,
								int troopsAt, int troopsDf) {
		// sets the battle visible and initiates it with these parameters
	}
	
	public void successfulAttackOnGUI(CountryName countryFrom, CountryName countryTo,
			int min, int max, ChoosePane choosePane) {
		
	}
	
	public void failedAttackOnGUI(CountryName countryAt, CountryName countryDf, 
			int troopsInAttacking, int troopsInDefender) {
		
	}
	
	public void receiveRiskCardOnGUI(Card card, int idOfPlayer) {
		
	}
	
	public void playerCanFortifyFromCountryOnGUI(CountryName countryName, 
			ArrayList<CountryName> fortifiableCountries) {
		
	}
	
	public void playerCanFortifySelectedCountryOnGUI(CountryName countryFrom, CountryName countryTo,
			int min, int max, ChoosePane choosePane) {
		//this.gamePaneController.showChoosingTroopsPane(country, min, max, choosePane);
	}
	
	public void movingTroopsConfirmedOnGUI(CountryName countryFrom, CountryName countryTo,
			int min, int max) {
		
	}
	
	public Lobby getLobby() {
		return lobby;
	}
}
