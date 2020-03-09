package shared;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Action implements Serializable {
    private List<InitOperation> initOperations;
    private List<MoveOperation> moveOperations;
    private List<AttackOperation> attackOperations;

    public Action() {
        this.initOperations= new ArrayList<InitOperation>();
        this.moveOperations= new ArrayList<MoveOperation>();
        this.attackOperations= new ArrayList<AttackOperation>();
    }

    public void addInitOperation(InitOperation iop) {
        this.initOperations.add(iop);
    }

    public void addMoveOperation(MoveOperation mop) {
        this.moveOperations.add(mop);
    }

    public void addAttackOperation(AttackOperation aop) {
        this.attackOperations.add(aop);
    }

    public void concatInitOperation(ArrayList<InitOperation> clientiop) {
        this.initOperations.addAll(clientiop);
    }


    public void concatGameOperation(Action clientaction) {
        this.moveOperations.addAll(clientaction.moveOperations);
        this.attackOperations.addAll(clientaction.attackOperations);
    }

}