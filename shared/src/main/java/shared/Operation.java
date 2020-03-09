package shared;

import java.io.Serializable;
import java.lang.String;

/* example:
    Operation op1 = InitOperation(Blue, 8); // deploy 8 units to territory Blue
    Operation op2 = MoveOperation(Blue, Green, 5); // move 5 units from Blue to Green
    Operation op3 = AttackOperation(Blue, Red, 6); // deploy 5 units from Blue to attack Red

*/

public class Operation implements Serializable {
    
    protected int type; // operation type: 0-deployment, 1-move, 2-attack
    protected String dest; // destination territory
    protected int num; // number of unit operated on

    public int getType() { // get the operation type
        return this.type;
    }

    public String getDest() { // get the destination
        return this.dest;
    }

    public int getNum() { // get the number of units being deployed
        return this.num;
    }

}


class GameOperation extends Operation { 

    private String src; // source territory

    GameOperation(String src, String dest, int num) {
        this.src = src;
        this.dest = dest;
        this.num = num;
    }


    public String getSrc() { // get the source territory
        return this.src;
    }

}



