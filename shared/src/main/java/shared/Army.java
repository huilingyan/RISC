package shared;

public class Army {
  private int num_units;//name can be changed later

  public Army() {//empty constructor
  };

  public Army(int num){//constructor
    num_units = num;
  }

  public Army(Army rhs) {//copy constructor
    num_units = rhs.getUnitNumber();
  }
  
  public void setUnitNumber(int num) {
    num_units = num;
  }

  public int getUnitNumber() {
    return num_units;
  }

  public void addUnits(int num) {
    //can throw exception if num is negative
    num_units += num;
  }

  public void subtractUnits(int num) {
    //can throw exception if num is negative
    num_units -= num;
  }
}
