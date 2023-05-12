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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import network.Client;
import network.Server;
import general.AppController;
import general.GameSound;
import general.Parameter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * First Multiplayer GUI Window. 
 * Player decides whether to host or join a server.
 *
 * @author pmikov
 *
 */

public class MultplayerHostJoinController extends StackPane {
	
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	private GameSound gameSound = AppController.getGameSound();
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
	private DesignButton hostServer;
	private DesignButton joinServer;

	private double ratio;
	
	private AnchorPane anchorPane;

	/**
	 * Default constructor. Builds the window.
	 *
	 * @throws FileNotFoundException for the background map image
	 */
	
	public MultplayerHostJoinController() throws FileNotFoundException {
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
		
		backButton = new ArrowButton(60 * ratio);

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
		
		lobbyTextBanner = new Label("MULTIPLAYER");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		banner.getChildren().addAll(topBannerContent, bannerSpacing);
		
		mainContent = new VBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(30 * ratio);
		mainContent.setMaxWidth(600 * ratio);

		hostServer = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 450 * ratio);
		hostServer.setText("Host Server");
		
		joinServer = new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 450 * ratio);
		joinServer.setText("Join Server");
		
		mainContent.setPadding(new Insets(0, 0, 100 * ratio, 0));
		
		mainContent.getChildren().addAll(hostServer, joinServer);
		contentVBox.getChildren().addAll(banner, new Spacing(50), mainContent, new Spacing(50));
		this.getChildren().addAll(vBox, vBoxColor, contentVBox);
	}
	
	/** Setting up button events. */
	
	public void buttonEvents() {
		hostServer.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	gameSound.buttonClickForwardSound();
				Node node = (Node) event.getSource();
				stage = (Stage)node.getScene().getWindow();
				try {
					ServerMainWindowController serverMenu = new ServerMainWindowController();
					serverMenu.initServer();
					serverMenu.actionEventsSetup();
					stage.getScene().setRoot(serverMenu);

				} catch (Exception e) {
					e.printStackTrace();
				}
		    }	
		});
		
		joinServer.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				gameSound.buttonClickForwardSound();
				Node node = (Node) event.getSource();
				stage = (Stage)node.getScene().getWindow();
				try {
					MultiplayerJoinWindowController joinMult = new MultiplayerJoinWindowController();
					stage.getScene().setRoot(joinMult);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				stage.show();
								
		    }
	   });
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	gameSound.buttonClickBackwardSound();
				Node node = (Node) event.getSource();
				stage = (Stage)node.getScene().getWindow();
				try {
					MainMenuPaneController mainMenu = new MainMenuPaneController();
					stage.getScene().setRoot(mainMenu);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				stage.show();
		    }
	   });
	}
}
