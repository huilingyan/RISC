package server;

import java.util.ArrayList;

import shared.*;

public class InitHandler implements Handler {

  @Override
  public ArrayList<Territory> handleAction(
         ArrayList<Territory> map, ArrayList<Operation> actions){
    ArrayList<Territory> newmap = map;
    
    System.out.println("handled init actions");

    return newmap;
  }
}
