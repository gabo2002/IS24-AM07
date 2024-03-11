package it.polimi.ingsw.am07.model;

import java.util.ArrayList;

public class Player {

    private final String nickname;

    private ResourceHolder playerResources;

    private int playerScore;

    private Pawn playerPawn;

    private GameField playerGameField;

    private ArrayList<GameCard> playerGameCards;    //handCards?

    private ObjectiveCard playerObjectiveCard;

    public Player(String nickname){
        this.nickname = nickname;
    }


    // setting up all the getters/setters for the Player
    public int getPlayerScore() {
        return playerScore;
    }

    public Pawn getPlayerPawn() {
        return playerPawn;
    }

    public ObjectiveCard getPlayerObjectiveCard() {
        return playerObjectiveCard;
    }

    public void setPlayerObjectiveCard(ObjectiveCard playerObjectiveCard) {
        this.playerObjectiveCard = playerObjectiveCard;
    }

    public ResourceHolder getPlayerResources() {
        return playerResources;
    }

    public void setPlayerResources(ResourceHolder playerResources) {
        this.playerResources = playerResources;
    }
}
