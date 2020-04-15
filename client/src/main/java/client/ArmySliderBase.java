package client;

import java.util.ArrayList;

import javafx.scene.control.Slider;
import shared.Army;

public class ArmySliderBase {
  public static final int totalLV = 7;
  public ArrayList<Slider> armysliders;

  public Army getArmy() {
    Army a = new Army();
    for (int i = 0; i < armysliders.size(); i++) {
      a.addSoldiers(i, (int) armysliders.get(i).getValue());
    }
    return a;
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
}
