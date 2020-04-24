package client.controller;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardAlertBox {
  private static final Map<Integer, String> cidtomsgMap = ImmutableMap.<Integer, String>builder()
        .put(-1, "You got a card: this card has no effect.")
        .build();

  private static boolean answer;

  public static boolean cardSelection(int cid) {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Card activation");
    window.setMaxWidth(450);

    Label l = new Label();
    if (cidtomsgMap.containsKey(cid)) {
      l.setText(cidtomsgMap.get(cid));
    }
    else {
      l.setText("No such card:"+cid);
    }

    Label activatenotice = new Label("Do you want to activate it or not?");

    Button noBtn = new Button("No");
    noBtn.setOnAction(e->{
      answer = false;
      window.close();
      });

    Button yesBtn = new Button("Yes");
    yesBtn.setOnAction(e -> {
        answer = true;
        window.close();
    });

    ButtonBar btnBar = new ButtonBar();
    btnBar.getButtons().addAll( yesBtn,noBtn);
    VBox vb = new VBox();
    vb.setSpacing(10);
    vb.getChildren().addAll(l, activatenotice,btnBar);
    vb.setAlignment(Pos.CENTER);

    Scene sc = new Scene(vb);
    window.setScene(sc);
    window.showAndWait();

    return answer;
  }
}
