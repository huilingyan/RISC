package server;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import shared.Territory;

public class InitializeMapTest {
  @Test
  public void testInitializeMap() {
    Gameserver server = new Gameserver();
    server.initializeMap(2);
    Displayer displayer = Displayer.getInstance();
    ArrayList<Territory> map = server.getMap();
    // print out tids and names
    for (Territory t : map) {
      System.out.println("tid: " + t.getTid() + ", name: " + t.getName());
    }
    displayer.displayMap(map);
  }

}
