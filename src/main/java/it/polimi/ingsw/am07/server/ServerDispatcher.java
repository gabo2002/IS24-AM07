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
import it.polimi.ingsw.am07.action.error.ErrorAction;
import it.polimi.ingsw.am07.action.lobby.CreateLobbyAction;
import it.polimi.ingsw.am07.action.lobby.PlayerJoinAction;
import it.polimi.ingsw.am07.action.server.ResumeGameAction;
import it.polimi.ingsw.am07.action.server.ServerGameStartAction;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.GameState;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.server.controller.GameController;
import it.polimi.ingsw.am07.server.controller.LobbyController;
import it.polimi.ingsw.am07.server.controller.MatchmakingController;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.HashMap;
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

        listenerDispatchers = new HashMap<>();
        gameControllers = new HashMap<>(games.size());

        // Create a game controller for each game
        games.forEach((id, game) -> {
            GameController gameController = new GameController(game);
            gameControllers.put(game, gameController);
        });

        lobbyControllers = new HashMap<>();

        Matchmaking matchmaking = new Matchmaking(lobbies.values());

        matchmakingController = new MatchmakingController(matchmaking, this::migrateToLobby, this::migrateToExistingLobby, this::reconnectToGame);
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
    public void registerNewListener(Listener listener) {
        LOGGER.debug("Registering new listener " + listener.getIdentity());

        if (listener.getIdentity() == null) {
            LOGGER.error("Listener identity is null. Dropping connection.");
            return;
        }

        synchronized (this) {
            listenerDispatchers.put(listener.getIdentity(), matchmakingController);
        }

        new Thread(() -> matchmakingController.registerNewListener(listener)).start();
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
        lobby.getPlayers().forEach(player -> listenerDispatchers.put(player.getIdentity(), gameController));

        // Inherit the listeners from the lobby controller
        gameController.inheritListeners(lobbyControllers.get(lobby));

        // Delete the lobby and lobby controller
        lobbyControllers.remove(lobby);
        lobbies.remove(lobby.getId());

        // Execute a game sync action to notify the listeners of the new game
        gameController.execute(new ServerGameStartAction(game));
    }

    /**
     * Migrate a listener attached to the general matchmaking to an existing lobby.
     *
     * @param listener   the listener to migrate
     * @param nickname   the nickname of the player
     * @param lobbyId    the id of the lobby to migrate to
     * @param playerPawn the pawn of the player
     */
    private synchronized Boolean migrateToExistingLobby(Listener listener, String nickname, UUID lobbyId, Pawn playerPawn) {
        LOGGER.debug("Migrating to existing lobby");

        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            ErrorAction errorAction = new ErrorAction("Error: Lobby not found");
            listener.notify(errorAction);
            return false;
        }

        if (lobby.getPlayers().size() >= 4) {
            ErrorAction errorAction = new ErrorAction("Error: Lobby is full");
            listener.notify(errorAction);
            return false;
        }

        try {
            lobby.addNewPlayer(nickname, listener.getIdentity(), playerPawn);
            //Notify the listener
            PlayerJoinAction action = new PlayerJoinAction(nickname, listener.getIdentity(), lobbyId, playerPawn);
            listener.notify(action);
        } catch (IllegalArgumentException e) {
            ErrorAction errorAction = new ErrorAction(e.getMessage());
            listener.notify(errorAction);
            return false;
        }

        //Migrate the listener from OutOfLobbyController to LobbyController
        listenerDispatchers.put(listener.getIdentity(), lobbyControllers.get(lobby));
        lobbyControllers.get(lobby).registerNewListener(listener);

        return true;
    }

    private synchronized Boolean reconnectToGame(Listener listener) {
        LOGGER.debug("Reconnecting to game");

        for (Map.Entry<Game, GameController> entry : gameControllers.entrySet()) {
            if (entry.getKey().getPlayers().stream().anyMatch(player -> player.getIdentity().equals(listener.getIdentity()))) {
                listenerDispatchers.put(listener.getIdentity(), entry.getValue());
                entry.getValue().registerNewListener(listener);

                // Number of listeners equals number of players in the game
                final boolean allClientsReady = entry.getValue().getListenersCount() == entry.getKey().getPlayers().size();

                // Send the game state to the listener
                entry.getValue().execute(new ResumeGameAction(entry.getKey(), allClientsReady));
                return true;
            }
        }

        LOGGER.debug("Reconnection failed");

        return false;
    }

    /**
     * Migrate a listener attached to the general matchmaking to a new lobby.
     *
     * @param listener            the listener to migrate
     * @param firstPlayerNickname the nickname of the first player
     * @param playerPawn          the pawn of the player
     */
    private synchronized void migrateToLobby(Listener listener, String firstPlayerNickname, Pawn playerPawn) {
        LOGGER.debug("Migrating to lobby");

        // Create a new lobby
        Lobby lobby = new Lobby();
        LobbyController lobbyController = new LobbyController(lobby, this::migrateLobbyToGame);

        //Notify the listener
        CreateLobbyAction action = new CreateLobbyAction(firstPlayerNickname, listener.getIdentity(), playerPawn);
        action.setCreatedLobby(lobby);
        listener.notify(action);

        // Store the new lobby and its controller
        lobbyControllers.put(lobby, lobbyController);

        // Store the new lobby
        lobbies.put(lobby.getId(), lobby);

        // Add the new player to the lobby
        lobby.addNewPlayer(firstPlayerNickname, listener.getIdentity(), playerPawn);
        listenerDispatchers.put(listener.getIdentity(), lobbyController);

        // Add the listener to the lobby
        lobbyController.registerNewListener(listener);
    }

    /**
     * Cleanup the dispatcher, removing ended games and the corresponding controllers.
     */
    public void cleanup() {
        games.entrySet().removeIf(entry -> {
            if (entry.getValue().getGameState() == GameState.ENDED) {
                gameControllers.remove(entry.getValue());
                return true;
            }
            return false;
        });
    }

}
