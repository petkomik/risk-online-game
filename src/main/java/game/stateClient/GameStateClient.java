package game.stateClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.Territory;

/**
 * Class for the gameState for client-logic/representation
 * @author srogalsk
 *
 */

public class GameStateClient {
	private HashMap<CountryName, Territory> territories;
	private HashMap<Continent, ArrayList<Territory>> continents;
	private HashMap<Integer, Player> players;
	private ArrayList<Card> cards;
	private int numberOfCardsTurnedIn;
	private boolean gameIsOver;

	private LocalDateTime gameTimer;
	private volatile Player currentPlayer;
	private volatile Player clientPlayer;
	
	public GameStateClient(HashMap<CountryName, Territory> territories, HashMap<Continent, ArrayList<Territory>> continents, HashMap<Integer, Player> players, ArrayList<Card> cards, Player clientPlayer) {
		this.territories = territories;
		this.continents = continents;
		this.players = players;
		this.cards = cards;
		this.clientPlayer = clientPlayer;
	}
	
	public void updateCountry(Territory territory) {
		territories.put(territory.getCountryName(), territory);
	}
	
	public void updatePlayer(Integer id , Player player) {
		players.put(id, player);
	}
	
	public void updateNumberOfCardsTurnedIn(int status) {
		numberOfCardsTurnedIn = status;
	}
	
	public void updateGameIsOver(boolean gameIsOver) {
		this.gameIsOver = gameIsOver;
	}
	
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public boolean getUpdateGameIsOver() {
		return this.gameIsOver;
	}
	
	public int getNumberOfCardsTurnedIn() {
		return this.numberOfCardsTurnedIn;
	}
	
	public Player getClientPlayer() {
		return clientPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public HashMap<Continent, ArrayList<Territory>> getContinents() {
		return continents;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

}
