package server;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import shared.*;

public class attackHandlerTest {
  @Test
  public void testAttackHandler() {
    
    Territory t0 = new Territory(0, 0, "Red");
    Territory t1 = new Territory(0, 1, "Blue");
    Territory t2 = new Territory(1, 2, "Green");
    Territory t3 = new Territory(2, 3, "Yellow");
    Territory t4 = new Territory(2, 4, "Purple");

    // set number of defenders
    t0.setDefender(new Army(10));
    t1.setDefender(new Army(1));
    t2.setDefender(new Army(new ArrayList<Integer>(
                            Arrays.asList(12,1,1,0,1,0,0))));
    t3.setDefender(new Army(1));
    t4.setDefender(new Army(1));

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
    ArrayList<PlayerStat> p_list = new ArrayList<PlayerStat>();
    p_list.add(p0);
    p_list.add(p1);
    p_list.add(p2);
    shared.Map worldmap = new shared.Map(t_map, p_list);

    //------------------------------------------------------
    //set up a init operation list
    //player 0: Red 5 Blue 9
    //player 1: Green 12
    //player 2: Yellow 7 Purple 6
    Action attackAction = new Action();
    AttackOperation attack1 = new AttackOperation("Blue", "Green", new Army(1));
    AttackOperation attack2 = new AttackOperation("Red", "Green", new Army(1));
    AttackOperation attack3 = new AttackOperation("Yellow", "Green", new Army(1));
    Army a1 = new Army();
    Army a2 = new Army();
    a1.addSoldiers(4, 1);
    a2.addSoldiers(2, 1);
    a2.addSoldiers(1, 1);
    AttackOperation attack4 = new AttackOperation("Green", "Yellow", a1);
    AttackOperation attack5 = new AttackOperation("Green", "Blue", a2);
    AttackOperation attack6 = new AttackOperation("Gree", "Red", new Army(1));
    attackAction.addAttackOperation(attack1);
    attackAction.addAttackOperation(attack2);
    attackAction.addAttackOperation(attack3);
    attackAction.addAttackOperation(attack4);
    attackAction.addAttackOperation(attack5);
    attackAction.addAttackOperation(attack6);
    //-----------------------------------
    //instance of initHandler
    GameHandler h1 = new GameHandler();
    shared.Map new_worldmap = h1.handleAction(worldmap, attackAction);
    
    System.out.println("p0 has " + new_worldmap.getPlayerStatByPid(0).getTerritoryNum());
    System.out.println("p1 has " + new_worldmap.getPlayerStatByPid(1).getTerritoryNum());
    System.out.println("p2 has " + new_worldmap.getPlayerStatByPid(2).getTerritoryNum());
    assert (new_worldmap.getPlayerStatByPid(0).getTerritoryNum() == 1);
    assert (new_worldmap.getPlayerStatByPid(1).getTerritoryNum() == 3);
    assert (new_worldmap.getPlayerStatByPid(2).getTerritoryNum() == 1);
    System.out.println("attackHandler test passed"); 
  }

}
