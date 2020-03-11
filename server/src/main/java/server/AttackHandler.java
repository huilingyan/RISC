package server;

import java.util.ArrayList;

import shared.*;

public class AttackHandler extends Handler {

  @Override
  public ArrayList<Territory> handleAction(
         ArrayList<Territory> map, Action action){
    ArrayList<Territory> newmap = map;
    
    System.out.println("handled attack actions");

    return newmap;
  }
}
