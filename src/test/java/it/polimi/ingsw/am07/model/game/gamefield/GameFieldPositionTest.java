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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldPositionTest {

    @Test
    void constructor() {
        assertTrue(new GameFieldPosition(0, 0, 0).isValid());
        assertTrue(new GameFieldPosition(0, 2, 0).isValid());
        assertTrue(new GameFieldPosition(2, 0, 0).isValid());
        assertTrue(new GameFieldPosition(2, 2, 0).isValid());

        assertFalse(new GameFieldPosition(1, 0, 0).isValid());
        assertFalse(new GameFieldPosition(0, 1, 0).isValid());
        assertFalse(new GameFieldPosition(2, 1, 0).isValid());
        assertFalse(new GameFieldPosition(1, 2, 0).isValid());
    }

    @Test
    void jsonTest() {
        GameFieldPosition position = new GameFieldPosition(0, 0, 0);
        String json = position.toString();
        GameFieldPosition position2 = GameFieldPosition.fromString(json);
        assertEquals(position, position2);

        assertEquals(0, position2.y());
        assertEquals(0, position2.x());
        assertEquals(0, position2.z());
        assertEquals(json, "GameFieldPosition{x=0, y=0, z=0}");

        position = new GameFieldPosition(0, 2, 0);
        json = position.toString();
        position2 = GameFieldPosition.fromString(json);
        assertEquals(position, position2);

        assertEquals(2, position2.y());
        assertEquals(0, position2.x());
        assertEquals(0, position2.z());
        assertEquals(json, "GameFieldPosition{x=0, y=2, z=0}");

        position = new GameFieldPosition(2343, 4324, 3434);

        json = position.toString();
        position2 = GameFieldPosition.fromString(json);
        assertEquals(position, position2);

        assertEquals(4324, position2.y());
        assertEquals(2343, position2.x());
        assertEquals(3434, position2.z());
        assertEquals(json, "GameFieldPosition{x=2343, y=4324, z=3434}");
    }

}