package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import game.Lobby;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import game.models.PlayerSingle;
import general.AppController;
import general.Parameter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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
import javafx.stage.Screen;
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
	private VBox mainContent;
	private DesignButton playTutorialButton;
	private DesignButton profileSettingsButton;
	private DesignButton singleplayerButton;
	private DesignButton multiplayerButton;
	private DesignButton logoutButton;
	private double ratio;
	
	private AnchorPane anchorPane;

	
	public MainMenuPaneController() throws FileNotFoundException {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth() * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
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
		VBox.setMargin(banner, new Insets(50 * ratio,0,0,0));
		banner.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: "
				+ "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
				+ "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(800 * ratio);
		topBannerContent.setMinWidth(500 * ratio);
		topBannerContent.setPadding(new Insets(10 * ratio, 150 * ratio, 10 * ratio, 30 * ratio));
		topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
		topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
		topBannerContent.setPrefHeight(100 * ratio);
		HBox.setHgrow(topBannerContent, Priority.ALWAYS);
				
		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		lobbyTextBanner = new Label("MAIN MENU");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		topBannerContent.getChildren().addAll(bannerContentSpacing, lobbyTextBanner);
		banner.getChildren().addAll(topBannerContent, bannerSpacing);
		
		mainContent = new VBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(30 * ratio);
		mainContent.setMaxWidth(600 * ratio);

		playTutorialButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 30, 40 * ratio, 450 * ratio);
		playTutorialButton.setText("Play Tutorial");
		
		profileSettingsButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 30, 40 * ratio, 450 * ratio);
		profileSettingsButton.setText("Profile Settings");
		
		singleplayerButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 30, 40 * ratio, 450 * ratio);
		singleplayerButton.setText("Singleplayer");
		
		multiplayerButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 30, 40 * ratio, 450 * ratio);
		multiplayerButton.setText("Multiplayer");
		
		logoutButton = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 30, 40 * ratio, 450 * ratio);
		logoutButton.setText("Log Out");
		
		mainContent.setPadding(new Insets(0, 0, 100 * ratio, 0));
		
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
				Lobby lobby = new Lobby();
				// TODO get methods for color + avatars
				lobby.joinLobby(new PlayerSingle(AppController.getProfile().getUserName(), AppController.getProfile().getId()));
				Node node = (Node) event.getSource();
				stage = (Stage)node.getScene().getWindow();
				try {
					LobbyMenuController lobbyPane = new LobbyMenuController(lobby, true);
					stage.getScene().setRoot(lobbyPane);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
//				Node node = (Node) event.getSource();
//				// Getting the Stage where the event is happened
//				stage = (Stage)node.getScene().getWindow();
//				// changing the AnchorPane from the main file
//				try {
//					anchorPane = (AnchorPane) loadFXML("gameFrame");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				// Setting the size of the anchorPane
//				anchorPane.setPrefSize(w, h);
//				// Setting the AnchorPane as a root of the main scene
//				stage.getScene().setRoot(anchorPane);
//				// Showing the Stage
//				stage.show();
		    }
	   });
		
		multiplayerButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
				Node node = (Node) event.getSource();
				stage = (Stage)node.getScene().getWindow();
				try {
					MultplayerHostJoinController mlt = new MultplayerHostJoinController();
					stage.getScene().setRoot(mlt);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
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
