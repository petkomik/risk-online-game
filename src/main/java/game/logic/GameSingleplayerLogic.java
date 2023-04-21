package game.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import game.exceptions.WrongCardsException;
import game.exceptions.WrongCountryException;
import game.exceptions.WrongPhaseException;
import game.exceptions.WrongTroopsCountException;
import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.PlayerMP;
import game.models.Territory;
import general.AppController;

/**
 * Class for Singleplayer game
 * 
 * @author srogalsk
 *
 */

public class GameSingleplayerLogic extends GameLogic {

	// Konstruktor
	public GameSingleplayerLogic(ArrayList<Player> players) {
		super(players);
	}

	@Override
	public void startGame() {
		diceThrowToDetermineTheBeginner();
	}

	@Override
	public PlayerMP gameRound() {
		return null;
	}
	
	

	@Override
	public boolean countryPossession(int id, CountryName country)
			throws WrongCountryException, WrongTroopsCountException, WrongPhaseException {
		Player player = players.get(id);
		if (getCurrentPlayer() != player) {
			throw new WrongPhaseException("It is not your turn");
		} else if (!player.isInitialPlacementPhase()) {
			throw new WrongPhaseException("You are not in your Initial Placement Phase");
		} else if (territories.get(country).getOwnedByPlayer() != null
				&& territories.get(country).getOwnedByPlayer() != player) {
			throw new WrongCountryException("The country you have choosen does not belong to you", country);
		} else if (territories.values().stream().anyMatch(o -> o.getOwnedByPlayer() == null)
				&& territories.get(country).getOwnedByPlayer() != null) {
			throw new WrongCountryException("There are still unpossessed countries left", country);
		} else if (player.getTroopsAvailable() < 1) {
			throw new WrongTroopsCountException("You have no more troops available", player.getTroopsAvailable());
		}

		player.removeTroopsAvailable(1);
		AppController.getGameStateClient().getCurrentPlayer().removeTroopsAvailable(1);
		territories.get(country).addNumberOfTroops(1);
		AppController.getGameStateClient().getTerritories().get(country).addNumberOfTroops(1);
		if (territories.get(country).getOwnedByPlayer() != player) {
			player.addAndUpdateOwnedCountries(territories.get(country));
			AppController.getGameStateClient().getCurrentPlayer().addAndUpdateOwnedCountries(AppController.getGameStateClient().getTerritories().get(country));
		}
		if (player.getTroopsAvailable() < 1) {
			player.setInitialPlacementPhase(false);
			player.setPreparationPhase(true);
			player.setCardsTurningInPhase(true);
			setNextActivePlayerAsCurrentPlayer();
			AppController.getGameStateClient().getCurrentPlayer().setInitialPlacementPhase(false);
			AppController.getGameStateClient().getCurrentPlayer().setPreparationPhase(true);
			AppController.getGameStateClient().getCurrentPlayer().setCardsTurningInPhase(true);
			AppController.getGameStateClient().setCurrentPlayer(getCurrentPlayer().getID());
			if(getCurrentPlayer().getTroopsAvailable() > 0) {
				getCurrentPlayer().setInitialPlacementPhase(true);
				AppController.getGameStateClient().getCurrentPlayer().setInitialPlacementPhase(true);
			} else {
				getCurrentPlayer().setPreparationPhase(true);
				getCurrentPlayer().setCardsTurningInPhase(true);
				getNewTroopsCountForPlayer(getCurrentPlayer().getID());
				AppController.getGameStateClient().getCurrentPlayer().setPreparationPhase(true);
				AppController.getGameStateClient().getCurrentPlayer().setCardsTurningInPhase(true);
			}
			return false;
		}
		setNextActivePlayerAsCurrentPlayer();
		getCurrentPlayer().setInitialPlacementPhase(true);
		return true;
	}

	@Override
	public boolean attackCountry(int playerId, CountryName countryFrom, CountryName countryTo, int troops)
			throws WrongCountryException, WrongTroopsCountException, WrongPhaseException {
		Player player = players.get(playerId);
		// Assertion: values are not null
		if (getCurrentPlayer() != player) {
			throw new WrongPhaseException("It is not your turn");
		} else if (!player.isAttackPhase()) {
			throw new WrongPhaseException("You are not in your Attack Phase");
		} else if (territories.get(countryTo).getOwnedByPlayer() == player) {
			throw new WrongCountryException("The country you are attacking belongs to you", countryTo);
		} else if (territories.get(countryFrom).getOwnedByPlayer() != player) {
			throw new WrongCountryException("The country you are attacking from does not belong to you", countryTo);
		} else if (territories.get(countryFrom).getNumberOfTroops() - 1 < troops) {
			throw new WrongTroopsCountException("You do not have enough troops", troops);
		} else if (!territories.get(countryFrom).getNeighboringTerritories().stream().anyMatch(o -> o.equals(territories.get(countryTo)))) {
			throw new WrongCountryException("The Countrys you have choosen are not neighbors", countryTo);
		}

		// dice throws are generated random
		int[] diceNumberAttacker = new int[troops];
		for (int i = 0; i < troops; i++) {
			diceNumberAttacker[i] = getRandomDiceNumber();
		}
		diceNumberAttacker = Arrays.stream(diceNumberAttacker).boxed().sorted(Comparator.reverseOrder())
				.mapToInt(Integer::intValue).toArray();
		 // set Dices Accordingly

		int[] diceNumberDefender = new int[territories.get(countryTo).getNumberOfTroops() > 1 ? 2 : 1];
		for (int i = 0; i < diceNumberDefender.length; i++) {
			diceNumberDefender[i] = getRandomDiceNumber();
		}

		diceNumberDefender = Arrays.stream(diceNumberAttacker).boxed().sorted(Comparator.reverseOrder())
				.mapToInt(Integer::intValue).toArray();
		
		// dice throws are compared
		Player defender = territories.get(countryTo).getOwnedByPlayer();
		for (int i = 0; i < diceNumberDefender.length; i++) {
			if (diceNumberAttacker[i] > diceNumberDefender[i]) {
				territories.get(countryTo).removeNumberOfTroops(1);
				defender.setSumOfAllTroops(defender.getSumOfAllTroops() - 1);
				if(!player.isCardThisRound()) {
				Card card = getRandomCard();
				card.setOwnedBy(player);
				player.addCard(card);
				player.setCardThisRound(true);
				}
			} else {
				territories.get(countryFrom).removeNumberOfTroops(1);
				player.setTroopsAvailable(player.getTroopsAvailable() - 1);
				player.setSumOfAllTroops(player.getSumOfAllTroops() - 1);
			}
		}

		if (territories.get(countryTo).getNumberOfTroops() < 1) {
			player.addAndUpdateOwnedCountries(territories.get(countryTo));
			if(defender.getOwnedCountries().size() < 1) {
				defender.setCanContinuePlaying(false);
				if(players.stream().filter(o -> o.isCanContinuePlaying()).collect(Collectors.toList()).size() <=1) {
					player.setCanContinuePlaying(false);
					setGameIsOver(true);
				}
				for(Card card : defender.getCards()) {
					card.setOwnedBy(player);
					player.addCard(card);
				}
				defender.removeCards(cards);
			}
		}

		if (territories.get(countryTo).getOwnedByPlayer() == player) {
			return true;
		} else {
			return false;
		}
	}
	
	// endAttackPhase
	public void endAttackPhase(Player player) throws WrongPhaseException {
		if (getCurrentPlayer() != player) {
			throw new WrongPhaseException("It is not your turn");
		} else if (!player.isAttackPhase()) {
			throw new WrongPhaseException("You are not in your Attack Phase");
		}
		player.setAttackPhase(false);
		player.setCardThisRound(true);
		player.setFortificationPhase(true);
	}

	@Override
	public boolean fortifyTroops(int playerId, CountryName countryFrom, CountryName countryTo, int troops)
			throws WrongPhaseException, WrongCountryException, WrongTroopsCountException {
		Player player = players.get(playerId);
		// Assertion: no values are null
		if (getCurrentPlayer() != player) {
			throw new WrongPhaseException("It is not your turn");
		} else if (!player.isFortificationPhase()) {
			throw new WrongPhaseException("You are not in your Fortification Phase");
		} else if (territories.get(countryFrom).getOwnedByPlayer() != player
				|| territories.get(countryTo).getOwnedByPlayer() != player) {
			throw new WrongCountryException("Both of your choosen countrys have to belong to you", countryTo);
		} else if (territories.get(countryFrom).getNumberOfTroops() - 1 < troops) {
			throw new WrongTroopsCountException(
					"You dont have enough troops available and one soldier has to stay in your country", troops);
		} else if (!super.isConnectionOwnedByPlayer(territories.get(countryFrom), territories.get(countryTo), playerId)) {
			throw new WrongCountryException("The Countrys you have choosen are not connected by your countries", countryTo);
		}
		territories.get(countryFrom).removeNumberOfTroops(troops);
		territories.get(countryTo).addNumberOfTroops(troops);
		player.setFortificationPhase(false);
		setNextActivePlayerAsCurrentPlayer();
		getCurrentPlayer().setPreparationPhase(true);
		getCurrentPlayer().setCardsTurningInPhase(true);
		 
		return false;
	}

	@Override
	public boolean placeTroops(int playerId, CountryName country, int troops)
			throws WrongPhaseException, WrongCountryException, WrongTroopsCountException {
		Player player = players.get(playerId);
		// Assertion: no values are null
		if (player != getCurrentPlayer() || !player.isPreparationPhase()) {
			throw new WrongPhaseException("It is not your turn or you are not in your Preparation Phase");
		} else if (player.getTroopsAvailable() - troops < 0) {
			throw new WrongTroopsCountException("You dont have enough troops available", troops);
		}
		
		player.removeTroopsAvailable(troops);
		territories.get(country).addNumberOfTroops(troops);
		if (player.getTroopsAvailable() < 1) {
			player.setPreparationPhase(false);
			player.setCardsTurningInPhase(false);
			player.setCardThisRound(false);
			player.setAttackPhase(true);
			return false;
		}
		return true;
	}

	@Override
	public void diceThrowToDetermineTheBeginner() {
		Player firstPlayer = null;
		int highestDiceNumber = 0;
		for (Player p : players) {
			int diceNumber = getRandomDiceNumber();
			if (diceNumber >= highestDiceNumber) {
				highestDiceNumber = diceNumber;
				firstPlayer = p;
			}
		}
		players = sortPlayerList(players, players.indexOf(firstPlayer));
		setInitialTroopsSize();
		setCurrentPlayer(firstPlayer);
		AppController.getGameStateClient().setCurrentPlayer(firstPlayer.getID());
		firstPlayer.setInitialPlacementPhase(true);
		AppController.getGameStateClient().setInitialPlacementPhaseOfPlayer(true, firstPlayer.getID());
	}
	
	@Override
	public void setInitialTroopsSize() {
		int troopsSize = 0;
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
		default:
			troopsSize = -1;
			break;
		}
		for (Player p : players) {
			p.setTroopsAvailable(troopsSize);
			AppController.getGameStateClient().setTroopsAvailableOfPlayer(troopsSize, p.getID());
			p.setSumOfAllTroops(troopsSize);
			AppController.getGameStateClient().setSumOfAllTroopsOfPlayer(troopsSize, p.getID());

		}
		/** *********** */
	}
	
	@Override
	public boolean turnInCards(int playerId, ArrayList<Card> cards)
			throws WrongCountryException, WrongTroopsCountException, WrongPhaseException, WrongCardsException {
		Player player = players.get(playerId);
		if (player == null || getCurrentPlayer() != player) {
			throw new WrongPhaseException("It is not your turn");
		} else if (cards == null || cards.size() != 3) {
			throw new WrongCardsException("You have to choose 3 cards", cards);
		} else if (!player.isCardsTurningInPhase()) {
			throw new WrongPhaseException("You cant turn in Cards right now");
		} else if (!player.getCards().containsAll(cards)) {
			throw new WrongCardsException("You have to own these cards", cards);
		} else if ((!cards.stream().allMatch(o -> o.getCardSymbol() == cards.get(0).getCardSymbol())) || (!cards
				.stream().map(Card::getCardSymbol).distinct().collect(Collectors.toSet()).equals(Set.of(1, 5, 10)))) {
			throw new WrongCardsException("You have to own these cards", cards);
		}
		player.setCardsTurningInPhase(false);
		player.getCards().removeAll(cards);
		for(Card card : cards) {
			card.setOwnedBy(null);
		}
		incrementNumberOfCardsTurnedIn();
		switch (getNumberOfCardsTurnedIn()) {
		case 1:
			player.addTroopsAvailable(4);
			player.setSumOfAllTroops(player.getSumOfAllTroops() + 4);
			break;
		case 2:
			player.addTroopsAvailable(6);
			player.setSumOfAllTroops(player.getSumOfAllTroops() + 6);
			break;
		case 3:
			player.addTroopsAvailable(8);
			player.setSumOfAllTroops(player.getSumOfAllTroops() + 8);
			break;
		case 4:
			player.addTroopsAvailable(10);
			player.setSumOfAllTroops(player.getSumOfAllTroops() + 10);
			break;
		case 5:
			player.addTroopsAvailable(12);
			player.setSumOfAllTroops(player.getSumOfAllTroops() + 12);
			break;
		case 6:
			player.addTroopsAvailable(15);
			player.setSumOfAllTroops(player.getSumOfAllTroops() + 15);
			break;
		default:
			player.addTroopsAvailable((getNumberOfCardsTurnedIn() - 6) * 5 + 15);
			player.setSumOfAllTroops(player.getSumOfAllTroops() + (getNumberOfCardsTurnedIn() - 6) * 5 + 15);
			break;
		}
		return true;
	}
	
	@Override
	public int getNewTroopsCountForPlayer(int playerId) {
		Player player = players.get(playerId);
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
		troops = troops < 3 ? 3 : troops;
		player.setSumOfAllTroops(
				player.getOwnedCountries().values().stream().mapToInt(Territory::getNumberOfTroops).sum() + troops);
		AppController.getGameStateClient().setSumOfAllTroopsOfPlayer(player.getOwnedCountries().values().stream().mapToInt(Territory::getNumberOfTroops).sum() + troops, player.getID());
		return troops;
	}

}
