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

package it.polimi.ingsw.am07.model.lobby;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * This class represents the lobby of a game.
 */
public class Lobby implements Serializable {

    private final UUID id;

    private final List<LobbyPlayer> players;

    private LobbyState state;

    /**
     * Constructs a new Lobby object with an empty list of players.
     */
    public Lobby() {
        players = new ArrayList<>();
        id = UUID.randomUUID();
        state = LobbyState.WAITING_FOR_PLAYERS;
    }

    /**
     * Constructs a new Lobby object as a copy of another lobby.
     *
     * @param other The lobby to copy.
     */
    public Lobby(Lobby other) {
        players = new ArrayList<>(other.players);
        id = other.id;
        state = other.state;
    }

    /**
     * Retrieves the list of players in the lobby.
     *
     * @return The list of players in the lobby.
     */
    public List<LobbyPlayer> getPlayers() {
        return players;
    }

    /**
     * Retrieves the number of players in the lobby.
     *
     * @return The number of players in the lobby.
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Adds a new player to the lobby.
     *
     * @param nickname The nickname of the player to add.
     * @throws IllegalArgumentException If the provided nickname is already taken.
     */
    public LobbyPlayer addNewPlayer(String nickname) throws IllegalArgumentException {
        if (state == LobbyState.FULL) {
            throw new IllegalStateException("Lobby is full");
        }

        LobbyPlayer player = new LobbyPlayer(nickname);

        if (players.contains(player)) {
            throw new IllegalArgumentException("Nickname already taken");
        }

        players.add(player);

        if (players.size() == 4) {
            state = LobbyState.FULL;
        }

        return player;
    }

    /**
     * Removes a player from the lobby.
     *
     * @param nickname The nickname of the player to remove.
     */
    public void removePlayer(String nickname) {
        players.removeIf(player -> player.getNickname().equals(nickname));

        if (players.size() < 4) {
            state = LobbyState.WAITING_FOR_PLAYERS;
        }
    }

    /**
     * Retrieves the first player in the lobby.
     *
     * @return The first player in the lobby.
     */
    public LobbyPlayer getFirstPlayer() {
        try {
            return players.getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Retrieves the UUID of the lobby.
     *
     * @return The UUID of the lobby.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Determines whether the lobby is ready to start the game.
     *
     * @return True if the lobby is ready to start the game, false otherwise.
     */
    public boolean readyToStart() {
        return state == LobbyState.READY_TO_START;
    }

    /**
     * Starts the game.
     */
    public void startGame() throws IllegalStateException {
        if (players.size() < 2) {
            throw new IllegalStateException("Not enough players to start the game");
        }

        state = LobbyState.READY_TO_START;
    }

    /**
     * Determines whether the lobby is full.
     *
     * @return True if the lobby is full, false otherwise.
     */
    public boolean isFull() {
        return state == LobbyState.FULL;
    }

}
