package server;

import java.util.ArrayList;
import java.util.List;

import shared.*;

public class InitHandler extends Handler {

  @Override
  public Map handleAction(
         Map worldmap, Action action){
    //deep copy
    Map new_worldmap = new Map(worldmap);
    
    List<InitOperation> initList = action.getInitOperations();
    for (int i = 0; i < initList.size(); i++) {
      InitOperation initOp = initList.get(i);
      String dest = initOp.getDest();
      Army army_depoly = initOp.getArmy();
      //System.out.println("dest:" + dest + " num:" + num);
      Territory t_dest = new_worldmap.getTerritoryByName(dest);
      if (t_dest != null) {
        t_dest.addDefender(army_depoly);
      }
     }


    return new_worldmap;
  }
}
