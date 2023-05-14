module risk5openjfx.openjfx {
  requires transitive javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires javafx.base;
  requires javafx.media;

  opens game.gui to javafx.fxml;

  exports game.gui;
}
