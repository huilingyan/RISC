package client.controller;

import java.util.ArrayList;

import client.ArmySlider;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import shared.MapGenerator;
import shared.MoveOperation;
import shared.Territory;

public class MoveOPPaneController implements PaneController {
    public GameController gc;
    private String terrName;

    public void setGameController(GameController gameC) {
        this.gc = gameC;
    }

    public MoveOPPaneController(String t_name) {
        terrName = t_name;
    }

    @Override
    public AnchorPane getCurrPane() {
        Territory terr =gc.getWorldmap().getTerritoryByName(terrName);

        Text costNotification = new Text("Move will cost food = the sum of territory sizes the path travelled including the destination\n x number of soldiers moved.");
        Text selectArmy = new Text("Move the slider to select how many soldiers you want to move:");
        ArmySlider amsld = new ArmySlider(terr.getDefender());
        Text selectDest = new Text("Select the destination where the army will go:");
        ChoiceBox<String> chBox = new ChoiceBox<>();
        ArrayList<String> ownTnames = gc.getWorldmap().getOwnTerritoryListName(gc.getPid());
        chBox.getItems().addAll(ownTnames);
        chBox.setValue(ownTnames.get(0));

        Button proceedBtn = new Button("Proceed");
        Button cancelBtn = new Button("Cancel");
        ButtonBar BtnBar = new ButtonBar();
        BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
        proceedBtn.setOnAction(e -> {
            gc.addMoveOP(new MoveOperation(terrName, chBox.getValue(), amsld.getArmy()));
            System.out.println(amsld.getArmy().getSoldierNumber(0));
            System.out.println(chBox.getValue());
            gc.showInfoPane();
          });
        cancelBtn.setOnAction(e -> gc.showInfoPane());

        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        vb.getChildren().addAll(costNotification,selectArmy, amsld.getArmySliders(), selectDest, chBox, BtnBar);

        AnchorPane anchorP = new AnchorPane(vb);
        AnchorPane.setTopAnchor(vb, 10.0);
        AnchorPane.setBottomAnchor(vb, 10.0);
        AnchorPane.setLeftAnchor(vb, 10.0);
        AnchorPane.setRightAnchor(vb, 10.0);

        return anchorP;
    }

}
