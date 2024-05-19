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

package it.polimi.ingsw.am07.client.cli.rendering.playershand;

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIElement;
import it.polimi.ingsw.am07.client.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.client.cli.rendering.common.CLILargeSideRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.common.CLISideRepresentation;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.matrix.Matrix;

import java.util.List;

public class CLIPlayableCardRepresentation implements CLIElement {

    private static final char LEFT_ANGLE = '/';
    private static final char RIGHT_ANGLE = '\\';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';

    private final Matrix<CLIGameSymbol> handRepresentation;
    private final List<GameCard> hand;

    private final StringBuilder bufferedRender;

    /**
     * Constructs a CLIPlayableCardRepresentation.
     *
     * @param List<GameCard> hand the hand to represent
     */
    public CLIPlayableCardRepresentation(List<GameCard> cards) {
        handRepresentation = new Matrix<>(0, 0, new CLIGameSymbol(' '));
        this.hand = cards;

        bufferedRender = new StringBuilder();
    }

    /**
     * Draws the hand representation.
     */
    private void drawHand() {
        final int handWidth = 1 + (CLILargeSideRepresentation.WIDTH + 1) * hand.size();
        final int handHeight = 1 + (CLILargeSideRepresentation.HEIGHT + 1) * 2;

        // Draw the borders
        CLIGameSymbol horizontal = new CLIGameSymbol(HORIZONTAL, CLIColor.WHITE);
        CLIGameSymbol vertical = new CLIGameSymbol(VERTICAL, CLIColor.WHITE);
        for (int x = 0; x < handWidth; x++) {
            handRepresentation.set(x, 0, horizontal);
            handRepresentation.set(x, handHeight - 1, horizontal);
            handRepresentation.set(x, CLILargeSideRepresentation.HEIGHT + 1, horizontal);
        }
        for (int y = 0; y < handHeight; y++) {
            handRepresentation.set(0, y, vertical);
            handRepresentation.set(handWidth - 1, y, vertical);
            handRepresentation.set(CLILargeSideRepresentation.WIDTH + 1, y, vertical);
            handRepresentation.set(2 * (CLILargeSideRepresentation.WIDTH + 1), y, vertical);
        }
        handRepresentation.set(0, 0, new CLIGameSymbol(LEFT_ANGLE, CLIColor.WHITE));
        handRepresentation.set(handWidth - 1, 0, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.WHITE));
        handRepresentation.set(0, handHeight - 1, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.WHITE));
        handRepresentation.set(handWidth - 1, handHeight - 1, new CLIGameSymbol(LEFT_ANGLE, CLIColor.WHITE));

        //draw the cards
        for (int i = 0; i < hand.size(); i++) {
            GameCard card = hand.get(i);
            renderSideAt(1 + i * (CLILargeSideRepresentation.WIDTH + 1), 1, card.front());
            renderSideAt(1 + i * (CLILargeSideRepresentation.WIDTH + 1), 1 + CLILargeSideRepresentation.HEIGHT + 1, card.back());
        }
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
                handRepresentation.set(x + i, y + j, sideRepresentation.getMatrix().get(i, j));
            }
        }
    }

    /**
     * Renders the hand.
     *
     * @return the rendered hand representation
     */
    @Override
    public String render() {
        drawHand();

        CLIColor currentColor = CLIColor.RESET;

        bufferedRender.setLength(0);

        for (int y = handRepresentation.getMinY(); y <= handRepresentation.getMaxY(); y++) {
            for (int x = handRepresentation.getMinX(); x <= handRepresentation.getMaxX(); x++) {
                CLIGameSymbol symbol = handRepresentation.get(x, y);
                bufferedRender.append(symbol.render(currentColor));
                currentColor = symbol.color();
            }

            bufferedRender.append("\n");
        }

        bufferedRender.append(CLIColor.RESET.getCode());

        return bufferedRender.toString();
    }

}
