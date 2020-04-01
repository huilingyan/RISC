package server;

import java.util.ArrayList;

import shared.*;

public abstract class Handler {
  abstract Map handleAction(
      Map WorldMap, Action action);
  //takes a map and a list of operations, apply operations to map and return updated map
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
  /*
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
  public ArrayList<Territory> copyMap(ArrayList<Territory> oldmap) {
    ArrayList<Territory> newmap = new ArrayList<Territory>();
    for (int m = 0; m < oldmap.size(); m++) {
      Territory t = new Territory(oldmap.get(m));
      //deep copy, do not affect original map
      newmap.add(t);
    }
    return newmap;
  }

  public ArrayList<PlayerStat> copyPlayerStats(ArrayList<PlayerStat> oldstats) {
    ArrayList<PlayerStat> newstats = new ArrayList<PlayerStat>();
    for (int p = 0; p < oldstats.size(); p++) {
      PlayerStat t = new PlayerStat(oldstats.get(p));
      //deep copy, do not affect original stat
      newstats.add(t);
    }
    return newstats;
  }
}
