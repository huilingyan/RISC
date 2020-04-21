package shared;

import java.io.Serializable;

public class ChatMessage implements Serializable {

  private int fromPid;
  private int toPid;
  private String message;

  public ChatMessage(int from, int to, String str) {
    fromPid = from;
    toPid = to;
    message = str;
  }
  
  //construct a tunnel without setting message 
  public ChatMessage(int from, int to){
    fromPid = from;
    toPid = to;
  }

  public int getFromPid() {
    return fromPid;
  }

  public int getToPid() {
    return toPid;
  }

  public String getMessage() {
    return message;
  }

  public void setFromPid(int from){
    fromPid = from;
  }

  public void setToPid(int to) {
    toPid = to;
  }

  public void setMessage(String str){
    message = str;
  }
}
