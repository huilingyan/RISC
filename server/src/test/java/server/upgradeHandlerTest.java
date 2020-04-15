package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import shared.*;

public class upgradeHandlerTest {
  @Test
  public void testUpgradeHandler() {
    
    Territory t0 = new Territory(0, 0, "Red");
    Territory t1 = new Territory(0, 1, "Blue");
    Territory t2 = new Territory(1, 2, "Green");
    Territory t3 = new Territory(2, 3, "Yellow");
    Territory t4 = new Territory(2, 4, "Purple");

    // set number of defenders
    t0.setDefender(new Army(10));
    t1.setDefender(new Army(9));
    t2.setDefender(new Army(8));
    t3.setDefender(new Army(7));
    t4.setDefender(new Army(6));

    t0.setNeighbor(0, 1);
    t1.setNeighbor(0, 0);
    t1.setNeighbor(1, 2);
    t2.setNeighbor(0, 1);
    t2.setNeighbor(1, 3);
    t2.setNeighbor(2, 4);
    t3.setNeighbor(0, 2);
    t3.setNeighbor(1, 4);
    t4.setNeighbor(0, 2);
    t4.setNeighbor(1, 3);

    ArrayList<Territory> t_map = new ArrayList<Territory>();
    t_map.add(t0);
    t_map.add(t1);
    t_map.add(t2);
    t_map.add(t3);
    t_map.add(t4);
    
	PlayerStat p0 = new PlayerStat(0, "test_player0", 999, 999, 2, "87CEFA");
    PlayerStat p1 = new PlayerStat(1, "test_player1", 999, 999, 1, "87CEFB");
    PlayerStat p2 = new PlayerStat(2, "test_player2", 999, 999, 2, "87CEFC");
    p2.setMaxTechLvl(6);
    ArrayList<PlayerStat> p_list = new ArrayList<PlayerStat>();
    p_list.add(p0);
    p_list.add(p1);
    p_list.add(p2);
    shared.Map worldmap = new shared.Map(t_map, p_list);

    

    //------------------------------------------------------
    //set up a init operation list
    Action upgradeAction = new Action();
    Army a1 = new Army();
    a1.addSoldiers(1, 5);
    UpgradeOperation upgrade1 = new UpgradeOperation("Blue", new Army(5), a1);
    Army a2 = new Army();
    a2.addSoldiers(2, 4);
    UpgradeOperation upgrade2 = new UpgradeOperation("Red", new Army(4), a2);
    Army a3 = new Army();
    a3.addSoldiers(3, 3);
    UpgradeOperation upgrade3 = new UpgradeOperation("Green", new Army(3), a3);
    UpgradeOperation upgrade4 = new UpgradeOperation("NULL", new Army(2), a1);
    upgradeAction.addUpgradeOperation(upgrade1);
    upgradeAction.addUpgradeOperation(upgrade2);
    upgradeAction.addUpgradeOperation(upgrade3);
    upgradeAction.addUpgradeOperation(upgrade4);

    Action upgrade_max_tech_lv_action = new Action();
    upgrade_max_tech_lv_action.upgradeMaxTechLv(0);
    upgrade_max_tech_lv_action.upgradeMaxTechLv(2);
    upgradeAction.concatGameOperation(upgrade_max_tech_lv_action);
    
    //-----------------------------------
    //instance of initHandler
    GameHandler h1 = new GameHandler();
    shared.Map new_worldmap = h1.handleAction(worldmap, upgradeAction);
    assert (new_worldmap.getTerritoryByName("Red").getDefender().getTotalSoldiers() == 10);
    assert (new_worldmap.getTerritoryByName("Blue").getDefender().getTotalSoldiers() == 9);
    assert (new_worldmap.getTerritoryByName("Green").getDefender().getTotalSoldiers() == 8);

    assert (new_worldmap.getTerritoryByName("Red").getDefender().getSoldierNumber(0) == 6);
    assert (new_worldmap.getTerritoryByName("Red").getDefender().getSoldierNumber(2) == 4);
    assert (new_worldmap.getTerritoryByName("Blue").getDefender().getSoldierNumber(1) == 5);
    assert (new_worldmap.getTerritoryByName("Green").getDefender().getSoldierNumber(3) == 3);
    
    //new_worldmap = h1.handleAction(new_worldmap, upgrade_max_tech_lv_action);
    assert (new_worldmap.getPlayerStatByPid(0).getMaxTechLvl() == 2);
    assert (new_worldmap.getPlayerStatByPid(2).getMaxTechLvl() == 6);
    System.out.println("upgradeHandler test passed");
    
    
  }

}
