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

package it.polimi.ingsw.am07.server;

import it.polimi.ingsw.am07.network.ServerNetworkManager;
import it.polimi.ingsw.am07.network.rmi.ServerRMINetworkManager;
import it.polimi.ingsw.am07.network.tcp.ServerTCPNetworkManager;
import it.polimi.ingsw.am07.utils.GameRegistry;
import it.polimi.ingsw.am07.utils.assets.AssetsRegistry;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The game server.
 */
public class Server {

    private final AppLogger LOGGER = new AppLogger(Server.class);

    private final ServerNetworkManager tcpNetworkManager;
    private final ServerNetworkManager rmiNetworkManager;

    /**
     * Constructor.
     *
     * @param tcpPort the TCP port
     * @param rmiPort the RMI port
     */
    public Server(int tcpPort, int rmiPort) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        ServerDispatcher dispatcher = new ServerDispatcher(gameRegistry.getGames());

        tcpNetworkManager = new ServerTCPNetworkManager(tcpPort, dispatcher);
        rmiNetworkManager = new ServerRMINetworkManager(rmiPort, dispatcher);
    }

    /**
     * Constructor.
     */
    public Server() {
        this(
                AssetsRegistry.getInstance().getGameResourceDefinition().tcpPort(),
                AssetsRegistry.getInstance().getGameResourceDefinition().rmiPort()
        );
    }

    /**
     * Start the server.
     */
    public void main() {
        setupAutoSave();

        tcpNetworkManager.start();
        rmiNetworkManager.start();
    }

    /**
     * Automatically saves the state of any active game every five seconds.
     */
    private void setupAutoSave() {
        Runnable autoSave = () -> {
            if (!GameRegistry.getInstance().saveState()) {
                LOGGER.error("Failed to save the game state.");
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(autoSave, 0, 5, TimeUnit.SECONDS);
    }

}
