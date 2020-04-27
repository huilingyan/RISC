package client.controller;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import shared.OperationValidator;

public class AllyPaneController implements PaneController {
  public GameController gc;

   public void setGameController(GameController gameC) {
        this.gc = gameC;
    }
  
	@Override
	public AnchorPane getCurrPane() {
    Text allynotice = new Text("Please select the player you want to be allied with (You can only be allied with one player!)");
    ChoiceBox<String> chBox = new ChoiceBox<>();
    ArrayList<String> playnamelist = this.gc.getWorldmap().getOtherPlayerNames(gc.getPid());
    chBox.getItems().addAll(playnamelist);
    chBox.setValue(playnamelist.get(0));
    
    Button proceedBtn = new Button("Proceed");
    Button cancelBtn = new Button("Cancel");
    ButtonBar BtnBar = new ButtonBar();
    BtnBar.getButtons().addAll(proceedBtn, cancelBtn);

    proceedBtn.setOnAction(e -> {
        System.out.println(chBox.getValue());
        int topid = gc.getWorldmap().getPidByName(chBox.getValue());
        int errorcode = gc.getOperationValidator().isValidAllianceRequest(topid);
        if (errorcode == OperationValidator.VALID) {
          gc.showInfoPane();
        }
        else {
          ErrorAlerts.inValidOpAlert(errorcode);
        }
    });
    cancelBtn.setOnAction(e -> this.gc.showInfoPane());

    VBox vb = new VBox();
    vb.setAlignment(Pos.CENTER);
    vb.setSpacing(10);
    vb.getChildren().addAll(allynotice, chBox, BtnBar);

    AnchorPane anchorP = new AnchorPane(vb);
    AnchorPane.setTopAnchor(vb, 10.0);
    AnchorPane.setBottomAnchor(vb, 10.0);
    AnchorPane.setLeftAnchor(vb, 10.0);
    AnchorPane.setRightAnchor(vb, 10.0);

    return anchorP;
	}

}
