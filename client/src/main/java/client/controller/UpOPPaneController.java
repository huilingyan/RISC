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
        Territory terr = gc.getWorldmap().getTerritoryByName(terrName);
        
        GridPane costTable = InfoLayoutGenerator.generateUpgradeTable();
        Text selectUpgrade = new Text("Move the slider to select number of soldiers and pick the target level you want to upgrade them to.");
        ArmySliderPlusLvSel amsld = new ArmySliderPlusLvSel(terr.getDefender());
        Button proceedBtn = new Button("Proceed");
        Button cancelBtn = new Button("Cancel");
        ButtonBar BtnBar = new ButtonBar();
        BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
        proceedBtn.setOnAction(e -> {
            Army oldArmy = amsld.getArmy();
            Army newArmy = upgradeArmy(oldArmy, amsld.getTargetLv());
            gc.addUpOP(new UpgradeOperation(terrName, oldArmy, newArmy));
            System.out.println(amsld.getTargetLv());
            gc.showInfoPane();
        });
        cancelBtn.setOnAction(e -> gc.showInfoPane());

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
