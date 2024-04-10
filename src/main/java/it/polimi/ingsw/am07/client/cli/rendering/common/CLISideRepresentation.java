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

package it.polimi.ingsw.am07.client.cli.rendering.common;

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideFrontGold;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import it.polimi.ingsw.am07.model.game.side.SideFrontStarter;
import it.polimi.ingsw.am07.utils.matrix.Matrix;

/**
 * Represents a side of a card in the CLI-rendered game field.
 */
public abstract class CLISideRepresentation {

    /**
     * Characters used to draw the side representation.
     */
    private static final char LEFT_ANGLE = '/';
    private static final char RIGHT_ANGLE = '\\';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';

    protected final Matrix<CLIGameSymbol> representation;

    /**
     * Constructs a CLISideRepresentation.
     */
    protected CLISideRepresentation() {
        representation = new Matrix<>(width(), height(), new CLIGameSymbol(' '));
    }

    /**
     * Getter for the width of the representation.
     *
     * @return the width of the representation
     */
    public abstract int width();

    /**
     * Getter for the height of the representation.
     *
     * @return the height of the representation
     */
    public abstract int height();

    /**
     * Getter for the amount of overlap of the representation.
     *
     * @return the amount of overlap of the representation
     */
    public abstract int overlapAmount();

    /**
     * Constructs a CLISideRepresentation for the specified Side
     *
     * @param side the side to represent
     */
    public abstract CLISideRepresentation forSide(Side side);

    /**
     * Draws the borders of the side representation.
     *
     * @param cardColor the color of the card
     * @param side      the side to draw the borders for
     */
    protected void drawBorders(CLIColor cardColor, Side side) {
        // Draw the horizontal borders
        CLIGameSymbol horizontalBorder = new CLIGameSymbol(HORIZONTAL, cardColor);
        for (int i = 1; i < width() - 1; i++) {
            representation.set(i, 0, horizontalBorder);
            representation.set(i, height() - 1, horizontalBorder);
        }

        // Draw the vertical borders
        CLIGameSymbol verticalBorder = new CLIGameSymbol(VERTICAL, cardColor);
        for (int i = 1; i < height() - 1; i++) {
            representation.set(0, i, verticalBorder);
            representation.set(width() - 1, i, verticalBorder);
        }

        // Draw the angles
        representation.set(0, 0, new CLIGameSymbol(LEFT_ANGLE, cardColor));
        representation.set(width() - 1, 0, new CLIGameSymbol(RIGHT_ANGLE, cardColor));
        representation.set(0, height() - 1, new CLIGameSymbol(RIGHT_ANGLE, cardColor));
        representation.set(width() - 1, height() - 1, new CLIGameSymbol(LEFT_ANGLE, cardColor));
    }

    protected CLIGameSymbol getCenterSymbol(CLIColor cardColor, Side side) {
        switch (side) {
            case SideFrontGold ignored -> {
                return new CLIGameSymbol(CLIGameSymbol.CARD_GOLD, cardColor);
            }
            case SideFrontRes ignored -> {
                return new CLIGameSymbol(CLIGameSymbol.CARD_RES, cardColor);
            }
            case SideFrontStarter ignored -> {
                return new CLIGameSymbol(CLIGameSymbol.CARD_STARTER, cardColor);
            }
            default -> {
                return new CLIGameSymbol(' ', cardColor);
            }
        }
    }

    /**
     * Getter for the side representation.
     *
     * @return the side representation
     */
    public Matrix<CLIGameSymbol> getMatrix() {
        return representation;
    }

    /**
     * Factory for CLISideRepresentation.
     */
    public static class Factory {

        private final Side side;

        /**
         * Constructs a CLISideRepresentation factory.
         */
        public Factory(Side side) {
            this.side = side;
        }

        /**
         * Constructs a large CLISideRepresentation.
         *
         * @return the large CLISideRepresentation
         */
        public CLISideRepresentation large() {
            return new CLILargeSideRepresentation().forSide(side);
        }

        /**
         * Constructs a small CLISideRepresentation.
         *
         * @return the small CLISideRepresentation
         */
        public CLISideRepresentation small() {
            return new CLISmallSideRepresentation().forSide(side);
        }

    }

}
