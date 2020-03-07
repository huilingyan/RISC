package shared;


public class Operation {
    
    protected int type; // operation type: 0-deployment, 1-move, 2-attack
    protected int dest; // destination territory
    protected int num; // number of unit operated on

    public int getType() { // get the operation type
        return this.type;
    }

    public int getDest() { // get the destination
        return this.dest;
    }

    public int getNum() { // get the number of units being deployed
        return this.num;
    }

}

class InitOperation extends Operation { // deploy units to own territories

    public static final int INIT = 0;

    InitOperation(int dest, int num) {
        this.type = INIT;
        this.dest = dest;
        this.num = num;
    }

}

class GameOperation extends Operation { 

    private int src; // source territory

    GameOperation(int src, int dest, int num) {
        this.src = src;
        this.dest = dest;
        this.num = num;
    }


    public int getSrc() { // get the source territory
        return this.src;
    }

}

class MoveOperation extends GameOperation { // move operation

    public static final int MOVE = 1;

    MoveOperation(int src, int dest, int num) {
        super(src, dest, num);
        this.type = MOVE;
    }

}

class AttackOperation extends GameOperation { // attack operation

    public static final int ATTACK = 2;

    AttackOperation(int src, int dest, int num) {
        super(src, dest, num);
        this.type = ATTACK;
    }
    
}