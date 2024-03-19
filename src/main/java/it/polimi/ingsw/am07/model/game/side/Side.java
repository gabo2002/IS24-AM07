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

import java.util.Optional;

public sealed abstract class Side permits SideFront, SideBack {

    private final int id;

    private final Symbol color;

    private final SideFieldRepresentation fieldRepresentation;

    private final ResourceHolder resources;

    protected Side(int id, SideFieldRepresentation fieldRepresentation, ResourceHolder resources, Symbol color) {
        this.id = id;
        this.fieldRepresentation = fieldRepresentation;
        this.resources = resources;
        this.color = color;
    }

    public int id() {
        return id;
    }

    public Symbol color() {
        return color;
    }

    public SideFieldRepresentation fieldRepresentation() {
        return fieldRepresentation;
    }

    public ResourceHolder resources() {
        return resources;
    }

    public int calculateAssociatedScore(ResourceHolder resources, int coveredCorners) {
        return 0;
    }

    public Optional<ResourceHolder> requirements() {
        return Optional.empty();
    }

}
