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

package it.polimi.ingsw.am07.cli.rendering.starterCard;

import it.polimi.ingsw.am07.client.cli.rendering.starterCard.CLIStarterCardRepresentation;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CLIStarterCardRepresentationTest {

    @Test
    public void renderStarterExample() {

        List<GameCard> starterCards = GameResources.getInstance().getStarterCards();

        //Render some of them
        CLIStarterCardRepresentation representation = new CLIStarterCardRepresentation(starterCards.getFirst());

        String output = representation.render();

        System.out.println(output);

        assertNotNull(output);
    }

}
