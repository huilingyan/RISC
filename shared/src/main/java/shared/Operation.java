package shared;


public class Operation {
    
    //protected int optype; // operation type: 0-deployment, 1-move, 2-attack
    protected int dest; // destination territory
    protected int num; // number of unit operated on

    public int getDest() { // get the destination
        return this.dest;
    }

    public int getNum() { // get the number of units being deployed
        return this.num;
    }

}

class InitOperation extends Operation { // deploy units to own territories

    InitOperation(int dest, int num) {
        this.dest = dest;
        this.num = num;
    }

}

class GameOperation extends Operation { // make move or attack

    private int optype; // operation type: 1-move, 2-attack
    private int src; // source territory

    GameOperation(int optype, int src, int dest, int num) {
        this.optype = optype;
        this.src = src;
        this.dest = dest;
        this.num = num;
    }

    public int getType() { // get the operation type
        return this.optype;
    }

    public int getSrc() { // get the source territory
        return this.src;
    }

}