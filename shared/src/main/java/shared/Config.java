package shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
A class that has a Properties field and can read property from it
 ***/
public class Config {

  private Properties pro;

  /***
  The constructor take a String as the filepath for properties file, and create the properties object
  according to that path  
   ***/
  public Config(String filePath) {
    pro = new Properties();
    InputStream inStream = getClass().getClassLoader().getResourceAsStream(filePath);
    if (inStream == null) {
      System.out.println("Cannot find the file " + filePath);
    } else {
      try {
        pro.load(inStream);
      } catch (IOException e) {
        System.out.println("Cannot open properties from the InputStream");
      }
    }
    
  }

  /***
  Given the key, return the property value.
  If the key doesn't exist in properties, return null  
  ****/
  public String readProperty(String key) {
    return pro.getProperty(key); 
  }
  
}
