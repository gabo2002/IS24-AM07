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

import java.io.Serializable;
import java.util.Optional;

public sealed abstract class Side implements Serializable permits SideFront, SideBack {

    private final int id;

    private final Symbol color;

    private final SideFieldRepresentation fieldRepresentation;

    private final ResourceHolder resources;

    /**
     * Constructs a new side with the specified parameters.
     *
     * @param id                  The unique identifier for the side.
     * @param fieldRepresentation The representation of the side's field.
     * @param resources           The resources associated with the side.
     * @param color               The color of the side.
     */
    protected Side(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources, Symbol color) {
        this.id = id;
        this.fieldRepresentation = fieldRepresentation;
        this.resources = resources;
        this.color = color;
    }

    /**
     * Returns the unique identifier of the side.
     *
     * @return The unique identifier of the side.
     */
    public int id() {
        return id;
    }

    /**
     * Returns the color of the side.
     *
     * @return The color of the side.
     */
    public Symbol color() {
        return color;
    }

    /**
     * Returns the field representation of the side.
     *
     * @return The field representation of the side.
     */
    public SideFieldRepresentation fieldRepresentation() {
        return fieldRepresentation;
    }

    /**
     * Returns the resources associated with the side.
     *
     * @return The resources associated with the side.
     */
    public ResourceHolder resources() {
        return resources;
    }

    /**
     * Calculates the associated score for the side based on the resources and covered corners.
     *
     * @param resources      The resources available to the side.
     * @param coveredCorners The number of covered corners.
     * @return The associated score for the side.
     */
    public int calculateAssociatedScore(ResourceHolder resources, int coveredCorners) {
        return 0;
    }

    /**
     * Returns an optional containing the requirements of the side, if any.
     *
     * @return An optional containing the requirements of the side, if any.
     */
    public Optional<ResourceHolder> requirements() {
        return Optional.empty();
    }

    /**
     * Checks if this side is equal to another object.
     *
     * @param o The object to compare with this side.
     * @return True if the given object is equal to this side, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Side s)) {
            return false;
        }
        return id == s.id();
    }

}
