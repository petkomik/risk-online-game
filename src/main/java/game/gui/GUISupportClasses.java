package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import general.Parameter;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
						+ "-fx-background-radius: 15;");
			
			this.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
	            if (newValue) {
	            	this.setStyle("-fx-background-color: #64441f;"
								+ "-fx-background-radius: 15;");
	            } else {
	            	this.setStyle("-fx-background-color: #b87331;"
								+ "-fx-background-radius: 15;");
	            }
	        });
		}
	}

}