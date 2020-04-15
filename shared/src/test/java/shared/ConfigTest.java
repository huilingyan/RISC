package shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConfigTest {
  @Test
  public void test_Config() {
    Config config = new Config("config.properties");
    String port = config.readProperty("port");
    
   Config confign = new Config("null");
   Config configio = new Config("TerritoryTest.java");
  
  }

}
