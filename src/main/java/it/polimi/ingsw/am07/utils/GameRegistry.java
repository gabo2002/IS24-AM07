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

package it.polimi.ingsw.am07.utils;

import it.polimi.ingsw.am07.model.game.Game;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * GameRegistry is a singleton that contains the games that are currently being played.
 */
public class GameRegistry {

    private static final String FILENAME = "savedGames.ser";

    private static GameRegistry instance = null;

    private final Map<UUID, Game> games;

    private GameRegistry() {
        games = loadSavedGames();
    }


    /**
     * Returns the singleton instance of the class.
     *
     * @return The singleton instance of the class.
     */
    public static GameRegistry getInstance() {
        if (instance == null) {
            instance = new GameRegistry();
        }
        return instance;
    }

    /**
     * Retrieves the map of games.
     *
     * @return the map of games, either active or ended.
     */
    public Map<UUID, Game> getGames() {
        return games;
    }

    /**
     * Saves the current state of the games.
     *
     * @return true if the state was saved successfully, false otherwise.
     */
    public boolean saveState() {
        try {
            FileOutputStream fileOut = new FileOutputStream(FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(games);

            out.close();
            fileOut.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads the saved games from the file.
     *
     * @return the map of saved games.
     */
    private Map<UUID, Game> loadSavedGames() {
        Map<UUID, Game> games = null;

        try {
            FileInputStream fileIn = new FileInputStream(FILENAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            Object obj = in.readObject();

            if (obj instanceof Map) {
                games = (Map<UUID, Game>) obj;
            }

            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            games = null;
        }

        if (games != null) {
            return new HashMap<>(games);
        } else {
            return new HashMap<>();
        }
    }

}
