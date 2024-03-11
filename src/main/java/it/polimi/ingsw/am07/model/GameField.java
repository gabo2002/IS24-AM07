package it.polimi.ingsw.am07.model;

import java.util.ArrayList;
import java.util.HashMap;

public class GameField {

    private final ArrayList<ArrayList<Symbol>> map;

    private final HashMap<Side, GameFieldPosition> usedCards;

    public GameField() {
        map = new ArrayList<>();
        usedCards = new HashMap<>();
    }

}
