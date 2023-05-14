package general;

import java.io.File;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Class for sound and sound effects of the game
 * 
 * @author srogalsk
 *
 */

public final class GameSound {
	private Media media;
	private MediaPlayer musicSoundPlayer;
	private MediaPlayer effectsSoundPlayer;
	private static GameSound instance = null;
	private boolean mutePropertyEffectsSound = false;

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

	/**
	 * Method for starting the theme song
	 */
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

	/**
	 * toggle the mute/unmute sound button
	 */
	public void mute_unmute_MusicSound() {
		musicSoundPlayer.setMute(!musicSoundPlayer.isMute());
	}

	/**
	 * toggle the mute/unmute effects button
	 */
	public void mute_unmute_EffectsSound() {
		effectsSoundPlayer.setMute(!effectsSoundPlayer.isMute());
	}

	/**
	 * plays the effects button sound
	 */
	public void buttonClickForwardSound() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (!mutePropertyEffectsSound) {
					effectsSoundPlayer = new MediaPlayer(
							new Media(new File(Parameter.buttonClick03Sound).toURI().toString()));
					effectsSoundPlayer.play();
				}
			}
		});
	}

	/**
	 * plays the effects button sound
	 */
	public void buttonClickBackwardSound() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (!mutePropertyEffectsSound) {
					effectsSoundPlayer = new MediaPlayer(
							new Media(new File(Parameter.buttonClick04Sound).toURI().toString()));
					effectsSoundPlayer.play();
				}
			}
		});
	}

	/**
	 * plays the helicopter sound
	 */
	public void buttonClickHelicopterSound() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (!mutePropertyEffectsSound) {
					effectsSoundPlayer = new MediaPlayer(
							new Media(new File(Parameter.soundsdir + "helicopter-helicopter.mp3").toURI().toString()));
					effectsSoundPlayer.play();
				}
			}
		});
	}

	public MediaPlayer getEffectsSoundPlayer() {
		return effectsSoundPlayer;
	}

	public MediaPlayer getMusicSoundPlayer() {
		return musicSoundPlayer;
	}

	public boolean isMutePropertyEffectsSound() {
		return mutePropertyEffectsSound;
	}

	public void setMutePropertyEffectsSound(boolean mutePropertyEffectsSound) {
		this.mutePropertyEffectsSound = mutePropertyEffectsSound;
	}

}
