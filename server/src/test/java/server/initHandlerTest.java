package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import shared.*;

public class initHandlerTest {
  @Test
  public void testInitHandler() {
    Displayer displayer = Displayer.getInstance();
    displayer.setNumOfPlayer(3);
    Territory t0 = new Territory(0, 0, "Red");
    Territory t1 = new Territory(0, 1, "Blue");
    Territory t2 = new Territory(1, 2, "Green");
    Territory t3 = new Territory(2, 3, "Yellow");
    Territory t4 = new Territory(2, 4, "Purple");

    // set number of defenders
    t0.setDefenderNum(0);
    t1.setDefenderNum(-1);
    t2.setDefenderNum(2);
    t3.setDefenderNum(3);
    t4.setDefenderNum(6);

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

    

    //------------------------------------------------------
    //set up a init operation list
    Action initAction = new Action();
    InitOperation init1 = new InitOperation("Blue", 8);
    InitOperation init2 = new InitOperation("Red", 2);
    InitOperation init3 = new InitOperation("Green", 5);
    InitOperation init4 = new InitOperation("NULL", 1);
    initAction.addInitOperation(init1);
    initAction.addInitOperation(init2);
    initAction.addInitOperation(init3);
    initAction.addInitOperation(init4);
    //-----------------------------------
    //instance of initHandler
    InitHandler h1 = new InitHandler();
    ArrayList<Territory> newmap = h1.handleAction(t_map, initAction);
    assert (newmap.get(0).getDefenderNum() == 2);
    assert (newmap.get(1).getDefenderNum() == 7);
    assert (newmap.get(2).getDefenderNum() == 7);
    System.out.println("initHandler test passed");
    //displayer.displayMap(t_map);
    //displayer.displayMap(newmap);
    
  }

}
