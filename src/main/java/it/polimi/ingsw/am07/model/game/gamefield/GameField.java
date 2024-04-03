/*
 * Codex Naturalis - Final Assignment for the Software Engineering Course
 * Copyright (C) 2024 Andrea Biasion Somaschini, Roberto Alessandro Bertolini, Omar Chaabani, Gabriele Corti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Please note that the GNU General Public License applies only to the
 * files that contain this license header. Other files within the project, such
 * as assets and images, are property of the original owners and may be
 * subject to different copyright terms.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.polimi.ingsw.am07.model.game.gamefield;

import it.polimi.ingsw.am07.exceptions.CardNotFoundException;
import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideFieldRepresentation;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import it.polimi.ingsw.am07.utils.matrix.MatrixElementIterator;
import it.polimi.ingsw.am07.utils.matrix.MatrixSubMatrixIterator;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the game field where cards are placed during the game.
 * Each player has their own game field. The game field is a 2D grid where cards are placed. The grid is made of cells,
 * each of which can contain a card. The game field is used to check whether a card can be placed at a given position,
 * to place a card on the game field and to count the number of patterns that match a given pattern in the game field.
 *
 * @author Gabriele Corti
 */
public class GameField {

    private final Matrix<Symbol> fieldMatrix;

    private final Map<Side, GameFieldPosition> placedCards;

    private int currentZ;

    /**
     * Creates a new game field with an empty grid and no cards placed on it.
     */
    public GameField() {
        fieldMatrix = new Matrix<>(SideFieldRepresentation.SIDE_SIZE, SideFieldRepresentation.SIDE_SIZE, Symbol.EMPTY);
        placedCards = new HashMap<>();
        currentZ = 0;
    }

    /**
     * Checks whether a card can be placed on the game field at a given position.
     *
     * @param card the card to be placed
     * @param pos  the position of the top left corner of the card
     * @return true if the card can be placed at the specified position, false otherwise
     * @author Andrea Biasion Somaschini
     */
    public boolean canBePlacedOnFieldAt(Side card, GameFieldPosition pos) {
        if (pos.x() == 0 && pos.y() == 0) {
            // Checks the first card --> starter card
            return fieldMatrix.get(0, 0) == Symbol.EMPTY && card.color() == Symbol.STARTER;
        }

        if ((pos.x() + pos.y()) % 2 != 0) {
            return false;
        }

        boolean placeable = false;

        for (int i = 0; i < SideFieldRepresentation.SIDE_SIZE; i++) {
            for (int j = 0; j < SideFieldRepresentation.SIDE_SIZE; j++) {
                if (fieldMatrix.get(pos.x() + i, pos.y() + j) == Symbol.NONE)
                    return false;

                if (fieldMatrix.get(pos.x() + i, pos.y() + j) != Symbol.EMPTY)
                    placeable = true;
            }
        }

        return placeable;
    }

    /**
     * Retrieves the color at the specified position on the game field. if the card is not present in the game field, an exception is thrown.
     *
     * @param x the x coordinate on the game field
     * @param y the y coordinate on the game field
     * @return the color at the specified position on the game field
     * @throws CardNotFoundException if the card is not present in the game field
     */
    private Symbol getCardColorAt(int x, int y) throws CardNotFoundException {
        if ((x + y) % 2 != 0) {
            throw new CardNotFoundException("The card is not present in the game field");
        }

        for (Side card : placedCards.keySet()) {
            GameFieldPosition pos = placedCards.get(card);
            if (pos.x() == x && pos.y() == y) {
                return card.color();
            }
        }

        throw new CardNotFoundException("The card is not present in the game field");
    }

    /**
     * get the number of patterns that match the given pattern in the game field.
     *
     * @param pattern the pattern to match
     * @return the number of matches of the given pattern in the game field
     * @author Gabriele Corti
     */
    public int countMatches(GameFieldPattern pattern) {
        int matches = 0;
        Matrix<Symbol> fieldCopy = fieldMatrix.copy();  // copy the field to avoid modifying the original field while calling remove() method

        Matrix<Symbol> shape = pattern.getShape();
        Matrix<Symbol> deletionMask = pattern.getDeletionMask();

        MatrixSubMatrixIterator<Symbol> subMatrixIterator = fieldCopy.iterator(shape.getWidth(), shape.getHeight());

        while (subMatrixIterator.hasNext()) {
            Matrix<Symbol> subMatrix = subMatrixIterator.next();
            boolean match = true;

            int relativeX = subMatrixIterator.getOffsetX();
            int relativeY = subMatrixIterator.getOffsetY();

            if (!subMatrix.containsShape(shape)) {
                continue;
            }

            MatrixElementIterator<Symbol> elementIterator = (MatrixElementIterator<Symbol>) pattern.pattern().iterator();

            while (match && elementIterator.hasNext()) {
                Symbol color = elementIterator.next();
                if (!color.equals(Symbol.EMPTY)) {
                    try {
                        Symbol cardColor = getCardColorAt(relativeX + elementIterator.getCurrentX(), relativeY + elementIterator.getCurrentY());
                        if (!cardColor.equals(color)) {
                            match = false;
                        }
                    } catch (CardNotFoundException e) {
                        match = false;
                    }
                }
            }

            if (match) {
                matches++;
                subMatrixIterator.remove(deletionMask);    // remove the submatrix from the field, so that it is not counted again
            }
        }
        return matches;
    }

    /**
     * This method counts the number of corners in a 2x2 area of the game field that are not empty.
     * The top left corner of the 2x2 area is specified by the given position.
     *
     * @param pos the position of the top left corner of the 2x2 area on the game field
     * @return the number of corners in the 2x2 area that are not empty
     * @author Andrea Biasion Somaschini
     */
    public int countCoveredCorners(GameFieldPosition pos) {
        int count = 0;

        for (int i = 0; i < SideFieldRepresentation.SIDE_SIZE; i++) {
            for (int j = 0; j < SideFieldRepresentation.SIDE_SIZE; j++) {
                if (fieldMatrix.get(pos.x() + i, pos.y() + j) != Symbol.EMPTY) {
                    ++count;
                }
            }
        }

        return count;
    }


    /**
     * Places a card on the game field at the specified position and updates resources accordingly.
     *
     * @param card the card to be placed on the game field
     * @param pos  the position on the game field where the top left card corner will be placed
     * @return a ResourceHolder containing the updated resource counts after placing the card
     */
    public ResourceHolder placeOnFieldAt(Side card, GameFieldPosition pos) {
        ResourceHolder resource = new ResourceHolder();

        for (int i = 0; i < SideFieldRepresentation.SIDE_SIZE; i++) {
            for (int j = 0; j < SideFieldRepresentation.SIDE_SIZE; j++) {
                resource.decrementResource(fieldMatrix.get(pos.x() + i, pos.y() + j));
                fieldMatrix.set(pos.x() + i, pos.y() + j, card.fieldRepresentation().corners().get(i, j));
            }
        }

        GameFieldPosition fieldPosition = new GameFieldPosition(pos.x(), pos.y(), currentZ);
        placedCards.put(card, fieldPosition);
        ++currentZ;

        resource.add(card.resources());

        return resource;
    }

    /**
     * Retrieves a HashMap containing the cards that have been placed on the game field, along with their positions.
     *
     * @return A HashMap where the keys are the placed cards and the values are their corresponding positions on the game field.
     */
    public Map<Side, GameFieldPosition> getPlacedCards() {
        return placedCards;
    }

}
