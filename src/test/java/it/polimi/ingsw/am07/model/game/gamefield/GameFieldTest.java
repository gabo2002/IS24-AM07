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

import it.polimi.ingsw.am07.exceptions.IllegalPlacementException;
import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.side.*;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

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
        assertTrue(gameField.canBePlacedOnFieldAt(starter_card, new GameFieldPosition(0, 0, 1)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0, 0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1, 0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0, 1, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1, 1, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(-1, -1, 1)));

        // Place the starter card
        gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0, 0, 1));

        // Checking after the starter card
        assertFalse(gameField.canBePlacedOnFieldAt(starter_card, new GameFieldPosition(0, 0, 1)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0, 0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1, 0, 2)));
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(0, 1, 2)));

        //non posizionabile su NONE (1,1)
        assertFalse(gameField.canBePlacedOnFieldAt(normal_card, new GameFieldPosition(1, 1, 2)));

        assertTrue(gameField.canBePlacedOnFieldAt(gold_card, new GameFieldPosition(-1, -1, 1)));
        assertTrue(gameField.canBePlacedOnFieldAt(gold_card, new GameFieldPosition(1, -1, 1)));
        assertTrue(gameField.canBePlacedOnFieldAt(gold_card, new GameFieldPosition(-1, 1, 1)));
    }

    @Test
    void countMatches() {
        GameField gameField = new GameField();

        //starter card
        Matrix<Symbol> corners = new Matrix<>(2, 2);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                corners.set(i, j, Symbol.BLANK);
            }
        }

        SideFieldRepresentation side = new SideFieldRepresentation(corners);
        ResourceHolder test = new ResourceHolder(side);
        Side starterCard = new SideFrontStarter(0, side, test);

        gameField.placeOnFieldAt(starterCard, new GameFieldPosition(0, 0, 1));

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

        Side firstCard = new SideBack(1, side, test, Symbol.RED);

        assertTrue(gameField.canBePlacedOnFieldAt(firstCard, new GameFieldPosition(-1, -1, 1)));
        gameField.placeOnFieldAt(firstCard, new GameFieldPosition(-1, -1, 1));

        corners = new Matrix<>(2, 2);
        corners.set(0, 0, Symbol.SCROLL);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(0, 1, Symbol.NONE);
        corners.set(1, 1, Symbol.BLANK);

        side = new SideFieldRepresentation(corners);
        Side secondCard = new SideBack(2, side, test, Symbol.RED);

        assertTrue(gameField.canBePlacedOnFieldAt(secondCard, new GameFieldPosition(1, 1, 2)));
        gameField.placeOnFieldAt(secondCard, new GameFieldPosition(1, 1, 2));

        Matrix<Symbol> pattern = new Matrix<>(3, 3, Symbol.EMPTY);
        for (int i = 0; i < 3; i++) {
            pattern.set(i, i, Symbol.RED);
        }

        assertEquals(0, gameField.countMatches(new GameFieldPattern(pattern))); //starter card shoult not be counted

        pattern = new Matrix<>(3, 3, Symbol.EMPTY);
        pattern.set(0, 0, Symbol.RED);
        pattern.set(2, 2, Symbol.RED);
        assertEquals(1, gameField.countMatches(new GameFieldPattern(pattern)));
    }

    @Test
    void countCoveredCorners() {
        var gameField = new GameField();
        Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.BLANK);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
        ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

        //Carta iniziale in posizione 0,0
        var pos1 = new GameFieldPosition(0, 0, 0);
        var sideBack = new SideBack(1, sideFieldRepresentation, resources, Symbol.GREEN);
        gameField.placeOnFieldAt(sideBack, pos1);

        var sideBack2 = new SideBack(2, sideFieldRepresentation, resources, Symbol.RED);
        var pos2 = new GameFieldPosition(1, 1, 1);

        //aggiungo una nuova carta in posizione 1,1
        assertEquals(1, gameField.countCoveredCorners(pos2));
        gameField.placeOnFieldAt(sideBack2, pos2);

        var sideBack3 = new SideBack(3, sideFieldRepresentation, resources, Symbol.RED);
        var pos3 = new GameFieldPosition(2, 0, 2);

        assertEquals(1, gameField.countCoveredCorners(pos3));
        gameField.placeOnFieldAt(sideBack3, pos3);

        var sideFront1 = new SideFrontRes(4, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos4 = new GameFieldPosition(2, 2, 3);

        assertEquals(1, gameField.countCoveredCorners(pos4));
        gameField.placeOnFieldAt(sideFront1, pos4);

        var sideFront2 = new SideFrontRes(5, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos5 = new GameFieldPosition(3, 1, 4);

        //aggiungo una nuova carta in posizione 3,1, che dovrebbe coprire 2 angoli
        assertEquals(2, gameField.countCoveredCorners(pos5));
        gameField.placeOnFieldAt(sideFront2, pos5);

        var sideFront3 = new SideFrontRes(6, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos6 = new GameFieldPosition(3, 3, 5);

        assertEquals(1, gameField.countCoveredCorners(pos6));
        gameField.placeOnFieldAt(sideFront3, pos6);

        var sideFront4 = new SideFrontRes(7, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos7 = new GameFieldPosition(4, 4, 6);

        assertEquals(1, gameField.countCoveredCorners(pos7));
        gameField.placeOnFieldAt(sideFront4, pos7);

        var sideFront5 = new SideFrontRes(8, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos8 = new GameFieldPosition(4, 0, 7);

        assertEquals(1, gameField.countCoveredCorners(pos8));
        gameField.placeOnFieldAt(sideFront5, pos8);

        var sideFront6 = new SideFrontRes(9, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos9 = new GameFieldPosition(5, 1, 8);

        assertEquals(1, gameField.countCoveredCorners(pos9));
        gameField.placeOnFieldAt(sideFront6, pos9);

        var sideFront7 = new SideFrontRes(10, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos10 = new GameFieldPosition(5, 3, 9);

        assertEquals(1, gameField.countCoveredCorners(pos10));
        gameField.placeOnFieldAt(sideFront7, pos10);

        var sideFront8 = new SideFrontRes(11, sideFieldRepresentation, resources, Symbol.BLUE);
        var pos11 = new GameFieldPosition(4, 2, 10);

        assertEquals(4, gameField.countCoveredCorners(pos11));
        gameField.placeOnFieldAt(sideFront8, pos11);

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
        Side normal_card = new SideFrontStarter(1, side, test);

        assertEquals(test, gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0, 0, 1)));
        gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0, 0, 1));

        ResourceHolder resources2 = new ResourceHolder();

        resources2.decrementResource(Symbol.RED);
        resources2.add(test);

        assertEquals(resources2, gameField.placeOnFieldAt(normal_card, new GameFieldPosition(-1, -1, 2)));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(-1, -1, 2));

        ResourceHolder resources3 = new ResourceHolder();

        resources3.decrementResource(Symbol.BLUE);
        resources3.add(test);

        assertEquals(resources3, gameField.placeOnFieldAt(normal_card, new GameFieldPosition(1, -1, 3)));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(1, -1, 3));
        ResourceHolder resources4 = new ResourceHolder();

        resources4.decrementResource(Symbol.BLUE);
        resources4.decrementResource(Symbol.RED);
        resources4.add(test);

        assertEquals(resources4, gameField.placeOnFieldAt(normal_card, new GameFieldPosition(0, -2, 4)));

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

        gameField.placeOnFieldAt(starter_card, new GameFieldPosition(0, 0, 0));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(-1, -1, 1));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(1, -1, 2));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(-1, 1, 3));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(1, 1, 4));
        gameField.placeOnFieldAt(normal_card, new GameFieldPosition(0, -2, 5));


        HashMap<GameFieldPosition, Side> placedCards = new HashMap<>();

        placedCards.put(new GameFieldPosition(0, 0, 0), starter_card);
        placedCards.put(new GameFieldPosition(-1, -1, 1), normal_card);
        placedCards.put(new GameFieldPosition(1, -1, 2), normal_card);
        placedCards.put(new GameFieldPosition(-1, 1, 3), normal_card);
        placedCards.put(new GameFieldPosition(1, 1, 4), normal_card);
        placedCards.put(new GameFieldPosition(0, -2, 5), normal_card);


        assertEquals(placedCards, gameField.getPlacedCards());
    }

    @Test
    public void setStarterCardTest() {
        GameField gameField = new GameField();

        List<GameCard> cards = GameResources.getInstance().getStarterCards();

        Side starterCard = cards.get(0).front();

        assertDoesNotThrow(() -> gameField.setStarerCard(starterCard));

        assertThrows(IllegalPlacementException.class, () -> gameField.setStarerCard(starterCard));

    }

}