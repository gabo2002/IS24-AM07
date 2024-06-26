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

package it.polimi.ingsw.am07.client.cli.rendering.starterCard;

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIElement;
import it.polimi.ingsw.am07.client.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.client.cli.rendering.common.side.CLILargeSideRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.common.side.CLISideRepresentation;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
/**
 * The class is responsible for rendering the
 * command line interface (CLI) representation of the starter card.
 */
public class CLIStarterCardRepresentation implements CLIElement {
    private static final char LEFT_ANGLE = '/';
    private static final char RIGHT_ANGLE = '\\';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';
    private final StringBuilder bufferedRender;
    private final Matrix<CLIGameSymbol> cardRepresentation;
    private final GameCard starterCard;
    /**
     * Constructs a new {@code CLIStarterCardRepresentation} with the specified starter card.
     *
     * @param card the starter card to be represented
     */
    public CLIStarterCardRepresentation(GameCard card) {
        cardRepresentation = new Matrix<>(0, 0, new CLIGameSymbol(' '));
        starterCard = card;
        bufferedRender = new StringBuilder();
    }

    /**
     * Draws the starter card.
     */
    private void drawStartCard() {
        final int cardWidth = 1 + (CLILargeSideRepresentation.WIDTH + 1) * 2;
        final int cardHeight = 1 + (CLILargeSideRepresentation.HEIGHT + 1);

        // Draw the borders
        CLIGameSymbol horizontal = new CLIGameSymbol(HORIZONTAL, CLIColor.WHITE);
        CLIGameSymbol vertical = new CLIGameSymbol(VERTICAL, CLIColor.WHITE);
        for (int x = 0; x < cardWidth; x++) {
            cardRepresentation.set(x, 0, horizontal);
            cardRepresentation.set(x, cardHeight - 1, horizontal);
            cardRepresentation.set(x, CLILargeSideRepresentation.HEIGHT + 1, horizontal);
        }
        for (int y = 0; y < cardHeight; y++) {
            cardRepresentation.set(0, y, vertical);
            cardRepresentation.set(cardWidth - 1, y, vertical);
            cardRepresentation.set(CLILargeSideRepresentation.WIDTH + 1, y, vertical);
            cardRepresentation.set(2 * (CLILargeSideRepresentation.WIDTH + 1), y, vertical);
        }
        cardRepresentation.set(0, 0, new CLIGameSymbol(LEFT_ANGLE, CLIColor.WHITE));
        cardRepresentation.set(cardWidth - 1, 0, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.WHITE));
        cardRepresentation.set(0, cardHeight - 1, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.WHITE));
        cardRepresentation.set(cardWidth - 1, cardHeight - 1, new CLIGameSymbol(LEFT_ANGLE, CLIColor.WHITE));

        //draw the cards
        renderSideAt(1, 1, starterCard.front());
        renderSideAt(1 + CLILargeSideRepresentation.WIDTH + 1, 1, starterCard.back());

    }

    /**
     * Renders a side at the specified position.
     *
     * @param x    the x coordinate
     * @param y    the y coordinate
     * @param side the side to render
     */
    private void renderSideAt(int x, int y, Side side) {
        CLISideRepresentation sideRepresentation = new CLISideRepresentation.Factory(side).large();
        for (int i = 0; i < sideRepresentation.width(); i++) {
            for (int j = 0; j < sideRepresentation.height(); j++) {
                cardRepresentation.set(x + i, y + j, sideRepresentation.getMatrix().get(i, j));
            }
        }
    }

    public String render() {
        drawStartCard();

        CLIColor currentColor = CLIColor.RESET;

        bufferedRender.setLength(0);

        for (int y = cardRepresentation.getMinY(); y <= cardRepresentation.getMaxY(); y++) {
            for (int x = cardRepresentation.getMinX(); x <= cardRepresentation.getMaxX(); x++) {
                CLIGameSymbol symbol = cardRepresentation.get(x, y);
                bufferedRender.append(symbol.render(currentColor));
                currentColor = symbol.color();
            }

            bufferedRender.append("\n");
        }

        bufferedRender.append(CLIColor.RESET.getCode());

        return bufferedRender.toString();
    }
}
