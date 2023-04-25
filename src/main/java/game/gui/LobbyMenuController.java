package game.gui;

import game.Lobby;
import game.PlayerInLobby;
import game.exceptions.WrongTextFieldInputException;
import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.PlayerCard;
import game.gui.GUISupportClasses.Spacing;
import game.logic.GameSingleplayerLogic;
import game.logic.GameType;
import game.models.Player;
import game.models.PlayerSingle;
import game.stateClient.GameStateClient;
import general.Parameter;
import general.AppController;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import database.Profile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

/*
 * Class for the Battle Frame
 * 
 * @author pmikov
 * 
 * This class creates a Lobby menu root node
 * Uses Lobby instance to initialize
 */

public class LobbyMenuController extends StackPane {
	
	Lobby lobby;
	String[] aiDifficultyLevels = {"easy", "casual", "hard"};
	
	VBox vBox;
	ImageView imgBackground;
	ImageViewPane imgBackgroundPane;
	VBox vBoxColor;
	VBox contentVBox;
	
	HBox topBannerParent;
	HBox topBannerContent;
	ArrowButton backButton;
	Label lobbyTextBanner;
	DesignButton chatButton;
	HBox chatDiv;

	HBox mainContent;
	FlowPane playerCardsPane;

	VBox settingsReadyPane;		
	VBox settingsPane;
	HBox settingsBanner;
	Label settingsName;
	VBox settingsControlPane;
		
	VBox numberPlayersDiv;
	Label numberPlayersLabel;
	HBox numberPlayersControls;
	ArrowButton lessBtnPlayers;
	Label labelBtnPlayers;
	ArrowButton moreBtnPlayers;
			
	VBox numberOfAiDiv;
	Label numberOfAiLabel;
	HBox numberOfAiControls;
	ArrowButton lessBtnAI;
	Label labelBtnAI;
	ArrowButton moreBtnAI;

	VBox AIDifficultyDiv;
	Label AIDifficultyLabel;
	HBox AIDifficultyControls;
	ArrowButton lessBtnDiff;
	Label labelBtnDiff;
	ArrowButton moreBtnDiff;
			
	HBox readyButtonPane;
	DesignButton readyBtn;
	
	double ratio;
	boolean singleplayerLobby;
	
	public LobbyMenuController(Lobby lobby, boolean singleplayerLobby) throws FileNotFoundException {
		this.lobby = lobby;
		this.ratio = Screen.getPrimary().getVisualBounds().getWidth() * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
		this.ratio = Math.min(ratio + 0.3, 1);
		this.singleplayerLobby = singleplayerLobby;
		this.setup();
		setUpPlayerCards();
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
		
		this.getChildren().addAll(vBox, vBoxColor);
		
		contentVBox = new VBox();
		contentVBox.setAlignment(Pos.CENTER);

		/*
		 * Setting up top banner
		 * Includes topBannerContent and topBannerSpacing
		 * topBannerContent has a back Button, Label and a spacing inbetween
		 */
		
		topBannerParent = new HBox(); 
		topBannerParent.setAlignment(Pos.TOP_LEFT);
		StackPane.setMargin(topBannerParent, new Insets(50 * ratio,0,0,0));
		topBannerParent.setPickOnBounds(false);

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
		
		backButton = new ArrowButton(60 * ratio);

		Spacing bannerContentSpacing = new Spacing();
		HBox.setHgrow(bannerContentSpacing, Priority.ALWAYS);
		
		lobbyTextBanner = new Label("LOBBY");
		lobbyTextBanner.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 60 * ratio));
		lobbyTextBanner.setTextFill(Color.WHITE);
		
		Spacing bannerSpacing = new Spacing();
		HBox.setHgrow(bannerSpacing, Priority.ALWAYS);
		bannerSpacing.setVisible(false);
		
		chatButton = new DesignButton(new Insets(10 * ratio, 20 * ratio, 10 * ratio, 20 * ratio), 30, 28 * ratio, 170 * ratio, true);
		chatButton.setAlignment(Pos.CENTER);
		chatDiv = new HBox();
		chatDiv.getChildren().add(chatButton);
		chatDiv.minHeightProperty().bind(chatDiv.maxHeightProperty());
		chatDiv.maxHeightProperty().bind(chatDiv.prefHeightProperty());
		chatDiv.setPrefHeight(100 * ratio);
		chatDiv.setPadding(new Insets(0, 50 * ratio, 0, 0));
		chatDiv.setAlignment(Pos.CENTER);

		
		
		topBannerContent.getChildren().addAll(backButton, bannerContentSpacing, lobbyTextBanner);
		topBannerParent.getChildren().addAll(topBannerContent, bannerSpacing, chatDiv);
		
		/*
		 * Setting up the main content
		 * Includes mainContent pane with playerCardsPane and settingsReadyPane
		 * playerCardsPane holds the cards of the joined players
		 * settingsReadyPane includes settings controlls and ready button
		 */

		mainContent = new HBox();
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setSpacing(50 * ratio);
		mainContent.setFillHeight(true);
		mainContent.setPadding(new Insets(50 * ratio, 150 * ratio, 50 * ratio, 150 * ratio));

		playerCardsPane = new FlowPane();
		playerCardsPane.setOrientation(Orientation.HORIZONTAL);
		playerCardsPane.minHeightProperty().bind(playerCardsPane.maxHeightProperty());
		playerCardsPane.maxHeightProperty().bind(playerCardsPane.prefHeightProperty());
		playerCardsPane.setPrefHeight(450 * ratio);
		playerCardsPane.minWidthProperty().bind(playerCardsPane.maxWidthProperty());
		playerCardsPane.maxWidthProperty().bind(playerCardsPane.prefWidthProperty());
		playerCardsPane.setPrefWidth(640 * ratio);
		playerCardsPane.setHgap(20 * ratio);
		playerCardsPane.setVgap(20 * ratio);
		playerCardsPane.setAlignment(Pos.TOP_LEFT);
		
		setUpPlayerCards();
		
		settingsReadyPane = new VBox();		
		settingsPane = new VBox();
		settingsBanner = new HBox();
		settingsControlPane = new VBox();
		settingsName = new Label("SETTINGS");
		
		numberPlayersDiv = new VBox();
		numberPlayersLabel = new Label(this.singleplayerLobby ? "Number of Players" : " Maximum Players");
		numberPlayersControls = new HBox();
		lessBtnPlayers = new ArrowButton(30 * ratio, 6.5 * ratio);
		labelBtnPlayers = new Label();
		moreBtnPlayers = new ArrowButton(30 * ratio, 6.5 * ratio);
		
		numberOfAiDiv = new VBox();
		numberOfAiLabel = new Label("Number of AI Players");
		numberOfAiControls = new HBox();
		lessBtnAI = new ArrowButton(30 * ratio, 6.5 * ratio);
		labelBtnAI= new Label("0");
		moreBtnAI = new ArrowButton(30 * ratio, 6.5 * ratio);
		
		AIDifficultyDiv = new VBox();
		AIDifficultyLabel = new Label("AI Player Difficulty");
		AIDifficultyControls = new HBox();
		lessBtnDiff = new ArrowButton(30 * ratio, 6.5 * ratio);
		labelBtnDiff = new Label();
		moreBtnDiff = new ArrowButton(30 * ratio, 6.5 * ratio);
		
		readyButtonPane = new HBox();
		readyBtn = new DesignButton(new Insets(5 * ratio, 25 * ratio, 5 * ratio, 20 * ratio), 25, 28 * ratio, 300 * ratio);
		
		/*
		 * Settings Pane
		 */
		
		settingsPane.minHeightProperty().bind(settingsPane.maxHeightProperty());
		settingsPane.maxHeightProperty().bind(settingsPane.prefHeightProperty());
		settingsPane.setPrefHeight(370 * ratio);
		
		settingsPane.minWidthProperty().bind(settingsPane.maxWidthProperty());
		settingsPane.maxWidthProperty().bind(settingsPane.prefWidthProperty());
		settingsPane.setPrefWidth(320 * ratio);
		
		settingsPane.setStyle("-fx-background-color: rgba(100, 68, 31, 0.7);");
		settingsPane.setFillWidth(true);

		/*
		 * Settings Banner
		 */
		
		settingsBanner.setStyle("-fx-background-color: #64441f;");
		settingsBanner.setAlignment(Pos.TOP_CENTER);
		settingsBanner.setPadding(new Insets(10 * ratio, 20 * ratio, 10 * ratio, 20 * ratio));
	
		settingsName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 32 * ratio));
		settingsName.setTextFill(Color.WHITE);
		settingsName.setAlignment(Pos.CENTER);
		
		/*
		 * Settings Controlls
		 */
		
		settingsControlPane.setSpacing(20 * ratio);
		settingsControlPane.setFillWidth(true);
		settingsControlPane.setPadding(new Insets(30 * ratio, 50 * ratio, 30 * ratio, 50 * ratio));
		settingsControlPane.setAlignment(Pos.CENTER);
		
		numberPlayersDiv.setAlignment(Pos.CENTER);
		numberOfAiDiv.setAlignment(Pos.CENTER);
		AIDifficultyDiv.setAlignment(Pos.CENTER);
		
		numberPlayersLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20 * ratio));
		numberPlayersLabel.setTextFill(Color.WHITE);
		numberPlayersLabel.setAlignment(Pos.CENTER);
		
		numberOfAiLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20 * ratio));
		numberOfAiLabel.setTextFill(Color.WHITE);
		numberOfAiLabel.setAlignment(Pos.CENTER);
		
		AIDifficultyLabel.setFont(Font.font("Cooper Black", FontWeight.BOLD, 20 * ratio));
		AIDifficultyLabel.setTextFill(Color.WHITE);
		AIDifficultyLabel.setAlignment(Pos.CENTER);
		
		lessBtnPlayers.setText("<");
		moreBtnPlayers.setText(">");
		labelBtnPlayers.setText(String.valueOf(this.singleplayerLobby ? 
				this.lobby.getHumanPlayerList().size() : 
				this.lobby.maxNumberOfPlayers));

		lessBtnAI.setText("<");
		moreBtnAI.setText(">");
		labelBtnAI.setText(String.valueOf(this.lobby.getAIPlayerList().size()));

		lessBtnDiff.setText("<");
		moreBtnDiff.setText(">");
		labelBtnDiff.setText(String.valueOf(this.aiDifficultyLevels[this.lobby.difficultyOfAI]));

		labelBtnPlayers.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
		labelBtnPlayers.textOverrunProperty().set(OverrunStyle.CLIP);
		labelBtnPlayers.setMinWidth(50 * ratio);
		labelBtnPlayers.setAlignment(Pos.CENTER);
		labelBtnPlayers.setTextFill(Color.WHITE);
		
		labelBtnAI.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
		labelBtnAI.textOverrunProperty().set(OverrunStyle.CLIP);
		labelBtnAI.setMinWidth(50 * ratio);
		labelBtnAI.setAlignment(Pos.CENTER);
		labelBtnAI.setTextFill(Color.WHITE);

		labelBtnDiff.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30 * ratio));
		labelBtnDiff.textOverrunProperty().set(OverrunStyle.CLIP);
		labelBtnDiff.setMinWidth(50 * ratio);
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
		
		readyBtn.setText("Ready");
		settingsReadyPane.setSpacing(30 * ratio);
		settingsReadyPane.setAlignment(Pos.TOP_CENTER);
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
		
		numberPlayersDiv.setSpacing(7 * ratio * ratio);
		numberOfAiDiv.setSpacing(7 * ratio); 
		AIDifficultyDiv.setSpacing(7 * ratio);
		
		settingsControlPane.getChildren().addAll(numberPlayersDiv, numberOfAiDiv, AIDifficultyDiv);
		settingsPane.getChildren().addAll(settingsBanner, settingsControlPane);
		
		settingsReadyPane.getChildren().addAll(settingsPane, readyButtonPane);

		mainContent.getChildren().addAll(playerCardsPane, settingsReadyPane);
		contentVBox.getChildren().addAll(mainContent);

		/*
		 * Action Handlers for all the buttons
		 */
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickBackwardSound();
				Node node = (Node) event.getSource();
				Stage stage = (Stage) node.getScene().getWindow();
				try {
			    	MainMenuPaneController mainMenu = new MainMenuPaneController();
					stage.getScene().setRoot(mainMenu);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				stage.show();
	
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
		    	
		    	lobby.difficultyOfAI = Arrays.asList(aiDifficultyLevels).indexOf(labelBtnDiff.getText());

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
		    	
		    	lobby.difficultyOfAI = Arrays.asList(aiDifficultyLevels).indexOf(labelBtnDiff.getText());; 	    	

	    	}
		});
		
		moreBtnAI.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	// TODO
		    	int labelBefore = Integer.parseInt(labelBtnAI.getText());
		    	if(lobby.getAIPlayerList().size() < 5 &&  lobby.maxNumberOfPlayers > lobby.getPlayerList().size()) {
		    		System.out.println("Btn click");
		    		lobby.addAI();
		    		labelBtnAI.setText(String.valueOf(labelBefore + 1));
		    	}
		    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}

		    	// TODO add remove ai players

	    	}
		});
		
		lessBtnAI.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
	    		if(lobby.getAIPlayerList().size() > 0) {
		    		lobby.removeAI();
		    		labelBtnAI.setText(String.valueOf(lobby.getAIPlayerList().size()));
		    	}
		    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}
	    	
	    	}
		});
		
		moreBtnPlayers.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	if(singleplayerLobby) {
		    		if(lobby.getHumanPlayerList().size() < 6) {
				    	Profile newProfile = new SecondaryPlayerDialog().addPlayerDialog();
				    	if(newProfile != null ) {
					    	PlayerSingle playerSingle = new PlayerSingle(newProfile);
					    	lobby.joinLobby(playerSingle);
				    		labelBtnPlayers.setText(String.valueOf(lobby.getHumanPlayerList().size()));
				    	}
			    	}
			    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}
		    		
		    	} else {
		    		if(lobby.maxNumberOfPlayers < 6) {
			    		labelBtnPlayers.setText(String.valueOf(++lobby.maxNumberOfPlayers));
			    	}
			    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}
		    	}

	    	}
		});
		
		lessBtnPlayers.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		
		    	if(singleplayerLobby) {
		    		if(lobby.getHumanPlayerList().size() > 1) {
		    			lobby.leaveLobby(lobby.getHumanPlayerList().get(lobby.getHumanPlayerList().size() - 1));
			    		labelBtnPlayers.setText(String.valueOf(lobby.getHumanPlayerList().size()));
			    	}
			    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}
		    		
		    	} else {
		        	// TODO number of players joined lobby
			    	if(lobby.maxNumberOfPlayers > 2 && lobby.maxNumberOfPlayers > lobby.getPlayerList().size()) {
			    		labelBtnPlayers.setText(String.valueOf(--lobby.maxNumberOfPlayers));
			    	}
			    	try {setUpPlayerCards();} catch (FileNotFoundException e) {}
		    	}

		    }
		});
		
		readyBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	boolean ready = readyBtn.getText().equals("Ready");
		    	if (ready) {
		    		readyBtn.setText("Not Ready");
		    	} else {
		    		readyBtn.setText("Ready");
		    	}
		    	if (singleplayerLobby) {
		    		ArrayList<Player> humans = (ArrayList<Player>) lobby.getHumanPlayerList();
		    		for(Player pl : humans) {
			    		lobby.setReady(pl, ready);	
			    		try {setUpPlayerCards();} catch (FileNotFoundException e) {}
		    			System.out.println(pl.getName() + ready);
		    		}
		    		
		    		if(lobby.isEveryoneReady() && lobby.getPlayerList().size() > 1) {
		    			System.out.println("start game");
		    			//Setting up the gameServerlogic
		    			AppController.setGameLogic(new GameSingleplayerLogic((ArrayList<Player>) lobby.getPlayerList()));
						try {
					    	Node node = (Node) event.getSource();
							// Getting the Stage where the event is happened
							Stage stage = (Stage) node.getScene().getWindow();
							// changing the AnchorPane from the main file
							// AnchorPane anchorPane = (AnchorPane) loadFXML("gameFrame");
							
							FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameFrame.fxml"));
							AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
							GamePaneController gamePaneController = fxmlLoader.getController();
							// Setting the size of the anchorPane
							stage.getScene().setRoot(anchorPane);
							
							// Setting up the gameState
			    			AppController.setGameStateClient(new GameStateClient(GameType.SinglePlayer, humans, (ArrayList<Player>) lobby.getPlayerList(), gamePaneController));
			    			AppController.getGameLogic().startGame();

							// Showing the Stage
							stage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

		    		}
		    	}
		    }
		});
		
		chatButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	// TODO
		    	System.out.println("Open Chat");
	    	}
		});
		
		this.getChildren().addAll(contentVBox, topBannerParent);
	}
	
	
	public void setUpPlayerCards() throws FileNotFoundException {
		playerCardsPane.getChildren().removeAll(playerCardsPane.getChildren());
		//ArrayList<PlayerInLobby> players = new ArrayList<>(lobby.getPlayersInLobby());
		ArrayList<Player> players = new ArrayList<>(lobby.getPlayerList());
		Iterator<Player> itt = players.iterator();
		while(itt.hasNext()) {
			Player ply = itt.next();
			PlayerCard plyc = new PlayerCard(ply, ply.getAvatar(), Color.web(ply.getColor()), ratio, lobby.isReady(ply));
			if (lobby.getAIPlayerList().contains(ply)) {
				plyc.setReady(true);
				lobby.setReady(ply, true);
			}
			playerCardsPane.getChildren().add(plyc);
		}
	}
	
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
