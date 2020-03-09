package shared;

public class MoveOperation extends GameOperation { // move operation

    public static final int MOVE = 1;

    MoveOperation(String src, String dest, int num) {
        super(src, dest, num);
        this.type = MOVE;
    }

}