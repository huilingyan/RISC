package client.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import shared.ClientMessage;
import shared.GameMessage;
import shared.Map;
import shared.OperationValidator;
import shared.RoomMessage;
import shared.ServerMessage;
import shared.Territory;

public class GameController extends SceneController {

    public MainController mc;
    // fields
    private BorderPane root;
    private boolean ismoved;
    // models
    private Map worldmap;//the map passed in, 
    private int masterpid;

    private String player_name;
    private int room_num; // id of current room
    private OperationValidator ov;
    
    // constructor
    public GameController(Map m, String pname, int room_num, int pid) {
        this.worldmap = m;
        this.root = new BorderPane();
        this.ismoved = false;
        this.player_name = pname;
        this.room_num = room_num;
        setMaster(pid);
    }

    public void setMainController(MainController mainC) {
        this.mc = mainC;
    }

    public void setMaster(int pid) {
        this.masterpid = pid;
        this.ov = new OperationValidator(pid, this.worldmap);
    }

    public OperationValidator getOperationValidator() {
        return ov;
    }

    public Map getWorldmap() {
      return this.ov.getCurrentMapState();
    }

    public int getRoomNum() {
        return this.room_num;
    }

    public String getPlayerName() {
        return this.player_name;
    }

    public int getPid() {
        return masterpid;
    }

    public boolean isMoved() {
        return ismoved;
    }

    public void moved() {
      this.ismoved = true;
    }
  
    @Override
    public Scene getCurrScene() {

        setMaster(this.masterpid);
        boolean activate = false;
        if (this.worldmap.getPlayerStatByName(this.player_name).hasTerritory()) {
            activate = CardAlertBox.cardSelection(getWorldmap().getPlayerStatByPid(masterpid).getNewCard());
        }
        if (activate) {
          int errorcode=this.getOperationValidator().isValidCardUsage();
        }
        
        root.setPadding(new Insets(10, 10, 10, 10));

        // set top
        Label l = new Label("Room " + this.getRoomNum() + "  " + this.getPlayerName());
        root.setTop(l);
        l.setStyle("-fx-font: 24 arial;");
        BorderPane.setMargin(l, new Insets(10, 10, 10, 10));
        BorderPane.setAlignment(l, Pos.CENTER);

        // set left
        Pane leftpane = new Pane();
        leftpane.setPadding(new Insets(10, 10, 10, 10));
        root.setLeft(leftpane);

        Group buttongroup = generateMap();
        leftpane.getChildren().add(buttongroup);
        leftpane.setStyle("-fx-background-color: #d0d0d0;");

        // set right
        AnchorPane rightpane = new InfoPaneController(this.getWorldmap(),this.getPid()).getCurrPane();
        root.setRight(rightpane);
        BorderPane.setMargin(rightpane, new Insets(10, 10, 10, 10));

        // set bottom
        Button switchoutbtn = new Button("Switch out");
        switchoutbtn.setStyle("-fx-font-weight: bold; -fx-background-color: #ff7575;");
        switchoutbtn.setOnAction(e -> {
            this.mc.switchoutMsg(); // send switchout message to server
            this.mc.endChatClient();
            this.mc.closeChatWindow();
            RoomMessage room_msg = (RoomMessage)this.mc.recvFromServer();           
            this.mc.showRoomScene(room_msg);            
        });
        Button upgradeMaxTechbtn = new Button("Upgrade Max Tech Lv");
        if (!this.worldmap.getPlayerStatByName(this.player_name).hasTerritory()) {
            upgradeMaxTechbtn.setDisable(true);
        }
        upgradeMaxTechbtn.setOnAction(e -> {

            int errorcode = this.ov.isValidUpgradeMaxTechLv();
            if (errorcode == OperationValidator.VALID) {
                ErrorAlerts.upgradeTechSucceed(); // pop up alert box
            }
            else {
                ErrorAlerts.inValidOpAlert(errorcode);
            }

        });

        Button allyBtn = new Button("Make an ally");
        allyBtn.setOnAction(e->showAllyPane());
        if (!this.worldmap.getPlayerStatByName(this.player_name).hasTerritory()) {
            allyBtn.setDisable(true);
        }
        
        Button endTurnbtn = new Button("End Turn");
        endTurnbtn.setOnAction(e -> {
            this.mc.sendToServer(new ClientMessage(this.room_num, 2, this.ov.getAction())); // commit order
            ErrorAlerts.WaitForOtherPlayers();
            ServerMessage servermsg = (ServerMessage)this.mc.recvFromServer();
            this.mc.setWorldMap(servermsg.getMap());
            
            if (servermsg.getStage() == GameMessage.GAME_OVER) { // if game over
                this.mc.gameOverAlertBox(this.player_name, servermsg);
            }
            else if (servermsg.getStage() == GameMessage.GAME_PLAY) { // if game play
                if (!servermsg.getMap().getPlayerStatByName(this.player_name).hasTerritory()) { // if lost during game
                    this.mc.showLoserBox(this.player_name, servermsg);
                }
                else {
                    int pid = servermsg.getMap().getPidByName(this.player_name);
                    int room_number = servermsg.getGameID();
                    this.mc.showGameScene(room_number, pid);
                }               
            }
            else {
                System.out.println("Unexpected game stage!");
            }
                               
        });

        FlowPane bottompane = new FlowPane(switchoutbtn, upgradeMaxTechbtn,allyBtn, endTurnbtn);
        bottompane.setHgap(5); 
        root.setBottom(bottompane);
        bottompane.setPadding(new Insets(10, 10, 10, 10));       
        BorderPane.setMargin(bottompane, new Insets(10, 10, 10, 10));
        BorderPane.setAlignment(bottompane, Pos.TOP_LEFT);
  
        // set scene
        Scene mapscene = new Scene(root, 1280, 720);

        return mapscene;

    }

    private Group generateMap() {
        Group buttongroup = new Group();
        int init_x = 50;
        int init_y = 50;

        for (int i = 0; i < Territory.MAP_SIZE; i++) {
            String t_name = this.worldmap.getTerritoryNameByTid(i);
            if (t_name != null) {
                // // debug
                // System.out.println(" tid: " + i + " name: " + t_name);
                Button button = new Button(t_name);
                // get the button colour according to player
                int pid = this.worldmap.getTerritoryByName(t_name).getOwnership();
                // System.out.println("pid: " + pid);
                String color = this.worldmap.getPlayerStatByPid(pid).getColor();

                button.setPrefWidth(100);
                button.setPrefHeight(100);
                button.setLayoutX(init_x + 75 * (i / 4));
                button.setLayoutY(init_y + 100 * (i % 4) + ((i % 8 > 3)? 50 : 0));
                button.setStyle("-fx-shape: \"M 700 450 L 625 325 L 700 200 L 850 200 L 925 325 L 850 450 Z\"; " 
                                + "-fx-background-color: #" + color + ";");
                button.setOnAction(e -> {
                    showModePane(t_name);
                });
                buttongroup.getChildren().addAll(button);
            }
        }
        return buttongroup;
    }

    public void showAllyPane() {
      AllyPaneController aPC = new AllyPaneController();
      aPC.setGameController(this);
      updateRightPane(aPC);
    }

    public void showModePane(String t_name) {
        ModeSelectPaneController msPC = new ModeSelectPaneController(t_name);
        msPC.setGameController(this);
        updateRightPane(msPC);
    }

    public void showMovePane(String t_name) {
        MoveOPPaneController mopPC = new MoveOPPaneController(t_name);
        mopPC.setGameController(this);
        updateRightPane(mopPC);
    }

    public void showAtkPane(String t_name) {
        AtkOPPaneController aopPC=new AtkOPPaneController(t_name);
        aopPC.setGameController(this);
        updateRightPane(aopPC);
    }

    public void showUpgradePane(String t_name) {
        UpOPPaneController uopPC = new UpOPPaneController(t_name);
        uopPC.setGameController(this);
        updateRightPane(uopPC);
    }

    public void showInfoPane() {
      updateRightPane(new InfoPaneController(this.getWorldmap(),this.getPid()));
    }

    public void showWaitPane() {
        updateRightPane(new WaitPaneController());
    }

    public void updateRightPane(PaneController pc) {
        root.setRight(pc.getCurrPane());
    }
    
}
