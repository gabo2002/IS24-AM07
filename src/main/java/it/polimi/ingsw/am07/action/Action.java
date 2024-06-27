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
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;

import java.io.Serializable;

/**
 * Interface for an action that can be executed on the game model.
 */
public abstract class Action implements Serializable {

    protected boolean executedCorrectly;
    private String identity;
    private String error;

    /**
     * Empty constructor.
     */
    protected Action() {
        executedCorrectly = false;
    }

    /**
     * Constructor.
     *
     * @param identity the identity of the action
     */
    protected Action(String identity) {
        this.identity = identity;
    }

    /**
     * Execute the action on the game model.
     *
     * @param gameModel the game model
     */
    public void execute(Game gameModel) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * Execute the action on the lobby model.
     *
     * @param lobbyModel the lobby model
     */
    public void execute(Lobby lobbyModel) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Execute the action on the matchmaking model.
     *
     * @param matchmaking the matchmaking model
     */
    public void execute(Matchmaking matchmaking) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Execute the action on the client state.
     *
     * @param clientState the client state
     */
    public void reflect(ClientState clientState) {
        throw new RuntimeException("Not implemented");
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

    /**
     * Get the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return error;
    }

    /**
     * Set the error message.
     */
    public void setErrorMessage(String errorMessage) {
        this.error = errorMessage;
    }

    /**
     * Check if the action was executed correctly.
     *
     * @return true if the action was executed correctly, false otherwise
     */
    public boolean isExecutedCorrectly() {
        return executedCorrectly;
    }

    /**
     * Set if the action was executed correctly.
     */
    public void setExecutedCorrectly(boolean executedCorrectly) {
        this.executedCorrectly = executedCorrectly;
    }

}
