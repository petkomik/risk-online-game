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
public final class GameSound {
	private Media media;
	private MediaPlayer musicSoundPlayer;
	private MediaPlayer effectsSoundPlayer;
    private static GameSound instance = null;
	
	private GameSound() {
		// Setting up musicSound
		File file = new File(Parameter.themeSong);
		media = new Media(file.toURI().toString());
		musicSoundPlayer = new MediaPlayer(media);
		
		// Setting up 
		effectsSoundPlayer = new MediaPlayer(new Media(new File(Parameter.buttonClick03Sound).toURI().toString()));
		
	}
	
	protected static GameSound getGameSoundInstance() {
        if (instance == null) {
            instance = new GameSound();
        }
        return instance;
    }

	public void startThemeSong() {
		
		musicSoundPlayer.play();
		musicSoundPlayer.setAutoPlay(true);
		musicSoundPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				musicSoundPlayer.seek(Duration.ZERO);
				musicSoundPlayer.play();
			}
		});
	}
	
	public void mute_unmute_MusicSound() {
		musicSoundPlayer.setMute(!musicSoundPlayer.isMute());
	}
	
	public void mute_unmute_EffectsSound() {
		effectsSoundPlayer.setMute(!effectsSoundPlayer.isMute());
	}

	public void buttonClickForwardSound() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				effectsSoundPlayer = new MediaPlayer(new Media(new File(Parameter.buttonClick03Sound).toURI().toString()));
				effectsSoundPlayer.play();
			}
		});
	}

	public void buttonClickBackwardSound() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				effectsSoundPlayer = new MediaPlayer(new Media(new File(Parameter.buttonClick04Sound).toURI().toString()));
				effectsSoundPlayer.play();
			}
		});
	}

	public MediaPlayer getEffectsSoundPlayer() {
		return effectsSoundPlayer;
	}
	
	public MediaPlayer getMusicSoundPlayer() {
		return musicSoundPlayer;
	}

}
// push
