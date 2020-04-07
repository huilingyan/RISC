package shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageTest {
  @Test
  public void test_Message() {
    UserMessage m1 = new UserMessage("admin", "pswd", true);
    assert (m1.getUsername().equals("admin"));
    assert (m1.getPassword().equals("pswd"));
    assert (m1.isLogin());
    
    ClientMessage m2 = new ClientMessage(0, -1, new Action());
    m2.getAction();
    assert (m2.getGameID() == 0);
    assert (m2.getStage() == -1);

    ServerMessage m3 = new ServerMessage(0, 1, MapGenerator.gamemapGenerator());
    m3.getMap();
  }

}
