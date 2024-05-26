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

package it.polimi.ingsw.am07.client.cli.rendering.deck;

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIElement;
import it.polimi.ingsw.am07.client.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.client.cli.rendering.common.side.CLILargeSideRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.common.side.CLISideRepresentation;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.matrix.Matrix;

/**
 * Represents the deck in the CLI-rendered game field.
 */
public class CLIGameDeckRepresentation implements CLIElement {

    private static final char LEFT_ANGLE = '/';
    private static final char RIGHT_ANGLE = '\\';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';

    private final Matrix<CLIGameSymbol> deckRepresentation;
    private final Deck deck;

    private final StringBuilder bufferedRender;

    /**
     * Constructs a CLIGameDeckRepresentation, keeping a deck reference.
     *
     * @param deck the deck to represent
     */
    public CLIGameDeckRepresentation(Deck deck) {
        deckRepresentation = new Matrix<>(0, 0, new CLIGameSymbol(' '));
        this.deck = deck;

        bufferedRender = new StringBuilder();
    }

    /**
     * Draws the deck representation.
     */
    private void drawDeck() {
        final int deckWidth = 1 + (CLILargeSideRepresentation.WIDTH + 1) * 3;
        final int deckHeight = 1 + (CLILargeSideRepresentation.HEIGHT + 1) * 2;

        // Draw the borders
        CLIGameSymbol horizontal = new CLIGameSymbol(HORIZONTAL, CLIColor.WHITE);
        CLIGameSymbol vertical = new CLIGameSymbol(VERTICAL, CLIColor.WHITE);
        for (int x = 0; x < deckWidth; x++) {
            deckRepresentation.set(x, 0, horizontal);
            deckRepresentation.set(x, deckHeight - 1, horizontal);
            deckRepresentation.set(x, CLILargeSideRepresentation.HEIGHT + 1, horizontal);
        }
        for (int y = 0; y < deckHeight; y++) {
            deckRepresentation.set(0, y, vertical);
            deckRepresentation.set(deckWidth - 1, y, vertical);
            deckRepresentation.set(CLILargeSideRepresentation.WIDTH + 1, y, vertical);
            deckRepresentation.set(2 * (CLILargeSideRepresentation.WIDTH + 1), y, vertical);
        }
        deckRepresentation.set(0, 0, new CLIGameSymbol(LEFT_ANGLE, CLIColor.WHITE));
        deckRepresentation.set(deckWidth - 1, 0, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.WHITE));
        deckRepresentation.set(0, deckHeight - 1, new CLIGameSymbol(RIGHT_ANGLE, CLIColor.WHITE));
        deckRepresentation.set(deckWidth - 1, deckHeight - 1, new CLIGameSymbol(LEFT_ANGLE, CLIColor.WHITE));

        // Draw the sides (if any)
        GameCard goldTop = deck.peekTopGoldCard();
        if (goldTop != null) {
            renderSideAt(1, 1, goldTop.back());
        }
        GameCard visibleGold_1 = deck.visibleGoldCards()[0];
        if (visibleGold_1 != null) {
            renderSideAt(1 + CLILargeSideRepresentation.WIDTH + 1, 1, visibleGold_1.front());
        }
        GameCard visibleGold_2 = deck.visibleGoldCards()[1];
        if (visibleGold_2 != null) {
            renderSideAt(1 + 2 * (CLILargeSideRepresentation.WIDTH + 1), 1, visibleGold_2.front());
        }

        GameCard resourceTop = deck.peekTopResCard();
        if (resourceTop != null) {
            renderSideAt(1, 1 + CLILargeSideRepresentation.HEIGHT + 1, resourceTop.back());
        }
        GameCard visibleResource_1 = deck.visibleResCards()[0];
        if (visibleResource_1 != null) {
            renderSideAt(1 + CLILargeSideRepresentation.WIDTH + 1, 1 + CLILargeSideRepresentation.HEIGHT + 1, visibleResource_1.front());
        }
        GameCard visibleResource_2 = deck.visibleResCards()[1];
        if (visibleResource_2 != null) {
            renderSideAt(1 + 2 * (CLILargeSideRepresentation.WIDTH + 1), 1 + CLILargeSideRepresentation.HEIGHT + 1, visibleResource_2.front());
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
                deckRepresentation.set(x + i, y + j, sideRepresentation.getMatrix().get(i, j));
            }
        }
    }

    /**
     * Renders the deck.
     *
     * @return the rendered deck
     */
    @Override
    public String render() {
        drawDeck();

        CLIColor currentColor = CLIColor.RESET;

        bufferedRender.setLength(0);

        for (int y = deckRepresentation.getMinY(); y <= deckRepresentation.getMaxY(); y++) {
            for (int x = deckRepresentation.getMinX(); x <= deckRepresentation.getMaxX(); x++) {
                CLIGameSymbol symbol = deckRepresentation.get(x, y);
                bufferedRender.append(symbol.render(currentColor));
                currentColor = symbol.color();
            }

            bufferedRender.append("\n");
        }

        bufferedRender.append(CLIColor.RESET.getCode());

        return bufferedRender.toString();
    }

}
