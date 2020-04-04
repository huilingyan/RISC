package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import shared.*;

public class initHandlerTest {
  @Test
  public void testInitHandler() {
    
    Territory t0 = new Territory(0, 0, "Red");
    Territory t1 = new Territory(0, 1, "Blue");
    Territory t2 = new Territory(1, 2, "Green");
    Territory t3 = new Territory(2, 3, "Yellow");
    Territory t4 = new Territory(2, 4, "Purple");

    // set number of defenders
    t0.setDefender(new Army(0));
    t1.setDefender(new Army(-1));
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
    shared.Map worldmap = new shared.Map(t_map);
    

    //------------------------------------------------------
    //set up a init operation list
    Action initAction = new Action();
    InitOperation init1 = new InitOperation("Blue", new Army(8));
    InitOperation init2 = new InitOperation("Red", new Army(2));
    InitOperation init3 = new InitOperation("Green", new Army(5));
    InitOperation init4 = new InitOperation("NULL", new Army(1));
    initAction.addInitOperation(init1);
    initAction.addInitOperation(init2);
    initAction.addInitOperation(init3);
    initAction.addInitOperation(init4);
    //-----------------------------------
    //instance of initHandler
    InitHandler h1 = new InitHandler();
    shared.Map new_worldmap = h1.handleAction(worldmap, initAction);
    assert (new_worldmap.getTerritoryByName("Red").getDefender().getTotalSoldiers() == 2);
    assert (new_worldmap.getTerritoryByName("Blue").getDefender().getTotalSoldiers() == 7);
    assert (new_worldmap.getTerritoryByName("Green").getDefender().getTotalSoldiers() == 7);
    System.out.println("initHandler test passed");
    
    
  }

}
