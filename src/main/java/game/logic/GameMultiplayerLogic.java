package game.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import game.exceptions.WrongCardsException;
import game.exceptions.WrongCountryException;
import game.exceptions.WrongPhaseException;
import game.exceptions.WrongTroopsCountException;
import game.models.Card;
import game.models.CountryName;
import game.models.Player;
import game.models.PlayerMP;
import game.models.Territory;
import network.messages.MessageDiceThrow;
import network.messages.MessagePlayerTurn;
//import network.messages.MessagePossessCountry;
import network.messages.MessageType;

/**
 * Class for the actual multiplayer game logic handling
 * 
 * @author srogalsk
 *
 */

// public class GameMultiplayerLogic extends GameLogic {
//
//	private AtomicInteger interactionCount = new AtomicInteger(0);
//
//	/**
//	 * Constructor for the class
//	 */
//	public GameMultiplayerLogic(ArrayList<Player> players) {
//		super(players);
//		startGame();
//	}
//
//	public void startGame() {
//		PlayerMP winner;
//		gameTimer = LocalDateTime.now();
//		diceThrowToDetermineTheBeginner();
//
//		players = sortPlayerList(players, players.indexOf(getCurrentPlayer()));
//
//		((PlayerMP)getCurrentPlayer()).getClientHandler()
//				.broadcastMessage(new MessagePlayerTurn(MessageType.MessagePlayerTurn, getCurrentPlayer().getID()));
//		((PlayerMP)getCurrentPlayer()).getClientHandler().sendMessage(new MessagePlayerAction("PlayerAction: ChooseCountry"));
//
//		countryPossession();
//		winner = gameRound();
//	}
//
//	public PlayerMP gameRound() {
//		PlayerMP winner;
//		PlayerMP p;
//		MessagePlacingTroopsRequest messagePossessCountryRequest;
//		while (players.stream().anyMatch(o -> o.getOwnedCountries().size() < territories.size())) {
//			for (int index = 0; index < players.size(); index++) {
//				p = players.get(index);
//				p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
//				p.setCardsTurningInPhase(true);
//				messagePossessCountryRequest = (MessagePlacingTroopsRequest) p.awaitMessage(10_000,
//						MessageType.MessagePlacingTroopsRequest);
//				// TODO
//
//			}
//		}
//		return winner;
//	}
//
//	public void countryPossession() {
//		boolean countryLeftToPick = true;
//		int troopsSize;
//		boolean countryPossessionSucces;
//		MessagePossessCountryRequest messagePossessCountryRequest;
//		MessageChooseCountry messageChooseCountry;
//		/** set available troopsize */
//		switch (players.size()) {
//		case 2:
//			troopsSize = 40;
//			break;
//		case 3:
//			troopsSize = 35;
//			break;
//		case 4:
//			troopsSize = 30;
//			break;
//		case 5:
//			troopsSize = 25;
//			break;
//		case 6:
//			troopsSize = 20;
//			break;
//		}
//		for (Player p : players) {
//			p.setTroopsAvailable(troopsSize);
//			p.setSumOfAllTroops(troopsSize);
//		}
//		/** *********** */
//
//		PlayerMP p;
//		while (countryLeftToPick) {
//			for (int index = 0; index < players.size(); index++) {
//				p = (PlayerMP) players.get(index);
//				p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
//				messagePossessCountryRequest = (MessagePossessCountryRequest) p.awaitMessage(10_000,
//						MessageType.MessagePossessCountryRequest);
//				countryPossessionSucces = possessCountry(messagePossessCountryRequest.getCountryName(), p);
//				if (!countryPossessionSucces) {
//					p.getClientHandler().sendMessage(new MessageErrorInput());
//					// TODO choose random country and add it to the player
//				}
//				countryLeftToPick = false;
//				for (Territory t : territories.values()) {
//					if (t.getOwnedByPlayer() == null) {
//						countryLeftToPick = true;
//						break;
//					}
//				}
//			}
//		}
//
//		while (players.stream().anyMatch(o -> o.getTroopsAvailable() > 0)) {
//			for (int index = 0; index < players.size(); index++) {
//				p = players.get(index);
//				if (p.getTroopsAvailable() > 0) {
//					p.getClientHandler().broadcastMessage(new MessagePlayerTurn(p));
//					messageChooseCountry = (MessageChooseCountry) p.awaitMessage(10_000,
//							MessageType.MessagePlaceTroops);
//					if (messageChooseCountry.getCountry().getOwnedByPlayer() != p) {
//						p.getClientHandler().sendMessage(new MessageErrorInput());
//						// TODO send WrongCountry Message to player and wait for response again
//					}
//					p.removeTroopsAvailable(1);
//					messageChooseCountry.getCountry().addNumberOfTroops(1);
//				}
//			}
//		}
//
//	}
//
//	public void diceThrowToDetermineTheBeginner() {
//		Player firstPlayer = new Player(null);
//		AtomicInteger highestDiceNumber = new AtomicInteger(0);
//		long startTime;
//		PlayerMP p;
//		AtomicReference<Player> firstPlayerReference = new AtomicReference<>();
//		firstPlayerReference.set(firstPlayer);
//		
//		for (int index = 0; index < players.size(); index++) {
//			p = (PlayerMP) players.get(index);
//			Thread messageThread = new Thread(() -> {
//				int diceNumber = getRandomDiceNumber();
//				MessageDiceThrow messageDiceThrowRequest;
//				try {
//					messageDiceThrowRequest = (MessageDiceThrow) p.awaitMessage(10_000,
//							MessageType.MessageDiceThrowRequest);
//
//					if (messageDiceThrowRequest != null) { // if player interacts in time
//						// p.sendMessage(new MessageDiceThrow(diceNumber));
//						synchronized (highestDiceNumber) {
//							if (diceNumber >= highestDiceNumber.get()) {
//								highestDiceNumber.set(diceNumber);
//								firstPlayerReference.set(p);
//							}
//							interactionCount.incrementAndGet();
//						}
//
//					} else { // if Player did not responds in time
//						// p.sendMessage(new MessageDiceThrow(0));
//						synchronized (highestDiceNumber) {
//							if (diceNumber >= highestDiceNumber.get()) {
//								highestDiceNumber.set(diceNumber);
//								firstPlayerReference.set(p);
//							}
//						}
//						interactionCount.incrementAndGet();
//					}
//				} catch (InterruptedException e) {
//					System.out.println("Thread \"Dice Throw Beginn\" interupted by player " + p.getName());
//				}
//
//			});
//			messageThread.start();
//		}
//
//		startTime = System.currentTimeMillis(); // to avoid a deadlock through possible player loss during that phase
//		while ((interactionCount.get() < players.size()) || System.currentTimeMillis() - startTime < 12_000) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} // to avoid busy waiting
//		}
//		interactionCount.set(0);
//	}
//
//	public boolean placeTroops(CountryName countryName, int numberOfTroops) {
//		return false;
//	}
//
//	@Override
//	public boolean countryPossession(int playerId, CountryName country)
//			throws WrongCountryException, WrongTroopsCountException, WrongPhaseException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean attackCountry(int playerId, CountryName countryFrom, CountryName countryTo, int troops)
//			throws WrongCountryException, WrongTroopsCountException, WrongPhaseException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean fortifyTroops(int playerId, CountryName countryFrom, CountryName countryTo, int troops)
//			throws WrongPhaseException, WrongCountryException, WrongTroopsCountException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean placeTroops(int playerId, CountryName country, int troops)
//			throws WrongPhaseException, WrongCountryException, WrongTroopsCountException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void setInitialTroopsSize() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public int getNewTroopsCountForPlayer(int playerId) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public boolean turnInCards(int playerId, ArrayList<Card> cards)
//			throws WrongCountryException, WrongTroopsCountException, WrongPhaseException, WrongCardsException {
//		// TODO Auto-generated method stub
//		return false;
//	}

//}
