package client.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChatBox {
  public ChatBox() {
  }
  public void displaychatbox() {
    Stage window = new Stage();
    window.initModality(Modality.NONE);
    window.setTitle("WeeChat");

    TextArea mesgs = new TextArea();
    mesgs.setPrefHeight(450);

    TextField inputF = new TextField();
    inputF.setPrefHeight(50);

    HBox hb = new HBox(10);
    hb.setPadding(new Insets(0, 20, 0, 340));//t,r,b,l
    Button clrBtn = new Button("Clear");
    clrBtn.setOnAction(e->inputF.clear());
    Button sendBtn = new Button("Send");
    sendBtn.setOnAction(e -> {
        mesgs.appendText(inputF.getText()+"\n");
        inputF.clear();
    });
    Text txt1 = new Text("To");
    ChoiceBox<String> chBox = new ChoiceBox<>();
    chBox.getItems().add("self");
    chBox.setValue("self");
    hb.getChildren().addAll(clrBtn, sendBtn, txt1, chBox);

    VBox vb = new VBox(20);
    vb.getChildren().addAll(mesgs, inputF, hb);
    vb.setPrefSize(600,580);//width height

    Scene sc = new Scene(vb);
    window.setScene(sc);
    window.show();
  }
}
