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
  public int getTotalSoldiers(){
  	int total_soldier_num = 0;
  	for (int i = 0; i < Soldiers.size(); i++) {
      total_soldier_num += Soldiers.get(i);
    }
    return total_soldier_num;
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
  
  public int getHighestLevel(){
  	for (int i = Soldiers.size() - 1; i >= 0; i--) {
      if (Soldiers.get(i) > 0) {
        return i;
      }
    }
    //empty army
    return 0;
  }
  
  public int getLowestLevel(){
  	for (int i = 0; i < Soldiers.size(); i++) {
      if (Soldiers.get(i) > 0) {
        return i;
      }
    }
    //empty army
    return 0;
  }  
  public int getHighestBonus() {
    return getBonus(getHighestLevel());
  }
  
  public int getLowestBonus(){
    return getBonus(getLowestLevel());
  }

  public int getBonus(int lv) {
    //match each level with a predefined bonusList
    ArrayList<Integer> bonusList = new ArrayList<Integer>(Arrays.asList(0,1,3,5,8,11,15));    
    return bonusList.get(lv);
  }
  /*
  public void subtractSoldiersFromHighestLv(int num){
  	int total_soldiers = getTotalSoldiers();
  	if(total_soldiers <= num){
      for (int i = Soldiers.size() - 1; i >= 0; i--) {
        //remove all soldiers
      	subtractSoldiers(i, getSoldierNumber(i));
      }
    } else {
  	  for (int i = Soldiers.size() - 1; i >= 0; i--) {
  	  	if (num > getSoldierNumber(i)){
      	  subtractSoldiers(i, getSoldierNumber(i));
      	  num -= getSoldierNumber(i);
      	} else {
      	  subtractSoldiers(i, num);
      	  break;
      	}
      }
  	}
  }
  */
  public int calculateUpgradeCost(Army army_upgrade) {
     
     ArrayList<Integer> upgrade_cost_list =
         new ArrayList<Integer>(Arrays.asList(0, 3, 11, 30, 55, 90, 140));
     Army army_to_upgrade = new Army(Soldiers);
     Army army_upgraded = new Army(army_upgrade);

     int value_upgraded = 0;
     int value_before_upgrade = 0;

     for (int i = 0; i < 7; i++) {
       value_upgraded += army_upgraded.getSoldierNumber(i) * upgrade_cost_list.get(i);
       value_before_upgrade  += army_to_upgrade.getSoldierNumber(i) * upgrade_cost_list.get(i);
      
     }
    //e.g. cost for lv1-->lv3 = (30-0) - (3-0)
    return value_upgraded - value_before_upgrade; 
     
   }
}
