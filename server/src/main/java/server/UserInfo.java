package server;

import lombok.*;

@Getter
@Setter
public class UserInfo {

    private String username; // user name
    private String password; // user passwors
    private boolean connected; // socket connection status
    private boolean loggedin; // mark if user's loggedin
    private int activeGid; // gameID of active game, 0 if in not on name

    // default constructor
    public UserInfo() {
        connected = false;
        loggedin = false;
        activeGid = 0;
    }

    // copy constructor
    public UserInfo(UserInfo rhs){
        username = rhs.getUsername();
        password = rhs.getPassword();
        connected = rhs.isConnected();
        loggedin = rhs.isLoggedin();
        activeGid = rhs.getActiveGid();
    }

    public void switchOut() {
        activeGid = 0;
    }

}