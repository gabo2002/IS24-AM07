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
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFieldRepresentation;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import javax.swing.text.Position;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {

    @Test
    void canBePlacedOnFieldAt() {

    }

    @Test
    void countMatches() {
    }

    @Test
    void countCoveredCorners() {

            var gameField = new GameField();
            Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.BLANK);
            SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
            ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

            //Carta iniziale in posizione 0,0
            var pos1 = new GameFieldPosition(0,0,0);
            var sideBack = new SideBack(1, sideFieldRepresentation,resources, Symbol.GREEN);
            gameField.placeOnFieldAt(sideBack, pos1);

            var sideBack2 = new SideBack(2, sideFieldRepresentation,resources, Symbol.RED);
            var pos2 = new GameFieldPosition(  1,1,1);

            //aggiungo una nuova carta in posizione 1,1
            assertEquals(1, gameField.countCoveredCorners(pos2));
            gameField.placeOnFieldAt(sideBack2, pos2);

            var sideBack3 = new SideBack(3, sideFieldRepresentation,resources, Symbol.RED);
            var pos3 = new GameFieldPosition(  2,0,2);

            assertEquals(1, gameField.countCoveredCorners(pos3));
            gameField.placeOnFieldAt(sideBack3, pos3);

            var sideFront1 = new SideFrontRes(4, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos4 = new GameFieldPosition(  2,2,3);

            assertEquals(1, gameField.countCoveredCorners(pos4));
            gameField.placeOnFieldAt(sideFront1, pos4);

            var sideFront2 = new SideFrontRes(5, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos5 = new GameFieldPosition(  3,1,4);

            //aggiungo una nuova carta in posizione 3,1, che dovrebbe coprire 2 angoli
            assertEquals(2, gameField.countCoveredCorners(pos5));
            gameField.placeOnFieldAt(sideFront2, pos5);

            var sideFront3 = new SideFrontRes(6, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos6 = new GameFieldPosition(  3,3,5);

            assertEquals(1, gameField.countCoveredCorners(pos6));
            gameField.placeOnFieldAt(sideFront3, pos6);

            var sideFront4 = new SideFrontRes(7, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos7 = new GameFieldPosition(  4,4,6);

            assertEquals(1, gameField.countCoveredCorners(pos7));
            gameField.placeOnFieldAt(sideFront4, pos7);

            var sideFront5 = new SideFrontRes(8, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos8 = new GameFieldPosition(  4,0,7);

            assertEquals(1, gameField.countCoveredCorners(pos8));
            gameField.placeOnFieldAt(sideFront5, pos8);

            var sideFront6 = new SideFrontRes(9, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos9 = new GameFieldPosition(  5,1,8);

            assertEquals(1, gameField.countCoveredCorners(pos9));
            gameField.placeOnFieldAt(sideFront6, pos9);

            var sideFront7 = new SideFrontRes(10, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos10 = new GameFieldPosition(  5,3,9);

            assertEquals(1, gameField.countCoveredCorners(pos10));
            gameField.placeOnFieldAt(sideFront7, pos10);

            var sideFront8 = new SideFrontRes(11, sideFieldRepresentation,resources, Symbol.BLUE);
            var pos11 = new GameFieldPosition(  4,2,10);

            assertEquals(4, gameField.countCoveredCorners(pos11));
            gameField.placeOnFieldAt(sideFront8, pos11);
    }

    @Test
    void placeOnFieldAt() {
    }

    @Test
    void getPlacedCards() {
    }
}