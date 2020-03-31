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
import shared.Map;
import shared.MapGenerator;

public class MainController {

    private static final MainController INSTANCE = new MainController();
    private Stage window;
    // controller instances
    private LoginController loginController = new LoginController();
    private InitController initController;
    private GameController gameController;
    // model instances
    Map worldmap = MapGenerator.initmapGenerator();

    public static MainController getInstance() {
        return INSTANCE;
    }

    public void setStage(Stage stage) {
        this.window = stage;
    }

    public void showLoginScene() {
        window.setScene(loginController.getCurrScene());
        loginController.getLoginBtn().setOnAction(e -> {
                String username = loginController.getUsername().getText();
                String pwd = loginController.getPwd().getText();
                // debug
                System.out.println("username: " + username);
                System.out.println("password: " + pwd);
                showInitScene();
            }); // if login, change to the init scene
    }

    public void showRoomScene() {

    }

    public void showInitScene() {
        this.initController = new InitController(this.worldmap);
        window.setScene(initController.getCurrScene());
        initController.getStartGameBtn().setOnAction(e -> {
                showGameScene();
            }); // if start game, change to the game scene
    }

    public void showGameScene() {
        this.gameController = new GameController(this.worldmap);
        window.setScene(gameController.getCurrScene());
    }

    public void showStage() {
        window.show();
    }

}
