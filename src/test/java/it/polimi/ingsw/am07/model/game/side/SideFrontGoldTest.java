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

package it.polimi.ingsw.am07.model.game.side;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SideFrontGoldTest {

    @Test
    void calculateAssociatedScore() {
        SideFrontGold side = new SideFrontGold(
                1, null, null, 3, Symbol.NONE, null, null
        );

        assertEquals(3, side.calculateAssociatedScore(null, 0));

        side = new SideFrontGold(
                1, null, null, 3, Symbol.CORNER, null, null
        );

        assertEquals(6, side.calculateAssociatedScore(null, 2));

        side = new SideFrontGold(
                1, null, null, 3, Symbol.FLASK, null, null
        );

        SideFrontGold finalSide = side;
        assertThrows(NullPointerException.class, () -> finalSide.calculateAssociatedScore(null, 0));

        ResourceHolder resources = new ResourceHolder();
        resources.incrementResource(Symbol.FLASK);
        resources.incrementResource(Symbol.FLASK);
        resources.incrementResource(Symbol.FLASK);

        assertEquals(9, side.calculateAssociatedScore(resources, 0));
    }

    @Test
    void requirements() {
        SideFrontGold side = new SideFrontGold(
                1, null, null, 0, null, null, null
        );

        assertFalse(side.requirements().isPresent());

        ResourceHolder requirements = new ResourceHolder();

        side = new SideFrontGold(
                1, null, null, 0, null, requirements, null
        );

        assertTrue(side.requirements().isPresent());
        assertEquals(requirements, side.requirements().get());
    }

}