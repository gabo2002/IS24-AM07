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
import it.polimi.ingsw.am07.model.outOfLobby.OutOfLobbyModel;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OutOfLobbyController extends Dispatcher {

    private final AppLogger LOGGER = new AppLogger(OutOfLobbyController.class);
    private final OutOfLobbyModel outOfLobbyModel;
    private final BiConsumer<Listener,String> migrateToLobby;


    public OutOfLobbyController(OutOfLobbyModel outOfLobbyModel, BiConsumer<Listener,String> migrateToLobby) {
        this.outOfLobbyModel = outOfLobbyModel;
        this.migrateToLobby = migrateToLobby;
    }

    @Override
    public void registerNewListener(Listener listener) {
        LOGGER.debug("Registering new listener " + listener + " in " + Thread.currentThread().getName());

        listeners.add(listener);

    }

    @Override
    public void removeListener(Listener listener) {
        LOGGER.debug("Removing listener " + listener + " in " + Thread.currentThread().getName());

        listeners.remove(listener);
    }

    @Override
    public synchronized void execute(Action action) {
        LOGGER.debug("Executing action " + action.getIdentity() + " in " + Thread.currentThread().getName());

        action.execute(outOfLobbyModel);

        for (Listener listener : listeners) {
            LOGGER.debug("Notifying listener " + listener + " with identity "+  listener.getIdentity() + " in " + Thread.currentThread().getName());
            listener.notify(action);
        }

        if (outOfLobbyModel.isNewLobbyCreated()) {
            Listener listener = listeners.stream()
                    .filter(l -> l.getIdentity().equals(action.getIdentity())).findFirst().orElse(null);
            migrateToLobby.accept(listener, outOfLobbyModel.getFirstPlayerNickname());
        }
    }
}
