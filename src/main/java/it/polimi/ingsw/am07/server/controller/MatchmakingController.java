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
import it.polimi.ingsw.am07.action.server.LobbyListAction;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;
import it.polimi.ingsw.am07.utils.trifunction.TriFunction;

import java.util.UUID;
import java.util.function.BiConsumer;

public class MatchmakingController extends Dispatcher {

    private final AppLogger LOGGER = new AppLogger(MatchmakingController.class);
    private final Matchmaking matchmaking;
    private final BiConsumer<Listener, String> migrateToLobby;
    private final TriFunction<Listener, String, UUID, Void> migrateToExistingLobby;


    public MatchmakingController(Matchmaking matchmaking, BiConsumer<Listener, String> migrateToLobby, TriFunction<Listener, String, UUID, Void> migratoToExistingLobby) {
        this.matchmaking = matchmaking;
        this.migrateToLobby = migrateToLobby;
        this.migrateToExistingLobby = migratoToExistingLobby;
    }

    @Override
    public void registerNewListener(Listener listener) {
        LOGGER.debug("Registering new listener " + listener + " in " + Thread.currentThread().getName());

        listeners.add(listener);

        listener.notify(new LobbyListAction(matchmaking.getLobbies()));
    }

    @Override
    public void removeListener(Listener listener) {
        LOGGER.debug("Removing listener " + listener + " in " + Thread.currentThread().getName());

        listeners.remove(listener);
    }

    @Override
    public synchronized void execute(Action action) {
        LOGGER.debug("Executing action " + action.getIdentity() + " in " + Thread.currentThread().getName());

        action.execute(matchmaking);

        if (matchmaking.isNewLobbyCreated()) {
            Listener listener = listeners.stream()
                    .filter(l -> l.getIdentity().equals(action.getIdentity())).findFirst().orElse(null);
            migrateToLobby.accept(listener, matchmaking.getFirstPlayerNickname());
            listeners.remove(listener);

            Action stateSyncAction = new LobbyListAction(matchmaking.getLobbies());
            listeners.forEach(l -> l.notify(stateSyncAction));
        } else if (matchmaking.getLobbyId() != null) {
            Listener listener = listeners.stream()
                    .filter(l -> l.getIdentity().equals(action.getIdentity())).findFirst().orElse(null);
            //devo migrare il listener in una lobby esistente
            migrateToExistingLobby.apply(listener, matchmaking.getFirstPlayerNickname(), matchmaking.getLobbyId());
        }
    }
}
