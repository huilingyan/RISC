package shared;
import java.io.Serializable;

/***
 * A UserMessage class that contains user login/register info
 */
public class UserMessage implements Serializable {

    private String username;
    private String password;
    private boolean login;    // true for login, false for register

    public UserMessage(String name, String pass_word, boolean isLogin){
        username = name;
        password = pass_word;
        login = isLogin;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public boolean isLogin(){
        return login;
    }

}