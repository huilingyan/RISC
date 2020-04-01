package server;

import shared.*;
import java.util.HashMap;

public class GameWorker extends Thread {

    private Game game;
    private Gameserver boss;

    public GameWorker(Game g, Gameserver server) {
        game = g;
        boss = server;
    }

    // run the thread
    public void run() {
        // keep running when game is not over
        while (game.getStage() < GameMessage.GAME_OVER) {
            switch (game.getStage()) {
                case (GameMessage.ERROR):
                    System.out.println("Error: Game " + game.getGid() + " is on error state");
                    break;
                case (GameMessage.WAIT_FOR_PLAYERS): // wait for players
                    // wait until all players join
                    while (!game.isFull()) {
                    }
                    // debug
                    System.out.println("All players joined game " + game.getGid());
                    // set player stats
                    game.setPlayerStats();
                    // change stage to initialize units
                    game.setStage(GameMessage.INITIALIZE_UNITS);
                    break;
                case (GameMessage.INITIALIZE_UNITS): // initialize units
                    // wait until all active players send action
                    while (!game.turnFinished()) {
                    }
                    initializeGameUnits();
                    break;
                case (GameMessage.GAME_PLAY): // play game
                    // wait until all active players send action
                    while (!game.turnFinished()) {
                    }
                    updateOneTurn();
                    break;
                default:
                    System.out.println("Game state: " + game.getStage());
            } // switch
            game.notifyAll(); // notify clientworkers
        } // while
          // gameover, gameworker exits
    }

    private void initializeGameUnits(){
        // debug
        System.out.println("All players placed units in game " + game.getGid());
        // validate actions
        Map gameMap = game.getMap();
        Action ac = validateAllInitOperations(game.getTempActionList(), gameMap);
        // call init handler
        InitHandler handler = new InitHandler();
        Map newMap = handler.handleAction(gameMap, ac);
        // set new map
        game.setMap(newMap);
        // clear tempActionList
        game.clearTempActions();
        // update stage
        game.setStage(GameMessage.GAME_PLAY);
    }

    private void updateOneTurn(){
        // debug
        System.out.println("All players finished one turn in game " + game.getGid());
        // validate actions
        Map gameMap = game.getMap();
        Action ac = validateAllGameOperations(game.getTempActionList(), gameMap);
        // call game handler
        GameHandler handler = new GameHandler();
        Map newMap = handler.handleAction(gameMap, ac);
        // check game over
        // if yes, update game stage
        // if no, update map
        if (newMap.isGameOver()){
            game.setStage(GameMessage.GAME_OVER);
        } else {
            newMap.updateUnitandResource();
        }
        // set new map
        game.setMap(newMap);
        // clear tempActionList
        game.clearTempActions();

    }

    /****
     * Returns an action which only contains validated init operations
     ****/
    private Action validateInitAction(Action ac, int pid, Map gameMap) {
        OperationValidator validator = new OperationValidator(pid, gameMap);
        for (InitOperation op : ac.getInitOperations()) {
            validator.isValidInitOperation(op, Map.INIT_UNIT);
        }
        return validator.getAction();
    }

    /*****
     * Returns an action which only contains validated game operations and upgrade max tech level or not
     ******/
    private Action validateGameAction(Action ac, int pid, Map gameMap) {
        OperationValidator validator = new OperationValidator(pid, gameMap);  // TODO
        for (MoveOperation op : ac.getMoveOperations()) {
            validator.isValidMoveOperation(op);
        }
        for (AttackOperation op : ac.getAttackOperations()) {
            validator.isValidAttackOperation(op);
        }
        for (UpgradeOperation: ac.getUpgradeOperations()){
            validator.isValidUpdateOperation(op);  // TODO
        }
        if (ac.getUpgradeMaxTechHashMap().containsKey(pid) && ac.getUpgradeMaxTechHashMap().get(pid)==true){
            validator.isValidUpgradeMaxTech();  // TODO
        }
        return validator.getAction();
    }

    private Action validateAllInitOperations(HashMap<Integer, Action> actionList, Map gamemap) {
        Action action = new Action();
        for (HashMap.Entry<Integer, Action> entry: actionList.entrySet()){
            int pid = entry.getKey();
            Action ac = entry.getValue();
            action.concatInitOperation(validateInitAction(ac, pid, gamemap));
        }
        return action;
    }

    private Action validateAllGameOperations(HashMap<Integer, Action> actionList, Map gamemap) {
        Action action = new Action();
        for (HashMap.Entry<Integer, Action> entry: actionList.entrySet()){
            int pid = entry.getKey();
            Action ac = entry.getValue();
            action.concatGameOperation(validateGameAction(ac, pid, gamemap));
        }
        return action;
    }

}