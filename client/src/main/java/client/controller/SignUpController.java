package client.controller;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import shared.*;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import java.io.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SignUpController extends SceneController {

    public MainController mc;

    @Override
    public void setMainController(MainController mainC) {
        this.mc = mainC;
    }

    @Override
    public Scene getCurrScene() {

        GridPane spane = new GridPane();
        spane.setPadding(new Insets(10, 10, 10, 10));
        spane.setVgap(8);
        spane.setHgap(10);

        // username label
        Label usernamelabel = new Label("Username:");
        GridPane.setConstraints(usernamelabel, 0, 0);

        // username box
        TextField userinput = new TextField();
        userinput.setPromptText("username");
        GridPane.setConstraints(userinput, 1, 0);

        // password label
        Label pwdlabel = new Label("Password:");
        GridPane.setConstraints(pwdlabel, 0, 1);

        // password box
        PasswordField pwdinput = new PasswordField();
        pwdinput.setPromptText("password");
        GridPane.setConstraints(pwdinput, 1, 1);

        // confirm password label
        Label confpwdlabel = new Label("Confirm password:");
        GridPane.setConstraints(confpwdlabel, 0, 2);

        // confirm password box
        PasswordField confpwdinput = new PasswordField();
        confpwdinput.setPromptText("password");
        GridPane.setConstraints(confpwdinput, 1, 2);
       
        // register
        Button registerbtn = new Button("Register");
        GridPane.setConstraints(registerbtn, 1, 3);
        registerbtn.setOnAction(e -> {
            // debug
            System.out.println("username: " + userinput.getText());
            System.out.println("password: " + pwdinput.getText());
            this.mc.sendToServer(new UserMessage(userinput.getText(), pwdinput.getText(), false));
            // TODO: check isValid from the received room message
            RoomMessage roomMsg = (RoomMessage)mc.recvFromServer();
            if (roomMsg.isValid()){
                this.mc.showLoginScene(); // return to the login scene
            } else {
                // TODO: 
                invalidUsername();  // pop out alert box
            }
            
        });

        spane.getChildren().addAll(usernamelabel, userinput, pwdlabel, pwdinput, confpwdlabel, confpwdinput, registerbtn);

        Scene signupscene = new Scene(spane, 350, 200);
        return signupscene;
        
    }

    public void invalidUsername() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid Register");
        alert.setContentText("Username already exists! Please try again.");

        alert.showAndWait();
    }
}