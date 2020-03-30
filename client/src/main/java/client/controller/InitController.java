package client.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

public class InitController extends SceneController {

    private HashMap<Integer, String> territorylist = new HashMap<Integer, String>();

    @Override
    public Scene getCurrScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        // set top
        Label l = new Label("Below is the map:");
        root.setTop(l);

        // set left
        Pane leftpane = new Pane();
        leftpane.setPadding(new Insets(10, 10, 10, 10));
        root.setLeft(leftpane);
        
        Group buttongroup = generateMap();
        leftpane.getChildren().add(buttongroup);

        root.setLeft(leftpane);
  
        // set scene
        Scene mapscene = new Scene(root, 960, 720);

        return mapscene;

    }

    private void addTerritoryList() {
        this.territorylist.put(0, "Pikachu");
        this.territorylist.put(1, "Ditto");
        this.territorylist.put(2, "Gengar");
        this.territorylist.put(3, "Eevee");

        this.territorylist.put(4, "Snorlax");
        this.territorylist.put(5, "Mew");
        this.territorylist.put(6, "Psyduck");
        this.territorylist.put(7, "Magneton");

        this.territorylist.put(8, "Vulpix");
        this.territorylist.put(9, "Jumpluff");
        this.territorylist.put(10, "Bulbasaur");
        this.territorylist.put(11, "Charmandar");

        this.territorylist.put(12, "Squirtle");
        this.territorylist.put(13, "Pidgey");
        this.territorylist.put(14, "Caterpie");
        this.territorylist.put(15, "Rattata");
    }

    private Group generateMap() {

        this.addTerritoryList();

        Group buttongroup = new Group();
        int init_x = 50;
        int init_y = 50;

        for (int i = 0; i < 16; i++) {
            Button button = new Button(this.territorylist.get(i));
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
