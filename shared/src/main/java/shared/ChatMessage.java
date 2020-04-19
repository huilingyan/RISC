package shared;

import java.io.Serializable;

public class ChatMessage implements Serializable {

  private String srcPlayerName;
  private String destPlayerName;
  private String message;

  public ChatMessage(String from, String to, String str) {
    this.srcPlayerName = from;
    this.destPlayerName = to;
    this.message = str;
  }
  
  //construct a tunnel without setting message 
  public ChatMessage(String from, String to){
    this.srcPlayerName = from;
    this.destPlayerName = to;
  }

  public String getSrcPlayerName() {
    return this.srcPlayerName;
  }

  public String getDestPlayerName() {
    return this.destPlayerName;
  }

  public String getMessage() {
    return this.message;
  }

  public void setSrcPlayerName(String from){
    this.srcPlayerName = from;
  }

  public void setDestPlayerName(String to) {
    this.destPlayerName = to;
  }

  public void setMessage(String str){
    message = str;
  }
}
