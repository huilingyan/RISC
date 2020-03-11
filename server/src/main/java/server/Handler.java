package server;

import java.util.ArrayList;

import shared.*;

interface Handler {
  ArrayList<Territory> handleAction(
      ArrayList<Territory> map, Action action);
  //takes a map and a list of operations, apply operations to map and return updated map
}
