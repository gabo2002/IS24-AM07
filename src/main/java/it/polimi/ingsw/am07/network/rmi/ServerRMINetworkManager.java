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

package it.polimi.ingsw.am07.network.rmi;

import it.polimi.ingsw.am07.network.ServerNetworkManager;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMINetworkManager implements ServerNetworkManager {

    private final AppLogger LOGGER = new AppLogger(ServerRMINetworkManager.class);
    private final Dispatcher dispatcher;
    private final Registry registry;

    public ServerRMINetworkManager(int port, Dispatcher dispatcher) {
        this.dispatcher = dispatcher;

        Registry tempRegistry = null;

        try {
            tempRegistry = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            LOGGER.error(e);
        }

        registry = tempRegistry;
    }

    @Override
    public void start() {
        try {
            ServerRMIDispatcher serverRMIDispatcher = new ServerRMIDispatcher(dispatcher);

            registry.rebind("dispatcher", serverRMIDispatcher);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void stop() {
        try {
            registry.unbind("dispatcher");
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

}
