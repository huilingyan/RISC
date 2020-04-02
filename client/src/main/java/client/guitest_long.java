package client;

import client.controller.InfoPaneController;
import client.controller.InitController;
import client.controller.InitOpPaneController;
import client.controller.ModeSelectPaneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shared.Action;
import shared.Map;
import shared.MapGenerator;

public class guitest_long extends Application{
    @Override
    public void start(Stage stage) {
       stage.setTitle("guitest");
       Label l = new Label("hello world");
       Map worldmap=MapGenerator.initmapGenerator();

       InitOpPaneController iopPC = new InitOpPaneController("Ditto");
       InfoPaneController iPC = new InfoPaneController(worldmap);
       ModeSelectPaneController msPC = new ModeSelectPaneController("Ditto");
       InitController iC = new InitController(worldmap,new Action());
       iC.setMaster(0);
       Scene s1 = new Scene(msPC.getCurrPane(), 1280, 720);
       stage.setScene(s1);
       stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
