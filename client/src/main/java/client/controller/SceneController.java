package client.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;

public abstract class SceneController {

    abstract void setMainController(MainController mainC);
    abstract Scene getCurrScene();
}
