package server;

import java.util.ArrayList;
import java.util.List;

import shared.*;

public class InitHandler implements Handler {

  @Override
  public ArrayList<Territory> handleAction(
         ArrayList<Territory> map, Action action){
    //ArrayList<Territory> newmap = map;//this copy will affect original map
    ArrayList<Territory> newmap = new ArrayList<Territory>();
    for (int m = 0; m < map.size(); m++) {
      Territory t = new Territory(map.get(m));
      //deep copy, do not affect original map
      newmap.add(t);
    }
    
    List<InitOperation> initList = action.getInitOperations();
    for (int i = 0; i < initList.size(); i++) {
      InitOperation initOp = initList.get(i);
      String dest = initOp.getDest();
      int num = initOp.getNum();
      //System.out.println("dest:" + dest + " num:" + num);
      for (int j = 0; j < newmap.size(); j++) {
        //System.out.println("name:" + newmap.get(i).getName());
        if (newmap.get(j).getName().contentEquals(dest)) {
          newmap.get(j).addDefender(num);
          //can change to setDefender(num), instead of adding
        }
        else{
          continue;
        }
      }
     }


    return newmap;
  }
}
