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

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.PatternObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.ResourceObjectiveCard;
import it.polimi.ingsw.am07.utils.matrix.Matrix;

import java.util.Map;

public abstract class CLIObjectiveCardRepresentation {
    /**
     * Characters used to draw the side representation.
     */
    private static final char LEFT_ANGLE = '/';
    private static final char RIGHT_ANGLE = '\\';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';

    protected final Matrix<CLIGameSymbol> representation;

    /**
     * Constructs a CLIObjectiveCardRepresentation.
     */
    protected CLIObjectiveCardRepresentation() {
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
     * Constructs a CLISideRepresentation for the specified Card
     *
     * @param card the Card to represent
     */
    public abstract CLIObjectiveCardRepresentation forCard(ObjectiveCard card);

    /**
     * Draws the borders of the side representation.
     *
     * @param card the ObjectiveCard to draw the borders for
     */
    protected void drawBorders(ObjectiveCard card) {
        // Draw the horizontal borders
        CLIGameSymbol horizontalBorder = new CLIGameSymbol(HORIZONTAL, CLIColor.YELLOW);
        for (int i = 1; i < width() - 1; i++) {
            representation.set(i, 0, horizontalBorder);
            representation.set(i, height() - 1, horizontalBorder);
        }

        // Draw the vertical borders
        CLIGameSymbol verticalBorder = new CLIGameSymbol(VERTICAL, CLIColor.YELLOW);
        for (int i = 1; i < height() - 1; i++) {
            representation.set(0, i, verticalBorder);
            representation.set(width() - 1, i, verticalBorder);
        }

        // Draw the associated Score
        representation.set(width() - 2, 1, new CLIGameSymbol(Character.forDigit(card.getAssociatedScore(), 10), CLIColor.YELLOW));

        switch (card) {
            case PatternObjectiveCard patternCard:
                for (int i = 0; i < patternCard.getPattern().pattern().getWidth(); i++) {
                    for (int j = 0; j < patternCard.getPattern().pattern().getHeight(); j++) {
                        Symbol symbol = patternCard.getPattern().pattern().get(i, j);

                        if (symbol == Symbol.EMPTY)
                            continue;

                        representation.set(j + 1 + (height() / 2 - 1), i + 2 + (width() / 2 - 5), new CLIGameSymbol(symbol, CLIColor.fromSymbol(symbol)));
                    }
                }
                break;
            case ResourceObjectiveCard resource:
                Map<Symbol, Integer> resources = resource.getRequirements().getResources();

                int i = height() / 2;
                for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
                    Symbol symbol = entry.getKey();
                    Integer amount = entry.getValue();

                    for (int j = 0; j < amount; j++) {
                        representation.set(1 + i, width() / 2, new CLIGameSymbol(symbol, CLIColor.fromSymbol(symbol)));
                        i++;
                    }
                }
                break;

        }

        //set the corners
        representation.set(0, 0, new CLIGameSymbol(LEFT_ANGLE, CLIColor.YELLOW));
        representation.set(width() - 1, 0, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.YELLOW));
        representation.set(0, height() - 1, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.YELLOW));
        representation.set(width() - 1, height() - 1, new CLIGameSymbol(LEFT_ANGLE, CLIColor.YELLOW));
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
     * Factory for CLIObjectiveCardRepresentation.
     */
    public static class Factory {

        private final ObjectiveCard card;

        /**
         * Constructs a CLIObjectiveCardRepresentation factory.
         */
        public Factory(ObjectiveCard card) {
            this.card = card;
        }

        /**
         * Constructs a large CLIObjectiveCardRepresentation.
         *
         * @return the large CLIObjectiveCardRepresentation
         */
        public CLIObjectiveCardRepresentation large() {
            return new CLILargeObjectiveCardRepresentation().forCard(card);
        }

        /**
         * Constructs a small CLIObjectiveCardRepresentation.
         *
         * @return the small CLIObjectiveCardRepresentation
         */
        public CLIObjectiveCardRepresentation small() {
            return new CLISmallObjectiveCardRepresentation().forCard(card);
        }

    }

}
