package client.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

public class InitController extends SceneController {

    @Override
    public Scene getCurrScene() {

        Label l = new Label("This is the map page");
        Scene mapscene = new Scene(new StackPane(l), 640, 480);

        return mapscene;

    }
}
