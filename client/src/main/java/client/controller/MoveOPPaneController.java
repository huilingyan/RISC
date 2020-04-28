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
import shared.Army;
import shared.MoveOperation;
import shared.OperationValidator;
import shared.PlayerStat;
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
        Territory terr = this.gc.getWorldmap().getTerritoryByName(terrName);
        PlayerStat masterPS = gc.getWorldmap().getPlayerStatByPid(gc.getPid());

        Text costNotification = new Text("Move will cost food = the sum of territory sizes the path travelled including the destination\n x number of soldiers moved.");
        Text selectArmy = new Text("Move the slider to select how many soldiers you want to move:");
        Army tempArmy = new Army(0);
        if (gc.getWorldmap().ownerstatus(terr, masterPS) ==0) {
          tempArmy.joinArmy(terr.getDefender());
        }
        else {
          tempArmy.joinArmy(terr.getFriendDefender());
        }
        ArmySlider amsld = new ArmySlider(tempArmy);
        Text selectDest = new Text("Select the destination where the army will go:");
        ChoiceBox<String> chBox = new ChoiceBox<>();
        ArrayList<String> ownTnames = this.gc.getWorldmap().getOwnTerritoryListName(gc.getPid());
        chBox.getItems().addAll(ownTnames);
        chBox.setValue(ownTnames.get(0));

        Button proceedBtn = new Button("Proceed");
        Button cancelBtn = new Button("Cancel");
        ButtonBar BtnBar = new ButtonBar();
        BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
        proceedBtn.setOnAction(e -> {
            MoveOperation mop = new MoveOperation(terrName, chBox.getValue(), amsld.getArmy());
            int errorcode = this.gc.getOperationValidator().isValidMoveOperation(mop);
            if(errorcode == OperationValidator.VALID) {
                this.gc.moved();
                System.out.println(amsld.getArmy().getSoldierNumber(0));
                System.out.println(chBox.getValue());
                this.gc.showInfoPane();
            }
            else {
                ErrorAlerts.inValidOpAlert(errorcode);
            }
          });
        cancelBtn.setOnAction(e -> this.gc.showInfoPane());

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
