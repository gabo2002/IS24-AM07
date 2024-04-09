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

package it.polimi.ingsw.am07.network;

import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.network.rmi.ClientRMINetworkManager;
import it.polimi.ingsw.am07.network.tcp.ClientTCPNetworkManager;
import it.polimi.ingsw.am07.reactive.Controller;

public interface ClientNetworkManager {

    void connect();

    void disconnect();

    void inflateListener(Game game);

    Controller getController();

    class Factory {

        private String hostname;
        private int port;
        private String identity;
        private Game gameModel;
        private NetworkType networkType;

        public Factory() {
            hostname = null;
            port = 0;
            identity = null;
            gameModel = null;
            networkType = null;
        }

        public Factory withHostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Factory withPort(int port) {
            this.port = port;
            return this;
        }

        public Factory withIdentity(String identity) {
            this.identity = identity;
            return this;
        }

        public Factory withGameModel(Game gameModel) {
            this.gameModel = gameModel;
            return this;
        }

        public Factory withNetworkType(NetworkType networkType) {
            this.networkType = networkType;
            return this;
        }

        public ClientNetworkManager build() {
            if (hostname == null || port == 0 || identity == null || gameModel == null || networkType == null) {
                throw new IllegalStateException("Missing parameters");
            }

            ClientNetworkManager manager;

            switch (networkType) {
                case TCP -> manager = new ClientTCPNetworkManager(hostname, port, identity);
                case RMI -> manager = new ClientRMINetworkManager(hostname, port, identity);
                default -> throw new IllegalArgumentException("Invalid network type");
            }

            manager.connect();

            manager.inflateListener(gameModel);

            return manager;
        }

    }

}
