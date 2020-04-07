package shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ActionTest {
  @Test
  public void test_Action() {
    Action a1 = new Action();
    a1.getInitOperations();
    a1.getUpgradeOperations();
    a1.getMoveOperations();
    a1.getAttackOperations();
    a1.getUpgradeMaxTechHashMap().put(0, false);
    a1.getUpgradeMaxTechHashMap().put(1, false);
    assert(a1.isUpgradeMaxTechLv(0)== false);
    a1.upgradeMaxTechLv(0);
    assert (a1.isUpgradeMaxTechLv(0));
    a1.concatInitOperation(a1);
    a1.concatGameOperation(a1);
    
  }

}
