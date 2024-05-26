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

package it.polimi.ingsw.am07.client.cli.rendering.common.side;

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideFrontGold;

/**
 * Represents a side of a card in the CLI-rendered game field.
 */
public class CLILargeSideRepresentation extends CLISideRepresentation {

    /**
     * Width, height and amount of overlap of the side representation.
     */
    public static final int WIDTH = 13;
    public static final int HEIGHT = 9;
    public static final int CORNER_OVERLAP = 4;

    /**
     * Constructs a CLISmallSideRepresentation.
     */
    public CLILargeSideRepresentation() {
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
        CLIGameSymbol topLeft = new CLIGameSymbol(side.fieldRepresentation().corners().get(0, 0), cardColor);
        CLIGameSymbol topRight = new CLIGameSymbol(side.fieldRepresentation().corners().get(1, 0), cardColor);
        CLIGameSymbol bottomLeft = new CLIGameSymbol(side.fieldRepresentation().corners().get(0, 1), cardColor);
        CLIGameSymbol bottomRight = new CLIGameSymbol(side.fieldRepresentation().corners().get(1, 1), cardColor);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                representation.set(1 + i, 1 + j, topLeft);
                representation.set(WIDTH - 3 + i, 1 + j, topRight);
                representation.set(1 + i, HEIGHT - 3 + j, bottomLeft);
                representation.set(WIDTH - 3 + i, HEIGHT - 3 + j, bottomRight);
            }
        }

        // Draw the center
        CLIGameSymbol center = new CLIGameSymbol(side.color(), cardColor);
        representation.set(WIDTH / 2, HEIGHT / 2, center);

        // Draw the card type
        CLIGameSymbol cardSymbol = getCenterSymbol(cardColor, side);
        representation.set(WIDTH / 2, 2, cardSymbol);

        // Draw the requirements
        side.requirements().ifPresent(requirements -> {
            int x = (WIDTH / 2) - 1;
            int y = HEIGHT - 3;

            for (Symbol s : Symbol.values()) {
                for (int i = 0; i < requirements.countOf(s); i++) {
                    representation.set(x, y, new CLIGameSymbol(s, CLIColor.fromSymbol(s)));
                    x++;

                    if (x > (WIDTH / 2) + 1) {
                        x = (WIDTH / 2) - 1;
                        y++;
                    }
                }
            }
        });

        // Draw the associated points and the multiplier
        switch (side) {
            case SideFrontGold sideFrontGold -> {
                if (sideFrontGold.getMultiplier() != Symbol.NONE) {
                    char scoreChar = String.valueOf(side.getAssociatedScore()).charAt(0);
                    representation.set(WIDTH / 2 - 1, 1, new CLIGameSymbol(scoreChar, cardColor));

                    CLIColor symbolColor = CLIColor.fromSymbol(sideFrontGold.getMultiplier());
                    CLIGameSymbol multiplierSymbol = new CLIGameSymbol(sideFrontGold.getMultiplier(), symbolColor);
                    representation.set(WIDTH / 2 + 1, 1, multiplierSymbol);
                } else {
                    char scoreChar = String.valueOf(side.getAssociatedScore()).charAt(0);
                    representation.set(WIDTH / 2, 1, new CLIGameSymbol(scoreChar, cardColor));
                }
            }
            default -> {
                if (side.getAssociatedScore() > 0) {
                    char scoreChar = String.valueOf(side.getAssociatedScore()).charAt(0);
                    representation.set(WIDTH / 2, 1, new CLIGameSymbol(scoreChar, cardColor));
                }
            }
        }

        return this;
    }

}
