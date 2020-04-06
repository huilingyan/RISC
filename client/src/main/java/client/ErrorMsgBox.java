package client;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.OperationValidator;


public class ErrorMsgBox {
  private static final Map<Integer,String> ecodetomsgMap=ImmutableMap.<Integer,String>builder()
    .put(OperationValidator.NOT_ENOUGH_FOOD,"You don't have enough food to do this!")
    .put(OperationValidator.NOT_ENOUGH_GOLD,"You don't have enough gold to upgrade!")
    .put(OperationValidator.NOT_ADJACENT,"You can only attack territories adjacent to this one!")
    .put(OperationValidator.INVALID_PATH,"There is no path to the destination you choose!")
    .build();
  public static void display(int errorcode) {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Cannot proceed");
    window.setMaxWidth(450);

    Label l = new Label();
    if (ecodetomsgMap.containsKey(errorcode)) {
      l.setText(ecodetomsgMap.get(errorcode));
    }
    else {
      l.setText("Error code:"+errorcode);
    }

    Button closeBtn = new Button("OK");
    closeBtn.setOnAction(e->window.close());

    VBox vb = new VBox();
    vb.setSpacing(10);
    vb.getChildren().addAll(l, closeBtn);
    vb.setAlignment(Pos.CENTER);

    Scene sc = new Scene(vb);
    window.setScene(sc);
    window.showAndWait();;
  }
}
