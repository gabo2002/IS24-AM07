package it.polimi.ingsw.am07.model.lobby;

import java.util.ArrayList;

public class Lobby {

    private final ArrayList<String> nicknames;


    public Lobby(){
        nicknames = new ArrayList<>();
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(String nickname) throws IllegalArgumentException {
        if (nicknames.contains(nickname)) {
            throw new IllegalArgumentException("Nickname already taken");
        }
        nicknames.add(nickname);
    }
}
