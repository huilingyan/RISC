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

import shared.InitOperation;
import shared.*;

public class InitController extends SceneController {

    public MainController mc;
    // models
    public static final HashMap<Integer, String> TERRITORY_LIST = new HashMap<Integer, String>();
    private Map worldmap;
    private BorderPane root;
    private String player_name;
    private int room_num; // id of current room
    private int masterpid; // id of the player
    private int totalnofsoldiers;
    private Action action;
    
    // constructor
    public InitController(Map m, String pname, int room_num, int pid) {
      this.worldmap = m;
      this.totalnofsoldiers = 20;//hard coded, need adjust
      this.root = new BorderPane();
      this.action = new Action();
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
    }
  
    public Map getWorldmap() {
        return worldmap;
    }

    public int getPid() {
        return masterpid;
    }

    public int getnofSoldiers() {
        return totalnofsoldiers;
    }

    public void addInitOP(InitOperation iop) {
        action.addInitOperation(iop);
    }

    public void subSoldiers(int n) {
      //lose n soldiers due to initialization
        totalnofsoldiers -= n;
    }

    public Action getAction() {
        return this.action;
    }
  
  
    @Override
    public Scene getCurrScene() {
        // hard-coded master pid for test
        setMaster(0);
        root.setPadding(new Insets(10, 10, 10, 10));

        // set top
        Label l = new Label("Below is the map:");
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
        AnchorPane rightpane = new InfoPaneController(worldmap).getCurrPane();
        root.setRight(rightpane);
        BorderPane.setMargin(rightpane, new Insets(10, 10, 10, 10));
        
        // set bottom
        Button switchoutbtn = new Button("Switch out");
        switchoutbtn.setStyle("-fx-font-weight: bold;");
        switchoutbtn.setPadding(new Insets(5, 5, 5, 5));
        switchoutbtn.setOnAction(e -> {            
            this.mc.switchoutMsg(); // send switchout message to server
            RoomMessage room_msg = (RoomMessage)this.mc.recvFromServer();
            this.mc.showRoomScene(room_msg);
            
        });
        Button startgamebtn = new Button("Start Game");
        startgamebtn.setPadding(new Insets(5, 5, 5, 5));
        startgamebtn.setOnAction(e -> {
            
            this.mc.sendToServer(new ClientMessage(this.room_num, 1, this.action)); // initialize units
            ServerMessage servermsg = (ServerMessage)this.mc.recvFromServer();
            if ((servermsg.getStage() == 0) || (servermsg.getStage() == 1) || (servermsg.getStage() == 3)) {
                System.out.println("Unexpected game stage!");
            }
            this.mc.setWorldMap(servermsg.getMap()); 
            int pid = servermsg.getMap().getPidByName(this.player_name);
            int room_num = servermsg.getGameID();
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

    private void addTerritoryList() {
        InitController.TERRITORY_LIST.put(0, "Pikachu");
        InitController.TERRITORY_LIST.put(1, "Ditto");
        InitController.TERRITORY_LIST.put(2, "Gengar");
        InitController.TERRITORY_LIST.put(3, "Eevee");

        InitController.TERRITORY_LIST.put(4, "Snorlax");
        InitController.TERRITORY_LIST.put(5, "Mew");
        InitController.TERRITORY_LIST.put(6, "Psyduck");
        InitController.TERRITORY_LIST.put(7, "Magneton");

        InitController.TERRITORY_LIST.put(8, "Vulpix");
        InitController.TERRITORY_LIST.put(9, "Jumpluff");
        InitController.TERRITORY_LIST.put(10, "Bulbasaur");
        InitController.TERRITORY_LIST.put(11, "Charmandar");

        InitController.TERRITORY_LIST.put(12, "Squirtle");
        InitController.TERRITORY_LIST.put(13, "Pidgey");
        InitController.TERRITORY_LIST.put(14, "Caterpie");
        InitController.TERRITORY_LIST.put(15, "Rattata");
    }

    private Group generateMap() {

        Group buttongroup = new Group();
        int init_x = 50;
        int init_y = 50;

        for (int i = 0; i < 9; i++) {
            // Button button = new Button(InitController.TERRITORY_LIST.get(i));
            String t_name = this.worldmap.getTerritories().get(i).getName();
            Button button = new Button(t_name);
            // get the button colour according to player
            int pid = this.worldmap.getTerritories().get(i).getOwnership();
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

        return buttongroup;
    }

    public void showInitOPPane(String t_name) {
        InitOpPaneController iopPC = new InitOpPaneController(t_name);
        iopPC.setInitController(this);
        updateRightPane(iopPC);
    }

    public void showInfoPane() {
        updateRightPane(new InfoPaneController(worldmap));
    }

    public void updateRightPane(PaneController pc) {
        root.setRight(pc.getCurrPane());
    }

}
