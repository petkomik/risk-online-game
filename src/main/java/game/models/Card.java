package game.models;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class to model a Risk card. Includes attributes to display it properly in GUI.
 *
 * @author pmikov
 *
 */

public class Card implements Serializable {

  private static final long serialVersionUID = 1L;
  private int cardValue; // e.g. infantry = 1, cavalry = 5, artillery = 10 or joker = -1
  private CountryName countryName;
  private String pngDir;
  private boolean isJoker;
  private int id;

  private static CountryName[] infantrySymbol = new CountryName[] {CountryName.Alaska,
      CountryName.Afghanistan, CountryName.Alberta, CountryName.Argentina,
      CountryName.EasternAustralia, CountryName.Egypt, CountryName.Iceland, CountryName.India,
      CountryName.Irkutsk, CountryName.Japan, CountryName.Madagascar, CountryName.NorthAfrica,
      CountryName.WesternEurope, CountryName.WesternUnitedStates};

  private static CountryName[] cavalrySymbol = new CountryName[] {CountryName.CentralAmerica,
      CountryName.China, CountryName.Congo, CountryName.GreatBritain, CountryName.Greenland,
      CountryName.Indonesia, CountryName.Kamchatka, CountryName.NewGuinea,
      CountryName.NorthernEurope, CountryName.Ontario, CountryName.Peru, CountryName.SouthernEurope,
      CountryName.Ural, CountryName.Yakutsk};

  /**
   * Constructor for non-Joker cards.
   *
   * @param name CountryName enum of the card
   * @param id od the Card
   * @param pngDir Path to the image of Territory
   */

  public Card(CountryName name, int id, String pngDir) {
    this.countryName = name;
    if (Arrays.asList(Card.infantrySymbol).contains(name)) {
      this.cardValue = 1;
    } else if (Arrays.asList(Card.cavalrySymbol).contains(name)) {
      this.cardValue = 5;
    } else {
      this.cardValue = 10;
    }
    this.isJoker = false;
    this.id = id;
    this.pngDir = pngDir;
  }

  /**
   * Constructor for a joker card.
   *
   * @param isJoker should be true
   * @param id of the risk card
   */

  public Card(boolean isJoker, int id) {
    this.isJoker = true;
    this.countryName = null;
    this.cardValue = -100;
    this.pngDir = "";
    this.id = id;
  }

  /**
   * Construvotr for cloning a card.
   *
   * @param c card to be cloned
   */

  public Card(Card c) {
    this.isJoker = c.isJoker;
    this.countryName = c.countryName;
    this.cardValue = c.cardValue;
    this.pngDir = c.pngDir;
    this.id = c.id;
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
    return id;
  }

  @Override
  public String toString() {
    return this.isJoker() ? "Joker" : this.countryName.toString();
  }

}
