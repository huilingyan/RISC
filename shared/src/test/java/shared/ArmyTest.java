package shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ArmyTest {
  @Test
  public void test_Army() {
    Army a1 = new Army();
    Army a2 = new Army(new ArrayList<Integer>(Arrays.asList(1, 4)));
    Army a3 = new Army(new ArrayList<Integer>(Arrays.asList(1, 0, 0, 0, 0, 0, 0)));
    Army a4 = new Army(1);
    Army a5 = new Army(a4);
    assert (a2.getTotalSoldiers() == 5);
    a5.joinArmy(a2);
    assert (a5.getTotalSoldiers() == 6);
    a3.subtractArmy(new Army(1));
    assert (a3.getTotalSoldiers() == 0);
    a3.subtractSoldiers(0, 1);
    assert (a3.getTotalSoldiers() == -1);
    assert (a5.getSoldierNumber(7) == 0);
    assert (a5.getSoldierNumber(1) == 4);
    a5.setSoldierNumber(2, 1);
    assert (a5.getSoldierNumber(2) == 1);
    a5.setSoldierNumber(7, 1);
    a5.addSoldiers(7, 1);
    a5.subtractSoldiers(7, 1);
    a1.getHighestBonus();
    a1.getLowestBonus();
    a5.getHighestBonus();
    a5.getLowestBonus();
    System.out.println("Army test passed");
  }

}
