package client;

import java.util.ArrayList;
import java.io.*;

import shared.Territory;
import shared.Operation;

public class Displayer {
    
    public void displayMap(ArrayList<Territory> t_map) {
        // For version 1, we're only displaying map via text
        /* Sample Output:
            Territory No.1:
                Name: Duke
                Belongs to: 3
                Next to: UNC, Cornell, CMU, G-Tech

        */   
        // TODO: For version2, visualize it
        for (Territory t : t_map) {
            System.out.println("Territory No." + t.getTid() + ":"); // name of territory
            System.out.println("\tName: " + t.getName()); // name of territory
            System.out.println("\tBelongs to:" + t.getOwnership()); // owner 
            System.out.print("\tNext to: "); // owner

            ArrayList<Territory> neighlist = t.getNeighborList();
            for (Territory neigh : neighlist) { // show neighbour list
                System.out.print(neigh.getName() + ((neighlist.indexOf(neigh) == neighlist.size() - 1)? ", " : "\n")); // owner
            }
            System.out.print("\n");
        }
    }

    public void displayIntroduction(int num_player, ArrayList<Territory> t_map) {
        for (int i = 0; i < num_player; i++) {
            System.out.println("Player " + i + ":");
            System.out.println("-------------");
            for (Territory t : t_map) {
                if (t.getOwnership() == i) {
                    System.out.print(t.getDefenderNum() + " units in " + t.getName());
                    System.out.print(" (next to: ");
                    ArrayList<Territory> neighlist = t.getNeighborList();
                    for (Territory neigh : neighlist) { // show neighbour list
                        System.out.print(neigh.getName() + ((neighlist.indexOf(neigh) == neighlist.size() - 1)? ", " : ")\n")); // owner
                    }
                }
            }
        }
        System.out.println("You are player" + num_player+ ", what would you like to do?");
        System.out.println("(M)ove\n(A)ttack\n(D)one");
    }

    // msg during communication with server

    public void connEstablishedMsg() {
        System.out.println("Successfully connected to server!");
    }

    public void disConnectMsg() {
        System.out.println("Disconnected from server!");
    }

    
    // error msg during territory setup

    public void noEnoughUnitMsg() {
        System.out.println("There're no enough units left to deploy!");
    }

    public void illegalNumberMsg() { // use it when client inputs negative num, etc
        System.out.println("The number you input is illegal! Please try again:");
    }

    // error msg during move commit

    public void notAdjacentMsg() { 
        System.out.println("You can't move units to a non-adjacent territory!");
    }

    // error msg during attack commit
    public void illegalTerritoryMsg() { 
        System.out.println("You can't attack your own territory...");
    }

    // execute move
    public void displayMove(Operation op) {
        /* PROBLEM: Here we need to import MoveOperation and AttackOperation
        class, which are invisible from client package, thus cannot be imported.*/

    }

    // execute attack
    public void displayAttack() {

    }

    // combat
    public void combatMsg() {

    }



}