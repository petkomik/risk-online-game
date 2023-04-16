package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import game.Lobby;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.PlayerCard;
import game.gui.GUISupportClasses.Spacing;
import general.Parameter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
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

/*
 * Class for the Battle Frame
 * 
 * @author pmikov
 * 
 * This class creates a Lobby menu root node
 * Uses Lobby instance to initialize
 */

public class LobbyMenuController extends StackPane {
	
	FlowPane playerCardsPane;
	Lobby lobby;
	
	//
	int joinedPlayersAll = 1;
	int joinedPlayersHuman = 1;
	int joinedPlayersAI = 0;
	int maxNumberOfPlayers = 6;
	int difficultyOfAI = 0;
	String[] aiDifficultyLevels = {"easy", "hard", "jojo"};
		
	public LobbyMenuController() throws Exception {
		this.setup();		
	}
	
	public LobbyMenuController(Lobby lobby) {
		
	}
	
	/**
     * 
     * @throws FileNotFoundException if some of the images cannot be found
     * 			is eihter world-map.png or any avatar png
     * 
     * This method builds and paints the lobby gui
     * Includes action handlers for the buttons
     * 
     */

	public void setup() throws FileNotFoundException {
		
		this.setAlignment(Pos.CENTER);
		
		/*
		 * First layer of stack
		 * Background map image
		 */
		
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setFillWidth(true);

		ImageView imgBackground = new ImageView();
		imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
		imgBackground.setPreserveRatio(false);
		imgBackground.setSmooth(true);
		imgBackground.setCache(true);
		
		ImageViewPane imgBackgroundPane = new ImageViewPane(imgBackground);
		VBox.setVgrow(imgBackgroundPane, Priority.ALWAYS);
		
		vBox.getChildren().add(imgBackgroundPane);
		
		/* 
		 * Second layer of stack
		 * Color mask
		 */
		
		VBox vBoxColor = new VBox();
		vBoxColor.setAlignment(Pos.CENTER);
		vBoxColor.setFillWidth(true);
		vBoxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.9);");
		
		this.getChildren().addAll(vBox, vBoxColor);
		
		VBox contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);

		/*
		 * Setting up top banner
		 * Includes topBannerContent and topBannerSpacing
		 * topBannerContent has a back Button, Label and a spacing inbetween
		 */
		
		HBox topBannerParent = new HBox(); 
		topBannerParent.setAlignment(Pos.TOP_LEFT);
		StackPane.setMargin(topBannerParent, new Insets(50,0,0,0));
		topBannerParent.setPickOnBounds(false);

		HBox topBannerContent = new HBox();
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
		
		ArrowButton backButton = new ArrowButton();
		
		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		Label lobbyTextBanner = new Label("LOBBY");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing);
		
		/*
		 * Setting up the main content
		 * Includes mainContent pane with playerCardsPane and settingsReadyPane
		 * playerCardsPane holds the cards of the joined players
		 * settingsReadyPane includes settings controlls and ready button
		 */

		HBox mainContent = new HBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(50);
		mainContent.setFillHeight(true);
		mainContent.setPadding(new Insets(50, 150, 50, 150));

		playerCardsPane = new FlowPane();
		playerCardsPane.setOrientation(Orientation.HORIZONTAL);
		playerCardsPane.minHeightProperty().bind(playerCardsPane.maxHeightProperty());
		playerCardsPane.maxHeightProperty().bind(playerCardsPane.prefHeightProperty());
		playerCardsPane.setPrefHeight(530);
		playerCardsPane.minWidthProperty().bind(playerCardsPane.maxWidthProperty());
		playerCardsPane.maxWidthProperty().bind(playerCardsPane.prefWidthProperty());
		playerCardsPane.setPrefWidth(650);
		playerCardsPane.setHgap(20);
		playerCardsPane.setVgap(20);
		playerCardsPane.setAlignment(Pos.CENTER_LEFT);
		
		setUpPlayerCards();
		
		VBox settingsReadyPane = new VBox();		
		VBox settingsPane = new VBox();
		HBox settingsBanner = new HBox();
		VBox settingsControlPane = new VBox();
		Label settingsName = new Label("SETTINGS");
		
		VBox numberPlayersDiv = new VBox();
		Label numberPlayersLabel = new Label("Number of Players");
		HBox numberPlayersControls = new HBox();
		ArrowButton lessBtnPlayers = new ArrowButton(1);
		Label labelBtnPlayers = new Label();
		ArrowButton moreBtnPlayers = new ArrowButton(1);
		
		VBox numberOfAiDiv = new VBox();
		Label numberOfAiLabel = new Label("Number of AI Players");
		HBox numberOfAiControls = new HBox();
		ArrowButton lessBtnAI = new ArrowButton(1);
		Label labelBtnAI= new Label("0");
		ArrowButton moreBtnAI = new ArrowButton(1);
		
		VBox AIDifficultyDiv = new VBox();
		Label AIDifficultyLabel = new Label("AI Player Difficulty");
		HBox AIDifficultyControls = new HBox();
		ArrowButton lessBtnDiff= new ArrowButton(1);
		Label labelBtnDiff= new Label();
		ArrowButton moreBtnDiff = new ArrowButton(1);
		
		HBox readyButtonPane = new HBox();
		DesignButton readyBtn = new DesignButton(new Insets(5, 80, 5, 80));
		
		/*
		 * Settings Pane
		 */
		
		settingsPane.minHeightProperty().bind(settingsPane.maxHeightProperty());
		settingsPane.maxHeightProperty().bind(settingsPane.prefHeightProperty());
		settingsPane.setPrefHeight(370);
		
		settingsPane.minWidthProperty().bind(settingsPane.maxWidthProperty());
		settingsPane.maxWidthProperty().bind(settingsPane.prefWidthProperty());
		settingsPane.setPrefWidth(320);
		
		settingsPane.setStyle("-fx-background-color: rgba(100, 68, 31, 0.7);");
		settingsPane.setFillWidth(true);

		/*
		 * Settings Banner
		 */
		
		settingsBanner.setStyle("-fx-background-color: #64441f;");
		settingsBanner.setAlignment(Pos.TOP_CENTER);
		settingsBanner.setPadding(new Insets(10, 20, 10, 20));
	
		settingsName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 32));
		settingsName.setTextFill(Color.WHITE);
		settingsName.setAlignment(Pos.CENTER);
		
		/*
		 * Settings Controlls
		 */
		
		settingsControlPane.setSpacing(20);
		settingsControlPane.setFillWidth(true);
		settingsControlPane.setPadding(new Insets(30, 50, 30, 50));
		settingsControlPane.setAlignment(Pos.CENTER);
		
		numberPlayersDiv.setAlignment(Pos.CENTER);
		numberOfAiDiv.setAlignment(Pos.CENTER);
		AIDifficultyDiv.setAlignment(Pos.CENTER);
		
		numberPlayersLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20));
		numberPlayersLabel.setTextFill(Color.WHITE);
		numberPlayersLabel.setAlignment(Pos.CENTER);
		
		numberOfAiLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20));
		numberOfAiLabel.setTextFill(Color.WHITE);
		numberOfAiLabel.setAlignment(Pos.CENTER);
		
		AIDifficultyLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20));
		AIDifficultyLabel.setTextFill(Color.WHITE);
		AIDifficultyLabel.setAlignment(Pos.CENTER);
		
		lessBtnPlayers.setText("<");
		moreBtnPlayers.setText(">");
		labelBtnPlayers.setText(String.valueOf(this.maxNumberOfPlayers));

		lessBtnAI.setText("<");
		moreBtnAI.setText(">");
		labelBtnAI.setText(String.valueOf(this.joinedPlayersAI));

		lessBtnDiff.setText("<");
		moreBtnDiff.setText(">");
		labelBtnDiff.setText(String.valueOf(this.aiDifficultyLevels[this.difficultyOfAI]));

		labelBtnPlayers.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30));
		labelBtnPlayers.textOverrunProperty().set(OverrunStyle.CLIP);
		labelBtnPlayers.setMinWidth(50);
		labelBtnPlayers.setAlignment(Pos.CENTER);
		labelBtnPlayers.setTextFill(Color.WHITE);
		
		labelBtnAI.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30));
		labelBtnAI.textOverrunProperty().set(OverrunStyle.CLIP);
		labelBtnAI.setMinWidth(50);
		labelBtnAI.setAlignment(Pos.CENTER);
		labelBtnAI.setTextFill(Color.WHITE);

		labelBtnDiff.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30));
		labelBtnDiff.textOverrunProperty().set(OverrunStyle.CLIP);
		labelBtnDiff.setMinWidth(50);
		labelBtnDiff.setAlignment(Pos.CENTER);
		labelBtnDiff.setTextFill(Color.WHITE);
		
		numberPlayersControls.setAlignment(Pos.CENTER);
		numberOfAiControls.setAlignment(Pos.CENTER);
		AIDifficultyControls.setAlignment(Pos.CENTER);
		
		numberPlayersDiv.setAlignment(Pos.CENTER);
		numberOfAiDiv.setAlignment(Pos.CENTER);
		AIDifficultyDiv.setAlignment(Pos.CENTER);
		
		Spacing spacing1 = new Spacing(1);
		Spacing spacing2 = new Spacing(1);
		Spacing spacing3 = new Spacing(1);
		Spacing spacing4 = new Spacing(1);
		Spacing spacing5 = new Spacing(1);
		Spacing spacing6 = new Spacing(1);
		Spacing spacing7 = new Spacing(150);
		Spacing spacing8 = new Spacing(1);
		spacing8.setPrefSize(250, 100);
		
		readyBtn.setText("Ready");
		settingsReadyPane.setSpacing(30);
		settingsReadyPane.setAlignment(Pos.CENTER);
		readyButtonPane.setAlignment(Pos.CENTER);
		
		settingsBanner.getChildren().add(settingsName);
		readyButtonPane.getChildren().add(readyBtn);
		
		numberPlayersControls.getChildren().addAll(lessBtnPlayers, 
				spacing1, labelBtnPlayers, spacing2, moreBtnPlayers);
		numberOfAiControls.getChildren().addAll(lessBtnAI, 
				spacing3, labelBtnAI, spacing4, moreBtnAI);
		AIDifficultyControls.getChildren().addAll(lessBtnDiff, 
				spacing5, labelBtnDiff, spacing6, moreBtnDiff);		
		
		numberPlayersDiv.getChildren().addAll(numberPlayersLabel, numberPlayersControls);
		numberOfAiDiv.getChildren().addAll(numberOfAiLabel, numberOfAiControls);
		AIDifficultyDiv.getChildren().addAll(AIDifficultyLabel, AIDifficultyControls);
		
		numberPlayersDiv.setSpacing(7);
		numberOfAiDiv.setSpacing(7); 
		AIDifficultyDiv.setSpacing(7);
		
		settingsControlPane.getChildren().addAll(numberPlayersDiv, numberOfAiDiv, AIDifficultyDiv);
		settingsPane.getChildren().addAll(settingsBanner, settingsControlPane);
		
		settingsReadyPane.getChildren().addAll(settingsPane, readyButtonPane);
		
		mainContent.getChildren().addAll(playerCardsPane, settingsReadyPane);
		contentVBox.getChildren().addAll(spacing7, mainContent, spacing8);

		/*
		 * Action Handlers for all the buttons
		 */
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	// TODO back button
		    	}
		});
		
		moreBtnDiff.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	String current = labelBtnDiff.getText();
		    	if(current.equals(aiDifficultyLevels[0])) {
		    		labelBtnDiff.setText(aiDifficultyLevels[1]);
		    	} else if(current.equals(aiDifficultyLevels[1])) { 
		    		labelBtnDiff.setText(aiDifficultyLevels[2]);
		    	} else {}
		    	
		    	difficultyOfAI = Arrays.asList(aiDifficultyLevels).indexOf(labelBtnDiff.getText());

	    	}
		});
		
		lessBtnDiff.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	String current = labelBtnDiff.getText();
		    	if(current.equals(aiDifficultyLevels[2])) {
		    		labelBtnDiff.setText(aiDifficultyLevels[1]);
		    	} else if(current.equals(aiDifficultyLevels[1])) { 
		    		labelBtnDiff.setText(aiDifficultyLevels[0]);
		    	} else {}
		    	
		    	difficultyOfAI = Arrays.asList(aiDifficultyLevels).indexOf(labelBtnDiff.getText());; 	    	

	    	}
		});
		
		moreBtnAI.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	// TODO
		    	int labelBefore = Integer.parseInt(labelBtnAI.getText());
		    	if(joinedPlayersAI < 5 && maxNumberOfPlayers > joinedPlayersAll) {
		    		labelBtnAI.setText(String.valueOf(labelBefore + 1));
		    		joinedPlayersAI++;
		    		joinedPlayersAll++;
		    	}
		    	// TODO add remove ai players

	    	}
		});
		
		lessBtnAI.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	if(joinedPlayersAI > 0) {
		    		labelBtnAI.setText(String.valueOf(--joinedPlayersAI));
		    		--joinedPlayersAll;
		    	}
		    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}

	    	}
		});
		
		moreBtnPlayers.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	if(maxNumberOfPlayers < 6) {
		    		labelBtnPlayers.setText(String.valueOf(++maxNumberOfPlayers));
		    	}
		    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}

	    	}
		});
		
		lessBtnPlayers.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	// TODO number of players joined lobby
		    	if(maxNumberOfPlayers > 2 && maxNumberOfPlayers > joinedPlayersAll) {
		    		labelBtnPlayers.setText(String.valueOf(--maxNumberOfPlayers));
		    	}

		    }
		});
		
		readyBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	if (readyBtn.getText().equals("Ready")) {
		    		readyBtn.setText("Not Ready");
		    	} else {
		    		readyBtn.setText("Ready");
		    	}
	    	}
		});
			
		this.getChildren().addAll(contentVBox, topBannerParent);
	
	}
	
	public void setUpPlayerCards() throws FileNotFoundException {
		if(playerCardsPane.getChildren().size() == 0 ) {
			PlayerCard blonde = new PlayerCard(2, Parameter.blondBoy, Parameter.blueColor);
			PlayerCard blonde2 = new PlayerCard(3, Parameter.gingerGirl, Parameter.orangeColor);
			PlayerCard blonde3 = new PlayerCard(4, Parameter.mustacheMan, Parameter.redColor);
			PlayerCard blonde4 = new PlayerCard(6, Parameter.bruntetteBoy, Parameter.purpleColor);
			playerCardsPane.getChildren().addAll(blonde, blonde2, blonde3, blonde4);
		 
		}

		
	}
}
