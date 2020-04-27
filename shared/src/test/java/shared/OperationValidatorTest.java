package shared;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OperationValidatorTest {
  @Test
  public void test_operationvalidator() {
    shared.Map worldmap = OldMapGenerator.gamemapGenerator();
    //PlayerStat p0 = new PlayerStat(0, "p0", 113, 40, 4, "87CEFA");
    //pid 0, food 113, gold 40, 4 territories tid 1,4,9,5
    
    //ArrayList<Integer> tids = new ArrayList<Integer>(Arrays.asList(1, 4, 9, 5, 10, 13, 2, 6, 11));
    
    //Army army = new Army(new ArrayList<Integer>(Arrays.asList(7,6,5,4,3,2,1)));
    OperationValidator v0 = new OperationValidator(0, worldmap);
    Map updated_map = v0.getCurrentMapState();
    System.out.println("territory 1: " + updated_map.getTerritoryNameByTid(1) + " owner: " + updated_map.getTerritoryByTid(1).getOwnership());
    assert (updated_map.getPlayerStatByPid(0).getFood() == 113);
    InitOperation initop1 = new InitOperation("Ditto", new Army(1));
    assert (v0.isValidInitOperation(initop1, 29) == VALID);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 8);
    
    InitOperation initop2 = new InitOperation("Ditt", new Army(1));
    assert(v0.isValidInitOperation(initop2, 29) == INVALID_DEST);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 8);

    InitOperation initop3 = new InitOperation("Ditto", new Army(1));
    assert(v0.isValidInitOperation(initop3, 28) == NOT_ENOUGH_UNITS);
    
    InitOperation initop4 = new InitOperation("Ditto", new Army(-1));
    assert (v0.isValidInitOperation(initop4, 29) == ILLEGAL_NUM);

    
    System.out.println("initOperation validator test passed");


    Army a1 = new Army();
    a1.addSoldiers(1, 1);
    UpgradeOperation uop1 = new UpgradeOperation("Ditto", new Army(1), a1);
    assert (v0.isValidUpgradeOperation(uop1) == VALID);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 7);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(1) == 7);
      
    UpgradeOperation uop2 = new UpgradeOperation("Ditt", new Army(1), a1);
    assert (v0.isValidUpgradeOperation(uop2) == INVALID_DEST);

    UpgradeOperation uop3 = new UpgradeOperation("Ditto", new Army(2), a1);
    assert (v0.isValidUpgradeOperation(uop3) == ILLEGAL_NUM);

    Army a4 = new Army();
    a4.addSoldiers(2, 99);
    UpgradeOperation uop4 = new UpgradeOperation("Ditto", new Army(99), a4);
    //System.out.println(v0.isTerritoryHaveEnoughArmy(new Army(99), v0.getCurrentMapState().getTerritoryByTid(1)));
    //System.out.println("uop4 return " + v0.isValidUpgradeOperation(uop4));
    assert (v0.isValidUpgradeOperation(uop4) == NOT_ENOUGH_UNITS);

    Army a5 = new Army();
    a5.addSoldiers(4, 2);
    UpgradeOperation uop5 = new UpgradeOperation("Ditto", new Army(2), a5);
    //System.out.println("uop5 return " + v0.isValidUpgradeOperation(uop5));
    assert (v0.isValidUpgradeOperation(uop5) == NOT_ENOUGH_GOLD);

    Army a6 = new Army();
    a6.addSoldiers(2, 1);
    UpgradeOperation uop6 = new UpgradeOperation("Ditto", new Army(1), a6);
    assert (v0.isValidUpgradeOperation(uop6) == EXCEED_MAX_LV);
    
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 7);

    System.out.println("upgradeOperation validator test passed");
    
    MoveOperation moveop0 = new MoveOperation("Mew", "Ditto", new Army(7));
    MoveOperation moveop1 = new MoveOperation("Snorlax", "Ditto", new Army(7));
    assert (v0.isValidMoveOperation(moveop0) == VALID);
    assert (v0.isValidMoveOperation(moveop1) == VALID);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 21);
    assert (v0.getCurrentMapState().getTerritoryByTid(4).getDefender().getSoldierNumber(0) == 0);
    

    MoveOperation moveop2 = new MoveOperation("Pikachu", "Snorlax", new Army(2));
    assert (v0.isValidMoveOperation(moveop2) == INVALID_SRC);

    MoveOperation moveop3 = new MoveOperation("Ditto", "Snorlax", new Army(-2));
    assert (v0.isValidMoveOperation(moveop3) == ILLEGAL_NUM);

    MoveOperation moveop4 = new MoveOperation("Ditto", "Snorlax", new Army(99));
    assert (v0.isValidMoveOperation(moveop4) == NOT_ENOUGH_UNITS);

    MoveOperation moveop5 = new MoveOperation("Ditto", "Snorlx", new Army(1));
    assert (v0.isValidMoveOperation(moveop5) == INVALID_DEST);

    MoveOperation moveop6 = new MoveOperation("Ditto", "Ditto", new Army(1));
    assert (v0.isValidMoveOperation(moveop6) == DEST_SAME_AS_SRC);
    v0.getCurrentMapState().getTerritoryByTid(4).setOwnership(1);
    v0.getCurrentMapState().getTerritoryByTid(5).setOwnership(1);
    //invalid path test
    MoveOperation moveop7 = new MoveOperation("Ditto", "Jumpluff", new Army(1));
     System.out.println("moveop7 return " + v0.isValidMoveOperation(moveop7));
    assert (v0.isValidMoveOperation(moveop7) == INVALID_PATH);
    v0.getCurrentMapState().getTerritoryByTid(4).setOwnership(0);
    v0.getCurrentMapState().getTerritoryByTid(5).setOwnership(0);

    MoveOperation moveop8 = new MoveOperation("Ditto", "Jumpluff", new Army(14));
    assert (v0.isValidMoveOperation(moveop8) == NOT_ENOUGH_FOOD);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 21);
    MoveOperation moveop9 = new MoveOperation("Ditto", "Jumpluff", new Army(11));
    assert (v0.isValidMoveOperation(moveop9) == VALID);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 10);
     assert (v0.getCurrentMapState().getPlayerStatByPid(0).getFood() == 5);
    
    System.out.println("moveOperation validator test passed");

    AttackOperation atkop1 = new AttackOperation("Ditto", "Gengar", new Army(1));
    assert (v0.isValidAttackOperation(atkop1) == VALID);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 9);
    
    AttackOperation atkop2 = new AttackOperation("Dito", "Gengar", new Army(1));
    assert (v0.isValidAttackOperation(atkop2) == INVALID_SRC);

    AttackOperation atkop3 = new AttackOperation("Ditto", "Gengar", new Army(-1));
    assert (v0.isValidAttackOperation(atkop3) == ILLEGAL_NUM);

    AttackOperation atkop4 = new AttackOperation("Ditto", "Gengar", new Army(99));
    assert (v0.isValidAttackOperation(atkop4) == NOT_ENOUGH_UNITS);
    
    AttackOperation atkop5 = new AttackOperation("Ditto", null, new Army(1));
    assert (v0.isValidAttackOperation(atkop5) == INVALID_DEST);

    AttackOperation atkop6 = new AttackOperation("Ditto","Psyduck", new Army(1));
    assert (v0.isValidAttackOperation(atkop6) == NOT_ADJACENT);

    AttackOperation atkop7 = new AttackOperation("Ditto", "Gengar", new Army(5));
    assert (v0.isValidAttackOperation(atkop7) == NOT_ENOUGH_FOOD);
    assert (v0.getCurrentMapState().getTerritoryByTid(1).getDefender().getSoldierNumber(0) == 9);
    assert (v0.getCurrentMapState().getPlayerStatByPid(0).getFood() == 4);
    
    System.out.println("attackOperation validator test passed");

    assert (v0.isValidUpgradeMaxTechLv() == NOT_ENOUGH_GOLD);
    v0.getCurrentMapState().getPlayerStatByPid(0).addGold(99);
    assert (v0.isValidUpgradeMaxTechLv() == VALID);
    
    assert (v0.isValidUpgradeMaxTechLv() == REPEATED_UPGRADE_MAX_TECH_LV);
    OperationValidator v1 = new OperationValidator(1, worldmap);
    v1.getCurrentMapState().getPlayerStatByPid(1).setMaxTechLvl(99);
    assert (v1.isValidUpgradeMaxTechLv() == EXCEED_MAX_LV);
    
    System.out.println("upgrade max tech lv validator test passed");

    v1.getAction();
  }
  public static final int VALID = 1;
  public static final int INVALID_DEST = -1;
  public static final int NOT_ENOUGH_UNITS = -2; //ev2: change no to not
  public static final int ILLEGAL_NUM = -3;
  public static final int INVALID_SRC = -4;
  public static final int INVALID_PATH = -5;
  public static final int NOT_ADJACENT = -6;
  public static final int DEST_SAME_AS_SRC = -7;
  public static final int NOT_ENOUGH_FOOD = -8;// 4 new flags for ev2
  public static final int NOT_ENOUGH_GOLD = -9;
  public static final int EXCEED_MAX_LV = -10;
  public static final int REPEATED_UPGRADE_MAX_TECH_LV = -11;
  public static final int INVALID_ALLIANCE_REQUEST = -12;// 1 new flag for ev3
}
