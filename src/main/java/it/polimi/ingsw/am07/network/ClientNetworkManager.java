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

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.network.rmi.ClientRMINetworkManager;
import it.polimi.ingsw.am07.network.tcp.ClientTCPNetworkManager;
import it.polimi.ingsw.am07.reactive.Controller;

/**
 * A generic interface for a client-side network manager.
 */
public interface ClientNetworkManager {

    /**
     * Connect to the server.
     */
    void connect();

    /**
     * Disconnect from the server.
     */
    void disconnect();

    /**
     * Reconnect to the server.
     */
    void reconnect(ClientState clientState);

    /**
     * Inflate the local listener.
     *
     * @param clientState a reference to the local client state
     */
    void inflateListener(ClientState clientState);

    /**
     * Get an interface to the remote controller.
     *
     * @return the controller
     */
    Controller getController();

    /**
     * A factory for client network managers.
     */
    class Factory {

        private String hostname;
        private int port;
        private String identity;
        private ClientState clientState;
        private NetworkType networkType;

        /**
         * Constructor.
         */
        public Factory() {
            hostname = null;
            port = 0;
            identity = null;
            clientState = null;
            networkType = null;
        }

        /**
         * Set the remote hostname.
         *
         * @param hostname the hostname
         * @return this factory
         */
        public Factory withHostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        /**
         * Set the remote port.
         *
         * @param port the port
         * @return this factory
         */
        public Factory withPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * Set the client identity.
         *
         * @param identity the identity
         * @return this factory
         */
        public Factory withIdentity(String identity) {
            this.identity = identity;
            return this;
        }

        /**
         * Set the reference to the client state.
         *
         * @param clientState the reference to the client state
         * @return this factory
         */
        public Factory withState(ClientState clientState) {
            this.clientState = clientState;
            return this;
        }

        /**
         * Set the network type.
         *
         * @param networkType the network type
         * @return this factory
         */
        public Factory withNetworkType(NetworkType networkType) {
            this.networkType = networkType;
            return this;
        }

        /**
         * Build the client network manager.
         *
         * @return the client network manager
         */
        public ClientNetworkManager build() {
            if (hostname == null || port == 0 || identity == null || clientState == null || networkType == null) {
                throw new IllegalStateException("Missing parameters");
            }

            ClientNetworkManager manager;

            switch (networkType) {
                case TCP -> manager = new ClientTCPNetworkManager(hostname, port, identity);
                case RMI -> manager = new ClientRMINetworkManager(hostname, port, identity);
                default -> throw new IllegalArgumentException("Invalid network type");
            }

            // Connect to the server
            manager.connect();

            // Inflate the local listener that writes to the client state
            manager.inflateListener(clientState);

            return manager;
        }

    }

}
