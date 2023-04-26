package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.ChatWindow;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import general.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 * @author pmalamov
 *
 */

public class EndGamePodiumController extends Application {

	public int players = 3;
	private double ratio;
	// public int players = game.Lobby.getPlayerList().size();

	private StackPane container;
	private HBox backgroundPic;
	private HBox backgroundColor;
	private VBox vBoxIcons;
	private HBox banner;
	private HBox topBannerContent;
	private Label lobbyTextBanner;
	private VBox contentVBox;
	private ArrowButton backButton;
	private Popup popup;
	private ImageView imgBackground;
	private ImageViewPane imgBackgroundPane;
	private HBox avatars;
	private StackPane firstP, secondP, thirdP;
	private Circle circleFirstP, circleSecondP, circleThirdP;
	private ImageView circleFirstI, circleSecondI, circleThirdI;
	private HBox place;
	private ImageView firstPlaceCup, secondPlaceCup, thirdPlaceCup;
	private Text caption;

	public EndGamePodiumController() throws Exception {
		super();
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
				* Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.container = this.setup();
	}

	public StackPane setup() throws Exception {

		/*
		 * to be returned Stackpane background->shade->content content consists of
		 * Icons->Button Icons consists of 2 VBoxes each VBox has 2-3 Parts
		 */
		
		container = new StackPane();
		contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);

		vBoxIcons = new VBox();
		vBoxIcons.setAlignment(Pos.CENTER);

		avatars = new HBox(50 * ratio);
		avatars.setAlignment(Pos.CENTER);
		place = new HBox(60 * ratio);
		place.setAlignment(Pos.CENTER);

		/*
		 * setting background Image and color
		 */
		
		backgroundPic = new HBox();
		backgroundColor = new HBox();

		imgBackground = new ImageView();
		imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
		imgBackground.setPreserveRatio(false);
		imgBackground.setSmooth(true);
		imgBackground.setCache(true);

		imgBackgroundPane = new ImageViewPane(imgBackground);
		HBox.setHgrow(imgBackgroundPane, Priority.ALWAYS);
		backgroundPic.getChildren().add(imgBackgroundPane);

		backgroundColor.setAlignment(Pos.CENTER);
		backgroundColor.setFillHeight(true);
		backgroundColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");

		/*
		 * setting up banner layer
		 */

		banner = new HBox();
		banner.setAlignment(Pos.TOP_LEFT);
		VBox.setMargin(banner, new Insets(50 * ratio, 0, 0, -15 * ratio));
		banner.setPickOnBounds(false);

		topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setStyle("-fx-background-color: " + "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, "
				+ "rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(800 * ratio);
		topBannerContent.setMinWidth(500 * ratio);
		topBannerContent.setPadding(new Insets(10 * ratio, 150 * ratio, 10 * ratio, 30 * ratio));
		topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
		topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
		topBannerContent.setPrefHeight(100 * ratio);
		HBox.setHgrow(topBannerContent, Priority.ALWAYS);

		backButton = new ArrowButton(60 * ratio);

		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);

		lobbyTextBanner = new Label("TOP PLAYERS");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);

		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);

		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		banner.getChildren().addAll(topBannerContent, bannerSpacing);

		/*
		 * setting up players avatars - in the VBox avatars
		 */

		firstP = new StackPane();
		secondP = new StackPane();
		thirdP = new StackPane();

		/*
		 * image and shape for the first avatar
		 */

		circleFirstP = new Circle(90 * ratio);
		circleFirstI = new ImageView();
		// TODO set up the right color
		circleFirstP.setFill(Parameter.blueColor);
		circleFirstP.setStroke(Color.WHITE);
		circleFirstP.setStrokeWidth(6);

		firstP.getChildren().add(circleFirstP);

		circleFirstI.setImage(new Image(new FileInputStream(Parameter.blondBoy)));
		circleFirstI.setFitWidth(200 * ratio);
		circleFirstI.setFitHeight(200 * ratio);
		circleFirstI.setPreserveRatio(true);
		circleFirstI.setSmooth(true);
		circleFirstI.setCache(true);

		firstP.getChildren().add(circleFirstI);

		/*
		 * image and shape for the second avatar
		 */

		circleSecondP = new Circle(80 * ratio);
		circleSecondI = new ImageView();
		// TODO set up the right color
		circleSecondP.setFill(Parameter.greenColor);
		circleSecondP.setStroke(Color.WHITE);
		circleSecondP.setStrokeWidth(6);

		secondP.getChildren().add(circleSecondP);

		circleSecondI.setImage(new Image(new FileInputStream(Parameter.hatBoy)));
		circleSecondI.setFitWidth(170 * ratio);
		circleSecondI.setFitHeight(170 * ratio);
		circleSecondI.setPreserveRatio(true);
		circleSecondI.setSmooth(true);
		circleSecondI.setCache(true);

		secondP.getChildren().add(circleSecondI);

		/*
		 * image and shape for the third avatar
		 */

		circleThirdP = new Circle(70 * ratio);
		circleThirdI = new ImageView();
		// TODO set up the right color
		if (players > 2) {
			circleThirdP.setFill(Parameter.yellowColor);
			circleThirdP.setStroke(Color.WHITE);
			circleThirdP.setStrokeWidth(6);

			thirdP.getChildren().add(circleThirdP);

			circleThirdI.setImage(new Image(new FileInputStream(Parameter.gingerGirl)));
			circleThirdI.setFitWidth(140 * ratio);
			circleThirdI.setFitHeight(140 * ratio);
			circleThirdI.setPreserveRatio(true);
			circleThirdI.setSmooth(true);
			circleThirdI.setCache(true);

			thirdP.getChildren().add(circleThirdI);
		}

		/*
		 * adding the avatars to their box
		 */
		
		if (players > 2) {
			avatars.getChildren().addAll(secondP, firstP, thirdP);
		} else {
			avatars.getChildren().addAll(firstP, secondP);
		}
		avatars.setPadding(new Insets(20, 200 * ratio, 20, 200 * ratio));

		/*
		 * setting up the images for the cups
		 */

		firstPlaceCup = new ImageView();
		firstPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "PodiumPlace1.png")));
		firstPlaceCup.setFitWidth(200 * ratio);
		firstPlaceCup.setFitHeight(200 * ratio);
		firstPlaceCup.setPreserveRatio(true);
		firstPlaceCup.setSmooth(true);
		firstPlaceCup.setCache(true);

		secondPlaceCup = new ImageView();
		secondPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "PodiumPlace2.png")));
		secondPlaceCup.setFitWidth(170 * ratio);
		secondPlaceCup.setFitHeight(170 * ratio);
		secondPlaceCup.setPreserveRatio(true);
		secondPlaceCup.setSmooth(true);
		secondPlaceCup.setCache(true);

		thirdPlaceCup = new ImageView();

		if (players > 2) {
			thirdPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "PodiumPlace3.png")));
			thirdPlaceCup.setFitWidth(140 * ratio);
			thirdPlaceCup.setFitHeight(140 * ratio);
			thirdPlaceCup.setPreserveRatio(true);
			thirdPlaceCup.setSmooth(true);
			thirdPlaceCup.setCache(true);
		}

		/*
		 * adding the cups to their vBox
		 */

		if (players > 2) {
			place.getChildren().addAll(secondPlaceCup, firstPlaceCup, thirdPlaceCup);
		} else {
			place.getChildren().addAll(firstPlaceCup, secondPlaceCup);
		}
		place.setPadding(new Insets(20 * ratio, 200 * ratio, 20 * ratio, 200 * ratio));

		/*
		 * adding endgame text
		 */
		caption = new Text("WINNER");
		caption.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 100 * ratio));
		caption.setFill(Color.GOLD);
		caption.setStroke(Color.WHITE);
		caption.setStrokeWidth(2.0);

		/*
		 * adding the cups and avatars to the hBox
		 */

		vBoxIcons.getChildren().addAll(caption, avatars, place);

		/*
		 * Action handler for the button
		 */

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				(new GameSound()).buttonClickForwardSound();
				// TODO has to return the player to the ServerMainWindow

			}
		});

		/*
		 * adding everything to the top container
		 */
		contentVBox.setAlignment(Pos.CENTER);
		contentVBox.setSpacing(30 * ratio);
		contentVBox.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());

		contentVBox.getChildren().addAll(banner, new Spacing(50), vBoxIcons, new Spacing(100));
		ChatWindow test = new ChatWindow();
		container.getChildren().addAll(backgroundPic, backgroundColor, contentVBox, test);

		return container;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Scene scene = new Scene(setup(), 1300, 900);
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.setTitle("EndPodium");
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1300);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
