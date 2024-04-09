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

import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CLIGameFieldRepresentationTest {

    @Test
    void render() {
        Side side1 = GameResources.getInstance().getGoldCards().getFirst().front();
        Side side2 = GameResources.getInstance().getGoldCards().get(12).front();
        Side side3 = GameResources.getInstance().getGoldCards().get(31).front();
        Side side4 = GameResources.getInstance().getGoldCards().get(24).front();
        Side side5 = GameResources.getInstance().getGoldCards().get(17).front();
        Side side6 = GameResources.getInstance().getGoldCards().get(39).front();
        Side side7 = GameResources.getInstance().getResourceCards().get(20).front();
        Side side8 = GameResources.getInstance().getResourceCards().get(31).front();
        Side side9 = GameResources.getInstance().getResourceCards().get(1).front();
        Side side10 = GameResources.getInstance().getResourceCards().get(10).front();

        GameField gameField = new GameField();

        CLIGameFieldRepresentation representation = new CLIGameFieldRepresentation(gameField);

        assertNotNull(representation.render());

        gameField.placeOnFieldAt(side1, new GameFieldPosition(0, 0));

        assertNotNull(representation.render());

        gameField.placeOnFieldAt(side2, new GameFieldPosition(1, 1));

        assertNotNull(representation.render());

        gameField.placeOnFieldAt(side3, new GameFieldPosition(2, 2));

        assertNotNull(representation.render());

        gameField.placeOnFieldAt(side4, new GameFieldPosition(2, 0));
        gameField.placeOnFieldAt(side5, new GameFieldPosition(1, -1));
        gameField.placeOnFieldAt(side6, new GameFieldPosition(2, -2));
        gameField.placeOnFieldAt(side7, new GameFieldPosition(3, -1));

        assertNotNull(representation.render());

        gameField.placeOnFieldAt(side8, new GameFieldPosition(4, 0));
        gameField.placeOnFieldAt(side9, new GameFieldPosition(5, 1));

        assertNotNull(representation.render());

        gameField.placeOnFieldAt(side10, new GameFieldPosition(-1, -1));

        assertNotNull(representation.render());
    }


}