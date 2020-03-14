package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import shared.*;

public class AttackHandler extends Handler {

  @Override
  public ArrayList<Territory> handleAction(
      ArrayList<Territory> map, Action action) {
    //ArrayList<Territory> newmap = map;
    ArrayList<Territory> newmap = new ArrayList<Territory>();
    for (int m = 0; m < map.size(); m++) {
      Territory t = new Territory(map.get(m));
      //deep copy, do not affect original map
      newmap.add(t);
    }

    //if player A attacks territory X with units from multiple of her own
    //territories, they count as a single combined force
    HashMap<String, HashMap<Integer, Integer>> combinedAttackMap =
      new HashMap<String, HashMap<Integer, Integer>>();
    //format: map<destination_territory, map<playerid, combinedNum> >
    //each territory contains a combined attack map which holds all playerid who attacks this territory and a combined unit number

    List<AttackOperation> attackList = action.getAttackOperations();
    for (int i = 0; i < attackList.size(); i++) {
      AttackOperation attackOp = attackList.get(i);
      String src = attackOp.getSrc();
      String dest = attackOp.getDest();
      int num = attackOp.getNum();

      //System.out.println("dest:" + dest + " num:" + num);
      Territory t_src = findTerritorybyString(newmap, src);
      Territory t_dest = findTerritorybyString(newmap, dest);
      if (t_src != null && t_dest != null) {
        t_src.subtractDefender(num);//src territory - unit
        //attack op immediately result in all attackers leaving home territory
        int playerid = t_src.getOwnership();
        combinedAttackMap.putIfAbsent(dest, new HashMap<Integer, Integer>());
        //add territory if attack map do not contain it yet

        if (combinedAttackMap.get(dest).get(playerid) == null) {
          //the player do not have previous attacks on this territory
          combinedAttackMap.get(dest).put(playerid, num);
          //add dest territory to this player's attack map
        } else {
          //the player have previous attacks on this territory
          int combinedNum = combinedAttackMap.get(dest).get(playerid) + num;
          combinedAttackMap.get(dest).replace(playerid, combinedNum);
          //replace attack unit number with latest combined number
        }

      }
    }
    /*
    // test combinedAttackMap correctness
    for (Map.Entry<String, HashMap<Integer, Integer>>
           t_entry : combinedAttackMap.entrySet()) {
      System.out.println("territory " + t_entry.getKey() + " was attacked by:");
       for (Map.Entry<Integer, Integer>
              p_entry : t_entry.getValue().entrySet()) {         
         System.out.println("player " + p_entry.getKey() + " with " + p_entry.getValue() + " units");
       }
     }
    */
    
    //execute each attack order on newmap
    Combat(combinedAttackMap, newmap);
   
    
    return newmap;
  }

  public void Combat(HashMap<String, HashMap<Integer, Integer>> combinedAttackMap, ArrayList<Territory> newmap){
    Dice atk_dice = new Dice(20);
    Dice def_dice = new Dice(20);
    for (Map.Entry<String, HashMap<Integer, Integer>>
           t_entry : combinedAttackMap.entrySet()) {
      //locate the territory being attacked
      Territory t_defender = findTerritorybyString(newmap, t_entry.getKey());
      int defender_id = t_defender.getOwnership();
      int defender_unit_num = t_defender.getDefenderNum();
      //initialize winner
      int winner_id = defender_id;
      int winner_unit_num = defender_unit_num;
      
      while (!t_entry.getValue().isEmpty()) {
        //if multiple players attack the same territory,
    //each attack is resolved sequentially
    //first pick a random attacker, fights with current defender
    //then pick second random attack fights with winner of first attack
    //until all attack are handled
        for (Iterator<Map.Entry<Integer, Integer> >
               it = t_entry.getValue().entrySet().iterator();it.hasNext();) {
          Map.Entry<Integer, Integer> p_entry = it.next();
          int rnd = (int) (Math.random()*2);
          if (rnd == 0) {
            //randomize attack sequence
            //50% to skip current attack order, making it at back of sequence 
            continue;
          } else {
            //execute combat calculation 
            int attacker_id = p_entry.getKey();
            int attacker_unit_num = p_entry.getValue();
            //System.out.println("On territory:" + t_defender.getName());
            //System.out.println("player " + attacker_id + " with " + attacker_unit_num + " units" + " ATTACKS player " + defender_id + " with " + defender_unit_num + " units");
            while ((attacker_unit_num > 0) && (defender_unit_num > 0)) {
              //combat: roll 2 dices, one for attacker, one for defender
              //one with lower points loses 1 unit (in a tie defender wins)
              if (atk_dice.rollDice() > def_dice.rollDice()) {
                defender_unit_num--;
              } else {
                attacker_unit_num--;
              }
            }
            if (attacker_unit_num <= 0) {
              winner_id = defender_id;
              winner_unit_num = defender_unit_num;
            } else {
              winner_id = attacker_id;
              winner_unit_num = attacker_unit_num;
              //attacker becomes defender for possible upcoming attacks
              defender_id = winner_id;
              defender_unit_num = winner_unit_num;
              
            }
            //System.out.println("player " + winner_id + " WINS, remaining " + winner_unit_num + " units");
            it.remove();
          }      
        }
        
      }
      //winner take over the territory and change ownership
      t_defender.setOwnership(winner_id);
      t_defender.setDefenderNum(winner_unit_num);
    }
  }
}
