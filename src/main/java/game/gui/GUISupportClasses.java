package game.gui;

import database.Profile;
import game.models.Player;
import general.AppController;
import general.Parameter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import network.Client;
import network.messages.MessageSend;
import network.messages.MessageSendInGame;
import network.messages.MessageToPerson;

/**
 * This class consist of static inner classes defines GUI elements presentin multiple places.
 *
 * @author pmikov, pmalamov, srogalsk
 *
 */

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

    public Spacing(double width, double heigth) {
      this();
      this.setMinWidth(width);
      this.setMinHeight(heigth);
      HBox.setHgrow(this, Priority.ALWAYS);
      VBox.setVgrow(this, Priority.ALWAYS);
    }

  }

  static class DiceFactory extends ImageView {
    public DiceFactory() {
      super();
    }

    public DiceFactory(int i, boolean at, double ratio) throws FileNotFoundException {
      super();
      String bDice = at ? "" : "b";
      this.setImage(new Image(
          new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(i) + bDice + ".png")));
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

  public static class DesignButton extends Button {
    public DesignButton() {
      super();
      this.setPadding(new Insets(10, 20, 10, 20));
      this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 28));
      this.setTextFill(Color.WHITE);
      this.setStyle("-fx-background-color: #b87331;" + "-fx-background-radius: 15;"
          + "-fx-background-insets: 1 1 1 1;" + "-fx-border-radius: 12;"
          + "-fx-border-color: #b87331;" + "-fx-border-width: 3px;");

      this.hoverProperty()
          .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
              this.setStyle("-fx-background-color: #64441f;" + "-fx-background-radius: 15;"
                  + "-fx-background-insets: 1 1 1 1;" + "-fx-border-radius: 12;"
                  + "-fx-border-color: #ffff;" + "-fx-border-width: 3px;");
            } else {
              this.setStyle("-fx-background-color: #b87331;" + "-fx-background-radius: 15;"
                  + "-fx-background-insets: 1 1 1 1;" + "-fx-border-radius: 12;"
                  + "-fx-border-color: #b87331;" + "-fx-border-width: 3px;");
            }
          });
    }

    public DesignButton(Insets inst, int radius, double fontSize, double width) {
      super();
      this.setPadding(inst);
      this.setPrefWidth(width);
      this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, fontSize));
      this.setTextFill(Color.WHITE);
      this.setStyle(
          "-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
              + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
              + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius + ";"
              + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");

      this.hoverProperty()
          .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {

              this.setStyle("-fx-background-color: #64441f;" + "-fx-background-insets: 1 1 1 1;"
                  + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius + ";"
                  + "-fx-border-color: #ffff;" + "-fx-border-width: 4px;");
            } else {
              this.setStyle(
                  "-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
                      + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
                      + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius
                      + ";" + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");
            }
          });

    }

    public DesignButton(Insets inst, int radius, double fontSize, double width, boolean chatButton)
        throws FileNotFoundException {
      this(inst, radius, fontSize, width);
      if (chatButton) {
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
    public ArrowButton(double k) throws FileNotFoundException {
      super();
      this.setMaxSize(k, k);
      this.setMinSize(k, k);
      this.setPrefSize(k, k);

      ImageView img = new ImageView();
      img.setImage(new Image(new FileInputStream(Parameter.buttonArrow)));
      img.setFitHeight(k / 1.7);
      img.setPreserveRatio(true);
      img.setSmooth(true);
      img.setCache(true);
      this.setGraphic(img);
      this.setAlignment(Pos.CENTER);
      this.setTextFill(Color.WHITE);
      this.setStyle("-fx-background-color: #b87331;" + "-fx-background-insets: 1 1 1 1;"
          + "-fx-background-radius: 12;" + "-fx-border-radius: 12;" + "-fx-border-color: #b87331;"
          + "-fx-border-width: " + k / 12 + "px;");

      this.hoverProperty()
          .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
              this.setStyle("-fx-background-color: #b87331;" + "-fx-background-insets: 1 1 1 1;"
                  + "-fx-background-radius: 12;" + "-fx-border-radius: 12;"
                  + "-fx-border-color: #ffffff;" + "-fx-border-width: " + k / 12 + "px;");
            } else {
              this.setStyle("-fx-background-color: #b87331;" + "-fx-background-insets: 1 1 1 1;"
                  + "-fx-background-radius: 12;" + "-fx-border-radius: 12;"
                  + "-fx-border-color: #b87331;" + "-fx-border-width: " + k / 12 + "px;");
            }
          });

    }
  }

  static class ChatButton extends ToggleButton {

    public ChatButton() {
      super();
      this.setPadding(new Insets(10, 20, 10, 20));
      this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 28));
      this.setTextFill(Color.WHITE);
      this.setStyle("-fx-background-color: #b87331;" + "-fx-background-radius: 15;"
          + "-fx-border-radius: 12;" + "-fx-border-color: #b87331;" + "-fx-border-width: 3px;");
      this.setSelected(false);

      this.hoverProperty()
          .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
              this.setStyle("-fx-background-color: #64441f;" + "-fx-background-radius: 15;"
                  + "-fx-border-radius: 12;" + "-fx-border-color: #ffff;"
                  + "-fx-border-width: 3px;");
            } else {
              this.setStyle("-fx-background-color: #b87331;" + "-fx-background-radius: 15;"
                  + "-fx-border-radius: 12;" + "-fx-border-color: #b87331;"
                  + "-fx-border-width: 3px;");
            }
          });
    }

    public ChatButton(Insets inst, int radius, double fontSize, double width) {
      super();
      this.setPadding(inst);
      this.setPrefWidth(width);
      this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, fontSize));
      this.setTextFill(Color.WHITE);
      this.setStyle(
          "-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
              + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
              + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius + ";"
              + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");

      this.selectedProperty()
          .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {

              this.setStyle("-fx-background-color: #64441f;" + "-fx-background-insets: 1 1 1 1;"
                  + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius + ";"
                  + "-fx-border-color: #ffff;" + "-fx-border-width: 4px;");
            } else {
              this.setStyle(
                  "-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
                      + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
                      + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius
                      + ";" + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");
            }
          });
    }

    public ChatButton(Insets inst, int radius, double fontSize, double width, boolean chatButton)
        throws FileNotFoundException {
      this(inst, radius, fontSize, width);
      if (chatButton) {
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

  static class PlayerCard extends VBox {
    String name;
    ImageView avatar = new ImageView();
    Color color;
    Label playerReady;
    double ratio;

    public PlayerCard(Player player, String avatar, Color color, double ratio, boolean ready)
        throws FileNotFoundException {
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

      String hex = String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
          (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));

      cardBanner.setStyle("-fx-background-color: " + hex + ";" + "-fx-background-radius: 7 7 0 0");
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

      this.setStyle(
          "-fx-background-color: rgba(225, 225, 225, 0.7);" + "-fx-background-radius: 7;");

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
        public void changed(ObservableValue<? extends ImageView> arg0, ImageView oldIV,
            ImageView newIV) {
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

  public static class ChatWindow extends VBox {

    private ScrollPane chat;
    private VBox vBoxMessages;
    private HBox textfieldAndButtons;
    private TextField textfieldMessage;
    private VBox comboAndSend;
    private DesignButton sendButton;
    private ComboBox<String> names;
    private HBox dragArea;
    private String messageToBeSend;
    // message to be send, should used by client
    private Client client;

    private boolean dragAreaHover = false;
    private double ratio;
    private double xCord;
    private double yCord;
    private ObservableList<String> items;

    public ChatWindow() {

      ratio = Screen.getPrimary().getVisualBounds().getWidth()
          * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
      ratio = Math.min(ratio + 0.3, 1);
      setup();
      actionEventsSetup();

    }

    public ChatWindow(ChatWindow chatPane) {
      this.chat = chatPane.chat;
      this.vBoxMessages = chatPane.vBoxMessages;
      this.textfieldAndButtons = chatPane.textfieldAndButtons;
      this.textfieldMessage = chatPane.textfieldMessage;
      this.comboAndSend = chatPane.comboAndSend;
      this.sendButton = chatPane.sendButton;
      this.names = chatPane.names;
      this.dragArea = chatPane.dragArea;
      this.messageToBeSend = chatPane.messageToBeSend;

      this.client = chatPane.client;

      this.dragAreaHover = chatPane.dragAreaHover;
      this.ratio = chatPane.ratio;
      this.xCord = chatPane.xCord;
      this.yCord = chatPane.yCord;
      this.items = chatPane.items;
    }

    public void setup() {

      chat = new ScrollPane();
      vBoxMessages = new VBox();
      textfieldAndButtons = new HBox();
      textfieldMessage = new TextField();
      comboAndSend = new VBox();
      names = new ComboBox<String>();
      dragArea = new HBox();
      items = names.getItems();

      dragArea.setStyle("-fx-background-color: rgba(92,64,51); -fx-background-radius: 10 10 0 0;");
      dragArea.minHeightProperty().bind(dragArea.maxHeightProperty());
      dragArea.maxHeightProperty().bind(dragArea.prefHeightProperty());
      dragArea.setPrefHeight(30 * ratio);
      dragArea.getChildren().add(new Spacing(1));
      HBox.setHgrow(dragArea, Priority.ALWAYS);

      /*
       * setting up the scrollpane that contains the chat visuals
       */
      chat.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
      chat.setMaxWidth(ratio * 600);
      chat.setMinWidth(ratio * 600);
      chat.setMaxHeight(ratio * 400);
      chat.setMinHeight(ratio * 400);
      chat.setHbarPolicy(ScrollBarPolicy.NEVER);
      chat.setVbarPolicy(ScrollBarPolicy.ALWAYS);
      chat.setCache(true);

      /*
       * setting up the Button and ComboBox
       */

      sendButton = new DesignButton(new Insets(0, 10, 0, 10), 10, ratio * 15, ratio * 120);
      sendButton.setText("SEND");

      names.setPrefWidth(ratio * 120);
      // TODO add names or change them with a foreach
      names.getItems().add("All");
      names.setValue("All");
      names.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

      comboAndSend.prefHeight(ratio * 100);
      comboAndSend.prefWidth(ratio * 120);
      comboAndSend.setAlignment(Pos.CENTER);
      comboAndSend.getChildren().addAll(sendButton, new Spacing(15), names);

      /*
       * setting up textfieldMessage
       */
      textfieldMessage.setMaxHeight(ratio * 100);
      textfieldMessage.setMaxWidth(ratio * 400);
      textfieldMessage.setPrefHeight(ratio * 100);
      textfieldMessage.setPrefWidth(ratio * 400);
      textfieldMessage.setStyle(
          "-fx-background-color: rgba(225,211,184,0.9); -fx-background-radius: 10 10 10 10");
      textfieldMessage.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 17 * ratio));
      /*
       * setting up the textfield with the buttons
       */

      textfieldAndButtons.setPrefHeight(ratio * 100);
      textfieldAndButtons.setPrefWidth(ratio * 600);
      textfieldAndButtons.setMaxHeight(ratio * 100);
      textfieldAndButtons.setMaxWidth(ratio * 600);
      textfieldAndButtons
          .setStyle("-fx-background-color: rgba(92,64,51); -fx-background-radius: 0 0 10 10;");
      textfieldAndButtons.setAlignment(Pos.CENTER_RIGHT);
      textfieldAndButtons.setPadding(new Insets(20, 20, 20, 20));
      textfieldAndButtons.getChildren().addAll(textfieldMessage, new Spacing(20), comboAndSend);
      textfieldAndButtons.setPickOnBounds(true);

      vBoxMessages.setPrefWidth(ratio * 600);
      vBoxMessages.setSpacing(ratio * 5);
      vBoxMessages.setPadding(new Insets(5 * ratio, 50 * ratio, 0, 5 * ratio));
      vBoxMessages.heightProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue,
            Number newValue) {
          chat.setVvalue((Double) newValue);
        }
      });

      // TODO decide if (host message if needed) stays

      chat.setContent(vBoxMessages);

      /*
       * setting up main window Proportions
       */

      this.setPrefHeight(ratio * 530);
      this.setMaxHeight(ratio * 570);
      this.setMaxWidth(ratio * 600);
      this.setAlignment(Pos.CENTER);
      this.setPickOnBounds(true);
      this.getChildren().addAll(dragArea, chat, textfieldAndButtons);
    }

    public void actionEventsSetup() {

      dragArea.setOnMousePressed(event -> {
        xCord = event.getSceneX();
        yCord = event.getSceneY();
        dragAreaHover = true;

      });

      dragArea.setOnMouseReleased(event -> {
        dragAreaHover = false;

      });

      textfieldMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
          if (keyEvent.getCode() == KeyCode.ENTER) {
            sendButton.fire();

          }
        }
      });

      sendButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

          messageToBeSend = textfieldMessage.getText();

          if (!messageToBeSend.isBlank()) {

            Text text = new Text(messageToBeSend);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Cooper Black", FontWeight.LIGHT, ratio * 15));

            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(92,64,51); " + "-fx-background-color: rgb(92,64,51); "
                + "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5 * ratio, 10 * ratio, 5 * ratio, 10 * ratio));

            HBox message = new HBox();
            message.setAlignment(Pos.CENTER_RIGHT);
            message.getChildren().addAll(new Spacing(200 * ratio, 1 * ratio), textFlow);
            HBox.setHgrow(message, Priority.ALWAYS);

            vBoxMessages.getChildren().add(message);
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                System.out.println("Run of Host sworks, this is what it starts with ");

                // Optional<Lobby> lobby = client.getLobbies().values().stream()
                // .filter(looby -> looby.getHumanPlayerList().stream()
                // .anyMatch(player -> (player.getID()== client.getProfile().getId()))).findAny();
                //
                // if(lobby.isPresent()){
                // Lobby lobby1 = lobby.get();
                // client.sendMessage( new MessageinLobby(lobby1, messageToBeSend));
                //
                // }
                System.out.println(client.isInALobby() + " sender");

                if (!names.getValue().equals("All")) {

                  String username = names.getValue();
                  // send the message to the specified user
                  System.out
                      .println(client.getProfile().getUserName() + " e izprashtach i prashta na  "
                          + findProfileFromString(username).getUserName() + " subshtenieto e "
                          + username);
                  // Assume that lobbies is a HashMap<String, Lobby> and client refers to the
                  // client object
                  // Assume that client.getLobbies() returns a HashMap<String, Lobby> and client
                  // refers to the client object
                  // Assume that client.getLobbies() returns a HashMap<String, Lobby> and client
                  // refers to the client object
                  if (!client.isInAGame()) {
                    client.sendMessage(new MessageToPerson(messageToBeSend, client.getProfile(),
                        findProfileFromString(username), client.isInALobby()));
                  } else {
                    client.sendMessage(new MessageToPerson(messageToBeSend, client.getProfile(),
                        findProfileFromString(username)));
                  }


                } else if (!messageToBeSend.equals(null) && names.getValue().equals("All")) {
                  // send the message to the general chat
                  // Assume that lobbyGUIList is a HashMap<String, LobbyGUI> and client refers to
                  // the client object
                  if (!client.isInAGame()) {
                    client.sendMessage(
                        new MessageSend(messageToBeSend, client.getProfile(), client.isInALobby()));
                  } else {
                    System.out.println(client.getClientsLobby().getLobbyName());

                    client.sendMessage(new MessageSendInGame(messageToBeSend,
                        client.getClientsLobby(), client.getProfile()));
                  }

                }
              }
            });

            textfieldMessage.clear();
            textfieldMessage.requestFocus();
          }
        }
      });

      this.setOnMouseDragged(event -> {
        if (this.isDragAreaHover()) {

          this.setTranslateX(this.getTranslateX() + event.getSceneX() - this.getxCord());
          this.setTranslateY(this.getTranslateY() + event.getSceneY() - this.getyCord());
          this.setxCord(event.getSceneX());
          this.setyCord(event.getSceneY());
          System.out.println("durpai");

        }
      });

    }

    /*
     * finds the username from the send message so that it can be a private message
     */

    private Profile findProfileFromString(String username) {

      for (Profile profile : Client.profiles) {

        if (profile.getUserName().equalsIgnoreCase(username)) {

          return profile;
        }
      }
      return null;
    }

    /*
     * adds to the GUI the incomming message from other users to all users
     */

    public void addLabel(String messageFromCLient) {

      Text text = new Text(messageFromCLient);
      text.setFill(Color.WHITE);
      text.setFont(Font.font("Cooper Black", FontWeight.LIGHT, ratio * 15));

      TextFlow textFlow = new TextFlow(text);
      textFlow.setStyle("-fx-color: rgb(92,64,51); " + "-fx-background-color: rgb(92,64,51); "
          + "-fx-background-radius: 20px;");
      textFlow.setPadding(new Insets(5 * ratio, 10 * ratio, 5 * ratio, 10 * ratio));

      HBox message = new HBox();
      message.setAlignment(Pos.CENTER_LEFT);
      message.getChildren().addAll(textFlow, new Spacing(150 * ratio, 1 * ratio));
      HBox.setHgrow(message, Priority.ALWAYS);

      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          vBoxMessages.getChildren().add(message);
        }
      });

    }

    /*
     * method for the GUI to display the personal message
     */

    public void addLabel(String messageFromCLient, String profile) {

      Text text = new Text(messageFromCLient);
      text.setFill(Color.WHITE);
      text.setFont(Font.font("Cooper Black", FontWeight.LIGHT, ratio * 15));

      Text username = new Text(profile + ": ");
      username.setFill(Color.YELLOW);
      username.setFont(Font.font("Cooper Black", FontWeight.LIGHT, ratio * 15));

      TextFlow textFlow = new TextFlow();
      textFlow.getChildren().addAll(username, text);
      textFlow.setStyle("-fx-color: rgb(92,64,51); " + "-fx-background-color: rgb(92,64,51); "
          + "-fx-background-radius: 20px;");
      textFlow.setLineSpacing(ratio * 5);
      textFlow.setPadding(new Insets(5 * ratio, 10 * ratio, 5 * ratio, 10 * ratio));

      HBox message = new HBox();
      message.setAlignment(Pos.CENTER_LEFT);
      message.getChildren().addAll(textFlow, new Spacing(150 * ratio, 1 * ratio));
      HBox.setHgrow(message, Priority.ALWAYS);

      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          vBoxMessages.getChildren().add(message);
        }
      });

    }

    public void addLabelFromSystem(String messageFromCLient) {

      Text text = new Text(messageFromCLient);
      text.setFill(Color.YELLOW);
      text.setFont(Font.font("Cooper Black", FontWeight.LIGHT, ratio * 15));

      TextFlow textFlow = new TextFlow(text);
      textFlow.setStyle("-fx-color: rgb(92,64,51); " + "-fx-background-color: rgb(92,64,51); "
          + "-fx-background-radius: 20px;");
      textFlow.setPadding(new Insets(5 * ratio, 10 * ratio, 5 * ratio, 10 * ratio));

      HBox message = new HBox();
      message.setAlignment(Pos.CENTER_LEFT);
      message.getChildren().addAll(textFlow, new Spacing(150 * ratio, 1 * ratio));
      HBox.setHgrow(message, Priority.ALWAYS);

      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          vBoxMessages.getChildren().add(message);
        }
      });

    }

    public void addItemsInComboBox(Profile profile) {
      Platform.runLater(() -> {
        items.add(profile.getUserName());
      });
    }

    public void removeItemsInComboBox(Profile profile) {
      Platform.runLater(() -> {
        items.remove(profile.getUserName());
      });
    }

    public void clearItemsInComboBox() {
      Platform.runLater(() -> {
        items.clear();
      });
    }

    public void setxCord(double xCord) {
      this.xCord = xCord;
    }

    public double getxCord() {
      return xCord;
    }

    public void setyCord(double yCord) {
      this.yCord = yCord;
    }

    public double getyCord() {
      return yCord;
    }

    public boolean isDragAreaHover() {
      return dragAreaHover;
    }

    public String getMessageToSend() {
      return messageToBeSend;
    }

    public Client getClient() {
      return client;
    }

    public void setClient(Client client) {
      this.client = client;
    }
  }

  static class SettingsButton extends Button {
    public SettingsButton() {
      super();
      this.setPadding(new Insets(10, 20, 10, 20));
      this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 28));
      this.setTextFill(Color.WHITE);
      this.setStyle("-fx-background-color: #b87331;" + "-fx-background-radius: 15;"
          + "-fx-border-radius: 12;" + "-fx-border-color: #b87331;" + "-fx-border-width: 3px;");

      this.hoverProperty()
          .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
              this.setStyle("-fx-background-color: #64441f;" + "-fx-background-radius: 15;"
                  + "-fx-border-radius: 12;" + "-fx-border-color: #ffff;"
                  + "-fx-border-width: 3px;");
            } else {
              this.setStyle("-fx-background-color: #b87331;" + "-fx-background-radius: 15;"
                  + "-fx-border-radius: 12;" + "-fx-border-color: #b87331;"
                  + "-fx-border-width: 3px;");
            }
          });
    }

    private SettingsButton(Insets inst, int radius, double fontSize, double width) {
      super();
      this.setPadding(inst);
      this.setPrefWidth(width);
      this.setFont(Font.font("Cooper Black", FontWeight.NORMAL, fontSize));
      this.setTextFill(Color.WHITE);
      this.setStyle(
          "-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
              + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
              + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius + ";"
              + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");

      this.hoverProperty()
          .addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {

              this.setStyle("-fx-background-color: #64441f;" + "-fx-background-insets: 1 1 1 1;"
                  + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius + ";"
                  + "-fx-border-color: #ffff;" + "-fx-border-width: 4px;");
            } else {
              this.setStyle(
                  "-fx-background-color: " + "radial-gradient(focus-distance 0% , center 50% 50% , "
                      + "radius 75% , #b87331, #64441f);" + "-fx-background-insets: 1 1 1 1;"
                      + "-fx-background-radius: " + radius + ";" + "-fx-border-radius: " + radius
                      + ";" + "-fx-border-color: transparent;" + "-fx-border-width: 4px;");
            }
          });
    }

    public SettingsButton(Insets inst, int radius, double fontSize, double width,
        boolean settingsButton) throws FileNotFoundException {
      this(inst, radius, fontSize, width);
      if (settingsButton) {
        this.setText("Settings");
      }

    }
  }

  public static class SettingsPane extends VBox {
    double fontSize;
    SettingsButton settingsButton;
    static double w = MainApp.screenWidth;
    static double h = MainApp.screenHeight;

    public SettingsPane(double fontSize, SettingsButton settingsButton) {
      this.fontSize = fontSize;
      this.settingsButton = settingsButton;
    }

    private static double getRelativeHorz(double x) {
      return (x / 1536.0) * w;
    }

    private static double getRelativeVer(double y) {
      return (y / 864.0) * h;
    }

    public static StackPane createMutePane(double fontSize, SettingsButton settingsButton,
        double ratio) {
      MediaPlayer musicPlayer = AppController.getGameSound().getMusicSoundPlayer();
      MediaPlayer soundPlayer = AppController.getGameSound().getEffectsSoundPlayer();
      // Create buttons for music and sound effects mute/unmute
      DesignButton musicButton =
          new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 600 * ratio);
      musicButton.setText("Music: Unmute");
      DesignButton soundButton =
          new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 600 * ratio);
      soundButton.setText("Sound Effects: Unmute");
      DesignButton confirmationButton =
          new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 300 * ratio);
      ImageView imgMusicButton = new ImageView();

      confirmationButton.setText("Confirm");

      try {
        imgMusicButton.setImage(new Image(new FileInputStream(Parameter.audioSpeakerIcon)));
        imgMusicButton.setFitHeight(fontSize);
        imgMusicButton.setPreserveRatio(true);
        imgMusicButton.setSmooth(true);
        imgMusicButton.setCache(true);
        musicButton.setGraphicTextGap(10);
        musicButton.setGraphic(imgMusicButton);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      // Add event handlers to toggle mute/unmute state of media players
      musicButton.setOnAction(event -> {
        if (musicPlayer != null) {
          AppController.getGameSound().buttonClickForwardSound();
          if (musicPlayer.isMute()) {
            musicPlayer.setMute(false);
            musicButton.setText("Music: Unmute");
            try {
              imgMusicButton.setImage(new Image(new FileInputStream(Parameter.audioSpeakerIcon)));
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            }

          } else {
            musicPlayer.setMute(true);
            musicButton.setText("Music: Mute");
            try {
              imgMusicButton
                  .setImage(new Image(new FileInputStream(Parameter.audioSpeakerMuteIcon)));
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            }
          }
        }
      });

      soundButton.setOnAction(event -> {
        if (soundPlayer != null) {
          AppController.getGameSound().buttonClickForwardSound();
          if (AppController.getGameSound().isMutePropertyEffectsSound()) {
            soundPlayer.setMute(false);
            AppController.getGameSound().setMutePropertyEffectsSound(false);
            soundButton.setText("Sound Effects: Unmute");
          } else {
            soundPlayer.setMute(true);
            AppController.getGameSound().setMutePropertyEffectsSound(true);
            soundButton.setText("Sound Effects: Mute");
          }
        }
      });

      // Set the initial state of the mute/unmute buttons based on the media player's
      // mute state
      if (musicPlayer != null && musicPlayer.isMute()) {
        musicButton.setText("Music: Mute");
        try {
          imgMusicButton.setImage(new Image(new FileInputStream(Parameter.audioSpeakerMuteIcon)));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
      if (soundPlayer != null && AppController.getGameSound().isMutePropertyEffectsSound()) {
        soundButton.setText("Sound Effects: Mute");
      }



      Rectangle whiteBackground = new Rectangle();
      whiteBackground.setFill(Color.WHITE);
      whiteBackground.setWidth(getRelativeHorz(400.0));
      whiteBackground.setHeight(getRelativeVer(200.0));
      whiteBackground.setLayoutX((w - whiteBackground.getWidth()) / 2.0);
      whiteBackground.setLayoutY(getRelativeVer(500));

      // Create a VBox to hold the buttons
      VBox vbox = new VBox(10, musicButton, soundButton, confirmationButton);

      vbox.setPadding(new Insets(100 * ratio, 20 * ratio, 100 * ratio, 60 * ratio));

      // Set the background color and opacity of the VBox
      vbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

      // Create a StackPane to hold the VBox and center it on the screen
      StackPane stackPane = new StackPane(vbox);
      stackPane.setPrefSize(200, 100);

      confirmationButton.setOnAction(event -> {
        AppController.getGameSound().buttonClickBackwardSound();
        stackPane.setVisible(false);
        settingsButton.setDisable(false);
      });


      stackPane.setPrefSize(w, h);

      return stackPane;
    }
  }
}
