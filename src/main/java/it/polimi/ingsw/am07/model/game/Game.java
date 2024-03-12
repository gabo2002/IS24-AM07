package it.polimi.ingsw.am07.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private final List<Player> players;

    private final List<GameCard> availableResCards;

    private final List<GameCard> availableGoldCards;

    private final GameCard[] visibleResCards;

    private final GameCard[] visibleGoldCards;

    private final String selfNickname;

    private final List<ObjectiveCard> availableObjectiveCards;

    private final ObjectiveCard[] commonObjectives; //only 2 cards

    private int nextTurnPlayerIndex;    //Reminder: randomly generated first

    private boolean endGameReached;

    public Game(String selfNickname) {
        this.selfNickname = selfNickname;
        players = new ArrayList<>();
        availableGoldCards = new ArrayList<>();
        availableResCards = new ArrayList<>();
        visibleResCards = new GameCard[2];
        visibleGoldCards = new GameCard[2];
        availableObjectiveCards = new ArrayList<>();
        commonObjectives = new ObjectiveCard[2];
        nextTurnPlayerIndex = 0;
        endGameReached = false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getAvailableResCardsSize() {
        return availableResCards.size();
    }

    public int getAvailableGoldCardsSize() {
        return availableGoldCards.size();
    }

    public GameCard[] getVisibleResCards() {
        return visibleResCards;
    }

    public GameCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }

    public String getSelfNickname() {
        return selfNickname;
    }

    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    public void incrementTurn() {
    }

    public int getNextTurnPlayerIndex() {
        return nextTurnPlayerIndex;
    }

    public GameCard pickRandomCard() {
        return null;
    }

    public GameCard popResCard(GameCard card) {
        return null;
    }

    public GameCard popGoldCard(GameCard card) {
        return null;
    }



}
