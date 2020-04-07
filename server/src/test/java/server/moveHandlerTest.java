package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import shared.*;

public class moveHandlerTest {
  @Test
  public void testMoveHandler() {

    Territory t0 = new Territory(0, 0, "Red", 1, 10, 10);
    Territory t1 = new Territory(0, 1, "Blue", 2, 10, 10);
    Territory t2 = new Territory(1, 2, "Green", 3, 10, 10);
    Territory t3 = new Territory(2, 3, "Yellow", 4, 10, 10);
    Territory t4 = new Territory(2, 4, "Purple", 5, 10, 10);

    // set number of defenders
    t0.setDefender(new Army(1));
    t1.setDefender(new Army(9));
    t2.setDefender(new Army(2));
    t3.setDefender(new Army(3));
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
    ArrayList<PlayerStat> p_list = new ArrayList<PlayerStat>();
    p_list.add(p0);
    p_list.add(p1);
    p_list.add(p2);
    shared.Map worldmap = new shared.Map(t_map, p_list);
    

    //------------------------------------------------------
    //set up a init operation list
    Action moveAction = new Action();
    MoveOperation move1 = new MoveOperation("Blue", "Red", new Army(2));
    MoveOperation move2 = new MoveOperation("Yellow", "Purple", new Army(3));
    MoveOperation move3 = new MoveOperation("NULL1", "Purple", new Army(3));
    moveAction.addMoveOperation(move1);
    moveAction.addMoveOperation(move2);
    moveAction.addMoveOperation(move3);
    //-----------------------------------
    //instance of initHandler
    GameHandler h1 = new GameHandler();
    shared.Map new_worldmap = h1.handleMove(worldmap, moveAction);
    assert (new_worldmap.getTerritoryByTid(0).getDefender().getTotalSoldiers() == 3);
    assert (new_worldmap.getTerritoryByTid(1).getDefender().getTotalSoldiers() == 7);
    assert (new_worldmap.getTerritoryByTid(2).getDefender().getTotalSoldiers() == 2);
    assert (new_worldmap.getTerritoryByTid(3).getDefender().getTotalSoldiers() == 0);
    assert (new_worldmap.getTerritoryByTid(4).getDefender().getTotalSoldiers() == 9);
    System.out.println("moveHandler test passed");


  }

}
