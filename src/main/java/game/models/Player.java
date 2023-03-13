package game.models;

import java.time.LocalTime;

/**
 * Player class to model the player entity
 *
 * @author jorohr
 */

public class Player {
	private int id;
	private int wins;
	
	private String name;
	private String color;
	private String picturePath;
	private LocalTime gameTime;
	
	private boolean isPersonal;
	
	public Player(int id, int wins, String name, String color, String picturePath, LocalTime gameTime,
			boolean isPersonal) {
		super();
		this.id = id;
		this.wins = wins;
		this.name = name;
		this.color = color;
		this.picturePath = picturePath;
		this.gameTime = gameTime;
		this.isPersonal = isPersonal;
	}
	
	public int getId() {
		return id;
	}

	public int getWins() {
		return wins;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public LocalTime getGameTime() {
		return gameTime;
	}

	public boolean isPersonal() {
		return isPersonal;
	}

}
