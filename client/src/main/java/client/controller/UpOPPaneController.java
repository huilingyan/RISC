package client.controller;

import java.util.ArrayList;

import client.ArmySliderPlusLvSel;
import client.InfoLayoutGenerator;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import shared.Army;
import shared.OperationValidator;
import shared.PlayerStat;
import shared.Territory;
import shared.UpgradeOperation;

public class UpOPPaneController implements PaneController {
    public GameController gc;
    private String terrName;

    public void setGameController(GameController gameC) {
        this.gc = gameC;
    }

    public UpOPPaneController(String t_name) {
        terrName = t_name;
    }

    @Override
    public AnchorPane getCurrPane() {
        Territory terr = this.gc.getWorldmap().getTerritoryByName(terrName);
        PlayerStat masterPS = gc.getWorldmap().getPlayerStatByPid(gc.getPid());
        
        GridPane costTable = InfoLayoutGenerator.generateUpgradeTable();
        Text selectUpgrade = new Text("Move the slider to select number of soldiers and pick the target level you want to upgrade them to.");
        Army tempArmy = new Army(0);
        if (gc.getWorldmap().ownerstatus(terr, masterPS) ==0) {
          tempArmy.joinArmy(terr.getDefender());
        }
        else {
          tempArmy.joinArmy(terr.getFriendDefender());
        }
        ArmySliderPlusLvSel amsld = new ArmySliderPlusLvSel(tempArmy);
        Button proceedBtn = new Button("Proceed");
        Button cancelBtn = new Button("Cancel");
        ButtonBar BtnBar = new ButtonBar();
        BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
        proceedBtn.setOnAction(e -> {
            System.out.println(amsld.getTargetLv());
            Army oldArmy = amsld.getArmy();
            Army newArmy = upgradeArmy(oldArmy, amsld.getTargetLv());
            UpgradeOperation uop = new UpgradeOperation(terrName, oldArmy, newArmy);
            int errorcode = this.gc.getOperationValidator().isValidUpgradeOperation(uop);
            if (errorcode == OperationValidator.VALID) {
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
        vb.getChildren().addAll(costTable, selectUpgrade, amsld.getUpgradeSelectionPane(), BtnBar);

        AnchorPane anchorP = new AnchorPane(vb);
        AnchorPane.setTopAnchor(vb, 10.0);
        AnchorPane.setBottomAnchor(vb, 10.0);
        AnchorPane.setLeftAnchor(vb, 10.0);
        AnchorPane.setRightAnchor(vb, 10.0);

        return anchorP;
    }

    public Army upgradeArmy(Army oldarmy, ArrayList<Integer> targetLv) {
        Army newarmy = new Army();
        for (int i = 0; i < targetLv.size(); i++) {
            newarmy.addSoldiers(targetLv.get(i), oldarmy.getSoldierNumber(i));
        }
        return newarmy;
    }
}
