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

package it.polimi.ingsw.am07.model.game.card;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.utils.json.GameDataJsonParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceObjectiveCardTest {

    @Test
    void calculateScore() {
        ResourceHolder playerResources = new ResourceHolder();
        playerResources.incrementResource(Symbol.RED);
        playerResources.incrementResource(Symbol.RED);
        playerResources.incrementResource(Symbol.RED);
        playerResources.incrementResource(Symbol.RED);

        ResourceHolder requirements = new ResourceHolder();
        requirements.incrementResource(Symbol.RED);
        requirements.incrementResource(Symbol.RED);

        ObjectiveCard resourceObjectiveCard = new ResourceObjectiveCard(2, requirements);

        assertEquals(4, resourceObjectiveCard.calculateScore(playerResources, null));

        requirements.incrementResource(Symbol.RED);

        assertEquals(2, resourceObjectiveCard.calculateScore(playerResources, null));

        requirements.incrementResource(Symbol.RED);

        assertEquals(2, resourceObjectiveCard.calculateScore(playerResources, null));

        requirements.incrementResource(Symbol.RED);

        assertEquals(0, resourceObjectiveCard.calculateScore(playerResources, null));
    }

    @Test
    void ensureSerializability() {
        assertDoesNotThrow(() -> {
            ResourceHolder requirements = new ResourceHolder();
            requirements.incrementResource(Symbol.RED);
            requirements.incrementResource(Symbol.RED);

            ObjectiveCard card = new ResourceObjectiveCard(5, requirements);

            GameDataJsonParser<ObjectiveCard> parser = new GameDataJsonParser<>(ObjectiveCard.class);

            String json = parser.toJson(card);

            ObjectiveCard deserialized = parser.fromJson(json);
        });
    }

}