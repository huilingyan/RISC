package client.controller;

import client.ArmySlider;
import client.InfoLayoutGenerator;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import shared.MapGenerator;
import shared.Territory;

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
    Territory terr = MapGenerator.gamemapGenerator().getTerritories().get(0);
    
    GridPane costTable = InfoLayoutGenerator.generateUpgradeTable();
    Text selectUpgrade = new Text("Move the slider to select number of soldiers and pick the target level you want to upgrade them to.");
    ArmySlider amsld = new ArmySlider(terr.getDefender());
    Button proceedBtn = new Button("Proceed");
    Button cancelBtn = new Button("Cancel");
    ButtonBar BtnBar = new ButtonBar();
    BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
    proceedBtn.setOnAction(e -> {
        System.out.println(amsld.getTargetLv());
    });

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

}
