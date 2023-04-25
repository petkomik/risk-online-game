package game.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import game.logic.GameLogic;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Player class to model the player entity
 *
 * @author jorohr
 */

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String color;
	private String avatar;
	private int id;
	private int rank;
	private HashMap<CountryName, Territory> ownedCountries;
	private ArrayList<Continent> ownedContinents;
	private int sumOfAllTroops;
	private int troopsAvailable;
	private ArrayList<Card> cards;
	private LocalDateTime gameEndTime;
	private boolean canContinuePlaying = true;
	private volatile boolean cardsTurningInPhase;
	private volatile boolean initialPlacementPhase;
	private volatile boolean preparationPhase;
	private volatile boolean attackPhase;
	private volatile boolean cardThisRound;
	private volatile boolean fortificationPhase;

	public Player(String name, int id) {
		ownedCountries = new HashMap<>();
		ownedContinents = new ArrayList<>();
		cards = new ArrayList<>();
		this.name = name;
		this.id = id;
		// set Random avatar
	}
	
	public Player(String name, int id, String imagePath) {
		ownedCountries = new HashMap<>();
		ownedContinents = new ArrayList<>();
		cards = new ArrayList<>();
		this.name = name;
		this.id = id;
		this.avatar = imagePath;
	}

	/**constructor for defensive copying*/
	public Player(Player player) {
	    this.name = new String(player.getName());
	    this.color = player.getColor();
	    this.id = player.getID();
	    this.rank = player.getRank();
	    this.ownedCountries = new HashMap<CountryName, Territory>(player.getOwnedCountries());
	    this.ownedContinents = new ArrayList<Continent>(player.getOwnedContinents());
	    this.sumOfAllTroops = player.getSumOfAllTroops();
	    this.troopsAvailable = player.getTroopsAvailable();
	    this.cards = player.getCards().stream()
                .map(card -> new Card(card, this))
                .collect(Collectors.toCollection(ArrayList::new));
	    this.gameEndTime = player.getGameEndTime();
	    this.canContinuePlaying = player.isCanContinuePlaying();
	    this.cardsTurningInPhase = player.isCardsTurningInPhase();
	    this.initialPlacementPhase = player.isInitialPlacementPhase();
	    this.preparationPhase = player.isPreparationPhase();
	    this.attackPhase = player.isAttackPhase();
	    this.cardThisRound = player.isCardThisRound();
	    this.fortificationPhase = player.isFortificationPhase();
	    this.avatar = player.avatar;
	}


	public void incrementRank() {
		rank += 1;
	}

	public int getRank() {
		return rank;
	}

	public int getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getTroopsAvailable() {
		return troopsAvailable;
	}

	public void setTroopsAvailable(int troopsAvailable) {
		this.troopsAvailable = troopsAvailable;
	}

	public void addTroopsAvailable(int number) {
		this.troopsAvailable += number;
	}

	public int getSumOfAllTroops() {
		return sumOfAllTroops;
	}

	public void setSumOfAllTroops(int sumOfAllTroops) {
		this.sumOfAllTroops = sumOfAllTroops;
	}

	public void removeTroopsAvailable(int numberOfTroops) {
		this.troopsAvailable -= numberOfTroops;
	}

	public HashMap<CountryName, Territory> getOwnedCountries() {
		return ownedCountries;
	}

	public void addAndUpdateOwnedCountries(Territory territory) {
		if (territory.getOwnedByPlayer() != null && territory.getOwnedByPlayer() != this) {
			territory.getOwnedByPlayer().getOwnedCountries().remove(territory.getCountryName(), territory);
			territory.getOwnedByPlayer().updateOwnedContinents(GameLogic.getContinents());
		}
		territory.setOwnedByPlayer(this);
		this.ownedCountries.put(territory.getCountryName(), territory);
		updateOwnedContinents(GameLogic.getContinents());
	}

	public ArrayList<Continent> getOwnedContinents() {
		return ownedContinents;
	}

	private void updateOwnedContinents(HashMap<Continent, ArrayList<Territory>> continents) {
		// Create a HashMap to store the number of territories owned by the player in
		// each continent
		HashMap<Continent, Integer> territoriesOwnedInContinent = new HashMap<>();
		for (Continent continent : continents.keySet()) {
			territoriesOwnedInContinent.put(continent, 0);
		}

		// Count the number of territories owned by the player in each continent
		for (Territory territory : this.getOwnedCountries().values()) {
			Continent continent = territory.getContinent();
			int numTerritoriesOwned = territoriesOwnedInContinent.get(continent);
			territoriesOwnedInContinent.put(continent, numTerritoriesOwned + 1);
		}

		// Check if the player owns all territories in each continent and update the
		// player's ownedContinents accordingly
		for (Continent continent : continents.keySet()) {
			ArrayList<Territory> territoriesInContinent = continents.get(continent);
			int numTerritoriesInContinent = territoriesInContinent.size();
			int numTerritoriesOwned = territoriesOwnedInContinent.get(continent);
			if (numTerritoriesOwned == numTerritoriesInContinent) {
				ownedContinents.add(continent);
			} else {
				ownedContinents.remove(continent);
			}
		}
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public boolean isCanContinuePlaying() {
		return canContinuePlaying;
	}

	public void setCanContinuePlaying(boolean stillActive) {
		this.canContinuePlaying = stillActive;
		gameEndTime = LocalDateTime.now();
	}

	public LocalDateTime getGameEndTime() {
		return gameEndTime;

	}

	public boolean isCardsTurningInPhase() {
		return cardsTurningInPhase;
	}

	public void setCardsTurningInPhase(boolean cardsTurningInPhase) {
		this.cardsTurningInPhase = cardsTurningInPhase;
	}

	public boolean isInitialPlacementPhase() {
		return initialPlacementPhase;
	}

	public void setInitialPlacementPhase(boolean initialPlacementPhase) {
		this.initialPlacementPhase = initialPlacementPhase;
	}

	public boolean isPreparationPhase() {
		return preparationPhase;
	}

	public void setPreparationPhase(boolean preparationPhase) {
		this.preparationPhase = preparationPhase;
	}

	public boolean isAttackPhase() {
		return attackPhase;
	}

	public void setAttackPhase(boolean attackPhase) {
		this.attackPhase = attackPhase;
	}

	public boolean isFortificationPhase() {
		return fortificationPhase;
	}

	public void setFortificationPhase(boolean fortificationPhase) {
		this.fortificationPhase = fortificationPhase;
	}

	public void addCard(Card card) {
		this.cards.add(card);
	}

	public void removeCards(ArrayList<Card> cards) {
		this.cards.removeAll(cards);
	}

	public boolean isCardThisRound() {
		return cardThisRound;
	}

	public void setCardThisRound(boolean cardThisRound) {
		this.cardThisRound = cardThisRound;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setOwnedContinents(ArrayList<Continent> ownedContinents) {
		this.ownedContinents = ownedContinents;
	}
	
	public String getAvatar() {
		return this.avatar;
	}
	
	public void setAvatar(String imagePath) {
		this.avatar = imagePath;
	}

}
