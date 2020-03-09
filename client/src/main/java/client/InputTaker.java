package client;

import java.util.Scanner;
import shared.*;

public class InputTaker {
  //this class will try to get a valid format operation from the user
  //whether the operation can be proceed needs to be checked

  private Displayer dp;

  public InputTaker() {
  }
  
  public InputTaker(Displayer gamedp) {
    this.dp = gamedp;
  }

  public int readnofPlayers(Scanner sc) {
    //the first player must tell the server how many players will join the game
    //assume the game client already prompted the user to input nof players
    int n = 0;
    boolean numbervalid = false;
    while (numbervalid == false) {
      n = readanInt(sc);
      numbervalid = checknofPlayers(n);
    }
    return n;
  }
  

  private boolean checknofPlayers(int n) {
    if (n >= 2 && n <= 5) {
      return true;
    }
    //dp.print_playerssupport()
    System.out.println("this game only supports 2-5 players");
    return false;
  }

  public int readanInt(Scanner sc) {
    //get a single integer from this method
    boolean readsuccess = false;
    int n = 0;
    while (readsuccess == false) {
      try {
        String s = sc.nextLine();
        n = Integer.parseInt(s);
        readsuccess = true;
      }
      catch (NumberFormatException ex) {
        //dp.print_notanumber
        System.out.println("please input just one number");
        
      }
      
    }
    return n;
  }

  public InitOperation readInitOperation(int tid,Scanner sc) {
    //the game client will pick a tid for the user to input how many soldiers they want to put on tid
    //still the game client will prompted the user asking for input
    int n = readanInt(sc);
    
    return new InitOperation(tid, n);
  }
}
