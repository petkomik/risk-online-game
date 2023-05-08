package game.models;

import java.io.Serializable;
import java.util.Arrays;

/**
 * class for game card representation
 * @author pmikov
 *
 */
public class Card implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int cardValue; // e.g. infantry = 1, cavalry = 5, artillery = 10 or joker = -1
	private CountryName countryName;
	private String pngDir;
	private boolean isJoker;
	private int ID;
	
	private static CountryName[] infantrySymbol = new CountryName[] {CountryName.Alaska, 
			CountryName.Afghanistan, CountryName.Alberta, CountryName.Argentina, 
			CountryName.EasternAustralia, CountryName.Egypt, CountryName.Iceland, 
			CountryName.India, CountryName.Irkutsk, CountryName.Japan, CountryName.Madagascar, 
			CountryName.NorthAfrica, CountryName.WesternEurope, CountryName.WesternUnitedStates};
	
	private static CountryName[] cavalrySymbol = new CountryName[] {CountryName.CentralAmerica, 
			CountryName.China, CountryName.Congo, CountryName.GreatBritain, 
			CountryName.Greenland, CountryName.Indonesia, CountryName.Kamchatka, 
			CountryName.NewGuinea, CountryName.NorthernEurope, CountryName.Ontario, 
			CountryName.Peru, CountryName.SouthernEurope, CountryName.Ural, CountryName.Yakutsk};
	
	public Card(CountryName name, int id, String pngDir) {
		this.countryName = name;
		if(Arrays.asList(Card.infantrySymbol).contains(name)) {
			this.cardValue = 1;
		} else if(Arrays.asList(Card.cavalrySymbol).contains(name)) {
			this.cardValue = 5;
		} else {
			this.cardValue = 10;
		}
		this.isJoker = false;
		this.ID = id;
		this.pngDir = pngDir;
	} 
	
	public Card(boolean isJoker, int id) {
		this.isJoker = true;
		this.countryName = null;
		this.cardValue = -100;
		this.pngDir = "";
		this.ID = id;
	}
	
	public int getCardSymbol() {
		return cardValue;
	}
	
	public CountryName getName() {
		return countryName;
	}
	
	public String getPngDir() {
		return pngDir;
	}
	
	public boolean isJoker() {
		return isJoker;
	}
	
	public int getID() {
		return ID;
	}

    @Override
    public String toString() {
        return this.getID() + " " + this.cardValue;
    }
	
}
