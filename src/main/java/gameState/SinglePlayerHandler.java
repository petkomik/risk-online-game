package gameState;

import java.util.List;

import game.Lobby;
import game.gui.GamePaneController;
import game.models.Card;
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
	
	public void clickCountry(int idOfPlayer, CountryName country) {
		this.gameHandler.clickCountry(idOfPlayer, country);
	}
	
	public void possesCountryOnGUI(CountryName country, Color colorOfPlayer) {
//		this.gamePaneController.claimCountry(country.toString(),
//				1, colorOfPlayer);
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
	
	public int getInitialThrowDice(int idOfPlayer) {
		return this.gameHandler.getInitialThrowDice(idOfPlayer);
	}
	
	/*
	 * gives the GUI the current Player
	 */
	public void setCurrentPlayerOnGUI(int idOfPlayer) {
		
	}
	
	public void turnInRiskCards(List<Card> cards, int idOfPlayer) {
		this.gameHandler.turnInRiskCards(cards, idOfPlayer);
	}
	
	public Lobby getLobby() {
		return lobby;
	}

	public void riskCardsTurnedInOnGui(int idOfPlayer, List<Card> cards,
			int troopsPlyaerGets) {
		// - removeRiskCard(List<Card> toRemove, Player player)
		// - addAmountOfTroopsLeftToDeploy(Player player,  int number)

	}
}
