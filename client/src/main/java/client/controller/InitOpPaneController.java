package client.controller;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class InitOpPaneController implements PaneController{
  public InitController ic;

  public void setInitController(InitController initC) {
    this.ic = initC;
  }
  
	@Override
  public AnchorPane getCurrPane() {
    GridPane grid=new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    
    Text T_info = new Text("You have 10 units left to deploy, how many you want to put in charmandar");
    Slider NofArmySlider = new Slider(0, 10, 0);
    NofArmySlider.setShowTickLabels(true);
    NofArmySlider.setShowTickMarks(true);
    NofArmySlider.setBlockIncrement(1);
    NofArmySlider.setMajorTickUnit(1);
    NofArmySlider.setMinorTickCount(0);
    NofArmySlider.setSnapToTicks(true);
    Button proceedBtn = new Button("Proceed");
    Button cancelBtn = new Button("Cancel");
    ButtonBar BtnBar = new ButtonBar();
    BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
    grid.addRow(0, T_info);
    grid.addRow(1, NofArmySlider);
    grid.add(BtnBar, 0, 2, 2, 1);
    GridPane.setHalignment(BtnBar, HPos.CENTER);
    proceedBtn.setOnAction(e->{
        System.out.println((int)NofArmySlider.getValue());
      });

    AnchorPane anchorP = new AnchorPane(grid);
    anchorP.setTopAnchor(grid, 10.0);
    anchorP.setBottomAnchor(grid, 10.0);
    anchorP.setLeftAnchor(grid, 10.0);
    anchorP.setRightAnchor(grid, 10.0);
		return anchorP;
	}

}
