package shared;

import java.io.Serializable;

/***
 * A class that represents player stats. Contains player id, player name, food
 * resource, gold (technology) resource, max technology level, and number of
 * territories owned by the player
 */
public class PlayerStat implements Serializable {

    private int pid; // player id
    private String name; // player name, same as user name
    private int food; // food resource owned by the player
    private int gold; // gold resource owned by the player, can be used to upgrade max tech level and
                      // solder level
    private int maxTechLvl; // max tech level, start at 1, can be upgraded up to 6
    private int territoryNum; // territory number owned by the player
    private String color; // color of the territories

    // constructor that set default maxTechLvl (1)
    public PlayerStat(int p_id, String p_name, int init_food, int init_gold, int init_territoryNum, String c) {
        pid = p_id;
        name = p_name;
        food = init_food;
        gold = init_gold;
        maxTechLvl = 1; // start with level 1, could be upgraded up to 6
        territoryNum = init_territoryNum;
        color = c;
    }

    // copy constructor
    public PlayerStat(PlayerStat rhs) {
        pid = rhs.getPid();
        name = rhs.getPName();
        food = rhs.getFood();
        gold = rhs.getGold();
        maxTechLvl = rhs.getMaxTechLvl();
        territoryNum = rhs.getTerritoryNum();
        color = rhs.getColor();
    }

    public String getColor() {
        return color;
    }

    public int getPid() {
        return pid;
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

}