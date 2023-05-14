package game.gui;

import game.gui.GUISupportClasses.ArrowButton;
import game.gui.GUISupportClasses.ChatButton;
import game.gui.GUISupportClasses.ChatWindow;
import game.gui.GUISupportClasses.DesignButton;
import game.gui.GUISupportClasses.DiceFactory;
import game.gui.GUISupportClasses.ImageViewPane;
import game.gui.GUISupportClasses.Spacing;
import game.models.Battle;
import game.models.Continent;
import game.models.CountryName;
import game.models.Territory;
import gameState.GameType;
import gameState.SinglePlayerHandler;
import general.AppController;
import general.Parameter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.util.Duration;
import network.Client;

/*
 * Class for the Battle Frame
 * 
 * @author pmikov
 * 
 */

public class BattleFrameController extends StackPane {

  private VBox root;
  private int lastThrow;
  private int maxDiceToThrow;
  private int defendingDice;
  private int[] dicesAttacker;
  private int[] dicesDefender;
  private String attackingPNG;
  private String defendingPNG;
  private int troopsInAttackAt;
  private int troopsInAttackDf;
  private String attackingAvatar;
  private String defendingAvatar;
  private Color attackerColor;
  private Color defenderColor;

  private GameType gameType;
  private SinglePlayerHandler singleplayerHandler;
  private Client client;
  private HBox chatDiv;
  private ChatButton chatButton;
  private ChatWindow chatWindow;

  private HBox imgTerritories;
  private StackPane defendingStack;
  private ImageView imgDefending;
  private StackPane attackingStack;
  private ImageView imgAttacking;

  private ImageViewPane imgAttackingPane;
  private ImageViewPane imgDefendingPane;
  private FlowPane armiesFlowDf;
  private FlowPane armiesFlowAt;

  private HBox diceAndProfile;
  private StackPane playerAt;
  private StackPane playerDf;
  private HBox diceSection;

  private StackPane stackTroopsAt;
  private Circle circleAt;
  private ImageView avatarAt;
  private Circle circleTroopsAt;
  private Label troopsTextAt;

  private StackPane stackTroopsDf;
  private Circle circleDf;
  private ImageView avatarDf;
  private Circle circleTroopsDf;
  private Label troopsTextDf;

  private VBox diceControls;
  private HBox numberOfDiceControls;
  private HBox diceButtonPane;
  private DesignButton throwBtn;
  private ArrowButton lessBtn;
  private ArrowButton moreBtn;
  private Label numberLabel;
  private FlowPane diceImagesAt;
  private FlowPane diceImagesDf;

  private double ratio;
  private double menuRatio;

  private Timeline timeline;

  public BattleFrameController() throws Exception {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.menuRatio = Math.min(ratio + 0.3, 1);
    this.attackingPNG =
        new Territory(CountryName.SouthernEurope, Continent.Europe).getAddressToPNG();
    this.defendingPNG = new Territory(CountryName.Ukraine, Continent.Europe).getAddressToPNG();
    this.troopsInAttackAt = 50;
    this.troopsInAttackDf = 38;
    this.maxDiceToThrow = Math.min(3, this.troopsInAttackAt);
    this.defendingDice = Math.min(2, this.troopsInAttackDf);
    this.dicesAttacker = new int[this.maxDiceToThrow];
    this.dicesDefender = new int[this.defendingDice];
    this.attackingAvatar = Parameter.avatarsdir + "blonde-boy.png";
    this.defendingAvatar = Parameter.avatarsdir + "ginger-girl.png";
    this.attackerColor = Parameter.blueColor;
    this.defenderColor = Parameter.greenColor;
    this.gameType = GameType.SinglePlayer;
    setup();

  }

  public BattleFrameController(Battle battle, SinglePlayerHandler singlePlayerHandler,
      boolean attacker) throws Exception {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.menuRatio = Math.min(ratio + 0.3, 1);
    this.menuRatio = Math.min(ratio + 0.3, 1);
    this.attackingPNG = battle.getAttackingPng();
    this.defendingPNG = battle.getDefendingPng();
    this.troopsInAttackAt = battle.getTroopsInAttackAt();
    this.troopsInAttackDf = battle.getTroopsInAttackDf();
    this.maxDiceToThrow = battle.getMaxDiceToThrow();
    this.defendingDice = battle.getDefendingDice();
    this.attackingAvatar = battle.getAttackingAvatar();
    this.defendingAvatar = battle.getDefendingAvatar();
    this.attackerColor = Color.web(battle.getAttackerColor());
    this.defenderColor = Color.web(battle.getDefenderColor());
    this.gameType = battle.getGameType();
    this.singleplayerHandler = singlePlayerHandler;
    setup();
    this.throwBtn.setVisible(attacker);
    this.chatDiv.setVisible(false);
    this.chatButton.setVisible(false);
  }

  public BattleFrameController(Battle battle, Client client, boolean attacker,
      ChatWindow chatWindow) throws Exception {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.menuRatio = Math.min(ratio + 0.3, 1);
    this.menuRatio = Math.min(ratio + 0.3, 1);
    this.attackingPNG =
        new Territory(battle.getCountryNameAt(), battle.getContinentAt()).getAddressToPNG();
    this.defendingPNG =
        new Territory(battle.getCountryNameDf(), battle.getContinentDf()).getAddressToPNG();
    this.troopsInAttackAt = battle.getTroopsInAttackAt();
    this.troopsInAttackDf = battle.getTroopsInAttackDf();
    this.maxDiceToThrow = battle.getMaxDiceToThrow();
    this.defendingDice = battle.getDefendingDice();
    this.attackingAvatar = battle.getAttackingAvatar();
    this.defendingAvatar = battle.getDefendingAvatar();
    this.attackerColor = Color.web(battle.getAttackerColor());
    this.defenderColor = Color.web(battle.getDefenderColor());
    this.gameType = battle.getGameType();
    this.client = client;
    for (String avatar : Parameter.allAvatars) {
      if (attackingAvatar.contains(avatar)) {
        attackingAvatar = Parameter.avatarsdir + avatar;
      }
      if (defendingAvatar.contains(avatar)) {
        defendingAvatar = Parameter.avatarsdir + avatar;
      }
    }
    this.chatWindow = chatWindow;
    setup();
    this.throwBtn.setVisible(attacker);
  }

  public void setup() throws Exception {

    /*
     * setting up root panel root
     */

    this.root = new VBox();
    this.root.setAlignment(Pos.CENTER);
    this.root.setFillWidth(true);
    this.setAlignment(Pos.CENTER);
    this.setStyle("-fx-background-color: rgb(225, 211, 184);");

    /*
     * Setting up imTerritories pane Includes imgAttacking, spacingImg, imgDefending ImageView are
     * wrapped in ImageViewPane which delivers responsiveness
     */
    chatButton =
        new ChatButton(new Insets(10 * menuRatio, 20 * menuRatio, 10 * menuRatio, 20 * menuRatio),
            30, 28 * menuRatio, 170 * menuRatio, true);
    chatButton.setAlignment(Pos.CENTER);
    if (chatWindow != null) {
      chatButton.setSelected(chatWindow.isVisible());
    }

    if (chatWindow != null) {
      if (client.getClientsLobby().getHumanPlayerList().size() == 1) {
        chatButton.setDisable(true);
      }

    }
    chatDiv = new HBox();
    chatDiv.getChildren().add(chatButton);
    chatDiv.minHeightProperty().bind(chatDiv.maxHeightProperty());
    chatDiv.maxHeightProperty().bind(chatDiv.prefHeightProperty());
    chatDiv.setPrefHeight(100 * menuRatio);
    chatDiv.setPadding(new Insets(20 * menuRatio, 20 * menuRatio, 0, 0));
    chatDiv.setAlignment(Pos.TOP_RIGHT);

    imgTerritories = new HBox();

    imgAttacking = new ImageView();
    imgAttacking.setImage(new Image(new FileInputStream(this.attackingPNG)));
    imgAttacking.setPreserveRatio(true);
    imgAttacking.setSmooth(true);
    imgAttacking.setCache(true);

    imgAttackingPane = new ImageViewPane(imgAttacking);
    HBox.setHgrow(imgAttackingPane, Priority.ALWAYS);

    attackingStack = new StackPane();
    attackingStack.setAlignment(Pos.CENTER);
    HBox.setHgrow(attackingStack, Priority.ALWAYS);

    armiesFlowAt = new FlowPane();
    armiesFlowAt.setAlignment(Pos.CENTER);
    armiesFlowAt.setHgap(30 * ratio);
    armiesFlowAt.setVgap(30 * ratio);

    attackingStack.getChildren().addAll(imgAttackingPane, armiesFlowAt);

    imgDefending = new ImageView();
    imgDefending.setImage(new Image(new FileInputStream(this.defendingPNG)));
    imgDefending.setPreserveRatio(true);
    imgDefending.setSmooth(true);
    imgDefending.setCache(true);

    imgDefendingPane = new ImageViewPane(imgDefending);
    HBox.setHgrow(imgDefendingPane, Priority.ALWAYS);

    defendingStack = new StackPane();
    defendingStack.setAlignment(Pos.CENTER);
    HBox.setHgrow(defendingStack, Priority.ALWAYS);

    armiesFlowDf = new FlowPane();
    armiesFlowDf.setAlignment(Pos.CENTER);
    armiesFlowDf.setHgap(30 * ratio);
    armiesFlowDf.setVgap(30 * ratio);
    armiesFlowDf.setRotationAxis(Rotate.Y_AXIS);
    armiesFlowDf.setRotate(180);

    defendingStack.getChildren().addAll(imgDefendingPane, armiesFlowDf);

    Spacing spacingImg = new Spacing();
    HBox.setHgrow(spacingImg, Priority.SOMETIMES);

    imgTerritories.getChildren().addAll(attackingStack, spacingImg, defendingStack);
    imgTerritories.setAlignment(Pos.TOP_CENTER);
    imgTerritories.setPadding(new Insets(0 * ratio, 100 * ratio, 0, 100 * ratio));

    /*
     * Add spacingRoot Add both imgTerritories, spacingRoot to the root root
     */

    Spacing spacingRoot = new Spacing();
    root.getChildren().addAll(chatDiv, imgTerritories, spacingRoot);
    VBox.setVgrow(spacingRoot, Priority.SOMETIMES);
    VBox.setVgrow(imgTerritories, Priority.ALWAYS);

    /*
     * Setting up bottom portion of window Includes playerAt, playerDf - players avatar, color,
     * number of troops Includes diceSection - dice controls, dice images
     */

    diceAndProfile = new HBox();
    playerAt = new StackPane();
    playerDf = new StackPane();
    diceSection = new HBox();

    /*
     * Setting up playerAt Includes Circle with player color (circleAt) Player avatar (avatarAt) Top
     * circle with number of troops left in attack - circleTroopsAt, troopsTextAt, stackTroopsAt
     */

    circleAt = new Circle(80 * menuRatio);
    circleAt.setFill(this.attackerColor);
    circleAt.setStroke(Color.WHITE);
    circleAt.setStrokeWidth(6 * menuRatio);

    playerAt.getChildren().add(circleAt);

    avatarAt = new ImageView();
    avatarAt.setImage(new Image(new FileInputStream(this.attackingAvatar)));
    avatarAt.setFitWidth(140 * menuRatio);
    avatarAt.setFitHeight(140 * menuRatio);
    avatarAt.setPreserveRatio(true);
    avatarAt.setSmooth(true);
    avatarAt.setCache(true);

    playerAt.getChildren().add(avatarAt);

    circleTroopsAt = new Circle(40 * menuRatio);
    circleTroopsAt.setFill(Color.WHITE);
    circleTroopsAt.setStroke(Color.WHITE);
    circleTroopsAt.setStrokeWidth(0);

    troopsTextAt = new Label(String.valueOf(this.troopsInAttackAt));
    troopsTextAt.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 34 * menuRatio));
    troopsTextAt.setTextFill(Color.web("#303030"));
    troopsTextAt.setMinWidth(80 * menuRatio);
    troopsTextAt.setMinHeight(80 * menuRatio);
    troopsTextAt.setAlignment(Pos.CENTER);

    stackTroopsAt = new StackPane();
    stackTroopsAt.getChildren().addAll(circleTroopsAt, troopsTextAt);
    stackTroopsAt.setAlignment(Pos.TOP_RIGHT);

    playerAt.getChildren().add(stackTroopsAt);

    /*
     * chat
     * 
     * Setting up playerDf Includes Circle with player color (circleDf) Player avatar (avatarDf) Top
     * circle with number of troops left in attack - circleTroopsDf, troopsTextDf, stackTroopsDf
     */

    circleDf = new Circle(80 * menuRatio);
    circleDf.setFill(this.defenderColor);
    circleDf.setStroke(Color.WHITE);
    circleDf.setStrokeWidth(6);

    playerDf.getChildren().add(circleDf);

    avatarDf = new ImageView();
    avatarDf.setImage(new Image(new FileInputStream(this.defendingAvatar)));
    avatarDf.setFitWidth(140 * menuRatio);
    avatarDf.setFitHeight(140 * menuRatio);
    avatarDf.setPreserveRatio(true);
    avatarDf.setSmooth(true);
    avatarDf.setCache(true);

    playerDf.getChildren().add(avatarDf);

    circleTroopsDf = new Circle(40 * menuRatio);
    circleTroopsDf.setFill(Color.WHITE);
    circleTroopsDf.setStroke(Color.WHITE);
    circleTroopsDf.setStrokeWidth(0);

    troopsTextDf = new Label(String.valueOf(this.troopsInAttackDf));
    troopsTextDf.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 34 * menuRatio));
    troopsTextDf.setTextFill(Color.web("#303030"));
    troopsTextDf.setMinWidth(80 * menuRatio);
    troopsTextDf.setMinHeight(80 * menuRatio);
    troopsTextDf.setAlignment(Pos.CENTER);

    stackTroopsDf = new StackPane();
    stackTroopsDf.getChildren().addAll(circleTroopsDf, troopsTextDf);
    stackTroopsDf.setAlignment(Pos.TOP_LEFT);

    playerDf.getChildren().add(stackTroopsDf);

    /*
     * Setting up numberOfdiceControls Includes More, Less Dice Buttons Dice numberLabel Throw Dice
     * Button Event Handlers for all Buttons
     */

    diceControls = new VBox();

    numberOfDiceControls = new HBox();
    lessBtn = new ArrowButton(60 * menuRatio);
    numberLabel = new Label(String.valueOf(this.maxDiceToThrow));
    moreBtn = new ArrowButton(60 * menuRatio);

    diceButtonPane = new HBox();
    throwBtn = new DesignButton();

    lessBtn.setText("<");
    moreBtn.setText(">");
    numberLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 34 * menuRatio));
    numberLabel.setTextFill(Color.web("#b87331"));
    numberLabel.textOverrunProperty().set(OverrunStyle.CLIP);
    numberLabel.setMinWidth(50 * menuRatio);
    numberLabel.setAlignment(Pos.CENTER);

    numberOfDiceControls.setSpacing(25 * menuRatio);
    numberOfDiceControls.getChildren().addAll(lessBtn, numberLabel, moreBtn);
    numberOfDiceControls.setAlignment(Pos.CENTER);
    numberOfDiceControls.setVisible(false);

    throwBtn.setText("Throw Dice");
    throwBtn.setPadding(new Insets(10 * menuRatio, 40 * menuRatio, 10 * menuRatio, 40 * menuRatio));
    throwBtn.setFont(Font.font("Cooper Black", FontWeight.NORMAL, 24 * menuRatio));
    diceButtonPane.getChildren().add(throwBtn);
    diceButtonPane.setAlignment(Pos.CENTER);
    diceButtonPane.setMinWidth(250 * menuRatio);

    diceControls.setSpacing(20 * menuRatio);
    diceControls.getChildren().addAll(numberOfDiceControls, diceButtonPane);
    diceControls.setAlignment(Pos.CENTER);
    diceControls.setMinWidth(250 * menuRatio);

    /*
     * Setting up Images of Dices both Attacker + Defender
     */

    diceImagesAt = diceImageFactory(maxDiceToThrow, true);
    diceImagesDf = diceImageFactory(defendingDice, false);

    throwBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        AppController.getGameSound().buttonClickForwardSound();
        throwBtn.setDisable(true);
        switch (gameType) {
          case SinglePlayer:
            singleplayerHandler.battleDiceThrow();
            break;
          case Multiplayer:
            client.battleDiceThrow();
            break;
          case Tutorial:
            singleplayerHandler.battleDiceThrow();
            break;
          default:
            break;
        }

      }
    });

    chatButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (!chatButton.isSelected()) {
          chatButton.setSelected(false);
          chatWindow.setVisible(false);
          System.out.println(chatWindow.isVisible() + " SERVERMAIN set on 'false' ");
        } else {
          chatButton.setSelected(true);
          chatWindow.setVisible(true);
          System.out.println(chatWindow.isVisible() + " SERVERMAIN set on 'true' ");
        }
        System.out.println(chatWindow.getParent() + " ist chats vater in SMW");

      }
    });

    diceSection.setSpacing(50 * menuRatio);
    diceSection.getChildren().addAll(diceImagesAt, diceControls, diceImagesDf);
    diceSection.setAlignment(Pos.CENTER);

    /*
     * Setting up spacing between DiceImages and DieceControls Packing children into parent
     * containers
     */

    Spacing spacingControls1 = new Spacing();
    Spacing spacingControls2 = new Spacing();

    diceAndProfile.getChildren().addAll(playerAt, spacingControls1, diceSection, spacingControls2,
        playerDf);
    diceAndProfile.setAlignment(Pos.BOTTOM_CENTER);
    diceAndProfile.setPadding(new Insets(0, 160 * ratio, 50 * ratio, 160 * ratio));
    HBox.setHgrow(spacingControls1, Priority.ALWAYS);
    HBox.setHgrow(spacingControls2, Priority.ALWAYS);

    root.getChildren().addAll(diceAndProfile);
    this.getChildren().add(root);

    if (this.chatWindow != null) {
      this.getChildren().add(chatWindow);
    }
  }

  public FlowPane diceImageFactory(int k, boolean at) throws FileNotFoundException {
    FlowPane diceImages = new FlowPane();

    diceImages.minHeightProperty().bind(diceImages.maxHeightProperty());
    diceImages.maxHeightProperty().bind(diceImages.prefHeightProperty());
    diceImages.setPrefHeight(200 * menuRatio);

    diceImages.minWidthProperty().bind(diceImages.maxWidthProperty());
    diceImages.maxWidthProperty().bind(diceImages.prefWidthProperty());
    diceImages.setPrefWidth(200 * menuRatio);

    diceImages.setHgap(20 * menuRatio);
    diceImages.setVgap(20 * menuRatio);

    DiceFactory[] dices = new DiceFactory[k];
    for (int i = 0; i < dices.length; i++) {
      dices[i] = new DiceFactory((i * 29) % 6 + 1, at, menuRatio);
      diceImages.getChildren().add(dices[i]);
    }

    diceImages.setAlignment(Pos.CENTER);

    return diceImages;
  }

  public void setCorrectTroops() throws FileNotFoundException {
    this.setCorrectTroops(this.armiesFlowAt, true);
    this.setCorrectTroops(this.armiesFlowDf, false);
  }

  public void setCorrectTroops(FlowPane flow, boolean attacking) throws FileNotFoundException {

    int inf = 0;
    int cav = 0;
    int art = 0;

    Territory territ;
    int numberTroops;

    double heightFrame = MainApp.screenHeight;
    double multiplier = Math.min(1, heightFrame / 1000);
    multiplier *= 0.8;

    if (attacking) {
      numberTroops = Math.min(30 + (this.troopsInAttackAt) % 10, this.troopsInAttackAt);
    } else {
      numberTroops = Math.min(30 + (this.troopsInAttackDf) % 10, this.troopsInAttackDf);
    }

    while (numberTroops > 0) {
      if (numberTroops >= 10) {
        numberTroops -= 10;
        art++;
      } else if (numberTroops >= 5) {
        numberTroops -= 5;
        cav++;
      } else {
        numberTroops--;
        inf++;
      }
    }

    ImageView[] artAr = new ImageView[art];
    ImageView[] cavAr = new ImageView[cav];
    ImageView[] infAr = new ImageView[inf];

    flow.getChildren().removeAll(flow.getChildren());

    for (ImageView iv : artAr) {
      iv = new ImageView();
      iv.setImage(new Image(new FileInputStream(Parameter.artillery)));
      iv.setPreserveRatio(true);
      iv.setSmooth(true);
      iv.setCache(true);
      iv.setFitHeight(220 * multiplier);
      flow.getChildren().add(iv);

    }

    for (ImageView iv : cavAr) {
      iv = new ImageView();
      iv.setImage(new Image(new FileInputStream(Parameter.cavalry)));
      iv.setPreserveRatio(true);
      iv.setSmooth(true);
      iv.setCache(true);
      iv.setFitHeight(200 * multiplier);
      flow.getChildren().add(iv);

    }

    for (ImageView iv : infAr) {
      iv = new ImageView();
      iv.setImage(new Image(new FileInputStream(Parameter.infantry)));
      iv.setPreserveRatio(true);
      iv.setSmooth(true);
      iv.setCache(true);
      iv.setFitHeight(160 * multiplier);
      flow.getChildren().add(iv);
    }

    flow.minWidthProperty().bind(flow.prefWidthProperty());
    flow.prefWidthProperty().bind(flow.maxWidthProperty());

    if (art >= 2) {
      flow.setMaxWidth(660 * multiplier);
    } else if (art == 1 && cav == 1) {
      flow.setMaxWidth(600 * multiplier);
    } else {
      flow.setMaxWidth(400 * multiplier);
    }

    this.diceAndProfile
        .setPadding(new Insets(0, 160 * multiplier, 50 * multiplier, 160 * multiplier));
  }

  public void rollBattleDice(int[] attackerDiceValues, int[] defenderDiceValues,
      int troopsInAttackAt, int troopsInAttackDf, int[] numberOfDice) throws FileNotFoundException {
    this.troopsInAttackAt = troopsInAttackAt;
    this.troopsInAttackDf = troopsInAttackDf;
    this.maxDiceToThrow = numberOfDice[0];
    this.defendingDice = numberOfDice[1];
    this.dicesAttacker = attackerDiceValues;
    this.dicesDefender = defenderDiceValues;

    Platform.runLater(() -> {
      timeline = new Timeline(new KeyFrame(Duration.millis(80.0), e -> {

        for (int k = 0; k < diceImagesAt.getChildren().size(); k++) {
          Random random = new Random();
          int n = random.nextInt(6) + 1;
          DiceFactory dice = (DiceFactory) diceImagesAt.getChildren().get(k);
          try {
            dice.setImage(new Image(
                new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(n) + ".png")));
          } catch (FileNotFoundException e1) {
            e1.printStackTrace();
          }
          final int m = k;
          diceImagesAt.getChildren().set(m, dice);
        }

        for (int k = 0; k < diceImagesDf.getChildren().size(); k++) {
          Random random = new Random();
          int n = random.nextInt(6) + 1;
          DiceFactory dice = (DiceFactory) diceImagesDf.getChildren().get(k);
          try {
            dice.setImage(new Image(
                new FileInputStream(Parameter.dicedir + "dice" + String.valueOf(n) + "b.png")));
          } catch (FileNotFoundException e1) {
            e1.printStackTrace();
          }
          final int m = k;
          diceImagesDf.getChildren().set(m, dice);
        }

      }));
      timeline.setCycleCount(12);
      timeline.play();

      timeline.setOnFinished(finish -> {
        try {
          this.timelineFinished(attackerDiceValues, defenderDiceValues);
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
      });
    });
  }

  public void timelineFinished(int[] attackerDiceValues, int[] defenderDiceValues)
      throws FileNotFoundException {
    Platform.runLater(() -> {
      for (int k = 0; k < diceImagesAt.getChildren().size(); k++) {
        DiceFactory dice = (DiceFactory) diceImagesAt.getChildren().get(k);
        try {
          dice.setImage(new Image(new FileInputStream(
              Parameter.dicedir + "dice" + String.valueOf(attackerDiceValues[k]) + ".png")));
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        final int m = k;
        diceImagesAt.getChildren().set(m, dice);
      }

      for (int k = 0; k < diceImagesDf.getChildren().size(); k++) {
        DiceFactory dice = (DiceFactory) diceImagesDf.getChildren().get(k);
        try {
          dice.setImage(new Image(new FileInputStream(
              Parameter.dicedir + "dice" + String.valueOf(defenderDiceValues[k]) + "b.png")));
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        final int m = k;
        diceImagesDf.getChildren().set(m, dice);
      }

      try {
        this.setCorrectTroops();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      troopsTextAt.setText(String.valueOf(this.troopsInAttackAt));
      troopsTextDf.setText(String.valueOf(this.troopsInAttackDf));

      while (this.diceImagesAt.getChildren().size() > this.maxDiceToThrow) {
        this.diceImagesAt.getChildren().remove(0);
      }
      if (this.diceImagesDf.getChildren().size() > this.defendingDice) {
        this.diceImagesDf.getChildren().remove(0);
      }

      throwBtn.setDisable(false);
    });
  }

  public ChatWindow getChatWindow() {
    chatWindow.setVisible(false);
    return chatWindow;
  }

}
