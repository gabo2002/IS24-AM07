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

package it.polimi.ingsw.am07.action;

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;

import java.io.Serializable;

/**
 * Interface for an action that can be executed on the game model.
 */
public abstract class Action implements Serializable {

    private String identity;

    /**
     * Empty constructor.
     */
    protected Action() {

    }

    /**
     * Execute the action on the game model.
     *
     * @param gameModel the game model
     * @return true if the action was executed successfully, false otherwise
     */
    public boolean execute(Game gameModel) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Reflect the action on the game model.
     *
     * @param gameModel the game model
     * @return true if the action was reflected successfully, false otherwise
     */
    public boolean reflect(Game gameModel) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Execute the action on the lobby model.
     *
     * @param lobbyModel the lobby model
     * @return true if the action was executed successfully, false otherwise
     */
    public boolean execute(Lobby lobbyModel) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Reflect the action on the lobby model.
     *
     * @param lobbyModel the lobby model
     * @return true if the action was reflected successfully, false otherwise
     */
    public boolean reflect(Lobby lobbyModel) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Execute the action on the client state.
     *
     * @param clientState the client state
     * @return true if the action was executed successfully, false otherwise
     */
    public boolean reflect(ClientState clientState) {
        clientState.notifyGameModelUpdate();

        return true;
    }

    /**
     * Get the identity of the action.
     *
     * @return the identity of the action
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Set the identity of the action.
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

}
