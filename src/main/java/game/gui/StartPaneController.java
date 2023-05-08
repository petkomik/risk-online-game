package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import game.Lobby;
import game.gui.GUISupportClasses.ChatButton;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.MenuButton;
import game.gui.GUISupportClasses.SettingsButton;
import game.gui.GUISupportClasses.SettingsPane;
import game.gui.GUISupportClasses.Spacing;
import general.AppController;
import general.GameSound;
import general.Parameter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 * @author majda this class handles the events on the very first frame
 */
public class StartPaneController extends StackPane {
	private Stage stage;
	private VBox vBox;
	private ImageView imgBackground;
	private ImageViewPane imgBackgroundPane;
	private VBox vBoxColor;
	private VBox contentVBox;
	private ImageView riskLogo;
	private DesignButton playButton;
	private SettingsButton settingsButton;
	private StackPane settingsPane;
	private double ratio;

	private GameSound gameSound = AppController.getGameSound();

	public StartPaneController() throws FileNotFoundException {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		setup();
	}

	public void setup() throws FileNotFoundException {

		this.setAlignment(Pos.CENTER);

		/*
		 * First layer of stack Background map image
		 */

		vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setFillWidth(true);

		imgBackground = new ImageView();
		imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
		imgBackground.setPreserveRatio(false);
		imgBackground.setSmooth(true);
		imgBackground.setCache(true);

		imgBackgroundPane = new ImageViewPane(imgBackground);
		VBox.setVgrow(imgBackgroundPane, Priority.ALWAYS);

		vBox.getChildren().add(imgBackgroundPane);

		/*
		 * Second layer of stack Color mask
		 */

		vBoxColor = new VBox();
		vBoxColor.setAlignment(Pos.CENTER);
		vBoxColor.setFillWidth(true);
		vBoxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.7);");

		contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);

		riskLogo = new ImageView();
		riskLogo.setImage(new Image(new FileInputStream(Parameter.logoImage)));
		riskLogo.setFitWidth(650 * ratio);
		riskLogo.setPreserveRatio(true);
		riskLogo.setSmooth(true);
		riskLogo.setCache(true);

		settingsButton = new SettingsButton(new Insets(10 * ratio, 20 * ratio, 10 * ratio, 20 * ratio), 30, 28 * ratio,
				170 * ratio, true);

		playButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 300 * ratio);
		playButton.setText("Play");

		contentVBox.setSpacing(30 * ratio);

		contentVBox.getChildren().addAll(riskLogo, playButton, settingsButton);
		contentVBox.setPadding(new Insets(0, 0, 50 * ratio, 0));

		settingsPane = new StackPane();

		// maybe add vBoxColor
		this.getChildren().addAll(vBox, vBoxColor, contentVBox);

		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();
				Node node = (Node) event.getSource();
				stage = (Stage) node.getScene().getWindow();

				try {
					UserAccessPaneController stp = new UserAccessPaneController();
					stage.getScene().setRoot(stp);

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				stage.show();

			}
		});

		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();
				
				settingsPane = SettingsPane.createMutePane(28 * ratio, settingsButton, ratio);
				contentVBox.getChildren().add(settingsPane);
				settingsPane.setVisible(true);
				settingsButton.setDisable(true);
				
			}
		});
		// For deleting the settingsPane imidiatly
		settingsButton.disabledProperty().addListener((ChangeListener<? super Boolean>) new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(!settingsPane.isVisible()) {
					contentVBox.getChildren().removeAll(settingsPane);
				}
		    }
		});

		contentVBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					playButton.fire();

				}
			}
		});
	}
}
