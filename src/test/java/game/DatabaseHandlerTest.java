package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import database.DatabaseHandler;
import database.Profile;

public class DatabaseHandlerTest {
  DatabaseHandler dbh1 = new DatabaseHandler();
  DatabaseHandler dbh2 = new DatabaseHandler();
  DatabaseHandler dbh3 = new DatabaseHandler();

  @Test
  void createProfileDataTest() {
    Profile profile = new Profile("Max", "Mustermann", "maxi", "password");
    dbh1.createProfileData(profile);
    Profile comp = dbh1.getProfileById(profile.getId());
    dbh1.disconnect();

    assertEquals(profile.getId(), comp.getId());
  }

  @Test
  void updateProfileInfoTest() {
    Profile profile = new Profile("Max", "Mustermann", "maxi2", "password");
    dbh2.createProfileData(profile);
    dbh2.updateProfileInfo("mustermann2", "UserName", profile.getId());
    Profile comp = dbh2.getProfileById(profile.getId());
    dbh2.disconnect();

    assertEquals(comp.getUserName(), "mustermann2");
  }

  @Test
  void deleteProfileTest() {
    Profile profile = new Profile("Max", "Mustermann", "maxi3", "password");
    dbh3.createProfileData(profile);
    dbh3.deleteProfile(profile.getId());
    Profile comp = dbh3.getProfileById(profile.getId());
    dbh3.disconnect();

    assertEquals(comp, null);
  }
}


