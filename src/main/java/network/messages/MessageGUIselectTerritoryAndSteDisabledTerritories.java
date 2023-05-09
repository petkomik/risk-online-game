package network.messages;

import java.util.ArrayList;

import game.models.CountryName;
import gameState.GameState;

public class MessageGUIselectTerritoryAndSteDisabledTerritories extends Message {

	private static final long serialVersionUID = 1L;
	
	GameState gameState;
	CountryName countryName;
	ArrayList<CountryName> unreachableCountries;
	
	public MessageGUIselectTerritoryAndSteDisabledTerritories(GameState gameState, CountryName countryName, 
			ArrayList<CountryName> unreachableCountries) {
		super(MessageType.MessageGUIselectTerritoryAndSteDisabledTerritories);
		this.gameState = gameState;
		this.countryName = countryName;
		this.unreachableCountries = unreachableCountries;
	}

	public GameState getGameState() {
		return gameState;
	}

	public CountryName getCountryName() {
		return countryName;
	}

	public ArrayList<CountryName> getUnreachableCountries() {
		return unreachableCountries;
	}
	

}
