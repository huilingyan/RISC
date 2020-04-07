package shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class MapTest {
  @Test
  public void test_shortestPath() {
    Map worldmap = MapGenerator.gamemapGenerator();
    assertEquals(-1, worldmap.CostofShortestPath("Ditto", "Charmandar"));
    assertEquals(-1, worldmap.CostofShortestPath("Ditto", "Pidgey"));
    assertEquals(0, worldmap.CostofShortestPath("Ditto", "Ditto"));
    assertEquals(3, worldmap.CostofShortestPath("Ditto", "Mew"));
    assertEquals(6, worldmap.CostofShortestPath("Ditto", "Jumpluff"));
    assertEquals(9, worldmap.CostofShortestPath("Gengar", "Pidgey"));
    assertEquals(9, worldmap.CostofShortestPath("Pidgey", "Gengar"));
  }
  @Test
  public void test_Map() {
    Map m1 = new Map();
    Map m2 = new Map(new ArrayList<Territory>());
    Map m3 = new Map(new ArrayList<Territory>(), new ArrayList<PlayerStat>());
    Map m4 = new Map(m1);
    m4.setTerritories(new ArrayList<Territory>());
    m4.setPlayerStats(new ArrayList<PlayerStat>());
    m4.addPlayerStat(new PlayerStat(0, "p0", 10, 10, 3, "87CEFA"));
    m4.addTerritory(new Territory(0, 0, "test", 3, 9, 6));
    
    
    //assert();
  }
}
