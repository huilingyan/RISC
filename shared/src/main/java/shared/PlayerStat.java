package shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/***
 * A class that represents player stats. Contains player id, player name, food
 * resource, gold (technology) resource, max technology level, and number of
 * territories owned by the player
 */
public class PlayerStat implements Serializable {

  private int pid; // player id
  private int aid; //alliance id
  private boolean allied; //set to true if the player is within an alliance
  //evolution 3:default value = pid; when allied, aid = min(pid, allied_pid)
  private String name; // player name, same as user name
  
  private int food; // food resource owned by the player
  private int gold; // gold resource owned by the player, can be used to upgrade max tech level and
  // solder level
  private int maxTechLvl; // max tech level, start at 1, can be upgraded up to 6
  private int territoryNum; // territory number owned by the player
  private String color; // color of the territories

  private int newCard; //(card id for the card drawn this turn
  private HashMap<Integer, Integer> activatedCards; //cid, effecting turns left 

  // constructor that set default maxTechLvl (1)
  public PlayerStat(int p_id, String p_name, int init_food, int init_gold, int init_territoryNum, String c) {
    pid = p_id;
    aid = pid;//default value = pid
    allied = false;
    name = p_name;
    food = init_food;
    gold = init_gold;
    maxTechLvl = 1; // start with level 1, could be upgraded up to 6
    territoryNum = init_territoryNum;
    color = c;

    activatedCards = new HashMap<Integer, Integer>();
  }

  // copy constructor
  public PlayerStat(PlayerStat rhs) {
    pid = rhs.getPid();
    aid = rhs.getAid();
    allied = rhs.isAllied();
    name = rhs.getPName();
    food = rhs.getFood();
    gold = rhs.getGold();
    maxTechLvl = rhs.getMaxTechLvl();
    territoryNum = rhs.getTerritoryNum();
    color = rhs.getColor();
    newCard = rhs.getNewCard();
    
    //deep copy, do not affect original cards
    activatedCards = new HashMap<Integer, Integer>(activatedCards);

  }

  public String getColor() {
    return color;
  }

  public int getPid() {
    return pid;
  }
  
  public int getAid() {
    return aid;
  }

  public boolean isAllied(){
    return allied;
  }
  
  public String getPName() {
    return name;
  }

  public int getFood() {
    return food;
  }

  public int getGold() {
    return gold;
  }

  public int getMaxTechLvl() {
    return maxTechLvl;
  }

  public int getTerritoryNum() {
    return territoryNum;
  }

  public int getNewCard(){
    return newCard;
  }

  public HashMap<Integer, Integer> getActivatedCards(){
    
    return activatedCards;
  }

  public void formAlliance(int newPid) {
    //i added a formAlliance(int p1, int p2) method in Map which calls this method
    aid = Math.min(pid, newPid); //when allied, aid = min(pid, allied_pid)
    allied = true;
  }

  public void breakAlliance(){
    aid = pid;
    allied = false;
  }
    
  public boolean hasTerritory() {
    return territoryNum > 0;
  }

  // upgrade max tech level by 1
  public void upgradeMaxTechLvl() {
    maxTechLvl++;
  }

  public void setMaxTechLvl(int l) {
    maxTechLvl = l;
  }

  public void setTerritoryNum(int num) {
    territoryNum = num;
  }

  public void addTerritoryNum(int num) {
    territoryNum += num;
  }

  public void subtractTerritoryNum(int num) {
    territoryNum -= num;
  }

  public void addFood(int num) {
    food += num;
  }

  public void subtractFood(int num) {
    food -= num;
  }

  public void addGold(int num) {
    gold += num;
  }

  public void subtractGold(int num) {
    gold -= num;
  }

  public void setNewCard(int cid) {
    newCard = cid;
  }

  public void activateCard(int cid, int turns) {
    //indicate which card to activate and how many turns it will last
    activatedCards.put(cid, turns);
    //If the map previously contained an active card, the old turn value is replaced.
  }

  public void updateCardTurns(){
    for (Map.Entry<Integer, Integer> entry : activatedCards.entrySet()) {
      if (entry.getValue() <= 1) {
        //the card effect expired, remove card from map
        activatedCards.remove(entry.getKey(), entry.getValue());
      }
      //deduct number of turns left by 1
      activatedCards.put(entry.getKey(), entry.getValue() - 1);
     } 
  }
}
