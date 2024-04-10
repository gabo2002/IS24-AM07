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

package it.polimi.ingsw.am07.server;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.controller.GameController;
import it.polimi.ingsw.am07.controller.LobbyController;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerDispatcher implements Dispatcher {

    private static final AppLogger LOGGER = new AppLogger(ServerDispatcher.class);

    private final Map<UUID, Lobby> lobbies;
    private final Map<UUID, Game> games;
    private final Map<Game, GameController> gameControllers;
    private final Map<Lobby, LobbyController> lobbyControllers;
    private final Map<String, Dispatcher> listenerDispatchers;

    public ServerDispatcher(Map<UUID, Game> games) {
        this.games = games;
        this.lobbies = new HashMap<>();

        gameControllers = new HashMap<>(games.size());
        lobbyControllers = new HashMap<>();
        listenerDispatchers = new HashMap<>();
    }

    @Override
    public synchronized void execute(Action action) {
        LOGGER.debug("Executing action " + action.getIdentity() + " in " + Thread.currentThread().getName());

        if (listenerDispatchers.containsKey(action.getIdentity())) {
            listenerDispatchers.get(action.getIdentity()).execute(action);
        } else {
            throw new IllegalArgumentException("Listener not found");
        }
    }

    @Override
    public synchronized void registerNewListener(Listener listener) {
        LOGGER.debug("Registering new listener " + listener.getIdentity());

        // Reconnect the player to the game
        for (Map.Entry<Game, GameController> entry : gameControllers.entrySet()) {
            if (entry.getKey().getPlayers().stream().anyMatch(player -> player.getNickname().equals(listener.getIdentity()))) {
                listenerDispatchers.put(listener.getIdentity(), entry.getValue());
                entry.getValue().registerNewListener(listener);
                return;
            }
        }

        LOGGER.debug("Listener " + listener.getIdentity() + " not found in any game");

        // Find a lobby with an empty slot
        for (Map.Entry<Lobby, LobbyController> entry : lobbyControllers.entrySet()) {
            if (!entry.getKey().isFull()) {
                // Store the new listener
                listenerDispatchers.put(listener.getIdentity(), entry.getValue());

                // Add the new player to the lobby
                entry.getKey().addNewPlayer(listener.getIdentity());

                // Add the listener to the lobby
                entry.getValue().registerNewListener(listener);
                return;
            }
        }

        LOGGER.debug("Creating a new lobby for listener " + listener.getIdentity());

        // Create a new lobby
        Lobby lobby = new Lobby();
        LobbyController lobbyController = new LobbyController(lobby, this::migrateLobbyToGame);

        // Store the new lobby and its controller
        lobbies.put(lobby.getId(), lobby);
        lobbyControllers.put(lobby, lobbyController);
        listenerDispatchers.put(listener.getIdentity(), lobbyController);

        // Add the new player to the lobby
        lobby.addNewPlayer(listener.getIdentity());

        // Add the listener to the lobby
        lobbyController.registerNewListener(listener);
    }

    @Override
    public synchronized void removeListener(Listener listener) {
        LOGGER.debug("Removing listener " + listener.getIdentity());

        listenerDispatchers.remove(listener.getIdentity());
    }

    private synchronized void migrateLobbyToGame(Lobby lobby) {
        LOGGER.debug("Migrating lobby " + lobby.getId() + " to a new game");

        // Create a new game
        Game game = new Game.Factory()
                .fromLobby(lobby)
                .build();

        // Create a new game controller
        GameController gameController = new GameController(game);

        // Store the new game and its controller
        games.put(game.getId(), game);
        gameControllers.put(game, gameController);

        // Remove the lobby and its controller
        lobbies.remove(lobby.getId());
        lobbyControllers.remove(lobby);

        // Update the listener dispatchers
        lobby.getPlayers().forEach(player -> listenerDispatchers.put(player.getNickname(), gameController));
    }

}
