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

package it.polimi.ingsw.am07.server.controller;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.server.LobbyStateSyncAction;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.function.Consumer;

/**
 * Controller for the lobby.
 */
public class LobbyController extends Dispatcher {

    private final AppLogger LOGGER = new AppLogger(LobbyController.class);

    private final Lobby lobby;
    private final Consumer<Lobby> migrateToGame;
    /**
     * Constructor.
     *
     * @param lobby         the lobby model
     * @param migrateToGame a callback that migrates the players in the lobby to a new game
     */
    public LobbyController(Lobby lobby, Consumer<Lobby> migrateToGame) {
        super(Game.MAX_PLAYERS);
        this.lobby = lobby;
        this.migrateToGame = migrateToGame;
    }

    /**
     * Execute an action.
     *
     * @param action the action to execute
     */
    @Override
    public synchronized void execute(Action action) {
        LOGGER.debug("Executing action " + action.getIdentity() + " in " + Thread.currentThread().getName());

        action.execute(lobby);

        for (Listener listener : listeners) {
            LOGGER.debug("Notifying listener " + listener + " in " + Thread.currentThread().getName());
            listener.notify(action);
        }

        if (lobby.readyToStart()) {
            migrateToGame.accept(lobby);
        }
    }

    /**
     * Register a new listener.
     *
     * @param listener the listener to register
     */
    @Override
    public synchronized void registerNewListener(Listener listener) {
        LOGGER.debug("Registering new listener " + listener + " in " + Thread.currentThread().getName());

        listeners.add(listener);

        execute(new LobbyStateSyncAction(lobby));
    }

    /**
     * Remove a listener.
     *
     * @param listener the listener to remove
     */
    @Override
    public synchronized void removeListener(Listener listener) {
        LOGGER.debug("Removing listener " + listener + " in " + Thread.currentThread().getName());

        listeners.remove(listener);

        execute(new LobbyStateSyncAction(lobby));
    }

}
