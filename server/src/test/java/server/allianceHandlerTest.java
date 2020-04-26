package server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import shared.*;

public class allianceHandlerTest {
  @Test
  public void testAlliance() {
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

    
    Action allianceAction = new Action();
    allianceAction.addAllianceRequest(0, 1);
    Action a2 = new Action();
    a2.addAllianceRequest(2, 3);
    a2.addAllianceRequest(1, 0);
    a2.addAllianceRequest(2, 1);
    allianceAction.concatGameOperation(a2);
    //------------------------------------------------------
    GameHandler h1 = new GameHandler();
    shared.Map new_worldmap = h1.handleAction(worldmap, allianceAction);
    assert (new_worldmap.getPlayerStatByPid(0).isAllied());
    assert (new_worldmap.getPlayerStatByPid(1).isAllied());
    assert (new_worldmap.getPlayerStatByPid(1).getAid() == 0);
    assert (!new_worldmap.getPlayerStatByPid(2).isAllied());
    assert (!new_worldmap.getPlayerStatByPid(3).isAllied());

    //------------------------------------------------------
    Action moveAction = new Action();
    MoveOperation move1 = new MoveOperation("Blue", "Green", new Army(2));
    MoveOperation move2 = new MoveOperation("Green", "Blue", new Army(3));
    moveAction.addMoveOperation(move1);
    moveAction.addMoveOperation(move2);
    new_worldmap = h1.handleMove(new_worldmap, moveAction);
    assert (new_worldmap.getTerritoryByTid(1).getFriendDefender().getTotalSoldiers() == 3);
    assert (new_worldmap.getTerritoryByTid(1).getDefender().getTotalSoldiers() == 7);
    assert (new_worldmap.getTerritoryByTid(2).getFriendDefender().getTotalSoldiers() == 2);
    assert (new_worldmap.getTerritoryByTid(2).getDefender().getTotalSoldiers() == 5);
    //------------------------------------------------------
    //move from ally's territory
    Action moveAction1 = new Action();
    MoveOperation move3 = new MoveOperation("Blue", "Green", new Army(1));
    MoveOperation move4 = new MoveOperation("Green", "Blue", new Army(2));
    MoveOperation move5 = new MoveOperation("Blue", "Red", new Army(1));
    moveAction1.addMoveFromAllyOperation(move3);
    moveAction1.addMoveFromAllyOperation(move4);
    moveAction1.addMoveFromAllyOperation(move5);
    shared.Map m1 = h1.handleMove(new_worldmap, moveAction1);
    assert (m1.getTerritoryByTid(1).getFriendDefender().getTotalSoldiers() == 1);
    assert (m1.getTerritoryByTid(1).getDefender().getTotalSoldiers() == 9);
    assert (m1.getTerritoryByTid(2).getFriendDefender().getTotalSoldiers() == 0);
    assert (m1.getTerritoryByTid(2).getDefender().getTotalSoldiers() == 6);
    assert (m1.getTerritoryByTid(0).getFriendDefender().getTotalSoldiers() == 1);
    
    //------------------------------------------------------
    Action attackAction = new Action();
    AttackOperation attack1 = new AttackOperation("Blue", "Purple", new Army(5));
    AttackOperation attack2 = new AttackOperation("Green", "Purple", new Army(4));
    AttackOperation attack3 = new AttackOperation("Yellow", "Green", new Army(2));
    
    attackAction.addAttackOperation(attack1);
    attackAction.addAttackOperation(attack2);
    attackAction.addAttackOperation(attack3);
    shared.Map m2 = h1.handleAttack(new_worldmap, attackAction);
    System.out.println("Territory purple has " + m2.getTerritoryByTid(4).getDefender().getTotalSoldiers() + " defenders and " + m2.getTerritoryByTid(4).getFriendDefender().getTotalSoldiers() + " friend defenders");
    assert (m2.getTerritoryByTid(4).getOwnership() == 0);
    assert (m2.getPlayerStatByPid(0).getTerritoryNum() == 3);
    
    Action attackAction1 = new Action();
    attack1 = new AttackOperation("Blue", "Purple", new Army(2));
    Army a1 = new Army();
    
    a1.addSoldiers(4, 1);
    attack2 = new AttackOperation("Green", "Purple", a1);
    
    attack3 = new AttackOperation("Yellow", "Green", new Army(2));
    attackAction1.addAttackOperation(attack1);
    attackAction1.addAttackOperation(attack2);
    attackAction1.addAttackOperation(attack3);
    m2.getTerritoryByTid(2).addDefender(a1);
    m2.getTerritoryByTid(2).addFriendDefender(a1);
    shared.Map m4 = h1.handleAttack(m2, attackAction1);

    //------------------------------------------------------
    Action attackAction2 = new Action();
    AttackOperation attack4 = new AttackOperation("Blue", "Green", new Army(1));
    attackAction2.addAttackOperation(attack4);
    assert (new_worldmap.getTerritoryByTid(2).getDefender().getTotalSoldiers() == 5);
    shared.Map m3 = h1.handleAllianceBreak(new_worldmap, attackAction2);
    //System.out.println("Territory green defender: " + m3.getTerritoryByTid(2).getDefender().getTotalSoldiers());
    assert (m3.getTerritoryByTid(2).getDefender().getTotalSoldiers() == 8);
    assert (m3.getTerritoryByTid(1).getDefender().getTotalSoldiers() == 7);
    assert (m3.getTerritoryByTid(0).getDefender().getTotalSoldiers() == 12);
    assert (!m3.getPlayerStatByPid(0).isAllied());
    assert (!m3.getPlayerStatByPid(1).isAllied());
    assert (m3.getPlayerStatByPid(1).getAid() == 1);
    assert (m3.getTerritoryByTid(1).getFriendDefender().getTotalSoldiers() == 0);
    assert (m3.getTerritoryByTid(2).getFriendDefender().getTotalSoldiers() == 0);
    
    
    //------------------------------------------------------
    System.out.println("alliance test passed");
  }

}
