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

import it.polimi.ingsw.am07.action.server.HangGameAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.connection.Connection;
import it.polimi.ingsw.am07.network.connection.RemoteConnection;
import it.polimi.ingsw.am07.network.packets.ActionNetworkPacket;
import it.polimi.ingsw.am07.network.packets.HeartbeatNetworkPacket;
import it.polimi.ingsw.am07.network.packets.IdentityNetworkPacket;
import it.polimi.ingsw.am07.network.packets.NetworkPacket;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.reactive.StatefulListener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Client network manager for TCP.
 */
public class ClientTCPNetworkManager implements ClientNetworkManager {

    private final AppLogger LOGGER = new AppLogger(ClientTCPNetworkManager.class);

    private final String serverAddress;
    private final int serverPort;
    private final String identity;

    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private Connection connection;
    private StatefulListener listener;
    private Controller controller;

    /**
     * Constructor.
     *
     * @param serverAddress the server address
     * @param serverPort    the server port
     * @param identity      the client identity
     */
    public ClientTCPNetworkManager(String serverAddress, int serverPort, String identity) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.identity = identity;

        listener = null;
        socket = null;
    }

    /**
     * Connect to the server.
     */
    @Override
    public void connect() {
        LOGGER.debug("Connecting to " + serverAddress + ":" + serverPort);

        if (socket != null) {
            disconnect();
        }

        try {
            socket = new Socket(serverAddress, serverPort);
            socket.setSoTimeout(5000);
        } catch (Exception e) {
            LOGGER.error(e);
            return;
        }

        try {
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            LOGGER.error(e);
            try {
                socket.close();
            } catch (Exception ex) {
                LOGGER.error(ex);
            }
            return;
        }

        connection = new RemoteConnection(socket, reader, writer);

        controller = new ClientTCPController(connection);

        // Identify ourselves to the server
        connection.send(new IdentityNetworkPacket(identity));
    }

    /**
     * Disconnect from the server.
     */
    @Override
    public void disconnect() {
        LOGGER.debug("Disconnecting from " + serverAddress + ":" + serverPort);

        try {
            reader.close();
        } catch (Exception e) {
            LOGGER.error(e);
        }

        try {
            writer.close();
        } catch (Exception e) {
            LOGGER.error(e);
        }

        try {
            socket.close();
        } catch (Exception e) {
            LOGGER.error(e);
        }

        socket = null;
    }

    /**
     * Reconnect to the server.
     */
    @Override
    public void reconnect(ClientState clientState) {
        controller = null;
        connection = null;
        listener = null;

        connect();

        if (connection != null) {
            inflateListener(clientState);
        }
    }

    /**
     * Inflate the listener.
     *
     * @param clientState the client state
     */
    @Override
    public void inflateListener(ClientState clientState) {
        if (listener != null) {
            return;
        }

        listener = new ClientTCPListener(clientState);

        setupReceiverLoop();
        setupHeartbeatLoop();
    }

    /**
     * Get an interface to the remote controller.
     *
     * @return the controller
     */
    @Override
    public Controller getController() {
        return controller;
    }

    /**
     * Set up the receiver loop in a background thread to handle inbounds packets.
     */
    private void setupReceiverLoop() {
        new Thread(() -> {
            while (socket != null) {
                receivePacket();
            }
        }).start();
    }

    /**
     * Receives, parses and handles a packet.
     */
    private void receivePacket() {
        NetworkPacket packet;
        try {
            packet = connection.receive();
        } catch (Exception e) {
            LOGGER.error(e);
            disconnect();
            listener.notify(new HangGameAction(identity));
            listener = null;
            return;
        }

        LOGGER.debug("Received packet: " + packet);

        if (packet != null) {
            switch (packet) {
                case ActionNetworkPacket actionPacket -> {
                    LOGGER.debug("Received ActionNetworkPacket with Action: " + actionPacket.getAction());
                    listener.notify(actionPacket.getAction());
                }
                case HeartbeatNetworkPacket ignored -> {
                    LOGGER.debug("Received HeartbeatNetworkPacket");
                    listener.heartbeat();
                }
                default -> {
                }
            }
        } else {
            LOGGER.error("Connection closed");
            disconnect();

            listener.notify(new HangGameAction(identity));
            listener = null;
        }
    }

    /**
     * Set up the heartbeat loop in a background thread to send heartbeats.
     */
    private void setupHeartbeatLoop() {
        new Thread(() -> {
            while (socket != null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                }

                connection.send(new HeartbeatNetworkPacket());

                if (listener != null && !listener.checkPulse()) {
                    listener.notify(new HangGameAction(identity));
                    listener = null;
                }
            }
        }).start();
    }

}
