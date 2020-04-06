package client.controller;

import javafx.scene.Scene;

public abstract class SceneController {

    abstract void setMainController(MainController mainC);
    abstract Scene getCurrScene();
}
