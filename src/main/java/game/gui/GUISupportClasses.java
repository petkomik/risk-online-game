package game.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import game.PlayerInLobby;
import game.models.Player;
import general.Parameter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
		
		public Spacing(double i) {
			this();
			this.setMinWidth(i);
			this.setMinHeight(i);
			HBox.setHgrow(this, Priority.ALWAYS);
			VBox.setVgrow(this, Priority.ALWAYS);
		}
	}
	
	static class DiceFactory extends ImageView{
		public DiceFactory() {
			super();
		}
		
		public DiceFactory(int i, boolean at, double ratio) throws FileNotFoundException {
			super();
			String bDice = at ? "" : "b";
			this.setImage(new Image(new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(i) + bDice + ".png")));
			this.setFitWidth(70 * ratio);
			this.setFitHeight(70 * ratio);
			this.setPreserveRatio(true);
			this.setSmooth(true);
			this.setCache(true);
		}
		
		
	}

	static class MenuButton extends Button {
		public MenuButton(String text) {
			super();
			this.setText(text);
			this.getStyleClass().add("menuButton");
			this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 68));
			
		}
	}

	
	static class DesignButton extends Button {
		public DesignButton() {
			super();
			this.setPadding(new Insets(10, 20, 10, 20));
			this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 28));
			this.setTextFill(Color.WHITE);
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
		
		public DesignButton(Insets inst, int radius, double fontSize, double width) {
			super();
			this.setPadding(inst);
			this.setPrefWidth(width);
			this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, fontSize));
			this.setTextFill(Color.WHITE);
			this.setStyle("-fx-background-color: "
					+ "radial-gradient(focus-distance 0% , center 50% 50% , "
					+ "radius 75% , #b87331, #64441f);"
					+ "-fx-background-insets: 1 1 1 1;"
					+ "-fx-background-radius: " + radius + ";"
					+ "-fx-border-radius: " + radius + ";"
        			+ "-fx-border-color: transparent;"
        			+ "-fx-border-width: 4px;");
		
		this.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
            	this.setStyle("-fx-background-color: #64441f;"
            				+ "-fx-background-insets: 1 1 1 1;"
							+ "-fx-background-radius: " + radius + ";"
							+ "-fx-border-radius: " + radius + ";"
	            			+ "-fx-border-color: #ffff;"
	            			+ "-fx-border-width: 4px;");
            } else {
            	this.setStyle("-fx-background-color: "
            				+ "radial-gradient(focus-distance 0% , center 50% 50% , "
            				+ "radius 75% , #b87331, #64441f);"
        					+ "-fx-background-insets: 1 1 1 1;"
							+ "-fx-background-radius: " + radius + ";"
							+ "-fx-border-radius: " + radius + ";"
	            			+ "-fx-border-color: transparent;"
	            			+ "-fx-border-width: 4px;");
            }
	        });
		}
		
		public DesignButton(Insets inst, int radius, double fontSize, double width, boolean chatButton) throws FileNotFoundException {
			this(inst, radius, fontSize, width);
			if(chatButton) {
				this.setText("Chat");
		        ImageView img = new ImageView();
		        img.setImage(new Image(new FileInputStream(Parameter.chatIcon)));
		        img.setFitHeight(fontSize);
		        img.setPreserveRatio(true);
				img.setSmooth(true);
				img.setCache(true);
				this.setGraphicTextGap(10);
		        this.setGraphic(img);				
			}
			
		
		}
	}
	
	
	static class ArrowButton extends Button {
		public ArrowButton() {
			super();
			this.setText("<");
			this.setAlignment(Pos.CENTER_LEFT);
			this.setFont(Font.font("Consolas", FontWeight.BLACK, 60));
			this.setMaxSize(60, 60);
			this.setMinSize(60, 60);
			this.setPrefSize(60, 60);
			this.setAlignment(Pos.CENTER);
			this.setPadding(new Insets(-12, 0, 0, 0));

			this.setTextFill(Color.WHITE);
			this.setStyle("-fx-background-color: #b87331;"
					+ "-fx-background-insets: 1 1 1 1;"
					+ "-fx-background-radius: 12;"
					+ "-fx-border-radius: 12;"
	       			+ "-fx-border-color: #b87331;"
	    			+ "-fx-border-width: 3px;");
			
			this.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
	            if (newValue) {
	            	this.setStyle("-fx-background-color: #b87331;"
	    					+ "-fx-background-insets: 1 1 1 1;"
	        				+ "-fx-background-radius: 12;"
	        				+ "-fx-border-radius: 12;"
	            			+ "-fx-border-color: #ffffff;"
	            			+ "-fx-border-width: 3px;");
	            } else {
	            	this.setStyle("-fx-background-color: #b87331;"
	    					+ "-fx-background-insets: 1 1 1 1;"
	        				+ "-fx-background-radius: 12;"
	        				+ "-fx-border-radius: 12;"
	            			+ "-fx-border-color: #b87331;"
	            			+ "-fx-border-width: 3px;");
	            }
	        });
		}
		
		public ArrowButton(double k) {
			this();
			this.setFont(Font.font("Consolas", FontWeight.BLACK, k));
			this.setMaxSize(k, k);
			this.setMinSize(k, k);
			this.setPrefSize(k, k);
			this.setPadding(new Insets(-k/6, 0, 0, 0));
		}
		
		public ArrowButton(double size, double inset) {
			this(size);
			//inset = Math.min(1, inset + 0.05);
			this.setPadding(new Insets(-inset, 0, 0, 0));
		}
	}
	
	static class ChatButton extends ToggleButton {
		
		public ChatButton() {
			super();
			this.setText("\uD83D\uDDE8");
			this.setAlignment(Pos.CENTER_LEFT);
			this.setFont(Font.font("Segoe UI Emoji", FontWeight.BOLD, 60));
			this.setMaxSize(60, 60);
			this.setMinSize(60, 60);
			this.setPrefSize(60, 60);
			this.setAlignment(Pos.BASELINE_CENTER);
			this.setPadding(new Insets(0, 0, 0, 0));
			this.setSelected(false);

			this.setTextFill(Color.WHITE);
			this.setStyle("-fx-background-color: #b87331;"
					+ "-fx-background-insets: 1 1 1 1;"
					+ "-fx-background-radius: 12;"
					+ "-fx-border-radius: 12;"
	       			+ "-fx-border-color: #b87331;"
	    			+ "-fx-border-width: 3px;");
			
			this.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
	            if (newValue) {
	            	this.setStyle("-fx-background-color: #b87331;"
	    					+ "-fx-background-insets: 1 1 1 1;"
	        				+ "-fx-background-radius: 12;"
	        				+ "-fx-border-radius: 12;"
	            			+ "-fx-border-color: #ffffff;"
	            			+ "-fx-border-width: 3px;");
	            } else {
	            	this.setStyle("-fx-background-color: #b87331;"
	    					+ "-fx-background-insets: 1 1 1 1;"
	        				+ "-fx-background-radius: 12;"
	        				+ "-fx-border-radius: 12;"
	            			+ "-fx-border-color: #b87331;"
	            			+ "-fx-border-width: 3px;");
	            }
	        });
		}
		
		public ChatButton(double k) {
			this();
			this.setFont(Font.font("Segoe UI Emoji", FontWeight.BOLD, k/3));
			this.setMaxSize(k, k);
			this.setMinSize(k, k);
			this.setPrefSize(k, k);
		}

	}
	
	static class PlayerCard extends VBox {
		String name;
		ImageView avatar = new ImageView();
		Color color;		
		Label playerReady;
		double ratio;
		

		public PlayerCard(Player player, String avatar, Color color, double ratio, boolean ready) throws FileNotFoundException {
			super();
			this.name = player.getName();
			this.avatar.setImage(new Image(new FileInputStream(avatar)));
			this.color = color;
			this.ratio = ratio;
			this.playerReady = ready ? new Label("Ready") : new Label("Not Ready");
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
			cardBanner.setPadding(new Insets(10 * ratio, 20 * ratio, 10 * ratio, 20 * ratio));
		
			playerName.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 22 * ratio));
			playerName.setTextFill(Color.WHITE);
			playerName.setAlignment(Pos.CENTER);
			
			cardBanner.getChildren().add(playerName);
			
			Circle circlePl = new Circle(50 * ratio);
			circlePl.setFill(color);
			circlePl.setStroke(color);
			circlePl.setStrokeWidth(6 * ratio);
						
			this.avatar.setFitWidth(100 * ratio);
			this.avatar.setFitHeight(100 * ratio);
			this.avatar.setPreserveRatio(true);
			this.avatar.setSmooth(true);
			this.avatar.setCache(true);
			
			playerImage.getChildren().addAll(circlePl, this.avatar);
			playerImage.setAlignment(Pos.CENTER);
			
			readyBanner.setAlignment(Pos.BOTTOM_CENTER);
			
			playerReady.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 24 * ratio));
			playerReady.setTextFill(Parameter.darkGrey);
			playerReady.setAlignment(Pos.CENTER);
			
			readyBanner.getChildren().add(playerReady);
			
			this.setStyle("-fx-background-color: rgba(225, 225, 225, 0.8);");
			this.minHeightProperty().bind(this.maxHeightProperty());
			this.maxHeightProperty().bind(this.prefHeightProperty());
			this.setPrefHeight(215 * ratio);
			
			this.minWidthProperty().bind(this.maxWidthProperty());
			this.maxWidthProperty().bind(this.prefWidthProperty());
			this.setPrefWidth(200 * ratio);
			
			this.setStyle("-fx-background-color: rgba(225, 225, 225, 0.7);"
					+ "-fx-background-radius: 7;");

			this.setSpacing(10 * ratio);
			this.getChildren().addAll(cardBanner, playerImage, readyBanner);

			
		}
		
		public void setReady(boolean status) {
			this.playerReady.setText(status ? "Ready" : "Not Ready");		
		}
	}
	
	static class ImageViewPane extends Region {
	    
	    private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<ImageView>();
	    
	    public ObjectProperty<ImageView> imageViewProperty() {
	        return imageViewProperty;
	    }
	    
	    public ImageView getImageView() {
	        return imageViewProperty.get();
	    }
	    
	    public void setImageView(ImageView imageView) {
	        this.imageViewProperty.set(imageView);
	    }

	    public ImageViewPane() {
	        this(new ImageView());
	    }

	    @Override
	    protected void layoutChildren() {
	        ImageView imageView = imageViewProperty.get();
	        if (imageView != null) {
	            imageView.setFitWidth(getWidth());
	            imageView.setFitHeight(getHeight());
	            layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
	        }
	        super.layoutChildren();
	    }
	    
	    public ImageViewPane(ImageView imageView) {
	        imageViewProperty.addListener(new ChangeListener<ImageView>() {

	            @Override
	            public void changed(ObservableValue<? extends ImageView> arg0, ImageView oldIV, ImageView newIV) {
	                if (oldIV != null) {
	                    getChildren().remove(oldIV);
	                }
	                if (newIV != null) {
	                    getChildren().add(newIV);
	                }
	            }
	        });
	        this.imageViewProperty.set(imageView);
	    }
	}
}
