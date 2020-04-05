package client;

import javafx.application.Application;
import javafx.stage.Stage;

import client.controller.*;

public class RISC_GUI extends Application {

    @Override
    public void start(Stage stage) {

        MainController maincontroller = MainController.getInstance();
        maincontroller.initializeSocketConnection();
        maincontroller.setStage(stage);
        maincontroller.showLoginScene();
        maincontroller.showStage();

    }

    public static void main(String[] args) {
        launch();
    }

}
