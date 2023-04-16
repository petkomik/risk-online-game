package game.gui;

import game.gui.GUISupportClasses.DiceFactory;
import game.models.Continent;
import game.models.CountryName;
import game.models.Territory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import general.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/*
 * Class for the Battle Frame
 * 
 * @author pmikov
 * 
 */

public class BattleFrameController {
	
	// TODO
	// controll the ints
	int maxDiceToThrow;
	public int chosenNumberOfDice;
	Territory attacking;
	Territory defending;
	VBox root;
	int[] dicesAttacker = new int[3];
	int[] dicesDefender = new int[2];


	public BattleFrameController() throws Exception {
		this.maxDiceToThrow = 3;
		this.chosenNumberOfDice = maxDiceToThrow;
		this.attacking = new Territory(CountryName.SouthernEurope, Continent.Europe);
		this.defending = new Territory(CountryName.Ukraine, Continent.Europe);
		// TODO remove
		attacking.addNumberOfTroops(48);
		defending.addNumberOfTroops(27);
		this.root = this.setup();
	}
	
	public BattleFrameController(Territory at, Territory df) throws Exception {
		// TODO set correct max dice
		this.maxDiceToThrow = 3;
		this.chosenNumberOfDice = maxDiceToThrow;
		this.attacking = at;
		this.defending = df;
		this.root = this.setup();
	}


	public VBox setup() throws Exception {

		/* 
		 * setting up root panel vBox
		 */
		
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setFillWidth(true);
		vBox.setStyle("-fx-background-color: rgb(225, 211, 184);");
		
		/*
		 * Setting up imTerritories pane
		 * Includes imgAttacking, spacingImg, imgDefending
		 * ImageView are wrapped in ImageViewPane which delivers responsiveness
		 */
		
		HBox imgTerritories = new HBox();
		
		ImageView imgAttacking = new ImageView();
		imgAttacking.setImage(new Image(new FileInputStream(attacking.getAddressToPNG())));
		imgAttacking.setPreserveRatio(true);
		imgAttacking.setSmooth(true);
		imgAttacking.setCache(true);
		
		ImageViewPane imgAttackingPane = new ImageViewPane(imgAttacking);
		HBox.setHgrow(imgAttackingPane, Priority.ALWAYS);
		
		StackPane attackingStack = new StackPane();
		attackingStack.setAlignment(Pos.CENTER);
		HBox.setHgrow(attackingStack, Priority.ALWAYS);

		FlowPane armiesFlowAt = new FlowPane();
		armiesFlowAt.minHeightProperty().bind(imgAttackingPane.minHeightProperty());
		armiesFlowAt.maxHeightProperty().bind(imgAttackingPane.maxHeightProperty());
		armiesFlowAt.prefHeightProperty().bind(imgAttackingPane.prefHeightProperty());
		
		armiesFlowAt.minWidthProperty().bind(imgAttackingPane.minWidthProperty());
		armiesFlowAt.maxWidthProperty().bind(imgAttackingPane.maxWidthProperty());
		armiesFlowAt.prefWidthProperty().bind(imgAttackingPane.prefWidthProperty());
		
		setCorrectTroops(armiesFlowAt, true);
		armiesFlowAt.setAlignment(Pos.CENTER);
		armiesFlowAt.setHgap(20);
		armiesFlowAt.setVgap(30);

		attackingStack.getChildren().addAll(imgAttackingPane, armiesFlowAt);
		
		ImageView imgDefending = new ImageView();
		imgDefending.setImage(new Image(new FileInputStream(defending.getAddressToPNG())));
		imgDefending.setPreserveRatio(true);
		imgDefending.setSmooth(true);
		imgDefending.setCache(true);
		
		ImageViewPane imgDefendingPane = new ImageViewPane(imgDefending);
		HBox.setHgrow(imgDefendingPane, Priority.ALWAYS);
		
		StackPane defendingStack = new StackPane();
		defendingStack.setAlignment(Pos.CENTER);
		HBox.setHgrow(defendingStack, Priority.ALWAYS);

		FlowPane armiesFlowDf = new FlowPane();
		armiesFlowDf.minHeightProperty().bind(imgDefendingPane.minHeightProperty());
		armiesFlowDf.maxHeightProperty().bind(imgDefendingPane.maxHeightProperty());
		armiesFlowDf.prefHeightProperty().bind(imgDefendingPane.prefHeightProperty());
		
		armiesFlowDf.minWidthProperty().bind(imgDefendingPane.minWidthProperty());
		armiesFlowDf.maxWidthProperty().bind(imgDefendingPane.maxWidthProperty());
		armiesFlowDf.prefWidthProperty().bind(imgDefendingPane.prefWidthProperty());
		
		setCorrectTroops(armiesFlowDf, false);
		armiesFlowDf.setAlignment(Pos.CENTER);
		armiesFlowDf.setHgap(20);
		armiesFlowDf.setVgap(30);
		
		defendingStack.getChildren().addAll(imgDefendingPane, armiesFlowDf);

		GUISupportClasses.Spacing spacingImg = new GUISupportClasses.Spacing();
		HBox.setHgrow(spacingImg, Priority.SOMETIMES);

		imgTerritories.getChildren().addAll(attackingStack, spacingImg, defendingStack);
		imgTerritories.setPadding(new Insets(100, 50, 0, 50));
		imgTerritories.setAlignment(Pos.TOP_CENTER);
		
		/*
		 * Add spacingRoot 
		 * Add both imgTerritories, spacingRoot to the root vBox
		 */
		
		GUISupportClasses.Spacing spacingRoot = new GUISupportClasses.Spacing();
		vBox.getChildren().addAll(imgTerritories, spacingRoot);
		VBox.setVgrow(spacingRoot, Priority.SOMETIMES);
		VBox.setVgrow(imgTerritories, Priority.ALWAYS);
		
		/*
		 * Setting up bottom portion of window
		 * Includes playerAt, playerDf - players avatar, color, number of troops
		 * Includes diceSection - dice controls, dice images 
		 */
		
		HBox diceAndProfile = new HBox();
		
		StackPane playerAt = new StackPane();
		StackPane playerDf = new StackPane();
		HBox diceSection = new HBox();
				
		/*
		 * Setting up playerAt
		 * Includes Circle with player color (circleAt)
		 * 			Player avatar (avatarAt)
		 * 			Top circle with number of troops left in attack - circleTroopsAt, troopsTextAt, stackTroopsAt
		 */
		
		Circle circleAt = new Circle(80);
		// TODO change to correct collor
		circleAt.setFill(Parameter.blueColor);
		circleAt.setStroke(Color.WHITE);
		circleAt.setStrokeWidth(6);
		
		playerAt.getChildren().add(circleAt);
		
		ImageView avatarAt = new ImageView();
		// TODO change to correct avatar
		avatarAt.setImage(new Image(new FileInputStream(Parameter.avatarsdir + "blonde-boy.png")));
		avatarAt.setFitWidth(140);
		avatarAt.setFitHeight(140);
		avatarAt.setPreserveRatio(true);
		avatarAt.setSmooth(true);
		avatarAt.setCache(true);
		
		playerAt.getChildren().add(avatarAt);
		
		Circle circleTroopsAt = new Circle(40);
		circleTroopsAt.setFill(Color.WHITE);
		circleTroopsAt.setStroke(Color.WHITE);
		circleTroopsAt.setStrokeWidth(0);
		
		Label troopsTextAt = new Label(String.valueOf(attacking.getNumberOfTroops()));
		troopsTextAt.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 34));
		troopsTextAt.setTextFill(Color.web("#303030"));
		troopsTextAt.setMinWidth(80);
		troopsTextAt.setMinHeight(80);
		troopsTextAt.setAlignment(Pos.CENTER);
		
		StackPane stackTroopsAt = new StackPane();
		stackTroopsAt.getChildren().addAll(circleTroopsAt, troopsTextAt);
		stackTroopsAt.setAlignment(Pos.TOP_RIGHT);
		
		playerAt.getChildren().add(stackTroopsAt);
		
		/*
		 * Setting up playerDf
		 * Includes Circle with player color (circleDf)
		 * 			Player avatar (avatarDf)
		 * 			Top circle with number of troops left in attack - circleTroopsDf, troopsTextDf, stackTroopsDf
		 */
		
		Circle circleDf = new Circle(80);
		// TODO change to correct collor
		circleDf.setFill(Parameter.greenColor);
		circleDf.setStroke(Color.WHITE);
		circleDf.setStrokeWidth(6);

		playerDf.getChildren().add(circleDf);
		
		ImageView avatarDf = new ImageView();
		// TODO change to correct avatar

		avatarDf.setImage(new Image(new FileInputStream(Parameter.avatarsdir + "ginger-girl.png")));
		avatarDf.setFitWidth(140);
		avatarDf.setFitHeight(140);
		avatarDf.setPreserveRatio(true);
		avatarDf.setSmooth(true);
		avatarDf.setCache(true);
		
		playerDf.getChildren().add(avatarDf);
		
		Circle circleTroopsDf = new Circle(40);
		circleTroopsDf.setFill(Color.WHITE);
		circleTroopsDf.setStroke(Color.WHITE);
		circleTroopsDf.setStrokeWidth(0);
		
		Label troopsTextDf = new Label(String.valueOf(defending.getNumberOfTroops()));
		troopsTextDf.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 34));
		troopsTextDf.setTextFill(Color.web("#303030"));
		troopsTextDf.setMinWidth(80);
		troopsTextDf.setMinHeight(80);
		troopsTextDf.setAlignment(Pos.CENTER);
		
		StackPane stackTroopsDf = new StackPane();
		stackTroopsDf.getChildren().addAll(circleTroopsDf, troopsTextDf);
		stackTroopsDf.setAlignment(Pos.TOP_LEFT);
		
		playerDf.getChildren().add(stackTroopsDf);
		
		/*
		 * Setting up numberOfdiceControls
		 * Includes More, Less Dice Buttons
		 * 		 	Dice numberLabel
		 * 			Throw Dice Button
		 * 			Event Handlers for all Buttons
		 */
		
		VBox diceControls = new VBox();

		HBox numberOfDiceControls = new HBox();
		GUISupportClasses.DesignButton lessBtn = new GUISupportClasses.DesignButton();
		Label numberLabel = new Label("3");
		GUISupportClasses.DesignButton moreBtn = new GUISupportClasses.DesignButton();
		
		HBox diceButtonPane = new HBox();
		GUISupportClasses.DesignButton throwBtn = new GUISupportClasses.DesignButton();

		lessBtn.setText("<");
		moreBtn.setText(">");
		numberLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 34));
		numberLabel.setTextFill(Color.web("#b87331"));
		numberLabel.textOverrunProperty().set(OverrunStyle.CLIP);
		numberLabel.setMinWidth(50);
		numberLabel.setAlignment(Pos.CENTER);
		
		numberOfDiceControls.setSpacing(25);
		numberOfDiceControls.getChildren().addAll(lessBtn, numberLabel, moreBtn);
		numberOfDiceControls.setAlignment(Pos.CENTER);
		
		throwBtn.setText("Throw Dice");
		throwBtn.setPadding(new Insets(10, 40, 10, 40));
		throwBtn.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 24));
		diceButtonPane.getChildren().add(throwBtn);
		diceButtonPane.setAlignment(Pos.CENTER);
		diceButtonPane.setMinWidth(250);
		
		
		diceControls.setSpacing(20);
		diceControls.getChildren().addAll(numberOfDiceControls, diceButtonPane);
		diceControls.setAlignment(Pos.CENTER);
		diceControls.setMinWidth(250);
		
		/*
		 * Setting up Images of Dices	
		 * 			  both Attacker + Defender
		 */

		// TODO set correct number of dice
		FlowPane diceImagesAt = diceImageFactory(maxDiceToThrow);
		FlowPane diceImagesDf = diceImageFactory(2);
		
		lessBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	if (Integer.parseInt(numberLabel.getText()) > 1) {
		    		int i = Integer.parseInt(numberLabel.getText()) - 1;
			    	numberLabel.setText(String.valueOf(i));
			    	chosenNumberOfDice = i;
			    	diceImagesAt.getChildren().remove(0);
			    	
		    	}

		    }
		});
		
		moreBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	if (Integer.parseInt(numberLabel.getText()) < maxDiceToThrow) {
		    		int i = Integer.parseInt(numberLabel.getText()) + 1;
			    	numberLabel.setText(String.valueOf(i));
			    	chosenNumberOfDice = i;
			    	GUISupportClasses.DiceFactory newDice;
					try {
						newDice = new GUISupportClasses.DiceFactory((i*29)%6 + 1);
				    	diceImagesAt.getChildren().add(newDice);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					}
		    	}
		});

		throwBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	
		    	throwBtn.setDisable(true);
		    	
		    	Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80.0), e -> {
		    		
		    		for (int k = 0; k < diceImagesAt.getChildren().size(); k++) {
		    			Random random = new Random();
		        		int n = random.nextInt(6)+1;
		                DiceFactory dice = (DiceFactory) diceImagesAt.getChildren().get(k);
		                try {
							dice.setImage(new Image(new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(n) + ".png")));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
		                final int m = k;
		                Platform.runLater(new Runnable() {
		                    @Override public void run() {
		                    	diceImagesAt.getChildren().set(m, dice);                 
		                    	}
		                });  
		                dicesAttacker[k] = n;
		    		}
		    		
		    		for (int k = 0; k < diceImagesDf.getChildren().size(); k++) {
		    			Random random = new Random();
		        		int n = random.nextInt(6)+1;
		                DiceFactory dice = (DiceFactory) diceImagesDf.getChildren().get(k);
		                try {
							dice.setImage(new Image(new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(n) + ".png")));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
		                final int m = k;
		                Platform.runLater(new Runnable() {
		                    @Override public void run() {
		                    	diceImagesDf.getChildren().set(m, dice);                 
		                    	}
		                }); 
		                dicesDefender[k] = n;
		    		}
		    		
		    		})
    			);
    			timeline.setCycleCount(12);
    			timeline.play();
                throwBtn.setDisable(false);
                
                
                
            }
		       
	    });
	    	   
		
		diceSection.setSpacing(50);
		diceSection.getChildren().addAll(diceImagesAt, diceControls, diceImagesDf);
		diceSection.setAlignment(Pos.CENTER);
		

		/*
		 * Setting up spacing between DiceImages and DieceControls
		 * Packing children into parent containers
		 */
		
		GUISupportClasses.Spacing spacingControls1 = new GUISupportClasses.Spacing();
		GUISupportClasses.Spacing spacingControls2 = new GUISupportClasses.Spacing();

		diceAndProfile.getChildren().addAll(playerAt, spacingControls1, diceSection, spacingControls2, playerDf);
		diceAndProfile.setPadding(new Insets(0, 120, 50, 120));
		diceAndProfile.setAlignment(Pos.BOTTOM_CENTER);
		HBox.setHgrow(spacingControls1, Priority.ALWAYS);
		HBox.setHgrow(spacingControls2, Priority.ALWAYS);

		vBox.getChildren().add(diceAndProfile);
		return vBox;
		
	}
	
	public FlowPane diceImageFactory (int k) throws FileNotFoundException {
		FlowPane diceImages = new FlowPane();
		
		diceImages.minHeightProperty().bind(diceImages.maxHeightProperty());
		diceImages.maxHeightProperty().bind(diceImages.prefHeightProperty());
		diceImages.setPrefHeight(200);
		
		diceImages.minWidthProperty().bind(diceImages.maxWidthProperty());
		diceImages.maxWidthProperty().bind(diceImages.prefWidthProperty());
		diceImages.setPrefWidth(200);
		
		diceImages.setHgap(20);
		diceImages.setVgap(20);
		
		// TODO
		GUISupportClasses.DiceFactory[] dices = new GUISupportClasses.DiceFactory[k];
		for(int i = 0; i < dices.length; i++) {
			dices[i] = new GUISupportClasses.DiceFactory((i*29)%6 + 1);
			diceImages.getChildren().add(dices[i]);
		}
	
		diceImages.setAlignment(Pos.CENTER);
		
		return diceImages;
	}
	
	public void setCorrectTroops(FlowPane flow, boolean attacking) throws FileNotFoundException {
		
		int inf = 0;
		int cav = 0;
		int art = 0;
		Territory territ;
		int numberTroops;
	
		if(attacking) {
			territ = this.attacking;
		} else {
			territ = this.defending;
		}
		
		numberTroops = territ.getNumberOfTroops();
		
		while (numberTroops > 0) {
			if (numberTroops > 10) {
				numberTroops -= 10;
				art++;
			} else if (numberTroops > 4) {
				numberTroops -= 5;
				cav++;
			} else {
				numberTroops --;
				inf++;
			}
		}
		
		ImageView[] artAr = new ImageView[art];
		ImageView[] cavAr = new ImageView[cav];
		ImageView[] infAr = new ImageView[inf];
				
		for(ImageView iv : artAr) {
			iv = new ImageView();
			iv.setImage(new Image(new FileInputStream(Parameter.artillery)));
			iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			iv.setFitHeight(220);
			flow.getChildren().add(iv);		
		}
		
		for(ImageView iv : cavAr) {
			iv = new ImageView();
			iv.setImage(new Image(new FileInputStream(Parameter.cavalry)));
			iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			iv.setFitHeight(200);
			flow.getChildren().add(iv);		
		}
		
		for(ImageView iv : infAr) {
			iv = new ImageView();
			iv.setImage(new Image(new FileInputStream(Parameter.infantry)));
			iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			iv.setFitHeight(150);
			flow.getChildren().add(iv);		
			
		}
	}	
}
