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

package it.polimi.ingsw.am07.cli.rendering;

import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideFrontGold;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import it.polimi.ingsw.am07.model.game.side.SideFrontStarter;
import it.polimi.ingsw.am07.utils.matrix.Matrix;

public class CLISideRepresentation {

    public static final int WIDTH = 11;
    public static final int HEIGHT = 7;
    public static final int CORNER_OVERLAP = 3;

    private static final char LEFTANGLE = '/';
    private static final char RIGHTANGLE = '\\';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';

    private final Matrix<CLIGameFieldSymbol> representation;

    public CLISideRepresentation(Side side) {
        representation = new Matrix<>(WIDTH, HEIGHT, new CLIGameFieldSymbol(' '));

        CLIColor cardColor = CLIColor.fromSymbol(side.color());

        // Draw the horizontal borders
        CLIGameFieldSymbol horizontalBorder = new CLIGameFieldSymbol(HORIZONTAL, cardColor);
        for (int i = 1; i < WIDTH - 1; i++) {
            representation.set(i, 0, horizontalBorder);
            representation.set(i, HEIGHT - 1, horizontalBorder);
        }

        // Draw the vertical borders
        CLIGameFieldSymbol verticalBorder = new CLIGameFieldSymbol(VERTICAL, cardColor);
        for (int i = 1; i < HEIGHT - 1; i++) {
            representation.set(0, i, verticalBorder);
            representation.set(WIDTH - 1, i, verticalBorder);
        }

        // Draw the angles
        representation.set(0, 0, new CLIGameFieldSymbol(LEFTANGLE, cardColor));
        representation.set(WIDTH - 1, 0, new CLIGameFieldSymbol(RIGHTANGLE, cardColor));
        representation.set(0, HEIGHT - 1, new CLIGameFieldSymbol(RIGHTANGLE, cardColor));
        representation.set(WIDTH - 1, HEIGHT - 1, new CLIGameFieldSymbol(LEFTANGLE, cardColor));

        // Draw the corners
        representation.set(1, 1, new CLIGameFieldSymbol(side.fieldRepresentation().corners().get(0, 0), cardColor));
        representation.set(WIDTH - 2, 1, new CLIGameFieldSymbol(side.fieldRepresentation().corners().get(0, 1), cardColor));
        representation.set(1, HEIGHT - 2, new CLIGameFieldSymbol(side.fieldRepresentation().corners().get(1, 0), cardColor));
        representation.set(WIDTH - 2, HEIGHT - 2, new CLIGameFieldSymbol(side.fieldRepresentation().corners().get(1, 1), cardColor));

        // Draw the center
        representation.set(WIDTH / 2, HEIGHT / 2, new CLIGameFieldSymbol(side.color(), cardColor));

        char cardSymbol = ' ';
        switch (side) {
            case SideFrontGold ignored -> cardSymbol = CLISymbolMapping.CARD_GOLD;
            case SideFrontRes ignored -> cardSymbol = CLISymbolMapping.CARD_RES;
            case SideFrontStarter ignored -> cardSymbol = CLISymbolMapping.CARD_STARTER;
            default -> {
            }
        }
        representation.set(WIDTH / 2, HEIGHT - 2, new CLIGameFieldSymbol(cardSymbol, cardColor));

        if (side.getAssociatedScore() > 0) {
            char scoreChar = String.valueOf(side.getAssociatedScore()).charAt(0);
            representation.set(WIDTH / 2, 1, new CLIGameFieldSymbol(scoreChar, cardColor));
        }
    }

    public Matrix<CLIGameFieldSymbol> getRepresentation() {
        return representation;
    }

}
