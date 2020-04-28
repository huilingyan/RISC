package client.controller;

import client.InfoLayoutGenerator;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import shared.PlayerStat;
import shared.Territory;

public class ModeSelectPaneController implements PaneController {
    public GameController gc;
    private String terrName;

    public void setGameController(GameController gameC) {
        this.gc = gameC;
    }

    public ModeSelectPaneController(String t_name) {
        terrName = t_name;
    }
    
    @Override
    public AnchorPane getCurrPane() {
        Territory terr = gc.getWorldmap().getTerritoryByName(terrName);
        PlayerStat masterPS = gc.getWorldmap().getPlayerStatByPid(gc.getPid());
        PlayerStat ownerPS = gc.getWorldmap().getPlayerStatByPid(terr.getOwnership());
        int showmodeBtn = gc.getWorldmap().ownerstatus(terr, masterPS);//decide if show the selection buttons
        boolean hasmoved = gc.isMoved();//decide if show the upgrade button

        GridPane t_textGridPane = InfoLayoutGenerator.generateTerritoryText(terr,ownerPS);//text info about this territory

        Button upgradeBtn = new Button("Upgrade");
        Button moveBtn = new Button("Move");
        Button atkBtn = new Button("Attack");
        Button cancelBtn = new Button("Cancel");
        Text upgradeNotification = new Text("Please upgrade your army first before any move or attack orders.\nOnce moved or attacked, you can no longer upgrade your army.");
        upgradeBtn.setOnAction(e -> {
            System.out.println("upgrade");
            gc.showUpgradePane(terrName);
        });

        moveBtn.setOnAction(e -> {
            System.out.println("move");
            gc.showMovePane(terrName);
        });

        atkBtn.setOnAction(e -> {
            System.out.println("attack");
            gc.showAtkPane(terrName);
        });

        cancelBtn.setOnAction(e -> this.gc.showInfoPane());

        VBox vb = new VBox();
        //TilePane vb = new TilePane();
        vb.setSpacing(10);
        //vb.setPrefColumns(0);
        vb.getChildren().add(t_textGridPane);
        vb.setAlignment(Pos.CENTER);
        if (showmodeBtn==0) {
            if (hasmoved == false) {
                vb.getChildren().addAll(upgradeNotification,upgradeBtn);
            }
            vb.getChildren().addAll(moveBtn, atkBtn);
        }
        if (showmodeBtn == 1) {
          vb.getChildren().addAll(moveBtn);
        }
        vb.getChildren().add(cancelBtn);
        
        AnchorPane anchorP = new AnchorPane(vb);
        AnchorPane.setTopAnchor(vb, 10.0);
        AnchorPane.setBottomAnchor(vb, 10.0);
        AnchorPane.setLeftAnchor(vb, 10.0);
        AnchorPane.setRightAnchor(vb, 10.0);

        return anchorP;
    }
}
