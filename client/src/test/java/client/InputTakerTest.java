package client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import shared.AttackOperation;
import shared.InitOperation;
import shared.MoveOperation;

public class InputTakerTest {
  @Test
  public void test_readInt() {
    Scanner sc = new Scanner("asdf\n-1\n1 23\n\n50\n");
    InputTaker it=new InputTaker();
    
    assertEquals(-1, it.readanInt(sc));
    assertEquals(50, it.readanInt(sc));

    Scanner sc2 = new Scanner("-1\n0\n1\n");
    assertEquals(0, it.readaPosInt(sc2));
    assertEquals(1, it.readaPosInt(sc2));
  }


  @Test
  public void test_readnofP() {
    Scanner sc = new Scanner("asdf\n-1\n1 23\n50\n2\n5\n");
    InputTaker it=new InputTaker();
    
    assertEquals(2, it.readnofPlayers(sc));
    assertEquals(5, it.readnofPlayers(sc));
  }

  @Test
  public void test_readOP() {
    Scanner sc = new Scanner("jiangsu\n10\nshanghai\n\nwasd\nBeijing\n-1\n99\njiangsu\n9 90\n3\n");
    InputTaker it=new InputTaker();
    HashSet<String> hs = new HashSet<String>();
    hs.add("Jiangsu");
    hs.add("Shanghai");
    hs.add("Beijing");
    it.setTNameSet(hs);
    MoveOperation mop = it.readMoveOperation(sc);
    AttackOperation aop = it.readAttackOperation(sc);
    assertEquals("Jiangsu", mop.getSrc());
    assertEquals(10, mop.getNum());
    assertEquals("Shanghai", mop.getDest());
    assertEquals("Beijing", aop.getSrc());
    assertEquals(99, aop.getNum());
    assertEquals("Jiangsu", aop.getDest());
    InitOperation iop= it.readInitOperation("anhui", sc);
    assertEquals(3, iop.getNum());
  }

  @Test
  public void test_readselection() {
    Scanner sc = new Scanner("aser\n90\nd\n");
    InputTaker it = new InputTaker();
    assertEquals("D", it.readselectionStr(sc));
  }

  @Test
  public void test_readyesorno() {
    Scanner sc = new Scanner("yes\ny n\ny\nN\n");
    InputTaker it = new InputTaker();
    assertEquals(true, it.readYorN(sc));
    assertEquals(false, it.readYorN(sc));
  }
}

