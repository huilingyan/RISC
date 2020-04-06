package shared;

import static org.junit.jupiter.api.Assertions.*;

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

}
