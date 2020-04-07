package shared;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TerritoryTest {
  @Test
  public void test_Territory() {
    Territory t = new Territory(0, 0, "test", 3, 9, 6);
    Territory t1 = new Territory(1, 1, "t1");
    t1.set_terr_attributes(1, 10, 10);
    Territory t2 = new Territory(t1);
    t2.setOwnership(2);
    t2.setTid(2);
    t2.setName("t2");
    t2.setDefender(new Army(10));
    t2.setNeighborList(new ArrayList<Integer>());
    t2.countNeighbors();
    assert (t2.getOwnership() == 2);
    assert (t2.getTid() == 2);
    assert (t2.getName() == "t2");
    assert (t2.getSize() == 1);
    assert (t2.getFood() == 10);
    assert (t2.getGold() == 10);
    assert (t2.getDefender().getTotalSoldiers() == 10);
    t2.getNeighborList().add(0);
    t2.getNeighborList().add(-1);
    t2.getNeighbor(0);
    t2.countNeighbors();
    assert (t2.calcNbID(-1) == -1);
    
    
    
  }

}
