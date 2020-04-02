package client;

import java.util.ArrayList;

import javafx.scene.control.Slider;
import shared.Army;

public class ArmySlider {
  private ArrayList<Slider> armysliders;
  public ArmySlider(Army army) {
    armysliders = new ArrayList<Slider>();
    
  }

  public Slider signleSlider(int n) {
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
