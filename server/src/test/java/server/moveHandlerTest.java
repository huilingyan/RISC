package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import shared.*;

public class moveHandlerTest {
  @Test
  public void testMoveHandler() {
    Displayer displayer = Displayer.getInstance();
    displayer.setNumOfPlayer(3);
    Territory t0 = new Territory(0, 0, "Red");
    Territory t1 = new Territory(0, 1, "Blue");
    Territory t2 = new Territory(1, 2, "Green");
    Territory t3 = new Territory(2, 3, "Yellow");
    Territory t4 = new Territory(2, 4, "Purple");

    // set number of defenders
    t0.setDefenderNum(1);
    t1.setDefenderNum(9);
    t2.setDefenderNum(2);
    t3.setDefenderNum(3);
    t4.setDefenderNum(6);

    t0.setNeighbor(0, t1);
    t1.setNeighbor(0, t0);
    t1.setNeighbor(1, t2);
    t2.setNeighbor(0, t1);
    t2.setNeighbor(1, t3);
    t2.setNeighbor(2, t4);
    t3.setNeighbor(0, t2);
    t3.setNeighbor(1, t4);
    t4.setNeighbor(0, t2);
    t4.setNeighbor(1, t3);

    ArrayList<Territory> t_map = new ArrayList<Territory>();
    t_map.add(t0);
    t_map.add(t1);
    t_map.add(t2);
    t_map.add(t3);
    t_map.add(t4);

    

    //------------------------------------------------------
    //set up a init operation list
    Action moveAction = new Action();
    MoveOperation move1 = new MoveOperation("Blue", "Red", 2);
    MoveOperation move2 = new MoveOperation("Yellow", "Purple", 3);
    MoveOperation move3 = new MoveOperation("NULL1", "Purple", 3);
    moveAction.addMoveOperation(move1);
    moveAction.addMoveOperation(move2);
    moveAction.addMoveOperation(move3);
    //-----------------------------------
    //instance of initHandler
    GameHandler h1 = new GameHandler();
    ArrayList<Territory> newmap = h1.handleMove(t_map, moveAction);
    assert (newmap.get(0).getDefenderNum() == 3);
    assert (newmap.get(1).getDefenderNum() == 7);
    assert (newmap.get(2).getDefenderNum() == 2);
    assert (newmap.get(3).getDefenderNum() == 0);
    assert (newmap.get(4).getDefenderNum() == 9);
    System.out.println("moveHandler test passed");
    //displayer.displayMap(t_map);
    //displayer.displayMap(newmap);

  }

}
