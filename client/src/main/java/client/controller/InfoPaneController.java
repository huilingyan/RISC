package client.controller;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import shared.Map;
import shared.PlayerStat;

public class InfoPaneController implements PaneController {
  private Map worldmap;

  public InfoPaneController(Map m) {
    this.worldmap = m;
  }

  @Override
  public AnchorPane getCurrPane() {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.add(new Text("territory color"),0,0);
    grid.add(new Text("player name"),1,0);
    grid.add(new Text("number of territories"),2,0);
    grid.add(new Text("food"),3,0);
    grid.add(new Text("gold"),4,0);
    grid.add(new Text("max tech"),5,0);
    
    ArrayList<PlayerStat> psList = worldmap.getPlayerStats();
    for (int i = 0; i < psList.size(); i++) {
      int row = i + 1;
      PlayerStat ps = psList.get(i);
      Button btn = new Button();
      btn.setStyle("-fx-background-color: #"+ps.getColor());
      grid.add(btn, 0, row);
      grid.add(new Text(ps.getPName()),1,row);
      grid.add(new Text(Integer.toString(ps.getTerritoryNum())),2,row);
      grid.add(new Text(Integer.toString(ps.getFood())),3,row);
      grid.add(new Text(Integer.toString(ps.getGold())),4,row);
      grid.add(new Text(Integer.toString(ps.getMaxTechLvl())),5,row);
    }

    AnchorPane anchorP = new AnchorPane(grid);
      anchorP.setTopAnchor(grid, 10.0);
      anchorP.setBottomAnchor(grid, 10.0);
      anchorP.setLeftAnchor(grid, 10.0);
      anchorP.setRightAnchor(grid, 10.0);
      return anchorP;
  }


}
