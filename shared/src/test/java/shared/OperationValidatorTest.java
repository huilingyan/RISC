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
    t1.setNeighbor(1, t2);
    t1.setNeighbor(3, t5);

    t2.setNeighbor(2, t3);
    t2.setNeighbor(3, t4);
    t2.setNeighbor(4, t1);

    t3.setNeighbor(1, t6);
    t3.setNeighbor(3, t7);
    t3.setNeighbor(4, t4);
    t3.setNeighbor(5, t2);

    t4.setNeighbor(0, t2);
    t4.setNeighbor(1, t3);
    t4.setNeighbor(2, t7);
    t4.setNeighbor(4, t5);
    t4.setNeighbor(5, t1);

    t5.setNeighbor(0, t1);
    t5.setNeighbor(1, t4);
    t5.setNeighbor(3, t9);

    t6.setNeighbor(4, t3);

    t7.setNeighbor(0, t3);
    t7.setNeighbor(2, t8);
    t7.setNeighbor(5, t4);

    t8.setNeighbor(5, t7);

    t9.setNeighbor(0, t5);

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
    

    ArrayList<InitOperation> initlist1 = new ArrayList<InitOperation>();
    ArrayList<InitOperation> initlist2 = new ArrayList<InitOperation>();
    ArrayList<InitOperation> initlist3 = new ArrayList<InitOperation>();

    initlist1.add(iop1);
    initlist1.add(iop2);
    initlist1.add(iop3);

    initlist2.add(iop4);
    initlist2.add(iop5);
    initlist2.add(iop6);

    initlist3.add(iop7);
    initlist3.add(iop8);
    initlist3.add(iop9);
    initlist3.add(iop10);


    for (InitOperation iop : initlist1) {
      if (v0.isValidInitOperation(iop, totalunit) == v0.VALID) {
        dp.deployUnits(iop);  
        dp.showCurrentMap(v0.getCurrentMapState());
      }
      // else if (v0.isValidInitOperation(iop, totalunit) == v0.INVALID_DEST) {
      //   dp.showErrorMsg(v0.INVALID_DEST);
      // }
      // else if (v0.isValidInitOperation(iop, totalunit) == v0.NO_ENOUGH_UNITS) {
      //   dp.showErrorMsg(v0.NO_ENOUGH_UNITS);
      // }
    }

    for (InitOperation iop : initlist2) {
      if (v1.isValidInitOperation(iop, totalunit) == v1.VALID) {
        dp.deployUnits(iop);
      }
      else if (v1.isValidInitOperation(iop, totalunit) == v1.INVALID_DEST) {
        dp.showErrorMsg(v1.INVALID_DEST);
      }
      // else if (v1.isValidInitOperation(iop, totalunit) == v1.NO_ENOUGH_UNITS) {
      //   dp.showErrorMsg(v1.NO_ENOUGH_UNITS);
      // }
    }

    for (InitOperation iop : initlist3) {
      if (v2.isValidInitOperation(iop, totalunit) == v2.VALID) {
        dp.deployUnits(iop);
      }
      else if (v2.isValidInitOperation(iop, totalunit) == v2.ILLEGAL_NUM) {
        dp.showErrorMsg(v2.ILLEGAL_NUM);
      }
      else if (v2.isValidInitOperation(iop, totalunit) == v2.NO_ENOUGH_UNITS) {
        dp.showErrorMsg(v2.NO_ENOUGH_UNITS);
      }
    }

    // move operations
    MoveOperation mop1 = new MoveOperation("One", "Three", 2); // valid move
    MoveOperation mop2 = new MoveOperation("One", "Eight", 2); // invalid dest
    MoveOperation mop3 = new MoveOperation("One", "Two", 10); // no enough units
    MoveOperation mop4 = new MoveOperation("Six", "Two", 1); // invalid src
    MoveOperation mop5 = new MoveOperation("One", "Three", -2); // invalid number
    

    ArrayList<MoveOperation> movelist = new ArrayList<MoveOperation>();
    movelist.add(mop1);
    movelist.add(mop2);
    movelist.add(mop3);
    movelist.add(mop4);
    movelist.add(mop5);

    for (MoveOperation mop : movelist) {
      if (v0.isValidMoveOperation(mop) == v0.VALID) {
        dp.moveUnits(mop);
      }
      else if (v0.isValidMoveOperation(mop) == v0.INVALID_DEST) {
        dp.showErrorMsg(v0.INVALID_DEST);
      }
      else if (v0.isValidMoveOperation(mop) == v0.NO_ENOUGH_UNITS) {
        dp.showErrorMsg(v0.NO_ENOUGH_UNITS);
      }
      else if (v0.isValidMoveOperation(mop) == v0.INVALID_SRC) {
        dp.showErrorMsg(v0.INVALID_SRC);
      }
      else if (v0.isValidMoveOperation(mop) == v0.ILLEGAL_NUM) {
        dp.showErrorMsg(v0.ILLEGAL_NUM);
      }
    }

    MoveOperation mop6 = new MoveOperation("Five", "Six", 1); // invalid src

    // if (v1.isValidMoveOperation(mop6) == v0.VALID) {
    //   dp.moveUnits(mop5);
    // }
    // else if (v1.isValidMoveOperation(mop6) == v1.INVALID_DEST) {
    //   dp.showErrorMsg(v1.INVALID_DEST);
    // }
    // else if (v1.isValidMoveOperation(mop6) == v1.NO_ENOUGH_UNITS) {
    //   dp.showErrorMsg(v1.NO_ENOUGH_UNITS);
    // }
    // else if (v1.isValidMoveOperation(mop6) == v1.INVALID_SRC) {
    //   dp.showErrorMsg(v1.INVALID_SRC);
    // }
    if (v1.isValidMoveOperation(mop6) == v1.INVALID_PATH) {
      dp.showErrorMsg(v1.INVALID_PATH);
    }

    // attack operations
    AttackOperation aop1 = new AttackOperation("One", "Five", 1); // valid move
    AttackOperation aop2 = new AttackOperation("One", "Two", 2); // invalid dest
    AttackOperation aop3 = new AttackOperation("One", "Seven", 2); // not adjacent
    AttackOperation aop4 = new AttackOperation("One", "Five", -1); // invalid number
    AttackOperation aop5 = new AttackOperation("Nine", "Five", 1); // invalid src
    AttackOperation aop6 = new AttackOperation("One", "Five", 20); // no enough units

    ArrayList<AttackOperation> attacklist = new ArrayList<AttackOperation>();
    attacklist.add(aop1);
    attacklist.add(aop2);
    attacklist.add(aop3);
    attacklist.add(aop4);
    attacklist.add(aop5);
    attacklist.add(aop6);

    for (AttackOperation aop : attacklist) {
      if (v0.isValidAttackOperation(aop) == v0.VALID) {
        dp.attackUnits(aop);
      }
      else if (v0.isValidAttackOperation(aop) == v0.INVALID_DEST) {
        dp.showErrorMsg(v0.INVALID_DEST);
      }
      else if (v0.isValidAttackOperation(aop) == v0.NOT_ADJACENT) {
        dp.showErrorMsg(v0.NOT_ADJACENT);
      }
      else if (v0.isValidAttackOperation(aop) == v0.ILLEGAL_NUM) {
        dp.showErrorMsg(v0.ILLEGAL_NUM);
      }
      else if (v0.isValidAttackOperation(aop) == v0.INVALID_SRC) {
        dp.showErrorMsg(v0.INVALID_SRC);
      }
      else if (v0.isValidAttackOperation(aop) == v0.NO_ENOUGH_UNITS) {
        dp.showErrorMsg(v0.NO_ENOUGH_UNITS);
      }
      
      
    }

  }

}
