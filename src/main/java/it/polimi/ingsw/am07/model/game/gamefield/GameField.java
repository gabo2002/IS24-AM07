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

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.Matrix;

import java.util.HashMap;

public class GameField {

    private final Matrix<Symbol> fieldMatrix;

    private final HashMap<Side, GameFieldPosition> placedCards;

    private int currentZ;

    public GameField() {
        fieldMatrix = new Matrix<>(2,2, Symbol.EMPTY);
        placedCards = new HashMap<>();
        currentZ = 0;
    }

    /**
     * Checks whether a card can be placed on the game field at a given position.
     *
     * @param card the card to be placed
     * @param pos the position of the top left corner of the card
     * @return true if the card can be placed at the specified position, false otherwise
     * @author Andrea Biasion Somaschini
     */
    public boolean canBePlacedOnFieldAt(Side card, GameFieldPosition pos) {
        if ((pos.x() + pos.y()) % 2 != 0)
            return false;

        boolean placeable = false;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (fieldMatrix.get(pos.x() + i, pos.y() + j) == Symbol.NONE)
                    return false;

                if (fieldMatrix.get(pos.x() + i, pos.y() + j) != Symbol.EMPTY)
                    placeable = true;
            }
        }

        return placeable;
    }

    public int countMatches(GameFieldPattern pattern) {
        return 0;
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

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
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
     * @param pos the position on the game field where the top left card corner will be placed
     * @return a ResourceHolder containing the updated resource counts after placing the card
     */
    public ResourceHolder placeOnFieldAt(Side card, GameFieldPosition pos) {
        ResourceHolder resource = new ResourceHolder();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
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

    public HashMap<Side, GameFieldPosition> getPlacedCards() {
        return placedCards;
    }

}
