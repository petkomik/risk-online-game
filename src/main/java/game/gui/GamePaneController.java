package game.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

import game.gui.GUISupportClasses.DesignButton;
import game.logic.GameType;
import game.models.CountryName;
import game.models.Player;
import gameState.ChoosePane;
import gameState.GameHandler;
import gameState.GameState;
import gameState.Period;
import gameState.Phase;
import gameState.SinglePlayerHandler;
import general.AppController;
import general.Parameter;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author majda
 * This class handles the events on the game board 
 */
public class GamePaneController implements Initializable{
	
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	
	@FXML
	private AnchorPane gameBoard;
	@FXML
	private Pane map;
	
	private ArrayList<SVGPath> countries;
	private ArrayList<StackPane> spTroopsDisplay;
	private HashMap<String, Circle> circleTroopsDisplay;
	private HashMap<String, Label> labelTroopsDisplay;	
	private int numOfPlayer;
	private int turn;
	private String[] avatars = {"blonde-boy", "bruntette-boy", "earrings-girl", "ginger-girl", "hat-boy", "mustache-man"};
	private Color[] colors = {Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED,
			Color.BROWN, Color.YELLOW};
	
	
	private VBox vb;
	private Pane[] panes;
	private Rectangle[] rectangles;
	private ImageView[] ivTimer;
	private Label[] labTimer;
	private StackPane[] stackPanes;
	private Circle[] circles;
	private ImageView[] imageviews;
	
	private Pane phaseBoard;
	private VBox vbPhase;
	private Rectangle rectPhase;
	private ProgressBar pB;
	private StackPane spPhase;
	private Circle cirPhase;
	private ImageView ivPhase;
	private StackPane spNum;
	private Circle cirNum;
	private Label labNum;
	private Label labPhase;
	private ImageView firstPhaseLogo;
	private ImageView middlePhaseLogo;
	private ImageView lastPhaseLogo;
	private Button nextPhaseButton;
	private Rectangle rectCards;
	private ImageView cardsImageView;
	private Pane cardsPane;
	private Label nextPhaseLabel;
	private Label numCardsLabel;
	
	private Pane choosingTroopsPane;
	private DesignButton lessBtn;
	private DesignButton moreBtn;
	private Label numberLabel;
	private Button trueButtonChoosingTroops;
	private Button falseButtonChoosingTroops;
	private Label choosingTroopsPhaseLabel;
	
	private GameType gameType;
	private SinglePlayerHandler singlePlayerHandler;
	private ArrayList<String> playerColors;
	private ArrayList<String> playerAvatar;
	private ArrayList<Integer> playerIDs;
	
	private Phase currentPhase;
	private Period currentPeriod;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		double scaleX = (0.7 * w)/map.getPrefWidth();
		double scaleY = (0.7 * h)/map.getPrefHeight();
		map.setScaleX(scaleX);
		map.setScaleY(-scaleY);
		
		double translateX = -(-3356.693401*scaleX+3356.9374584);
		double translateY = -(2109.9775723*(-scaleY)+2109.9955679);
		map.setTranslateX(translateX);
		map.setTranslateY(translateY);
		
		double layoutX = (w - scaleX * map.getPrefWidth()) / 2.0;
		double layoutY = ((h - scaleY * map.getPrefHeight()) / 2.0) - 20.0;
		map.setLayoutX(layoutX);
		map.setLayoutY(layoutY);
		
		for(Node n : map.getChildren()) {
			if(n instanceof StackPane) {
				n.setVisible(false);
			}
		}
		
		/*  */
		getComponents();
		Button leaveGameButton = new Button("LEAVE GAME");
		leaveGameButton.setId("leaveGameButton");
		leaveGameButton.setOnAction(e -> clickLeaveGameButton(e));
		
		leaveGameButton.setPrefSize((150.0/1536.0) * w, (50.0/864.0) * h);
		leaveGameButton.setLayoutX((40.0/1536.0) * w);
		leaveGameButton.setLayoutY((40.0/864.0) * h);
		
		gameBoard.getChildren().add(leaveGameButton);
		
		setUpPhaseBoard();
		setUpChoosingTroopsPane();
	}
	
	private double getRelativeHorz(double x) {
		return (x / 1536.0) * w;
	}
	private double getRelativeVer(double y) {
		return (y / 864.0) * h;
	}
	public void initSinglePlayer(SinglePlayerHandler singlePlayerHandler) {
		gameType = GameType.SinglePlayer;
		this.singlePlayerHandler = singlePlayerHandler;
		playerColors = new ArrayList<>();
		playerAvatar = new ArrayList<>();
		playerIDs = new ArrayList<>();
		
		for(Player p : this.singlePlayerHandler.getLobby().getPlayerList()) {
			playerColors.add(p.getColor());
			playerAvatar.add(p.getAvatar());
			playerIDs.add(p.getID());
		}
		numOfPlayer = this.singlePlayerHandler.getLobby().getPlayerList().size();
		setUpPlayerList();
		for(int i = 0; i < numOfPlayer; i++) {
			circles[i].setFill(Color.web(playerColors.get(i)));
			rectangles[i].setFill(Color.web(playerColors.get(i)));
			panes[i].setId(String.valueOf(playerIDs.get(i)));
		}
		rectangles[0].setVisible(true);
        cirPhase.setFill(Color.web(playerColors.get(0)));
        ivPhase.setImage(new Image(playerAvatar.get(0)));
		pB.setStyle("-fx-accent: " + playerColors.get(0) + ";");
        nextPhaseButton.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
    			+ "	-fx-font-size: 30px;"
    			+ "	-fx-background-color: "+ playerColors.get(0) +";");
        nextPhaseButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
        	if (newValue) {
            	nextPhaseButton.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
            			+ "	-fx-font-size: 30px;"
            			+ "	-fx-background-color: "+ playerColors.get(0) +";");
            } else {
            	nextPhaseButton.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
            			+ "	-fx-font-size: 30px;"
            			+ "	-fx-background-color: "+ makeColorHexDarker(Color.web(playerColors.get(0))) +";");
            }
	        });
        rectCards.setFill(Color.web(playerColors.get(0)));
        cirNum.setFill(Color.web(playerColors.get(0)));
        
        this.setCurrentPlayer(playerIDs.get(0));
	}
	
	private void getComponents() {
		countries = new ArrayList<>();
		spTroopsDisplay = new ArrayList<>();
		circleTroopsDisplay = new HashMap<>();
		labelTroopsDisplay = new HashMap<>();
		
		for(Node n : map.getChildren()) {
			if(n instanceof SVGPath) {
				countries.add((SVGPath) n);
			}
			if(n instanceof StackPane) {
				StackPane tmp = (StackPane) n;
				spTroopsDisplay.add(tmp);
				tmp.setMouseTransparent(true);
				for(Node node : tmp.getChildren()) {
					if(node instanceof Circle) {
						circleTroopsDisplay.put(tmp.getId().substring(2), (Circle) node);
					}
					if(node instanceof Label) {
						Label labTmp = (Label)  node;
						labTmp.setAlignment(Pos.CENTER);
						labTmp.setStyle("-fx-font-weight: bold;"
								+ "-fx-font-size: 100px;");
						labelTroopsDisplay.put(tmp.getId().substring(2), labTmp);
					}
				}
			}
		}
	}
	public void setUpPlayerList() {
		vb = new VBox();
		vb.setPrefWidth(192);
		vb.setPrefHeight(numOfPlayer * 100);
		panes = new Pane[numOfPlayer];
		rectangles = new Rectangle[numOfPlayer];
		stackPanes = new StackPane[numOfPlayer];
		circles = new Circle[numOfPlayer];
		imageviews = new ImageView[numOfPlayer];
		labTimer = new Label[numOfPlayer];
		ivTimer = new ImageView[numOfPlayer];
		
		for(int i = 0; i < numOfPlayer; i++) {
			System.out.println(playerAvatar.get(i));
			imageviews[i] = new ImageView(playerAvatar.get(i));
			imageviews[i].setFitWidth(80);
			imageviews[i].setFitHeight(80);
			circles[i] = new Circle(42);
			circles[i].setStrokeWidth(3);
			circles[i].setStroke(Color.WHITE);
			stackPanes[i] = new StackPane(circles[i], imageviews[i]);
			stackPanes[i].setLayoutX(108);
			rectangles[i] = new Rectangle(150, 84);
			rectangles[i].setStrokeWidth(0);
			rectangles[i].setOpacity(0.44);
			rectangles[i].setArcHeight(5.0);
			rectangles[i].setArcWidth(5.0);
			rectangles[i].setStrokeType(StrokeType.INSIDE);
			rectangles[i].setStrokeWidth(0.0);
			rectangles[i].setVisible(false);
			
			// Hinzufügen des BoxBlur-Effekts
			BoxBlur boxBlur = new BoxBlur();
			boxBlur.setHeight(0.0);
			boxBlur.setWidth(38.25);
			rectangles[i].setEffect(boxBlur);
			
			labTimer[i] = new Label("Timer");
			labTimer[i].setLayoutX(16.0);
			labTimer[i].setLayoutY(42.0);
			labTimer[i].setPrefHeight(34.0);
			labTimer[i].setPrefWidth(80.0);
			labTimer[i].setAlignment(Pos.CENTER);
			labTimer[i].setVisible(false);

			// ImageView
			ivTimer[i] = new ImageView(Parameter.phaseLogosdir + "timer.png");
			ivTimer[i].setFitHeight(27.0);
			ivTimer[i].setFitWidth(32.0);
			ivTimer[i].setLayoutX(40.0);
			ivTimer[i].setLayoutY(9.0);
			ivTimer[i].setPickOnBounds(true);
			ivTimer[i].setPreserveRatio(true);
			ivTimer[i].setVisible(false);
			
			panes[i] = new Pane(rectangles[i], stackPanes[i], labTimer[i], ivTimer[i]);
			
		}
		vb.getChildren().addAll(panes);
		vb.setScaleX(w / 1536.0);
		vb.setScaleY(h / 864.0);
		
		vb.setLayoutX((1335.0/1536.0) * w);
		vb.setLayoutY((h - vb.getPrefHeight() * vb.getScaleY()) / 2.0);
		
		vb.setSpacing(24);
		
		gameBoard.getChildren().add(vb);
	}
	
	public void setUpPhaseBoard() {
		phaseBoard = new Pane();
		phaseBoard.setPrefSize(getRelativeHorz(800.0), getRelativeVer(130.0));
		
		vbPhase = new VBox();
		vbPhase.setPrefSize(getRelativeHorz(250.0), getRelativeVer(130.0));
		vbPhase.setLayoutX(getRelativeHorz(341.0));
		
		rectPhase = new Rectangle(getRelativeHorz(300.0), getRelativeVer(70.0));
		rectPhase.setArcWidth(5.0);
        rectPhase.setFill(Color.WHITE);
        rectPhase.setStrokeType(StrokeType.INSIDE);
        rectPhase.setStrokeWidth(0.0);

        // Erstelle eine Fortschrittsleiste
        pB = new ProgressBar();
        pB.setPrefHeight(getRelativeVer(60.0));
        pB.setPrefWidth(getRelativeHorz(300.0));
        pB.setProgress(1.0);
        
        vbPhase.getChildren().addAll(rectPhase, pB);
        
        cirPhase = new Circle();
        cirPhase.setRadius(getRelativeHorz(42.0));
        cirPhase.setStroke(Color.WHITE);
        cirPhase.setStrokeType(StrokeType.INSIDE);
        cirPhase.setStrokeWidth(3.0);

        // Erstelle ein ImageView mit einem Bild
        ivPhase = new ImageView();
        ivPhase.setFitHeight(getRelativeHorz(80.0));
        ivPhase.setFitWidth(getRelativeHorz(80.0));
        ivPhase.setPickOnBounds(true);
        ivPhase.setPreserveRatio(true);
        
        // Füge den Kreis und das ImageView in ein StackPane
        spPhase = new StackPane();
        spPhase.setLayoutX(getRelativeHorz(282.0));
        spPhase.setLayoutY(getRelativeVer(23.0));
        spPhase.getChildren().addAll(cirPhase, ivPhase);
        
        cirNum = new Circle();
        cirNum.setRadius(getRelativeHorz(20.0));
        cirNum.setStroke(Color.WHITE);
        cirNum.setStrokeType(StrokeType.INSIDE);
        cirNum.setStrokeWidth(3.0);

        // Erstelle ein Label mit Text "5"
        labNum = new Label("0");

        // Füge den Kreis und das Label in ein StackPane
        spNum = new StackPane();
        spNum.setLayoutX(getRelativeHorz(340.0));
        spNum.setLayoutY(getRelativeVer(11.0));
        spNum.setPrefHeight(getRelativeHorz(20.0));
        spNum.setPrefWidth(getRelativeHorz(20.0));
        spNum.getChildren().addAll(cirNum, labNum);
        
        labPhase = new Label("CLAIM");
        labPhase.setPrefSize(getRelativeHorz(245.0), getRelativeVer(40.0));
        labPhase.setLayoutX(getRelativeHorz(378.0));
        labPhase.setLayoutY(getRelativeVer(81.0));
        labPhase.setAlignment(Pos.CENTER);
        labPhase.setStyle("-fx-font-family: \"Helvetica\";"
        		+ "    -fx-font-weight: bold;"
        		+ "    -fx-font-size: 30px;");

        firstPhaseLogo = new ImageView(Parameter.phaseLogosdir + "reinforce.png"); 
        firstPhaseLogo.setFitHeight(getRelativeVer(27.0));
        firstPhaseLogo.setFitWidth(getRelativeHorz(32.0));
        firstPhaseLogo.setLayoutX(getRelativeHorz(400.0));
        firstPhaseLogo.setLayoutY(getRelativeVer(27.0));
        firstPhaseLogo.setPickOnBounds(true);
        firstPhaseLogo.setPreserveRatio(true);
        firstPhaseLogo.setVisible(false);
        
        // Erstelle ein ImageView mit einem Bild
        middlePhaseLogo = new ImageView(Parameter.phaseLogosdir + "claim.png"); 
        middlePhaseLogo.setFitHeight(getRelativeVer(27.0));
        middlePhaseLogo.setFitWidth(getRelativeHorz(32.0));
        middlePhaseLogo.setLayoutX(getRelativeHorz(474.0));
        middlePhaseLogo.setLayoutY(getRelativeVer(27.0));
        middlePhaseLogo.setPickOnBounds(true);
        middlePhaseLogo.setPreserveRatio(true);
        
        lastPhaseLogo = new ImageView(Parameter.phaseLogosdir + "fortify.png"); 
        lastPhaseLogo.setFitHeight(getRelativeVer(27.0));
        lastPhaseLogo.setFitWidth(getRelativeHorz(32.0));
        lastPhaseLogo.setLayoutX(getRelativeHorz(548.0));
        lastPhaseLogo.setLayoutY(getRelativeVer(27.0));
        lastPhaseLogo.setPickOnBounds(true);
        lastPhaseLogo.setPreserveRatio(true);
        lastPhaseLogo.setVisible(false);
        
        nextPhaseButton = new Button("✓");
        nextPhaseButton.setId("nextPhaseButton");
        nextPhaseButton.setLayoutX(getRelativeHorz(676.0));
        nextPhaseButton.setLayoutY(getRelativeVer(52.0));
        nextPhaseButton.setMnemonicParsing(false);
        nextPhaseButton.setPrefHeight(getRelativeHorz(72.0));
        nextPhaseButton.setPrefWidth(getRelativeHorz(72.0));
        
        nextPhaseButton.setVisible(false);

        rectCards = new Rectangle();
        rectCards.setArcHeight(5.0);
        rectCards.setArcWidth(5.0);
		rectCards.setOpacity(0.44);
        rectCards.setHeight(getRelativeVer(84.0));
        rectCards.setLayoutX(getRelativeHorz(99.0));
        rectCards.setLayoutY(getRelativeVer(23.0));
        rectCards.setStrokeType(StrokeType.INSIDE);
        rectCards.setStrokeWidth(0.0);
        rectCards.setWidth(getRelativeHorz(150.0));
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setHeight(0.0);
        boxBlur.setWidth(38.25);
        rectCards.setEffect(boxBlur);
        rectCards.setVisible(false);

        cardsPane = new Pane();
        cardsPane.setId("cardsPane");
        cardsPane.setLayoutX(getRelativeHorz(10.0));
        cardsPane.setLayoutY(getRelativeVer(7.0));
        cardsPane.setPrefHeight(getRelativeVer(115.0));
        cardsPane.setPrefWidth(getRelativeHorz(110.0));
        cardsImageView = new ImageView(new Image(Parameter.phaseLogosdir + "cards.png"));
        cardsImageView.setFitHeight(getRelativeVer(115.0));
        cardsImageView.setFitWidth(getRelativeHorz(110.0));
        cardsImageView.setPickOnBounds(true);
        cardsImageView.setPreserveRatio(true);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.25);
        colorAdjust.setContrast(0.1);
        colorAdjust.setSaturation(-0.18);
        cardsPane.getChildren().add(cardsImageView);
        cardsPane.setEffect(colorAdjust);
        cardsPane.setVisible(false);
        
        numCardsLabel = new Label("Label");
        numCardsLabel.setLayoutX(getRelativeHorz(138.0));
        numCardsLabel.setLayoutY(getRelativeVer(45.0));
        numCardsLabel.setPrefHeight(getRelativeVer(50.0));
        numCardsLabel.setPrefWidth(getRelativeHorz(92.0));
        numCardsLabel.setVisible(false);

        // nextPhaseLabel
        nextPhaseLabel = new Label("Label");
        nextPhaseLabel.setLayoutX(getRelativeHorz(682.0));
        nextPhaseLabel.setLayoutY(getRelativeVer(6.0));
        nextPhaseLabel.setPrefHeight(getRelativeVer(34.0));
        nextPhaseLabel.setPrefWidth(getRelativeHorz(60.0));
        nextPhaseLabel.setVisible(false);
        
        phaseBoard.getChildren().addAll(vbPhase, spPhase, spNum, labPhase, firstPhaseLogo, middlePhaseLogo, lastPhaseLogo,nextPhaseButton, rectCards, cardsPane, numCardsLabel, nextPhaseLabel);
        
        phaseBoard.setLayoutX((w - phaseBoard.getPrefWidth() * phaseBoard.getScaleX()) / 2.0);
        phaseBoard.setLayoutY((700.0 / 864.0) * h);
        
        gameBoard.getChildren().add(phaseBoard);
	}
	
	public void setUpChoosingTroopsPane() {
		choosingTroopsPane = new Pane();
		choosingTroopsPane.setPrefSize(w, h);
		choosingTroopsPane.setStyle("-fx-background-color: rgba(0, 0, 255, 0.2);");

		Rectangle rectangle = new Rectangle();
		rectangle.setArcHeight(5.0);
		rectangle.setArcWidth(5.0);
		rectangle.setFill(Color.web("#ecd9c6"));
		rectangle.setStrokeWidth(0.0);
		rectangle.setWidth(getRelativeHorz(284.0));
		rectangle.setHeight(getRelativeVer(72.0));
		
		falseButtonChoosingTroops = new Button("x");
		falseButtonChoosingTroops.setLayoutX(getRelativeHorz(586.0));
		falseButtonChoosingTroops.setLayoutY(getRelativeVer(608.0));
		falseButtonChoosingTroops.setMnemonicParsing(false);
		falseButtonChoosingTroops.setPrefSize(getRelativeHorz(72.0), getRelativeHorz(72.0));

		trueButtonChoosingTroops = new Button("✓");
		trueButtonChoosingTroops.setLayoutX(getRelativeHorz(879.0));
		trueButtonChoosingTroops.setLayoutY(getRelativeVer(608.0));
		trueButtonChoosingTroops.setMnemonicParsing(false);
		trueButtonChoosingTroops.setPrefSize(getRelativeHorz(72.0), getRelativeHorz(72.0));
	
		choosingTroopsPhaseLabel = new Label();
		choosingTroopsPhaseLabel.setPrefSize(getRelativeHorz(204.0), getRelativeVer(72.0));
		
		StackPane confirmationSP = new StackPane();
		confirmationSP.getChildren().addAll(rectangle, choosingTroopsPhaseLabel);
		confirmationSP.setLayoutX(getRelativeHorz(622.0));
		confirmationSP.setLayoutY(getRelativeVer(608.0));
		
		
		lessBtn = new DesignButton();
		moreBtn = new DesignButton();
		numberLabel = new Label();
		
		lessBtn.setText("<");
		moreBtn.setText(">");
		numberLabel.setFont(Font.font("Verdana", FontWeight.BOLD, getRelativeHorz(34)));
		numberLabel.setTextFill(Color.web("#b87331"));
		numberLabel.textOverrunProperty().set(OverrunStyle.CLIP);
		numberLabel.setAlignment(Pos.CENTER);
		
		BorderPane numTroopsBP = new BorderPane();
		numTroopsBP.setLeft(lessBtn);
		numTroopsBP.setCenter(numberLabel);
		numTroopsBP.setRight(moreBtn);
		numTroopsBP.setPrefSize(getRelativeHorz(300.0), getRelativeVer(60.0));
		numTroopsBP.setLayoutX((w - numTroopsBP.getPrefWidth()) / 2.0);
		numTroopsBP.setLayoutY(getRelativeVer(514.0));
		
		choosingTroopsPane.getChildren().addAll(confirmationSP, trueButtonChoosingTroops, falseButtonChoosingTroops, numTroopsBP);
		choosingTroopsPane.setVisible(true);
		gameBoard.getChildren().add(choosingTroopsPane);
		

		
	}
	
	public void decreaseProgressbar() {
		pB.setProgress(pB.getProgress()-1);
	}
	
	public void clickCountry(MouseEvent e) {
		String countryName = ((SVGPath) e.getSource()).getId();
		int idOfPlayer = singlePlayerHandler.getGameHandler().getGameState().getCurrentPlayer().getID();
		CountryName country = CountryName.valueOf(countryName);
		switch (gameType) {
		case SinglePlayer:
			singlePlayerHandler.getGameHandler().clickCountry(idOfPlayer, country);
			break;

		case Tutorial:
			
			break;
		case Multiplayer:
			
			break;
		default:
			break;
		}
	}
	
	
	public void claimCountry(CountryName countryName, int id) {
		Player player = singlePlayerHandler.getGameHandler().getGameState().getPlayers().get(id);
		for(StackPane sp : spTroopsDisplay) {
			if(sp.getId().equals("sp"+countryName.toString())) {
				sp.setVisible(true);
			}
		}
		labelTroopsDisplay.get(countryName.toString()).setText(String.valueOf(1));
		circleTroopsDisplay.get(countryName.toString()).setFill(Color.web(player.getColor()));
		for(SVGPath s : countries) {
			if(s.getId().equals(countryName.toString())) {
            	s.setStyle("-fx-fill: "+ player.getColor() +";");
				s.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
		        	if (newValue) {
		            	s.setStyle("-fx-fill: "+ makeColorHexDarker(Color.web(player.getColor())) +";");
		            } else {
		            	s.setStyle("-fx-fill: "+ player.getColor() +";");
		            }
			        });
			}
		}
	}
	public void setCurrentPlayer(int id) {
		Player player = singlePlayerHandler.getGameHandler().getGameState().getPlayers().get(id);
		for(int i = 0; i < playerIDs.size(); i++) {
			if(player.getID() == playerIDs.get(i)) {
				turn = i;
			}
		}
		for(int i = 0; i < numOfPlayer; i++) {
			rectangles[i].setVisible(i == turn);
			labTimer[i].setVisible(i == turn);
			ivTimer[i].setVisible(i == turn);
		}
		cirPhase.setFill(Color.web(playerColors.get(turn)));
		ivPhase.setImage(new Image(playerAvatar.get(turn)));
		cirNum.setFill(Color.web(playerColors.get(turn)));
		pB.setStyle("-fx-accent: " + playerColors.get(turn) + ";");
		rectCards.setFill(Color.web(playerColors.get(turn)));
		cardsPane.setStyle(null);
		numCardsLabel.setText(String.valueOf(player.getCards().size()));
	}
	
	public void setNumTroops(CountryName countryName, int numTroops) {
		labelTroopsDisplay.get(countryName.toString()).setText(String.valueOf(numTroops));;
	}
	
	
	public void showChoosingTroopsPane(CountryName countryName, int minTroops, int maxTroops, ChoosePane choosePane) {
		choosingTroopsPane.setVisible(true);
		choosingTroopsPhaseLabel.setText(choosePane.toString());
		this.numberLabel.setText(String.valueOf(minTroops));
		if(currentPhase == Phase.ATTACK) {
			falseButtonChoosingTroops.setVisible(false);
		}
		
		falseButtonChoosingTroops.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
    			+ "	-fx-font-size: 30px;"
    			+ "	-fx-background-color: "+ playerColors.get(turn) +";");
		falseButtonChoosingTroops.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
        	if (newValue) {
        		falseButtonChoosingTroops.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
            			+ "	-fx-font-size: 30px;"
            			+ "	-fx-background-color: "+ makeColorHexDarker(Color.web(playerColors.get(turn))) +";");
            } else {
            	falseButtonChoosingTroops.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
            			+ "	-fx-font-size: 30px;"
            			+ "	-fx-background-color: "+ playerColors.get(turn) +";");
            }
	        });
		falseButtonChoosingTroops.setOnAction(e -> clickFalseButtonChoosingTroops(e));
		trueButtonChoosingTroops.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
    			+ "	-fx-font-size: 30px;"
    			+ "	-fx-background-color: "+ playerColors.get(turn) +";");
        trueButtonChoosingTroops.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
        	if (newValue) {
            	trueButtonChoosingTroops.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
            			+ "	-fx-font-size: 30px;"
            			+ "	-fx-background-color: "+ makeColorHexDarker(Color.web(playerColors.get(turn))) +";");
            } else {
            	trueButtonChoosingTroops.setStyle("-fx-shape: \"M 30 0 A 30 30 0 1 1 30 60 A 30 30 0 1 1 30 0\";"
            			+ "	-fx-font-size: 30px;"
            			+ "	-fx-background-color: "+ playerColors.get(turn) +";");
            }
	        });
        trueButtonChoosingTroops.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	singlePlayerHandler.confirmNumberOfTroops(countryName, Integer.parseInt(numberLabel.getText()), choosePane);
		    }
		});
        
		lessBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	
		    	if (Integer.parseInt(numberLabel.getText()) > minTroops) {
		    		int i = Integer.parseInt(numberLabel.getText()) - 1;
			    	numberLabel.setText(String.valueOf(i));		    	
		    	}

		    }
		});
		
		moreBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	
		    	if (Integer.parseInt(numberLabel.getText()) < maxTroops) {
		    		int i = Integer.parseInt(numberLabel.getText()) + 1;
		    		numberLabel.setText(String.valueOf(i));
		    	}
		    }
		});
	}
	
	public void setPhase(Phase phase) {
		firstPhaseLogo.setVisible(phase == Phase.REINFORCE);
		middlePhaseLogo.setVisible(phase == Phase.ATTACK);
		lastPhaseLogo.setVisible(phase == Phase.FORTIFY);
		labPhase.setText(phase.toString());
		this.currentPhase = phase;
	}
	
	public void setPeriod(Period period) {
		String path = Parameter.phaseLogosdir;
		switch(period) {
		case COUNTRYPOSESSION:
			labPhase.setText(period.toString());
			path += "claim.png";
			break;
		case INITIALDEPLOY:
			path += "claim.png";
			break;
		case MAINPERIOD:
			labPhase.setText(period.toString());
			path += "attack.png";
			break;
		default:
			break;
		}
		middlePhaseLogo.setImage(new Image(path));
		this.currentPeriod = period;
	}
	
	
	public void showNextPhaseButton() {
		nextPhaseButton.setVisible(true);
	}
	
	public void showCards() {
		rectCards.setVisible(true);
		cardsPane.setVisible(true);
	}
	
	public void turnCountryGrey(CountryName countryName) {
		for(SVGPath s : countries) {
			if(s.getId().equals(countryName.toString())) {
				ColorAdjust colorAdjust = new ColorAdjust();
		        colorAdjust.setBrightness(-0.2);

		        s.setEffect(colorAdjust);
			}
		}
	}
	
	public void pointUpCountry(CountryName countryName) {
		for(SVGPath s : countries) {
			if(s.getId().equals(countryName.toString())) {
				Lighting lighting = new Lighting();
				lighting.setBumpInput(null);
				lighting.setDiffuseConstant(1.68);
				lighting.setSpecularConstant(2.0);
				lighting.setSpecularExponent(40.0);
				lighting.setSurfaceScale(10.0);

				Light.Distant light = new Light.Distant();
				light.setColor((Color) s.getFill());

				lighting.setLight(light);

				s.setEffect(lighting);
			}
		}
	}
	
	public void activateCountry(CountryName countryName) {
		for(SVGPath s : countries) {
			if(s.getId().equals(countryName.toString())) {
				s.setDisable(false);
			}
		}
	}
	
	public void deactivateCountry(CountryName countryName) {
		for(SVGPath s : countries) {
			if(s.getId().equals(countryName.toString())) {
				s.setDisable(true);
			}
		}
	}
	
	public void removeAmountOfTroopsLeftToDeploy() {
		spNum.setVisible(false);
	}
	
	public void setAmountOfTroopsLeftToDeploy(int number) {
		labNum.setText(String.valueOf(number));
	}
	
	public void endGame(LinkedList<Player> playersByRank) {
		
	}
	
	public void showException(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.setHeaderText("ERROR");
		alert.setTitle("");
		Stage tmp = (Stage)alert.getDialogPane().getScene().getWindow();
		tmp.getIcons().add(new Image(Parameter.errorIcon));
		alert.showAndWait();
	}
	
	private String toHex(Color c) {
		String colorHex = String.format("#%02X%02X%02X",
                (int)( c.getRed() * 255 ),
                (int)( c.getGreen() * 255 ),
                (int)( c.getBlue() * 255 ));
		return colorHex;
	}
	
	private String makeColorHexDarker(Color c) {
		String colorHex = String.format("#%02X%02X%02X",
                (int)( (c.getRed() * 255 - 20) >= 0 ? c.getRed() * 255 - 20:0),
                (int)( (c.getGreen() * 255 - 20) >= 0 ? c.getGreen() * 255 - 20:0),
                (int)( (c.getBlue() * 255 - 20) >= 0 ? c.getBlue() * 255 - 20:0));
		return colorHex;
	}
	
	private void clickLeaveGameButton(ActionEvent e) {
		
	}
	private void clickTrueButtonChoosingTroops(ActionEvent e) {
		
	}
	private void clickFalseButtonChoosingTroops(ActionEvent e) {

	}
}