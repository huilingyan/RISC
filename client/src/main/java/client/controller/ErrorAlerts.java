package client.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

}