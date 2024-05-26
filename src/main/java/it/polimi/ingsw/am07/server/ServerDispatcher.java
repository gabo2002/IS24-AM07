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
import it.polimi.ingsw.am07.action.lobby.CreateLobbyAction;
import it.polimi.ingsw.am07.action.server.GameStateSyncAction;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.server.controller.GameController;
import it.polimi.ingsw.am07.server.controller.LobbyController;
import it.polimi.ingsw.am07.server.controller.MatchmakingController;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Server dispatcher.
 */
public class ServerDispatcher extends Dispatcher {

    private static final AppLogger LOGGER = new AppLogger(ServerDispatcher.class);

    private final Map<UUID, Lobby> lobbies;
    private final Map<UUID, Game> games;
    private final Map<Game, GameController> gameControllers;
    private final Map<Lobby, LobbyController> lobbyControllers;
    private final Map<String, Dispatcher> listenerDispatchers;
    private final MatchmakingController matchmakingController;

    /**
     * Constructor.
     *
     * @param games the list of games, restored from storage
     */
    public ServerDispatcher(Map<UUID, Game> games) {
        super();

        this.games = games;
        this.lobbies = new HashMap<>();

        gameControllers = new HashMap<>(games.size());
        lobbyControllers = new HashMap<>();
        listenerDispatchers = new HashMap<>();

        Matchmaking matchmaking = new Matchmaking(lobbies.values());

        matchmakingController = new MatchmakingController(matchmaking, this::migrateToLobby, this::migrateToExistingLobby);
    }

    /**
     * Get the list of open lobbies. This method is used to provide the list of lobbies to the clients.
     */
    public List<Lobby> getLobbies() {
        return this.lobbies.values().stream().toList();
    }

    /**
     * Execute an action.
     * Internally, the action is dispatched to the correct listener based on the action's caller identity.
     *
     * @param action the action to dispatch
     */
    @Override
    public synchronized void execute(Action action) {
        LOGGER.debug("Executing action " + action.getIdentity() + " in " + Thread.currentThread().getName());

        if (listenerDispatchers.containsKey(action.getIdentity())) {
            listenerDispatchers.get(action.getIdentity()).execute(action);
        } else {
            throw new IllegalArgumentException("Listener not found");
        }
    }

    /**
     * Register a new listener.
     * If the identity of the listener is already present in the server, the listener is registered to the correct game or lobby.
     * Otherwise, a new lobby is created and the listener is registered to it.
     *
     * @param listener the listener to register
     */
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

        listenerDispatchers.put(listener.getIdentity(), matchmakingController);
        matchmakingController.registerNewListener(listener);
    }

    /**
     * Remove a listener.
     *
     * @param listener the listener to remove
     */
    @Override
    public synchronized void removeListener(Listener listener) {
        LOGGER.debug("Removing listener " + listener.getIdentity());

        listenerDispatchers.remove(listener.getIdentity());
    }

    /**
     * Migrate a lobby to a new game.
     *
     * @param lobby the lobby to migrate
     */
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

        // Update the listener dispatchers
        lobby.getPlayers().forEach(player -> listenerDispatchers.put(player.getNickname(), gameController));

        // Inherit the listeners from the lobby controller
        gameController.inheritListeners(lobbyControllers.get(lobby));

        // Delete the lobby and lobby controller
        lobbyControllers.remove(lobby);
        lobbies.remove(lobby.getId());

        // Execute a game sync action to notify the listeners of the new game
        gameController.execute(new GameStateSyncAction(game));
    }

    private synchronized Void migrateToExistingLobby(Listener listener, String nickname, UUID lobbyId) {
        LOGGER.debug("Migrating to existing lobby");

        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Lobby not found");
        }

        lobby.addNewPlayer(nickname);
        lobbyControllers.get(lobby).registerNewListener(listener);
        return null;
    }

    private synchronized void migrateToLobby(Listener listener, String firstPlayerNickname) {
        LOGGER.debug("Migrating to lobby");

        // Create a new lobby
        Lobby lobby = new Lobby();
        LobbyController lobbyController = new LobbyController(lobby, this::migrateLobbyToGame);

        //Notify the listener
        CreateLobbyAction action = new CreateLobbyAction(firstPlayerNickname, listener.getIdentity());
        action.setCreatedLobby(lobby);
        listener.notify(action);

        // Store the new lobby and its controller
        lobbyControllers.put(lobby, lobbyController);

        // Store the new lobby
        lobbies.put(lobby.getId(), lobby);

        // Add the new player to the lobby
        lobby.addNewPlayer(firstPlayerNickname);

        // Add the listener to the lobby
        lobbyController.registerNewListener(listener);
    }

}
