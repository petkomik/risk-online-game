package gameState;

import game.Lobby;
import game.gui.GamePaneController;
import game.models.CountryName;
import game.models.Player;
import javafx.scene.paint.Color;

public class SinglePlayerHandler {
	
	GameHandler gameHandler;
	GamePaneController gamePaneController;
	Lobby lobby;

	public SinglePlayerHandler(Lobby lobby) {
		gameHandler = new GameHandler(lobby);
		this.lobby = lobby;
		
	}
	
	public GameHandler getGameHandler() {
		return gameHandler;
	}

	public void clickCountry(int id, CountryName country) {
		this.gameHandler.clickCountry(id, country);
	}
	
	public void possesCountryOnGUI(CountryName country, int id, int numTroopsOfPlayer) {
		this.gamePaneController.claimCountry(country, id);
		this.gamePaneController.addAmountOfTroopsLeftToDeploy(numTroopsOfPlayer);
	}
	
	public void setPeriod(Period period) {
		this.gamePaneController.setPeriod(period);
	}
	
	public void chooseNumberOfTroopsOnGUI(CountryName country,int min, int max) {
		// show menu in the gui where the player chooses 
		//how many troops to deploy on territory
		//max is the number of troops that the player has left
		// min is 1 (i think)
		//this.gamePaneController.setNumTroops(country.toString(), max); Idee
	}
	
	public void addTroopsToCountry(CountryName country,int troops) {
		this.gameHandler.addTroopsToCountry(country, troops);
	}

	public void setGamePaneController(GamePaneController gamePaneController) {
		this.gamePaneController = gamePaneController;
	}
	
	/*
	 * gets called from GUI when player throws initial dice to 
	 * decide who gets to be first 
	 * returns the value of the dice for the player 
	 */
	
	public int getInitialThrowDice(Player player) {
		return this.gameHandler.getInitialThrowDice(player);
	}
	
	/*
	 * gives the GUI the current Player
	 */
	public void setCurrentPlayerOnGUI(int id) {
		this.gamePaneController.setCurrentPlayer(id);
	}
	
	public void initialDeployOnGUI(CountryName countryName, int numTroopsOfCountry, int numTroopsOfPlayer) {
		this.gamePaneController.setNumTroops(countryName, numTroopsOfCountry);
		this.gamePaneController.addAmountOfTroopsLeftToDeploy(numTroopsOfPlayer);
	}
	
	public Lobby getLobby() {
		return lobby;
	}
}
