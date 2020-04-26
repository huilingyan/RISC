package server;

import java.util.ArrayList;

import javax.persistence.*;

import lombok.*;

// TODO: persistence annotation
@Entity
@Getter
@Setter
public class UserInfo {

    @Id
    private String username; // user name

    private String password; // user passwors
    private boolean connected; // socket connection status
    private boolean loggedin; // mark if user's loggedin
    private int activeGid; // gameID of active game, 0 if in not on name

    // default constructor
    public UserInfo() {
        username = "";
        password = "";
        connected = false;
        loggedin = false;
        activeGid = 0;
    }

    public UserInfo(String name, String pin){
        username = name;
        password = pin;
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

    public void setOffline(){
        activeGid = 0;
        connected = false;
        loggedin = false;
    }

    public boolean isOffline(){
        return activeGid==0 && (!connected) && (!loggedin);
    }

}