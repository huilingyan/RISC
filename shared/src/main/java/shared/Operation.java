package shared;

import java.io.Serializable;
import java.lang.String;


public class Operation implements Serializable {
    
    //protected int type; // operation type: 0-deployment, 1-move, 2-attack
    protected String dest; // destination territory
    protected Army army_deploy; //army to deploy, move to or attack

    // public int getType() { // get the operation type
    //     return this.type;
    // }

    public String getDest() { // get the destination
        return this.dest;
    }

    public Army getArmy() { // get army to deploy, move to or attack
        return this.army_deploy;
    }

}


class GameOperation extends Operation { 

    private String src; // source territory

    public GameOperation(String src, String dest, Army army) {
        this.src = src;
        this.dest = dest;
        this.army_deploy = army;
    }


    public String getSrc() { // get the source territory
        return this.src;
    }

}



