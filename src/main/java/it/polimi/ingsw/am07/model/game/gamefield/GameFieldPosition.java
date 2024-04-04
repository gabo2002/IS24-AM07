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

/**
 * Represents a position within the game field.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @param z the z-index
 */
public record GameFieldPosition(
        int x, int y, int z
) {

    /**
     * Constructs a new GameFieldPosition with the specified parameters and no z-index.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public GameFieldPosition(int x, int y) {
        this(x, y, -1);
    }

    /**
     * Verifies if the position is valid within the game field position system.
     *
     * @return true if the position is valid, false otherwise
     */
    public boolean isValid() {
        return (x + y) % 2 == 0;
    }

}
