package network.messages;

import game.models.Lobby;
import gameState.GameState;
/**
 * The MessageGUIRollDiceBattle class represents a message used to indicate the result of rolling dice in a battle in the game GUI.
 * It includes the game state, the values of the attacker's dice, the values of the defender's dice,
 * the number of troops in the attacking territory, the number of troops in the defending territory,
 * the number of dice rolled by each player, and the associated lobby.
 * 
 * author dignatov
 */
public class MessageGUIRollDiceBattle extends Message {

	private static final long serialVersionUID = 1L;
	private int[] attackerDiceValues;
	private int[] defenderDiceValues;

	private int troopsInAttackAt;
	private int troopsInAttackDf;
	private int[] numberOfDice;
	private GameState gameState;
	private Lobby lobby;
	/**
	 * Constructs a MessageGUIRollDiceBattle object with the specified game state, attacker's dice values,
	 * defender's dice values, number of troops in the attacking territory, number of troops in the defending territory,
	 * number of dice rolled by each player, and associated lobby.
	 *
	 * @param gameState The game state associated with the message
	 * @param attackerDiceValues The values of the attacker's dice
	 * @param defenderDiceValues The values of the defender's dice
	 * @param troopsInAttackAt The number of troops in the attacking territory
	 * @param troopsInAttackDf The number of troops in the defending territory
	 * @param numberOfDice The number of dice rolled by each player
	 * @param lobby The associated lobby
	 */
	public MessageGUIRollDiceBattle(GameState gameState, int[] attackerDiceValues, int[] defenderDiceValues,
			int troopsInAttackAt, int troopsInAttackDf, int[] numberOfDice, Lobby lobby) {
		super(MessageType.MessageGUIRollDiceBattle);
		this.attackerDiceValues = attackerDiceValues;
		this.gameState = gameState;
		this.numberOfDice = numberOfDice;
		this.defenderDiceValues = defenderDiceValues;
		this.troopsInAttackAt = troopsInAttackAt;
		this.troopsInAttackDf = troopsInAttackDf;
		this.lobby = lobby;

	}

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

	public GameState getGameState() {
		return gameState;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
