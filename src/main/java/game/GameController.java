package game;

import database.PlayerProfileHandler;

/**
 * Class for the actual game logic handling
 * 
 * @author srogalsk
 *
 */

public class GameController {

	private Profile profile;

	public void createFirstProfile(String userName, String firstName, String lastName, String password) throws WrongTextFieldInputException {
		
			if(userName.isBlank()) {
				throw new WrongTextFieldInputException("Username must not be blank.");
			} else if(!userName.matches("[a-zA-Z0-9]+")) {
				throw new WrongTextFieldInputException("Username must only contains characters or numbers.");
			}
			if(firstName.isBlank()) {
				throw new WrongTextFieldInputException("Firstname must not be blank.");
			} else if(!firstName.matches("^(?!-)[a-zA-Z\\-]+(?<!-)$")) {
				throw new WrongTextFieldInputException("Firstname must only contains characters or hyphens and must start and end with a character.");
			}
			if(lastName.isBlank()) {
				throw new WrongTextFieldInputException("Lastname must not be blank.");
			} else if(!lastName.matches("^(?!-)[a-zA-Z\\-]+(?<!-)$")) {
				throw new WrongTextFieldInputException("Lastname must only contains characters or hyphens and must start and end with a character.");
			}
			if(password.isBlank()) {
				throw new WrongTextFieldInputException("Password must not be blank.");
			}
			profile = new Profile(firstName, lastName, userName, password);
			PlayerProfileHandler dbH = new PlayerProfileHandler();
			dbH.createProfileData(profile);		

	}

}
