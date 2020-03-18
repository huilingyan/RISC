package shared;

import static org.junit.jupiter.api.Assertions.*;

// import org.graalvm.compiler.lir.StandardOp.MoveOp;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class OperationValidatorTest {
  @Test
  public void testOperationValidator() {

    Displayer dp = Displayer.getInstance();

    // create territories
    Territory t1 = new Territory(0, 1, "One");
    Territory t2 = new Territory(0, 2, "Two");
    Territory t3 = new Territory(0, 3, "Three");

    Territory t4 = new Territory(1, 4, "Four");
    Territory t5 = new Territory(1, 5, "Five");
    Territory t6 = new Territory(1, 6, "Six");

    Territory t7 = new Territory(2, 7, "Seven");
    Territory t8 = new Territory(2, 8, "Eight");
    Territory t9 = new Territory(2, 9, "Nine");

    // set neighbours for each of them
    t1.setNeighbor(1, 2);
    t1.setNeighbor(3, 5);

    t2.setNeighbor(2, 3);
    t2.setNeighbor(3, 4);
    t2.setNeighbor(4, 1);

    t3.setNeighbor(1, 6);
    t3.setNeighbor(3, 7);
    t3.setNeighbor(4, 4);
    t3.setNeighbor(5, 2);

    t4.setNeighbor(0, 2);
    t4.setNeighbor(1, 3);
    t4.setNeighbor(2, 7);
    t4.setNeighbor(4, 5);
    t4.setNeighbor(5, 1);

    t5.setNeighbor(0, 1);
    t5.setNeighbor(1, 4);
    t5.setNeighbor(3, 9);

    t6.setNeighbor(4, 3);

    t7.setNeighbor(0, 3);
    t7.setNeighbor(2, 8);
    t7.setNeighbor(5, 4);

    t8.setNeighbor(5, 7);

    t9.setNeighbor(0, 5);

    ArrayList<Territory> curr_map = new ArrayList<Territory>();
    curr_map.add(t1);
    curr_map.add(t2);
    curr_map.add(t3);

    curr_map.add(t4);
    curr_map.add(t5);
    curr_map.add(t6);

    curr_map.add(t7);
    curr_map.add(t8);
    curr_map.add(t9);

    OperationValidator v0 = new OperationValidator(0, curr_map);
    OperationValidator v1 = new OperationValidator(1, curr_map);
    OperationValidator v2 = new OperationValidator(2, curr_map);


    // create operations
    // set total unit number
    int totalunit = 20;

    // init operation
    InitOperation iop1 = new InitOperation("One", 6);
    InitOperation iop2 = new InitOperation("Two", 7);
    InitOperation iop3 = new InitOperation("Three", 7); 

    InitOperation iop4 = new InitOperation("fkaoevl", 10); // invalid dest
    InitOperation iop5 = new InitOperation("Five", 5);
    InitOperation iop6 = new InitOperation("Six", 6); 

    InitOperation iop7 = new InitOperation("Seven", 8);
    InitOperation iop8 = new InitOperation("Eight", 6); 
    InitOperation iop9 = new InitOperation("Nine", 7); // no enough units
    InitOperation iop10 = new InitOperation("Nine", -1); // no enough units
    
    assertEquals(OperationValidator.VALID, v0.isValidInitOperation(iop1, totalunit));
    dp.deployUnits(iop1);

    assertEquals(OperationValidator.VALID, v0.isValidInitOperation(iop2, totalunit));
    assertEquals(OperationValidator.VALID, v0.isValidInitOperation(iop3, totalunit));

    assertEquals(OperationValidator.INVALID_DEST, v1.isValidInitOperation(iop4, totalunit));
    assertEquals(OperationValidator.VALID, v1.isValidInitOperation(iop5, totalunit));
    assertEquals(OperationValidator.VALID, v1.isValidInitOperation(iop6, totalunit));

    assertEquals(OperationValidator.VALID, v2.isValidInitOperation(iop7, totalunit));
    assertEquals(OperationValidator.VALID, v2.isValidInitOperation(iop8, totalunit));
    assertEquals(OperationValidator.NO_ENOUGH_UNITS, v2.isValidInitOperation(iop9, totalunit));
    assertEquals(OperationValidator.ILLEGAL_NUM, v2.isValidInitOperation(iop10, totalunit));

    

    // move operations
    MoveOperation mop1 = new MoveOperation("One", "Three", 2); // valid move
    MoveOperation mop2 = new MoveOperation("One", "Eight", 2); // invalid dest
    MoveOperation mop3 = new MoveOperation("One", "Two", 10); // no enough units
    MoveOperation mop4 = new MoveOperation("Six", "Two", 1); // invalid src
    MoveOperation mop5 = new MoveOperation("One", "Three", -2); // invalid number
    MoveOperation mop6 = new MoveOperation("One", "One", 1); // src same as dest
    MoveOperation mop7 = new MoveOperation("Five", "Six", 1); // no path
    MoveOperation mop8 = new MoveOperation("Two", "One", 1); // invalid src

    assertEquals(OperationValidator.VALID, v0.isValidMoveOperation(mop1));
    dp.moveUnits(mop1);

    assertEquals(OperationValidator.INVALID_DEST, v0.isValidMoveOperation(mop2));
    assertEquals(OperationValidator.NO_ENOUGH_UNITS, v0.isValidMoveOperation(mop3));
    assertEquals(OperationValidator.INVALID_SRC, v0.isValidMoveOperation(mop4));
    assertEquals(OperationValidator.ILLEGAL_NUM, v0.isValidMoveOperation(mop5));
    assertEquals(OperationValidator.DEST_SAME_AS_SRC, v0.isValidMoveOperation(mop6));
    assertEquals(OperationValidator.INVALID_PATH, v1.isValidMoveOperation(mop7));
    assertEquals(OperationValidator.VALID, v0.isValidMoveOperation(mop8));



    // attack operations
    AttackOperation aop1 = new AttackOperation("One", "Five", 1); // valid move
    AttackOperation aop2 = new AttackOperation("One", "Two", 2); // invalid dest
    AttackOperation aop3 = new AttackOperation("One", "Seven", 2); // not adjacent
    AttackOperation aop4 = new AttackOperation("One", "Five", -1); // invalid number
    AttackOperation aop5 = new AttackOperation("Nine", "Five", 1); // invalid src
    AttackOperation aop6 = new AttackOperation("One", "Five", 20); // no enough units


    assertEquals(OperationValidator.VALID, v0.isValidAttackOperation(aop1));
    dp.attackUnits(aop1);
    assertEquals(OperationValidator.INVALID_DEST, v0.isValidAttackOperation(aop2));
    assertEquals(OperationValidator.NOT_ADJACENT, v0.isValidAttackOperation(aop3));
    assertEquals(OperationValidator.ILLEGAL_NUM, v0.isValidAttackOperation(aop4));
    assertEquals(OperationValidator.INVALID_SRC, v0.isValidAttackOperation(aop5));
    assertEquals(OperationValidator.NO_ENOUGH_UNITS, v0.isValidAttackOperation(aop6));


  }

}
