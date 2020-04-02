package client.controller;

import client.InfoLayoutGenerator;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import shared.MapGenerator;
import shared.Territory;

public class ModeSelectPaneController implements PaneController {
  public GameController gc;
  private String terrName;

  public void setGameController(GameController gameC) {
    this.gc = gameC;
  }

  public ModeSelectPaneController(String t_name) {
      terrName = t_name;
    }
  
	@Override
	public AnchorPane getCurrPane() {
      Territory terr = MapGenerator.gamemapGenerator().getTerritories().get(0);
      boolean showmodeBtn = true;
      boolean hasmoved = false;

      GridPane t_textGridPane = InfoLayoutGenerator.generateTerritoryText(terr);//text info about this territory

      Button upgradeBtn = new Button("Upgrade");
      Button moveBtn = new Button("Move");
      Button atkBtn = new Button("Attack");
      Button cancelBtn = new Button("Cancel");
      upgradeBtn.setOnAction(e -> {
        System.out.println("upgrade");
      });

      moveBtn.setOnAction(e -> {
        System.out.println("move");
      });

      atkBtn.setOnAction(e -> {
        System.out.println("attack");
      });

      VBox vb = new VBox();
      //TilePane vb = new TilePane();
      vb.setSpacing(10);
      //vb.setPrefColumns(0);
      vb.getChildren().add(t_textGridPane);
      vb.setAlignment(Pos.CENTER);
      if (showmodeBtn) {
        if (hasmoved == false) {
          vb.getChildren().add(upgradeBtn);
        }
        vb.getChildren().addAll(moveBtn, atkBtn);
      }
      vb.getChildren().add(cancelBtn);
      
      AnchorPane anchorP = new AnchorPane(vb);
      AnchorPane.setTopAnchor(vb, 10.0);
      AnchorPane.setBottomAnchor(vb, 10.0);
      AnchorPane.setLeftAnchor(vb, 10.0);
      AnchorPane.setRightAnchor(vb, 10.0);

      return anchorP;
	}

}
