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

package it.polimi.ingsw.am07.action.server;

import it.polimi.ingsw.am07.action.ServerAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.lobby.Lobby;

/**
 * Action to synchronize the lobby state.
 */
public class LobbyStateSyncAction extends ServerAction {

    private final Lobby lobby;

    /**
     * Constructor.
     *
     * @param lobby the lobby
     */
    public LobbyStateSyncAction(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Execute the action. Since we are synchronizing the lobby state from the server model, no change is to be
     * propagated to the server model.
     *
     * @param lobby the lobby model
     * @return true if the action executed successfully, false otherwise
     */
    @Override
    public boolean execute(Lobby lobby) {
        return true;
    }

    /**
     * Reflect the action on the client state.
     *
     * @param clientState the client state
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean reflect(ClientState clientState) {
        clientState.setLobbyModel(lobby);
        clientState.notifyGameModelUpdate();
        return true;
    }

    /**
     * Get the identity of the client that sent the action.
     *
     * @return the identity
     */
    @Override
    public String getIdentity() {
        return getClass().getSimpleName();
    }

}
