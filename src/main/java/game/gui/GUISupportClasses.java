package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import general.Parameter;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GUISupportClasses {
	static class Spacing extends Region {
		public Spacing() {
			super();
			this.setMinWidth(50);
			this.setMinHeight(50);
			this.setMaxWidth(Double.MAX_VALUE);
			this.setMaxHeight(Double.MAX_VALUE);
		}
		
		public Spacing(int i) {
			this();
			this.setMinWidth(i);
			this.setMinHeight(i);
	
		}
	}
	
	static class DiceFactory extends ImageView{
		public DiceFactory() {
			super();
		}
		
		public DiceFactory(int i) throws FileNotFoundException {
			super();
			this.setImage(new Image(new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(i) + ".png")));
			this.setFitWidth(70);
			this.setFitHeight(70);
			this.setPreserveRatio(true);
			this.setSmooth(true);
			this.setCache(true);
		}
	}

	static class DesignButton extends Button {
		public DesignButton() {
			super();
			this.setPadding(new Insets(10, 20, 10, 20));
			this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 28));
			this.setTextFill(Parameter.white);
			this.setStyle("-fx-background-color: #b87331;"
						+ "-fx-background-radius: 15;"
						+ "-fx-border-radius: 12;"
            			+ "-fx-border-color: #b87331;"
            			+ "-fx-border-width: 3px;");
			
			this.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
	            if (newValue) {
	            	this.setStyle("-fx-background-color: #64441f;"
								+ "-fx-background-radius: 15;"
								+ "-fx-border-radius: 12;"
		            			+ "-fx-border-color: #ffff;"
		            			+ "-fx-border-width: 3px;");
	            } else {
	            	this.setStyle("-fx-background-color: #b87331;"
								+ "-fx-background-radius: 15;"
								+ "-fx-border-radius: 12;"
		            			+ "-fx-border-color: #b87331;"
		            			+ "-fx-border-width: 3px;");
	            }
	        });
		}
	}
	
	static class PlayerCard extends VBox {
		String name;
		ImageView avatar = new ImageView();
		Color color;
		String[] aiNames = {"Jasper", "Andrea", "Mick", "Tosho", "Maria"};
		
		
		Label playerReady = new Label("Not Ready");
		
		public PlayerCard() {
			super();
		}
		
		public PlayerCard(int i, String avatar, Color color) throws FileNotFoundException {
			super();
			this.name = this.aiNames[i % aiNames.length] + "(AI)";
			this.avatar.setImage(new Image(new FileInputStream(Parameter.avatarsdir + avatar)));
			this.color = color;
			buildCard();

		}
		
		public PlayerCard(String name, String avatar, Color color) throws FileNotFoundException {
			super();
			this.name = name;
			this.avatar.setImage(new Image(new FileInputStream(Parameter.avatarsdir + avatar)));
			this.color = color;
			buildCard();
			
			
		}
		
		private void buildCard() {
			
			HBox cardBanner = new HBox();
			Label playerName = new Label(name.toUpperCase());
			StackPane playerImage = new StackPane();
			HBox readyBanner = new HBox();
			this.setFillWidth(true);

			String hex =  String.format( "#%02X%02X%02X",
		            (int)( color.getRed() * 255 ),
		            (int)( color.getGreen() * 255 ),
		            (int)( color.getBlue() * 255 ) );
			
			cardBanner.setStyle("-fx-background-color: " + hex + ";"
					+ "-fx-background-radius: 7 7 0 0");
			cardBanner.setAlignment(Pos.TOP_CENTER);
			cardBanner.setPadding(new Insets(10, 20, 10, 20));
		
			playerName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 28));
			playerName.setTextFill(Color.WHITE);
			playerName.setAlignment(Pos.CENTER);
			
			cardBanner.getChildren().add(playerName);
			
			Circle circlePl = new Circle(60);
			circlePl.setFill(color);
			circlePl.setStroke(color);
			circlePl.setStrokeWidth(6);
						
			this.avatar.setFitWidth(120);
			this.avatar.setFitHeight(140);
			this.avatar.setPreserveRatio(true);
			this.avatar.setSmooth(true);
			this.avatar.setCache(true);
			
			playerImage.getChildren().addAll(circlePl, this.avatar);
			playerImage.setAlignment(Pos.CENTER);
			
			readyBanner.setAlignment(Pos.BOTTOM_CENTER);
			
			playerReady.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 24));
			playerReady.setTextFill(Parameter.darkGrey);
			playerReady.setAlignment(Pos.CENTER);
			
			readyBanner.getChildren().add(playerReady);
			
			this.setStyle("-fx-background-color: rgba(225, 225, 225, 0.8);");
			this.minHeightProperty().bind(this.maxHeightProperty());
			this.maxHeightProperty().bind(this.prefHeightProperty());
			this.setPrefHeight(250);
			
			this.minWidthProperty().bind(this.maxWidthProperty());
			this.maxWidthProperty().bind(this.prefWidthProperty());
			this.setPrefWidth(200);
			
			this.setStyle("-fx-background-color: rgba(225, 225, 225, 0.7);"
					+ "-fx-background-radius: 7;");
			
			this.setSpacing(15);
			this.getChildren().addAll(cardBanner, playerImage, readyBanner);

			
		}
		
		public void setReady(boolean status) {
			this.playerReady.setText(status ? "Ready" : "Not Ready");		}
	}
}



