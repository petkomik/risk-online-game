package general;


import database.PlayerProfileHandler;
import game.Profile;
import game.WrongTextFieldInputException;

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
	
	public static AppController getInstance() {
		return AppController.appController;
	}
	
	public static Profile getProfile() {
		return AppController.profile;
	}
}
