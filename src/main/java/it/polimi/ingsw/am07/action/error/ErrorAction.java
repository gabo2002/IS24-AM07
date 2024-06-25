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

package it.polimi.ingsw.am07.action.error;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.model.ClientState;

/**
 * Action to notify an error to the client not related to another action.
 */
public class ErrorAction extends Action {

    private final String errorMessage;

    /**
     * Constructor.
     *
     * @param errorMessage the error message
     */
    public ErrorAction(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    /**
     * Get the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Reflect the action on the client state.
     *
     * @param clientState the client state
     */
    public void reflect(ClientState clientState) {
        clientState.setClientStringErrorMessage(errorMessage);
        clientState.notifyGameModelUpdate();
    }

}
