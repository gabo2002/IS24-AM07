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

package it.polimi.ingsw.am07.client.cli.rendering.common.objectiveCard;

import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;

/**
 * Represents the CLI (Command Line Interface) representation of a large objective card.
 * <p>
 * This class extends CLIObjectiveCardRepresentation.
 * </p>
 */
public class CLILargeObjectiveCardRepresentation extends CLIObjectiveCardRepresentation {

    /** The width of the large objective card representation. */
    public static final int WIDTH = 13;

    /** The height of the large objective card representation. */
    public static final int HEIGHT = 9;

    /** The amount of overlap for the corners of the card. */
    public static final int CORNER_OVERLAP = 1;

    /**
     * Constructs a CLILargeObjectiveCardRepresentation.
     */
    public CLILargeObjectiveCardRepresentation() {
        super();
    }

    /**
     * Returns the width of the large objective card representation.
     *
     * @return the width of the large objective card.
     */
    @Override
    public int width() {
        return WIDTH;
    }

    /**
     * Returns the height of the large objective card representation.
     *
     * @return the height of the large objective card.
     */
    @Override
    public int height() {
        return HEIGHT;
    }

    /**
     * Returns the amount of overlap for the corners of the card.
     *
     * @return the amount of corner overlap.
     */
    @Override
    public int overlapAmount() {
        return CORNER_OVERLAP;
    }

    /**
     * Generates a CLI representation for the given ObjectiveCard by drawing its borders.
     *
     * @param card the ObjectiveCard to be represented.
     * @return the CLIObjectiveCardRepresentation of the specified ObjectiveCard.
     */
    @Override
    public CLIObjectiveCardRepresentation forCard(ObjectiveCard card) {
        drawBorders(card);
        return this;
    }
}
