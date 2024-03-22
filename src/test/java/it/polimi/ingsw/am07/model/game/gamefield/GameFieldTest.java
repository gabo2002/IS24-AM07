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

import java.util.HashMap;

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

        // Should always start with starter card
        assertTrue(gameField.canBePlacedOnFieldAt(starter_card, new GameFieldPosition(0,0, 1)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0,0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1,0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0,1, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1,1, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(-1,-1, 1)));

        // Place the starter card
        gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0,0, 1));

        // Checking after the starter card
        assertFalse(gameField.canBePlacedOnFieldAt(starter_card, new GameFieldPosition(0,0, 1)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0,0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1,0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0,1, 2)));

        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1,1, 2)));
        assertTrue(gameField.canBePlacedOnFieldAt(gold_card, new GameFieldPosition(-1,-1, 1)));
        assertTrue(gameField.canBePlacedOnFieldAt(gold_card, new GameFieldPosition(1,-1, 1)));
        assertTrue(gameField.canBePlacedOnFieldAt(gold_card, new GameFieldPosition(-1,1, 1)));
    }

    @Test
    void countMatches() {
        GameField gameField = new GameField();

        //starter card
        Matrix<Symbol> corners = new Matrix<>(2, 2);
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                corners.set(i, j, Symbol.BLANK);
            }
        }

        SideFieldRepresentation side = new SideFieldRepresentation(corners);
        ResourceHolder test = new ResourceHolder(side);
        Side starterCard = new SideFrontStarter(0,side,test);

        gameField.placeOnFieldAt(starterCard, new GameFieldPosition(0,0,1));

        //Resource holder for the cards
        test = new ResourceHolder();
        test.incrementResource(Symbol.BLUE);
        test.incrementResource(Symbol.SCROLL);

        //Generate 3 cards with different symbols
        corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.SCROLL);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.NONE);
        corners.set(1, 1, Symbol.BLANK);
        side = new SideFieldRepresentation(corners);

        Side firstCard = new SideBack(1,side,test,Symbol.RED);

        assertTrue(gameField.canBePlacedOnFieldAt(firstCard, new GameFieldPosition(-1,-1,1)));
        gameField.placeOnFieldAt(firstCard, new GameFieldPosition(-1,-1,1));

        corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.SCROLL);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.NONE);
        corners.set(1, 1, Symbol.BLANK);

        side = new SideFieldRepresentation(corners);
        Side secondCard = new SideBack(2,side,test,Symbol.RED);

        assertTrue(gameField.canBePlacedOnFieldAt(secondCard, new GameFieldPosition(1,1,2)));
        gameField.placeOnFieldAt(secondCard, new GameFieldPosition(1,1,2));

        Matrix<Symbol> pattern = new Matrix<>(3, 3, Symbol.EMPTY);
        for(int i = 0; i < 3; i++) {
            pattern.set(i, i, Symbol.RED);
        }

        assertEquals(0, gameField.countMatches(new GameFieldPattern(pattern))); //starter card shoult not be counted

        pattern = new Matrix<>(3, 3, Symbol.EMPTY);
        pattern.set(0,0,Symbol.RED);
        pattern.set(2,2,Symbol.RED);
        assertEquals(1, gameField.countMatches(new GameFieldPattern(pattern)));
    }

    @Test
    void countCoveredCorners() {
        GameField gameField = new GameField();

        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.NONE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        ResourceHolder test = new ResourceHolder(side);

        Side starter_card = new SideFrontStarter(0, side, test);
        Side normal_card = new SideFrontStarter(1, side, test);

        gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0,0,1));
        assertEquals(4, gameField.countCoveredCorners(new GameFieldPosition(0,0,1)));


        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(-1,-1,1));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(0,-2,1));

        assertEquals(4, gameField.countCoveredCorners(new GameFieldPosition(-1,-1,1)));
        assertEquals(4, gameField.countCoveredCorners(new GameFieldPosition(0,-2,1)));

        assertEquals(3, gameField.countCoveredCorners(new GameFieldPosition(-1,0,1)));
        assertEquals(3, gameField.countCoveredCorners(new GameFieldPosition(-1,-2,1)));

        assertEquals(2,gameField.countCoveredCorners(new GameFieldPosition(-2,-1,1)));
        assertEquals(2,gameField.countCoveredCorners(new GameFieldPosition(1,0,1)));

        assertEquals(1,gameField.countCoveredCorners(new GameFieldPosition(-2,-2,1)));
        assertEquals(1, gameField.countCoveredCorners(new GameFieldPosition(-2,0,1)));

        assertEquals(0, gameField.countCoveredCorners(new GameFieldPosition(-3,-1,1)));
        assertEquals(0, gameField.countCoveredCorners(new GameFieldPosition(2,1,1)));





    }

    @Test
    void placeOnFieldAt() {
        GameField gameField = new GameField();

        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.NONE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        ResourceHolder test = new ResourceHolder(side);

        Side starter_card = new SideFrontStarter(0, side, test);

        assertEquals(test, gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0,0,1)));
        assertEquals(test, gameField.placeOnFieldAt(starter_card, new GameFieldPosition(1,-2,1)));
        assertEquals(test, gameField.placeOnFieldAt(starter_card, new GameFieldPosition(2,2,1)));
        assertEquals(test, gameField.placeOnFieldAt(starter_card, new GameFieldPosition(3,-1,1)));
        assertEquals(test, gameField.placeOnFieldAt(starter_card, new GameFieldPosition(-3,4,1)));

    }

    @Test
    void getPlacedCards() {
        GameField gameField = new GameField();

        Matrix<Symbol> corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.RED);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.GREEN);
        corners.set(1, 1, Symbol.NONE);
        SideFieldRepresentation side = new SideFieldRepresentation(corners);

        ResourceHolder test = new ResourceHolder(side);

        Side starter_card = new SideFrontStarter(0, side, test);
        Side normal_card = new SideFrontStarter(1, side, test);

        gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0,0,0));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(-1,-1,1));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(1,-1,2));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(-1,1,3));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(1,1,4));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(0,-2,5));


        HashMap<Side, GameFieldPosition> placedCards = new HashMap<>();

        placedCards.put(starter_card,new GameFieldPosition(0,0,0));
        placedCards.put(normal_card,new GameFieldPosition(-1,-1,1));
        placedCards.put(normal_card,new GameFieldPosition(1,-1,2));
        placedCards.put(normal_card,new GameFieldPosition(-1,1,3));
        placedCards.put(normal_card,new GameFieldPosition(1,1,4));
        placedCards.put(normal_card, new GameFieldPosition(0,-2,5));


        assertEquals(placedCards, gameField.getPlacedCards());
    }
}