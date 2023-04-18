package game.gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import general.Parameter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.scene.effect.BoxBlur;

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
	
	private int numOfPlayer;
	private int turn = 0;
	private String[] avatars = {"blonde-boy", "bruntette-boy", "earrings-girl", "ginger-girl", "hat-boy", "mustache-man"};
	private Color[] colors = {Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED,
			Color.BROWN, Color.YELLOW};
	
	
	private VBox vb;
	private Pane[] panes;
	private Rectangle[] rectangles;
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
	private ImageView logo;
	
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
		double layoutY = (h - scaleY * map.getPrefHeight()) / 2.0;
		map.setLayoutX(layoutX);
		map.setLayoutY(layoutY);
		
		for(Node n : map.getChildren()) {
			if(n instanceof StackPane) {
				n.setVisible(false);
			}
		}
		
		/*  */
		setUpPlayerList();
		
		Button leaveGameButton = new Button("LEAVE GAME");
		leaveGameButton.setId("leaveGameButton");
		leaveGameButton.setOnAction(e -> clickLeaveGameButton(e));
		
		leaveGameButton.setPrefSize((150.0/1536.0) * w, (50.0/864.0) * h);
		leaveGameButton.setLayoutX((40.0/1536.0) * w);
		leaveGameButton.setLayoutY((40.0/864.0) * h);
		
		gameBoard.getChildren().add(leaveGameButton);
		
		setUpPhaseBoard();
	}
	
	public void setUpPlayerList() {
		numOfPlayer = 3; // we need a method to get the number of the playing players
		vb = new VBox();
		vb.setPrefWidth(192);
		vb.setPrefHeight(numOfPlayer * 100);
		panes = new Pane[numOfPlayer];
		rectangles = new Rectangle[numOfPlayer];
		stackPanes = new StackPane[numOfPlayer];
		circles = new Circle[numOfPlayer];
		imageviews = new ImageView[numOfPlayer];
		
		
		for(int i = 0; i < numOfPlayer; i++) {
			imageviews[i] = new ImageView(Parameter.avatarsdir + avatars[i] + ".png");
			imageviews[i].setFitWidth(80);
			imageviews[i].setFitHeight(80);
			circles[i] = new Circle(42);
			circles[i].setStrokeWidth(3);
			circles[i].setStroke(Color.WHITE);
			circles[i].setFill(colors[i]);
			stackPanes[i] = new StackPane(circles[i], imageviews[i]);
			stackPanes[i].setLayoutX(108);
			rectangles[i] = new Rectangle(150, 84);
			rectangles[i].setStrokeWidth(0);
			rectangles[i].setFill(colors[i]);
			rectangles[i].setOpacity(0.44);
			rectangles[i].setArcHeight(5.0);
			rectangles[i].setArcWidth(5.0);
			rectangles[i].setStrokeType(StrokeType.INSIDE);
			rectangles[i].setStrokeWidth(0.0);
			
			// Hinzufügen des BoxBlur-Effekts
			BoxBlur boxBlur = new BoxBlur();
			boxBlur.setHeight(0.0);
			boxBlur.setWidth(38.25);
			rectangles[i].setEffect(boxBlur);
			panes[i] = new Pane(rectangles[i], stackPanes[i]);
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
		phaseBoard.setPrefSize(474, 130);
		
		vbPhase = new VBox();
		vbPhase.setPrefSize(250, 130);
		vbPhase.setLayoutX(87);
		
		rectPhase = new Rectangle(300, 70);
		rectPhase.setArcWidth(5.0);
        rectPhase.setFill(Color.WHITE);
        rectPhase.setStrokeType(StrokeType.INSIDE);
        rectPhase.setStrokeWidth(0.0);

        // Erstelle eine Fortschrittsleiste
        pB = new ProgressBar();
        pB.setPrefHeight(60.0);
        pB.setPrefWidth(300.0);
        pB.setProgress(1.0);
        
        vbPhase.getChildren().addAll(rectPhase, pB);
        
        cirPhase = new Circle();
        cirPhase.setFill(colors[turn]);
        cirPhase.setRadius(42.0);
        cirPhase.setStroke(Color.WHITE);
        cirPhase.setStrokeType(StrokeType.INSIDE);
        cirPhase.setStrokeWidth(3.0);

        // Erstelle ein ImageView mit einem Bild
        ivPhase = new ImageView(Parameter.avatarsdir + avatars[turn] + ".png");
        ivPhase.setFitHeight(80.0);
        ivPhase.setFitWidth(80.0);
        ivPhase.setPickOnBounds(true);
        ivPhase.setPreserveRatio(true);
        
        // Füge den Kreis und das ImageView in ein StackPane
        spPhase = new StackPane();
        spPhase.setLayoutX(28.0);
        spPhase.setLayoutY(23.0);
        spPhase.getChildren().addAll(cirPhase, ivPhase);
        
        cirNum = new Circle();
        cirNum.setFill(colors[turn]);
        cirNum.setRadius(20.0);
        cirNum.setStroke(Color.WHITE);
        cirNum.setStrokeType(StrokeType.INSIDE);
        cirNum.setStrokeWidth(3.0);

        // Erstelle ein Label mit Text "5"
        labNum = new Label("0");

        // Füge den Kreis und das Label in ein StackPane
        spNum = new StackPane();
        spNum.setLayoutX(86.0);
        spNum.setLayoutY(11.0);
        spNum.setPrefHeight(20.0);
        spNum.setPrefWidth(20.0);
        spNum.getChildren().addAll(cirNum, labNum);
        
        labPhase = new Label("CLAIM");
        labPhase.setLayoutX(211.0);
        labPhase.setLayoutY(91.0);

        // Erstelle ein ImageView mit einem Bild
        logo = new ImageView(); // we need enum for phases
        logo.setFitHeight(27.0);
        logo.setFitWidth(32.0);
        logo.setLayoutX(218.0);
        logo.setLayoutY(27.0);
        logo.setPickOnBounds(true);
        logo.setPreserveRatio(true);
        
        phaseBoard.getChildren().addAll(vbPhase, spPhase, spNum, labPhase, logo);
        phaseBoard.setScaleX(0.8 * w / 1536.0);
        phaseBoard.setScaleY(0.8 * h / 864.0);
        
        phaseBoard.setLayoutX((w - phaseBoard.getPrefWidth() * phaseBoard.getScaleX()) / 2.0);
        phaseBoard.setLayoutY((720.0 / 864.0) * h);
        
        gameBoard.getChildren().add(phaseBoard);
	}
	
	public void changeTurnColor(int playerTurn) {
		cirPhase.setFill(colors[playerTurn]);
		ivPhase.setImage(new Image(Parameter.avatarsdir + avatars[playerTurn] + ".png"));
		cirNum.setFill(colors[playerTurn]);
		pB.setStyle("-fx-accent: "+colors[playerTurn].toString()+";");
		
		for(int i = 0; i < numOfPlayer; i++) {
			if(i == playerTurn) {
				rectangles[i].setVisible(true);
			}
			else {
				rectangles[i].setVisible(false);
			}
		}
	}
	
	public void decreaseProgressbar() {
		pB.setProgress(pB.getProgress()-1);
	}
	
	public void clickCountry(MouseEvent e) {
		String countryName = ((SVGPath)e.getSource()).getId();
		// method to set the countryName from GameStateClientController
		claimCountry(countryName);
	}
	
	public void claimCountry(String countryName) {
		for(Node n : map.getChildren()) {
			if(n instanceof StackPane) {
				if(n.getId().equals("sp"+countryName)) {
					n.setVisible(true);
					for(Node node : ((StackPane) n).getChildren()) {
						if(node instanceof Label) {
							((Label) node).setText("");
						}
					}
				}
			}
		}
	}
	
	public void changePhase(String phase) {
		labPhase.setText(phase);
//		logo.setImage(new Image(Parameter.resourcesdir));
	}

	private void clickLeaveGameButton(ActionEvent e) {
		
	}
}

