package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

public class Action implements Serializable {
  private ArrayList<InitOperation> initOperations;
  private ArrayList<MoveOperation> moveOperations;
  private ArrayList<AttackOperation> attackOperations;
  private ArrayList<UpgradeOperation> upgradeOperations;
  private HashMap<Integer, Boolean> upgradeMaxTechLv;//pid, upgrade or not
  
  //first handle upgrade, then move, finally attack
  //gui should disable UpgradeUnits after client made a Move or Attack operation

  public Action() {
    this.initOperations= new ArrayList<InitOperation>();
    this.moveOperations= new ArrayList<MoveOperation>();
    this.attackOperations = new ArrayList<AttackOperation>();
    this.upgradeOperations = new ArrayList<UpgradeOperation>();
    upgradeMaxTechLv = new HashMap<Integer, Boolean>();
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
  
  public boolean isUpgradeMaxTechLv(int pid) {
    if(upgradeMaxTechLv.get(pid) == true){
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

  public void addMoveOperation(MoveOperation mop) {
    this.moveOperations.add(mop);
  }

  public void addAttackOperation(AttackOperation aop) {
    this.attackOperations.add(aop);
  }

  public void concatInitOperation(Action clientaction) {
    this.initOperations.addAll(clientaction.initOperations);
  }


  public void concatGameOperation(Action clientaction) {
    this.moveOperations.addAll(clientaction.moveOperations);
    this.attackOperations.addAll(clientaction.attackOperations);
    this.upgradeOperations.addAll(clientaction.upgradeOperations);
    //if any player choose to upgrade his max tech level, set it to true
    for (Map.Entry<Integer, Boolean> entry :
    clientaction.getUpgradeMaxTechHashMap().entrySet()) {
      if (entry.getValue() == true) {//untested
        upgradeMaxTechLv.replace(entry.getKey(), entry.getValue());
      }
    }
  }
  
}
