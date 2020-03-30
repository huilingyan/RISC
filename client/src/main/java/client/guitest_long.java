package client;

import client.controller.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class guitest_long extends Application{
    @Override
    public void start(Stage stage) {
      stage.setTitle("guitest");
      Label l = new Label("hello world");
      InitOpPaneController iopPC = new InitOpPaneController();
      Scene s1 = new Scene(iopPC.getCurrPane(), 1280, 720);
      stage.setScene(s1);
      stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
