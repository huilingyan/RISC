package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Army implements Serializable{
  private ArrayList<Integer> Soldiers;  // size of 7
  //Soldiers[0] or Soldiers.get(0) indicates the number of lv 0 soldiers

  public Army() {
    this.Soldiers = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0));
    //level 0 - level 6
    //may need to handle exceptions if outbound
  }

  public Army(ArrayList<Integer> arr){//constructor
    this.Soldiers = new ArrayList<Integer>();
    for (int i = 0; i < 7; i++) {   // size of solders is 7, 0-6 levels
      if (i <= (arr.size()-1)){
        Soldiers.add(arr.get(i));
      } else {
        Soldiers.add(0);
      }
      
    }
  }

  public Army(int n) {
    //assign n level 0 soldiers
    this.Soldiers = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0));
    this.Soldiers.set(0, n);
    
  }
  public Army(Army rhs) {//copy constructor
    this.Soldiers = new ArrayList<Integer>(rhs.Soldiers);
    
  }
  
  public void joinArmy(Army rhs) {
    //join two armies into one combined army
    //add the value of each level respectively
    for (int i = 0; i < Soldiers.size(); i++) {
      Soldiers.set(i, Soldiers.get(i) + rhs.Soldiers.get(i));
    }
  }
  
  public void subtractArmy(Army rhs) {
    //subtract this army to right handside army
    for (int i = 0; i < Soldiers.size(); i++) {
      Soldiers.set(i, Soldiers.get(i) - rhs.Soldiers.get(i));
    }
  }
  
  public int getSoldierNumber(int lv) {
    if (lv > 6) {
      return 0;
      //handle exceptions if outbound
    }
    return Soldiers.get(lv);
  }

  public void setSoldierNumber(int lv, int num) {
    if (lv > 6) {
      return;
      //handle exceptions if outbound
    }
    Soldiers.set(lv, num);
  }

  public void addSoldiers(int lv, int num) {
    if (lv > 6) {
      return;
      //handle exceptions if outbound
    }
    Soldiers.set(lv, Soldiers.get(lv) + num);
  }

  public void subtractSoldiers(int lv, int num) {
    if (lv > 6) {
      return;
      //handle exceptions if outbound
    }
    Soldiers.set(lv, Soldiers.get(lv) - num);
  }

  public int getHighestBonus() {
    for (int i = Soldiers.size() - 1; i >= 0; i--) {
      if (Soldiers.get(i) > 0) {
        return getBonus(i);
      }
    }
    //empty army
    return getBonus(0);
  }
  
  public int getLowestBonus(){
    for (int i = 0; i < Soldiers.size(); i++) {
      if (Soldiers.get(i) > 0) {
        return getBonus(i);
      }
    }
    //empty army
    return getBonus(0);
  }

  public int getBonus(int lv) {
    //match each level with a predefined bonus
    int bonus = 0;
    switch (lv) {
    case 0:
      bonus = 0;
      break;
    case 1:
      bonus = 1;
      break;
    case 2:
      bonus = 3;
      break;
    case 3:
      bonus = 5;
      break;
    case 4:
      bonus = 8;
      break;
    case 5:
      bonus = 11;
      break;
    case 6:
      bonus = 15;
      break;
    default:
      bonus = 0;
      break;
    }
    return bonus;
  }

}
