package general;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

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
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
				mediaPlayer.play();
			}
		});
	}

	public void buttonClickForwardSound() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new MediaPlayer(new Media(new File(Parameter.buttonClick03Sound).toURI().toString())).play();
			}
		});
	}

	public void buttonClickBackwardSound() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new MediaPlayer(new Media(new File(Parameter.buttonClick04Sound).toURI().toString())).play();
			}
		});
	}

}
