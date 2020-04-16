package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Action implements Serializable {
  private ArrayList<InitOperation> initOperations;
  private ArrayList<MoveOperation> moveOperations;
  private ArrayList<AttackOperation> attackOperations;
  private ArrayList<UpgradeOperation> upgradeOperations;
  private HashMap<Integer, Boolean> upgradeMaxTechLv;//pid, upgrade or not
  private HashMap<Integer, Integer> allianceRequests;// pid1 allies pid2
  private HashMap<Integer, Integer> newCards; //pid, cid
  //which player uses which card
  
  //first handle upgrade, then move, finally attack
  //gui should disable UpgradeUnits after client made a Move or Attack operation

  public Action() {
    this.initOperations= new ArrayList<InitOperation>();
    this.moveOperations= new ArrayList<MoveOperation>();
    this.attackOperations = new ArrayList<AttackOperation>();
    this.upgradeOperations = new ArrayList<UpgradeOperation>();
    this.upgradeMaxTechLv = new HashMap<Integer, Boolean>();
    this.allianceRequests = new HashMap<Integer, Integer>();
    this.newCards = new HashMap<Integer, Integer>();
  }

  public ArrayList<InitOperation> getInitOperations() {
      return initOperations;
  }

  public ArrayList<MoveOperation> getMoveOperations() {
      return moveOperations;
  }

  public ArrayList<AttackOperation> getAttackOperations() {
      return attackOperations;
  }

  public ArrayList<UpgradeOperation> getUpgradeOperations() {
      return upgradeOperations;
  }

  public HashMap<Integer, Boolean> getUpgradeMaxTechHashMap() {
      return upgradeMaxTechLv;
  }

  public HashMap<Integer, Integer> getAllianceRequests() {
    return allianceRequests;
  }

  public HashMap<Integer, Integer> getNewCards(){
    return newCards;
  }
  
  public boolean isUpgradeMaxTechLv(int pid) {
      if(upgradeMaxTechLv.get(pid) == true) {
          return true;
      } else {//get() returns null
          return false;
      }
    //return upgradeMaxTechLv.get(pid);
  }

  public void upgradeMaxTechLv(int pid) {
    //this method has the same name with variable
      upgradeMaxTechLv.put(pid, true);
  }
    
  public void addInitOperation(InitOperation iop) {
      this.initOperations.add(iop);
  }

  public void addUpgradeOperation(UpgradeOperation uop) {
      this.upgradeOperations.add(uop);
  }

  public void addMoveOperation(MoveOperation mop) {
      this.moveOperations.add(mop);
  }

  public void addAttackOperation(AttackOperation aop) {
    this.attackOperations.add(aop);
  }

  public void addAllianceRequest(int pid1, int pid2) {
    this.allianceRequests.put(pid1, pid2);
  }

  public void useNewCard(int pid, int cid) {
    //player pid choose to use card cid this turn
    this.newCards.put(pid, cid);
  }

  public void concatInitOperation(Action clientaction) {
      this.initOperations.addAll(clientaction.initOperations);
  }


  public void concatGameOperation(Action clientaction) {
    
    this.upgradeOperations.addAll(clientaction.upgradeOperations);
    this.moveOperations.addAll(clientaction.moveOperations);
    this.attackOperations.addAll(clientaction.attackOperations);
    //if any player choose to upgrade his max tech level, set it to true
    for (HashMap.Entry<Integer, Boolean> entry :
    clientaction.getUpgradeMaxTechHashMap().entrySet()) {

      if (entry.getValue()) {
        //replace is not suitable here because it doesn't replace null value
        upgradeMaxTechLv.put(entry.getKey(), entry.getValue());
        //System.out.println("player " + entry.getKey() + " choose to upgrade max tech lv " + upgradeMaxTechLv.get(entry.getKey()) );
      }
    }

    //combine alliance requests
    for (HashMap.Entry<Integer, Integer> entry :
    clientaction.getAllianceRequests().entrySet()) {
      if (!allianceRequests.containsKey(entry.getKey())) {
        //if player has no alliance requests with another player
        //add request to hashmap
        //but if AB requested alliance, second request AC will be neglected
        allianceRequests.put(entry.getKey(), entry.getValue());
      }
    }

    //combine card activation
    for (HashMap.Entry<Integer, Integer> entry :
    clientaction.getNewCards().entrySet()) {
      if (!newCards.containsKey(entry.getKey())) {
        //if player didn't use any card this turn
        //add card to hashmap
        newCards.put(entry.getKey(), entry.getValue());
      }
    }
  }
  
}
