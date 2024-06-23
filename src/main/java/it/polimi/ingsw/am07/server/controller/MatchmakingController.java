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
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.utils.lambda.QuadFunction;
import it.polimi.ingsw.am07.utils.logging.AppLogger;
import it.polimi.ingsw.am07.utils.lambda.TriConsumer;

import java.util.UUID;

public class MatchmakingController extends Dispatcher {

    private final AppLogger LOGGER = new AppLogger(MatchmakingController.class);
    private final Matchmaking matchmaking;
    private final TriConsumer<Listener, String, Pawn> migrateToLobby;
    private final QuadFunction<Listener, String, UUID, Pawn, Boolean> migrateToExistingLobby;


    public MatchmakingController(Matchmaking matchmaking, TriConsumer<Listener, String, Pawn> migrateToLobby, QuadFunction<Listener, String, UUID, Pawn, Boolean> migrateToExistingLobby) {
        this.matchmaking = matchmaking;
        this.migrateToLobby = migrateToLobby;
        this.migrateToExistingLobby = migrateToExistingLobby;
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
        LOGGER.debug("Executing action " + action + " in " + Thread.currentThread().getName());

        action.execute(matchmaking);

        if (matchmaking.isNewLobbyCreated()) {
            Listener listener = listeners.stream()
                    .filter(l -> l.getIdentity().equals(action.getIdentity())).findFirst().orElse(null);
            migrateToLobby.accept(listener, matchmaking.getPlayerNickname(), matchmaking.getPlayerPawn());
            listeners.remove(listener);

            Action stateSyncAction = new LobbyListAction(matchmaking.getLobbies());
            listeners.forEach(l -> l.notify(stateSyncAction));
        } else if (matchmaking.getLobbyId() != null) {
            Listener listener = listeners.stream()
                    .filter(l -> l.getIdentity().equals(action.getIdentity())).findFirst().orElse(null);

            //I have to migrate the player to an existing lobby
            Boolean result = migrateToExistingLobby.apply(listener, matchmaking.getPlayerNickname(), matchmaking.getLobbyId(), matchmaking.getPlayerPawn());

            if (result) {
                // Success, remove the listener from the out of lobby controller
                listeners.remove(listener);
            }
        }
    }
}
