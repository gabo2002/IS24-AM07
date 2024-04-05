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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class AssetsRegistry {

    private static final String CARDS_JSON = "cards.json";
    private static final String OBJECTIVES_JSON = "objectives.json";
    public static int CARDS_COUNT = 40;
    public static int OBJECTIVES_COUNT = 0;

    public static String getCardsJson() {
        String json;

        try (InputStream inputStream = Application.class.getResourceAsStream(CARDS_JSON)) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load card asset");
            }
            json = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read card asset");
        }

        return json;
    }

    public static String getObjectivesJson() {
        String json;

        try (InputStream inputStream = Application.class.getResourceAsStream(OBJECTIVES_JSON)) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load objective asset");
            }
            json = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read objective asset");
        }

        return json;
    }

}
