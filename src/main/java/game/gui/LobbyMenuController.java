package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.PlayerCard;
import game.gui.GUISupportClasses.Spacing;
import general.Parameter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LobbyMenuController extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/*
		 * Setting up root pane
		 * Background Image + Color Mask
		 */
		
		StackPane root = new StackPane();
		root.setAlignment(Pos.CENTER);
		
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setFillWidth(true);

		ImageView imgBackground = new ImageView();
		imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
		imgBackground.setPreserveRatio(false);
		imgBackground.setSmooth(true);
		imgBackground.setCache(true);
		
		ImageViewPane imgBackgroundPane = new ImageViewPane(imgBackground);
		HBox.setHgrow(imgBackgroundPane, Priority.ALWAYS);
		
		vBox.getChildren().add(imgBackgroundPane);
		
		VBox vBoxColor = new VBox();
		vBoxColor.setAlignment(Pos.CENTER);
		vBoxColor.setFillWidth(true);
		vBoxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");
		
		root.getChildren().addAll(vBox, vBoxColor);
		
		VBox contentVBox = new VBox();
		
		/*
		 * Setting up top banner
		 * Include go back button, text,...
		 */
		
		HBox topBannerStack = new HBox(); 
		topBannerStack.setAlignment(Pos.TOP_LEFT);
		VBox.setMargin(topBannerStack, new Insets(50,0,0,0));
		
		HBox topBannerContent = new HBox();
		topBannerContent.setAlignment(Pos.CENTER);
		topBannerContent.setFillHeight(true);
		topBannerContent.setStyle("-fx-background-color: "
				+ "linear-gradient(to right, rgba(100, 68, 31, 1) 60%, rgba(100, 68, 31, 0.7) 75%, rgba(100, 68, 31, 0) 95%);");
		topBannerContent.setMaxWidth(800);
		topBannerContent.setMinWidth(500);
		topBannerContent.setPadding(new Insets(10, 150, 10, 30));

		HBox.setHgrow(topBannerContent, Priority.ALWAYS);
		
		topBannerContent.minHeightProperty().bind(topBannerContent.maxHeightProperty());
		topBannerContent.maxHeightProperty().bind(topBannerContent.prefHeightProperty());
		topBannerContent.setPrefHeight(100);
		
		Spacing bannerSpacing = new Spacing();
		bannerSpacing.setMinWidth(100);
		bannerSpacing.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		
		Button backButton = new Button();
		backButton.setText("<");
		backButton.setAlignment(Pos.CENTER_LEFT);
		backButton.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 38));
		backButton.setPadding(new Insets(10, 20, 10, 20));
		backButton.setTextFill(Parameter.white);
		backButton.setStyle("-fx-background-color: #b87331;"
				+ "-fx-background-radius: 15;"
				+ "-fx-border-radius: 15;"
       			+ "-fx-border-color: #b87331;"
    			+ "-fx-border-width: 3px;");
		
		backButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
            	backButton.setStyle("-fx-background-color: #b87331;"
        				+ "-fx-background-radius: 15;"
        				+ "-fx-border-radius: 15;"
            			+ "-fx-border-color: #ffffff;"
            			+ "-fx-border-width: 3px;");
            } else {
            	backButton.setStyle("-fx-background-color: #b87331;"
        				+ "-fx-background-radius: 15;"
        				+ "-fx-border-radius: 15;"
            			+ "-fx-border-color: #b87331;"
            			+ "-fx-border-width: 3px;");
            }
        });
		
		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		Label lobbyTextBanner = new Label("LOBBY");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60));
		lobbyTextBanner.setTextFill(Parameter.white);
		lobbyTextBanner.setAlignment(Pos.CENTER_RIGHT);
		
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		topBannerStack.getChildren().addAll(topBannerContent, bannerSpacing);
		contentVBox.getChildren().add(topBannerStack);
		
		/*
		 * Setting up the main content pane
		 */
		
		HBox mainContent = new HBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setPadding(new Insets(100, 100, 100, 100));
		
		FlowPane playerCardsPane = new FlowPane();
		PlayerCard blonde = new PlayerCard(2, "blonde-boy.png", Parameter.blueColor);
		playerCardsPane.getChildren().add(blonde);			
		
		mainContent.getChildren().addAll(playerCardsPane);
		contentVBox.getChildren().add(mainContent);

		/*
		 * Action Handlers for all the buttons
		 */
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	
		    	}
		});
		
		root.getChildren().add(contentVBox);
	
		Scene scene = new Scene(root, 1300, 900);
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.setTitle("Lobby Menu");
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(800);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}