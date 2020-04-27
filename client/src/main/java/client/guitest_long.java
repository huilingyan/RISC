package client;

import client.controller.AtkOPPaneController;
import client.controller.CardAlertBox;
import client.controller.GameController;
import client.controller.InfoPaneController;
import client.controller.InitController;
import client.controller.InitOpPaneController;
import client.controller.ModeSelectPaneController;
import client.controller.MoveOPPaneController;
import client.controller.UpOPPaneController;
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
       Map gamemap=MapGenerator.gamemapGenerator();
       /*
       InitOpPaneController iopPC = new InitOpPaneController("Ditto");
       InfoPaneController iPC = new InfoPaneController(worldmap);
       ModeSelectPaneController msPC = new ModeSelectPaneController("Ditto");
       MoveOPPaneController mopPC = new MoveOPPaneController("Ditto");
       AtkOPPaneController aopPC = new AtkOPPaneController("Ditto");
       UpOPPaneController uopPC = new UpOPPaneController("Ditto");
       */
    //    InitController iC = new InitController(worldmap);
    //    iC.setMaster(0);
       GameController gC = new GameController(gamemap,"p0",101,0);
       //Scene s1 = new Scene(uopPC.getCurrPane(), 1280, 720);
        stage.setScene(gC.getCurrScene());
       stage.show();
       //boolean activate=CardAlertBox.cardSelection(-1);
       //System.out.println(activate);

    }

    public static void main(String[] args) {
        launch();
    }
}
