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
    .put(1,"You got card 'Portal': For this turn, you can spend 25 gold and your move and attack will cost no food.")
    .put(2,"You got card 'Communism': For the next 5 turns, you get no gold from you territories, but all your soldiers get +5 bonus during battle.")
    .put(3,"You got card 'Technology Worship': Automatically upgrade you max tech lv by 1 this turn (IT\"S FREE!).")
    .put(4, "You got card 'Conscription': For this turn, you will get 5 soldiers from each territory at the end but it will cost 10 food per territory. ")
    .put(5,"You got card 'Silk Road': For this turn you get double gold from you terrtories, but it will cost 5 food per territory.")
    .put(6,"You got card 'Loan': You instantly get 300 gold this turn, but pay 70 gold for the next 5 turns.")
    .build();

  private static boolean answer;

  public static boolean cardSelection(int cid) {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Card activation");
    //window.setMaxWidth(450);

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
