package shared;

import java.util.ArrayList;
import java.util.Arrays;

public class OldMapGenerator {
  //generate a map for testing
  public static final String[] TERRITORY_NAME_LIST = {"Pikachu", "Ditto", "Gengar", "Eevee", "Snorlax", "Mew", "Psyduck", "Magneton", "Vulpix", "Jumpluff", "Bulbasaur", "Charmandar", "Squirtle", "Pidgey", "Caterpie", "Rattata"};

  public static Map initmapGenerator() {
    PlayerStat p0 = new PlayerStat(0, "p0", 10, 10, 3, "87CEFA");
    PlayerStat p1 = new PlayerStat(1, "p1", 10, 10, 3, "F08080");
    PlayerStat p2 = new PlayerStat(2, "p2", 10, 10, 3, "90EE90");

    ArrayList<PlayerStat> psList = new ArrayList<>();
    psList.add(p0);
    psList.add(p1);
    psList.add(p2);
    
    ArrayList<Integer> tids = new ArrayList<Integer>(Arrays.asList(1, 4, 9, 5, 10, 13, 2, 6, 11));
    ArrayList<Territory> tList = new ArrayList<>();

    for (int i = 0; i < tids.size(); i++) {
      int pid = i / 3;   // each player has three territories
      int tid = tids.get(i);
      String name = TERRITORY_NAME_LIST[tid];
      Territory t = new Territory(pid, tid, name,3,9,6);
      tList.add(t);  // right now territories ordered as in tids
    }

    for (Territory t: tList) {
      for (int j = 0; j < Territory.MAX_NEIGHBOR; j++) {
        int nbID = t.calcNbID(j);
        if (nbID >= 0 && tids.contains(nbID)) {
          t.setNeighbor(j, nbID);
        }
      }
    }

    Map m = new Map(tList, psList);
    return m;
  }

  public static Map gamemapGenerator() {
    PlayerStat p0 = new PlayerStat(0, "p0", 113, 40, 4, "87CEFA");
    PlayerStat p1 = new PlayerStat(1, "p1", 100, 60, 4, "F08080");
    PlayerStat p2 = new PlayerStat(2, "p2", 10, 30, 1, "90EE90");

    ArrayList<PlayerStat> psList = new ArrayList<>();
    psList.add(p0);
    psList.add(p1);
    psList.add(p2);
    
    ArrayList<Integer> tids = new ArrayList<Integer>(Arrays.asList(1, 4, 9, 5, 10, 13, 2, 6, 11));
    ArrayList<Territory> tList = new ArrayList<>();
    Army army = new Army(new ArrayList<Integer>(Arrays.asList(7,6,5,4,3,2,1)));

    for (int i = 0; i < tids.size(); i++) {
      int pid = i / 4;  
      int tid = tids.get(i);
      String name = TERRITORY_NAME_LIST[tid];
      Territory t = new Territory(pid, tid, name,3,9,6);
      t.addDefender(army);
      tList.add(t);  // right now territories ordered as in tids
    }

    for (Territory t: tList) {
      for (int j = 0; j < Territory.MAX_NEIGHBOR; j++) {
        int nbID = t.calcNbID(j);
        if (nbID >= 0 && tids.contains(nbID)) {
          t.setNeighbor(j, nbID);
        }
      }
    }

    Map m = new Map(tList, psList);

    return m;
  }

}
