package client.controller;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import shared.Map;
import shared.PlayerStat;



public class WaitPaneController implements PaneController {

    @Override
    public AnchorPane getCurrPane() {

        Label l = new Label("Waiting for other players...");
        l.setStyle("-fx-font: 24 arial;");

        AnchorPane anchorP = new AnchorPane(l);
        // l.setAlignment();
        AnchorPane.setTopAnchor(l, 10.0);
        AnchorPane.setBottomAnchor(l, 10.0);
        AnchorPane.setLeftAnchor(l, 10.0);
        AnchorPane.setRightAnchor(l, 10.0);
        return anchorP;
    }

}
