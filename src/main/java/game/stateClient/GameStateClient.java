package game.stateClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
	private int dicesAttacker[];
	private int dicesDefender[];

	private LocalDateTime gameTimer;
	private volatile Player currentPlayer;
	private volatile Player clientPlayer;
	
	public GameStateClient(HashMap<CountryName, Territory> territories, HashMap<Continent, ArrayList<Territory>> continents, HashMap<Integer, Player> players, ArrayList<Card> cards, Player clientPlayer) {
		this.territories = new HashMap<CountryName, Territory>(territories);
		this.continents = new HashMap<Continent, ArrayList<Territory>> (continents);
		this.players = new HashMap<Integer, Player> (players);
		this.cards = new ArrayList<Card>(cards);
		this.clientPlayer = new Player(clientPlayer);
	}
	
	/** handle with care
	public void updateCountry(Territory territory) {
		territories.put(territory.getCountryName(), territory);
	}
	*/
	
	/** handle with care
	public void updatePlayer(Integer id , Player player) {
		players.put(id, player);
	}
	*/
	
	public void updateNumberOfCardsTurnedIn(int status) {
		numberOfCardsTurnedIn = status;
		// set Number of Cards turned in accordingly
	}
	
	public void updateGameIsOver(boolean gameIsOver) {
		this.gameIsOver = gameIsOver;
		// show endscreen
	}
	
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		// highlight current Player
	}
	
	public boolean getUpdateGameIsOver() {
		return this.gameIsOver;
		// show endscreen game
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
	
	public String getNameOfPlayer(int id) {
	    return players.get(id).getName();
	}

	/** handle with care
	public void setNameOfPlayer(String name, int id) {
	    players.get(id).setName(name);
	}
	*/
	
	public void setDicesAttacker(int[] dicesAttacker) {
		this.dicesAttacker = dicesAttacker;
		//show dices Attacker
	}
	
	public void SetDicesDefender(int[] dicesDefender) {
		this.dicesDefender = dicesDefender;
		//show dices Defender
	}
	
	
	/** Method for getting all the countries that are owned by this player and are in someway connected to that current territory */
	public ArrayList<Territory> getConnectedTerritoriesOwnedByPlayer(Territory selectedCountry, Player player) {
	    ArrayList<Territory> connectedTerritories = new ArrayList<>();
	    Set<Territory> visited = new HashSet<>();
	    dfs(selectedCountry, visited, connectedTerritories, player);
	    return connectedTerritories;
	}

	private void dfs(Territory current, Set<Territory> visited, ArrayList<Territory> connectedTerritories, Player player) {
	    visited.add(current);
	    if (current.getOwnedByPlayer() == player) {
	        connectedTerritories.add(current);
	    }
	    for (Territory neighbor : current.getNeighboringTerritories()) {
	        if (!visited.contains(neighbor) && neighbor.getOwnedByPlayer() == player) {
	            dfs(neighbor, visited, connectedTerritories, player);
	        }
	    }
	}

	
	public void setColorOfPlayer(String color, int id) {
	    players.get(id).setColor(color);
	    // update Color in the GUI
	}

	public int getIdOfPlayer(int id) {
	    return players.get(id).getID();
	}
	
/** handle with care
	public void setIdOfPlayer(int id, int newId) {
	    players.get(id).id = newId;
	}
*/
	public int getRankOfPlayer(int id) {
	    return players.get(id).getRank();
	}

	public void setRankOfPlayer(int rank, int id) {
	    players.get(id).setRank(rank);
	    // display Rank of Player in GUI
	}

	public HashMap<CountryName, Territory> getOwnedCountriesOfPlayer(int id) {
	    return players.get(id).getOwnedCountries();
	}

	public void addAndUpdateCountriesOwnedOfPlayer(int id, Territory territory) {
		players.get(id).addAndUpdateOwnedCountries(territory);
		// set GUI accordingly and get Continents to highlight them
	}
	
	/** handle with care
	public void setOwnedCountriesOfPlayer(HashMap<CountryName, Territory> ownedCountries, int id) {
	    players.get(id).setownedCountries = ownedCountries;
	} */

	public ArrayList<Continent> getOwnedContinentsOfPlayer(int id) {
	    return players.get(id).getOwnedContinents();
	}

	/** handle with care
	public void setOwnedContinentsOfPlayer(ArrayList<Continent> ownedContinents, int id) {
	    players.get(id).setOwnedContinents(ownedContinents);
	}
	 */
	
	public int getSumOfAllTroopsOfPlayer(int id) {
	    return players.get(id).getSumOfAllTroops();
	}

	public void setSumOfAllTroopsOfPlayer(int sumOfAllTroops, int id) {
	    players.get(id).setSumOfAllTroops(sumOfAllTroops);
	    // set the amount of troops in the window
	}

	public int getTroopsAvailableOfPlayer(int id) {
	    return players.get(id).getTroopsAvailable();
	}

	public void setTroopsAvailableOfPlayer(int troopsAvailable, int id) {
	    players.get(id).setTroopsAvailable(troopsAvailable);
	    //display the amount of troops left
	}

	/** returns just the cards of the client Players*/
	public ArrayList<Card> getCardsOfPlayer(int id) {
	    return players.get(id).getCards();
	    // display the cards the Player has if the id is clientPlayer
	}

	public void addCardOfPlayer(Card card, int id) {
	    players.get(id).addCard(card);
	    // show a card if the id is clientPlayer
	}

	public boolean isCanContinuePlayingOfPlayer(int id) {
	    return players.get(id).isCanContinuePlaying();
	}

	public void setCanContinuePlayingOfPlayer(boolean canContinuePlaying, int id) {
	    players.get(id).setCanContinuePlaying(canContinuePlaying);
	    // show something like you lost or you won according to anything else if id is clientplayer else just grey the other player
	}

	public boolean isCardsTurningInPhaseOfPlayer(int id) {
	    return players.get(id).isCardsTurningInPhase();
	}

	public void setCardsTurningInPhaseOfPlayer(boolean cardsTurningInPhase, int id) {
	    players.get(id).setCardsTurningInPhase(cardsTurningInPhase);
	    // display if the id is clientPlayer && currentPlayer the turn in Cards Button accordingly
	}

	public boolean isInitialPlacementPhaseOfPlayer(int id) {
	    return players.get(id).isInitialPlacementPhase();
	}

	public void setInitialPlacementPhaseOfPlayer(boolean initialPlacementPhase, int id) {
	    players.get(id).setInitialPlacementPhase(initialPlacementPhase);
	 // display if the id is clientPlayer && currentPlayer the Phase Button accordingly
	}

	public boolean isPreparationPhaseOfPlayer(int id) {
	    return players.get(id).isPreparationPhase();
	}

	public void setPreparationPhaseOfPlayer(boolean preparationPhase, int id) {
	    players.get(id).setPreparationPhase(preparationPhase);
		 // display if the id is clientPlayer && currentPlayer the Phase Button accordingly
	}

	public boolean isAttackPhaseOfPlayer(int id) {
	    return players.get(id).isAttackPhase();
	}

	public void setAttackPhaseOfPlayer(boolean attackPhase, int id) {
	    players.get(id).setAttackPhase(attackPhase);
		 // display if the id is clientPlayer && currentPlayer the Phase Button accordingly
	}

	public boolean isCardThisRoundOfPlayer(int id) {
	    return players.get(id).isCardThisRound();
	}

	public void setCardThisRoundOfPlayer(boolean cardThisRound, int id) {
	    players.get(id).setCardThisRound(cardThisRound);
	    // grey out the button of turning in cards
	}

	public boolean isFortificationPhaseOfPlayer(int id) {
	    return players.get(id).isFortificationPhase();
	}

	public void setFortificationPhaseOfPlayer(boolean fortificationPhase, int id) {
	    players.get(id).setFortificationPhase(fortificationPhase);
		 // display if the id is clientPlayer && currentPlayer the Phase Button accordingly
	}

}
