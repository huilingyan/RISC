package server;

import java.util.ArrayList;

import shared.*;

public class MoveHandler implements Handler {

  @Override
  public ArrayList<Territory> handleAction(
         ArrayList<Territory> map, ArrayList<Operation> actions){
    ArrayList<Territory> newmap = map;
    
    System.out.println("handled move actions");

    return newmap;
  }
}
