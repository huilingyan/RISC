package client;

import client.controller.InfoPaneController;
import client.controller.InitOpPaneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shared.Map;
import shared.MapGenerator;

public class guitest_long extends Application{
    @Override
    public void start(Stage stage) {
      stage.setTitle("guitest");
      Label l = new Label("hello world");
      Map worldmap=MapGenerator.initmapGenerator();
      InitOpPaneController iopPC = new InitOpPaneController();
      InfoPaneController iPC = new InfoPaneController(worldmap);
      Scene s1 = new Scene(iPC.getCurrPane(), 1280, 720);
      stage.setScene(s1);
      stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
