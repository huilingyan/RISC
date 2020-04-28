package client.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.OperationValidator;


public class ErrorAlerts {

    /********* login scene **********/
    public static void invalidLogin() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid Login");
        alert.setContentText("Invalid username or password! Please try again.");

        alert.showAndWait();
    }

    public static void bothFieldNullAlert() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid Login");
        alert.setContentText("Username or password can't be empty! Please try again.");

        alert.showAndWait();
    }

    /********* signup scene **********/
    public static void invalidUsername() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid Register");
        alert.setContentText("Username already exists! Please try again.");

        alert.showAndWait();
    }

    public static void RgstfieldNullAlert() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid Register");
        alert.setContentText("All fields should be non-empty! Please try again.");

        alert.showAndWait();
    }

    public static void diffPwdAlert() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid Password");
        alert.setContentText("Two passwords must the same! Please try again.");

        alert.showAndWait();
    }

    /********* room scene **********/

    public static void nullRoomNumAlert() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Empty room number");
        alert.setContentText("Room number can't be empty! Please try again.");

        alert.showAndWait();
    }



    public static void invalidTypeAlert() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid Type");
        alert.setContentText("Please input an integer!");

        alert.showAndWait();
    }

    public static void invalidRoom(int roomNum) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Room unavailable");
        alert.setContentText("Room " + roomNum + " doesn't exist! Please choose another room.");

        alert.showAndWait();
    }

    public static void InvalidPlayerNum() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Invalid player number");
        alert.setContentText("Player number must be between 2 and 5! Please input again.");

        alert.showAndWait();
    }

    public static void deployArmyPrompt() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Please deploy");

        alert.setHeaderText(null);
        alert.setContentText("Please click on the territory to deploy army");
 
        alert.showAndWait();
    }

    private static final Map<Integer, String> ecodetomsgMap = ImmutableMap.<Integer, String>builder()
      .put(OperationValidator.NOT_ENOUGH_FOOD, "You don't have enough food to do this!")
      .put(OperationValidator.NOT_ENOUGH_GOLD, "You don't have enough gold to upgrade!")
      .put(OperationValidator.NOT_ADJACENT, "You can only attack territories adjacent to this one!")
      .put(OperationValidator.INVALID_PATH, "There is no path to the destination you choose!")
      .put(OperationValidator.REPEATED_UPGRADE_MAX_TECH_LV, "You can't upgrade max tech Lv more than once in one turn!")
      .put(OperationValidator.EXCEED_MAX_LV, "Your upgrade cannot exceed your maximum tech Lv!")
      .put(OperationValidator.PLAYER_ALREADY_ALLIED,"You are already allied with someone!")
      .put(OperationValidator.REPEATED_ALLIANCE_REQUEST,"You can only request ally once in a turn!")
      .put(OperationValidator.BREAKING_ALLIANCE,"You broke the alliance!")
      .build();

    public static void inValidOpAlert(int errorcode) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Cannot proceed");
        window.setMaxWidth(450);

        Label l = new Label();
        if (ecodetomsgMap.containsKey(errorcode)) {
            l.setText(ecodetomsgMap.get(errorcode));
        }
        else {
            l.setText("Error code:"+errorcode);
        }

        Button closeBtn = new Button("OK");
        closeBtn.setOnAction(e->window.close());

        VBox vb = new VBox();
        vb.setSpacing(10);
        vb.getChildren().addAll(l, closeBtn);
        vb.setAlignment(Pos.CENTER);

        Scene sc = new Scene(vb);
        window.setScene(sc);
        window.showAndWait();
    }

    public static void upgradeTechSucceed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");

        alert.setHeaderText(null);
        alert.setContentText("Maximum Tech Level upgraded successfully!");
 
        alert.showAndWait();
    }

    public static void WaitForOtherPlayers() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Please Wait");

        alert.setHeaderText(null);
        alert.setContentText("Please wait for other players...");
 
        alert.showAndWait();
    }

    public static void WaitForJoin() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Please Wait");

        alert.setHeaderText(null);
        alert.setContentText("Please wait for other players to join...");
 
        alert.showAndWait();
    }

    public static void mustChooseAPlayer() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Please choose a player");
        alert.setContentText("You must choose a player to send message to! Please try again:");

        alert.showAndWait();
    }

    public static void sendEmptyMsg() {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Please write something");
        alert.setContentText("You can't send an empty message! Please input some words:");

        alert.showAndWait();
    }

}
