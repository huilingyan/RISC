package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

import client.controller.*;

public class RISC_GUI extends Application {

    @Override
    public void start(Stage stage) {

        MainController maincontroller = MainController.getInstance();
        maincontroller.setStage(stage);
        maincontroller.showLoginScene();
        maincontroller.showStage();

    }

    public static void main(String[] args) {
        launch();
    }

}
