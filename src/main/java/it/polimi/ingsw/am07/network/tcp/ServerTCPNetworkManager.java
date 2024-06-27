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

import it.polimi.ingsw.am07.network.ServerNetworkManager;
import it.polimi.ingsw.am07.network.connection.Connection;
import it.polimi.ingsw.am07.network.connection.RemoteConnection;
import it.polimi.ingsw.am07.network.packets.ActionNetworkPacket;
import it.polimi.ingsw.am07.network.packets.HeartbeatNetworkPacket;
import it.polimi.ingsw.am07.network.packets.IdentityNetworkPacket;
import it.polimi.ingsw.am07.network.packets.NetworkPacket;
import it.polimi.ingsw.am07.reactive.StatefulListener;
import it.polimi.ingsw.am07.server.ServerDispatcher;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Server network manager for TCP.
 * This class is responsible for managing the server-side network operations.
 */
public class ServerTCPNetworkManager implements ServerNetworkManager {

    private final AppLogger LOGGER = new AppLogger(ServerTCPNetworkManager.class);

    private final int listeningPort;
    private final ServerDispatcher dispatcher;
    private final List<RemoteConnection> connectionList;
    private final Map<Connection, StatefulListener> listeners;
    private ServerSocket serverSocket;

    /**
     * Constructor.
     *
     * @param listeningPort the listening port
     * @param dispatcher    the dispatcher
     */
    public ServerTCPNetworkManager(int listeningPort, ServerDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.listeningPort = listeningPort;

        connectionList = new ArrayList<>();
        listeners = new HashMap<>();
    }

    /**
     * Start listening for remote clients.
     */
    @Override
    public void start() {
        if (serverSocket != null) {
            stop();
        }

        try {
            serverSocket = new ServerSocket(listeningPort);
            LOGGER.info("Server started on port " + listeningPort);
        } catch (Exception e) {
            LOGGER.error(e);
        }

        listenForConnections();
        sendHeartbeats();
    }

    /**
     * Close the connection to the remote clients.
     */
    @Override
    public void stop() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            LOGGER.error(e);
        }

        serverSocket = null;
    }

    /**
     * Listens for incoming connections in a separate thread.
     */
    private void listenForConnections() {
        new Thread(() -> {
            while (serverSocket != null) {
                accept();
            }
        }).start();
    }

    /**
     * Check the connection for incoming packets.
     *
     * @param connection the connection to check
     */
    private boolean checkConnection(Connection connection) {
        NetworkPacket packet;
        try {
            packet = connection.receive();
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        }

        if (packet != null) {
            switch (packet) {
                case ActionNetworkPacket actionPacket:
                    dispatcher.execute(actionPacket.getAction());
                    break;
                case IdentityNetworkPacket ignored:
                    break;
                case HeartbeatNetworkPacket ignored:
                    listeners.get(connection).heartbeat();
                    break;
            }
            return true;
        } else {
            synchronized (connectionList) {
                LOGGER.error("Connection closed: " + connection);
                connectionList.remove(connection);
            }
        }
        return false;
    }

    /**
     * Accept a new connection.
     * This will also register a new listener for the connection and it will send the list of open lobbies.
     */
    private void accept() {
        Socket socket;

        try {
            socket = serverSocket.accept();
            LOGGER.info("New connection accepted from " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (Exception e) {
            LOGGER.error(e);
            return;
        }

        DataInputStream reader;
        DataOutputStream writer;

        try {
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            LOGGER.error(e);
            return;
        }

        RemoteConnection connection = new RemoteConnection(socket, reader, writer);

        new Thread(() -> {
            boolean connectionOpen = true;

            IdentityNetworkPacket identityPacket;
            try {
                // As the server, we expect the first packet to be an identity packet
                identityPacket = (IdentityNetworkPacket) connection.receive();
            } catch (Exception e) {
                LOGGER.error(e);
                return;
            }

            if (identityPacket == null || identityPacket.getIdentity() == null) {
                LOGGER.error("Connection closed unexpectedly. Could not read identity packet.");
                return;
            }

            synchronized (connectionList) {
                for (RemoteConnection c : connectionList) {
                    if (c.getIdentity().equals(identityPacket.getIdentity())) {
                        LOGGER.error("Connection closed unexpectedly. Identity already in use.");
                        return;
                    }
                }
            }

            connection.setIdentity(identityPacket.getIdentity());

            StatefulListener listener = new ServerTCPListener(connection, identityPacket.getIdentity());
            dispatcher.registerNewListener(listener);

            listeners.put(connection, listener);

            listener.heartbeat();

            synchronized (connectionList) {
                connectionList.add(connection);
            }

            while (connectionOpen && listener.checkPulse()) {
                connectionOpen = checkConnection(connection);
            }

            LOGGER.error("Connection closed: " + connection);

            dispatcher.removeListener(listener);

            synchronized (connectionList) {
                connectionList.remove(connection);
            }
        }).start();
    }

    /**
     * Send a heartbeat to all the connected clients.
     */
    private void sendHeartbeats() {
        new Thread(() -> {
            while (serverSocket != null) {
                synchronized (connectionList) {
                    for (Connection connection : connectionList) {
                        connection.send(new HeartbeatNetworkPacket());
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                }
            }
        }).start();
    }

}
