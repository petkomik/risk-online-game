package game.gui;

import java.util.Optional;

import database.Profile;
import game.exceptions.WrongTextFieldInputException;
import game.gui.GUISupportClasses.Spacing;
import general.AppController;
import general.Parameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Dialog box for a login/signup with a secondary profile. Called in singleplayer lobby.
 *
 * @author pmikov
 *
 */

public class SecondaryPlayerDialog {

  Profile profileToReturn;

  /**
   * Method to call the Dialog box.
   *
   * @return Profile instance that the user created or logged in to.
   */

  @SuppressWarnings("static-access")
  public Profile addPlayerDialog() {
    Dialog<Profile> dialog = new Dialog<>();
    dialog.setTitle("Add Player");
    dialog.getDialogPane().setMinSize(350, 230);

    ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType);

    VBox vBox = new VBox();
    HBox userRow = new HBox();
    TextField username = new TextField();
    Label userLabel = new Label();
    userRow.getChildren().addAll(userLabel, new Spacing(1), username);
    userRow.setSpacing(20);

    HBox passRow = new HBox();
    PasswordField password = new PasswordField();
    Label passLabel = new Label();
    passRow.getChildren().addAll(passLabel, new Spacing(1), password);
    passRow.setSpacing(20);


    HBox firstNameRow = new HBox();
    TextField firstName = new TextField();
    Label firstLabel = new Label();
    firstNameRow.getChildren().addAll(firstLabel, new Spacing(1), firstName);
    firstNameRow.setSpacing(20);


    HBox lastNameRow = new HBox();
    TextField lastName = new TextField();
    Label lastLabel = new Label();
    lastNameRow.getChildren().addAll(lastLabel, new Spacing(1), lastName);
    lastNameRow.setSpacing(20);

    vBox.setSpacing(10);
    vBox.setPadding(new Insets(50, 50, 50, 50));
    vBox.setAlignment(Pos.CENTER);

    username.setPromptText("Username");
    password.setPromptText("Password");
    firstName.setPromptText("First Name");
    lastName.setPromptText("Last Name");
    userLabel.setText("Username:");
    passLabel.setText("Password:");
    firstLabel.setText("First Name:");
    lastLabel.setText("Last Name:");

    Button logIn = new Button("Log In");
    Button signUp = new Button("Sign Up");

    vBox.getChildren().addAll(logIn, signUp);


    Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
    confirmButton.setVisible(false);


    logIn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        vBox.getChildren().removeIf(x -> true);
        vBox.getChildren().addAll(userRow, passRow);
        dialog.setTitle("Log In");
        confirmButton.setVisible(true);

      }
    });

    signUp.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        vBox.getChildren().removeIf(x -> true);
        vBox.getChildren().addAll(userRow, passRow, firstNameRow, lastNameRow);
        dialog.setTitle("Sign Up");
        confirmButton.setVisible(true);

      }
    });



    dialog.getDialogPane().setContent(vBox);

    // Convert the result to a username-password-pair when the login button is clicked.
    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == confirmButtonType) {
        // return new Pair<>(username.getText(), password.getText());
        Profile profile = AppController.logIntoSecondProfile(username.getText().trim(),
            password.getText().trim());

        if (dialog.getTitle().equals("Log In")) {
          if (profile != null) {
            return profile;
          } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Username or password is incorrect!");
            alert.setHeaderText("ERROR");
            alert.setTitle("");
            Stage tmp = (Stage) alert.getDialogPane().getScene().getWindow();
            tmp.getIcons().add(new Image(Parameter.errorIcon));
            alert.showAndWait();
          }
        } else {
          try {
            return MainApp.getAppController().createFirstSecondaryProfile(firstName.getText(),
                lastName.getText(), username.getText(), password.getText());

          } catch (WrongTextFieldInputException e1) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e1.getMessage());
            alert.setHeaderText("ERROR");
            alert.setTitle("");
            Stage tmp = (Stage) alert.getDialogPane().getScene().getWindow();
            tmp.getIcons().add(new Image(Parameter.errorIcon));
            alert.showAndWait();
          }
        }
      }
      return null;
    });

    Optional<Profile> result = dialog.showAndWait();

    result.ifPresent(profileToAdd -> {
      this.profileToReturn = profileToAdd;
    });

    return profileToReturn;


  }
}
