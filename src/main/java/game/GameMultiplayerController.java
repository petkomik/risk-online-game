package game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.PlayerMP;
import game.models.Territory;
import network.messages.MessagePossessCountry;
import network.messages.MessageType;

/**
 * Class for the actual game logic handling
 * 
 * @author srogalsk
 *
 */

public class GameMultiplayerController {
	private HashMap<CountryName, Territory> territories;
	private HashMap<Continent, ArrayList<Territory>> continents;
	private ArrayList<PlayerMP> players;
	private GameState gameState;
	private LocalDateTime gameTimer;
	volatile int interactionCount;

	private GameMultiplayerController multiplayerGameController;

	public GameMultiplayerController getInstance() {
		return this.multiplayerGameController;
	}

	/**
	 * Constructor for the class
	 */
	public GameMultiplayerController() {
		createTerritories();
		createContinents();
		this.gameState = new GameState();
	}

	public void addPlayer(PlayerMP player) {
		players.add(player);
	}

	public ArrayList<PlayerMP> getPlayers() {
		return this.players;
	}

	public void startGame() {
		PlayerMP playersTurn;
		PlayerMP winner;
		gameTimer = LocalDateTime.now();
		playersTurn = diceThrowToDetermineTheBeginner();
		
		players = sortPlayerList(players, players.indexOf(playersTurn));
		
		playersTurn.getClientHandler().broadcastMessage(new MessagePlayerTurn(playersTurn));
		playersTurn.getClientHandler().sendMessage(new MessagePlayerAction("PlayerAction: ChooseCountry"));
		countryPossession();
		winner = gameRound();
	}
	
	public ArrayList<PlayerMP> sortPlayerList(ArrayList<PlayerMP> players2, int firstPlayerIndex) {
		ArrayList<PlayerMP> firstSublist;
		ArrayList<PlayerMP> endSublist;
		
		firstSublist = (ArrayList<PlayerMP>) players2.subList(firstPlayerIndex, players2.size());
		if(firstPlayerIndex > 0) {
			endSublist = (ArrayList<PlayerMP>) players2.subList(0,firstPlayerIndex);
			firstSublist.addAll(endSublist);
		}
		return firstSublist;
		
	}

	private PlayerMP gameRound() {
		PlayerMP winner;
		PlayerMP p;
		MessagePlacingTroops messagePossessCountry;
		for (int index = 0; index < players.size(); index++) {
			p = players.get(index);
			p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
			messagePossessCountry = (MessagePlacingTroops) p.awaitMessage(10_000,
					MessageType.MessagePlacingTroops);
			// TODO
		}
		return winner;
	}

	public int getNewTroopsCountForPlayer(PlayerMP player) {
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

	private void countryPossession() {
		boolean countryLeftToPick = true;
		int troopsSize;
		boolean countryPossessionSucces;
		MessagePossessCountry messagePossessCountry;
		MessageChooseCountry messageChooseCountry;
		/** set available troopsize */
		switch (players.size()) {
		case 2:
			troopsSize = 40;
			break;
		case 3:
			troopsSize = 35;
			break;
		case 4:
			troopsSize = 30;
			break;
		case 5:
			troopsSize = 25;
			break;
		case 6:
			troopsSize = 20;
			break;
		}
		for (PlayerMP p : players) {
			p.setTroopsAvailable(troopsSize);
			p.setSumOfAllTroops(troopsSize);
		}
		/** *********** */

		PlayerMP p;
		while (countryLeftToPick) {
			for (int index = 0; index < players.size(); index++) {
				p = players.get(index);
				p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
				messagePossessCountry = (MessagePossessCountry) p.awaitMessage(10_000,
						MessageType.MessagePossessCountry);
				countryPossessionSucces = possessCountry(messagePossessCountry.getCountryName(), p);
				if (!countryPossessionSucces) {
					p.getClientHandler().sendMessage(new MessageErrorInput());
					// TODO choose random country and add it to the player
				}
				countryLeftToPick = false;
				for (Territory t : territories.values()) {
					if (t.getOwnedByPlayer() == null) {
						countryLeftToPick = true;
						break;
					}
				}
			}
		}
		while (players.stream().filter(o -> o.getTroopsAvailable() > 0) != null) {
			for (int index = 0; index < players.size(); index++) {
				p = players.get(index);
				if (p.getTroopsAvailable() > 0) {
					p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
					messageChooseCountry = (MessageChooseCountry) p.awaitMessage(10_000,
							MessageType.MessagePlaceTroops);
					if (messageChooseCountry.getCountry().getOwnedByPlayer() != p) {
						p.getClientHandler().sendMessage(new MessageErrorInput());
						// TODO send WrongCountry Message to player and wait for response again
					}
					p.removeTroopsAvailable(1);
					messageChooseCountry.getCountry().addNumberOfTroops(1);
				}
			}
		}

	}

	public PlayerMP diceThrowToDetermineTheBeginner() {
		PlayerMP firstPlayer;
		int highestDiceNumber;
		MessageDiceThrow messageDiceThrowRequest;
		long startTime;
		interactionCount = 0;
		PlayerMP p;
		for (int index = 0; index < players.size(); index++) {
			p = players.get(index);
			Thread messageThread = new Thread(() -> {
				int diceNumber = getRandomDiceNumber();
				try {
					messageDiceThrowRequest = p.awaitMessage(10_000, MessageType.MessageDiceThrowRequest);

					if (messageDiceThrowRequest != null) { // if player interacts in time
						p.sendMessage(new MessageDiceThrow(diceNumber));
						if (diceNumber >= highestDiceNumber) {
							firstPlayer = p;
						}
						interactionCount++;
					} else { // if Player did not responds in time
						p.sendMessage(new MessageDiceThrow(0));
						if (diceNumber >= highestDiceNumber) {
							firstPlayer = p;
						}
						interactionCount++;
					}
				} catch (InterruptedException e) {
					System.out.println("Thread \"Dice Throw Beginn\" interupted by player " + p.getName());
				}

			});
			messageThread.start();
		}
		
		startTime = System.currentTimeMillis(); // to avoid a deadlock through possible player loss during that phase
		while ((interactionCount < players.size()) || System.currentTimeMillis() - startTime < 12_000) {
			Thread.sleep(100); // to avoid busy waiting
		}
		interactionCount = 0;
		return firstPlayer;
	}

	private static synchronized int getRandomDiceNumber() {
		return (int) (Math.random() * 6) + 1;
	}

	public boolean placeTroops(CountryName countryName, int numberOfTroops) {
		return false;
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

}