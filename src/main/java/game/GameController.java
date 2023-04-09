package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.Territory;

public class GameController {
	private HashMap<CountryName, Territory> territories;
	private HashMap<Continent, ArrayList<Territory>> continents;
	private ArrayList<Player> players;
	private volatile Player currentPlayer;
	private int numberOfCardsTurnedIn;
	private boolean gameIsOver;

	// Konstruktor
	public GameController(ArrayList<Player> players) {
		this.players = players;
		createTerritories();
		createContinents();
	}

	public boolean turnInCards(ArrayList<Card> cards, Player player) {
		if (player == null || currentPlayer != player) {
			return false;
		} else if (cards == null || cards.size() != 3) {
			return false;
		} else if (!player.isCardsTurningInPhase()) {
			return false;
		} else if (!player.getCards().containsAll(cards)) {
			return false;
		} else if ((!cards.stream().allMatch(o -> o.getCardSymbol() == cards.get(0).getCardSymbol())) || (!cards
				.stream().map(Card::getCardSymbol).distinct().collect(Collectors.toSet()).equals(Set.of(1, 5, 10)))) {
			return false;
		}
		player.setCardsTurningInPhase(false);
		player.getCards().removeAll(cards);
		incrementNumberOfCardsTurnedIn();
		switch (getNumberOfCardsTurnedIn()) {
		case 1:
			player.addTroopsAvailable(4);
		case 2:
			player.addTroopsAvailable(6);
		case 3:
			player.addTroopsAvailable(8);
		case 4:
			player.addTroopsAvailable(10);
		case 5:
			player.addTroopsAvailable(12);
		case 6:
			player.addTroopsAvailable(15);
		default:
			player.addTroopsAvailable((getNumberOfCardsTurnedIn() - 6) * 5 + 15);
		}

		return true;
	}

	public boolean possessCountry(CountryName countryName, Player player) {
		if ((territories.get(countryName) == null) && (player.getTroopsAvailable() > 0)) {
			territories.get(countryName).setOwnedByPlayer(player);
			player.addOwnedCountries(territories.get(countryName));
			territories.get(countryName).addNumberOfTroops(1);
			player.removeTroopsAvailable(1);
			return true;
		}
		return false;
	}
	
	private static synchronized int getRandomDiceNumber() {
		return (int) (Math.random() * 6) + 1;
	}

	public int getNewTroopsCountForPlayer(Player player) {
		int troops = 0;
		ArrayList<Continent> ownedContinents = player.getOwnedContinents();

		troops += ownedContinents.size() / 3;
		if (ownedContinents != null) {
			for (Continent c : ownedContinents) {
				switch (c) {
				case Africa:
					troops += 3;
					break;
				case Asia:
					troops += 7;
					break;
				case Australia:
					troops += 2;
					break;
				case Europe:
					troops += 5;
					break;
				case NorthAmerica:
					troops += 5;
					break;
				case SouthAmerica:
					troops += 2;
					break;
				}
			}
		}
		// TODO checking for cards that are turned in
		return troops < 3 ? 3 : troops;
	}

	private void createTerritories() {
		// add territories for each country name
		territories.put(CountryName.Alaska, new Territory(CountryName.Alaska, Continent.NorthAmerica));
		territories.put(CountryName.Alberta, new Territory(CountryName.Alberta, Continent.NorthAmerica));
		territories.put(CountryName.NorthwestTerritory,
				new Territory(CountryName.NorthwestTerritory, Continent.NorthAmerica));
		territories.put(CountryName.Ontario, new Territory(CountryName.Ontario, Continent.NorthAmerica));
		territories.put(CountryName.Quebec, new Territory(CountryName.Quebec, Continent.NorthAmerica));
		territories.put(CountryName.WesternUnitedStates,
				new Territory(CountryName.WesternUnitedStates, Continent.NorthAmerica));
		territories.put(CountryName.EasternUnitedStates,
				new Territory(CountryName.EasternUnitedStates, Continent.NorthAmerica));
		territories.put(CountryName.CentralAmerica, new Territory(CountryName.CentralAmerica, Continent.NorthAmerica));
		territories.put(CountryName.Venezuela, new Territory(CountryName.Venezuela, Continent.SouthAmerica));
		territories.put(CountryName.Brazil, new Territory(CountryName.Brazil, Continent.SouthAmerica));
		territories.put(CountryName.Peru, new Territory(CountryName.Peru, Continent.SouthAmerica));
		territories.put(CountryName.Argentina, new Territory(CountryName.Argentina, Continent.SouthAmerica));
		territories.put(CountryName.Iceland, new Territory(CountryName.Iceland, Continent.Europe));
		territories.put(CountryName.Scandinavia, new Territory(CountryName.Scandinavia, Continent.Europe));
		territories.put(CountryName.GreatBritain, new Territory(CountryName.GreatBritain, Continent.Europe));
		territories.put(CountryName.NorthernEurope, new Territory(CountryName.NorthernEurope, Continent.Europe));
		territories.put(CountryName.WesternEurope, new Territory(CountryName.WesternEurope, Continent.Europe));
		territories.put(CountryName.SouthernEurope, new Territory(CountryName.SouthernEurope, Continent.Europe));
		territories.put(CountryName.Ukraine, new Territory(CountryName.Ukraine, Continent.Europe));
		territories.put(CountryName.Ural, new Territory(CountryName.Ural, Continent.Asia));
		territories.put(CountryName.Siberia, new Territory(CountryName.Siberia, Continent.Asia));
		territories.put(CountryName.Yakutsk, new Territory(CountryName.Yakutsk, Continent.Asia));
		territories.put(CountryName.Kamchatka, new Territory(CountryName.Kamchatka, Continent.Asia));
		territories.put(CountryName.Irkutsk, new Territory(CountryName.Irkutsk, Continent.Asia));
		territories.put(CountryName.Mongolia, new Territory(CountryName.Mongolia, Continent.Asia));
		territories.put(CountryName.Afghanistan, new Territory(CountryName.Afghanistan, Continent.Asia));
		territories.put(CountryName.Japan, new Territory(CountryName.Japan, Continent.Asia));
		territories.put(CountryName.China, new Territory(CountryName.China, Continent.Asia));
		territories.put(CountryName.Siam, new Territory(CountryName.Siam, Continent.Asia));
		territories.put(CountryName.India, new Territory(CountryName.India, Continent.Asia));
		territories.put(CountryName.MiddleEast, new Territory(CountryName.MiddleEast, Continent.Asia));
		territories.put(CountryName.Indonesia, new Territory(CountryName.Indonesia, Continent.Australia));
		territories.put(CountryName.NewGuinea, new Territory(CountryName.NewGuinea, Continent.Australia));
		territories.put(CountryName.EasternAustralia, new Territory(CountryName.EasternAustralia, Continent.Australia));
		territories.put(CountryName.WesternAustralia, new Territory(CountryName.WesternAustralia, Continent.Australia));
		territories.put(CountryName.NorthAfrica, new Territory(CountryName.NorthAfrica, Continent.Africa));
		territories.put(CountryName.Egypt, new Territory(CountryName.Egypt, Continent.Africa));
		territories.put(CountryName.EastAfrica, new Territory(CountryName.EastAfrica, Continent.Africa));
		territories.put(CountryName.Congo, new Territory(CountryName.Congo, Continent.Africa));
		territories.put(CountryName.Madagascar, new Territory(CountryName.Madagascar, Continent.Africa));
		territories.put(CountryName.SouthAfrica, new Territory(CountryName.SouthAfrica, Continent.Africa));
	}

	private void createContinents() {
		continents.put(Continent.Australia,
				new ArrayList<Territory>(Arrays.asList(territories.get(CountryName.Indonesia),
						territories.get(CountryName.NewGuinea), territories.get(CountryName.EasternAustralia),
						territories.get(CountryName.WesternAustralia))));

		continents.put(Continent.Asia, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.Asia)).collect(Collectors.toList()));
		continents.put(Continent.Africa, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.Africa)).collect(Collectors.toList()));
		continents.put(Continent.NorthAmerica, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.NorthAmerica)).collect(Collectors.toList()));
		continents.put(Continent.SouthAmerica, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.SouthAmerica)).collect(Collectors.toList()));
		continents.put(Continent.Europe, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.Europe)).collect(Collectors.toList()));
	}

	public int getNumberOfCardsTurnedIn() {
		return numberOfCardsTurnedIn;
	}

	public void resetNumberOfCardsTurnedIn() {
		this.numberOfCardsTurnedIn = 0;
	}

	public void incrementNumberOfCardsTurnedIn() {
		this.numberOfCardsTurnedIn += 1;
	}
}
