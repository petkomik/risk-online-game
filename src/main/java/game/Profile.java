package game;

import java.time.LocalDateTime;

/**
 * Profile class to handle data via database.
 *
 * @author jorohr
 */
public class Profile {
	
	private String color;
	private String userName;
	private String firstName;
	private String lastName;
	private int id;
	private String photo;
	private int loses;
	private int wins;
	private String password;
	
	private int isPersonal;
	
	
	public Profile(int id,  String userName, String firstName, String lastName,String color,  int wins, int loses, String photo, String password, int isPersonal) {
		
		this.setId(id);
		this.setUserName(userName);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setColor(color);
		this.setPhoto(photo);
		this.setLoses(loses);;
		this.setWins(wins);
		this.setPassword(password);
		this.setPersonal(isPersonal);
		
	}
	
	public Profile(int id, String userName, String firstName, String lastName, String color, String photo, int wins, int loses, String password) {
		
		this.setId(id);
		this.setUserName(userName);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setColor(color);
		this.setPhoto(photo);
		this.setLoses(loses);
		this.setWins(wins);
		this.setPassword(password);
		
	}

	public Profile(String firstName, String lastName, String userName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.id = Integer.parseInt(LocalDateTime.now().toString().replace("-", "").replace(":", "").replace("'", "").replace("T", "").substring(15,24));
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
		return "This is the Player : " + this.userName + " with ID: " + this.id + ". His preffered Color is  " + this.color + " and he has collected  " + this.wins + " wins in total.\n" + this.photo;
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

}