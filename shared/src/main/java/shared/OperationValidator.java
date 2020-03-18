package shared;

import java.util.ArrayList;
import java.lang.String;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class OperationValidator {

    public static final int VALID = 1;
    public static final int INVALID_DEST = -1;
    public static final int NO_ENOUGH_UNITS = -2;
    public static final int ILLEGAL_NUM = -3;
    public static final int INVALID_SRC = -4;
    public static final int INVALID_PATH = -5;
    public static final int NOT_ADJACENT = -6;
    public static final int DEST_SAME_AS_SRC = -7;

    private Action validatedaction;
    private ArrayList<Territory> temp_map;
    private int player_id;

    public OperationValidator(int pid, ArrayList<Territory> curr_map) {
        this.validatedaction = new Action();
        this.temp_map = new ArrayList<Territory>();
        for (Territory t : curr_map) {
          temp_map.add(new Territory(t));
        }
        this.player_id = pid;
    }

    public Action getAction() {
        return this.validatedaction;
    }

    public ArrayList<Territory> getCurrentMapState() {
        return temp_map;
    }


    public int isValidInitOperation(InitOperation initop, int totalunit) {

        String dest = initop.getDest();

    // 1. Check the name of destination territory
        Territory t_to_deploy = findTerritory(dest); // the territory to operate on
        if ((t_to_deploy == null) || (isOwnTerritory(t_to_deploy) == false)) {
            return INVALID_DEST;
        }

    // 2. Check if valid number
        int remains = getRemainingUnit(totalunit);
        if (initop.num > remains) { // if no enough units
            return NO_ENOUGH_UNITS;
        }
        if (initop.num < 0) { // if illegal number
            return ILLEGAL_NUM;
        }

        // update temp_map: add units to the territory
        t_to_deploy.addDefender(initop.num);

        // add operation to action
        validatedaction.addInitOperation(initop);
   
        return VALID;
    }

    public int isValidMoveOperation(MoveOperation moveop) {
        String src = moveop.getSrc();
        String dest = moveop.getDest();

    // 1. check if valid src
        Territory t_to_remove = findTerritory(src); // the territory to remove units
        if ((t_to_remove == null) || (isOwnTerritory(t_to_remove) == false)) {
            return INVALID_SRC;
        }

    // 2. check if valid number
        if (moveop.num < 0) { 
            return ILLEGAL_NUM;
        }
        if (moveop.num > t_to_remove.getDefenderNum()) { // if no enough units
            return NO_ENOUGH_UNITS;
        }

    // 3. check if valid dest
       // 3.1 check if own territory
        Territory t_to_move = findTerritory(dest); // the territory to move units to
        if ((t_to_move == null) || (isOwnTerritory(t_to_move) == false)) {
           return INVALID_DEST;
        }

       // 3.2 check if is different from src
        if (dest.equalsIgnoreCase(src)) { // if the dest is same as src
            return DEST_SAME_AS_SRC;
        }

       // 3.3 check if there's a path
        if (isValidPath(t_to_remove, t_to_move) == false) {
            return INVALID_PATH;
        }

        // update temp_map: add and sub units
        t_to_remove.subtractDefender(moveop.num);       
        t_to_move.addDefender(moveop.num);

        // if valid, add to move operation
        validatedaction.addMoveOperation(moveop);
        return VALID;

    }

    public int isValidAttackOperation(AttackOperation attackop) {
        String src = attackop.getSrc();
        String dest = attackop.getDest();

    // 1. check if valid src
        Territory t_to_remove = findTerritory(src); // the territory to remove units
        if ((t_to_remove == null) || (isOwnTerritory(t_to_remove) == false)) {
            return INVALID_SRC;
        }

    // 2. check if valid number
        if (attackop.num < 0) { 
            return ILLEGAL_NUM;
        }
        if (attackop.num > t_to_remove.getDefenderNum()) { // if no enough units
            return NO_ENOUGH_UNITS;
        }

    // 3. check if valid dest
       // 3.1 check if is other's territory
        Territory t_to_move = findTerritory(dest); // the territory to attack
        if ((t_to_move == null) || (isOwnTerritory(t_to_move) == true)) {
          return INVALID_DEST;
        }

       // 3.2 check if is adjacent
        if (isAdjacent(t_to_remove, t_to_move) == false) {
            return NOT_ADJACENT;
        }

        // update temp_map: add and sub units
        t_to_remove.subtractDefender(attackop.num);

        // if valid, add to move operation
        validatedaction.addAttackOperation(attackop);
        return VALID;

    }

    // helper method: get the remaining number of unit for player
    public int getRemainingUnit(int totalunit) {
        int remains = totalunit;
        for (Territory t : this.temp_map) {
            if (t.getOwnership() == this.player_id) { // if is the player's territory
                remains -= t.getDefenderNum();
            }
        }
        return remains;
    }

    private Territory findTerritory(String t_name) {
        Territory t_to_deploy = null; // the territory to operate on
        for (Territory t : this.temp_map) {
            if (t.getName().equalsIgnoreCase(t_name)) { // if is valid territory
                t_to_deploy = t; 
                return t_to_deploy;
            }
        }
        return null;
    }

    private boolean isOwnTerritory(Territory t) {
        return (t.getOwnership() == this.player_id);
    }
  
    private boolean isValidPath(Territory src, Territory dest) {

<<<<<<< HEAD
        // LinkedList<Territory> visited = new LinkedList<Territory>();
        LinkedList<Territory> queue = new LinkedList<Territory>();
        Set<Territory> visited = new HashSet<Territory>();
        // debug
        System.out.println("dest tid: " + dest.getTid());
=======
        LinkedList<Integer> visited = new LinkedList<Integer>();
        LinkedList<Integer> queue = new LinkedList<Integer>();
>>>>>>> 43147e59191ccc2bbc1c5f3d581ed3c7695ce40f

        queue.add(src.getTid());

        while(queue.size() != 0) { // when queue is not empty
            int tid = queue.poll();

<<<<<<< HEAD
            // debug
            System.out.println("t name: " + t.getName() + ", tid: " + t.getTid());

            if (t.getTid() == dest.getTid()) {
=======
            if (tid == dest.getTid()) {
>>>>>>> 43147e59191ccc2bbc1c5f3d581ed3c7695ce40f
                return true; // find the path
            }

            if (visited.contains(tid)) {
                continue;
            }

            Territory t = findTerritoryByTid(tid);

            for (int neigh : t.getNeighborList()) {
                if ((neigh != -1) && (findOwnershipByTid(neigh) == this.player_id)) {
                    queue.add(neigh);
                }                
            }
            visited.add(t.getTid());
        }

        return false;
    }

    private boolean isAdjacent(Territory src, Territory dest) {

        for (int neigh : src.getNeighborList()) {
            if (neigh != -1) {
                if (neigh == dest.getTid()) { // if dest in neighbout list
                    return true;
                }
            }
        }
        return false;
    }

    private Territory findTerritoryByTid(int tid) {
        for (Territory t : this.temp_map) {
            if (t.getTid() == tid) { // if find the territory
                return t;
            }
        }
        return null; // not found
    }

    private int findOwnershipByTid(int tid) {
        for (Territory t : this.temp_map) {
            if (t.getTid() == tid) { // if find the territory
                return t.getOwnership();
            }
        }
        return -1; // not found
    }

}
