package general;

import java.util.Arrays;
import database.DatabaseHandler;
import database.GameStatistic;
import database.Profile;
import game.exceptions.WrongTextFieldInputException;
import network.Client;
import network.Server;

/**
 * Class for the application logic handling
 * 
 * @author srogalsk
 *
 */


public class AppController {
  private static Profile profile;
  private static AppController appController = new AppController();
  public static DatabaseHandler dbH = new DatabaseHandler();
  private static int portNumber = Parameter.portDefault;
  private static String host = Parameter.hostDefault;
  private static Client client;
  private static Server server;

  /**
   * creates the initial profile after starting the game
   * 
   * @param firstName
   * @param lastName
   * @param userName
   * @param password
   * @throws WrongTextFieldInputException
   */
  public static void createFirstProfile(String firstName, String lastName, String userName,
      String password) throws WrongTextFieldInputException {
    /* checking for inputs */
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
    /* Profile creating */
    AppController.profile = new Profile(firstName, lastName, userName, password);
    dbH.createProfileData(profile);
  }

  /**
   * 
   * creates secondary profile
   * 
   * @param firstName
   * @param lastName
   * @param userName
   * @param password
   * @return Profile
   * @throws WrongTextFieldInputException
   */
  public static Profile createFirstSecondaryProfile(String firstName, String lastName,
      String userName, String password) throws WrongTextFieldInputException {
    /* checking for inputs */
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
    /* Profile creating */
    Profile profile = new Profile(firstName, lastName, userName, password);
    dbH.createProfileData(profile);

    return profile;
  }

  /**
   * 
   * handles the log into profiles
   * 
   * @param username
   * @param password
   * @return
   */
  public static boolean logIntoProfile(String username, String password) {
    for (Profile profile : dbH.getAllProfiles()) {
      if (profile.getUserName().equals(username) && profile.getPassword().equals(password)) {
        // TODO delete
        if (profile.getColor() == null || profile.getPhoto() == null) {
          profile.setAnyColorAvatar();
        }
        AppController.profile = profile;
        return true;
      }
    }
    return false;
  }

  /**
   * 
   * handles the log in into secondary profiles
   * 
   * @param username
   * @param password
   * @return
   */
  public static Profile logIntoSecondProfile(String username, String password) {
    for (Profile profile : dbH.getAllProfiles()) {
      if (profile.getUserName().equals(username) && profile.getPassword().equals(password)) {
        return profile;
      }
    }
    return null;
  }

  /**
   * 
   * updates the profile according to the choosen attribute in the settings pane
   * 
   * @param value
   * @param attribute
   * @throws WrongTextFieldInputException
   */
  public static void updateProfile(String value, String attribute)
      throws WrongTextFieldInputException {
    /* checking for inputs */
    if (attribute.equals("UserName") && value.isBlank()) {
      throw new WrongTextFieldInputException("Username must not be blank.");
    } else if (attribute.equals("UserName") && !value.matches("[a-zA-Z0-9]+")) {
      throw new WrongTextFieldInputException("Username must only contains characters or numbers.");
    }
    if (attribute.equals("FirstName") && value.isBlank()) {
      throw new WrongTextFieldInputException("Firstname must not be blank.");
    } else if (attribute.equals("FirstName") && !value.matches("^(?!-)[a-zA-Z\\-]+(?<!-)$")) {
      throw new WrongTextFieldInputException(
          "Firstname must only contains characters or hyphens and must start and end with a character.");
    }
    if (attribute.equals("LastName") && value.isBlank()) {
      throw new WrongTextFieldInputException("Lastname must not be blank.");
    } else if (attribute.equals("LastName") && !value.matches("^(?!-)[a-zA-Z\\-]+(?<!-)$")) {
      throw new WrongTextFieldInputException(
          "Lastname must only contains characters or hyphens and must start and end with a character.");
    }
    if (attribute.equals("Password") && value.isBlank()) {
      throw new WrongTextFieldInputException("Password must not be blank.");
    }
    if (attribute.equals("UserName")) {
      for (Profile p : dbH.getAllProfiles()) {
        if (p.getUserName().equals(value)) {
          throw new WrongTextFieldInputException("This username is used already.");
        }
      }
    }
    if (attribute.equals("Color")
        && !Arrays.stream(Parameter.allColors).anyMatch(n -> n.equals(value))) {
      throw new WrongTextFieldInputException("Invalid Color Code.");
    }
    if (attribute.equals("Photo")
        && !Arrays.stream(Parameter.allAvatars).anyMatch(n -> n.equals(value))) {
      throw new WrongTextFieldInputException("Invalid Avatar.");
    }
    dbH.updateProfileInfo(value, attribute, AppController.profile.getId());
    AppController.profile.setAttribute(attribute, value);
  }

  public static void deleteProfile() {
    dbH.deleteProfile(AppController.profile.getId());
  }

  public static void createGameStatistic(GameStatistic gameStatistic) {
    dbH.createGameStatistic(gameStatistic);
  }

  public static void logoutAndSetValuesToNull() {
    profile = null;
    client = null;

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

  public static Server getServer() {
    return server;
  }

  public static void setServer(Server server) {
    AppController.server = server;
  }

  public static DatabaseHandler getDatabaseHandler() {
    return dbH;
  }

  public static void setDatabaseHandler(DatabaseHandler dbH) {
    AppController.dbH = dbH;
  }

  public static GameSound getGameSound() {
    return GameSound.getGameSoundInstance();
  }
}
