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
		this.cardValue = -1;
		this.pngDir = "";
		this.ID = id;
	}
	
	public int getCardSymbol() {
		return cardValue;
	}
	public void setCardSymbol(int cardSymbol) {
		this.cardValue = cardSymbol;
	}
	
	public CountryName getName() {
		return countryName;
	}
	public void setName(CountryName name) {
		this.countryName = name;
	}

	public String getPngDir() {
		return pngDir;
	}
	
	public void setPngDir(String pngDir) {
		this.pngDir = pngDir;
	}
	
	public boolean isJoker() {
		return isJoker;
	}
	
	public void setJoker(boolean isJoker) {
		this.isJoker = isJoker;
	}
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	private int ID;
}
