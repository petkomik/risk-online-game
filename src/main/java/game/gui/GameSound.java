package game.gui;

import java.io.File;
import java.io.IOException;

import general.Parameter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 * 
 * @author srogalsk
 *
 */
public class GameSound {
	private Media media;
	private MediaPlayer mediaPlayer;
	
	public void startThemeSong() {
		File file = new File(Parameter.themeSong);
		media = new Media(file.toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		mediaPlayer.setAutoPlay(true);
	}
	
	public void buttonClickForwardSound() {
		new MediaPlayer(new Media(new File(Parameter.buttonClick03Sound).toURI().toString())).play();
	}
	
	public void buttonClickBackwardSound() {
		new MediaPlayer(new Media(new File(Parameter.buttonClick04Sound).toURI().toString())).play();
	}
	
}
