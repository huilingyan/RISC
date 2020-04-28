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
import shared.AttackOperation;
import shared.OperationValidator;
import shared.PlayerStat;
import shared.Territory;

public class AtkOPPaneController implements PaneController {
    public GameController gc;
    private String terrName;

    public void setGameController(GameController gameC) {
        this.gc = gameC;
    }

    public AtkOPPaneController(String t_name) {
        terrName = t_name;
    }

    @Override
    public AnchorPane getCurrPane() {
        Territory terr = gc.getWorldmap().getTerritoryByName(terrName);
        PlayerStat masterPS = gc.getWorldmap().getPlayerStatByPid(gc.getPid());

        Text costNotification = new Text("Attack will cost food = number of soldiers dispatched.");
        Text selectArmy = new Text("Move the slider to select how many soldiers you want to dispatch:");
        Army tempArmy = new Army(0);
        if (gc.getWorldmap().ownerstatus(terr, masterPS) ==0) {
          tempArmy.joinArmy(terr.getDefender());
        }
        else {
          tempArmy.joinArmy(terr.getFriendDefender());
        }
        ArmySlider amsld = new ArmySlider(tempArmy);
        Text selectDest = new Text("Select the destination where the army will attack:");
        ChoiceBox<String> chBox = new ChoiceBox<>();
        ArrayList<String> enermyTnames = gc.getWorldmap().getEnermyTerritoryListName(gc.getPid());
        chBox.getItems().addAll(enermyTnames);
        chBox.setValue(enermyTnames.get(0));

        Button proceedBtn = new Button("Proceed");
        Button cancelBtn = new Button("Cancel");
        ButtonBar BtnBar = new ButtonBar();
        BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
        proceedBtn.setOnAction(e -> {
            System.out.println(amsld.getArmy().getSoldierNumber(0));
            System.out.println(chBox.getValue());
            AttackOperation aop = new AttackOperation(terrName, chBox.getValue(), amsld.getArmy());
            int errorcode = gc.getOperationValidator().isValidAttackOperation(aop);
            if (errorcode == OperationValidator.VALID){
                gc.moved();
                gc.showInfoPane();
            }
            else {
              
                ErrorAlerts.inValidOpAlert(errorcode);
                if (errorcode == OperationValidator.BREAKING_ALLIANCE) {
                  gc.showInfoPane();
                }
            }
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
