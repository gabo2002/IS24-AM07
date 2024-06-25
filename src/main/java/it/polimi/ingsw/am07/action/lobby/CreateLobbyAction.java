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

package it.polimi.ingsw.am07.action.lobby;

import it.polimi.ingsw.am07.action.PlayerAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;

/**
 * This action is used by a player to create a new lobby
 */
public class CreateLobbyAction extends PlayerAction {

    /**
     * The player's pawn color
     */
    private final Pawn color;

    /**
     * A reference to the lobby created by the player
     */
    private Lobby createdLobby;

    /**
     * Create a new CreateLobbyAction for CLI
     *
     * @param playerNickname the player's nickname
     * @param identity       the player's identity
     * @param color          the player's pawn color
     */
    public CreateLobbyAction(String playerNickname, String identity, Pawn color) {
        super(playerNickname, identity);
        this.color = color;
        createdLobby = null;
    }

    /**
     * Execute the function server-side
     *
     * @param matchmaking the matchmaking
     */
    @Override
    public void execute(Matchmaking matchmaking) {
        matchmaking.setNewLobbyCreated(true);
        matchmaking.setPlayerNickname(getPlayerNickname());
        matchmaking.setPlayerPawn(color);
    }

    /**
     * Execute the function client-side
     *
     * @param state the client state
     */
    @Override
    public void reflect(ClientState state) {
        if (createdLobby == null) {
            return;
        }

        state.setLobbyModel(createdLobby);
        state.setPlayerState(PlayerState.ADMIN_WAITING_FOR_PLAYERS);
        state.notifyGameModelUpdate();
    }

    /**
     * Set a reference to the lobby created by the player
     *
     * @param lobby the lobby created by the player
     */
    public void setCreatedLobby(Lobby lobby) {
        createdLobby = lobby;
    }

}
