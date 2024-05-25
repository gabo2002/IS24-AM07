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


import it.polimi.ingsw.am07.action.PlayerAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.outOfLobby.OutOfLobbyModel;

import java.util.UUID;

/**
 * Action to add a player to the lobby.
 */
public class PlayerJoinAction extends PlayerAction {

    private final UUID lobbyId;

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     */
    public PlayerJoinAction(String playerNickname, String identity, UUID lobbyId) {
        super(playerNickname, identity);
        this.lobbyId = lobbyId;
    }

    /**
     * Execute the action.
     *
     * @param outOfLobbyModel the outOfLobbyModel model
     * @return true if the action was successful, false otherwise
     */
    // TODO: change to void
    public boolean execute(OutOfLobbyModel outOfLobbyModel) {
        outOfLobbyModel.setNewLobbyCreated(false);
        outOfLobbyModel.setPlayerNickname(getPlayerNickname());
        outOfLobbyModel.setLobbyId(lobbyId);
        return false;
    }

    /**
     * Reflect the action on the client state.
     *
     * @param state the client state
     * @return true if the action executed successfully, false otherwise
     */
    public boolean reflect(ClientState state) {
        state.setPlayerState(PlayerState.WAITING_FOR_PLAYERS);
        state.notifyGameModelUpdate();
        return false;
    }

    @Override
    public String toString() {
        return "PlayJoinAction";
    }
}
