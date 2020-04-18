package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.controller.ChatBox;
import client.model.ChatModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import shared.ChatMessage;

public class guichat extends Application {
  ChatModel chatM = new ChatModel("127.0.0.1", 2019);
  private ChatBox cb = new ChatBox(chatM);

	@Override
	public void start(Stage primaryStage) throws Exception {
    /*Socket echoSkt = new Socket("127.0.0.1", 2019);
    ObjectInputStream in = new ObjectInputStream(echoSkt.getInputStream());
    ObjectOutputStream out = new ObjectOutputStream(echoSkt.getOutputStream());
    */
    primaryStage.setTitle("RISC_chat");
    Button clickBtn = new Button("click for nothing");
    clickBtn.setOnAction(e->{
      System.out.println("don't click me");
      /*
      try{
        out.writeObject(new ChatMessage(0, 1, "connected!"));
        ChatMessage cm =(ChatMessage) in.readObject();
        System.out.println(cm.getMessage());
      } catch (IOException ioe) {
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
        }*/
      
      
      });
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
