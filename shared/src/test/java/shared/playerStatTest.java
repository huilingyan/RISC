package shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class playerStatTest {
  @Test
  public void test_playerStat() {
    PlayerStat p0 = new PlayerStat(0, "p0", 10, 10, 3, "87CEFA");
    //pid, name, food, gold, territoryNum, color
    PlayerStat p1 = new PlayerStat(p0);
    assert (p1.getColor().equals("87CEFA") == true);
    assert (p1.getPid() == 0);
    assert (p1.getPName().equals("p0") == true);
    assert (p1.getFood() == 10);
    assert (p1.getGold() == 10);
    assert (p1.getMaxTechLvl() == 1);
    assert (p1.getTerritoryNum() == 3);
    assert (p1.hasTerritory() == true);
    p1.upgradeMaxTechLvl();
    assert (p1.getMaxTechLvl() == 2);
    p1.setMaxTechLvl(6);
    assert (p1.getMaxTechLvl() == 6);
    p1.setTerritoryNum(0);
    assert (p1.hasTerritory() == false);
    p1.subtractTerritoryNum(1);
    assert (p1.getTerritoryNum() == -1);
    p1.addTerritoryNum(5);
    assert (p1.getTerritoryNum() == 4);
    p1.addFood(100);
    assert (p1.getFood() == 110);
    p1.subtractFood(7);
    assert (p1.getFood() == 103);
    p1.addGold(100);
    assert (p1.getGold() == 110);
    p1.subtractGold(5);
    assert (p1.getGold() == 105);
    System.out.println("playerStat test passed");
    
    
  }

}
