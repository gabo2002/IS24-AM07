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

package it.polimi.ingsw.am07.cli.rendering.common;

import it.polimi.ingsw.am07.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.model.game.side.Side;

/**
 * Represents a side of a card in the CLI-rendered game field.
 */
public class CLISmallSideRepresentation extends CLISideRepresentation {

    /**
     * Width, height and amount of overlap of the side representation.
     */
    public static final int WIDTH = 11;
    public static final int HEIGHT = 7;
    public static final int CORNER_OVERLAP = 3;

    /**
     * Constructs a CLISmallSideRepresentation.
     */
    public CLISmallSideRepresentation() {
        super();
    }

    /**
     * Getter for the width of the representation.
     *
     * @return the width of the representation
     */
    @Override
    public int height() {
        return HEIGHT;
    }

    /**
     * Getter for the height of the representation.
     *
     * @return the height of the representation
     */
    @Override
    public int width() {
        return WIDTH;
    }

    /**
     * Getter for the amount of overlap of the representation.
     *
     * @return the amount of overlap of the representation
     */
    @Override
    public int overlapAmount() {
        return CORNER_OVERLAP;
    }

    /**
     * Constructs a CLISideRepresentation for the specified Side
     *
     * @param side the side to represent
     */
    @Override
    public CLISideRepresentation forSide(Side side) {
        CLIColor cardColor = CLIColor.fromSymbol(side.color());

        drawBorders(cardColor, side);

        // Draw the corners
        representation.set(1, 1, new CLIGameSymbol(side.fieldRepresentation().corners().get(0, 0), cardColor));
        representation.set(WIDTH - 2, 1, new CLIGameSymbol(side.fieldRepresentation().corners().get(1, 0), cardColor));
        representation.set(1, HEIGHT - 2, new CLIGameSymbol(side.fieldRepresentation().corners().get(0, 1), cardColor));
        representation.set(WIDTH - 2, HEIGHT - 2, new CLIGameSymbol(side.fieldRepresentation().corners().get(1, 1), cardColor));

        // Draw the center
        representation.set(WIDTH / 2, HEIGHT / 2, new CLIGameSymbol(side.color(), cardColor));

        // Draw the card type
        CLIGameSymbol cardSymbol = getCenterSymbol(cardColor, side);
        representation.set(WIDTH / 2, HEIGHT - 2, cardSymbol);

        // Draw the score, if any
        if (side.getAssociatedScore() > 0) {
            char scoreChar = String.valueOf(side.getAssociatedScore()).charAt(0);
            representation.set(WIDTH / 2, 1, new CLIGameSymbol(scoreChar, cardColor));
        }

        return this;
    }

}
