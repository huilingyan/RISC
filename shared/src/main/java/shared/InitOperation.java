package shared;

public class InitOperation extends Operation { // deploy units to own territories

    public InitOperation(String dest, Army army) {
        // this.type = INIT;
        this.dest = dest;
        this.army_deploy = army;
    }

}
