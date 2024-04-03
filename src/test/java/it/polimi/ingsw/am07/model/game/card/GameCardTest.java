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

package it.polimi.ingsw.am07.model.game.card;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFieldRepresentation;
import it.polimi.ingsw.am07.model.game.side.SideFront;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameCardTest {

    @Test
    void id() {
        Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.RED);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
        ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

        SideFront sideFront = new SideFrontRes(1, sideFieldRepresentation, resources, Symbol.BLUE);
        SideBack sideBack = new SideBack(1, sideFieldRepresentation, resources, Symbol.BLUE);

        GameCard gameCard = new GameCard(sideFront, sideBack);

        assertEquals(1, gameCard.id());

        SideBack sideBack_2 = new SideBack(2, sideFieldRepresentation, resources, Symbol.BLUE);

        assertThrows(IllegalArgumentException.class, () -> new GameCard(sideFront, sideBack_2));
    }

    @Test
    void testEquals() {
        Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.RED);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
        ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

        SideFront sideFront_1 = new SideFrontRes(1, sideFieldRepresentation, resources, Symbol.BLUE);
        SideBack sideBack_1 = new SideBack(1, sideFieldRepresentation, resources, Symbol.BLUE);

        GameCard gameCard_1 = new GameCard(sideFront_1, sideBack_1);

        SideFront sideFront_2 = new SideFrontRes(2, sideFieldRepresentation, resources, Symbol.BLUE);
        SideBack sideBack_2 = new SideBack(2, sideFieldRepresentation, resources, Symbol.BLUE);

        GameCard gameCard_2 = new GameCard(sideFront_2, sideBack_2);

        GameCard gameCard_3 = new GameCard(sideFront_1, sideBack_1);

        assertEquals(gameCard_1, gameCard_1);
        assertNotEquals(gameCard_1, gameCard_2);
        assertEquals(gameCard_1, gameCard_3);
    }

    @Test
    void front() {
        Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.RED);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
        ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

        SideFront sideFront = new SideFrontRes(1, sideFieldRepresentation, resources, Symbol.BLUE);
        SideBack sideBack = new SideBack(1, sideFieldRepresentation, resources, Symbol.BLUE);

        GameCard gameCard = new GameCard(sideFront, sideBack);

        assertEquals(sideFront, gameCard.front());
    }

    @Test
    void back() {
        Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.RED);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
        ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

        SideFront sideFront = new SideFrontRes(1, sideFieldRepresentation, resources, Symbol.BLUE);
        SideBack sideBack = new SideBack(1, sideFieldRepresentation, resources, Symbol.BLUE);

        GameCard gameCard = new GameCard(sideFront, sideBack);

        assertEquals(sideBack, gameCard.back());
    }

}