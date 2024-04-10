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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    private final ServerDispatcher dispatcher;
    private final ServerNetworkManager tcpNetworkManager;
    private final ServerNetworkManager rmiNetworkManager;

    public Server(int tcpPort, int rmiPort) {
        GameRegistry gameRegistry = GameRegistry.getInstance();
        dispatcher = new ServerDispatcher(gameRegistry.getGames());

        tcpNetworkManager = new ServerTCPNetworkManager(tcpPort, dispatcher);
        rmiNetworkManager = new ServerRMINetworkManager(rmiPort, dispatcher);
    }

    public void main() {
        setupAutoSave();

        tcpNetworkManager.start();
        rmiNetworkManager.start();
    }

    private void setupAutoSave() {
        Runnable autoSave = () -> {
            GameRegistry.getInstance().saveState();
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(autoSave, 0, 5, TimeUnit.SECONDS);
    }

}
