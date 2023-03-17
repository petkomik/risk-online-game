package risk5openjfx.openjfx;

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
	}
	
}
