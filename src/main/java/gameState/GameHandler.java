package gameState;

import game.Lobby;
import game.logic.GameType;
import game.logic.Logic;
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
	}
	// 
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
	
	public void addTroopsToCountry(CountryName country, int troops) {
		this.gameState.getTerritories().get(country).addNumberOfTroops(troops);
		this.gameState.subtractTroopsToPlayer(this.gameState.getCurrentPlayer(), troops);
		
		switch (this.gameType) {
		case SinglePlayer:
			this.singlePlayerHandler.reinforceOnGUI(country, this.gameState.getTerritories().get(country).getNumberOfTroops(),
					this.gameState.getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer()));
			break;
		case Multiplayer:
			break;
		case Tutorial:
			break;
		}
	}
	
	/*
	 * gets the order of the player turns
	 * you get the first and then continiue down the ArrayList 
	 */
	
	public int getInitialThrowDice(Player player) {
		if(gameState.getAlivePlayers().indexOf(player)==gameState.getAlivePlayers().size()-1) {
			gameState.setCurrentPlayer(Logic.getFirstPlayer(gameState));
		}else {
			gameState.setNextPlayer();
		}
		singlePlayerHandler.setCurrentPlayerOnGUI(gameState.getCurrentPlayer().getID(), 0);
		
		return this.gameState.getPlayersDiceThrown().get(player);
	}
	
	/*
	 * gets called from GamePane, everytime a country is clicked
	 * decides what to do with the click, depending on period and phase 
	 */
	
	public void clickCountry(int idOfPlayer, CountryName country) {
		Player player = this.gameState.getPlayers().get(idOfPlayer);
		switch (this.gameState.getCurrentGamePeriod()) {
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
						this.singlePlayerHandler.setPeriod(Period.INITIALDEPLOY);
					}
					gameState.setNextPlayer();
					this.singlePlayerHandler.setCurrentPlayerOnGUI(gameState.getCurrentPlayer().getID(),
							numTroopsPlayer);
					break;
				case Multiplayer:
					break;
				case Tutorial:
					break;
				}
			}
			break;
		case INITIALDEPLOY:
			if(Logic.canDeployTroopsToTerritory(this.gameState, player, country)!=-1) {
				switch (this.gameType) {
				case SinglePlayer:
					this.gameState.getTerritories().get(country).addNumberOfTroops(1);
					int numTroopsPlayer = this.gameState.getPlayerTroopsLeft().get(player) - 1; 
					this.gameState.getPlayerTroopsLeft().replace(player, numTroopsPlayer);
					this.singlePlayerHandler.initialDeployOnGUI(country, gameState.getTerritories().get(country).getNumberOfTroops(), numTroopsPlayer);
					if(Logic.isDeployPeriodOver(this.gameState)) {
						gameState.setCurrentGamePeriod(Period.MAINPERIOD);
						this.singlePlayerHandler.setPeriod(Period.MAINPERIOD);
						
						gameState.setCurrentTurnPhase(Phase.REINFORCE);
						this.singlePlayerHandler.setPhase(Phase.REINFORCE);
						this.gameState.setPlayerTroopsLeft(Logic.getTroopsReinforce(this.gameState));
					}
					this.gameState.setNextPlayer();
					this.singlePlayerHandler.setCurrentPlayerOnGUI(gameState.getCurrentPlayer().getID(), 
							this.gameState.getPlayerTroopsLeft().get(this.gameState.getCurrentPlayer()));
//					this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(country, 1, player.getTroopsAvailable());
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
							this.singlePlayerHandler.chooseNumberOfTroopsOnGUI(country, 1, player.getTroopsAvailable());
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
				break;
			case FORTIFY:
				break;
			}
			break;
		}
	}
	
	public GameState getGameState() {
		return this.gameState;
	}

}


