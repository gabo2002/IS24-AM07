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

import it.polimi.ingsw.am07.Application;
import it.polimi.ingsw.am07.utils.json.GameDataJsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * AssetsRegistry is a class that provides access to the game assets.
 */
public class AssetsRegistry {

    public static String GAME_RESOURCE_DEFINITION_JSON = "config.json";

    private static AssetsRegistry instance;

    private String cardsJsonContent;
    private String objectivesJsonContent;
    private final String starterCardsJsonContent;

    private GameResourceDefinition gameResourceDefinition;

    private AssetsRegistry() {
        cardsJsonContent = null;
        objectivesJsonContent = null;
        starterCardsJsonContent = null;

        loadGameDefinition();
    }

    public static AssetsRegistry getInstance() {
        if (instance == null) {
            instance = new AssetsRegistry();
        }
        return instance;
    }

    /**
     * Returns the JSON representation of the cards.
     *
     * @return The JSON representation of the cards.
     */
    public String getCardsJson() {
        if (cardsJsonContent == null) {
            String json;

            try (InputStream inputStream = Application.class.getResourceAsStream(gameResourceDefinition.cardsJsonFileName())) {
                if (inputStream == null) {
                    throw new RuntimeException("Failed to load card asset");
                }
                json = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                throw new RuntimeException("Failed to read card asset");
            }

            cardsJsonContent = json;
        }

        return cardsJsonContent;
    }

    /**
     * Returns the JSON representation of the objectives.
     *
     * @return The JSON representation of the objectives.
     */
    public String getObjectivesJson() {
        if (objectivesJsonContent == null) {
            String json;

            try (InputStream inputStream = Application.class.getResourceAsStream(gameResourceDefinition.objectivesJsonFileName())) {
                if (inputStream == null) {
                    throw new RuntimeException("Failed to load objective asset");
                }
                json = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                throw new RuntimeException("Failed to read objective asset");
            }

            objectivesJsonContent = json;
        }

        return objectivesJsonContent;
    }

    /**
     * Returns the game resource definition.
     *
     * @return The game resource definition.
     */
    public GameResourceDefinition getGameResourceDefinition() {
        return gameResourceDefinition;
    }

    private void loadGameDefinition() {
        GameDataJsonParser<GameResourceDefinition> gameResourceDefinitionParser = new GameDataJsonParser<>(GameResourceDefinition.class);

        String gameResourceDefinitionJson;

        try (InputStream inputStream = Application.class.getResourceAsStream(GAME_RESOURCE_DEFINITION_JSON)) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load game resource definition asset");
            }
            gameResourceDefinitionJson = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read game resource definition asset");
        }

        gameResourceDefinition = gameResourceDefinitionParser.fromJson(gameResourceDefinitionJson);
    }

}
