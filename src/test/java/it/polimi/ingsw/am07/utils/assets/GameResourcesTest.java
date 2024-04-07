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

package it.polimi.ingsw.am07.utils.assets;

import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameResourcesTest {

    @Test
    void getGoldCards() {
        GameResources gameResources = GameResources.getInstance();

        List<GameCard> goldCards = gameResources.getGoldCards();

        assertNotNull(goldCards);
        assertEquals(GameResources.getInstance().getCardsCount(), goldCards.size());
    }

    @Test
    void getResourceCards() {
        GameResources gameResources = GameResources.getInstance();

        List<GameCard> resourceCards = gameResources.getResourceCards();

        assertNotNull(resourceCards);
        assertEquals(GameResources.getInstance().getCardsCount(), resourceCards.size());
    }

    @Test
    void getObjectiveCards() {
        GameResources gameResources = GameResources.getInstance();

        List<ObjectiveCard> objectiveCards = gameResources.getObjectiveCards();

        assertNotNull(objectiveCards);
        assertEquals(GameResources.getInstance().getObjectivesCount(), objectiveCards.size());
    }

    @Test
    void getStarterCards() {
        GameResources gameResources = GameResources.getInstance();

        List<GameCard> starterCards = gameResources.getStarterCards();

        assertNotNull(starterCards);
        assertEquals(GameResources.getInstance().getStarterCardsCount(), starterCards.size());
    }

    @Test
    void getShuffledObjectiveCards() {
        GameResources gameResources = GameResources.getInstance();

        List<ObjectiveCard> shuffledObjectiveCards = gameResources.getShuffledObjectiveCards();

        assertNotNull(shuffledObjectiveCards);
        assertEquals(GameResources.getInstance().getObjectivesCount(), shuffledObjectiveCards.size());

        List<ObjectiveCard> objectiveCards = gameResources.getObjectiveCards();

        for (ObjectiveCard card : objectiveCards) {
            assertEquals(1, shuffledObjectiveCards.stream().filter(c -> c.equals(card)).count());
        }
    }

    @Test
    void getShuffledStarterCards() {
        GameResources gameResources = GameResources.getInstance();

        List<GameCard> shuffledStarterCards = gameResources.getShuffledStarterCards();

        assertNotNull(shuffledStarterCards);
        assertEquals(GameResources.getInstance().getStarterCardsCount(), shuffledStarterCards.size());

        List<GameCard> starterCards = gameResources.getStarterCards();

        for (GameCard card : starterCards) {
            assertEquals(1, shuffledStarterCards.stream().filter(c -> c.equals(card)).count());
        }
    }

    @Test
    void getCardsCount() {
        assertEquals(AssetsRegistry.getInstance().getGameResourceDefinition().cardsCount(), GameResources.getInstance().getCardsCount());
    }

    @Test
    void getObjectivesCount() {
        assertEquals(AssetsRegistry.getInstance().getGameResourceDefinition().objectivesCount(), GameResources.getInstance().getObjectivesCount());
    }

    @Test
    void getStarterCardsCount() {
        assertEquals(AssetsRegistry.getInstance().getGameResourceDefinition().starterCardsCount(), GameResources.getInstance().getStarterCardsCount());
    }

}