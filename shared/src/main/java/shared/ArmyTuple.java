package shared;

public class ArmyTuple {
  private Army hostArmy;
  private Army friendArmy;
  private int hostid;
  private int allyid;

  public ArmyTuple() {
    hostArmy = new Army();
    friendArmy = new Army();
    hostid = -1;
    allyid = -1;
  }

  public ArmyTuple(Army host, int hid) {
    hostArmy = host;
    friendArmy = new Army();
    hostid = hid;
    allyid = -1;
  }

  public ArmyTuple(Army host, int hid, Army friend, int fid) {
    hostArmy = host;
    friendArmy = friend;
    hostid = hid;
    allyid = fid;
  }

  public Army getHostArmy() {
    return hostArmy;
  }

  public Army getFriendArmy() {
    return friendArmy;
  }
  
  public int getHostId(){
  	return hostid;
  }
  
  public int getAllyId(){
  	return allyid;
  }

  public Army getArmyByPid(int pid){
    if (pid == hostid) {
      return hostArmy;
    }
    if (pid == allyid) {
      return friendArmy;
    }
    return null;
  }

  public void setHostArmy(Army rhs, int hid){
    hostArmy = rhs;
    hostid = hid;
  }

  public void setFriendArmy(Army rhs, int fid){
    friendArmy = rhs;
    allyid = fid;
  }

  public int getTotalSoldiers() {
    return hostArmy.getTotalSoldiers() + friendArmy.getTotalSoldiers();
    
  }

  

}
