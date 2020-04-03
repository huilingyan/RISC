package client;

import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import shared.Army;

public class ArmySlider {
  public static final int totalLV = 7;
  private ArrayList<Slider> armysliders;
  private ArrayList<ChoiceBox<Integer>> chboxes;
  private GridPane sliderPane;
  private GridPane upgradeselectionPane;
  
  public ArmySlider(Army army) {
    armysliders = new ArrayList<Slider>();
    chboxes = new ArrayList<>();
    sliderPane = new GridPane();
    upgradeselectionPane = new GridPane();
    sliderPane.setHgap(10);
    sliderPane.setVgap(10);
    upgradeselectionPane.setHgap(10);
    upgradeselectionPane.setVgap(10);
    
    for (int i = 0; i < totalLV; i++) {
      Slider sld = singleSlider(army.getSoldierNumber(i));
      Text txt = new Text("Lv" + i);
      sliderPane.add(txt, 0, i);
      sliderPane.add(sld, 1, i);
      armysliders.add(sld);
      if (i < (totalLV - 1)) {
        upgradeselectionPane.add(txt, 0, i);
        upgradeselectionPane.add(sld, 1, i);
        ChoiceBox<Integer> chBox = singleChoiceBox(i);
        upgradeselectionPane.add(chBox, 2, i);
        chboxes.add(chBox);
      }
    }
  }

  public GridPane getArmySliders() {
    return sliderPane;
  }

  public GridPane getUpgradeSelectionPane() {
    return upgradeselectionPane;
  }

  public Army getArmy() {
    Army a = new Army();
    for (int i = 0; i < armysliders.size(); i++) {
      a.addSoldiers(i, (int) armysliders.get(i).getValue());
    }
    return a;
  }

  public ArrayList<Integer> getTargetLv() {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < chboxes.size(); i++) {
      list.add(chboxes.get(i).getValue());
    }
    return list;
  }

  public Slider singleSlider(int n) {
    Slider NofArmySlider = new Slider(0,n, 0);
    NofArmySlider.setShowTickLabels(true);
    NofArmySlider.setShowTickMarks(true);
    NofArmySlider.setBlockIncrement(1);
    NofArmySlider.setMajorTickUnit(1);
    NofArmySlider.setMinorTickCount(0);
    NofArmySlider.setSnapToTicks(true);
    return NofArmySlider;
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
