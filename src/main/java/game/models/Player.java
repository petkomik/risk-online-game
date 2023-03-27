package game.models;

import java.time.LocalTime;

import game.GameState;
import game.Profile;

/**
 * Player class to model the player entity
 *
 * @author jorohr
 */

public class Player {
	
	private GameState gameState;
	private String color;
	private String name;
	private int rank;
	
	public Player() {
		
	}
	
	
	public int getRank() {
		return rank;
	}
	

	
}
