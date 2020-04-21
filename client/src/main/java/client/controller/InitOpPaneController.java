package client.controller;

import client.InfoLayoutGenerator;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import shared.*;
import java.io.*;

public class InitOpPaneController implements PaneController {
    public InitController ic;
    private String terrName;

    public void setInitController(InitController initC) {
      this.ic = initC;

    }

    public InitOpPaneController(String t_name) {
      terrName = t_name;
    }
    
    @Override
    public AnchorPane getCurrPane() {
        Territory terr = ic.getWorldmap().getTerritoryByName(terrName);
        PlayerStat ownerPS = ic.getWorldmap().getPlayerStatByPid(terr.getOwnership());
        boolean showiop = (terr.getOwnership() == ic.getPid()); // decide if show the slider
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // slider for chosing soldier number
        Text iop_info = new Text("You have " + ic.getnofSoldiers() + " units left to deploy, how many you want to put in " + terrName);
        Slider NofArmySlider = new Slider(0, ic.getnofSoldiers(), 0);
        NofArmySlider.setShowTickLabels(true);
        NofArmySlider.setShowTickMarks(true);
        NofArmySlider.setBlockIncrement(1);
        NofArmySlider.setMajorTickUnit(1);
        NofArmySlider.setMinorTickCount(0);
        NofArmySlider.setSnapToTicks(true);
        // buttons for proceed or cancel
        Button proceedBtn = new Button("Proceed");
        Button cancelBtn = new Button("Cancel");
        ButtonBar BtnBar = new ButtonBar();
        BtnBar.getButtons().addAll(proceedBtn, cancelBtn);
        grid.addRow(0, iop_info);
        grid.addRow(1, NofArmySlider);
        grid.add(BtnBar, 0, 2, 2, 1);
        GridPane.setHalignment(BtnBar, HPos.CENTER);
        // control button actions
        proceedBtn.setOnAction(e -> {
            int n = (int) NofArmySlider.getValue();
            // debug
            // System.out.println("soldiers to deploy: " + n);
            Army army = new Army(n);
            // validate operation
            InitOperation iop = new InitOperation(terrName, army);
            // debug
            // System.out.println("Current soldier number: " + this.ic.getnofSoldiers());
            int errorcode = this.ic.getOperationValidator().isValidInitOperation(iop, Map.INIT_UNIT);
            // int errorcode = this.ic.getOperationValidator().isValidInitOperation(iop, this.ic.getnofSoldiers());
            if (errorcode == OperationValidator.VALID) {
                this.ic.subSoldiers(n);
                // debug
                // System.out.println("Now have soldiers: " + this.ic.getnofSoldiers());
                this.ic.showInfoPane();
            }
            else {
                ErrorAlerts.inValidOpAlert(errorcode);
            }
            
        });
        cancelBtn.setOnAction(e -> this.ic.showInfoPane());
        
        GridPane t_textGridPane = InfoLayoutGenerator.generateTerritoryText(terr,ownerPS);//text info about this territory
        
        VBox vb = new VBox();
        //vb.setPadding(new Insets(10));
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        if (showiop) {
          vb.getChildren().addAll(t_textGridPane, grid);
        }
        else {
          vb.getChildren().addAll(t_textGridPane, cancelBtn);
        }
        
        AnchorPane anchorP = new AnchorPane(vb);
        AnchorPane.setTopAnchor(vb, 10.0);
        AnchorPane.setBottomAnchor(vb, 10.0);
        AnchorPane.setLeftAnchor(vb, 10.0);
        if (showiop) {
          AnchorPane.setRightAnchor(vb, 10.0);
        }
        else {
          AnchorPane.setRightAnchor(vb, 200.0);
        }
        
        return anchorP;
	  }

}
