package client.controller;

import javafx.stage.Stage;
import shared.*;
import shared.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
// import shared.Action;

import client.GameClient;
import client.Model;

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
    Model gamemodel = new Model();
    // GameClient gclient;
    // String player_name;

    // Map worldmap = MapGenerator.initmapGenerator(); // dummy model

    public void initializeSocketConnection() {
        this.getGameClient().connectToServer(); 
        this.getGameClient().setUpInputStream();
    }

    public static MainController getInstance() {
        return INSTANCE;
    }

    public GameClient getGameClient() {
        return this.gamemodel.gclient;
    }

    public String getPlayerName() {
        return this.gamemodel.player_name;
    }

    public Map getWorldMap() {
        return this.gamemodel.worldmap;
    }

    public void setStage(Stage stage) {
        this.window = stage;
    }

    public void setPlayerName(String pname) {
        this.gamemodel.player_name = pname;
    }

    public void setWorldMap(Map m) {
        this.gamemodel.worldmap = m;
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
        this.roomController = new RoomController(this.getPlayerName());
        this.roomController.setMainController(this);
        this.roomController.setRoomMessage(rmsg);
        updateCurrScene(this.roomController);
    }

    public void showInitScene(int room_num, int pid) {
        this.initController = new InitController(this.getWorldMap(), this.getPlayerName(), room_num, pid);
        this.initController.setMainController(this);
        updateCurrScene(this.initController);
    }

    public void showGameScene(int room_num, int pid) {
        this.gameController = new GameController(this.getWorldMap(), this.getPlayerName(), room_num, pid);
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
            showFinalLoserBox();
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
            this.switchoutMsg();
            RoomMessage room_msg = (RoomMessage)recvFromServer();
            showRoomScene(room_msg);
        }
    }

    public void showFinalLoserBox() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("You lost");
        alert.setHeaderText("You lost! Please exit:");
 
        ButtonType exit = new ButtonType("Exit");
 
        alert.getButtonTypes().clear(); 
        alert.getButtonTypes().addAll(exit);
 
        // option != null.
        Optional<ButtonType> option = alert.showAndWait();
 
        if (option.get() == exit) {
            this.switchoutMsg();
            RoomMessage room_msg = (RoomMessage)recvFromServer();
            showRoomScene(room_msg);
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
            this.switchoutMsg();
            RoomMessage room_msg = (RoomMessage)recvFromServer();
            showRoomScene(room_msg);
        } 
        else if (option.get() == watch) {
            sendToServer(new ClientMessage(servermsg.getGameID(), servermsg.getStage(), new Action())); // watch the game
            ServerMessage newservermsg = (ServerMessage)recvFromServer();
            int pid = newservermsg.getMap().getPidByName(pname);
            if (newservermsg.getStage()== 1) { // initialize
                showInitScene(newservermsg.getGameID(), pid);
            }
            else if (newservermsg.getStage() == 2) { // playing game
                showGameScene(newservermsg.getGameID(), pid);
            }
        }

    }

    public void sendToServer(Object obj) {
        this.getGameClient().sendObject(obj);
    }

    public Object recvFromServer() {
        Object obj = this.getGameClient().recvObject();
        return obj;
    }

    public void setupIStream() {
        this.getGameClient().setUpInputStream();
    }

    public void switchoutMsg() {
        this.getGameClient().sendSwitchOutMsg();
    }

}
