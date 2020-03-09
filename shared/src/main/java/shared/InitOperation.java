package shared;

public class InitOperation extends Operation { // deploy units to own territories

    public static final int INIT = 0;

    public InitOperation(String dest, int num) {
        this.type = INIT;
        this.dest = dest;
        this.num = num;
    }

}