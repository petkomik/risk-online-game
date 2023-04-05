package game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.PlayerMP;
import game.models.Territory;
import network.messages.MessageType;
import network.messages.MessagePossessCountry;

/**
 * Class for the actual game logic handling
 * 
 * @author srogalsk
 *
 */

public class GameMultiplayerController {
	private HashMap<CountryName, Territory> territories;
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
		gameTimer = LocalDateTime.now();
		playersTurn = diceThrowToDetermineTheBeginner();
		players.remove(playersTurn); // remove player from list to put him first
		players.add(playersTurn);
		playersTurn.getClientHandler().broadcastMessage(new MessagePlayerTurn(playersTurn));
		playersTurn.getClientHandler().sendMessage(new MessagePlayerAction("PlayerAction: ChooseCountry"));
		countryPossession();
		PlayerMP winner = gameRound();
	}

	private PlayerMP gameRound() {
		PlayerMP winner;
		return winner;
	}

	private void countryPossession() {
		boolean countryLeftToPick = true;
		while (countryLeftToPick) {
			for (PlayerMP p : players) {
				p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
				MessagePossessCountry messagePossessCountry = (MessagePossessCountry) p.awaitMessage(10_000,
						MessageType.MessagePossessCountry);
				boolean countryPossessionSucces = possessCountry(messagePossessCountry.getCountryName(), p);
				if (!countryPossessionSucces) {
					p.getClientHandler().sendMessage(new MessageErrorInput());
					// TODO choose random country and add it to the player
				}
				countryLeftToPick = false;
				for(Territory t: territories.values()) {
					if(t.getOwnedByPlayer() == null) {
						countryLeftToPick = true;
						break;
					}
				}
			}
		}
	}

	public PlayerMP diceThrowToDetermineTheBeginner() {
		PlayerMP firstPlayer;
		int highestDiceNumber;
		interactionCount = 0;
		for (PlayerMP p : players) {
			Thread messageThread = new Thread(() -> {
				int diceNumber = getRandomDiceNumber();
				try {
					MessageDiceThrow messageDiceThrow = p.awaitMessage(10_000, MessageType.MessageDiceThrow);

					if (messageDiceThrow != null) { // if player interacts in time
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
		while ((interactionCount < players.size())) {
			Thread.sleep(100); // to avoid busy waiting
		}
		interactionCount = 0;
		return firstPlayer;
	}

	private static synchronized int getRandomDiceNumber() {
		return (int) (Math.random() * 6) + 1;
	}

	public boolean possessCountry(CountryName countryName, Player player) {
		if (territories.get(countryName) == null) {
			territories.get(countryName).setOwnedByPlayer(player);
			territories.get(countryName).setNumberOfTroops(1);;
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

}
