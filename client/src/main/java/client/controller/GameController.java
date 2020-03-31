package client.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import shared.Map;

public class GameController extends SceneController {

    private Map worldmap;
    // private HashMap<Integer, String> territorylist = new HashMap<Integer, String>();

    // constructor
    public GameController(Map m) {
        this.worldmap = m;
    }

    @Override
    public Scene getCurrScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        // set top
        HBox player_info = new HBox();
        root.setTop(player_info);

        Label room_num = new Label("Room 103: ");
        room_num.setStyle("-fx-font: 24 arial;");
        player_info.getChildren().addAll(room_num);

        Label player_name = new Label("Ashley");
        player_name.setStyle("-fx-font: 24 arial;");
        player_info.getChildren().addAll(player_name);

        BorderPane.setMargin(player_name, new Insets(15, 15, 15, 15));
        BorderPane.setAlignment(player_name, Pos.TOP_CENTER);
        BorderPane.setMargin(room_num, new Insets(15, 15, 15, 15));
        BorderPane.setAlignment(room_num, Pos.TOP_LEFT);

        // set left
        Pane leftpane = new Pane();
        leftpane.setPadding(new Insets(10, 10, 10, 10));
        root.setLeft(leftpane);

        Group buttongroup = generateMap();
        leftpane.getChildren().add(buttongroup);
        leftpane.setStyle("-fx-background-color: #d0d0d0;");
  
        // set scene
        Scene mapscene = new Scene(root, 960, 720);

        return mapscene;

    }

    private Group generateMap() {

        // this.addTerritoryList();

        Group buttongroup = new Group();
        int init_x = 50;
        int init_y = 50;

        for (int i = 0; i < 9; i++) {
            Button button = new Button(this.worldmap.getTerritories().get(i).getName());
            button.setPrefWidth(100);
            button.setPrefHeight(100);
            button.setLayoutX(init_x + 75 * (i / 4));
            button.setLayoutY(init_y + 100 * (i % 4) + ((i % 8 > 3)? 50 : 0));
            button.setStyle("-fx-shape: \"M 700 450 L 625 325 L 700 200 L 850 200 L 925 325 L 850 450 Z\";");
            
            buttongroup.getChildren().addAll(button);
        }

        return buttongroup;
    }

    
}
