package shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.String;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class OperationValidator {

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

  private Action validatedaction;
  private shared.Map temp_map;//bad naming from ev1
  private int player_id;
  private boolean upgrade_max_tech_lv;
  private boolean request_alliance;

  public OperationValidator(int pid, shared.Map curr_map) {
    this.validatedaction = new Action();//empty Action, only add valid ops
    this.temp_map = new shared.Map(curr_map);//deep copy original map
    this.player_id = pid;//store current player's pid
    this.upgrade_max_tech_lv = false;
    this.request_alliance = false;
  }

  public Action getAction() {
    return this.validatedaction;
  }

  public shared.Map getCurrentMapState() {//bad naming from evolution 1
    return temp_map;
  }

  public int isValidInitOperation(InitOperation initop, int totalunit) {
    //totalunit: bad naming from ev1
    //totalunit inidicates the number of soldiers a player can initially deploy
    //totalunit should be a fixed constant
    
      String dest = initop.getDest();

      //get the territory to deploy initial soldiers
      Territory t_to_deploy = temp_map.getTerritoryByName(dest);
      // 1. Check the name of destination territory
      if ((t_to_deploy == null) || (!isOwnTerritory(t_to_deploy))) {
        return INVALID_DEST;
      }
      // 2. Check if deployed army is valid 
      int remains = getRemainingUnit(totalunit);
      if (initop.getArmy().getTotalSoldiers() > remains) {
        // not enough units
        return NOT_ENOUGH_UNITS;
      }
      if (!isArmyPostive(initop.getArmy())) {
        // negative deployment, illegal number
        return ILLEGAL_NUM;
      }
      // update temp_map: add units to the territory
      t_to_deploy.addDefender(initop.getArmy());

      // add operation to action
      validatedaction.addInitOperation(initop);
    
      return VALID;
  }

  public int isValidUpgradeOperation(UpgradeOperation upgradeop) {
    String dest = upgradeop.getDest();
    Army army_to_upgrade = upgradeop.getArmyToUpgrade();
    Army army_upgraded = upgradeop.getArmy();

    
    Territory t_to_deploy = temp_map.getTerritoryByName(dest);
    // 1. Check the name of destination territory
    if ((t_to_deploy == null) || (!isOwnTerritory(t_to_deploy))) {
      return INVALID_DEST;
    }

    // 2. Check if army is valid
    if (!isArmyPostive (army_to_upgrade) ||
        !isArmyPostive (army_upgraded) ||
        (army_to_upgrade.getTotalSoldiers() != army_upgraded.getTotalSoldiers())) {
      return ILLEGAL_NUM;
    }
    if (!isTerritoryHaveEnoughArmy(army_to_upgrade, t_to_deploy)) {
      //with gui slider, this situation should never occur
        return NOT_ENOUGH_UNITS;
    }
   
    // 3 check if resource is enough
    int gold_remain = temp_map.getPlayerStatByPid(player_id).getGold();
    int upgrade_cost = army_to_upgrade.calculateUpgradeCost(army_upgraded);

    if (gold_remain < upgrade_cost) {
      return NOT_ENOUGH_GOLD;
    }
    //4 check if upgrade exceed max tech level
    int max_tech_lv = temp_map.getPlayerStatByPid(player_id).getMaxTechLvl();
    if (army_upgraded.getHighestLevel() > max_tech_lv) {
      //e.g. max tech lv = 2, order want to upgrade a soldier to lv 3
      return EXCEED_MAX_LV;
    }
    
    //update upgraded army to map
    t_to_deploy.subtractDefender(army_to_upgrade);       
    t_to_deploy.addDefender(army_upgraded);
    // add operation to action
    validatedaction.addUpgradeOperation(upgradeop);
    temp_map.getPlayerStatByPid(player_id).subtractGold(upgrade_cost);

    return VALID;
  }
  
  public int isValidMoveOperation(MoveOperation moveop) {
    String src = moveop.getSrc();
    String dest = moveop.getDest();

    
    // the territory to remove units
    Territory t_to_remove = temp_map.getTerritoryByName(src);
    // 1. check if valid src
    if ((t_to_remove == null) || (!isOwnTerritory(t_to_remove))) {
      return INVALID_SRC;
    }

    // 2. Check if moved army is valid
    if (!isArmyPostive(moveop.getArmy())) {
      return ILLEGAL_NUM;
    }
    if (!isTerritoryHaveEnoughArmy(moveop.getArmy(), t_to_remove)) {
      //with gui slider, this situation should never occur
        return NOT_ENOUGH_UNITS;
    }
    
    // 3. check if valid dest
    // the territory to move units to
    Territory t_to_move = temp_map.getTerritoryByName(dest);
    
    // 3.1 check if own territory
    if (t_to_move == null){
        return INVALID_DEST;
    }
    // can move to ally's territory
    if ((!isOwnTerritory(t_to_move))) {
      if(temp_map.getPlayerStatByPid(player_id).isAllied() &&
           (temp_map.getPlayerStatByPid(t_to_move.getOwnership()).getAid() ==
              temp_map.getPlayerStatByPid(player_id).getAid())) {
        //allow moving to ally's territory
      } else {
        return INVALID_DEST;
      }
    }
    // 3.2 check if is different from src
    if (dest.equalsIgnoreCase(src)) {
      // if the dest is same as src
      return DEST_SAME_AS_SRC;
    }

    // 3.3 check if there's a path
    // CostofShortestPath is updated, can move through ally's territory
    int move_dist = temp_map.CostofShortestPath(src, dest);
    if (move_dist < 0) {
      return INVALID_PATH;
    }

    // 4 check if resource is enough
    int food_remain = temp_map.getPlayerStatByPid(player_id).getFood();
    int move_cost = moveop.getArmy().getTotalSoldiers() * move_dist;
    
    if(food_remain < move_cost){
      return NOT_ENOUGH_FOOD;
    }

    // update temp_map: add and subtract army, deduct resources
    
    t_to_remove.subtractDefender(moveop.getArmy());
    if ((!isOwnTerritory(t_to_move))) {
      if(temp_map.getPlayerStatByPid(player_id).isAllied() &&
           (temp_map.getPlayerStatByPid(t_to_move.getOwnership()).getAid() ==
              temp_map.getPlayerStatByPid(player_id).getAid())) {
        //allow moving to ally's territory
        //add to FriendDefender instead of Defender
        t_to_move.addFriendDefender(moveop.getArmy());
      }
    } else {
      t_to_move.addDefender(moveop.getArmy());
    }
    temp_map.getPlayerStatByPid(player_id).subtractFood(move_cost);

    // if valid, add to move operation
    validatedaction.addMoveOperation(moveop);
    return VALID;

  }

  public int isValidAttackOperation(AttackOperation attackop) {
    String src = attackop.getSrc();
    String dest = attackop.getDest();

    // the territory to remove units
    Territory t_to_remove = temp_map.getTerritoryByName(src);
    
    // 1. check if valid src
    if ((t_to_remove == null) || (!isOwnTerritory(t_to_remove))) {
      return INVALID_SRC;
    }

    // 2. check if valid number
    if (!isArmyPostive(attackop.getArmy())) {
      // negative deployment, illegal number
      return ILLEGAL_NUM;
    }
    if (!isTerritoryHaveEnoughArmy(attackop.getArmy(), t_to_remove)) {
      //with gui slider, this situation should never occur
        return NOT_ENOUGH_UNITS;
    }
    

    // 3. check if valid dest
    // the territory to attack
    Territory t_to_move = temp_map.getTerritoryByName(dest); 
    // 3.1 check if is other's territory
    if ((t_to_move == null) || (isOwnTerritory(t_to_move))) {
      return INVALID_DEST;
    }

    // 3.2 check if is adjacent
    if (!isAdjacent(t_to_remove, t_to_move)) {
      return NOT_ADJACENT;
    }

    // 4 check if resource is enough
    int food_remain = temp_map.getPlayerStatByPid(player_id).getFood();
    int attack_cost = attackop.getArmy().getTotalSoldiers();
    //An attack order now costs 1 food per unit attacking
    if(food_remain < attack_cost){
      return NOT_ENOUGH_FOOD;
    }

    // update temp_map: sub army, deduct resources
    //do not change the territory being attacked
    t_to_remove.subtractDefender(attackop.getArmy());
    temp_map.getPlayerStatByPid(player_id).subtractFood(attack_cost);
    
    // if valid, add to move operation
    validatedaction.addAttackOperation(attackop);
    //Break alliance when current player attacks ally’s territory
    if (temp_map.getPlayerStatByPid(player_id).isAllied()
        && (temp_map.getPlayerStatByPid(t_to_move.getOwnership()).getAid() == temp_map.getPlayerStatByPid(player_id).getAid())) {
      int allyid = t_to_move.getOwnership();
      temp_map.breakAlliance(player_id, allyid);
      //return FriendArmy to nearest territory
      for (String tName : temp_map.getOwnTerritoryListName(player_id)) {
        Territory t = temp_map.getTerritoryByName(tName);
        if (t.getFriendDefender().getTotalSoldiers() > 0) {
          temp_map.getNearestTerritory(t, allyid).addDefender(t.getFriendDefender());
          t.setFriendDefender(new Army());
        }
      }
      for (String tName : temp_map.getOwnTerritoryListName(allyid)) {
        Territory t = temp_map.getTerritoryByName(tName);
        if (t.getFriendDefender().getTotalSoldiers() > 0) {
          temp_map.getNearestTerritory(t, player_id).addDefender(t.getFriendDefender());
          t.setFriendDefender(new Army());
        }
      }          
    }
    return VALID;

  }

  
  public int isValidUpgradeMaxTechLv() {
    //should be called if player choose to upgrade his max technology level
    //i suppose the gui controller can call this method if button is clicked
    if (upgrade_max_tech_lv) {
      //do not allow repeated upgrade
      return REPEATED_UPGRADE_MAX_TECH_LV;
    }
    int tech_lv = temp_map.getPlayerStatByPid(player_id).getMaxTechLvl();
    if (tech_lv >= 6) {
      // cannot exceed lv 6
      return EXCEED_MAX_LV;
    }

    int gold_remain = temp_map.getPlayerStatByPid(player_id).getGold();

    ArrayList<Integer> upgrade_cost_list = new ArrayList<Integer>(Arrays.asList(0, 50, 75, 125, 200, 300));
    int upgrade_cost = upgrade_cost_list.get(tech_lv);

    if (gold_remain < upgrade_cost) {
      return NOT_ENOUGH_GOLD;
    }
    // if valid, add to action
    //make validatedaction.isUpgradeMaxTechLv(player_id)==true
    temp_map.getPlayerStatByPid(player_id).subtractGold(upgrade_cost);
    validatedaction.upgradeMaxTechLv(player_id);
    upgrade_max_tech_lv = true;
    return VALID;

  }

   public int isValidAllianceRequest(int topid) {
     
     if (request_alliance) {
       //Cannot send multiple alliance requests in one turn
       return INVALID_ALLIANCE_REQUEST;
     }
     if(temp_map.getPlayerStatByPid(player_id).isAllied() ||
        temp_map.getPlayerStatByPid(topid).isAllied()){
       //Cannot form alliance with players already allied
       return INVALID_ALLIANCE_REQUEST;
     }
          
     request_alliance = true;
     validatedaction.addAllianceRequest(player_id, topid);
     return VALID;
   }

  public int isValidCardUsage() {


    validatedaction.useNewCard(player_id);
    return VALID;
   }

  //--------------------helper functions-------------------------
  // helper method: get the remaining number of unit for player
  public int getRemainingUnit(int totalunit) {
    int remains = totalunit;
    for (Territory t : this.temp_map.getTerritories()) {
      if (isOwnTerritory(t)) {
        // if is the player's territory
        remains -= t.getDefender().getSoldierNumber(0);
        //at init phase player only have lv 0 soldiers
      }
    }
    return remains;
  }

  private boolean isOwnTerritory(Territory t) {
    return (t.getOwnership() == this.player_id);
  }

  private boolean isAdjacent(Territory src, Territory dest) {

    for (int neigh : src.getNeighborList()) {
      if (neigh != -1) {
        if (neigh == dest.getTid()) { // if dest in neighbout list
          return true;
        }
      }
    }
    return false;
  }

  public boolean isTerritoryHaveEnoughArmy(Army army, Territory t) {
    for (int i = 0; i <= army.getHighestLevel(); i++) {
      if (army.getSoldierNumber(i) > t.getDefender().getSoldierNumber(i)) {
        // not enough units to move for level i (0-max lv)
        return false;
      }
    }
    return true;
  }

  private boolean isArmyPostive(Army army_to_check) {
    for (int i = 0; i < 7; i++) { //  0-6 levels
      if (army_to_check.getSoldierNumber(i) < 0) {
        //if any lv has negative number of soldiers
        return false;
      }
    }
    return true;
  }

}
