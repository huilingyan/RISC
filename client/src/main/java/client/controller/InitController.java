package client.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

public class InitController extends SceneController {

    @Override
    public Scene getCurrScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        // set top
        Label l = new Label("Below is the map:");
        root.setTop(l);

        // set left
        // AnchorPane leftpane = new AnchorPane();
        // leftpane.setPadding(new Insets(10, 10, 10, 10));
        // root.setLeft(leftpane);

        Button button1 = new Button("Mew");
        button1.setPrefWidth(50);
        button1.setPrefHeight(50);
        button1.setStyle("-fx-shape: \"M 700 450 L 625 325 L 700 200 L 850 200 L 925 325 L 850 450 Z\";");
        // AnchorPane.setRightAnchor(button1, 10.0); // this code is no effect
        
        Button button2 = new Button("Ditto");
        button2.setPrefWidth(25);
        button2.setPrefHeight(25);
        button2.setStyle("-fx-shape: \"M 700 450 L 625 325 L 700 200 L 850 200 L 925 325 L 850 450 Z\";");
        
        Group buttongroup = new Group();
        buttongroup.getChildren().addAll(button1, button2);
        root.setLeft(buttongroup);

        // leftpane.getChildren().add(buttongroup);
    
        // set scene
        Scene mapscene = new Scene(root, 960, 720);

        return mapscene;

    }
}
