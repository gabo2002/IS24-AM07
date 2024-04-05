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
import it.polimi.ingsw.am07.utils.json.GameDataJsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * GameResources is a singleton that contains the game resources.
 */
public class GameResources {

    public static int CARDS_COUNT = AssetsRegistry.CARDS_COUNT;
    public static int OBJECTIVES_COUNT = AssetsRegistry.OBJECTIVES_COUNT;
    private static GameResources instance;
    private final List<GameCard> goldCards;
    private final List<GameCard> resourceCards;
    private final List<ObjectiveCard> objectiveCards;

    private GameResources() {
        goldCards = new ArrayList<>();
        resourceCards = new ArrayList<>();
        objectiveCards = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the class.
     *
     * @return
     */
    public static GameResources getInstance() {
        if (instance == null) {
            instance = new GameResources();
            instance.initializeCards();
        }
        return instance;
    }

    /**
     * Initializes the cards from the backing JSON.
     */
    private void initializeCards() {
        GameDataJsonParser<GameCard> gameCardParser = new GameDataJsonParser<>(GameCard.class);

        String cardsJson = AssetsRegistry.getCardsJson();

        List<GameCard> cards = gameCardParser.listFromJson(cardsJson);

        resourceCards.addAll(cards.subList(0, AssetsRegistry.CARDS_COUNT));
        goldCards.addAll(cards.subList(AssetsRegistry.CARDS_COUNT, cards.size()));

        GameDataJsonParser<ObjectiveCard> objectiveCardParser = new GameDataJsonParser<>(ObjectiveCard.class);

        String objectivesJson = AssetsRegistry.getObjectivesJson();

        objectiveCards.addAll(objectiveCardParser.listFromJson(objectivesJson));
    }

    /**
     * Returns the gold cards.
     *
     * @return the gold cards
     */
    public List<GameCard> getGoldCards() {
        return goldCards;
    }

    /**
     * Returns the resource cards.
     *
     * @return the resource cards
     */
    public List<GameCard> getResourceCards() {
        return resourceCards;
    }

    /**
     * Returns the objective cards.
     *
     * @return the objective cards
     */
    public List<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

}
