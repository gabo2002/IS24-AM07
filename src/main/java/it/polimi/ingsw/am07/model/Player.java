package it.polimi.ingsw.am07.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private final String nickname;

    private final ResourceHolder playerResources;

    private int playerScore;

    private final Pawn playerPawn;

    private final GameField playerGameField;

    private final ArrayList<GameCard> playableCards;    //handCards?

    private ObjectiveCard playerObjectiveCard;

    public Player(String nickname, ResourceHolder playerResources, Pawn playerPawn, GameField playerGameField, ArrayList<GameCard> playableCards){
        this.nickname = nickname;
        this.playerResources = playerResources;
        this.playerPawn = playerPawn;
        this.playerGameField = playerGameField;
        this.playableCards = playableCards;
    }


    public String getNickname() {
        return nickname;
    }

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

    public boolean canBePlacedAt(Side card, GameFieldPosition pos) {
        return false;
    }

    public void placeAt(Side card, GameFieldPosition pos) {

    }

    public HashMap<Side, GameFieldPosition> getPlacedCards() {
        return null;
    }

    public ArrayList<GameCard> getPlayableCards() {
        return playableCards;
    }

    public void addPlayableCard(GameCard card) {
    }
}
