package it.polimi.ingsw.am07.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Player> players;

    private final List<GameCard> availableResCards;

    private final List<GameCard> availableGoldCards;

    private final GameCard[] visibleCards;

    private final List<ObjectiveCard> availableObjectiveCards;

    private final ObjectiveCard[] commonObjectives; //only 2 cards

    private int nextTurnPlayerIndex;    //Reminder: randomly generated first

    // how do we handle skipTurn?

    public Game() {
        players = new ArrayList<>();
        availableGoldCards = new ArrayList<>();
        availableResCards = new ArrayList<>();
        visibleCards = new GameCard[4];
        availableObjectiveCards = new ArrayList<>();
        commonObjectives = new ObjectiveCard[2];
        nextTurnPlayerIndex = 0;
    }

}
