package shared;

import java.util.ArrayList;
import java.io.*;
import java.lang.*;

import shared.Territory;
import shared.*;

public class Displayer {

    private static Displayer displayer = new Displayer();
    private int num_player;

    private Displayer() {

    }

    public static synchronized Displayer getInstance() {
        if (displayer == null) {
            displayer = new Displayer();
        }
        return displayer;
    }

    public void setNumOfPlayer(int num) {
        this.num_player = num;
    }

    
    public void displayMap(ArrayList<Territory> t_map) {
        // For version 1, we're only displaying map via text
        /* Sample Output:
            Territory No.1:
                Name: Duke
                Belongs to: 3
                Next to: UNC, Cornell, CMU, G-Tech

        */   
        // TODO: For version2, visualize it

        for (int i = 0; i < this.num_player; i++) {
            displayOnePlayerMap(i, t_map);
        }
    }

    public void displayOnePlayerMap(int pid, ArrayList<Territory> t_map) {
        for (Territory t : t_map) {
            if (t.getOwnership() == pid) { // display territories in group, by ownership
                System.out.println("Territory No." + t.getTid() + ":"); // name of territory
                System.out.println("\tName: " + t.getName()); // name of territory
                System.out.println("\tBelongs to: player " + t.getOwnership()); // owner 
                System.out.println("\tNumber of units: " + t.getDefenderNum()); // number of units 
                System.out.print("\tNext to: "); // neighbours
    
                ArrayList<Territory> neighlist = t.getNeighborList();
                int cnt = 0;
                for (Territory neigh : neighlist) { // show neighbour list
                    if (neigh != null) {
                        cnt++;
                        System.out.print(neigh.getName() + ((cnt == t.countNeighbors())? "\n" : ", ")); // owner
                        
                    }                   
                }
                System.out.print("\n");
            }
        }
    }

    public void displayBeforeInitMap() {
      System.out.println("Below shows your territories.\n");
    }

    public void displayIntroduction(int player_id) {
                
        System.out.println("You are player " + player_id + ", what would you like to do?");
        System.out.println("(M)ove\n(A)ttack\n(D)one");
    }

// msg during communication with server

    public void connEstablishedMsg(int player_id) {
        System.out.println("Successfully connected to server! You're playing as player " + player_id);
    }

    public void inputPlayerNum() {
        System.out.println("You're the first player. Please enter the number of players:");
    }

    public void disConnectMsg() {
        System.out.println("Disconnected from server!");
    }

    
// msg during initialization

    // for operation validator
    public void showRemainUnitNumber(int totalunit, String name) {
        System.out.println("You have " + totalunit + " units in total. ");
        System.out.println("How many units would you like to deploy to territory " + name + "?");
    }

    public void deployUnits(InitOperation initop) {
        System.out.println("Deploy " + initop.num + " to " + initop.getDest());
    }

// msg during move commit

    public void moveUnits(MoveOperation moveop) {
        System.out.println("Move " + moveop.num + " units from " + moveop.getSrc() + " to " + moveop.getDest());
    }

// msg during attack commit
    
    public void attackUnits(AttackOperation attackop) {
        System.out.println("Attack " + attackop.getDest() + " from " + attackop.getSrc() + " with " + attackop.num + " units");
    }


    public void showErrorMsg(int errorcode) {
        switch(errorcode) {
        case OperationValidator.INVALID_DEST:
            System.out.println("Invalid destination! Please enter again:");
            break;
        case OperationValidator.NO_ENOUGH_UNITS:
            System.out.println("There're no enough units left to deploy!");
            break;
        case OperationValidator.ILLEGAL_NUM:
            System.out.println("The number you input is illegal! Please try again:");
            break;
        case OperationValidator.INVALID_SRC:
            System.out.println("Invalid source! Please enter again:");
            break;
        case OperationValidator.INVALID_PATH:
            System.out.println("There's no path to that territory!");
            break;
        case OperationValidator.NOT_ADJACENT:
            System.out.println("The territory isn't adjacent! You can't attack it.");
            break;
        default:
            break;
        }
    }

// Print the result of the game
    public void winnerAnnouncement() {
        System.out.println("Congratulations! You win the game!");
    }

    public void loseGameAnnouncement() {
        System.out.println("You lose the game.");
    }

    public void gameOverAnnouncement(int pid) {
      System.out.println("Game Over! Player " + pid + " win the game.");
    }

// after player loses the game

    public void showCurrentMap(ArrayList<Territory> curr_map) {
        System.out.println("Here's the current map after last turn:");
        displayMap(curr_map); // show current map after each turn
    }

    public void askForExit() {
      System.out.println("Would you like to exit directly or watch the rest of the game?");
      System.out.println("Press Y(Exit) or N(Watch the game) to continue:");
    }

}
