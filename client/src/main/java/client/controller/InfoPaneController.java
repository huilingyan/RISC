package client.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import shared.Map;
import shared.PlayerStat;

public class InfoPaneController implements PaneController {
  private Map worldmap;
  private PlayerStat ownerPS;
  private HashMap<Integer, String> cidNameMap;

  public InfoPaneController(Map m,int pid) {
    this.worldmap = m;
    this.ownerPS = m.getPlayerStatByPid(pid);
    this.cidNameMap = new HashMap<>();
    cidNameMap.put(1, "Portal");
    cidNameMap.put(2, "Communism");
    cidNameMap.put(3, "Technology Worship");
    cidNameMap.put(4, "Conscription");
    cidNameMap.put(5, "Silk Road");
    cidNameMap.put(6, "Loan");
  }

  @Override
  public AnchorPane getCurrPane() {
      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);

      grid.add(new Text("Territory color"),0,0);
      grid.add(new Text("Player name"),1,0);
      grid.add(new Text("Number of territories"),2,0);
      grid.add(new Text("Food"),3,0);
      grid.add(new Text("Gold"),4,0);
      grid.add(new Text("Max tech"),5,0);
      grid.add(new Text("Aliance ID"),6,0);
      
      ArrayList<PlayerStat> psList = this.worldmap.getPlayerStats();
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
          grid.add(new Text(Integer.toString(ps.getAid())),6,row);
      }

      int rows = psList.size();
      rows++;
      grid.addRow(rows, new Text("Your current activeted card:"));
      if (ownerPS.getActivatedCards().isEmpty()) {
        //no card is activated
        rows++;
        grid.addRow(rows, new Text("None."));
      }
      else {
        for (HashMap.Entry<Integer, Integer> entry : ownerPS.getActivatedCards().entrySet()) {
          rows++;
          String s = cidNameMap.get(entry.getKey()) + ":" + entry.getValue() + " turns";
          grid.addRow(rows, new Text(s));
        }
      }

      AnchorPane anchorP = new AnchorPane(grid);
      AnchorPane.setTopAnchor(grid, 10.0);
      AnchorPane.setBottomAnchor(grid, 10.0);
      AnchorPane.setLeftAnchor(grid, 10.0);
      AnchorPane.setRightAnchor(grid, 10.0);
      return anchorP;
  }


}
