package client;

import java.util.HashSet;
import java.util.Scanner;
import shared.*;

public class InputTaker {
  //this class will try to get a valid format operation from the user
  //whether the operation can be proceed needs to be checked

  private Displayer dp;
  private HashSet<String> TNameSet;

  public InputTaker() {
    //remember to set Tnameset later!
    this.dp = Displayer.getInstance();
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

  public void setTNameSet(HashSet<String> hs) {
    //TNameSet will store the names of territories for quick lookup
    this.TNameSet = hs;
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
        System.out.println(s);//let user see his input
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

  public int readaPosInt(Scanner sc) {
    int n = -1;
    while (n < 0) {
      n = readanInt(sc);
      if (n < 0) {
        System.out.println("please input a number >=0");
      }
    }
    return n;
  }

  public InitOperation readInitOperation(String tname,Scanner sc) {
    //the game client will pick a territory name for the user to input how many soldiers they want to put on tid
    //still the game client will prompted the user asking for input, this method will return an initoperation whose units >=0
    //still need to operation check
    int n = readaPosInt(sc);
    
    return new InitOperation(tname, n);
  }

  
  public String readaTname(Scanner sc) {
    boolean readsuccess = false;
    String s="";
    String caps = "";
    while (readsuccess == false) {
      s = sc.nextLine();
      System.out.println(s);//let user see his input
      caps = capString(s);
      if (TNameSet.contains(caps) || TNameSet.contains(s)) {
        readsuccess = true;
      }
      else {
        System.out.println("The name you entered is not a territory name.");
      }
    }
    return caps;
  }

  public String capString(String s) {
    if (s.length() <= 1) {
      return s.toUpperCase();
    }
    
    return s.substring(0, 1).toUpperCase() + s.substring(1);
    
  }

  public MoveOperation readMoveOperation(Scanner sc) {
    //This method will prompt for user to input
    System.out.println("Please enter the name of territory to move armies from:");
    String srcT = readaTname(sc);
    System.out.println("Please enter the number of armies you want to move:");
    int n = readaPosInt(sc);
    System.out.println("Please enter the name of territory to move armies to:");
    String destT = readaTname(sc);
    return new MoveOperation(srcT, destT, n);
  }

  public AttackOperation readAttackOperation(Scanner sc) {
    System.out.println("Please enter the name of territory to dispatch armies from:");
    String srcT = readaTname(sc);
    System.out.println("Please enter the number of armies you want to dispatch:");
    int n = readaPosInt(sc);
    System.out.println("Please enter the name of territory you want to attack:");
    String destT = readaTname(sc);
    return new AttackOperation(srcT, destT, n);
  }

  private boolean turn_selectionSTRcheck(String s) {
    boolean accepted = (s.equalsIgnoreCase("D") || s.equalsIgnoreCase("M") || s.equalsIgnoreCase("A"));
    
    return accepted;
  }

  public String readselectionStr(Scanner sc) {//get a legal mode selection string
    String s="";
    boolean readsuccess = false;
    while (readsuccess == false) {
      s = sc.nextLine();
      System.out.println(s);//let user see his input
      if (turn_selectionSTRcheck(s)==true) {
        readsuccess = true;
      }
      else {
        System.out.println("Please choose what to do: A, M, or D");
      }
    }
    
    return capString(s);
  }

  private boolean YesorNoSTRcheck(String s) {
    boolean accepted = (s.equalsIgnoreCase("Y") || s.equalsIgnoreCase("N"));
    
    return accepted;
  }
  
  public boolean readYorN(Scanner sc) {
    //read Y or N from user and return true or false
    String s="";
    boolean readsuccess = false;
    while (readsuccess == false) {
      s = sc.nextLine();
      System.out.println(s);//let user see his input
      if (YesorNoSTRcheck(s)==true) {
        readsuccess = true;
      }
      else {
        System.out.println("Please enter Y or N");
      }
    }
    if (s.equalsIgnoreCase("Y")) {
      return true;
    }
    return false;
  }
}
