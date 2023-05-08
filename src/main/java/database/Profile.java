package database;

import java.io.Serializable;
import java.time.LocalDateTime;

import general.Parameter;
import javafx.scene.paint.Color;

/**
 * Profile class to handle data via database.
 *
 * @author jorohr
 */
public class Profile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String userName;
	private String firstName;
	private String lastName;
	private String color;
	private String password;
	private int wins;
	private int loses;
	private String photo;
		
	private int isPersonal;
	
	static Color[] colors = new Color[] {Parameter.blueColor, Parameter.greenColor, 
			Parameter.orangeColor, Parameter.purpleColor, 
			Parameter.redColor, Parameter.yellowColor};
	static String[] avatars = new String[] {Parameter.blondBoy, Parameter.gingerGirl,
			Parameter.bruntetteBoy, Parameter.mustacheMan,
			Parameter.earringsGirl, Parameter.hatBoy};

	public Profile(int id,  String userName, String firstName, String lastName,String color,  int wins, int loses, String photo, String password, int isPersonal) {
		
		this.color = color;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.photo = photo;
		this.loses = loses;
		this.wins = wins;
		this.password = password;
		this.isPersonal = isPersonal;
		
	}
	
	public Profile(int id, String userName, String firstName, String lastName, String color, String photo, int wins, int loses, String password) {
		
		this.color = color;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.photo = photo;
		this.loses = loses;
		this.wins = wins;
		this.password = password;
		
	}

	public Profile(String firstName, String lastName, String userName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.color = this.colorToHexCode(colors[(int) (Math.random() * 6)]);
		this.photo = avatars[(int) (Math.random() * 6)].replace(Parameter.avatarsdir, "");
		this.id = (int)Math.round(Math.random() * 10000000);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getWins() {
		return wins;
	}


	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public int isPersonal() {
		return isPersonal;
	}

	public void setPersonal(int isPersonal) {
		this.isPersonal = isPersonal;
	}
	
	public String toString() {
		return "Profile Data: \n"
				+ "Username: " + this.userName + " \n"
				+ "Firstname: " + this.firstName + " \n"
				+ "Lastname: " + this.lastName + " \n"
				+ "Wins: " + this.wins + " \n"
				+ "Loses: " + this.loses + " \n";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLoses() {
		return loses;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}
	
	public void setAttribute(String attribute, String value) {
		switch(attribute) {
		case "UserName": this.setUserName(value); break;
		case "FirstName": this.setFirstName(value); break;
		case "LastName": this.setLastName(value); break;
		case "Color": this.setColor(value); break;
		case "Photo": this.setPhoto(value); break;
		case "Password": this.setPassword(value); break;
		}
	}
	
	public String colorToHexCode( Color color ) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) ).toLowerCase();
    }
	
	// TODO delete 
	public void setAnyColorAvatar() {
		this.color = this.colorToHexCode(colors[(int) (Math.random() * 6)]);
		this.photo = avatars[(int) (Math.random() * 6)].replace(Parameter.avatarsdir, "");
	}
}