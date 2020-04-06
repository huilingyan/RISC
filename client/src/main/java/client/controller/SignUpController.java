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

        // back to login scene
        Button backbtn = new Button("Back");
        GridPane.setConstraints(backbtn, 0, 3);
        backbtn.setOnAction(e -> {
            this.mc.showLoginScene(); // return to the login scene
        });
       
        // register
        Button registerbtn = new Button("Register");
        GridPane.setConstraints(registerbtn, 1, 3);
        registerbtn.setOnAction(e -> {

            if ((userinput.getText().isEmpty()) || (pwdinput.getText().isEmpty()) || (confpwdinput.getText().isEmpty())) { // if any of the field is empty
                ErrorAlerts.RgstfieldNullAlert();
            }
            else if (!pwdinput.getText().equals(confpwdinput.getText())) { // if two passwords aren't equal
                ErrorAlerts.diffPwdAlert();
            }
            else {
                // debug
                System.out.println("username: " + userinput.getText());
                System.out.println("password: " + pwdinput.getText());
                this.mc.sendToServer(new UserMessage(userinput.getText(), pwdinput.getText(), false));
                RoomMessage roomMsg = (RoomMessage)mc.recvFromServer();
                if (roomMsg.isValid()){
                    this.mc.showLoginScene(); // return to the login scene
                } else {
                    ErrorAlerts.invalidUsername();  // pop out alert box
                }
            }  

        });

        spane.getChildren().addAll(usernamelabel, userinput, pwdlabel, pwdinput, confpwdlabel, confpwdinput, backbtn, registerbtn);

        Scene signupscene = new Scene(spane, 350, 200);
        return signupscene;
        
    }

}