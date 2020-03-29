import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

public class RISC_GUI extends Application {

    @Override
    public void start(Stage stage) {

        Stage window = stage;

        // map scene
        Label l = new Label("This is the map page");
        Scene mapscene = new Scene(new StackPane(l), 640, 480);

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
            window.setTitle("Map");
            window.setScene(mapscene);
        }); // if login, change to the map scene

        Button signupbtn = new Button("Sign Up");
        GridPane.setConstraints(signupbtn, 1, 2);

        gpane.getChildren().addAll(usernamelabel, userinput, pwdlabel, pwdinput, loginbtn, signupbtn);

        // login scene
        Scene loginscene = new Scene(gpane, 300, 200);
        window.setTitle("Log in");
        window.setScene(loginscene);


        window.show();

    }

    public static void main(String[] args) {
        launch();
    }

}