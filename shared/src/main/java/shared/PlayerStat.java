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

    newCard = 0;   // initial cid=0, regular cid 1-6
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

    activatedCards = new HashMap<Integer, Integer>(rhs.getActivatedCards());
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

  public void activateCard(int cid){
    if(cid == 2 || cid == 6){
      activateCard(cid, 5);
      //Communism and loan lasts for 5 turns
    } else {
      //other cards lasts for 1 turn
      activateCard(cid, 1);
    }
  }

  public boolean isPortalActivated(){
    if(activatedCards.get(1) != null){
      return true;
    }
    return false;
  }

  public boolean isCommunismActivated(){
    if(activatedCards.get(2) != null){
      return true;
    }
    return false;
  }

  public boolean isTechnologyWorshipActivated(){
    if(activatedCards.get(3) != null){
      return true;
    }
    return false;
  }

  public boolean isConscriptionActivated(){
    if(activatedCards.get(4) != null){
      return true;
    }
    return false;
  }

  public boolean isSilkRoadActivated(){
    if(activatedCards.get(5) != null){
      return true;
    }
    return false;
  }

  public boolean isLoanActivated(){
    if(activatedCards.get(6) != null){
      return true;
    }
    return false;
  }
  
  public void settleCardCost(int cid){
    switch(cid)
      {
      case 1:
        subtractGold(25);   
        System.out.println("Opening the portal costs 25 gold"); 
        break;
      case 2:
        break;
      case 3:
        break;
      case 4:
        subtractFood(10 * getTerritoryNum());
        System.out.println("Conscription costs 10 food per territory");
        break;
      case 5:
        subtractFood(5 * getTerritoryNum());
        System.out.println("Silk Road costs 5 food per territory");
        break;
      case 6:
        addGold(300);
        System.out.println("Loan: instantly get 300 gold");
        break;  
      default :
        System.out.println("invalid card id");
      }
  }

  public void updateCardTurns(){
    HashMap <Integer, Integer> tempCards = new HashMap <Integer, Integer>(activatedCards);
    
    for (Map.Entry<Integer, Integer> entry : activatedCards.entrySet()) {
      if (entry.getValue() <= 1) {
        //the card effect expired
        tempCards.remove(entry.getKey(), entry.getValue());
      } else {
      	//deduct number of turns left by 1
      	tempCards.put(entry.getKey(), entry.getValue() - 1);
      }
    }
    //temp and swap
    activatedCards = tempCards;
  }
}
