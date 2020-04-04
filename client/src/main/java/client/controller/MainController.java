package client.controller;

import javafx.stage.Stage;
import shared.*;
import shared.Map;
import shared.MapGenerator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
// import shared.Action;

public class MainController {

    private static final MainController INSTANCE = new MainController();
    private Stage window;
    // controller instances
    private LoginController loginController = new LoginController();
    private SignUpController signupController = new SignUpController();
    private RoomController roomController;
    private InitController initController;
    private GameController gameController;
    // model instances
    // TODO: add model map, validator
    // TODO: GameClient gclient;
    String player_name;

    Map worldmap = MapGenerator.initmapGenerator(); // dummy model

    public static MainController getInstance() {
        return INSTANCE;
    }

    public void setStage(Stage stage) {
        this.window = stage;
    }

    public void setPlayerName(String pname) {
        this.player_name = pname;
    }

    public void setWorldMap(Map m) {
        this.worldmap = m;
    }

    public void showLoginScene() {
        this.loginController.setMainController(this);
        updateCurrScene(this.loginController);
    }

    public void showSignupScene() {
        this.signupController.setMainController(this);
        updateCurrScene(this.signupController);
    }

    public void showRoomScene(RoomMessage rmsg) {
        this.roomController = new RoomController(this.player_name);
        this.roomController.setMainController(this);
        this.roomController.setRoomMessage(rmsg);
        updateCurrScene(this.roomController);
    }

    public void showInitScene(int room_num, int pid) {
        this.initController = new InitController(this.worldmap, this.player_name, room_num, pid);
        this.initController.setMainController(this);
        updateCurrScene(this.initController);
    }

    public void showGameScene(int room_num, int pid) {
        this.gameController = new GameController(this.worldmap, this.player_name, room_num, pid);
        this.gameController.setMainController(this);
        updateCurrScene(this.gameController);
        // window.setScene(gameController.getCurrScene());
    }

    public void updateCurrScene(SceneController sc) {
        window.setScene(sc.getCurrScene());
    }

    public void showStage() {
        window.show();
    }

    public void gameOverAlertBox(String pname, ServerMessage servermsg) {
        if (servermsg.getMap().getPlayerStatByName(pname).hasTerritory()) { // if has territory left, win
            showWinnerBox();
        }
        else {
            showLoserBox(pname, servermsg);
        }
    }

    public void showWinnerBox() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText("Congraulations! You won the game!");
 
        ButtonType exit = new ButtonType("Exit");
 
        alert.getButtonTypes().clear(); 
        alert.getButtonTypes().addAll(exit);
 
        // option != null.
        Optional<ButtonType> option = alert.showAndWait();
 
        if (option.get() == exit) {
            // this.gclient.sendSwitchoutMsg();
            // RoomMessage room_msg = (RoomMessage)recvFromServer();
            // dummy roommsg model for test
            // RoomMessage room_msg = RoomMsgGenerator.generateRooms();
            // this.mc.showRoomScene(room_msg);
        }
    }

    public void showLoserBox(String pname, ServerMessage servermsg) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("You lost! Please choose whether to exit or watch:");
 
        ButtonType exit = new ButtonType("Exit");
        ButtonType watch = new ButtonType("Watch the game");
 
        alert.getButtonTypes().clear(); 
        alert.getButtonTypes().addAll(exit, watch);
 
        Optional<ButtonType> option = alert.showAndWait();
 
        if (option.get() == exit) {
            // this.gclient.sendSwitchoutMsg();
            // RoomMessage room_msg = (RoomMessage)recvFromServer();
            // dummy roommsg model for test
            // RoomMessage room_msg = RoomMsgGenerator.generateRooms();
            // this.mc.showRoomScene(room_msg);
        } 
        else if (option.get() == watch) {
            // sendToServer(new ClientMessage(servermsg.gameId, servermsg.stage, new Action())); // watch the game
            // ServerMessage newservermsg = (ServerMessage)recvFromServer();
            // int pid = newservermsg.getMap().getPidByName(pname);
            // if (newservermsg.stage == 1) { // initialize
            //     showInitScene(newservermsg.getGameID(), pid);
            // }
            // else if (newservermsg.stage == 2) { // playing game
            //     showGameScene(newservermsg.getGameID(), pid);
            // }
        }

    }

    // TODO: umcomment the following methods after integrating gameclient

    // public void sendToServer(Object obj) {
    //     this.gclient.sendObject(obj);
    // }

    // public Object recvFromServer() {
    //     Object obj = this.gclient.recvObject();
    //     return obj;
    // }

    // public void setupIStream() {
    //     this.gclient.setUpInputStream();
    // }

    // public void switchoutMsg() {
    //     this.gclient.sendSwitchOutMsg();
    // }

}
