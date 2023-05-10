package network.messages;

import game.models.Lobby;
import gameState.GameState;

public class MessageGUIRollDiceBattle extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] attackerDiceValues;
	private int[] defenderDiceValues;
	
	private int troopsInAttackAt;
	private int troopsInAttackDf;
	private int[] numberOfDice;
	private GameState gameState;
	private Lobby lobby;
	
	public int[] getAttackerDiceValues() {
		return attackerDiceValues;
	}

	public int[] getDefenderDiceValues() {
		return defenderDiceValues;
	}

	public int getTroopsInAttackAt() {
		return troopsInAttackAt;
	}

	public int getTroopsInAttackDf() {
		return troopsInAttackDf;
	}

	public int[] getNumberOfDice() {
		return numberOfDice;
	}

	public MessageGUIRollDiceBattle(GameState gameState,int[] attackerDiceValues, int[] defenderDiceValues,
			int troopsInAttackAt, int troopsInAttackDf, int[] numberOfDice, Lobby lobby
			) {
		super(MessageType.MessageGUIRollDiceBattle);
		this.attackerDiceValues =attackerDiceValues;
		this.gameState = gameState;
		this.numberOfDice = numberOfDice;
		this.defenderDiceValues = defenderDiceValues;
		this.troopsInAttackAt = troopsInAttackAt;
		this.troopsInAttackDf = troopsInAttackDf;
		this.lobby = lobby;
		
	}

	public GameState getGameState() {
		return gameState;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
