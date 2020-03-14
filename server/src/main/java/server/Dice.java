package server;

public class Dice {
  private int sides;

  public Dice(int num) {
    sides = num;
  }

  public int rollDice(){
    int diceResult = (int) (Math.random() * sides + 1);
    return diceResult;
  }

}
