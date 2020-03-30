package shared;

public class UpgradeOperation extends Operation {
  // upgrade soldiers of a territory
  private Army army_upgrade;

  public UpgradeOperation(String dest, Army army_to_upgrade, Army army_upgraded) {

    this.dest = dest;
    this.army_upgrade = army_to_upgrade;
    this.army_deploy = army_upgraded;
    /*
      e.g. beginning: lv0 50 lv1 25
      upgrade 10 lv0 to lv1
      upgrade 10 lv0 to lv3
    
      store in army_to_upgrade: 20 lv0 (soldiers to remove from original Army)
      store in army_upgraded: 10 lv1, 10 lv3 (soldiers to add to original Army)
    
     */
  }

  public Army getArmyToUpgrade(){
    return this.army_upgrade;
  }

}
