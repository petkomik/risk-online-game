package game;

import java.io.Serializable;
import java.time.LocalDateTime;

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