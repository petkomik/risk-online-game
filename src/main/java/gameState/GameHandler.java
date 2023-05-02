package gameState;

import java.util.ArrayList;
import java.util.List;

import game.Lobby;
import game.exceptions.WrongCardsException;
import game.exceptions.WrongCardsSetException;
import game.exceptions.WrongCountryException;
import game.exceptions.WrongPeriodException;
import game.exceptions.WrongPhaseException;
import game.exceptions.WrongTroopsCountException;
import game.logic.GameType;
import game.logic.Logic;
import game.models.Card;
import game.models.CountryName;
import game.models.Player;

public class GameHandler {
	// Gamelogic Ausführung der Methoden
	// Kommunikation zu SingleHandler und ClientHandler
	// Übergabe an GameState
	// GamePane 
	
	private SinglePlayerHandler singlePlayerHandler;
	private GameState gameState;
	private Lobby lobby;
	private GameType gameType;
	
	public GameHandler (Lobby lobby) {
		this.gameState = new GameState(lobby);
		this.lobby = lobby;
		this.singlePlayerHandler= null; 
		determineInitialDice();
		gameState.setInitialTroops(Logic.setInitialTroopsSize(this.gameState));
		gameState.setCurrentPlayer(lobby.getPlayerList().get(0).getID());
	}
	
	public void initSingleplayer(SinglePlayerHandler singlePlayerHandler) {
		this.singlePlayerHandler = singlePlayerHandler;
		this.gameType = GameType.SinglePlayer;
	}
	
	public void determineInitialDice() {
		this.gameState.setPlayersDiceThrown(
				Logic.diceThrowToDetermineTheBeginner(gameState));
	}
	
	
	/*
	 * adds troops to country subs them from player 
	 * gets called by SinglePlayerHandler or ClientHandler
	 * 
	 * method addTroopsToCountryFortify will add troops to player and remove from country
	 */
	
	public void confirmTroopsToCountry(CountryName country, int troops, ChoosePane choosePane) {

		switch(choosePane) {
		case FORTIFY: 
			this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
			this.gameState.subtractTroopsToPlayer(this.gameState.getCurrentPlayer(), troops);
			switch (this.gameType) {
			case SinglePlayer:
				this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGUI(country, 
						this.gameState.getTerritories().get(country).getNumberOfTroops(),
						this.gameState.getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer()));
				break;
			case Multiplayer:
				break;
			case Tutorial:
				break;
			}
			break;
		case ATTACK_ATTACK:
			switch (this.gameType) {
			case SinglePlayer:
				this.singlePlayerHandler.openBattleFrameOnGUI(this.gameState.getTerritories()
						.get(this.gameState.getLastAttackingCountry()).getContinent(), 
						this.gameState.getLastAttackingCountry(),
						this.gameState.getTerritories().get(country).getContinent(), country, 
						this.gameState.getCurrentPlayer(),
						this.gameState.getTerritories().get(country).getOwnedByPlayer(), true, 
						troops, this.gameState.getTerritories().get(country).getNumberOfTroops());
				break;
			case Multiplayer:
				break;
			case Tutorial:
				break;
			}
			
			
			break;
		case ATTACK_COLONISE:
			this.gameState.getTerritories().get(this.gameState.getLastAttackingCountry())
			.removeNumberOfTroops(troops);
			this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
			switch (this.gameType) {
			case SinglePlayer:
				this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGUI(country, 
						this.gameState.getTerritories().get(this.gameState.getLastAttackingCountry()).getNumberOfTroops(), 
						this.gameState.getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer()));
				break;
			case Multiplayer:
				break;
			case Tutorial:
				break;
			}
			this.gameState.setLastAttackingCountry(null);
			break;
		case REINFORCE:
			this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
			this.gameState.subtractTroopsToPlayer(this.gameState.getCurrentPlayer(), troops);
			break;
			
		}

	}
	
	/*
	 * gets the order of the player turns
	 * you get the first and then continiue down the ArrayList 
	 */
	
	public void playerThrowsInitialDice(int idOfPlayer) {
		if(Logic.canThrowInitialDice(idOfPlayer, this.gameState)) {
			int i = this.gameState.getPlayersDiceThrown().get(this.gameState.getPlayers().get(idOfPlayer));			
			switch (this.gameType) {
			case SinglePlayer:
				this.singlePlayerHandler.rollInitialDiceOnGUI(idOfPlayer, i);
				break;
			case Multiplayer:
				break;
			case Tutorial:
				break;
			}
		}
	}
	
	
	/*
	 * gets called from GamePane, everytime a country is clicked
	 * decides what to do with the click, depending on period and phase 
	 */
	
	public void clickCountry(int idOfPlayer, CountryName country) {
		Player player = this.gameState.getPlayers().get(idOfPlayer);
		switch (this.gameState.getCurrentGamePeriod()) {
		case DICETHROW: //Map should be disabled during DICETHROW
			break;
		case COUNTRYPOSESSION: 
			if(Logic.claimTerritory(player, this.gameState, country)) {
				this.gameState.setOwnedByTerritory(country, player);
				this.gameState.updateTroopsOnTerritory(country, 1);
				switch (this.gameType) {
				case SinglePlayer:
					int numTroopsPlayer = this.gameState.getPlayerTroopsLeft().get(player) - 1; 
					this.gameState.getPlayerTroopsLeft().replace(player, numTroopsPlayer);
					this.singlePlayerHandler.possesCountryOnGUI(country, player.getID());
					if(Logic.allTerritoriesClaimed(gameState)) {
						gameState.setCurrentGamePeriod(Period.INITIALDEPLOY);
						this.singlePlayerHandler.setPeriodOnGUI(Period.INITIALDEPLOY);
					}
					break;
				case Multiplayer:
					break;
				case Tutorial:
					break;
				}
			}
			break;
		case INITIALDEPLOY:
			if(Logic.canInitialDeployTroopsToTerritory(this.gameState, player, country)) {
				switch (this.gameType) {
				case SinglePlayer:
					this.gameState.getTerritories().get(country).addNumberOfTroops(1);
					int numTroopsPlayer = this.gameState.getPlayerTroopsLeft().get(player) - 1; 
					this.gameState.getPlayerTroopsLeft().replace(player, numTroopsPlayer);
					this.singlePlayerHandler.setTroopsOnTerritoryAndLeftOnGUI(country, 
							gameState.getTerritories().get(country).getNumberOfTroops(), 
							numTroopsPlayer);
					if(Logic.isDeployPeriodOver(this.gameState)) {
						gameState.setCurrentGamePeriod(Period.MAINPERIOD);
						this.singlePlayerHandler.setPeriodOnGUI(Period.MAINPERIOD);
						
						gameState.setCurrentTurnPhase(Phase.REINFORCE);
						this.singlePlayerHandler.setPhaseOnGUI(Phase.REINFORCE);
						this.gameState.setPlayerTroopsLeft(Logic.getTroopsReinforce(this.gameState));
					}
					break;
				case Multiplayer:
					break;
				case Tutorial:
					break;
				}
			}
			break;
		case MAINPERIOD:
			switch(this.gameState.getCurrentTurnPhase()) {
			case REINFORCE:
				if(Logic.canReinforceTroopsToTerritory(this.gameState, player, country)!=-1) {
					switch(this.gameType) {
						case SinglePlayer:
							this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(country, 1, 
									this.gameState.getPlayerTroopsLeft().get(player), 
									ChoosePane.REINFORCE);
							break;
						case Multiplayer:
							break;
						case Tutorial:
							break;
					}
				}else {
					
				}
				break;
			case ATTACK:
				if(Logic.playerAttackingFromCountry(country, idOfPlayer, this.gameState)) {
					ArrayList<CountryName> unreachableCountries = 
							Logic.getUnreachableTerritories(country, idOfPlayer, this.gameState);
					this.gameState.setLastAttackingCountry(country);
					switch(this.gameType) {
					case SinglePlayer:
						this.singlePlayerHandler.playerCanAttackFromCountryOnGUI(country, unreachableCountries);
						break;
					case Multiplayer:
						break;
					case Tutorial:
						break;
					}
				}
				if(Logic.playerAttackingCountry(country, idOfPlayer, this.gameState)) {
					if(gameState.getLastAttackingCountry() == null) {
						
					} else {
						switch(this.gameType) {
						case SinglePlayer:
							this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(country, 1, 
									this.gameState.getTerritories().get(this.gameState.getLastAttackingCountry())
									.getNumberOfTroops() - 1, ChoosePane.ATTACK_ATTACK);
							break;
						case Multiplayer:
							break;
						case Tutorial:
							break;
						}					
					}
				}
				
				break;
			case FORTIFY:
				if(Logic.playerFortifyingPosition(country, idOfPlayer, this.gameState)) {
					if(this.gameState.getLastFortifyingCounty() == null) {
						if(this.gameState.getTerritories().get(country).getNumberOfTroops() > 1) {
							ArrayList<CountryName> fortifiableCountries = new ArrayList<CountryName>();
							fortifiableCountries.addAll(this.gameState.getTerritories().keySet());
							fortifiableCountries.removeIf(x -> !this.gameState.getTerritories().get(x)
									.getOwnedByPlayer().equals(player));
							switch(this.gameType) {
							case SinglePlayer:
								this.singlePlayerHandler.playerCanFortifyFromCountryOnGUI(country, fortifiableCountries);
								break;
							case Multiplayer:
								break;
							case Tutorial:
								break;
							}
						}
					} else if(this.gameState.getLastFortifyingCounty() == country){
						switch(this.gameType) {
						case SinglePlayer:
							this.singlePlayerHandler.resetFortifyingPhaseOnGUI();
							this.gameState.setLastFortifyingCounty(null);						
							break;
						case Multiplayer:
							break;
						case Tutorial:
							break;
						}
					} else {
						switch(this.gameType) {
						case SinglePlayer:
							this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(country, 1, 
									this.gameState.getTerritories().get(this.gameState
											.getLastFortifyingCounty()).getNumberOfTroops() - 1,
									ChoosePane.FORTIFY);
							break;
						case Multiplayer:
							break;
						case Tutorial:
							break;
						}
					}
				}
				break;
			}
			break;
		}
	}
	
	public GameState getGameState() {
		return this.gameState;
	}

	public void endPhaseTurn(Period period, Phase phase, int idOfPlayer) {
		if(period.equals(Period.MAINPERIOD)) {
			if(Logic.playerEndsPhase(phase, idOfPlayer, this.gameState)) {
				switch (phase) {
				case FORTIFY:
					this.gameState.setCurrentTurnPhase(Phase.ATTACK);
					switch(this.gameType) {
						case SinglePlayer:
							this.singlePlayerHandler.setPhaseOnGUI(Phase.ATTACK);
							break;
						case Multiplayer:
							break;
						case Tutorial:
							break;
					}
					break;
				case ATTACK:
					this.gameState.setCurrentTurnPhase(Phase.REINFORCE);
					switch(this.gameType) {
					case SinglePlayer:
						this.singlePlayerHandler.setPhaseOnGUI(Phase.REINFORCE);
						break;
					case Multiplayer:
						break;
					case Tutorial:
						break;
					}
					break;
				case REINFORCE:
					break;
				}
			}
		} else {
			if(Logic.playerEndsTurn(period, idOfPlayer, this.gameState)) {
				switch(period) {
				case DICETHROW:
					if(Logic.isDiceThrowPeriodOver(this.gameState,  idOfPlayer)) {
						this.gameState.setCurrentPlayer(Logic.getFirstPlayer(gameState).getID());
						this.gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
						switch(this.gameType) {
						case SinglePlayer:
							System.out.println("dice throw is over");
							this.singlePlayerHandler.setCurrentPlayerOnGUI(this.gameState.getCurrentPlayer().getID(), 0);
							this.singlePlayerHandler.setPeriodOnGUI(Period.COUNTRYPOSESSION);
							if(this.gameState.getCurrentPlayer().isAI()) {
								// calls the AI 
							} else {
								this.singlePlayerHandler.chnagePlayerOnGUI(this.gameState.getCurrentPlayer().getID(), null);
								
							}
							
							break;
						case Multiplayer:
							break;
						case Tutorial:
							break;
						}
					} else {
						this.gameState.setNextPlayer();
						switch(this.gameType) {
						case SinglePlayer:
							this.singlePlayerHandler.setCurrentPlayerOnGUI(this.gameState.getCurrentPlayer().getID(), 0);
							if(this.gameState.getCurrentPlayer().isAI()) {
								// calls the AI 
							} else {
								this.singlePlayerHandler.chnagePlayerOnGUI(this.gameState.getCurrentPlayer().getID(), null);
							}
							
							break;
						case Multiplayer:
							break;
						case Tutorial:
							break;
						}	
					}
					break;
				case COUNTRYPOSESSION:
					break;
				case INITIALDEPLOY:
					break;
				case MAINPERIOD:
					break;
				default:
					break;
				}
			}
		}
		
	}

	public void turnInRiskCards(ArrayList<Card> cards, int idOfPlayer) 
			throws WrongCountryException, WrongTroopsCountException, WrongPhaseException,
				WrongCardsException, WrongCardsSetException, WrongPeriodException {
		if(Logic.turnInRiskCards(cards, this.gameState.getPlayers().get(idOfPlayer), 
				this.gameState)) {
			ArrayList<Card> newCards = this.gameState.getRiskCardsInPlayers()
					.get(this.gameState.getPlayers().get(idOfPlayer));
			newCards.removeAll(cards);
			this.gameState.editRiskCardsInPlayers(newCards, idOfPlayer);
			int bonusTroops = this.gameState.playerTurnsInCard();
			switch(this.gameType) {
			case SinglePlayer:
				this.singlePlayerHandler.riskCardsTurnedInSuccessOnGUI(newCards, idOfPlayer,
						bonusTroops);
				break;
			case Multiplayer:
				break;
			case Tutorial:
				break;
			}
		}
	}
	
}
