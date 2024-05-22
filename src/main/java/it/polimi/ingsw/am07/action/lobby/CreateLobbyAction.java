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
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.outOfLobby.OutOfLobbyModel;
import it.polimi.ingsw.am07.server.ServerDispatcher;

import java.util.UUID;

public class CreateLobbyAction extends PlayerAction {

    private Lobby createdLobby;
    public CreateLobbyAction(String playerNickname, String identity) {
        super(playerNickname, identity);
        createdLobby = null;
    }
    // TODO: change to void
    public boolean execute(OutOfLobbyModel outOfLobbyModel){
        outOfLobbyModel.setNewLobbyCreated(true);
        outOfLobbyModel.setFirstPlayerNickname(getPlayerNickname());
        return false;
    }

    /**
     * Execute the function client-side
     * @param state the client state
     * @return
     */
    public boolean reflect(ClientState state) {

        if (createdLobby == null) {
            return true;
        }

        state.setLobbyModel(createdLobby);
        state.setPlayerState(PlayerState.WAITING_FOR_PLAYERS);
        return false;
    }

    @Override
    public String toString() {
        return "CreateLobbyAction Packet";
    }

    public void setCreatedLobby(Lobby lobby) {
        createdLobby = lobby;
    }

}
