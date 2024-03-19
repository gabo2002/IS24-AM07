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
import it.polimi.ingsw.am07.model.game.side.*;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {

    @Test
    void canBePlacedOnFieldAt() {
        GameField gameField = new GameField();

        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.NONE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        ResourceHolder test = new ResourceHolder(side);

        Side starter_card = new SideFrontStarter(0, side, test);
        Side normal_card = new SideBack(1, side, test, Symbol.RED);
        Side gold_card = new SideFrontGold(2, side, test, 2, Symbol.RED, new ResourceHolder(), Symbol.BLUE);

        assertTrue(gameField.canBePlacedOnFieldAt(starter_card, new GameFieldPosition(0,0, 1)));
        
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0,0, 2)));

        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1,0, 2)));

        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0,1, 2)));

        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1,1, 2)));

        // assertTrue(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1,-1, 1)));
    }

    @Test
    void countMatches() {
    }

    @Test
    void countCoveredCorners() {
    }

    @Test
    void placeOnFieldAt() {
    }

    @Test
    void getPlacedCards() {
    }
}