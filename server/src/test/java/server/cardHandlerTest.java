package server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import shared.*;

public class cardHandlerTest {
  @Test
  public void testCardHandler() {
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
    t4.setDefender(new Army(2));

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
    PlayerStat p3 = new PlayerStat(3, "test_player3", 999, 999, 0, "87CEFD");
    
    ArrayList<PlayerStat> p_list = new ArrayList<PlayerStat>();
    p_list.add(p0);
    p_list.add(p1);
    p_list.add(p2);
    p_list.add(p3);
    shared.Map worldmap = new shared.Map(t_map, p_list);

    //test portal
    //------------------------------------------------------
    worldmap.getPlayerStatByPid(0).setNewCard(1);
    worldmap.getPlayerStatByPid(3).setNewCard(1);
    Action a1 = new Action();
    a1.useNewCard(0);
    Action a11 = new Action();
    a11.useNewCard(3);
    a1.concatGameOperation(a11);
    MoveOperation move1 = new MoveOperation("Blue", "Red", new Army(2));
    AttackOperation attack1 = new AttackOperation("Blue", "Yellow", new Army(1));
    a1.addMoveOperation(move1);
    a1.addAttackOperation(attack1);
    
    GameHandler h1 = new GameHandler();
    shared.Map m1 = h1.handleAction(worldmap, a1);
    assert (m1.getPlayerStatByPid(0).isPortalActivated());
    assert (m1.getPlayerStatByPid(3).isPortalActivated());
    assert (m1.getPlayerStatByPid(0).getFood() == 999);
    
    //------------------------------------------------------
    //test communism
    worldmap.getPlayerStatByPid(0).setNewCard(2);
    
    Action a2 = new Action();
    a2.useNewCard(0);
    AttackOperation attack2 = new AttackOperation("Blue", "Green", new Army(3));
    AttackOperation attack3 = new AttackOperation("Blue", "Green", new Army(4));
    a2.addAttackOperation(attack2);
    a2.addAttackOperation(attack3);
    
    
    shared.Map m2 = h1.handleAction(worldmap, a2);
    //------------------------------------------------------
    //Max tech lv upgrade by 1
    worldmap.getPlayerStatByPid(0).setNewCard(3);
    
    Action a3 = new Action();
    a3.useNewCard(0);
    shared.Map m3 = h1.handleAction(worldmap, a3);
    assert (m3.getPlayerStatByPid(0).getMaxTechLvl() == 2);
    assert (m3.getPlayerStatByPid(0).getGold() == 999);
    //------------------------------------------------------
    //costs 20 food per territory
    //------------------------------------------------------
    //each territory costs 10 food
    //------------------------------------------------------
    //instantly get 300 gold
    //------------------------------------------------------
      //worldmap.generateCards();
    System.out.println("card handler test passed"); 
    
  }

}
