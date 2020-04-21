package client;

import shared.Map;

public class Model {

    public GameClient gclient;
    public String player_name;
    public Map worldmap;
    public ChatClient chatClient;

    public Model() {
        this.gclient = new GameClient();
    }

}