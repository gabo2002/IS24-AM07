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

package it.polimi.ingsw.am07.model.matchmaking;

import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.lobby.Lobby;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * This class represents the matchmaking, which is responsible for creating and managing lobbies.
 */
public class Matchmaking {

    /**
     * The list of lobbies.
     */
    private final Collection<Lobby> lobbies;

    /**
     * A boolean indicating whether a new lobby has just been created.
     * Any access to this field should be synchronized.
     */
    private boolean isNewLobbyCreated = false;

    /**
     * A boolean indicating whether the player has asked for reconnection.
     * Any access to this field should be synchronized.
     */
    private boolean askedForReconnection = false;

    /**
     * The unique identifier of the newly-created lobby.
     */
    private UUID lobbyId;

    /**
     * The nickname of the admin player of the newly-created lobby.
     */
    private String playerNickname;

    /**
     * The pawn of the admin player of the newly-created lobby.
     */
    private Pawn playerPawn;

    /**
     * Constructs a new Matchmaking object with a reference to the list of lobbies.
     *
     * @param lobbies The list of lobbies.
     */
    public Matchmaking(Collection<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    /**
     * Retrieves whether a new lobby has just been created.
     *
     * @return true if a new lobby has just been created, false otherwise.
     */
    public boolean isNewLobbyCreated() {
        return isNewLobbyCreated;
    }

    /**
     * Sets whether a new lobby has just been created.
     *
     * @param newLobbyCreated true if a new lobby has just been created, false otherwise.
     */
    public void setNewLobbyCreated(boolean newLobbyCreated) {
        isNewLobbyCreated = newLobbyCreated;
    }

    /**
     * Retrieves whether the player has asked for reconnection.
     *
     * @return true if the player has asked for reconnection, false otherwise.
     */
    public boolean hasAskedForReconnection() {
        return askedForReconnection;
    }

    /**
     * Sets whether the player has asked for reconnection.
     *
     * @param askedForReconnection true if the player has asked for reconnection, false otherwise.
     */
    public void setAskedForReconnection(boolean askedForReconnection) {
        this.askedForReconnection = askedForReconnection;
    }

    /**
     * Retrieves the nickname of the admin player of the newly-created lobby.
     *
     * @return The nickname of the admin player of the newly-created lobby.
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * Sets the nickname of the admin player of the newly-created lobby.
     *
     * @param playerNickname The nickname of the admin player of the newly-created lobby.
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    /**
     * Retrieves the pawn of the admin player of the newly-created lobby.
     *
     * @return The pawn of the admin player of the newly-created lobby.
     */
    public Pawn getPlayerPawn() {
        return playerPawn;
    }

    /**
     * Sets the pawn of the admin player of the newly-created lobby.
     *
     * @param playerPawn The pawn of the admin player of the newly-created lobby.
     */
    public void setPlayerPawn(Pawn playerPawn) {
        this.playerPawn = playerPawn;
    }

    /**
     * Retrieves the unique identifier of the newly-created lobby.
     *
     * @return The unique identifier of the newly-created lobby.
     */
    public UUID getLobbyId() {
        return lobbyId;
    }

    /**
     * Sets the unique identifier of the newly-created lobby.
     *
     * @param lobbyId The unique identifier of the newly-created lobby.
     */
    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Retrieves the list of lobbies.
     *
     * @return The list of lobbies.
     */
    public List<Lobby> getLobbies() {
        return List.copyOf(lobbies);
    }

}
