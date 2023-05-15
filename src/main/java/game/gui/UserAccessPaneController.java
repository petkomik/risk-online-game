package game.gui;

import game.gui.GuiSupportClasses.DesignButton;
import game.gui.GuiSupportClasses.ImageViewPane;
import general.AppController;
import general.GameSound;
import general.Parameter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Pane where the player chooses between log in and sign up.
 *
 * @author pmikov
 */

public class UserAccessPaneController extends StackPane {

  private GameSound gameSound = AppController.getGameSound();
  private Stage stage;
  private VBox vbox;
  private ImageView imgBackground;
  private ImageViewPane imgBackgroundPane;
  private VBox vboxColor;
  private VBox contentVbox;
  private ImageView riskLogo;
  private DesignButton logIn;
  private DesignButton signUp;
  private double ratio;

  /**
   * Default constructor. Build the pane.
   *
   * @throws FileNotFoundException for backgroud map image not found.
   */

  public UserAccessPaneController() throws FileNotFoundException {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.ratio = Math.min(ratio + 0.3, 1);
    setup();
  }

  /** Set up method. Sets Button action events and build nodes. */

  public void setup() throws FileNotFoundException {

    this.setAlignment(Pos.CENTER);

    /*
     * First layer of stack Background map image
     */

    vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.setFillWidth(true);

    imgBackground = new ImageView();
    imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
    imgBackground.setPreserveRatio(false);
    imgBackground.setSmooth(true);
    imgBackground.setCache(true);

    imgBackgroundPane = new ImageViewPane(imgBackground);
    VBox.setVgrow(imgBackgroundPane, Priority.ALWAYS);

    vbox.getChildren().add(imgBackgroundPane);

    /*
     * Second layer of stack Color mask
     */

    vboxColor = new VBox();
    vboxColor.setAlignment(Pos.CENTER);
    vboxColor.setFillWidth(true);
    vboxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.7);");


    contentVbox = new VBox();
    contentVbox.setAlignment(Pos.CENTER);

    riskLogo = new ImageView();
    riskLogo.setImage(new Image(new FileInputStream(Parameter.logoImage)));
    riskLogo.setFitWidth(650 * ratio);
    riskLogo.setPreserveRatio(true);
    riskLogo.setSmooth(true);
    riskLogo.setCache(true);

    logIn =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 300 * ratio);
    logIn.setText("Log In");

    signUp =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 300 * ratio);
    signUp.setText("Sign Up");

    contentVbox.setSpacing(30 * ratio);

    contentVbox.getChildren().addAll(riskLogo, logIn, signUp);
    contentVbox.setPadding(new Insets(0, 0, 50 * ratio, 0));


    // maybe add vBoxColor
    this.getChildren().addAll(vbox, vboxColor, contentVbox);

    logIn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();

        try {
          LogInPaneController logIn = new LogInPaneController();
          stage.getScene().setRoot(logIn);
        } catch (IOException e) {
          e.printStackTrace();
        }

        stage.show();
      }
    });

    signUp.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();

        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        try {
          CreateProfilePaneController signUp = new CreateProfilePaneController();
          stage.getScene().setRoot(signUp);
        } catch (IOException e) {
          e.printStackTrace();
        }

        stage.show();
      }
    });
  }
}


