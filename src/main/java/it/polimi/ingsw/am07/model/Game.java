package it.polimi.ingsw.am07.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Player> players;

    private final List<GameCard> availableGameCards;

    private final List<ObjectiveCard> availableObjectiveCards;

    private final List<ObjectiveCard> commonObjectives;

    private int nextTurnPlayerIndex;


    public Game() {
        players = new ArrayList<>();
        availableGameCards = new ArrayList<>();
        availableObjectiveCards = new ArrayList<>();
        commonObjectives = new ArrayList<>();
        nextTurnPlayerIndex = 0;
    }

}
