package it.polimi.ingsw.am07.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Player> players;

    private final List<GameCard> availableResCards;

    private final List<GameCard> availableGoldCards;

    private final GameCard[] visibleResCards;

    private final GameCard[] visibleGoldCards;


    private final List<ObjectiveCard> availableObjectiveCards;

    private final ObjectiveCard[] commonObjectives; //only 2 cards

    private int nextTurnPlayerIndex;    //Reminder: randomly generated first

    // how do we handle skipTurn?
    // -> maybe with a method that handles players turns: e.g: boolean checkTurn(int turn);
    // to skip the turn you can simply put checkTurn(myTurn) to false --> passes to the next


    public Game() {
        players = new ArrayList<>();
        availableGoldCards = new ArrayList<>();
        availableResCards = new ArrayList<>();
        visibleResCards = new GameCard[2];
        visibleGoldCards = new GameCard[2];
        availableObjectiveCards = new ArrayList<>();
        commonObjectives = new ObjectiveCard[2];
        nextTurnPlayerIndex = 0;
    }


    // setting up all the getters for the cards

}
