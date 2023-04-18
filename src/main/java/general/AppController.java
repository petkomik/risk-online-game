package general;


import database.PlayerProfileHandler;
import database.Profile;
import game.exceptions.WrongTextFieldInputException;
import game.logic.GameLogic;
import game.stateClient.GameStateClient;
import network.Client;

/**
 * Class for the application logic handling
 * 
 * @author srogalsk
 *
 */

public class AppController {
	private static Profile profile;
	private static AppController appController = new AppController();
	private static PlayerProfileHandler dbH = new PlayerProfileHandler();
	private static int portNumber = Parameter.portDefault;
	private static String host = Parameter.hostDefault;
	private static Client client;
	
	private static GameStateClient gameStateClient;
	private static GameLogic gameLogic;
	// private static GameMultiplayerLogic multiplayerGameLogic;


	
	/**
	 * @param firstName
	 * @param lastName
	 * @param userName
	 * @param password
	 * @throws WrongTextFieldInputException
	 */
	public static void createFirstProfile(String firstName, String lastName, String userName, String password)
			throws WrongTextFieldInputException {
		/*checking for inputs*/
		if (userName.isBlank()) {
			throw new WrongTextFieldInputException("Username must not be blank.");
		} else if (!userName.matches("[a-zA-Z0-9]+")) {
			throw new WrongTextFieldInputException("Username must only contains characters or numbers.");
		}
		if (firstName.isBlank()) {
			throw new WrongTextFieldInputException("Firstname must not be blank.");
		} else if (!firstName.matches("^(?!-)[a-zA-Z\\-]+(?<!-)$")) {
			throw new WrongTextFieldInputException(
					"Firstname must only contains characters or hyphens and must start and end with a character.");
		}
		if (lastName.isBlank()) {
			throw new WrongTextFieldInputException("Lastname must not be blank.");
		} else if (!lastName.matches("^(?!-)[a-zA-Z\\-]+(?<!-)$")) {
			throw new WrongTextFieldInputException(
					"Lastname must only contains characters or hyphens and must start and end with a character.");
		}
		if (password.isBlank()) {
			throw new WrongTextFieldInputException("Password must not be blank.");
		}
		/*Profile creating*/
		AppController.profile = new Profile(firstName, lastName, userName, password);
		dbH.createProfileData(profile);
	}
	
	public static boolean logIntoProfile(String username, String password) {
		for(Profile profile : dbH.getAllProfiles()) {
			if(profile.getUserName().equals(username) && profile.getPassword().equals(password)) {
				AppController.profile=profile;
				return true;
			}
		}
		return false;
	}
	
	public static void updateProfile(String value,String attribute) {
		dbH.updateProfileInfo(value, attribute, AppController.profile);
		AppController.profile.setAttribute(attribute, value);
	}
	
	public static void deleteProfile() {
		dbH.deleteProfile(AppController.profile.getId());
	}
	
	public static void logoutAndSetValuesToNull() {
		profile = null;
		client = null;
		gameStateClient = null;
		gameLogic = null;
	}
	
	public static AppController getInstance() {
		return AppController.appController;
	}
	
	public static Profile getProfile() {
		return AppController.profile;
	}

	public static int getPortNumber() {
		return portNumber;
	}

	public static void setPortNumber(int portNumber) {
		AppController.portNumber = portNumber;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		AppController.host = host;
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		AppController.client = client;
	}
	
	public static GameStateClient getGameStateClient() {
		return gameStateClient;
	}

	public static void setGameStateClient(GameStateClient gameStateClient) {
		AppController.gameStateClient = gameStateClient;
	}

	public static GameLogic getGameLogic() {
		return gameLogic;
	}

	public static void setGameLogic(GameLogic gameLogic) {
		AppController.gameLogic = gameLogic;
	}
}
