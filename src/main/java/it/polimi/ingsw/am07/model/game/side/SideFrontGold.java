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

/**
 * Represents a gold side of a card.
 */
public final class SideFrontGold extends SideFront {

    /**
     * The symbol that multiplies the score.
     */
    private final Symbol multiplier;

    /**
     * The resource requirements needed to place this side on the field.
     */
    private final ResourceHolder requirements;

    /**
     * Constructs a new SideFrontGold object with the specified parameters.
     *
     * @param id                  The unique identifier for the side.
     * @param fieldRepresentation The representation of the side's field.
     * @param resources           The resources associated with the side.
     * @param associatedScore     The score associated with the side.
     * @param multiplier          The symbol that multiplies the score.
     * @param requirements        The resource requirements needed to place this side on the field.
     * @param color               The color of the side.
     */
    public SideFrontGold(int id,
                         SideFieldRepresentation fieldRepresentation,
                         ResourceHolder resources,
                         int associatedScore,
                         Symbol multiplier,
                         ResourceHolder requirements,
                         Symbol color
    ) {
        super(id, fieldRepresentation, resources, color, associatedScore);
        this.multiplier = multiplier;
        this.requirements = requirements;
    }

    /**
     * Calculates the score associated with this side.
     *
     * @param resources      The current resources of the player.
     * @param coveredCorners The number of corners covered by the card upon being placed.
     * @return the amount to be added to the player's score.
     */
    @Override
    public int calculateAssociatedScore(ResourceHolder resources, int coveredCorners) {
        if (multiplier == Symbol.NONE) {
            return associatedScore;
        } else if (multiplier == Symbol.CORNER) {
            return associatedScore * coveredCorners;
        } else {
            return associatedScore * resources.countOf(multiplier);
        }
    }

    /**
     * Returns the resource requirements needed to place this side on the field.
     */
    @Override
    public Optional<ResourceHolder> requirements() {
        return Optional.ofNullable(requirements);
    }

    /**
     * Getter for the multiplier symbol.
     *
     * @return the multiplier symbol
     */
    public Symbol getMultiplier() {
        return multiplier;
    }

}
