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

import java.io.Serializable;

/**
 * Represents a position within the game field.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @param z the z-index
 */
public record GameFieldPosition(
        int x, int y, int z
) implements Serializable {

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
     * This method is used by the Moshi library to serialize the position to a string.
     * The string is in the format {x=...,y=...,z=...}.
     */
    public static GameFieldPosition fromString(String string) {
        String[] parts = string.split("[=,]|[=}]");
        return new GameFieldPosition(Integer.parseInt(parts[1]), Integer.parseInt(parts[3]), Integer.parseInt(parts[5]));
    }

    /**
     * Verifies if the position is valid within the game field position system.
     *
     * @return true if the position is valid, false otherwise
     */
    public boolean isValid() {
        return (x + y) % 2 == 0;
    }

    /**
     * Calculates the hashcode of the position.
     * Two positions with the same x and y coordinates will have the same hashcode.
     *
     * @return the hashcode of the position
     */
    @Override
    public int hashCode() {
        return x << 16 | y;
    }

    /**
     * Compares the position with another object.
     *
     * @param obj the object to compare
     * @return true if the object is a GameFieldPosition and has the same x and y coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameFieldPosition that = (GameFieldPosition) obj;
        return x == that.x && y == that.y;
    }

    /**
     * @return a string representation of the position
     */
    @Override
    public String toString() {
        return "GameFieldPosition{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

}
