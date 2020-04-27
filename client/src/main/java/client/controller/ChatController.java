package client.controller;

import client.model.ChatModel;
import client.model.PrintMessage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.ChatMessage;
import client.ChatClient;

public class ChatController  implements PrintMessage {

    private TextArea mesgs;
    private Stage window;
    // private ChatModel chatM;

    public MainController mc;
    // private ChatClient chatClient;

    public void setMainController(MainController mainC) {
        this.mc = mainC;
    }

    public ChatController() {
        // this.chatClient = c;
        this.mesgs = new TextArea();
        this.mesgs.setPrefHeight(450);
    }
  
    public void clearChatHistory() {
        mesgs.clear();
    }

    public TextArea getTextArea() {
        return this.mesgs;
    }
    
    public void displayChatBox() {
        this.window = new Stage();
        this.window.initModality(Modality.NONE);
        this.window.setTitle("WeeChat");
    
        TextField inputF = new TextField();
        inputF.setPrefHeight(50);
    
        HBox hb = new HBox(10);
        hb.setPadding(new Insets(0, 20, 0, 340));//t,r,b,l
        ChoiceBox<String> chBox = new ChoiceBox(FXCollections.observableArrayList(this.mc.getPlayerList())); 
        Button clrBtn = new Button("Clear");
        clrBtn.setOnAction(e->inputF.clear());
        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(e -> {
            if (chBox.getValue() == null) { // if don't choose anything
                ErrorAlerts.mustChooseAPlayer();
            }
            else {
                this.mc.sendChatMessage(this.mc.getPlayerName(), chBox.getValue(), inputF.getText());
                // debug
                System.out.println("send chat message to " + chBox.getValue());
                mesgs.appendText("Me to " + chBox.getValue() + " : " + inputF.getText()+"\n");
                inputF.clear();
            }
        });
        Text txt1 = new Text("To");

        hb.getChildren().addAll(clrBtn, sendBtn, txt1, chBox);
    
        VBox vb = new VBox(20);
        vb.getChildren().addAll(mesgs, inputF, hb);
        vb.setPrefSize(600,580);//width height
    
        Scene sc = new Scene(vb);
        this.window.setScene(sc);
        this.window.show();
    }

    public void closeChatBox() {
        this.window.close();
    }

    @Override
    public void printMsg(ChatMessage cm) {
        mesgs.appendText("Server: "+cm.getMessage()+"\n");	
    }

}

