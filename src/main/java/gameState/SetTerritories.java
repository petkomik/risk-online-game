package gameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Territory;

public class SetTerritories {


	public static void createContinents(HashMap<Continent, ArrayList<Territory>> continents,
			HashMap<CountryName, Territory> territories) {
		continents.put(Continent.Australia,
				new ArrayList<Territory>(Arrays.asList(territories.get(CountryName.Indonesia),
						territories.get(CountryName.NewGuinea), territories.get(CountryName.EasternAustralia),
						territories.get(CountryName.WesternAustralia))));

		continents.put(Continent.Asia, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.Asia)).collect(Collectors.toList()));
		continents.put(Continent.Africa, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.Africa)).collect(Collectors.toList()));
		continents.put(Continent.NorthAmerica, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.NorthAmerica)).collect(Collectors.toList()));
		continents.put(Continent.SouthAmerica, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.SouthAmerica)).collect(Collectors.toList()));
		continents.put(Continent.Europe, (ArrayList<Territory>) territories.values().stream()
				.filter(o -> o.getContinent().equals(Continent.Europe)).collect(Collectors.toList()));
	}

	public static void createCardDeck(ArrayList<Card> cards) {
		cards.add(new Card(CountryName.Alaska, 1));
		cards.add(new Card(CountryName.NorthwestTerritory, 10));
		cards.add(new Card(CountryName.Alberta, 1));
		cards.add(new Card(CountryName.WesternUnitedStates, 1));
		cards.add(new Card(CountryName.CentralAmerica, 5));
		cards.add(new Card(CountryName.Greenland, 5));
		cards.add(new Card(CountryName.Ontario, 5));
		cards.add(new Card(CountryName.Quebec, 10));
		cards.add(new Card(CountryName.EasternUnitedStates, 10));
		cards.add(new Card(CountryName.Venezuela, 10));
		cards.add(new Card(CountryName.Peru, 5));
		cards.add(new Card(CountryName.Brazil, 10));
		cards.add(new Card(CountryName.Argentina, 1));
		cards.add(new Card(CountryName.Iceland, 1));
		cards.add(new Card(CountryName.Scandinavia, 10));
		cards.add(new Card(CountryName.GreatBritain, 5));
		cards.add(new Card(CountryName.NorthernEurope, 5));
		cards.add(new Card(CountryName.WesternEurope, 1));
		cards.add(new Card(CountryName.SouthernEurope, 5));
		cards.add(new Card(CountryName.NorthAfrica, 1));
		cards.add(new Card(CountryName.Egypt, 1));
		cards.add(new Card(CountryName.Congo, 5));
		cards.add(new Card(CountryName.EastAfrica, 10));
		cards.add(new Card(CountryName.SouthAfrica, 10));
		cards.add(new Card(CountryName.Madagascar, 1));
		cards.add(new Card(CountryName.Siberia, 10));
		cards.add(new Card(CountryName.Ural, 5));
		cards.add(new Card(CountryName.China, 5));
		cards.add(new Card(CountryName.Afghanistan, 1));
		cards.add(new Card(CountryName.MiddleEast, 10));
		cards.add(new Card(CountryName.India, 1));
		cards.add(new Card(CountryName.Siam, 10));
		cards.add(new Card(CountryName.Yakutsk, 5));
		cards.add(new Card(CountryName.Irkutsk, 1));
		cards.add(new Card(CountryName.Mongolia, 10));
		cards.add(new Card(CountryName.Japan, 1));
		cards.add(new Card(CountryName.Kamchatka, 5));
		cards.add(new Card(CountryName.Indonesia, 5));
		cards.add(new Card(CountryName.NewGuinea, 5));
		cards.add(new Card(CountryName.WesternAustralia, 10));
		cards.add(new Card(CountryName.EasternAustralia, 1));
		;
	}

	public static void createTerritories(HashMap<CountryName, Territory> territories) {
		// add territories for each country name
		territories.put(CountryName.Alaska, new Territory(CountryName.Alaska, Continent.NorthAmerica));
		territories.put(CountryName.Greenland, new Territory(CountryName.Greenland, Continent.NorthAmerica));
		territories.put(CountryName.Alberta, new Territory(CountryName.Alberta, Continent.NorthAmerica));
		territories.put(CountryName.NorthwestTerritory,
				new Territory(CountryName.NorthwestTerritory, Continent.NorthAmerica));
		territories.put(CountryName.Ontario, new Territory(CountryName.Ontario, Continent.NorthAmerica));
		territories.put(CountryName.Quebec, new Territory(CountryName.Quebec, Continent.NorthAmerica));
		territories.put(CountryName.WesternUnitedStates,
				new Territory(CountryName.WesternUnitedStates, Continent.NorthAmerica));
		territories.put(CountryName.EasternUnitedStates,
				new Territory(CountryName.EasternUnitedStates, Continent.NorthAmerica));
		territories.put(CountryName.CentralAmerica, new Territory(CountryName.CentralAmerica, Continent.NorthAmerica));
		territories.put(CountryName.Venezuela, new Territory(CountryName.Venezuela, Continent.SouthAmerica));
		territories.put(CountryName.Brazil, new Territory(CountryName.Brazil, Continent.SouthAmerica));
		territories.put(CountryName.Peru, new Territory(CountryName.Peru, Continent.SouthAmerica));
		territories.put(CountryName.Argentina, new Territory(CountryName.Argentina, Continent.SouthAmerica));
		territories.put(CountryName.Iceland, new Territory(CountryName.Iceland, Continent.Europe));
		territories.put(CountryName.Scandinavia, new Territory(CountryName.Scandinavia, Continent.Europe));
		territories.put(CountryName.GreatBritain, new Territory(CountryName.GreatBritain, Continent.Europe));
		territories.put(CountryName.NorthernEurope, new Territory(CountryName.NorthernEurope, Continent.Europe));
		territories.put(CountryName.WesternEurope, new Territory(CountryName.WesternEurope, Continent.Europe));
		territories.put(CountryName.SouthernEurope, new Territory(CountryName.SouthernEurope, Continent.Europe));
		territories.put(CountryName.Ukraine, new Territory(CountryName.Ukraine, Continent.Europe));
		territories.put(CountryName.Ural, new Territory(CountryName.Ural, Continent.Asia));
		territories.put(CountryName.Siberia, new Territory(CountryName.Siberia, Continent.Asia));
		territories.put(CountryName.Yakutsk, new Territory(CountryName.Yakutsk, Continent.Asia));
		territories.put(CountryName.Kamchatka, new Territory(CountryName.Kamchatka, Continent.Asia));
		territories.put(CountryName.Irkutsk, new Territory(CountryName.Irkutsk, Continent.Asia));
		territories.put(CountryName.Mongolia, new Territory(CountryName.Mongolia, Continent.Asia));
		territories.put(CountryName.Afghanistan, new Territory(CountryName.Afghanistan, Continent.Asia));
		territories.put(CountryName.Japan, new Territory(CountryName.Japan, Continent.Asia));
		territories.put(CountryName.China, new Territory(CountryName.China, Continent.Asia));
		territories.put(CountryName.Siam, new Territory(CountryName.Siam, Continent.Asia));
		territories.put(CountryName.India, new Territory(CountryName.India, Continent.Asia));
		territories.put(CountryName.MiddleEast, new Territory(CountryName.MiddleEast, Continent.Asia));
		territories.put(CountryName.Indonesia, new Territory(CountryName.Indonesia, Continent.Australia));
		territories.put(CountryName.NewGuinea, new Territory(CountryName.NewGuinea, Continent.Australia));
		territories.put(CountryName.EasternAustralia, new Territory(CountryName.EasternAustralia, Continent.Australia));
		territories.put(CountryName.WesternAustralia, new Territory(CountryName.WesternAustralia, Continent.Australia));
		territories.put(CountryName.NorthAfrica, new Territory(CountryName.NorthAfrica, Continent.Africa));
		territories.put(CountryName.Egypt, new Territory(CountryName.Egypt, Continent.Africa));
		territories.put(CountryName.EastAfrica, new Territory(CountryName.EastAfrica, Continent.Africa));
		territories.put(CountryName.Congo, new Territory(CountryName.Congo, Continent.Africa));
		territories.put(CountryName.Madagascar, new Territory(CountryName.Madagascar, Continent.Africa));
		territories.put(CountryName.SouthAfrica, new Territory(CountryName.SouthAfrica, Continent.Africa));

		setNeighboringCountrys(territories);
	}

	public static void setNeighboringCountrys(HashMap<CountryName, Territory> territories) {
		territories.get(CountryName.Alaska)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Kamchatka),
						territories.get(CountryName.NorthwestTerritory), territories.get(CountryName.Alberta))));

		territories.get(CountryName.Alberta)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Alaska),
						territories.get(CountryName.NorthwestTerritory), territories.get(CountryName.Ontario),
						territories.get(CountryName.WesternUnitedStates))));

		territories.get(CountryName.NorthwestTerritory).setNeighboringTerritories(
				new ArrayList<>(Arrays.asList(territories.get(CountryName.Alaska), territories.get(CountryName.Alberta),
						territories.get(CountryName.Ontario), territories.get(CountryName.Greenland))));

		territories.get(CountryName.Ontario)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(
						territories.get(CountryName.NorthwestTerritory), territories.get(CountryName.Alberta),
						territories.get(CountryName.Quebec), territories.get(CountryName.WesternUnitedStates),
						territories.get(CountryName.EasternUnitedStates), territories.get(CountryName.Greenland))));

		territories.get(CountryName.Quebec)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Ontario),
						territories.get(CountryName.EasternUnitedStates), territories.get(CountryName.Greenland))));

		territories.get(CountryName.Greenland)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(
						territories.get(CountryName.NorthwestTerritory), territories.get(CountryName.Ontario),
						territories.get(CountryName.Quebec), territories.get(CountryName.Iceland))));

		territories.get(CountryName.WesternUnitedStates)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Alberta),
						territories.get(CountryName.Ontario), territories.get(CountryName.EasternUnitedStates),
						territories.get(CountryName.CentralAmerica))));

		territories.get(CountryName.EasternUnitedStates)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(
						territories.get(CountryName.WesternUnitedStates), territories.get(CountryName.Ontario),
						territories.get(CountryName.Quebec), territories.get(CountryName.CentralAmerica))));

		territories.get(CountryName.CentralAmerica)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(
						territories.get(CountryName.WesternUnitedStates),
						territories.get(CountryName.EasternUnitedStates), territories.get(CountryName.Venezuela))));

		territories.get(CountryName.Venezuela)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.CentralAmerica),
						territories.get(CountryName.Brazil), territories.get(CountryName.Peru))));

		territories.get(CountryName.Brazil).setNeighboringTerritories(
				new ArrayList<>(Arrays.asList(territories.get(CountryName.Venezuela), territories.get(CountryName.Peru),
						territories.get(CountryName.Argentina), territories.get(CountryName.NorthAfrica))));

		territories.get(CountryName.Peru)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Venezuela),
						territories.get(CountryName.Brazil), territories.get(CountryName.Argentina))));

		territories.get(CountryName.Argentina).setNeighboringTerritories(
				new ArrayList<>(Arrays.asList(territories.get(CountryName.Peru), territories.get(CountryName.Brazil))));
		territories.get(CountryName.Iceland)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.GreatBritain),
						territories.get(CountryName.Scandinavia), territories.get(CountryName.Greenland))));
		territories.get(CountryName.Scandinavia)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.Iceland), territories.get(CountryName.GreatBritain),
								territories.get(CountryName.NorthernEurope), territories.get(CountryName.Ukraine))));
		territories.get(CountryName.GreatBritain)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Iceland),
						territories.get(CountryName.Scandinavia), territories.get(CountryName.NorthernEurope),
						territories.get(CountryName.WesternEurope))));
		territories.get(CountryName.NorthernEurope)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.GreatBritain),
						territories.get(CountryName.Scandinavia), territories.get(CountryName.Ukraine),
						territories.get(CountryName.SouthernEurope), territories.get(CountryName.WesternEurope))));
		territories.get(CountryName.WesternEurope)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.GreatBritain),
						territories.get(CountryName.NorthernEurope), territories.get(CountryName.SouthernEurope),
						territories.get(CountryName.NorthAfrica))));
		territories.get(CountryName.SouthernEurope)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.WesternEurope),
						territories.get(CountryName.NorthernEurope), territories.get(CountryName.Ukraine),
						territories.get(CountryName.MiddleEast), territories.get(CountryName.Egypt),
						territories.get(CountryName.NorthAfrica))));
		territories.get(CountryName.Ukraine)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Scandinavia),
						territories.get(CountryName.NorthernEurope), territories.get(CountryName.SouthernEurope),
						territories.get(CountryName.Afghanistan), territories.get(CountryName.Ural),
						territories.get(CountryName.Siberia))));
		territories.get(CountryName.Ural)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.Ukraine), territories.get(CountryName.Afghanistan),
								territories.get(CountryName.China), territories.get(CountryName.Siberia))));
		territories.get(CountryName.Siberia)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Ural),
						territories.get(CountryName.China), territories.get(CountryName.Mongolia),
						territories.get(CountryName.Irkutsk), territories.get(CountryName.Yakutsk))));
		territories.get(CountryName.Yakutsk)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Siberia),
						territories.get(CountryName.Irkutsk), territories.get(CountryName.Kamchatka))));
		territories.get(CountryName.Kamchatka)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.Yakutsk), territories.get(CountryName.Irkutsk),
								territories.get(CountryName.Mongolia), territories.get(CountryName.Japan))));

		territories.get(CountryName.Irkutsk)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.Siberia), territories.get(CountryName.Yakutsk),
								territories.get(CountryName.Kamchatka), territories.get(CountryName.Mongolia))));
		territories.get(CountryName.Mongolia)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Irkutsk),
						territories.get(CountryName.Kamchatka), territories.get(CountryName.Japan),
						territories.get(CountryName.China), territories.get(CountryName.Siberia))));
		territories.get(CountryName.Afghanistan)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Ukraine),
						territories.get(CountryName.Ural), territories.get(CountryName.China),
						territories.get(CountryName.India), territories.get(CountryName.MiddleEast))));
		territories.get(CountryName.Japan).setNeighboringTerritories(new ArrayList<>(
				Arrays.asList(territories.get(CountryName.Kamchatka), territories.get(CountryName.Mongolia))));
		territories.get(CountryName.China).setNeighboringTerritories(
				new ArrayList<>(Arrays.asList(territories.get(CountryName.Siberia), territories.get(CountryName.Ural),
						territories.get(CountryName.Afghanistan), territories.get(CountryName.India),
						territories.get(CountryName.Mongolia), territories.get(CountryName.Siam))));
		territories.get(CountryName.Siam)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.India),
						territories.get(CountryName.China), territories.get(CountryName.Indonesia))));
		territories.get(CountryName.India)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.MiddleEast), territories.get(CountryName.Afghanistan),
								territories.get(CountryName.China), territories.get(CountryName.Siam))));
		territories.get(CountryName.MiddleEast)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.Ukraine), territories.get(CountryName.Afghanistan),
								territories.get(CountryName.India), territories.get(CountryName.EastAfrica),
								territories.get(CountryName.Egypt), territories.get(CountryName.SouthernEurope))));
		territories.get(CountryName.Indonesia).setNeighboringTerritories(new ArrayList<>(
				Arrays.asList(territories.get(CountryName.Siam), territories.get(CountryName.NewGuinea))));
		territories.get(CountryName.NewGuinea)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Indonesia),
						territories.get(CountryName.WesternAustralia), territories.get(CountryName.EasternAustralia))));
		territories.get(CountryName.EasternAustralia).setNeighboringTerritories(new ArrayList<>(
				Arrays.asList(territories.get(CountryName.NewGuinea), territories.get(CountryName.WesternAustralia))));
		territories.get(CountryName.WesternAustralia)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Indonesia),
						territories.get(CountryName.NewGuinea), territories.get(CountryName.EasternAustralia))));
		territories.get(CountryName.Madagascar).setNeighboringTerritories(new ArrayList<>(
				Arrays.asList(territories.get(CountryName.EastAfrica), territories.get(CountryName.SouthAfrica))));
		territories.get(CountryName.NorthAfrica)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.WesternEurope),
						territories.get(CountryName.SouthernEurope), territories.get(CountryName.Egypt),
						territories.get(CountryName.EastAfrica), territories.get(CountryName.Congo))));
		territories.get(CountryName.Congo)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.NorthAfrica),
						territories.get(CountryName.EastAfrica), territories.get(CountryName.SouthAfrica))));
		territories.get(CountryName.Egypt)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.NorthAfrica), territories.get(CountryName.EastAfrica),
								territories.get(CountryName.MiddleEast), territories.get(CountryName.SouthernEurope))));
		territories.get(CountryName.EastAfrica)
				.setNeighboringTerritories(new ArrayList<>(
						Arrays.asList(territories.get(CountryName.Egypt), territories.get(CountryName.NorthAfrica),
								territories.get(CountryName.Congo), territories.get(CountryName.SouthAfrica),
								territories.get(CountryName.Madagascar), territories.get(CountryName.MiddleEast))));
		territories.get(CountryName.SouthAfrica)
				.setNeighboringTerritories(new ArrayList<>(Arrays.asList(territories.get(CountryName.Congo),
						territories.get(CountryName.EastAfrica), territories.get(CountryName.Madagascar))));

	}



}
