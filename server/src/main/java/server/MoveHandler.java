package server;

import java.util.ArrayList;
import java.util.List;

import shared.*;

public class MoveHandler extends Handler {

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
      Territory t_src = findTerritorybyString(newmap, src);
      Territory t_dest = findTerritorybyString(newmap, dest);
      if (t_src != null && t_dest != null) {
        t_src.subtractDefender(num);//src territory - unit
        t_dest.addDefender(num);//dest territory + unit
      }
    }
    return newmap;
  }
}
