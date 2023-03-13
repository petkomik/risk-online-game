//@author Stefan Rogalski
package game.models;

import java.time.LocalTime;

public class PlayerAI extends Player {

	public PlayerAI(int id, int wins, String name, String color, String picturePath, LocalTime gameTime) {
		super(id, wins, name, color, picturePath, gameTime, false);
	}

}
