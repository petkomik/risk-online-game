package general;

import java.util.HashMap;

import database.PlayerProfileHandler;
import game.GameController;
import game.Profile;
import game.WrongTextFieldInputException;
import game.models.CountryName;
import game.models.Territory;

/**
 * Class for the application logic handling
 * 
 * @author srogalsk
 *
 */

public class AppController {
	private Profile profile;
	private HashMap<CountryName,Territory> territories;
	private static AppController appController = new AppController();
	
	private AppController() {
		
	}
	
	public static AppController getInstance() {
		return AppController.appController;
	}
	
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param userName
	 * @param password
	 * @throws WrongTextFieldInputException
	 */
	public void createFirstProfile(String firstName, String lastName, String userName, String password)
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
		profile = new Profile(firstName, lastName, userName, password);
		PlayerProfileHandler dbH = new PlayerProfileHandler();
		dbH.createProfileData(profile);
	}
}
