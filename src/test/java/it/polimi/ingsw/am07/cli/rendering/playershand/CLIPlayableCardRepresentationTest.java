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

package it.polimi.ingsw.am07.cli.rendering.playershand;

import it.polimi.ingsw.am07.client.cli.rendering.playershand.CLIPlayableCardRepresentation;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CLIPlayableCardRepresentationTest {

    @Test
    public void renderExample() {

        List<GameCard> cards = new ArrayList<>();

        GameCard card1 = GameResources.getInstance().getGoldCards().getFirst();
        GameCard card2 = GameResources.getInstance().getGoldCards().get(12);
        GameCard card3 = GameResources.getInstance().getGoldCards().get(31);
        GameCard res1 = GameResources.getInstance().getResourceCards().get(20);
        GameCard res2 = GameResources.getInstance().getResourceCards().get(31);
        GameCard res3 = GameResources.getInstance().getResourceCards().get(1);

        cards.add(card1);
        cards.add(card2);
        cards.add(res1);

        CLIPlayableCardRepresentation representation = new CLIPlayableCardRepresentation(cards);
        assertNotNull(representation.render());

        cards.clear();
        cards.add(card3);
        cards.add(res2);
        cards.add(res3);
        assertNotNull(representation.render());
    }

    @Test
    public void tooManyCards() {
        List<GameCard> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cards.add(GameResources.getInstance().getGoldCards().getFirst());
        }
        CLIPlayableCardRepresentation representation = new CLIPlayableCardRepresentation(cards);
        assertNotNull(representation.render());
        System.out.println(representation.render());
    }
}
