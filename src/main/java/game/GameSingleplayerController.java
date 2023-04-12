package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.PlayerMP;
import game.models.Territory;

/**
 * Class for Singleplayer game
 * 
 * @author srogalsk
 *
 */

public class GameSingleplayerController extends GameController {

	// Konstruktor
	public GameSingleplayerController(ArrayList<Player> players) {
		super(players);
		this.players = players;
	}

	@Override
	public void startGame() {

	}

	@Override
	public PlayerMP gameRound() {
		return null;
	}

	@Override
	public boolean countryPossession(Player player, CountryName country) {
		if (getCurrentPlayer() != player) {
			return false;
		} else if (!player.isInitialPlacementPhase()) {
			return false;
		} else if (territories.values().stream().anyMatch(o -> o.getOwnedByPlayer() == null)
				&& territories.get(country).getOwnedByPlayer() != null) {
			return false;
		} else if (territories.get(country).getOwnedByPlayer() != null) {
			return false;
		} else if (player.getTroopsAvailable() < 1) {
			return false;
		}

		territories.get(country).setOwnedByPlayer(player);
		player.addAndUpdateOwnedCountries(territories.get(country));
		setCurrentPlayer(players.get((players.indexOf(player) + 1) % players.size()));
		getCurrentPlayer().setInitialPlacementPhase(true);
		return true;
	}

	@Override
	public boolean attackCountry(Player player, CountryName countryFrom, CountryName countryTo, int troops) {
		
		player.getName();
		
		return false;
	}

	@Override
	public boolean fortifyTroops(Player player, CountryName countryFrom, CountryName countryTo, int troops) {
		// Assertion: no values are null
		if (getCurrentPlayer() != player) {
			return false;
		} else if (!player.isFortificationPhase()) {
			return false;
		} else if (territories.get(countryFrom).getOwnedByPlayer() != player
				|| territories.get(countryTo).getOwnedByPlayer() != player) {
			return false;
		} else if (territories.get(countryFrom).getNumberOfTroops() - 1 < troops) {
			return false;
		}
		territories.get(countryFrom).removeNumberOfTroops(troops);
		territories.get(countryTo).addNumberOfTroops(troops);
		player.setFortificationPhase(false);
		setCurrentPlayer(players.get((players.indexOf(player) + 1) % players.size()));
		getCurrentPlayer().setPreparationPhase(true);
		getCurrentPlayer().setCardsTurningInPhase(true);
		super.getNewTroopsCountForPlayer(getCurrentPlayer());
		return true;
	}

	@Override
	public boolean placeTroops(Player player, CountryName country, int troops) {
		// Assertion: no values are null
		if(player != getCurrentPlayer() || !player.isPreparationPhase()) {
			return false;
		} else if (player.getTroopsAvailable()-troops < 0) {
			return false;
		}
		player.removeTroopsAvailable(troops);
		territories.get(country).addNumberOfTroops(troops);
		
		return true;
	}

	@Override
	public Player diceThrowToDetermineTheBeginner() {
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
		setCurrentPlayer(firstPlayer);
		setInitialTroopsSize();
		firstPlayer.setInitialPlacementPhase(true);
		return firstPlayer;
	}

}
