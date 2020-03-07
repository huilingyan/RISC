package shared;

public class AttackOperation extends GameOperation { // attack operation

    public static final int ATTACK = 2;

    AttackOperation(int src, int dest, int num) {
        super(src, dest, num);
        this.type = ATTACK;
    }
    
}