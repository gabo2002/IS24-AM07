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

package it.polimi.ingsw.am07.cli.rendering.field;

import it.polimi.ingsw.am07.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.cli.rendering.CLIElement;
import it.polimi.ingsw.am07.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.cli.rendering.common.CLISideRepresentation;
import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.matrix.Matrix;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Renders the Game Field in the CLI.
 */
public class CLIGameFieldRepresentation implements CLIElement {

    private final Matrix<CLIGameSymbol> fieldRepresentation;
    private final GameField gameField;

    private final Map<Side, GameFieldPosition> bufferedField;
    private final StringBuilder bufferedRender;

    /**
     * Constructor for the CLIGameFieldRepresentation.
     * Keeps a reference to the GameField.
     *
     * @param field the GameField to render
     */
    public CLIGameFieldRepresentation(GameField field) {
        fieldRepresentation = new Matrix<>(0, 0, new CLIGameSymbol(' '));
        gameField = field;

        bufferedField = new HashMap<>(gameField.getPlacedCards().size());
        bufferedRender = new StringBuilder();
    }

    /**
     * Maps a card GameFieldPosition to a position inside the rendered field.
     *
     * @param position the GameFieldPosition to map
     * @return the transformed GameFieldPosition
     */
    public static GameFieldPosition mapToCliPosition(GameFieldPosition position, int width, int height, int overlap) {
        return new GameFieldPosition(
                (position.x() * (width - overlap)) - 1,
                (position.y() * (height - overlap)) - 1
        );
    }

    /**
     * Adds a Side to the field representation at the specified position.
     *
     * @param side     the Side to add
     * @param position the position to add the Side at, in GameField coordinates
     */
    private void addSideAt(Side side, GameFieldPosition position) {
        CLISideRepresentation representation = new CLISideRepresentation.Factory(side).small();
        GameFieldPosition newPosition = mapToCliPosition(position, representation.width(), representation.height(), representation.overlapAmount());

        for (int x = newPosition.x(); x < newPosition.x() + representation.width(); x++) {
            for (int y = newPosition.y(); y < newPosition.y() + representation.height(); y++) {
                fieldRepresentation.set(x, y, representation.getMatrix().get(x - newPosition.x(), y - newPosition.y()));
            }
        }
    }

    /**
     * Updates the field representation with the current state of the GameField.
     *
     * @return true if the field representation was updated, false otherwise
     */
    private boolean updateFieldRepresentation() {
        Map<Side, GameFieldPosition> fieldCards = gameField.getPlacedCards();

        if (fieldCards.size() == bufferedField.size()) {
            // No changes, no need to update
            return false;
        }

        List<Map.Entry<Side, GameFieldPosition>> sortedEntrySet = fieldCards.entrySet().stream()
                .sorted(Comparator.comparingInt(t -> t.getValue().z()))
                .collect(Collectors.toCollection(ArrayList::new));

        for (Map.Entry<Side, GameFieldPosition> entry : sortedEntrySet) {
            if (!bufferedField.containsKey(entry.getKey())) {
                addSideAt(entry.getKey(), entry.getValue());
                bufferedField.put(entry.getKey(), entry.getValue());
            }
        }

        return true;
    }

    /**
     * Renders the Game Field.
     *
     * @return the rendered Game Field, which can be printed to the CLI
     */
    public String render() {
        if (updateFieldRepresentation()) {
            CLIColor currentColor = CLIColor.RESET;

            bufferedRender.setLength(0);
            bufferedRender.append(CLIColor.RESET.getCode());

            for (int y = fieldRepresentation.getMinY(); y <= fieldRepresentation.getMaxY(); y++) {
                for (int x = fieldRepresentation.getMinX(); x <= fieldRepresentation.getMaxX(); x++) {
                    CLIGameSymbol symbol = fieldRepresentation.get(x, y);
                    bufferedRender.append(symbol.render(currentColor));
                    currentColor = symbol.color();
                }

                bufferedRender.append('\n');
            }

            bufferedRender.append(CLIColor.RESET.getCode());
        }

        return bufferedRender.toString();
    }

}
