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

package it.polimi.ingsw.am07.network.tcp;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.network.connection.Connection;
import it.polimi.ingsw.am07.network.packets.ActionNetworkPacket;
import it.polimi.ingsw.am07.reactive.Controller;

/**
 * Client interface to the remote controller over TCP.
 */
public class ClientTCPController implements Controller {

    private final Connection serverConnection;

    /**
     * Constructor.
     *
     * @param serverConnection the connection to the server
     */
    public ClientTCPController(Connection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Execute an action.
     *
     * @param action the action
     */
    @Override
    public synchronized void execute(Action action) {
        serverConnection.send(new ActionNetworkPacket(action));
    }

}
