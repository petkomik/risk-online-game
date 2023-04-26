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
	private HashMap<Continent, ArrayList<CountryName>> continents;
	private HashMap<Integer, Player> players;
	private ArrayList<Card> cards;
	private int numberOfCardsTurnedIn;
	private boolean gameIsOver;
	private ArrayList<Player> alivePlayers;
//	private int dicesAttacker[];
//	private int dicesDefender[];

//	private LocalDateTime gameTimer;
	private Player currentPlayer;
	private Phase currentTurnPhase;
	private Period currentGamePeriod;
	private HashMap<Player, Integer> playersDiceThrown;
	private HashMap<Player, Integer> playerTroopsLeft;

	public GameState(Lobby lobby) {
		continents = new HashMap<Continent, ArrayList<CountryName>>();
		cards = new ArrayList<Card>();
		territories = new HashMap<CountryName, Territory>();
		players = new HashMap<Integer, Player>();
		alivePlayers = new ArrayList<Player>();
		playerTroopsLeft = new HashMap<Player, Integer>();
		playersDiceThrown = new HashMap<Player, Integer>();
		numberOfCardsTurnedIn = 0;
		currentTurnPhase = null;
		currentGamePeriod = Period.COUNTRYPOSESSION;
		gameIsOver = false;
		
		SetTerritories.createCardDeck(cards);
		SetTerritories.createTerritories(territories);
		SetTerritories.setNeighboringCountrys(territories);
		SetTerritories.createContinents(continents, territories);

		for (Player player : lobby.getPlayerList()) {
			this.players.put(player.getID(), player);
			this.alivePlayers.add(player);
		}
		
	}
	
	public HashMap<Player, Integer> getPlayersDiceThrown() {
		return playersDiceThrown;
	}

	public void setPlayersDiceThrown(HashMap<Player, Integer> playersDiceThrown) {
		this.playersDiceThrown = playersDiceThrown;
	}
	
	public void addTroopsToPlayer(Player player, int numberOfTroops){
		this.getPlayerTroopsLeft().put(player,
				this.getPlayerTroopsLeft().get(player) + numberOfTroops);
	}
	
	public void subtractTroopsToPlayer(Player player, int numberOfTroops){
		if(this.playerTroopsLeft.get(player) >= numberOfTroops) {
		this.getPlayerTroopsLeft().put(player,
				this.getPlayerTroopsLeft().get(player) - numberOfTroops);
		}else {
			System.out.println("Tried to remove more troops than available");
		}
		
	}

	public void updateTerritory(CountryName countryName, Player player, int numberTroops) {
		this.territories.get(countryName).setOwnedByPlayer(player);
		this.territories.get(countryName).addNumberOfTroops(numberTroops);
	}
	
	public void updateTroopsOnTerritory(CountryName countryName, int numberTroops) {
		this.territories.get(countryName).setNumberOfTroops(numberTroops);
	}
	
	public void setInitialTroops(int troops) {
		for(Player player : this.players.values()) {
			this.playerTroopsLeft.put(player, 0);
		}
		for(Player player : this.players.values()) {
			this.addTroopsToPlayer(player, troops);
		}
	}
	
	public void setOwnedByTerritory(CountryName country, Player player) {
		this.territories.get(country).setOwnedByPlayer(player);
	}

	public HashMap<CountryName, Territory> getTerritories() {
		return territories;
	}

	public HashMap<Continent, ArrayList<CountryName>> getContinents() {
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

	public ArrayList <Player> getAlivePlayers() {
		return alivePlayers;
	}

	public Phase getCurrentTurnPhase() {
		return currentTurnPhase;
	}

	public void setCurrentTurnPhase(Phase currentTurnPhase) {
		this.currentTurnPhase = currentTurnPhase;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Period getCurrentGamePeriod() {
		return currentGamePeriod;
	}

	public void setCurrentGamePeriod(Period currentGamePeriod) {
		this.currentGamePeriod = currentGamePeriod;
	}

	public HashMap<Player, Integer> getPlayerTroopsLeft() {
		return playerTroopsLeft;
	}

	public void setNextPlayer() {
		this.currentPlayer = this.alivePlayers.get((this.alivePlayers.indexOf(currentPlayer)+1)%this.alivePlayers.size());
	}
}