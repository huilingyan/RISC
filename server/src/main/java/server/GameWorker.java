package server;

import shared.*;

import java.util.ArrayList;
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
        System.out.println("Start a new game worker");
        // keep running when game is not over
        while (game.getStage() < GameMessage.GAME_OVER) {
            switch (game.getStage()) {
                case (GameMessage.ERROR):
                    System.out.println("Error: Game " + game.getGid() + " is on error state");
                    synchronized (game) {
                        notifyClientWorkers();
                    }
                    break;
                case (GameMessage.WAIT_FOR_PLAYERS): // wait for players
                    // wait until all players join
                    System.out.println("Player num: " + game.getPlayerNum());
                    while (!game.isFilled()) {
                        sleepOnThread(10);
                    }
                    // debug
                    System.out.println("All players joined game " + game.getGid());
                    // set player stats
                    game.setPlayerStats();
                    // change stage to initialize units
                    game.setStage(GameMessage.INITIALIZE_UNITS);
                    HibernateUtil.updateGame(game);  // update game in database
                    synchronized (game) {
                        notifyClientWorkers();
                    }
                    break;
                case (GameMessage.INITIALIZE_UNITS): // initialize units
                    // wait until all active players send action
                    while (!boss.turnFinished(game)) {
                        sleepOnThread(10);
                    }
                    synchronized (game) {
                        initializeGameUnits();
                        notifyClientWorkers();
                    }
                    break;
                case (GameMessage.GAME_PLAY): // play game
                    // wait until all active players send action
                    while (!boss.turnFinished(game)) {
                        sleepOnThread(10);
                    }
                    synchronized (game) {
                        updateOneTurn();
                        notifyClientWorkers();
                    }
                    break;
                default:
                    System.out.println("Game state: " + game.getStage());
                    synchronized (game) {
                        notifyClientWorkers();
                    }
            } // switch
        } // while
          // gameover, gameworker exits
          boss.deleteGame(game);  // delete game

    }

    private void notifyClientWorkers() {
        // System.out.println("Game worker notifies all client workers");
        game.notifyAll(); // notify clientworkers
    }

    private void sleepOnThread(int time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // check if game is over
    private boolean isGameOver(Map map) {
        int total = map.getTerritories().size();
        for (PlayerStat ps : map.getPlayerStats()) {
            // check if the player owns all territory
            if (ps.getTerritoryNum() == total) {
                return true;
            }
        }
        return false;
    }

    private void initializeGameUnits() {
        // debug
        System.out.println("All players placed units in game " + game.getGid());
        // validate actions
        Map gameMap = game.getMap();
        Action ac = validateAllInitOperations(game.getTempActionList(), gameMap);
        // call init handler
        InitHandler handler = new InitHandler();
        Map newMap = handler.handleAction(gameMap, ac);
        // turn++
        newMap.addTurnByOne();
        // draw cards
        drawCards(newMap);
        // set new map
        game.setMap(newMap);
        // clear tempActionList
        game.clearTempActions();
        // update stage
        game.setStage(GameMessage.GAME_PLAY);
        // update game in db
        HibernateUtil.updateGame(game);

    }

    private void updateOneTurn() {
        // debug
        // System.out.println("All players finished one turn in game " + game.getGid());
        // validate actions
        Map gameMap = game.getMap();
        Action ac = validateAllGameOperations(game.getTempActionList(), gameMap);
        // call game handler
        GameHandler handler = new GameHandler();
        Map newMap = handler.handleAction(gameMap, ac);
        // check game over
        // if yes, update game stage
        // if no, check active player number and update map
        if (isGameOver(newMap)) {
            game.setStage(GameMessage.GAME_OVER);
        } else if (boss.noActivePlayer(game)) {
            // if no active player, do nothing
            // System.out.println("No active player in game " + game.getGid());
        } else {
            // TODO: accomodate card system
            newMap.updateUnitandResource();
            // draw cards
            drawCards(newMap);
        }
        // set new map
        game.setMap(newMap);
        // clear tempActionList
        game.clearTempActions();
        // update game in db
        HibernateUtil.updateGame(game);

    }

    private void drawCards(Map map){
        Dice dice = new Dice(6);  // 6 sided dice
        ArrayList<Integer> newCards = new ArrayList<Integer>();
        for (int i=0; i<game.getPlayerNum(); i++){
            newCards.add(dice.rollDice());
        }
        map.setNewCards(newCards);
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
        // return ac; // not validated
    }

    /*****
     * Returns an action which only contains validated game operations and upgrade
     * max tech level or not
     ******/
    private Action validateGameAction(Action ac, int pid, Map gameMap) {
        OperationValidator validator = new OperationValidator(pid, gameMap);
        // 0. validate card usage
        if (ac.getNewCards().containsKey(pid) && ac.getNewCards().get(pid) == true){
            validator.isValidCardUsage();
        }
        // 1. validate upgrade operations
        for (UpgradeOperation uop : ac.getUpgradeOperations()) {
            validator.isValidUpgradeOperation(uop);
        }
        // 2. validate move operations
        for (MoveOperation mop : ac.getMoveOperations()) {
            validator.isValidMoveOperation(mop);
        }
        // 2.5 move from ally's army
        for (MoveOperation maop : ac.getMoveFromAllyOperations()){
            validator.isValidMoveOperation(maop);
        }
        // 3. validate attack operations
        for (AttackOperation aop : ac.getAttackOperations()) {
            validator.isValidAttackOperation(aop);
        }
        // 4. validate upgrade max tech level
        if (ac.getUpgradeMaxTechHashMap().containsKey(pid) && ac.getUpgradeMaxTechHashMap().get(pid) == true) {
            validator.isValidUpgradeMaxTechLv();
        }
        // 5. validate alliance formation
        if (ac.getAllianceRequests().containsKey(pid)) {
            validator.isValidAllianceRequest(ac.getAllianceRequests().get(pid));
        }
        
        return validator.getAction();
        // return ac; // not validated
    }

    private Action validateAllInitOperations(java.util.Map<Integer, Action> actionList, Map gamemap) {
        Action action = new Action();
        for (java.util.Map.Entry<Integer, Action> entry : actionList.entrySet()) {
            int pid = entry.getKey();
            Action ac = entry.getValue();
            action.concatInitOperation(validateInitAction(ac, pid, gamemap));
        }
        return action;
    }

    private Action validateAllGameOperations(java.util.Map<Integer, Action> actionList, Map gamemap) {
        Action action = new Action();
        for (java.util.Map.Entry<Integer, Action> entry : actionList.entrySet()) {
            // skip the action sent by already losed player
            int pid = entry.getKey();
            if (gamemap.getPlayerStatByPid(pid).hasTerritory()) {
                Action ac = entry.getValue();
                action.concatGameOperation(validateGameAction(ac, pid, gamemap));
            }
        }
        return action;
    }

}
