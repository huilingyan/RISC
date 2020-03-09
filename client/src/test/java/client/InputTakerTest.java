package client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class InputTakerTest {
  @Test
  public void test_readInt() {
    Scanner sc = new Scanner("asdf\n-1\n1 23\n50\n");
    InputTaker it=new InputTaker();
    
    assertEquals(-1, it.readanInt(sc));
    assertEquals(50, it.readanInt(sc));
  }


  @Test
  public void test_readnofP() {
    Scanner sc = new Scanner("asdf\n-1\n1 23\n50\n2\n5\n");
    InputTaker it=new InputTaker();
    
    assertEquals(2, it.readnofPlayers(sc));
    assertEquals(5, it.readnofPlayers(sc));
  }
}
