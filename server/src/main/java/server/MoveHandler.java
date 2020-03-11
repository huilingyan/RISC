package server;

import java.util.ArrayList;
import java.util.List;

import shared.*;

public class MoveHandler implements Handler {

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
    
    List<MoveOperation> moveList = action.getMoveOperations();
    for (int i = 0; i < moveList.size(); i++) {
      MoveOperation moveOp = moveList.get(i);
      String src = moveOp.getSrc();
      String dest = moveOp.getDest();
      int num = moveOp.getNum();
      //System.out.println("dest:" + dest + " num:" + num);
      for (int j = 0; j < newmap.size(); j++) {
        if (newmap.get(j).getName().contentEquals(src)) {
          newmap.get(j).subtractDefender(num);//src territory - unit
          //System.out.println(newmap.get(j).getName() + " - " + num);
        }
        if (newmap.get(j).getName().contentEquals(dest)) {
          newmap.get(j).addDefender(num);//dest territory + unit
          //System.out.println(newmap.get(j).getName() + " + " + num);
        }
      }
     }


    return newmap;
  }
}
