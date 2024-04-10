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

package it.polimi.ingsw.am07.controller;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the game.
 */
public class GameController implements Dispatcher {

    private final AppLogger LOGGER = new AppLogger(GameController.class);

    private final Game gameModel;
    private final List<Listener> listeners;

    /**
     * Constructor.
     *
     * @param game the game model
     */
    public GameController(Game game) {
        gameModel = game;
        listeners = new ArrayList<>(4);
    }

    /**
     * Execute an action.
     *
     * @param action the action to execute
     */
    @Override
    public synchronized void execute(Action action) {
        LOGGER.debug("Executing action on model: " + gameModel);

        action.execute(gameModel);

        for (Listener listener : listeners) {
            listener.notify(action);
        }
    }

    /**
     * Register a new listener.
     *
     * @param listener the listener to register
     */
    @Override
    public synchronized void registerNewListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener.
     *
     * @param listener the listener to remove
     */
    @Override
    public synchronized void removeListener(Listener listener) {
        listeners.remove(listener);
    }

}
