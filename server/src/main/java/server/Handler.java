package server;

import java.util.ArrayList;

import shared.*;

interface Handler {
  ArrayList<Territory> handleAction(
      ArrayList<Territory> map, ArrayList<Operation> actions);
  //takes a map and a list of operations, apply operations to map and return updated map
}
