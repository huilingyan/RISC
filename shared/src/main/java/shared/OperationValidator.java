package shared;

import java.util.ArrayList;


public class OperationValidator {

    public static final int VALID = 1;
    public static final int INVALID_DEST = -1;
    public static final int NO_ENOUGH_UNITS = -2;

    private Action validatedaction;
    private ArrayList<Territory> temp_map;
    private int player_id;

    public OperationValidator(int pid, ArrayList<Territory> curr_map) {
        this.validatedaction = new Action();
        this.temp_map = curr_map;
        this.player_id = pid;
    }

    public Action getAction() {
        return this.validatedaction;
    }

    public void setAction(Action action) {
        this.validatedaction = action;
    }

    public int isValidInitOperation(InitOperation initop, int totalunit) {

    // 1. Check the name of destination territory
        int isvaliddest = 0;
        Territory t_to_deploy = null; // the territory to operate on
        for (Territory t : this.temp_map) {
            if ((t.getName() == initop.dest) && (t.getOwnership() == this.player_id)) { // if is valid dest
                isvaliddest = 1;
                t_to_deploy = t; 
                break;
            }
        }
        
        if (isvaliddest == 0) {
            return INVALID_DEST;
        }

    // 2. Check if valid number
        int remains = getRemainingUnit(totalunit);
        if (initop.num > remains) { // if no enough units
            return NO_ENOUGH_UNITS;
        }

        // add units to the territory
        // TODO: check if this works
        if (t_to_deploy != null) {
            t_to_deploy.addDefender(initop.num);
        }
        
        return VALID;
    }

    public int checkOperationType(GameOperation op) {
        return 0;
    }

    // helper method: get the remaining number of unit for player
    private int getRemainingUnit(int totalunit) {
        int remains = totalunit;
        for (Territory t : this.temp_map) {
            if (t.getOwnership() == this.player_id) { // if is the player's territory
                remains -= t.getDefenderNum();
            }
        }
        return remains;
    }

}