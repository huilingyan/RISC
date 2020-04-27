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
    shared.Map map_cardused = handleCard(worldmap, action);
    shared.Map map_upgraded = handleUpgrade(map_cardused, action);
    shared.Map map_moved = handleMove(map_upgraded, action);
    shared.Map map_attacked = handleAttack(map_moved, action);
    shared.Map map_techlv_upgraded = handleMaxTechLvUpgrade(map_attacked, action);
    shared.Map map_broke = handleAllianceBreak(map_techlv_upgraded, action);
    shared.Map map_allied = handleAllianceFormation(map_broke, action);
    return map_allied;
  }

   public shared.Map handleCard(
       shared.Map worldmap, Action action) {

     //deep copy
     shared.Map new_worldmap = new shared.Map(worldmap);

     HashMap<Integer, Boolean> newCardMap = action.getNewCards();
     for (HashMap.Entry<Integer, Boolean> entry : newCardMap.entrySet()) {
       int card = new_worldmap.getPlayerStatByPid(entry.getKey()).getNewCard();
       System.out.println("player " + entry.getKey() + " choose to use card " + card);
       new_worldmap.getPlayerStatByPid(entry.getKey()).activateCard(card);
       new_worldmap.getPlayerStatByPid(entry.getKey()).settleCardCost(card);
       
     }

     return new_worldmap;
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
        int upgrade_cost = army_to_upgrade.calculateUpgradeCost(army_upgraded);
        
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

    List<MoveOperation> moveFromAllyList = action.getMoveFromAllyOperations();
    for (int i = 0; i < moveFromAllyList.size(); i++) {
      MoveOperation moveOp = moveFromAllyList.get(i);
      String src = moveOp.getSrc();
      String dest = moveOp.getDest();
      Army army_move = moveOp.getArmy();
      System.out.println("move from ally, dest:" + dest + " num:" + army_move.getTotalSoldiers());
      Territory t_src = new_worldmap.getTerritoryByName(src);
      Territory t_dest = new_worldmap.getTerritoryByName(dest);
      int playerid = 0;
      int allyid;
      if (t_src != null && t_dest != null) {
        allyid = t_src.getOwnership();
        for (PlayerStat p : new_worldmap.getPlayerStats()){
          if(p.getAid() == new_worldmap.getPlayerStatByPid(allyid).getAid() &&
             p.getPid() != allyid){
            playerid = p.getPid();
          }
        }
        t_src.subtractFriendDefender(army_move);//src is ally's territory
        
        if (t_dest.getOwnership() == allyid) {
          t_dest.addFriendDefender(army_move);//move to ally's territory

        } else {
          t_dest.addDefender(army_move);//move to own territory

        }
        //System.out.println("player id " + playerid);
        //System.out.println("ally id " + allyid);
        if (new_worldmap.getPlayerStatByPid(playerid).isPortalActivated()) {
          //the player can move to any self-owned territories without costing food.
          System.out.println("using portal to move");
        } else {
          //(total size of territories moved through) * (number of units moved)
          //change CostofShortestPath to account for ally territory
          int move_cost = worldmap.CostofShortestPath(src, dest) * army_move.getTotalSoldiers();
          new_worldmap.getPlayerStatByPid(playerid).subtractFood(move_cost);
        }
      }
    }
    
    List<MoveOperation> moveList = action.getMoveOperations();
    for (int i = 0; i < moveList.size(); i++) {
      MoveOperation moveOp = moveList.get(i);
      String src = moveOp.getSrc();
      String dest = moveOp.getDest();
      Army army_move = moveOp.getArmy();
      System.out.println("move from own, dest:" + dest + " num:" + army_move.getTotalSoldiers());
      Territory t_src = new_worldmap.getTerritoryByName(src);
      Territory t_dest = new_worldmap.getTerritoryByName(dest);
      int playerid;
      
      if (t_src != null && t_dest != null) {
        playerid = t_src.getOwnership();
        t_src.subtractDefender(army_move);//src territory - unit
        if(new_worldmap.getPlayerStatByPid(playerid).isAllied() &&
        (new_worldmap.ownerstatus(t_dest, new_worldmap.getPlayerStatByPid(playerid)) == 1)) {
          t_dest.addFriendDefender(army_move);//move to ally's territory
      
        } else {
          t_dest.addDefender(army_move);//move to own territory

        }
        if(new_worldmap.getPlayerStatByPid(playerid).isPortalActivated()){
          //the player can move to any self-owned territories without costing food.
          System.out.println("using portal to move");
        } else {
          //(total size of territories moved through) * (number of units moved)
          //change CostofShortestPath to account for ally territory
          int move_cost = worldmap.CostofShortestPath(src, dest) * army_move.getTotalSoldiers();
          new_worldmap.getPlayerStatByPid(playerid).subtractFood(move_cost);
        }
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
    HashMap<String, HashMap<Integer, ArmyTuple>> combinedAttackMap =
      new HashMap<String, HashMap<Integer, ArmyTuple>>();
    //format: map<destination_territory, map<playerid, combinedArmyTuple> >
    //each territory contains a combined attack map which holds all alliance id  who attacks this territory and a combined armytuple
    

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
        int allianceid = new_worldmap.getPlayerStatByPid(playerid).getAid();
        
        if(new_worldmap.getPlayerStatByPid(playerid).isPortalActivated()){
          //the player can attack non-adjacent territories without costing food.
          System.out.println("using portal to attack");
        } else {
          System.out.println("food before attack:" + new_worldmap.getPlayerStatByPid(playerid).getFood());

          new_worldmap.getPlayerStatByPid(playerid).subtractFood(attack_cost);

          System.out.println("food after attack:" + new_worldmap.getPlayerStatByPid(playerid).getFood());
        }
        combinedAttackMap.putIfAbsent(dest, new HashMap<Integer, ArmyTuple>());
        //add territory if attack map do not contain it yet

        if (combinedAttackMap.get(dest).containsKey(allianceid) == false) {
          //the alliance do not have previous attacks on this territory
          ArmyTuple armyTuple = new ArmyTuple(army_attack, playerid);
          if (new_worldmap.getPlayerStatByPid(playerid).isCommunismActivated()) {
            //all soldiers receive a +5 bonus when Communism is active
            armyTuple.setHostCommunism();
          }
          combinedAttackMap.get(dest).put(allianceid, armyTuple);
          //add dest territory to this alliance's attack map
        } else {
          //the alliance have previous attacks on this territory
          if (playerid == combinedAttackMap.get(dest).get(allianceid).getHostId()) {
            //combine into tuple's host army
            if (new_worldmap.getPlayerStatByPid(playerid).isCommunismActivated()) {
            //all soldiers receive a +5 bonus when Communism is active
            combinedAttackMap.get(dest).get(allianceid).setHostCommunism();
          }
            combinedAttackMap.get(dest).get(allianceid).getHostArmy().joinArmy(army_attack);
            
          } else {
            //combine into tuple's ally(friend) army
            combinedAttackMap.get(dest).get(allianceid).setAllyId(playerid);
            if (new_worldmap.getPlayerStatByPid(playerid).isCommunismActivated()) {
            //all soldiers receive a +5 bonus when Communism is active
            combinedAttackMap.get(dest).get(allianceid).setAllyCommunism();
          }
            combinedAttackMap.get(dest).get(allianceid).getFriendArmy().joinArmy(army_attack);            
          }
        }

      }
    }
    
    // test combinedAttackMap correctness
    for (Map.Entry<String, HashMap<Integer, ArmyTuple>>
           t_entry : combinedAttackMap.entrySet()) {
      System.out.println("territory " + t_entry.getKey() + " was attacked by:");
       for (Map.Entry<Integer, ArmyTuple>
              p_entry : t_entry.getValue().entrySet()) {         
         System.out.println("alliance " + p_entry.getKey() + " , host sent " + p_entry.getValue().getHostArmy().getTotalSoldiers() + " units" + " , ally sent " + p_entry.getValue().getFriendArmy().getTotalSoldiers() + " units");
       }
     }   
    //execute each attack order on newmap
    executeAttackOrders(combinedAttackMap, new_worldmap);
      
    return new_worldmap;
  }

  public void executeAttackOrders(HashMap<String, HashMap<Integer, ArmyTuple>> combinedAttackMap, shared.Map worldmap) {
    
    for (HashMap.Entry<String, HashMap<Integer, ArmyTuple>> t_entry : combinedAttackMap.entrySet()) {
      //locate the territory being attacked
      Territory t_defender = worldmap.getTerritoryByName(t_entry.getKey());
      int defender_id = t_defender.getOwnership();
      Army defender_army = t_defender.getDefender();
      Army defender_friendArmy = t_defender.getFriendDefender();
      ArmyTuple defenderArmyTuple = new ArmyTuple(
            t_defender.getDefender(), defender_id,
            t_defender.getFriendDefender(), worldmap.getAllyId(defender_id));
                                                  
      int original_defender_id = t_defender.getOwnership();
      //initialize winner
      int winner_id = defender_id;
      ArmyTuple winnerArmyTuple = defenderArmyTuple;
           
      // Get all the entries in the map into a list
      List<HashMap.Entry<Integer, ArmyTuple>> entry = new ArrayList<>(t_entry.getValue().entrySet());
      // Shuffle the list, randomize attack sequence
      Collections.shuffle(entry);
      // Insert them all into a LinkedHashMap
      for (Map.Entry<Integer, ArmyTuple> p_entry : entry) {
        int hostAttackerId = p_entry.getValue().getHostId();
        int friendAttackerId = p_entry.getValue().getAllyId();
        //If a combined force takes over a new territory, the ownership belongs to whoever sent army with more costs at the beginning
        Army baseArmy = new Army();
        int attacker_id;
        if(baseArmy.calculateUpgradeCost(p_entry.getValue().getHostArmy()) >=
           baseArmy.calculateUpgradeCost(p_entry.getValue().getFriendArmy())){
             attacker_id = hostAttackerId;
        } else{
             attacker_id = friendAttackerId;
        }
        Army attacker_hostArmy = p_entry.getValue().getHostArmy();
        Army attacker_friendArmy = p_entry.getValue().getFriendArmy();
        
        System.out.println("On territory:" + t_defender.getName());
        System.out.println("alliance " + attacker_id + " ATTACKS player " + defender_id);
        System.out.println("host attacker " + hostAttackerId + " has " + attacker_hostArmy.getTotalSoldiers() + " units");
        System.out.println("friend attacker " + friendAttackerId + " has " + attacker_friendArmy.getTotalSoldiers() + " units");
        System.out.println("host defender has " + defender_army.getTotalSoldiers() + " units");
        System.out.println("friend defender has " + defender_friendArmy.getTotalSoldiers() + " units");

        //execute combat calculation
        
        calculateCombatResult(p_entry.getValue(), defenderArmyTuple);
        
        if (p_entry.getValue().getTotalSoldiers() <= 0) {
          winner_id = defender_id;
          winnerArmyTuple = defenderArmyTuple;
          
        } else {
          winner_id = attacker_id;
          winnerArmyTuple = p_entry.getValue();
          
          //attacker becomes defender for possible upcoming attacks
          defender_id = winner_id;
          defenderArmyTuple = winnerArmyTuple;
          
        }
        System.out.println("player " + winner_id + " WINS, remaining " + winnerArmyTuple.getTotalSoldiers() + " units");
            
      }

      if (winner_id != t_defender.getOwnership()) {
        //winner take over the territory and change ownership
        t_defender.setOwnership(winner_id);
        t_defender.setDefender(winnerArmyTuple.getHostArmy());
        t_defender.setFriendDefender(winnerArmyTuple.getFriendArmy());
        //update territoryNum in PlayerStat if winner is attacker
        //otherwise territoryNum do not change
        
        worldmap.getPlayerStatByPid(winner_id).addTerritoryNum(1);
        worldmap.getPlayerStatByPid(original_defender_id).subtractTerritoryNum(1);

      }
    }
  }

  public void calculateCombatResult(ArmyTuple attackerArmyTuple,
                                    ArmyTuple defenderArmyTuple){
    

    Dice atk_dice = new Dice(20);
    Dice def_dice = new Dice(20);
    //combat: roll 2 dices, one for attacker, one for defende
    while ((attackerArmyTuple.getTotalSoldiers() > 0) &&
           (defenderArmyTuple.getTotalSoldiers() > 0)) {
      //first pick out soldier with highest and lowest bonus(level) for both attacker and defender
      //(if one side only has one soldier, the soldier can fight twice if he wins the first round)
      int attackerHighestBonus;//Math.max(,
      boolean attackerIsHost;
      //on tie, choose host's soldier to fight
      if(attackerArmyTuple.getHostArmy().getHighestBonus() >=
          attackerArmyTuple.getFriendArmy().getHighestBonus()){
        attackerHighestBonus = attackerArmyTuple.getHostArmy().getHighestBonus() + attackerArmyTuple.getHostCommunismBonus();
        attackerIsHost = true;
      } else {
        attackerHighestBonus = attackerArmyTuple.getFriendArmy().getHighestBonus() + attackerArmyTuple.getAllyCommunismBonus();
        attackerIsHost = false;
      }
      int defenderLowestBonus;//Math.max(,
      boolean defenderIsHost;
      if(defenderArmyTuple.getHostArmy().getLowestBonus() <=
          defenderArmyTuple.getFriendArmy().getLowestBonus()){
        defenderLowestBonus = defenderArmyTuple.getHostArmy().getLowestBonus() + defenderArmyTuple.getHostCommunismBonus();
        defenderIsHost = true;
      } else {
        defenderLowestBonus = defenderArmyTuple.getFriendArmy().getLowestBonus()+ defenderArmyTuple.getAllyCommunismBonus();
        defenderIsHost = false;
      }
      
      //attacker’s highest bonus fights defender’s lowest bonus
      int atk_value = atk_dice.rollDice() + attackerHighestBonus;
      int def_value = def_dice.rollDice() + defenderLowestBonus;
      System.out.println("attacker value (roll + bonus) :" + atk_value);
      System.out.println("defender value (roll + bonus) :" + def_value);
      if (atk_value > def_value) {
        //one with lower points (take bonus into account) loses 1 unit
        //(in a tie defender wins)

        //remove losing soldier from Army
        if(defenderIsHost){
          defenderArmyTuple.getHostArmy().subtractSoldiers(defenderArmyTuple.getHostArmy().getLowestLevel(), 1);
        } else{
          defenderArmyTuple.getFriendArmy().subtractSoldiers(defenderArmyTuple.getFriendArmy().getLowestLevel(), 1);
        }
        
      } else {
        if(attackerIsHost){
          attackerArmyTuple.getHostArmy().subtractSoldiers(attackerArmyTuple.getHostArmy().getHighestLevel(), 1);
        } else{
          attackerArmyTuple.getFriendArmy().subtractSoldiers(attackerArmyTuple.getFriendArmy().getHighestLevel(), 1);
        }
      }
      if ((attackerArmyTuple.getTotalSoldiers() <= 0) ||
          (defenderArmyTuple.getTotalSoldiers() <= 0)) {
        //check if any side has no more soldiers
        break;
      }

      //defender’s highest bonus fights attacker’s lowest bonus
      int attackerLowestBonus;//Math.max(,
      if(attackerArmyTuple.getHostArmy().getLowestBonus() <=
          attackerArmyTuple.getFriendArmy().getLowestBonus()){
        attackerLowestBonus = attackerArmyTuple.getHostArmy().getLowestBonus() + attackerArmyTuple.getHostCommunismBonus();
        attackerIsHost = true;
      } else {
        attackerLowestBonus = attackerArmyTuple.getFriendArmy().getLowestBonus()+ attackerArmyTuple.getAllyCommunismBonus();
        attackerIsHost = false;
      }
      int defenderHighestBonus;
      if(defenderArmyTuple.getHostArmy().getHighestBonus() >=
          defenderArmyTuple.getFriendArmy().getHighestBonus()){
        defenderHighestBonus = defenderArmyTuple.getHostArmy().getHighestBonus()+ defenderArmyTuple.getHostCommunismBonus();
        defenderIsHost = true;
      } else {
        defenderHighestBonus = defenderArmyTuple.getFriendArmy().getHighestBonus()+ defenderArmyTuple.getAllyCommunismBonus();
        defenderIsHost = false;
      }
      
      atk_value = atk_dice.rollDice() + attackerLowestBonus;
      def_value = def_dice.rollDice() + defenderHighestBonus;
      System.out.println("attacker value (roll + bonus) :" + atk_value);
      System.out.println("defender value (roll + bonus) :" + def_value);
      if (atk_value > def_value) {
        //one with lower points (take bonus into account) loses 1 unit
        //(in a tie defender wins)

        //remove losing soldier from Army
        if(defenderIsHost){
          defenderArmyTuple.getHostArmy().subtractSoldiers(defenderArmyTuple.getHostArmy().getHighestLevel(), 1);
        } else{
          defenderArmyTuple.getFriendArmy().subtractSoldiers(defenderArmyTuple.getFriendArmy().getHighestLevel(), 1);
        }
        
      } else {
        if(attackerIsHost){
          attackerArmyTuple.getHostArmy().subtractSoldiers(attackerArmyTuple.getHostArmy().getLowestLevel(), 1);
        } else{
          attackerArmyTuple.getFriendArmy().subtractSoldiers(attackerArmyTuple.getFriendArmy().getLowestLevel(), 1);
        }
      }
    }
  }

  public shared.Map handleMaxTechLvUpgrade(
       shared.Map worldmap, Action action) {
    ;
     //deep copy
    shared.Map new_worldmap = new shared.Map(worldmap);

    //Max tech lv upgrade by 1
    for(PlayerStat p : new_worldmap.getPlayerStats()){
      if(p.isTechnologyWorshipActivated()){
        int tech_lv = p.getMaxTechLvl();
        if (tech_lv >= 6){
          // cannot exceed lv 6
          continue;
        } else{
          p.upgradeMaxTechLvl();
        }
      }
    }
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

  public shared.Map handleAllianceBreak(
      shared.Map worldmap, Action action) {

    //deep copy
    shared.Map new_worldmap = new shared.Map(worldmap);
    List<AttackOperation> attackList = action.getAttackOperations();
    for (int i = 0; i < attackList.size(); i++) {
      AttackOperation attackOp = attackList.get(i);
      String src = attackOp.getSrc();
      String dest = attackOp.getDest();

      Territory t_src = new_worldmap.getTerritoryByName(src);
      Territory t_dest = new_worldmap.getTerritoryByName(dest);
      int playerid;

      if (t_src != null && t_dest != null) {
        playerid = t_src.getOwnership();
        //player attacks ally's territory
        if (new_worldmap.getPlayerStatByPid(playerid).isAllied() &&
        (new_worldmap.ownerstatus(t_dest, new_worldmap.getPlayerStatByPid(playerid)) == 1)) {
          //change aid and allied status for both players
          int allyid = t_dest.getOwnership();
          new_worldmap.breakAlliance(playerid, allyid);

          System.out.println("player " + playerid + " breaks alliance with player  " + t_dest.getOwnership());

          new_worldmap = returnFriendArmy(new_worldmap, playerid, allyid);
        }
      }
    }

    return new_worldmap;
  }

  private shared.Map returnFriendArmy(shared.Map worldmap, int playerid, int allyid) {
    shared.Map new_worldmap = new shared.Map(worldmap);
    //return friendArmy to the nearest self-owned territory
    //iterate all territories for both players
    for (String tName : new_worldmap.getOwnTerritoryListName(playerid)) {
      Territory t = new_worldmap.getTerritoryByName(tName);
      if (t.getFriendDefender().getTotalSoldiers() > 0) {
        //get former ally's nearest territory from t
        //and move FriendAmry to there
        new_worldmap.getNearestTerritory(t, allyid).addDefender(t.getFriendDefender());
        System.out.println("return player " + allyid + "'s army (" + t.getFriendDefender().getTotalSoldiers()
                           + " units) from " + t.getName() + " to " + new_worldmap.getNearestTerritory(t, allyid).getName());
        t.setFriendDefender(new Army());
      }
    }

    for (String tName : new_worldmap.getOwnTerritoryListName(allyid)) {
      Territory t = new_worldmap.getTerritoryByName(tName);
      if (t.getFriendDefender().getTotalSoldiers() > 0) {
        //get former ally's nearest territory from t
        //and move FriendAmry to there
        new_worldmap.getNearestTerritory(t, playerid).addDefender(t.getFriendDefender());
        System.out.println("return player " + playerid + "'s army (" + t.getFriendDefender().getTotalSoldiers()
            + " units) from " + t.getName() + " to " + new_worldmap.getNearestTerritory(t, playerid).getName());
        t.setFriendDefender(new Army());
      }
    }
    return new_worldmap;
  }

  public shared.Map handleAllianceFormation(
         shared.Map worldmap, Action action){
     //deep copy
     shared.Map new_worldmap = new shared.Map(worldmap);

     HashMap<Integer, Integer> allianceMap = action.getAllianceRequests();
     for (HashMap.Entry<Integer, Integer> entry : allianceMap.entrySet()) {
       //if A send alliance request with B,
       //then B must send alliance request with A on the same turn,
       //otherwise alliance will not form
       //e.g. request<0,1> must find request<1,0>
       if(allianceMap.get(entry.getValue()) == entry.getKey()) {
         //both player should be in unallied status to form new alliance
         if(!new_worldmap.getPlayerStatByPid(entry.getKey()).isAllied() &&
            !new_worldmap.getPlayerStatByPid(entry.getValue()).isAllied()) {
           //change pid and allied status
           new_worldmap.formAlliance(entry.getKey(), entry.getValue());
          
           System.out.println("player " + entry.getKey() + " form alliance with player  " + entry.getValue());
         }
       }
     }
     return new_worldmap;
  }
}
