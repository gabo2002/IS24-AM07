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

package it.polimi.ingsw.am07.client;

import it.polimi.ingsw.am07.model.ClientState;

/**
 * The {@code UserInterface} interface represents the user interface of the client.
 * It provides the entry point and the rendering method for the user interface.
 * CLI and GUI classes implement this interface.
 */
public interface UserInterface {

    /**
     * The entry point of the user interface.
     */
    abstract void entrypoint();

    /**
     * This method renders the user interface based on the client state.
     * @param clientState the client state to render
     */
    abstract void render(ClientState clientState);

}
