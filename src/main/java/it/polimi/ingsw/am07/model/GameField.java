package it.polimi.ingsw.am07.model;

import java.util.ArrayList;
import java.util.HashMap;

public class GameField {

    private final ArrayList<ArrayList<Symbol>> fieldMatrix;

    private final HashMap<Side, GameFieldPosition> placedCards;

    public GameField() {
        fieldMatrix = new ArrayList<>();
        placedCards = new HashMap<>();
    }

    public boolean canBePlacedOnFieldAt(Side card, GameFieldPosition pos) {
        return false;
    }

    public int countMatches(GameFieldPattern pattern) {
        return 0;
    }

    public int countCoveredCorners(Side card) {
        return 0;
    }

    public ResourceHolder placeOnFieldAt(Side card, GameFieldPosition pos) {
        return null;
    }
    public HashMap<Side, GameFieldPosition> getPlacedCards() {
        return placedCards;
    }
}
