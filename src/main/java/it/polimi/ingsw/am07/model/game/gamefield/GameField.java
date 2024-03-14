package it.polimi.ingsw.am07.model.game.gamefield;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.Matrix;

import java.util.HashMap;

public class GameField {

    private final Matrix<Symbol> fieldMatrix;

    private final HashMap<Side, GameFieldPosition> placedCards;

    public GameField() {
        fieldMatrix = new Matrix<>(2,2, Symbol.BLANK);
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
