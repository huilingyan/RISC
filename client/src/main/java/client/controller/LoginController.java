package client.controller;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import shared.RoomMessage;
import shared.UserMessage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import client.RoomMsgGenerator;

public class LoginController extends SceneController {

    public MainController mc;
    // Usermessage usrmsg;

    @Override
    public void setMainController(MainController mainC) {
        this.mc = mainC;
    }

    @Override
    public Scene getCurrScene() {

        GridPane gpane = new GridPane();
        gpane.setPadding(new Insets(10, 10, 10, 10));
        gpane.setVgap(8);
        gpane.setHgap(10);

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

        // set buttons
        Button loginbtn = new Button("Log In");
        Button signupbtn = new Button("Sign Up");
        GridPane.setConstraints(loginbtn, 0, 2);
        GridPane.setConstraints(signupbtn, 1, 2);

        gpane.getChildren().addAll(usernamelabel, userinput, pwdlabel, pwdinput, loginbtn, signupbtn);

        loginbtn.setOnAction(e -> {
            if ((!userinput.getText().isEmpty()) && (!pwdinput.getText().isEmpty())) {
                this.mc.setPlayerName(userinput.getText());
                this.mc.sendToServer(new UserMessage(userinput.getText(), pwdinput.getText(), true));
                RoomMessage room_msg = (RoomMessage) this.mc.recvFromServer();
                System.out.println("Login result: " + room_msg.isValid());
                if (room_msg.isValid()) {
                    // send init message to chatserver
                    this.mc.sendChatMessage(userinput.getText(), "", "Init msg from " + userinput.getText());
                    this.mc.showRoomScene(room_msg);
                } else {
                    ErrorAlerts.invalidLogin(); // pop up alert box
                }
            }
            else {
                // if either of the field is null
                ErrorAlerts.bothFieldNullAlert();
            }                    
        });

        signupbtn.setOnAction(e -> {
            this.mc.showSignupScene();
        });

        Scene loginscene = new Scene(gpane, 300, 200);
        return loginscene;
    }

}
