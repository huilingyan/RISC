package client;

import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import shared.Army;

public class ArmySliderPlusLvSel extends ArmySliderBase {
  private ArrayList<ChoiceBox<Integer>> chboxes;
  private GridPane upgradeselectionPane;
  
  public ArmySliderPlusLvSel(Army army) {
    armysliders = new ArrayList<Slider>();
    chboxes = new ArrayList<>();
    upgradeselectionPane = new GridPane();
    upgradeselectionPane.setHgap(10);
    upgradeselectionPane.setVgap(10);
    
    for (int i = 0; i < (totalLV-1); i++) {
      Slider sld = singleSlider(army.getSoldierNumber(i));
      Text txt = new Text("Lv" + i);
      armysliders.add(sld);
      upgradeselectionPane.add(txt, 0, i);
      upgradeselectionPane.add(sld, 1, i);
      Text txt2 = new Text("to Lv");
      upgradeselectionPane.add(txt2, 2, i);
      ChoiceBox<Integer> chBox = singleChoiceBox(i);
      upgradeselectionPane.add(chBox, 3, i);
      chboxes.add(chBox);
      
    }

  }


  public GridPane getUpgradeSelectionPane() {
    return upgradeselectionPane;
  }


  public ArrayList<Integer> getTargetLv() {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < chboxes.size(); i++) {
      list.add(chboxes.get(i).getValue());
    }
    return list;
  }


  public ChoiceBox<Integer> singleChoiceBox(int n) {
    ChoiceBox<Integer> chBox = new ChoiceBox<>();
    for (int i = n; i < totalLV; i++) {
      chBox.getItems().add(i);
    }
    chBox.setValue(n);
    return chBox;
  }
}
