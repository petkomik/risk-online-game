package gameState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import game.Lobby;
import game.gui.BattleFrameController;
import game.gui.GamePaneController;
import game.logic.GameLogic;
import game.logic.GameType;
import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.Territory;
import general.AppController;

public class GameState {

	/**
	 * Class for the gameState for client-logic/representation
	 * 
	 * @author petko,jo ,majd,petar & dignatov
	 *
	 */
	private HashMap<CountryName, Territory> territories;
	private HashMap<Continent, ArrayList<Territory>> continents;
	private HashMap<Integer, Player> players;
	private ArrayList<Card> cards;
	private int numberOfCardsTurnedIn;
	private boolean gameIsOver;
	private HashMap<Integer, Player> alivePlayers;
//	private int dicesAttacker[];
//	private int dicesDefender[];

//	private LocalDateTime gameTimer;
	private Player currentPlayer;

	public GameState(Lobby lobby) {

		SetTerritories.createContinents(continents, territories);
		SetTerritories.createCardDeck(cards);
		SetTerritories.createTerritories(territories);
		SetTerritories.setNeighboringCountrys(territories);

		for (Player player : lobby.getPlayerList()) {
			this.players.put(player.getID(), player);
		}

	}

	public void updateTerritorie(CountryName countryName, Player player, int numberTroops) {
		this.territories.get(countryName).setOwnedByPlayer(player);
		this.territories.get(countryName).addNumberOfTroops(numberTroops);

	}
	

	public HashMap<CountryName, Territory> getTerritories() {
		return territories;
	}
	
	public HashMap<Continent, ArrayList<Territory>> getContinents() {
		return continents;
	}
	
	public HashMap<Integer, Player> getPlayers() {
		return players;
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public int getNumberOfCardsTurnedIn() {
		return numberOfCardsTurnedIn;
	}
	
	public boolean isGameIsOver() {
		return gameIsOver;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public HashMap<Integer, Player> getAlivePlayers() {
		return alivePlayers;
	}
}
