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

}
