package client;

import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import shared.Army;

public class ArmySlider extends ArmySliderBase {
  
    private GridPane sliderPane;
    
    public ArmySlider(Army army) {
        armysliders = new ArrayList<Slider>();
        sliderPane = new GridPane();
        sliderPane.setHgap(10);
        sliderPane.setVgap(10);
      
        for (int i = 0; i < totalLV; i++) {
            Slider sld = singleSlider(army.getSoldierNumber(i));
            Text txt = new Text("Lv" + i);
            sliderPane.add(txt, 0, i);
            sliderPane.add(sld, 1, i);
            armysliders.add(sld);

        }

    }

    public GridPane getArmySliders() {
        return sliderPane;
    }

}
