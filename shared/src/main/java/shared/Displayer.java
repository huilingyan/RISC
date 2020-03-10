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
                System.out.println("\tBelongs to:" + t.getOwnership()); // owner 
                System.out.println("\tNumber of units:" + t.getDefenderNum()); // number of units 
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

    public void displayIntroduction(ArrayList<Territory> t_map, int player_id) {
        for (int i = 0; i < this.num_player; i++) {
            System.out.println("Player " + i + ":");
            System.out.println("-------------");
            for (Territory t : t_map) {
                if (t.getOwnership() == i) {
                    System.out.print(t.getDefenderNum() + " units in " + t.getName());
                    System.out.print(" (next to: ");
                    ArrayList<Territory> neighlist = t.getNeighborList();
                    int cnt = 0;
                    for (Territory neigh : neighlist) { // show neighbour list
                      if (neigh != null) {
                        cnt++;
                        System.out.print(neigh.getName() + ((cnt == t.countNeighbors())? ")\n" : ",")); // owner
                      }                        
                    }
                }
            }
            System.out.print("\n");
        }
        System.out.println("You are player " + player_id + ", what would you like to do?");
        System.out.println("(M)ove\n(A)ttack\n(D)one");
    }

// msg during communication with server

    public void connEstablishedMsg() {
        System.out.println("Successfully connected to server!");
    }

    public void disConnectMsg() {
        System.out.println("Disconnected from server!");
    }

    
// msg during initialization

    // for input validator
    public void illegalNumberMsg() { // use it when client inputs negative num, etc
        System.out.println("The number you input is illegal! Please try again:");
    }
    
    // for operation validator
    public void showRemainUnitNumber(int totalunit) {
        System.out.println("You have " + totalunit + "units in total. ");
        System.out.println("How many units would you like to deploy to this territory?");
    }

    // can be used by move and attack as well
    public void invalidDest() {
        System.out.println("Invalid destination! Please enter again:");
    }

    public void noEnoughUnitMsg() {
        System.out.println("There're no enough units left to deploy!");
    }

    public void deployUnits(InitOperation initop) {
        System.out.println("Deploy " + initop.num + " to " + initop.dest);
    }


// msg during move commit

    public void noPathMsg() { 
        System.out.println("There's no path to that territory!");
    }

// msg during attack commit
    public void illegalTerritoryMsg() { 
        System.out.println("You can't attack your own territory...");
    }
    

}
