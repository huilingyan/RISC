package client;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import shared.Army;
import shared.PlayerStat;
import shared.Territory;

public class InfoLayoutGenerator {

  public static GridPane generateTerritoryText(Territory t,PlayerStat ps) {
    //information to show when you click a territory will be used by infopane and modeselectpane
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.add(new Text("Name:"), 0, 0);
    grid.add(new Text("Owner:"), 0, 1);
    grid.add(new Text("Size:"), 0, 2);
    grid.add(new Text("Food Production:"), 0, 3);
    grid.add(new Text("Gold Porduction:"), 0, 4);
    for (int i = 0; i < 7; i++) {
      grid.add(new Text("Lv"+i+" soldiers:"), 0, i+5);
    }

    Army army = new Army(0);
    army.joinArmy(t.getDefender());
    army.joinArmy(t.getFriendDefender());
    
    grid.add(new Text(t.getName()), 1, 0);
    grid.add(new Text(ps.getPName()), 1, 1);
    grid.add(new Text(Integer.toString(t.getSize())), 1, 2);
    grid.add(new Text(Integer.toString(t.getFood())), 1, 3);
    grid.add(new Text(Integer.toString(t.getGold())), 1, 4);
    for (int i = 0; i < 7; i++) {
      grid.add(new Text(Integer.toString(army.getSoldierNumber(i))), 1, i+5);
    }
    return grid;
  }

  public static GridPane generateUpgradeTable() {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    grid.addRow(0,new Text("Each unit upgrade will cost the following gold:"));
    for (int i = 0; i < 6; i++) {
      grid.add(new Text("Lv"+i+" to "+(i+1)), i+1, 1);
    }
    grid.add(new Text("3"), 1, 2);
    grid.add(new Text("8"), 2, 2);
    grid.add(new Text("19"), 3, 2);
    grid.add(new Text("25"), 4, 2);
    grid.add(new Text("35"), 5, 2);
    grid.add(new Text("50"), 6, 2);

    return grid;
  }
  
}
