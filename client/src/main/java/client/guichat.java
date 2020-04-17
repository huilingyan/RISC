package client;

import client.controller.ChatBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class guichat extends Application {
  private ChatBox cb = new ChatBox();

	@Override
	public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("RISC_chat");
    Button clickBtn = new Button("click for nothing");
    clickBtn.setOnAction(e->System.out.println("don't click me"));
    Button chatBtn = new Button("chat");
    chatBtn.setOnAction(e -> {
        this.cb.displaychatbox();
    });
    HBox hb = new HBox(10,chatBtn,clickBtn);
    Scene sc = new Scene(hb,300,300);
    primaryStage.setScene(sc);
    primaryStage.show();
    
	}

  public static void main(String[] args) {
        launch();
    }
}
