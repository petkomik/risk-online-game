package game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.PlayerMP;
import game.models.Territory;
import network.messages.MessagePossessCountry;
import network.messages.MessageType;

/**
 * Class for the actual multiplayer game logic handling
 * 
 * @author srogalsk
 *
 */

public class GameMultiplayerController extends GameController {
	private volatile int interactionCount;

	/**
	 * Constructor for the class
	 */
	public GameMultiplayerController(ArrayList <Player> players) {
		super(players);
	}
	
	public static GameMultiplayerController getInstance() {
		return this;
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

	public PlayerMP gameRound() {
		PlayerMP winner;
		PlayerMP p;
		MessagePlacingTroopsRequest messagePossessCountryRequest;
		while (players.stream().anyMatch(o -> o.getOwnedCountries().size() < territories.size())) {
			for (int index = 0; index < players.size(); index++) {
				p = players.get(index);
				p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
				p.setCardsTurningInPhase(true);
				messagePossessCountryRequest = (MessagePlacingTroopsRequest) p.awaitMessage(10_000, MessageType.MessagePlacingTroopsRequest);
				// TODO

			}
		}
		return winner;
	}

	public void countryPossession() {
		boolean countryLeftToPick = true;
		int troopsSize;
		boolean countryPossessionSucces;
		MessagePossessCountryRequest messagePossessCountryRequest;
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
		for (Player p : players) {
			p.setTroopsAvailable(troopsSize);
			p.setSumOfAllTroops(troopsSize);
		}
		/** *********** */

		PlayerMP p;
		while (countryLeftToPick) {
			for (int index = 0; index < players.size(); index++) {
				p = (PlayerMP) players.get(index);
				p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
				messagePossessCountryRequest = (MessagePossessCountryRequest) p.awaitMessage(10_000,
						MessageType.MessagePossessCountryRequest);
				countryPossessionSucces = possessCountry(messagePossessCountryRequest.getCountryName(), p);
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

		while (players.stream().anyMatch(o -> o.getTroopsAvailable() > 0)) {
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

	public Player diceThrowToDetermineTheBeginner() {
		Player firstPlayer;
		int highestDiceNumber;
		MessageDiceThrow messageDiceThrowRequest;
		long startTime;
		interactionCount = 0;
		PlayerMP p;
		for (int index = 0; index < players.size(); index++) {
			p = (PlayerMP) players.get(index);
			Thread messageThread = new Thread(() -> {
				int diceNumber = getRandomDiceNumber();
				try {
					messageDiceThrowRequest = p.awaitMessage(10_000, MessageType.MessageDiceThrowRequest);

					if (messageDiceThrowRequest != null) { // if player interacts in time
						p.sendMessage(new MessageDiceThrow(diceNumber));
						if (diceNumber >= highestDiceNumber) {
							highestDiceNumber = diceNumber;
							firstPlayer = p;
						}
						interactionCount++;
					} else { // if Player did not responds in time
						p.sendMessage(new MessageDiceThrow(0));
						if (diceNumber >= highestDiceNumber) {
							highestDiceNumber = diceNumber;
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

	public boolean placeTroops(CountryName countryName, int numberOfTroops) {
		return false;
	}

	@Override
	public boolean countryPossession(Player player, CountryName country) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean attackCountry(Player player, CountryName countryFrom, CountryName countryTo, int troops) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fortifyTroops(Player player, CountryName countryFrom, CountryName countryTo, int troops) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean placeTroops(Player player, CountryName country, int troops) {
		// TODO Auto-generated method stub
		return false;
	}


}
