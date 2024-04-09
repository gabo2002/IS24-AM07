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

package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.*;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void canBePlacedAtTest() {
        Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.STARTER);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
        ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

        SideFront front_starter = new SideFrontStarter(0, sideFieldRepresentation, resources);

        SideFront front_gold = new SideFrontGold(
                1, sideFieldRepresentation, resources, 3, null, null, Symbol.RED);

        GameCard starterCard = new GameCard(front_starter, new SideBack(0, null, null, null));


        Player player = new Player("test", Pawn.GREEN, starterCard, null);

        // cant place without the starter card placed first
        assertFalse(player.canBePlacedAt(front_gold, new GameFieldPosition(0,0,0)));

        assertTrue(player.canBePlacedAt(front_starter, new GameFieldPosition(0,0,0)));
        assertFalse(player.canBePlacedAt(front_starter, new GameFieldPosition(-1,0,0)));
        assertFalse(player.canBePlacedAt(front_starter, new GameFieldPosition(1,0,0)));
        assertFalse(player.canBePlacedAt(front_gold, new GameFieldPosition(1,1,0)));

        assertDoesNotThrow(() ->  player.placeAt(front_starter, new GameFieldPosition(0,0,0)));

        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(1,1,1)));
        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(-1,-1,1)));
        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(1,-1,1)));
        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(-1,1,1)));

        // invalid position
        assertFalse(player.canBePlacedAt(front_gold, new GameFieldPosition(-2,0,1)));
        assertFalse(player.canBePlacedAt(front_gold, new GameFieldPosition(0,-1,1)));
        assertFalse(player.canBePlacedAt(front_gold, new GameFieldPosition(0,1,1)));
        assertFalse(player.canBePlacedAt(front_gold, new GameFieldPosition(1,2,1)));

        // new test card
        Matrix<Symbol> new_corners = new Matrix<>(2, 2, Symbol.RED);
        SideFieldRepresentation side = new SideFieldRepresentation(new_corners);

        ResourceHolder test = new ResourceHolder(side);

        SideFront another_front_gold = new SideFrontGold(2, side, new ResourceHolder(), 2, null, test, Symbol.BLUE);

        // does not have the requirements needed
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(1,1,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(-1,-1,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(1,-1,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(1,1,1)));

        SideFront front_gold2 = new SideFrontGold(3, side, test, 2, null, null, Symbol.BLUE);

        assertTrue(player.canBePlacedAt(front_gold2, new GameFieldPosition(1,1,1)));
        assertDoesNotThrow(() ->  player.placeAt(front_gold2, new GameFieldPosition(1,1,1)));

        // now should have the requirements needed
        assertTrue(player.canBePlacedAt(another_front_gold, new GameFieldPosition(-1,-1,1)));
        assertTrue(player.canBePlacedAt(another_front_gold, new GameFieldPosition(1,-1,1)));
        assertTrue(player.canBePlacedAt(another_front_gold, new GameFieldPosition(-1,1,1)));

        // cant place on top of another card
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(1,1,1)));

        // other invalid positions
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(-2,-1,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(-3,-3,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(0,1,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(0,0,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(1,0,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(2,1,1)));
        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(3,-1,1)));

        assertTrue(player.canBePlacedAt(another_front_gold, new GameFieldPosition(2,2,1)));
        assertTrue(player.canBePlacedAt(another_front_gold, new GameFieldPosition(-1,1,1)));
        assertTrue(player.canBePlacedAt(another_front_gold, new GameFieldPosition(-1,-1,1)));
        assertTrue(player.canBePlacedAt(another_front_gold, new GameFieldPosition(1,-1,1)));

        assertDoesNotThrow(() ->  player.placeAt(another_front_gold, new GameFieldPosition(2,2,1)));

        assertFalse(player.canBePlacedAt(another_front_gold, new GameFieldPosition(2,2,1)));

        assertFalse(player.canBePlacedAt(front_gold, new GameFieldPosition(2,2,2)));

        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(1,-1,1)));
        assertDoesNotThrow(() ->  player.placeAt(front_gold, new GameFieldPosition(1,-1,1)));

        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(2,-2,1)));
        assertDoesNotThrow(() ->  player.placeAt(front_gold, new GameFieldPosition(2,-2,1)));

        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(3,-1,1)));
        assertDoesNotThrow(() ->  player.placeAt(front_gold, new GameFieldPosition(3,-1,1)));

        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(3,1,1)));
        assertDoesNotThrow(() ->  player.placeAt(front_gold, new GameFieldPosition(3,1,1)));

        assertTrue(player.canBePlacedAt(front_gold, new GameFieldPosition(2,0,2)));
        assertDoesNotThrow(() ->  player.placeAt(front_gold, new GameFieldPosition(2,0,2)));

    }
}
