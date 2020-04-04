package client.controller;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import java.lang.String;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

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
        VBox room_list = new VBox();
        select_room.setLeft(room_list);
        BorderPane.setAlignment(select_room, Pos.CENTER);
        room_list.setPadding(new Insets(40, 20, 20, 20));
        room_list.setAlignment(Pos.TOP_CENTER);
        room_list.setSpacing(8);
        Label room_available = new Label("Available rooms:");
        room_available.setStyle("-fx-font: 18 arial; -fx-font-weight: bold;");
        room_list.getChildren().addAll(room_available);
        // list available rooms
        for (Room room : this.roomMsg.getRooms()) {
            if (!(room.isFull())) {
                Label room_id = new Label();
                room_id.setText(String.valueOf(room.getGid()));
                room_list.getChildren().addAll(room_id);
            }
        }
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
            /*
            this.mc.sendToServer(new ClientMessage(room_num.getText(), 0, new Action()));
            ServerMessage servermsg = (ServerMessage)this.mc.recvFromServer();
            if (servermsg.stage == 3) { // if game over
                this.mc.gameOverAlertBox(this.playername, servermsg);
            }
            else if (servermsg.stage == 1) { // waiting for players
                System.out.println("Unexpected game stage!");
            }
            else {
                this.mc.setWorldMap(servermsg.getMap()); 
                int pid = servermsg.getMap().getPidByName(this.player_name);
                int room_num = servermsg.gid;
                if (servermsg.stage == 1) { // initialize
                    this.mc.showInitScene(room_num, pid);
                }
                else if (servermsg.stage == 2) { // game play
                    this.mc.showGameScene(room_num, pid);
                }
            */
                // dummy model for test
                int pid = 0;
                this.mc.showInitScene(102, pid);
            /*
            }
            */
        });
        enter_room.getChildren().addAll(enter, input_room_num, room_num, enterbtn);

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
            // TODO： repeated code
            /*
            this.mc.sendToServer(new ClientMessage(player_num_field.getText(), 0, new Action()));
            ServerMessage servermsg = (ServerMessage)this.mc.recvFromServer();
            // TODO: how to deal with wait_for_player_mode
            else { 
                int pid = servermsg.getMap().getPidByName(this.player_name);
                int room_num = servermsg.gid;
                */
                // dummy model for test
                int pid = 0;
                this.mc.showInitScene(102, pid);
            /*
            }
            */
        });
        new_room_pane.getChildren().addAll(player_num_label, player_num_field, createbtn);

        // set scene
        Scene roomscene = new Scene(splitpane, 800, 480);

        return roomscene;
    }
    
}
