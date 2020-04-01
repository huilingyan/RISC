package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Arrays;
import shared.*;

public class GameHandler extends Handler {
  @Override
  public shared.Map handleAction(
      shared.Map WorldMap, Action action) {
    
    shared.Map map_upgraded = handleUpgrade(WorldMap, action);
    shared.Map map_moved = handleMove(map_upgraded, action);
    shared.Map map_attacked = handleAttack(map_moved, action);
    shared.Map map_techlv_upgraded = handleMaxTechLvUpgrade(map_attacked, action);
    return map_techlv_upgraded;
  }
  
   public shared.Map handleUpgrade(
       shared.Map WorldMap, Action action) {
     ArrayList<Territory> newmap = copyMap(WorldMap.getTerritories());
     ArrayList<PlayerStat> newstats = copyPlayerStats(WorldMap.getPlayerStats());
     //deep copy
     shared.Map NewMap = new shared.Map(newmap, newstats);
     
     List<UpgradeOperation> upgradeList = action.getUpgradeOperations();
    for (int i = 0; i < upgradeList.size(); i++) {
      UpgradeOperation upgradeOp = upgradeList.get(i);
      String dest = upgradeOp.getDest();
      Army army_to_upgrade = upgradeOp.getArmyToUpgrade();
      Army army_upgraded = upgradeOp.getArmy();
      
      System.out.println("dest:" + dest + " num:" + army_to_upgrade.getTotalSoldiers());
      
      Territory t_dest = NewMap.getTerritoryByName(dest);
      if (t_dest != null) {
        t_dest.subtractDefender(army_to_upgrade);//remove soldiers to upgrade
        t_dest.addDefender(army_upgraded);//add back upgraded soldiers
              
        int upgrade_cost = calculateUpgradeCost(army_to_upgrade, army_upgraded);
        int playerid = t_dest.getOwnership();
        
        System.out.println("gold before upgrade:" + NewMap.getPlayerStatByPid(playerid).getGold());

        NewMap.getPlayerStatByPid(playerid).subtractGold(upgrade_cost);

        System.out.println("gold after upgrade:" + NewMap.getPlayerStatByPid(playerid).getFood());
      }
    }
     return NewMap;
   }

   public int calculateUpgradeCost(Army army1, Army army2) {
     int cost = 0;
     ArrayList<Integer> upgrade_cost_list =
         new ArrayList<Integer>(Arrays.asList(0, 3, 11, 30, 55, 90, 140));
     Army army_to_upgrade = new Army(army1);
     Army army_upgraded = new Army(army2);
     
     while (army_upgraded.getTotalSoldiers() != 0) {
       //calculate number of soldiers to upgrade for highest level
       int upgrade_num = army_upgraded.getSoldierNumber(army_upgraded.getHighestLevel());
       System.out.println("number of soliders:" + upgrade_num);
       System.out.println("upgrade from lv " + army_to_upgrade.getHighestLevel() + "to lv " + army_upgraded.getHighestLevel());
       //get cost difference from level difference
       //e.g. cost for lv1-->lv3 = (30-0) - (3-0)
       int cost_difference = upgrade_cost_list.get(army_upgraded.getHighestLevel())
           - upgrade_cost_list.get(army_to_upgrade.getHighestLevel());
       //cumulatively add cost for upgrade
        cost += cost_difference * upgrade_num;
       
       //remove calculated soldiers from army
        army_upgraded.subtractSoldiers(army_upgraded.getHighestLevel(), upgrade_num);
       
        army_to_upgrade.subtractSoldiersFromHighestLv(upgrade_num);
     }
     
     return cost;
   }
  
  public shared.Map handleMove(
      shared.Map WorldMap, Action action) {
    ArrayList<Territory> newmap = copyMap(WorldMap.getTerritories());
    ArrayList<PlayerStat> newstats = copyPlayerStats(WorldMap.getPlayerStats());
    //deep copy
    shared.Map NewMap = new shared.Map(newmap, newstats);

    List<MoveOperation> moveList = action.getMoveOperations();
    for (int i = 0; i < moveList.size(); i++) {
      MoveOperation moveOp = moveList.get(i);
      String src = moveOp.getSrc();
      String dest = moveOp.getDest();
      Army army_move = moveOp.getArmy();
      System.out.println("dest:" + dest + " num:" + army_move.getTotalSoldiers());
      Territory t_src = NewMap.getTerritoryByName(src);
      Territory t_dest = NewMap.getTerritoryByName(dest);
      if (t_src != null && t_dest != null) {
        t_src.subtractDefender(army_move);//src territory - unit
        t_dest.addDefender(army_move);//dest territory + unit
        //(total size of territories moved through) * (number of units moved)
        //TODO: call minimum path finder which returns total size of the path
        int move_cost = 10 * army_move.getTotalSoldiers();
        int playerid = t_src.getOwnership();
        System.out.println("food before move:" + NewMap.getPlayerStatByPid(playerid).getFood());

        NewMap.getPlayerStatByPid(playerid).subtractFood(move_cost);

        System.out.println("food after move:" + NewMap.getPlayerStatByPid(playerid).getFood());
      }
    }
    return NewMap;
  }

  public shared.Map handleAttack(
    shared.Map WorldMap, Action action) {
    ArrayList<Territory> newmap = copyMap(WorldMap.getTerritories());
    ArrayList<PlayerStat> newstats = copyPlayerStats(WorldMap.getPlayerStats());
    //deep copy
    shared.Map NewMap = new shared.Map(newmap, newstats);

    //if player A attacks territory X with units from multiple of her own
    //territories, they count as a single combined force
    HashMap<String, HashMap<Integer, Army>> combinedAttackMap =
      new HashMap<String, HashMap<Integer, Army>>();
    //format: map<destination_territory, map<playerid, combinedArmy> >
    //each territory contains a combined attack map which holds all playerid who attacks this territory and a combined army

    List<AttackOperation> attackList = action.getAttackOperations();
    for (int i = 0; i < attackList.size(); i++) {
      AttackOperation attackOp = attackList.get(i);
      String src = attackOp.getSrc();
      String dest = attackOp.getDest();
      Army army_attack = attackOp.getArmy();

      System.out.println("dest:" + dest + " num:" + army_attack.getTotalSoldiers());
      Territory t_src = NewMap.getTerritoryByName(src);
      Territory t_dest = NewMap.getTerritoryByName(dest);
      if (t_src != null && t_dest != null) {
        t_src.subtractDefender(army_attack);//src territory - unit
        //attack op immediately result in all attackers leaving home territory
        
        int attack_cost = 1 * army_attack.getTotalSoldiers();
        //An attack order now costs 1 food per unit attacking
        int playerid = t_src.getOwnership();
        System.out.println("food before attack:" + NewMap.getPlayerStatByPid(playerid).getFood());

        NewMap.getPlayerStatByPid(playerid).subtractFood(attack_cost);

        System.out.println("food after attack:" + NewMap.getPlayerStatByPid(playerid).getFood());
        
        combinedAttackMap.putIfAbsent(dest, new HashMap<Integer, Army>());
        //add territory if attack map do not contain it yet

        if (combinedAttackMap.get(dest).containsKey(playerid) == false) {
          //the player do not have previous attacks on this territory
          combinedAttackMap.get(dest).put(playerid, army_attack);
          //add dest territory to this player's attack map
        } else {
          //the player have previous attacks on this territory
          Army combinedArmy = new Army(army_attack);
          combinedArmy.joinArmy(combinedAttackMap.get(dest).get(playerid));  
          combinedAttackMap.get(dest).replace(playerid, combinedArmy);
          
        }

      }
    }
    
    // test combinedAttackMap correctness
    for (Map.Entry<String, HashMap<Integer, Army>>
           t_entry : combinedAttackMap.entrySet()) {
      System.out.println("territory " + t_entry.getKey() + " was attacked by:");
       for (Map.Entry<Integer, Army>
              p_entry : t_entry.getValue().entrySet()) {         
         System.out.println("player " + p_entry.getKey() + " with " + p_entry.getValue().getTotalSoldiers() + " units");
       }
     }
    
    
    //execute each attack order on newmap
    Combat(combinedAttackMap, NewMap);
   
    
    return NewMap;
  }

  public void Combat(HashMap<String, HashMap<Integer, Army>> combinedAttackMap, shared.Map WorldMap) {
    Dice atk_dice = new Dice(20);
    Dice def_dice = new Dice(20);
    //combat: roll 2 dices, one for attacker, one for defender
    for (Map.Entry<String, HashMap<Integer, Army>> t_entry : combinedAttackMap.entrySet()) {
      //locate the territory being attacked
      Territory t_defender = WorldMap.getTerritoryByName(t_entry.getKey());
      int defender_id = t_defender.getOwnership();
      Army defender_army = t_defender.getDefender();
      //initialize winner
      int winner_id = defender_id;
      Army winner_army = defender_army;

      //if multiple players attack the same territory,
      //each attack is resolved sequentially
      //first pick a random attacker, fights with current defender
      //then pick second random attack fights with winner of first attack
      //until all attack are handled
        
      // Get all the entries in the map into a list
      List<Map.Entry<Integer, Army>> entry = new ArrayList<>(t_entry.getValue().entrySet());
 
      // Shuffle the list, randomize attack sequence
      Collections.shuffle(entry);
 
      // Insert them all into a LinkedHashMap
      //Map<Integer, Integer> shuffledmap = new LinkedHashMap<>();
      for (Map.Entry<Integer, Army> p_entry : entry) {
        //execute combat calculation 
        int attacker_id = p_entry.getKey();
        Army attacker_army = p_entry.getValue();
        System.out.println("On territory:" + t_defender.getName());
        System.out.println("player " + attacker_id + " with " + attacker_army.getTotalSoldiers() + " units"
            + " ATTACKS player " + defender_id + " with " + defender_army.getTotalSoldiers() + " units");
        
        while ((attacker_army.getTotalSoldiers() > 0) &&
            (defender_army.getTotalSoldiers() > 0)) {

          //one with lower points (take bonus into account) loses 1 unit
          //(in a tie defender wins)

          //first pick out soldier with highest and lowest bonus(level) for both attacker and defender
          //(if one side only has one soldier, the soldier can fight twice if he wins the first round)

          //attacker’s highest bonus fights defender’s lowest bonus
          int atk_value = atk_dice.rollDice() + attacker_army.getHighestBonus();
          int def_value = def_dice.rollDice() + defender_army.getLowestBonus();
          if (atk_value > def_value) {
            //remove losing soldier from Army
            defender_army.subtractSoldiers(defender_army.getLowestLevel(), 1);
          } else {
            attacker_army.subtractSoldiers(attacker_army.getHighestLevel(), 1);
          }
          if ((attacker_army.getTotalSoldiers() <= 0) ||
              (defender_army.getTotalSoldiers() <= 0)) {
            //check if any side has no more soldiers
            break;
          }

          //defender’s highest bonus fights attacker’s lowest bonus
          atk_value = atk_dice.rollDice() + attacker_army.getLowestBonus();
          def_value = def_dice.rollDice() + defender_army.getHighestBonus();
          if (atk_value > def_value) {
            //remove losing soldier from Army
            defender_army.subtractSoldiers(defender_army.getHighestLevel(), 1);
          } else {
            attacker_army.subtractSoldiers(attacker_army.getLowestLevel(), 1);
          }

        }
        
        if (attacker_army.getTotalSoldiers() <= 0) {
          winner_id = defender_id;
          winner_army = defender_army;
        } else {
          winner_id = attacker_id;
          winner_army = attacker_army;
          //attacker becomes defender for possible upcoming attacks
          defender_id = winner_id;
          defender_army = winner_army;

        }
        System.out.println("player " + winner_id + " WINS, remaining " + winner_army.getTotalSoldiers() + " units");
            
      }
        
      //winner take over the territory and change ownership
      t_defender.setOwnership(winner_id);
      t_defender.setDefender(winner_army);
    }
  }

  public shared.Map handleMaxTechLvUpgrade(
       shared.Map WorldMap, Action action) {
     ArrayList<Territory> newmap = copyMap(WorldMap.getTerritories());
     ArrayList<PlayerStat> newstats = copyPlayerStats(WorldMap.getPlayerStats());
     //deep copy
     shared.Map NewMap = new shared.Map(newmap, newstats);
     
     //upgrade max tech lv is executed after all other orders were executed
     //so that its effect are available starting next turn

     HashMap<Integer, Boolean> upgrade_map = action.getUpgradeMaxTechHashMap();
     ArrayList<Integer> upgrade_cost_list =
       new ArrayList<Integer>(Arrays.asList(0, 50, 75, 125, 200, 300));
     //Upgrade Level Cost
     //1 ->2 50
     //2 ->3 75
     //3 ->4 125
     //4 ->5 200
     //5 ->6 300
     for (Map.Entry<Integer, Boolean>
              entry : upgrade_map.entrySet()) {
       
         System.out.println("player " + entry.getKey() + " upgrade max tech lv: " + entry.getValue());
         
         int tech_lv = NewMap.getPlayerStatByPid(entry.getKey()).getMaxTechLvl();
         if (tech_lv >= 6){
           // cannot exceed lv 6
           continue;
         }
         //deduct resource for upgrade
         int cost = upgrade_cost_list.get(tech_lv);
         NewMap.getPlayerStatByPid(entry.getKey()).subtractGold(cost);
         //maxTechLvl+1
         NewMap.getPlayerStatByPid(entry.getKey()).upgradeMaxTechLvl();
       }

     return NewMap;
  }
}
