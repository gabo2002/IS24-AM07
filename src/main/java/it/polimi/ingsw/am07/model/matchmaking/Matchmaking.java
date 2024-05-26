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

public class Matchmaking {

    private final Collection<Lobby> lobbies;
    private boolean isNewLobbyCreated = false;
    private UUID lobbyId;

    private String playerNickname;

    private Pawn playerPawn;
    private String firstPlayerNickname;

    public Matchmaking(Collection<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public boolean isNewLobbyCreated() {
        return isNewLobbyCreated;
    }

    public void setNewLobbyCreated(boolean newLobbyCreated) {
        isNewLobbyCreated = newLobbyCreated;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public Pawn getPlayerPawn() {
        return playerPawn;
    }

    public void setPlayerPawn(Pawn playerPawn) {
        this.playerPawn = playerPawn;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }

    public List<Lobby> getLobbies() {
        return List.copyOf(lobbies);
    }

}
