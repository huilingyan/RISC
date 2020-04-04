package server;

import java.util.ArrayList;

import shared.*;

public abstract class Handler {
  abstract Map handleAction(
      Map worldmap, Action action);
  //takes a map and a list of operations, apply operations to map and return updated map
  
  
  //ev2: these methods now implemented in shared.Map
  /*
	public Territory findTerritorybyString(ArrayList<Territory> map, String tname) {
    for (int j = 0; j < map.size(); j++) {
      //System.out.println("name:" + newmap.get(i).getName());
      if (map.get(j).getName().equalsIgnoreCase(tname)) {
        //find territory by specificed territory name, case insensitive
        return map.get(j);       
      }
    }
    return null;
  }
  
  public Territory findTerritorybyTid(ArrayList<Territory> map, int tid) {
    for (int j = 0; j < map.size(); j++) {
      //System.out.println("name:" + newmap.get(i).getName());
      if (map.get(j).getTid() == tid) {
        return map.get(j);
      }
    }
    return null;
  }
  */

}
