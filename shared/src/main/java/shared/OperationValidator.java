package shared;

import java.util.ArrayList;
import java.lang.String;
import java.util.LinkedList;


public class OperationValidator {

    public static final int VALID = 1;
    public static final int INVALID_DEST = -1;
    public static final int NO_ENOUGH_UNITS = -2;
    public static final int ILLEGAL_NUM = -3;
    public static final int INVALID_SRC = -4;
    public static final int INVALID_PATH = -5;
    public static final int NOT_ADJACENT = -6;

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
            if ((t.getName().toLowerCase().equals(initop.getDest().toLowerCase())) && (t.getOwnership() == this.player_id)) { // if is valid dest
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

        if (initop.num <= 0) { // if illegal number
            return ILLEGAL_NUM;
        }

        // update temp_map: add units to the territory
        if (t_to_deploy != null) {
            t_to_deploy.addDefender(initop.num);
        }
        // add operation to action
        validatedaction.addInitOperation(initop);
   
        return VALID;
    }

    public int isValidMoveOperation(MoveOperation moveop) {
        String src = moveop.getSrc();
        String dest = moveop.getDest();
        Territory t_to_remove = null; // the territory to remove units
        Territory t_to_move = null; // the territory to move units to
    // 1. check if valid src
        int isvalidsrc = 0;
        for (Territory t : this.temp_map) {
            if ((t.getName().toLowerCase().equals(src.toLowerCase())) && (t.getOwnership() == this.player_id)) { // if is valid src
                isvalidsrc = 1;
                t_to_remove = t; 
                break;
            }
        }       
        if (isvalidsrc == 0) {
            return INVALID_SRC;
        }
    // 2. check if valid number
        if (t_to_remove != null) {
            if (moveop.num <= 0) { 
                return ILLEGAL_NUM;
            }
            if (moveop.num > t_to_remove.getDefenderNum()) { // if no enough units
                return NO_ENOUGH_UNITS;
            }
        }
    // 3. check if valid dest
       // 3.1 check if own territory
        int isvaliddest = 0;
        for (Territory t : this.temp_map) {
            if ((t.getName().toLowerCase().equals(dest.toLowerCase())) && (t.getOwnership() == this.player_id)) { // if is valid dest
                isvaliddest = 1;
                t_to_move = t; 
                break;
            }
        }      
        if (isvaliddest == 0) {
            return INVALID_DEST;
        }
       // 3.2 check if there's a path
        if (isValidPath(t_to_remove, t_to_move) == false) {
            return INVALID_PATH;
        }

        // update temp_map: add and sub units
        if (t_to_remove != null) {
            t_to_remove.subtractDefender(moveop.num);
        }
        if (t_to_move != null) {
            t_to_move.addDefender(moveop.num);
        }

        // if valid, add to move operation
        validatedaction.addMoveOperation(moveop);
        return VALID;

    }

    public int isValidAttackOperation(AttackOperation attackop) {
        String src = attackop.getSrc();
        String dest = attackop.getDest();
        Territory t_to_remove = null; // the territory to remove units
        Territory t_to_move = null; // the territory to move units to
    // 1. check if valid src
        int isvalidsrc = 0;       
        for (Territory t : this.temp_map) {
            if ((t.getName().toLowerCase().equals(src.toLowerCase())) && (t.getOwnership() == this.player_id)) { // if is valid src
                isvalidsrc = 1;
                t_to_remove = t; 
                break;
            }
        }       
        if (isvalidsrc == 0) {
            return INVALID_SRC;
        }
    // 2. check if valid number
        if (t_to_remove != null) {
            if (attackop.num <= 0) { 
                return ILLEGAL_NUM;
            }
            if (attackop.num > t_to_remove.getDefenderNum()) { // if no enough units
                return NO_ENOUGH_UNITS;
            }
        }
    // 3. check if valid dest
       // 3.1 check if is other's territory
        int isvaliddest = 0;
        for (Territory t : this.temp_map) {
            if ((t.getName().toLowerCase().equals(dest.toLowerCase())) && (t.getOwnership() != this.player_id)) { // if is valid dest
                isvaliddest = 1;
                t_to_move = t; 
                break;
            }
        }      
        if (isvaliddest == 0) {
            return INVALID_DEST;
        }
       // 3.2 check if is adjacent
        if (isAdjacent(t_to_remove, t_to_move) == false) {
            return NOT_ADJACENT;
        }

        if (t_to_remove != null) {
            t_to_remove.subtractDefender(attackop.num);
        }

        // if valid, add to move operation
        validatedaction.addAttackOperation(attackop);
        return VALID;

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

    private boolean isValidPath(Territory src, Territory dest) {

        LinkedList<Territory> visited = new LinkedList<Territory>();
        LinkedList<Territory> queue = new LinkedList<Territory>();

        queue.add(src);

        while(queue.size() != 0) { // when queue is not empty
            Territory t = queue.poll();

            if (t.getTid() == dest.getTid()) {
                return true; // find the path
            }

            if (visited.contains(t)) {
                continue;
            }

            for (Territory neigh : t.getNeighborList()) {
                if ((neigh != null) && (neigh.getOwnership() == this.player_id)) {
                    queue.add(neigh);
                }                
            }
            visited.add(t);
        }

        return false;
    }

    private boolean isAdjacent(Territory src, Territory dest) {
        for (Territory neigh : src.getNeighborList()) {
            if (neigh != null) {
                if (neigh.getTid() == dest.getTid()) { // if dest in neighbout list
                    return true;
                }
            }
        }
        return false;
    }

}