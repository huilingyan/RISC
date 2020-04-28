package client.controller;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import java.lang.String;
import java.lang.Integer;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.nio.channels.SocketChannel;

import shared.*;

public class RoomController extends SceneController {

    public MainController mc;
    // models
    private RoomMessage roomMsg;
    String playername;

    @Override
    public void setMainController(MainController mainC) {
        this.mc = mainC;
    }

    public void setRoomMessage(RoomMessage rmsg) {
        this.roomMsg = rmsg;
    }

    public RoomController(String pname) {
        this.playername = pname;
    }

    @Override
    public Scene getCurrScene() {

        BorderPane select_room = new BorderPane();
        BorderPane create_room = new BorderPane();

        SplitPane splitpane = new SplitPane();
        splitpane.getItems().addAll(select_room, create_room);
        splitpane.setDividerPositions(0.6);

        // set select_room pane
        // set top
        Label welcome = new Label("Welcome!");
        select_room.setTop(welcome);
        welcome.setStyle("-fx-font: 24 arial;");
        BorderPane.setMargin(welcome, new Insets(10, 10, 10, 10));
        select_room.setPadding(new Insets(10, 20, 40, 20));
        BorderPane.setAlignment(welcome, Pos.TOP_CENTER);

        // set left
        GridPane room_list = new GridPane();
        room_list.setHgap(10);
        room_list.setVgap(10);

        room_list.add(new Text("room#"),0,0);
        room_list.add(new Text("player#"),1,0);
        room_list.add(new Text("IsFull"),2,0);

        int row = 0;
        for (Room room : this.roomMsg.getRooms()) {
            row++;
            room_list.add(new Text(String.valueOf(room.getGid())), 0, row);
            room_list.add(new Text(String.valueOf(room.getPlayerNum())), 1, row);
            room_list.add(new Text(room.isFull()? "Full" : "Not Full"), 2, row);
        }

        select_room.setLeft(room_list);
        BorderPane.setAlignment(select_room, Pos.CENTER);
        room_list.setPadding(new Insets(40, 20, 20, 20));
        room_list.setAlignment(Pos.TOP_CENTER);

        // set right
        GridPane enter_room = new GridPane();
        enter_room.setHgap(10);
        enter_room.setVgap(20);
        select_room.setRight(enter_room);
        enter_room.setPadding(new Insets(40, 20, 20, 20));
        Label enter = new Label("Enter a room:");
        enter.setStyle("-fx-font: 18 arial; -fx-font-weight: bold;");
        GridPane.setConstraints(enter, 0, 0);
        Label input_room_num = new Label("Room number:");
        input_room_num.setStyle("-fx-font: 18 arial;");
        GridPane.setConstraints(input_room_num, 0, 1);
        TextField room_num = new TextField();
        GridPane.setConstraints(room_num, 0, 2);
        Button enterbtn = new Button("Join");
        GridPane.setConstraints(enterbtn, 0, 3);
        enterbtn.setOnAction(e -> {

            if (room_num.getText().isEmpty()) { // if empty input
                ErrorAlerts.nullRoomNumAlert();
            }
            else { // if not empty input
                try {
                    int roomNum = Integer.parseInt(room_num.getText());
                    if (!this.roomMsg.ifIsValidRoom(Integer.parseInt(room_num.getText()))) { // if room not exist
                        ErrorAlerts.invalidRoom(roomNum);
                    }
                    else {
                        this.mc.sendToServer(new ClientMessage(roomNum, 0, new Action()));
                        ErrorAlerts.WaitForJoin();
                        ServerMessage servermsg = (ServerMessage) this.mc.recvFromServer();
                        System.out.println("Game id is " + servermsg.getGameID());
                        this.mc.setWorldMap(servermsg.getMap());  // set map
                        int pid = servermsg.getMap().getPidByName(this.mc.getPlayerName());
                        int gid = servermsg.getGameID();
                        // create chatclient thread
                        // SocketChannel chatChannel = this.mc.getChatChannel();
                        this.mc.showChatBox();
                        this.mc.startChatClient(playername, this.mc);
                        // debug stage number
                        int stage = servermsg.getStage();
                        if (stage == GameMessage.INITIALIZE_UNITS) {
                            this.mc.showInitScene(gid, pid);
                            ErrorAlerts.deployArmyPrompt();
                        } 
                        else if (stage == GameMessage.GAME_PLAY) {   
                            this.mc.showGameScene(gid, pid);
                        } 
                        else if (stage == GameMessage.GAME_OVER) {
                            this.mc.gameOverAlertBox(this.playername, servermsg);
                        } 
                        else {
                            System.out.println("Stage number is " + stage);
                            System.out.println("Error: wrong game state");
                        }
                    }

                } catch (NumberFormatException ex) {
                    ErrorAlerts.invalidTypeAlert();
                }

            }
        });
        enter_room.getChildren().addAll(enter, input_room_num, room_num, enterbtn);

        // set bottom
        Button refreshbtn = new Button("Refresh");
        refreshbtn.setPadding(new Insets(5, 5, 5, 5));
        refreshbtn.setOnAction(e -> { // refresh room list           
            this.mc.switchoutMsg(); // send switchout message to server
            RoomMessage room_msg = (RoomMessage)this.mc.recvFromServer();
            this.mc.showRoomScene(room_msg);
            
        });
        select_room.setBottom(refreshbtn);
        BorderPane.setMargin(refreshbtn, new Insets(10, 10, 10, 10));

        // set create_room pane
        Label newroom = new Label("Create a new room:");
        create_room.setTop(newroom);
        newroom.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
        BorderPane.setMargin(newroom, new Insets(10, 10, 10, 10));
        BorderPane.setAlignment(newroom, Pos.CENTER);

        GridPane new_room_pane = new GridPane();
        new_room_pane.setHgap(10);
        new_room_pane.setVgap(20);
        create_room.setCenter(new_room_pane);
        new_room_pane.setPadding(new Insets(40, 10, 20, 30));
        BorderPane.setMargin(new_room_pane, new Insets(10, 10, 10, 10));
        Label player_num_label = new Label("Number of players:");
        player_num_label.setStyle("-fx-font: 18 arial;");
        GridPane.setConstraints(player_num_label, 0, 1);
        TextField player_num_field = new TextField();
        GridPane.setConstraints(player_num_field, 0, 2);
        Button createbtn = new Button("Create");
        GridPane.setConstraints(createbtn, 0, 3);
        createbtn.setOnAction(e -> {

            if (player_num_field.getText().isEmpty()) { // if empty input
                ErrorAlerts.nullRoomNumAlert();
            }
            else { // if not empty input
                try {
                    int roomNum = Integer.parseInt(player_num_field.getText());
                    if ((roomNum < 2) || (roomNum > 5)) { // if player number not between 2 to 5
                        ErrorAlerts.InvalidPlayerNum();
                    }
                    else {
                        this.mc.sendToServer(new ClientMessage(roomNum, 0, new Action()));
                        System.out.println("Sent client message");
                        ErrorAlerts.WaitForJoin();
                        // ErrorAlerts.deployArmyPrompt();
                        ServerMessage servermsg = (ServerMessage) this.mc.recvFromServer();
                        this.mc.setWorldMap(servermsg.getMap());
                        // create chatclient thread
                        // SocketChannel chatChannel = this.mc.getChatChannel();
                        this.mc.showChatBox();
                        this.mc.startChatClient(playername, this.mc);                        
                        // debug stage number
                        int stage = servermsg.getStage();
                        if (stage != GameMessage.INITIALIZE_UNITS) {
                            System.out.println("Stage number is " + stage);
                            System.out.println("Error: should be 1 (initialize units)");
                        }
                        // this.mc.setWorldMap(servermsg.getMap());
                        int pid = servermsg.getMap().getPidByName(this.mc.getPlayerName());
                        int gid = servermsg.getGameID();
                        this.mc.showInitScene(gid, pid);
                        
                    }

                } catch (NumberFormatException ex) {
                    ErrorAlerts.invalidTypeAlert();
                }
            }

        });
        new_room_pane.getChildren().addAll(player_num_label, player_num_field, createbtn);

        // set scene
        Scene roomscene = new Scene(splitpane, 800, 480);

        return roomscene;
    }



}
