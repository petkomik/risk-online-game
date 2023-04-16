package game.gui;

import java.io.FileInputStream;

import game.gui.GUISupportClasses.ArrowButton;
import general.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * 
 * @author pmalamov
 *
 */

public class EndGamePodiumController extends Application {
	
	
	
	public int players = 3;
	//public int players = game.Lobby.getPlayerList().size();
	
	StackPane container;	
	HBox backgroundH;
	HBox backgroundColor;
	HBox hBoxIcons;
	HBox hBoxButton;
	ArrowButton backbutton;
	
	public EndGamePodiumController() throws Exception {
		this.container = this.setup();
	}
	
	
	public StackPane setup() throws Exception {
		
	/*
	 * to be returned Stackpane
	 * background->shade->Icons->Button
	 * Icons consists of 2 VBoxes
	 * each VBox has 2-3 Parts 
	 */
	StackPane container = new StackPane();	
	HBox backgroundH = new HBox();
	HBox backgroundColor = new HBox();
	HBox hBoxIcons = new HBox();
	HBox hBoxButton = new HBox();
	
	hBoxIcons.setAlignment(Pos.CENTER);
	hBoxButton.setAlignment(Pos.TOP_LEFT);
	
	
	VBox avatars = new VBox(50);
	VBox place = new VBox(55);
	
	/*
	 * setting background Image and color
	 */
	
	ImageView imgBackground = new ImageView();
	imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
	imgBackground.setPreserveRatio(false);
	imgBackground.setSmooth(true);
	imgBackground.setCache(true);
	
	backgroundH.getChildren().add(imgBackground);
	
	backgroundColor.setAlignment(Pos.CENTER);
	backgroundColor.setFillHeight(true);
	backgroundColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");
	
	
	
	/*
	 * setting up players avatars - in the VBox avatars
	 */
	
	StackPane firstP= new StackPane();
	StackPane secondP = new StackPane();
	StackPane thirdP = new StackPane();
		
	/*
	 * image and shape for the first avatar
	 */
	
	Circle circleFirstP = new Circle(70);
	ImageView circleFirstI = new ImageView();
	//TODO set up the right color
	circleFirstP.setFill(Parameter.blueColor);
	circleFirstP.setStroke(Color.WHITE);
	circleFirstP.setStrokeWidth(6);	
	
	firstP.getChildren().add(circleFirstP);
	
	circleFirstI.setImage(new Image(new FileInputStream(Parameter.blondBoy)));
	circleFirstI.setFitWidth(140);
	circleFirstI.setFitHeight(140);
	circleFirstI.setPreserveRatio(true);
	circleFirstI.setSmooth(true);
	circleFirstI.setCache(true);	
	
	firstP.getChildren().add(circleFirstI);
	
	
	/*
	 * image and shape for the second avatar
	 */
	
	Circle circleSecondP = new Circle(70);
	ImageView circleSecondI = new ImageView();
	//TODO set up the right color
	circleSecondP.setFill(Parameter.greenColor);
	circleSecondP.setStroke(Color.WHITE);
	circleSecondP.setStrokeWidth(6);
	
	secondP.getChildren().add(circleSecondP);
	
	circleSecondI.setImage(new Image(new FileInputStream(Parameter.hatBoy)));
	circleSecondI.setFitWidth(140);
	circleSecondI.setFitHeight(140);
	circleSecondI.setPreserveRatio(true);
	circleSecondI.setSmooth(true);
	circleSecondI.setCache(true);
	
	secondP.getChildren().add(circleSecondI);
	
	
	/*
	 * image and shape for the third avatar
	 */
	
	Circle circleThirdP = new Circle(70);
	ImageView circleThirdI = new ImageView();
	//TODO set up the right color
	circleThirdP.setFill(Parameter.yellowColor);
	circleThirdP.setStroke(Color.WHITE);
	circleThirdP.setStrokeWidth(6);
	
	thirdP.getChildren().add(circleThirdP);
	
	circleThirdI.setImage(new Image(new FileInputStream(Parameter.gingerGirl)));
	circleThirdI.setFitWidth(140);
	circleThirdI.setFitHeight(140);
	circleThirdI.setPreserveRatio(true);
	circleThirdI.setSmooth(true);
	circleThirdI.setCache(true);

	thirdP.getChildren().add(circleThirdI);

	avatars.getChildren().addAll(firstP, secondP, thirdP);
	avatars.setPadding(new Insets(300,50,300,50));
	 
	/*
	 * setting up the images for the cups 
	 */
	
	ImageView firstPlaceCup = new ImageView();
	firstPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "PodiumPlace1.png")));
	firstPlaceCup.setFitWidth(140);
	firstPlaceCup.setFitHeight(140);
	firstPlaceCup.setPreserveRatio(true);
	firstPlaceCup.setSmooth(true);
	firstPlaceCup.setCache(true);
	
	ImageView secondPlaceCup = new ImageView();
	secondPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "PodiumPlace2.png")));
	secondPlaceCup.setFitWidth(140);
	secondPlaceCup.setFitHeight(140);
	secondPlaceCup.setPreserveRatio(true);
	secondPlaceCup.setSmooth(true);
	secondPlaceCup.setCache(true);
	
	ImageView thirdPlaceCup = new ImageView();
	thirdPlaceCup.setImage(new Image(new FileInputStream(Parameter.podiumdir + "PodiumPlace3.png")));
	thirdPlaceCup.setFitWidth(140);
	thirdPlaceCup.setFitHeight(140);
	thirdPlaceCup.setPreserveRatio(true);
	thirdPlaceCup.setSmooth(true);
	thirdPlaceCup.setCache(true);
	
	/*
	 * adding the cups to their vBox
	 */
	
	place.getChildren().addAll(firstPlaceCup, secondPlaceCup, thirdPlaceCup);
	place.setPadding(new Insets(300,50,300,50));
	
	/*
	 * adding the cups and avatars to the hBox
	 */
	
	hBoxIcons.getChildren().addAll(place,avatars);
	
	/*
	 * setting up back button
	 */
	
	ArrowButton backButton = new ArrowButton();
	hBoxButton.getChildren().add(backButton);
	hBoxButton.setPadding(new Insets(20,20,20,20));
	
	/*
	 * Action handler for the button
	 */
	
	backButton.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
		(new GameSound()).buttonClickForwardSound();
		//TODO has to return the player to the ServerMainWindow
		}
	});
	
	/*
	 * adding everything to the top container
	 */
	
	container.getChildren().addAll(backgroundH, backgroundColor, hBoxIcons, hBoxButton);	
	
	return container;
	}
		

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Scene scene = new Scene(setup(),1300,900);
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
