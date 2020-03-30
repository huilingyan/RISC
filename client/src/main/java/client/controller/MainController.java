package client.controller;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

public class MainController {

    private static final MainController INSTANCE = new MainController();
    private Stage window;

    private LoginController loginController = new LoginController();
    private InitController initController = new InitController();

    public static MainController getInstance() {
        return INSTANCE;
    }

    public void setStage(Stage stage) {
        this.window = stage;
    }

    public void showLoginScene() {
        window.setScene(loginController.getCurrScene());
        loginController.getLoginbtn().setOnAction(e -> {
                showInitScene();
            }); // if login, change to the map scene
    }

    public void showInitScene() {
        window.setScene(initController.getCurrScene());
    }

    public void showRoomScene() {
        
    }

    public void showGameScene() {

    }

    public void showStage() {
        window.show();
    }

}
