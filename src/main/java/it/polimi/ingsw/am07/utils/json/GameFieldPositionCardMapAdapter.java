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

package it.polimi.ingsw.am07.utils.json;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a custom Moshi handler that can serialize and deserialize the map of GameFieldPosition and Side

 */
public class GameFieldPositionCardMapAdapter {
    /**
     * Converts a map of GameFieldPosition and Side to a map of String and Side
     * @param map The map to convert
     * @return The converted map
     */
    @ToJson
    public Map<String, Side> toJson(Map<GameFieldPosition, Side> map) {
        Map<String, Side> jsonMap = new HashMap<>();
        for (Map.Entry<GameFieldPosition, Side> entry : map.entrySet()) {
            jsonMap.put(entry.getKey().toString(), entry.getValue());
        }
        return jsonMap;
    }

    /**
     * Converts a map of String and Side to a map of GameFieldPosition and Side
     * @param jsonMap The map to convert
     * @return The converted map
     */
    @FromJson
    public Map<GameFieldPosition, Side> fromJson(Map<String, Side> jsonMap) {
        Map<GameFieldPosition, Side> map = new HashMap<>();
        for (Map.Entry<String, Side> entry : jsonMap.entrySet()) {
            map.put(GameFieldPosition.fromString(entry.getKey()), entry.getValue());
        }
        return map;
    }
}
