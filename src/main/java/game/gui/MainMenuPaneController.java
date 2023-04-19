package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import general.AppController;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * 
 * @author majda
 * This class handles the events on the main
 */
public class MainMenuPaneController extends StackPane {
	
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	private GameSound gameSound = new GameSound();
	private Stage stage;	
	private VBox vBox;
	private ImageView imgBackground;
	private ImageViewPane imgBackgroundPane;
	private VBox vBoxColor;
	private VBox contentVBox;
	private HBox banner;
	private HBox topBannerContent;
	private Label lobbyTextBanner;
	private ArrowButton backButton;
	private VBox mainContent;
	private DesignButton playTutorialButton;
	private DesignButton profileSettingsButton;
	private DesignButton singleplayerButton;
	private DesignButton multiplayerButton;
	private DesignButton logoutButton;
	
	private AnchorPane anchorPane;

	
	public MainMenuPaneController() throws FileNotFoundException {
		super();
		setup();
		buttonEvents();
	}

	public void setup() throws FileNotFoundException {
		
		this.setAlignment(Pos.CENTER);
		
		/*
		 * First layer of stack
		 * Background map image
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
		 * Second layer of stack
		 * Color mask
		 */
		
		vBoxColor = new VBox();
		vBoxColor.setAlignment(Pos.CENTER);
		vBoxColor.setFillWidth(true);
		vBoxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");
		
		contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);
		
		banner = new HBox(); 
		banner.setAlignment(Pos.TOP_LEFT);
		VBox.setMargin(banner, new Insets(50,0,0,0));
		banner.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: "
				+ "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
				+ "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(800);
		topBannerContent.setMinWidth(500);
		topBannerContent.setPadding(new Insets(10, 150, 10, 30));
		topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
		topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
		topBannerContent.setPrefHeight(100);
		HBox.setHgrow(topBannerContent, Priority.ALWAYS);
				
		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		lobbyTextBanner = new Label("MAIN MENU");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		topBannerContent.getChildren().addAll(bannerContentSpacing, lobbyTextBanner);
		banner.getChildren().addAll(topBannerContent, bannerSpacing);
		
		mainContent = new VBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(30);
		mainContent.setMaxWidth(600);

		playTutorialButton = new DesignButton(new Insets(10, 20, 10, 20), 30, 40, 450);
		playTutorialButton.setText("Play Tutorial");
		
		profileSettingsButton = new DesignButton(new Insets(10, 20, 10, 20), 30, 40, 450);
		profileSettingsButton.setText("Profile Settings");
		
		singleplayerButton = new DesignButton(new Insets(10, 20, 10, 20), 30, 40, 450);
		singleplayerButton.setText("Singleplayer");
		
		multiplayerButton = new DesignButton(new Insets(10, 20, 10, 20), 30, 40, 450);
		multiplayerButton.setText("Multiplayer");
		
		logoutButton = new DesignButton(new Insets(10, 20, 10, 20), 30, 40, 450);
		logoutButton.setText("Log Out");
		
		mainContent.getChildren().addAll(playTutorialButton, profileSettingsButton, singleplayerButton, multiplayerButton, logoutButton);
		contentVBox.getChildren().addAll(banner, new Spacing(50), mainContent, new Spacing(50));
		this.getChildren().addAll(vBox, vBoxColor, contentVBox);
	}
	
	public void buttonEvents() {
		playTutorialButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				(new GameSound()).buttonClickForwardSound();
				Node node = (Node) event.getSource();
				// Getting the Stage where the event is happened
				stage = (Stage)node.getScene().getWindow();
				BattleFrameController battle;
				try {
					battle = new BattleFrameController();
					stage.getScene().setRoot(battle);
					battle.setCorrectTroops(battle.armiesFlowAt, true);
					battle.setCorrectTroops(battle.armiesFlowDf, false);
					stage.getScene().heightProperty().addListener(new ChangeListener<Number>() {
					    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
					    	if(newSceneHeight.doubleValue() != oldSceneHeight.doubleValue()) {
							try {
						    	battle.setCorrectTroops(battle.armiesFlowAt, true);
								battle.setCorrectTroops(battle.armiesFlowDf, false);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
					    	}
					    }
					});
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				stage.show();
		    }	
		});
		
		profileSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				(new GameSound()).buttonClickForwardSound();
				Node node = (Node) event.getSource();
				// Getting the Stage where the event is happened
				stage = (Stage)node.getScene().getWindow();
				// changing the AnchorPane from the main file
				try {
					anchorPane = (AnchorPane) loadFXML("updateSettingsFrame");
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Setting the size of the anchorPane
				anchorPane.setPrefSize(w, h);
				// Setting the AnchorPane as a root of the main scene
				stage.getScene().setRoot(anchorPane);
				// Showing the Stage
				stage.show();
				
		    }
	   });
		
		singleplayerButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				(new GameSound()).buttonClickForwardSound();
				Node node = (Node) event.getSource();
				// Getting the Stage where the event is happened
				stage = (Stage)node.getScene().getWindow();
				// changing the AnchorPane from the main file
				try {
					anchorPane = (AnchorPane) loadFXML("gameFrame");
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Setting the size of the anchorPane
				anchorPane.setPrefSize(w, h);
				// Setting the AnchorPane as a root of the main scene
				stage.getScene().setRoot(anchorPane);
				// Showing the Stage
				stage.show();
		    }
	   });
		
		multiplayerButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
				
				Node node = (Node) event.getSource();
				// Getting the Stage where the event is happened
				stage = (Stage)node.getScene().getWindow();
				// changing the AnchorPane from the main file
				try {
					anchorPane = (AnchorPane) loadFXML("MultiplayerHostJoinFrame");
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Setting the size of the anchorPane
				anchorPane.setPrefSize(w, h);
				// Setting the AnchorPane as a root of the main scene
				stage.getScene().setRoot(anchorPane);
				// Showing the Stage
				stage.show();
		    	
		    }
	   });
		
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickBackwardSound();
				
				AppController.logoutAndSetValuesToNull();
				Node node = (Node) event.getSource();
				stage = (Stage)node.getScene().getWindow();

				try {
					UserAccessPaneController stp = new UserAccessPaneController();
					stage.getScene().setRoot(stp);

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				stage.show();
		    }
	   });
	}

//	public void clickDisplayStatistics(ActionEvent e) throws IOException {
//		Node node = (Node)e.getSource();
//		// Getting the Stage where the event is happened
//		stage = (Stage)node.getScene().getWindow();
//		// changing the AnchorPane from the main file
//		anchorPane = (AnchorPane) loadFXML("displayStatistics");
//		// Setting the AnchorPane as a root of the main scene
//		stage.getScene().setRoot(anchorPane);
//		// Showing the Stage
//		stage.show();
//	}
	
	/**
     * 
     * @param fxml, file name without the ending .fxml
     * @return Parent object, to be set as a root in a Scene object
     * @throws IOException
     * 
     * This method is responsible for loading a fxml file
     */
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}


}
