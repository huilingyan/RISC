package shared;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Integer;

public class Action {
    public Map<Integer, ArrayList<Operation> > actions= new HashMap<Integer, ArrayList<Operation> >();

    public void addOperation(int type, GameOperation op) { // add operation to the actionlist

        if (actions.containsKey(type) == true) { // if operation type already exists
            ArrayList<Operation> oplist = actions.get(type); // get the operation list
            oplist.add(op); // append current operation to the list
            actions.replace(type, oplist); // replace the map with new oplist
        }
        else { // if operation type not exist, add to the map
            ArrayList<Operation> oplist = new ArrayList<>(); // create new oplist
            oplist.add(op); // append current operation to the list
            actions.put(type, oplist); // create a new pair of operation for map
        }

    }
}