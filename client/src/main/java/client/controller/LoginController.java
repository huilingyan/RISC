package client.controller;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import java.io.*;

public class LoginController extends SceneController {

    public MainController mc;

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

        Button loginbtn = new Button("Log In");
        GridPane.setConstraints(loginbtn, 0, 2);
        loginbtn.setOnAction(e -> {
            String username = userinput.getText();
            String pwd = pwdinput.getText();
            // debug
            System.out.println("username: " + username);
            System.out.println("password: " + pwd);
            this.mc.showRoomScene();
        });


        Button signupbtn = new Button("Sign Up");
        GridPane.setConstraints(signupbtn, 1, 2);

        gpane.getChildren().addAll(usernamelabel, userinput, pwdlabel, pwdinput, loginbtn, signupbtn);

        Scene loginscene = new Scene(gpane, 300, 200);

        return loginscene;
    }

}
