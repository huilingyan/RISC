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
      shared.Map worldmap, Action action) {
    
    shared.Map map_upgraded = handleUpgrade(worldmap, action);
    shared.Map map_moved = handleMove(map_upgraded, action);
    shared.Map map_attacked = handleAttack(map_moved, action);
    shared.Map map_techlv_upgraded = handleMaxTechLvUpgrade(map_attacked, action);
    return map_techlv_upgraded;
  }
  
   public shared.Map handleUpgrade(
       shared.Map worldmap, Action action) {
 
     //deep copy
     shared.Map new_worldmap = new shared.Map(worldmap);
     
     List<UpgradeOperation> upgradeList = action.getUpgradeOperations();
    for (int i = 0; i < upgradeList.size(); i++) {
      UpgradeOperation upgradeOp = upgradeList.get(i);
      String dest = upgradeOp.getDest();
      Army army_to_upgrade = upgradeOp.getArmyToUpgrade();
      Army army_upgraded = upgradeOp.getArmy();
      
      System.out.println("dest:" + dest + " num:" + army_to_upgrade.getTotalSoldiers());
      
      Territory t_dest = new_worldmap.getTerritoryByName(dest);
      if (t_dest != null) {
        t_dest.subtractDefender(army_to_upgrade);//remove soldiers to upgrade
        t_dest.addDefender(army_upgraded);//add back upgraded soldiers
        
        int playerid = t_dest.getOwnership();
        //instance of OperationValidator   
        OperationValidator ov = new OperationValidator(playerid, new_worldmap);
        int upgrade_cost = ov.calculateUpgradeCost(army_to_upgrade, army_upgraded);
        
        System.out.println("gold before upgrade:" + new_worldmap.getPlayerStatByPid(playerid).getGold());

        new_worldmap.getPlayerStatByPid(playerid).subtractGold(upgrade_cost);

        System.out.println("gold after upgrade:" + new_worldmap.getPlayerStatByPid(playerid).getGold());
      }
    }
     return new_worldmap;
   }

   
  
  public shared.Map handleMove(
      shared.Map worldmap, Action action) {
    
    //deep copy
    shared.Map new_worldmap = new shared.Map(worldmap);

    List<MoveOperation> moveList = action.getMoveOperations();
    for (int i = 0; i < moveList.size(); i++) {
      MoveOperation moveOp = moveList.get(i);
      String src = moveOp.getSrc();
      String dest = moveOp.getDest();
      Army army_move = moveOp.getArmy();
      System.out.println("dest:" + dest + " num:" + army_move.getTotalSoldiers());
      Territory t_src = new_worldmap.getTerritoryByName(src);
      Territory t_dest = new_worldmap.getTerritoryByName(dest);
      if (t_src != null && t_dest != null) {
        t_src.subtractDefender(army_move);//src territory - unit
        t_dest.addDefender(army_move);//dest territory + unit
        
        //(total size of territories moved through) * (number of units moved)
        int move_cost = worldmap.CostofShortestPath(src, dest) * army_move.getTotalSoldiers();
        int playerid = t_src.getOwnership();
        System.out.println("food before move:" + new_worldmap.getPlayerStatByPid(playerid).getFood());
        new_worldmap.getPlayerStatByPid(playerid).subtractFood(move_cost);

        System.out.println("food after move:" + new_worldmap.getPlayerStatByPid(playerid).getFood());
      }
    }
    return new_worldmap;
  }

  public shared.Map handleAttack(
    shared.Map worldmap, Action action) {
    
    //deep copy
    shared.Map new_worldmap = new shared.Map(worldmap);

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
      Territory t_src = new_worldmap.getTerritoryByName(src);
      Territory t_dest = new_worldmap.getTerritoryByName(dest);
      if (t_src != null && t_dest != null) {
        t_src.subtractDefender(army_attack);//src territory - unit
        //attack op immediately result in all attackers leaving home territory
        
        int attack_cost = 1 * army_attack.getTotalSoldiers();
        //An attack order now costs 1 food per unit attacking
        int playerid = t_src.getOwnership();
        System.out.println("food before attack:" + new_worldmap.getPlayerStatByPid(playerid).getFood());

        new_worldmap.getPlayerStatByPid(playerid).subtractFood(attack_cost);

        System.out.println("food after attack:" + new_worldmap.getPlayerStatByPid(playerid).getFood());
        
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
    Combat(combinedAttackMap, new_worldmap);
      
    return new_worldmap;
  }

  public void Combat(HashMap<String, HashMap<Integer, Army>> combinedAttackMap, shared.Map worldmap) {
    
    for (HashMap.Entry<String, HashMap<Integer, Army>> t_entry : combinedAttackMap.entrySet()) {
      //locate the territory being attacked
      Territory t_defender = worldmap.getTerritoryByName(t_entry.getKey());
      int defender_id = t_defender.getOwnership();
      Army defender_army = t_defender.getDefender();
      //initialize winner
      int winner_id = defender_id;
      Army winner_army = defender_army;
      //initialize loser
      int loser_id = defender_id;
        
      // Get all the entries in the map into a list
      List<HashMap.Entry<Integer, Army>> entry = new ArrayList<>(t_entry.getValue().entrySet());
      // Shuffle the list, randomize attack sequence
      Collections.shuffle(entry);
      // Insert them all into a LinkedHashMap
      for (Map.Entry<Integer, Army> p_entry : entry) {         
        int attacker_id = p_entry.getKey();
        Army attacker_army = p_entry.getValue();//get attacker info
        System.out.println("On territory:" + t_defender.getName());
        System.out.println("player " + attacker_id + " with " + attacker_army.getTotalSoldiers() + " units"
            + " ATTACKS player " + defender_id + " with " + defender_army.getTotalSoldiers() + " units");

        //execute combat calculation
        calculateCombatResult(attacker_id, attacker_army, defender_id, defender_army);
        
        if (attacker_army.getTotalSoldiers() <= 0) {
          winner_id = defender_id;
          winner_army = defender_army;
          loser_id = attacker_id;
        } else {
          winner_id = attacker_id;
          winner_army = attacker_army;
          loser_id = defender_id;
          //attacker becomes defender for possible upcoming attacks
          defender_id = winner_id;
          defender_army = winner_army;

        }
        System.out.println("player " + winner_id + " WINS, remaining " + winner_army.getTotalSoldiers() + " units");
            
      }
        
      //winner take over the territory and change ownership
      t_defender.setOwnership(winner_id);
      t_defender.setDefender(winner_army);
      //update territoryNum in PlayerStat of the winner and the loser 
      worldmap.getPlayerStatByPid(winner_id).addTerritoryNum(1);
      worldmap.getPlayerStatByPid(loser_id).subtractTerritoryNum(1);
    }
  }

  public void calculateCombatResult(int attacker_id, Army attacker_army,
                                    int defender_id, Army defender_army){
    

    Dice atk_dice = new Dice(20);
    Dice def_dice = new Dice(20);
    //combat: roll 2 dices, one for attacker, one for defende
    while ((attacker_army.getTotalSoldiers() > 0) &&
           (defender_army.getTotalSoldiers() > 0)) {

          
      //first pick out soldier with highest and lowest bonus(level) for both attacker and defender
      //(if one side only has one soldier, the soldier can fight twice if he wins the first round)

      //attacker’s highest bonus fights defender’s lowest bonus
      int atk_value = atk_dice.rollDice() + attacker_army.getHighestBonus();
      int def_value = def_dice.rollDice() + defender_army.getLowestBonus();
      if (atk_value > def_value) {
        //one with lower points (take bonus into account) loses 1 unit
        //(in a tie defender wins)

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
  }

  public shared.Map handleMaxTechLvUpgrade(
       shared.Map worldmap, Action action) {
    ;
     //deep copy
     shared.Map new_worldmap = new shared.Map(worldmap);
     
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
     for (HashMap.Entry<Integer, Boolean>
              entry : upgrade_map.entrySet()) {
       
         System.out.println("player " + entry.getKey() + " upgrade max tech lv: " + entry.getValue());
         
         int tech_lv = new_worldmap.getPlayerStatByPid(entry.getKey()).getMaxTechLvl();
         if (tech_lv >= 6){
           // cannot exceed lv 6
           continue;
         }
         //deduct resource for upgrade
         int cost = upgrade_cost_list.get(tech_lv);
         new_worldmap.getPlayerStatByPid(entry.getKey()).subtractGold(cost);
         //maxTechLvl+1
         new_worldmap.getPlayerStatByPid(entry.getKey()).upgradeMaxTechLvl();
       }

     return new_worldmap;
  }
}
