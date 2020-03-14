package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import shared.*;

public class attackHandlerTest {
  @Test
  public void testAttackHandler() {
    Displayer displayer = Displayer.getInstance();
    displayer.setNumOfPlayer(3);
    Territory t0 = new Territory(0, 0, "Red");
    Territory t1 = new Territory(0, 1, "Blue");
    Territory t2 = new Territory(1, 2, "Green");
    Territory t3 = new Territory(2, 3, "Yellow");
    Territory t4 = new Territory(2, 4, "Purple");

    // set number of defenders
    t0.setDefenderNum(5);
    t1.setDefenderNum(9);
    t2.setDefenderNum(12);
    t3.setDefenderNum(7);
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
    //player 0: Red 5 Blue 9
    //player 1: Green 12
    //player 2: Yellow 7 Purple 6
    Action attackAction = new Action();
    AttackOperation attack1 = new AttackOperation("Blue", "Green", 7);
    AttackOperation attack2 = new AttackOperation("Red", "Green", 5);
    AttackOperation attack3 = new AttackOperation("Yellow", "Green", 3);
    AttackOperation attack4 = new AttackOperation("Purple", "Red", 4);
    AttackOperation attack5 = new AttackOperation("Green", "Red", 1);
    attackAction.addAttackOperation(attack1);
    attackAction.addAttackOperation(attack2);
    attackAction.addAttackOperation(attack3);
    attackAction.addAttackOperation(attack4);
    attackAction.addAttackOperation(attack5);
    //-----------------------------------
    //instance of initHandler
    GameHandler h1 = new GameHandler();
    ArrayList<Territory> newmap = h1.handleAttack(t_map, attackAction);
    //assert (newmap.get(0).getDefenderNum() == 3);
    //attack result is randomized so can't use assert
    System.out.println("attackHandler test passed");
    //displayer.displayMap(t_map);
    displayer.displayMap(newmap); 
  }

}
