package it.polimi.ingsw.am07.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Game {

    private ArrayList<Player> players;

    private Set<GameCard> availableGameCards;

    private Set<ObjectiveCard> availableObjectiveCards;

    private List<ObjectiveCard> commonObjectives;

    private int nextTurnPlayerIndex;

}
