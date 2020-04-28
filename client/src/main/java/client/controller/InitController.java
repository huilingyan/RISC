package client.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import java.util.HashMap;
import java.lang.Integer;
import java.lang.String;
import javafx.geometry.Pos;
import java.io.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import shared.InitOperation;
import shared.*;

public class InitController extends SceneController {

    public MainController mc;
    // models
    private Map worldmap;
    private BorderPane root;
    private String player_name;
    private int room_num; // id of current room
    private int masterpid; // id of the player
    private int totalnofsoldiers;
    private OperationValidator ov;
    
    // constructor
    public InitController(Map m, String pname, int room_num, int pid) {
      this.worldmap = m;
      this.totalnofsoldiers = Map.INIT_UNIT;
      this.root = new BorderPane();
      this.player_name = pname;
      this.room_num = room_num;
      this.masterpid = pid;
    }

    @Override
    public void setMainController(MainController mainC) {
        this.mc = mainC;
    }

    public void setMaster(int pid) {
        this.masterpid = pid;
        this.ov = new OperationValidator(pid, this.worldmap);
    }

    public OperationValidator getOperationValidator() {
        return this.ov;
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
        return this.masterpid;
    }

    public int getnofSoldiers() {
        return totalnofsoldiers;
    }


    public void subSoldiers(int n) {
      //lose n soldiers due to initialization
        totalnofsoldiers -= n;
    }
  
    @Override
    public Scene getCurrScene() {

        setMaster(this.masterpid);
        
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
        BorderPane.setMargin(leftpane, new Insets(10, 10, 10, 10));      
        Group buttongroup = generateMap(); //show map
        leftpane.getChildren().add(buttongroup); 
        leftpane.setStyle("-fx-background-color: #d0d0d0;");

        // set right
        AnchorPane rightpane = new InfoPaneController(this.getWorldmap(),getPid()).getCurrPane();
        root.setRight(rightpane);
        BorderPane.setMargin(rightpane, new Insets(10, 10, 10, 10));
        
        // set bottom
        Button switchoutbtn = new Button("Switch out");
        switchoutbtn.setStyle("-fx-font-weight: bold; -fx-background-color: #ff7575;");
        switchoutbtn.setPadding(new Insets(5, 5, 5, 5));
        switchoutbtn.setOnAction(e -> {            
            this.mc.switchoutMsg(); // send switchout message to server
            this.mc.endChatClient();
            this.mc.closeChatWindow();
            RoomMessage room_msg = (RoomMessage)this.mc.recvFromServer();
            this.mc.showRoomScene(room_msg);
            
        });
        Button startgamebtn = new Button("Start Game");
        startgamebtn.setPadding(new Insets(5, 5, 5, 5));
        startgamebtn.setOnAction(e -> { 
            this.mc.sendToServer(new ClientMessage(this.room_num, 1, this.ov.getAction())); // initialize units
            ErrorAlerts.WaitForOtherPlayers();
            ServerMessage servermsg = (ServerMessage)this.mc.recvFromServer();
            System.out.println("server message received");
            if ((servermsg.getStage() == 0) || (servermsg.getStage() == 1) || (servermsg.getStage() == 3)) {
                System.out.println("Unexpected game stage!");
            }
            this.mc.setWorldMap(servermsg.getMap()); 
            int pid = servermsg.getMap().getPidByName(this.player_name);
            int room_num = servermsg.getGameID();
            // debug
            System.out.println("chat msg sent from initcontroller");
            this.mc.showGameScene(room_num, pid);
            
        });
        AnchorPane bottompane = new AnchorPane(switchoutbtn, startgamebtn);
        root.setBottom(bottompane);
        AnchorPane.setLeftAnchor(switchoutbtn, 10.0);
        AnchorPane.setRightAnchor(startgamebtn, 10.0);
        BorderPane.setMargin(bottompane, new Insets(10, 10, 10, 10));
 
        // set scene
        Scene mapscene = new Scene(root, 960, 720);

        return mapscene;

    }

    private Group generateMap() {
        // // debug
        // for (Territory t : this.worldmap.getTerritories()) {
        //     System.out.println("Territory id: " + t.getTid());
        //     System.out.println("Territory name: " + t.getName());
        //     System.out.println("Belongs to: " + t.getOwnership());
        // }
        // System.out.println("");

        Group buttongroup = new Group();
        int init_x = 50;
        int init_y = 50;

        for (int i = 0; i < Territory.MAP_SIZE; i++) {
            String t_name = this.worldmap.getTerritoryNameByTid(i);
            if (t_name != null) {
                Button button = new Button(t_name);
                // get the button colour according to player
                int pid = this.worldmap.getTerritoryByName(t_name).getOwnership();
                String color = this.worldmap.getPlayerStatByPid(pid).getColor();

                button.setPrefWidth(100);
                button.setPrefHeight(100);
                button.setLayoutX(init_x + 75 * (i / 4));
                button.setLayoutY(init_y + 100 * (i % 4) + ((i % 8 > 3)? 50 : 0));
                button.setStyle("-fx-shape: \"M 700 450 L 625 325 L 700 200 L 850 200 L 925 325 L 850 450 Z\"; " 
                                + "-fx-background-color: #" + color + ";");
                button.setOnAction(e -> {
                    showInitOPPane(t_name);
                });
                buttongroup.getChildren().addAll(button);
            }
        }
        return buttongroup;
    }

    public void showInitOPPane(String t_name) {
        InitOpPaneController iopPC = new InitOpPaneController(t_name);
        iopPC.setInitController(this);
        updateRightPane(iopPC);
    }

    public void showInfoPane() {
      updateRightPane(new InfoPaneController(this.worldmap,getPid()));
    }

    public void showWaitPane() {
        updateRightPane(new WaitPaneController());
    }

    public void updateRightPane(PaneController pc) {
        root.setRight(pc.getCurrPane());
    }

}
