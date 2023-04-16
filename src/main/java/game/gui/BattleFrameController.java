package game.gui;

import game.gui.GUISupportClasses.*;
import game.models.Continent;
import game.models.CountryName;
import game.models.Territory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import general.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/*
 * Class for the Battle Frame
 * 
 * @author pmikov
 * 
 */

public class BattleFrameController extends VBox {
	
	// TODO
	// controll the ints
	int maxDiceToThrow;
	int defendingDice;
	int[] dicesAttacker;
	int[] dicesDefender;
	// negative attacker loses n troops, positive defender loses n, 0 both lose 1 {-2, -1, 0, 1, 2}
	int lastThrow;
	
	Territory attacking;
	Territory defending;

	ImageViewPane imgAttackingPane;
	ImageViewPane imgDefendingPane;
	FlowPane armiesFlowDf;
	FlowPane armiesFlowAt;
	
	Label troopsTextAt;
	Label troopsTextDf;

	DesignButton lessBtn;
	DesignButton moreBtn;
	Label numberLabel;
	FlowPane diceImagesAt;
	FlowPane diceImagesDf;
	

	public BattleFrameController() throws Exception {
		super();
		this.attacking = new Territory(CountryName.SouthernEurope, Continent.Europe);
		this.defending = new Territory(CountryName.Ukraine, Continent.Europe);
		// TODO remove
		attacking.addNumberOfTroops(24);
		defending.addNumberOfTroops(20);
		this.maxDiceToThrow = Math.min(3, attacking.getNumberOfTroops() - 1);
		this.defendingDice =  Math.min(2, defending.getNumberOfTroops());
		dicesAttacker = new int[this.maxDiceToThrow];
		dicesDefender = new int[this.defendingDice];
	}
	
	public BattleFrameController(Territory at, Territory df) throws Exception {
		super();
		// TODO set correct max dice
		this.maxDiceToThrow = 3;
		this.attacking = at;
		this.defending = df;
	}


	public void setup() throws Exception {

		/* 
		 * setting up root panel root
		 */
		
		this.setAlignment(Pos.CENTER);
		this.setFillWidth(true);
		this.setStyle("-fx-background-color: rgb(225, 211, 184);");
		
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

		armiesFlowAt = new FlowPane();
		setCorrectTroops(armiesFlowAt, true);
		armiesFlowAt.setAlignment(Pos.CENTER);
		armiesFlowAt.setHgap(30);
		armiesFlowAt.setVgap(30);
		
		
		attackingStack.getChildren().addAll(imgAttackingPane, armiesFlowAt);
		
		ImageView imgDefending = new ImageView();
		imgDefending.setImage(new Image(new FileInputStream(defending.getAddressToPNG())));
		imgDefending.setPreserveRatio(true);
		imgDefending.setSmooth(true);
		imgDefending.setCache(true);
		
		imgDefendingPane = new ImageViewPane(imgDefending);
		HBox.setHgrow(imgDefendingPane, Priority.ALWAYS);
		
		StackPane defendingStack = new StackPane();
		defendingStack.setAlignment(Pos.CENTER);
		HBox.setHgrow(defendingStack, Priority.ALWAYS);

		armiesFlowDf = new FlowPane();
		setCorrectTroops(armiesFlowDf, false);
		armiesFlowDf.setAlignment(Pos.CENTER);
		armiesFlowDf.setHgap(30);
		armiesFlowDf.setVgap(30);
		armiesFlowDf.setRotationAxis(Rotate.Y_AXIS);
		armiesFlowDf.setRotate(180);
	
		defendingStack.getChildren().addAll(imgDefendingPane, armiesFlowDf);

		Spacing spacingImg = new Spacing();
		HBox.setHgrow(spacingImg, Priority.SOMETIMES);

		imgTerritories.getChildren().addAll(attackingStack, spacingImg, defendingStack);
		imgTerritories.setPadding(new Insets(100, 50, 0, 50));
		imgTerritories.setAlignment(Pos.TOP_CENTER);
		
		/*
		 * Add spacingRoot 
		 * Add both imgTerritories, spacingRoot to the root root
		 */
		
		Spacing spacingRoot = new Spacing();
		this.getChildren().addAll(imgTerritories, spacingRoot);
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
		
		troopsTextAt = new Label(String.valueOf(attacking.getNumberOfTroops() - 1));
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
		
		troopsTextDf = new Label(String.valueOf(defending.getNumberOfTroops()));
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
		lessBtn = new DesignButton();
		numberLabel = new Label(String.valueOf(this.maxDiceToThrow));
		moreBtn = new DesignButton();
		
		HBox diceButtonPane = new HBox();
		DesignButton throwBtn = new DesignButton();

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
		diceImagesAt = diceImageFactory(maxDiceToThrow, true);
		diceImagesDf = diceImageFactory(defendingDice, false);
		
		lessBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	(new GameSound()).buttonClickForwardSound();
		    	if (Integer.parseInt(numberLabel.getText()) > 1) {
		    		int i = Integer.parseInt(numberLabel.getText()) - 1;
			    	numberLabel.setText(String.valueOf(i));
			    	dicesAttacker = new int[dicesAttacker.length - 1];
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
			    	dicesAttacker = new int[dicesAttacker.length + 1];
			    	DiceFactory newDice;
					try {
						newDice = new DiceFactory((i*29)%6 + 1, true);
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
		    	
				dicesAttacker = new int[dicesAttacker.length];
				dicesDefender = new int[defendingDice];
				
		    	throwBtn.setDisable(true);
                lessBtn.setDisable(true);
                moreBtn.setDisable(true);
		    		    	
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
                    	diceImagesAt.getChildren().set(m, dice);      
		                dicesAttacker[k] = n;
		    		}
		    		
		    		for (int k = 0; k < diceImagesDf.getChildren().size(); k++) {
		    			Random random = new Random();
		        		int n = random.nextInt(6)+1;
		                DiceFactory dice = (DiceFactory) diceImagesDf.getChildren().get(k);
		                try {
							dice.setImage(new Image(new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(n) + "b.png")));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
		                final int m = k;
                    	diceImagesDf.getChildren().set(m, dice);                 
		                dicesDefender[k] = n;
		    		}
		    		
		    		})
    			);
    			timeline.setCycleCount(12);
    			timeline.play();
    			
    			lastThrow--;
    			
    			timeline.setOnFinished(new EventHandler<ActionEvent>() {
    			      @Override
    			      public void handle(ActionEvent evt) {
    			    	  
    			    	  lastThrow = 0;
    			    	  int maxDf = Arrays.stream(dicesDefender).max().getAsInt();
    			    	  int maxAt = Arrays.stream(dicesAttacker).max().getAsInt();
    			    	  
    			    	  if (maxAt > maxDf) {
    			    		  lastThrow++;  
    			    	  } else {
    			    		  lastThrow--;  
    			    	  }
    			    	  
    			    	  if(dicesDefender.length == 2 && dicesAttacker.length > 1) {
        			    	  int nextDf = Arrays.stream(dicesDefender).min().getAsInt();
        			    	  
        			    	  int minAt = Arrays.stream(dicesAttacker).min().getAsInt();
        			    	  int nextAt = Arrays.stream(dicesAttacker).sum() - minAt - maxAt ;

        			    	  if (nextAt > nextDf) {
        			    		  lastThrow++;  
        			    	  } else {
        			    		  lastThrow--;  
        			    	  }
    			    	  }
    			    	     			    	  
    			    	try {
							updateTroops();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} 

		                throwBtn.setDisable(false);
		                lessBtn.setDisable(false);
		                moreBtn.setDisable(false);
		                    			      }
    			    });            
            }
		       
	    });
	    	   
		
		diceSection.setSpacing(50);
		diceSection.getChildren().addAll(diceImagesAt, diceControls, diceImagesDf);
		diceSection.setAlignment(Pos.CENTER);
		
		/*
		 * Setting up spacing between DiceImages and DieceControls
		 * Packing children into parent containers
		 */
		
		Spacing spacingControls1 = new Spacing();
		Spacing spacingControls2 = new Spacing();

		diceAndProfile.getChildren().addAll(playerAt, spacingControls1, diceSection, spacingControls2, playerDf);
		diceAndProfile.setPadding(new Insets(0, 120, 50, 120));
		diceAndProfile.setAlignment(Pos.BOTTOM_CENTER);
		HBox.setHgrow(spacingControls1, Priority.ALWAYS);
		HBox.setHgrow(spacingControls2, Priority.ALWAYS);

		this.getChildren().add(diceAndProfile);		
	}
	
	public FlowPane diceImageFactory (int k, boolean at) throws FileNotFoundException {
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
		DiceFactory[] dices = new DiceFactory[k];
		for(int i = 0; i < dices.length; i++) {
			dices[i] = new DiceFactory((i*29)%6 + 1, at);
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
		int leftOverSoldier = attacking ? 1 : 0;
			
		if(attacking) {
			territ = this.attacking;
		} else {
			territ = this.defending;
		}
		
		numberTroops = Math.min(40 + (territ.getNumberOfTroops() - leftOverSoldier) % 10, territ.getNumberOfTroops() - leftOverSoldier);

		while (numberTroops > 0) {
			if (numberTroops >= 10) {
				numberTroops -= 10;
				art++;
			} else if (numberTroops >= 5) {
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
		
		flow.getChildren().removeAll(flow.getChildren());
				
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
		
		if(flow.getChildren().size() > 8 || art > 2) {
			flow.maxWidth(1200);
		} else if (art == 2) {
			flow.setMaxWidth(700);
		} else if (art == 1 && cav == 1) {
			flow.setMaxWidth(600);
		} else {
			flow.setMaxWidth(400);
		}	
	}	
	
	public void updateTroops() throws FileNotFoundException {
		
		if(this.lastThrow < 0) {
			this.attacking.removeNumberOfTroops(-1 * this.lastThrow);
		} else if (this.lastThrow > 0) {
			this.defending.removeNumberOfTroops(this.lastThrow);
		} else {
			this.attacking.removeNumberOfTroops(1);
			this.defending.removeNumberOfTroops(1);

		}
		
		this.setCorrectTroops(armiesFlowAt, true);
		this.setCorrectTroops(armiesFlowDf, false);
		
		troopsTextAt.setText(String.valueOf(attacking.getNumberOfTroops() - 1));
		troopsTextDf.setText(String.valueOf(defending.getNumberOfTroops()));
		
		if(this.defending.getNumberOfTroops() == 1 && defendingDice != 1) {
			this.defendingDice = 1;
			diceImagesDf.getChildren().remove(1); 
		}
		
		
		if(this.attacking.getNumberOfTroops() == 2) {
			this.maxDiceToThrow = 1;
			if (Integer.parseInt(numberLabel.getText()) > 1) {
	    		int i = Integer.parseInt(numberLabel.getText()) - 1;
		    	numberLabel.setText("1");
		    	dicesAttacker = new int[1];
		    	
		    	if(diceImagesAt.getChildren().size() == 3) {
		    		diceImagesAt.getChildren().remove(1);
		    		diceImagesAt.getChildren().remove(1);
		    	} else {
		    		diceImagesAt.getChildren().remove(1);
		    	}
			
			} 

		} else if (this.attacking.getNumberOfTroops() == 3) {
			this.maxDiceToThrow = 2;
			if (Integer.parseInt(numberLabel.getText()) > 2) {
	    		int i = Integer.parseInt(numberLabel.getText()) - 1;
		    	numberLabel.setText(String.valueOf(i));
		    	dicesAttacker = new int[dicesAttacker.length - 1];
		    	diceImagesAt.getChildren().remove(0);
		    	
	    	}
		}
		
		if(this.defending.getNumberOfTroops() == 0) {
			// TODO stop decide win
		} else if(this.attacking.getNumberOfTroops() == 0) {
			// TODO stop decide win

		}

	}
}
