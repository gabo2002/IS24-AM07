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

import it.polimi.ingsw.am07.cli.CLIElement;
import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.matrix.Matrix;

import java.util.HashMap;
import java.util.Map;

public class CLIGameFieldRepresentation implements CLIElement {

    private final Matrix<CLIGameFieldSymbol> fieldRepresentation;
    private final GameField gameField;

    private final Map<Side, GameFieldPosition> bufferedField;
    private final StringBuilder bufferedRender;

    public CLIGameFieldRepresentation(GameField field) {
        fieldRepresentation = new Matrix<>(0, 0, new CLIGameFieldSymbol(' '));
        gameField = field;

        bufferedField = new HashMap<>(gameField.getPlacedCards().size());
        bufferedRender = new StringBuilder();
    }

    public static GameFieldPosition mapToCliPosition(GameFieldPosition position) {
        return new GameFieldPosition(
                (position.x() * (CLISideRepresentation.WIDTH - CLISideRepresentation.CORNER_OVERLAP)) - 1,
                (position.y() * (CLISideRepresentation.HEIGHT - CLISideRepresentation.CORNER_OVERLAP)) - 1
        );
    }

    private void addSideAt(Side side, GameFieldPosition position) {
        CLISideRepresentation representation = new CLISideRepresentation(side);
        GameFieldPosition newPosition = mapToCliPosition(position);

        for (int x = newPosition.x(); x < newPosition.x() + CLISideRepresentation.WIDTH; x++) {
            for (int y = newPosition.y(); y < newPosition.y() + CLISideRepresentation.HEIGHT; y++) {
                fieldRepresentation.set(x, y, representation.getRepresentation().get(x - newPosition.x(), y - newPosition.y()));
            }
        }
    }

    private boolean updateFieldRepresentation() {
        Map<Side, GameFieldPosition> fieldCards = gameField.getPlacedCards();

        if (fieldCards.size() == bufferedField.size()) {
            return false;
        }

        for (Map.Entry<Side, GameFieldPosition> entry : fieldCards.entrySet()) {
            if (!bufferedField.containsKey(entry.getKey())) {
                addSideAt(entry.getKey(), entry.getValue());
                bufferedField.put(entry.getKey(), entry.getValue());
            }
        }

        return true;
    }

    public String render() {
        if (updateFieldRepresentation()) {
            CLIColor currentColor = CLIColor.RESET;

            bufferedRender.setLength(0);
            bufferedRender.append(CLIColor.RESET.getCode());

            for (int y = fieldRepresentation.getMinY(); y <= fieldRepresentation.getMaxY(); y++) {
                for (int x = fieldRepresentation.getMinX(); x <= fieldRepresentation.getMaxX(); x++) {
                    bufferedRender.append(fieldRepresentation.get(x, y).render(currentColor));
                    currentColor = fieldRepresentation.get(x, y).color();
                }

                bufferedRender.append('\n');
            }

            bufferedRender.append(CLIColor.RESET.getCode());
        }

        return bufferedRender.toString();
    }

}
