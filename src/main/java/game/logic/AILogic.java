package game.logic;

import java.util.HashMap;
import java.util.Map.Entry;

import game.models.CountryName;
import game.models.PlayerAI;
import game.models.Territory;
import gameState.GameState;

public class AILogic {

	public static CountryName chooseTerritoryToInitialClaim(GameState gameState, PlayerAI player) {
		
		HashMap<CountryName, Territory> territories = gameState.getTerritories();
		
		switch(player.getLevel()) {
			case EASY:
				for(Entry<CountryName, Territory> set : territories.entrySet()) {
					if(set.getValue().getOwnedByPlayer() == null) {
						return set.getKey();
					}
				}	
				return null;
		}
		return null;
	}
	
	public static CountryName chooseTerritoryToReinforce(GameState gameState, PlayerAI player) {
		return null;
	}
	
	public static CountryName chooseCountryToAttack(GameState gameState, PlayerAI player) {
		return null;
	}
	
	public static boolean willAttack(GameState gameState, PlayerAI player) {
		return false;
	}
	
}