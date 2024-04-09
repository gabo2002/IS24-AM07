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
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.StatefulListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerTCPNetworkManager implements ServerNetworkManager {

    private final int listeningPort;
    private final Dispatcher dispatcher;
    private final List<Connection> connectionList;
    private ServerSocket serverSocket;

    public ServerTCPNetworkManager(int listeningPort, Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.listeningPort = listeningPort;

        connectionList = new ArrayList<>();
    }

    @Override
    public void start() {
        if (serverSocket != null) {
            stop();
        }

        try {
            serverSocket = new ServerSocket(listeningPort);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listenForConnections();
        reactToConnections();
    }

    @Override
    public void stop() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        serverSocket = null;
    }

    private void listenForConnections() {
        new Thread(() -> {
            while (serverSocket != null) {
                accept();
            }
        }).start();
    }

    private void reactToConnections() {
        new Thread(() -> {
            while (serverSocket != null) {
                synchronized (connectionList) {
                    for (Connection connection : connectionList) {
                        if (connection.available() == 0) {
                            continue;
                        }

                        NetworkPacket packet = connection.receive();

                        if (packet != null) {
                            switch (packet) {
                                case ActionNetworkPacket actionPacket:
                                    dispatcher.execute(actionPacket.getAction());

                                    synchronized (dispatcher) {
                                        dispatcher.notify();
                                    }
                                    break;
                                case IdentityNetworkPacket identityPacket:
                                    break;
                                case HeartbeatNetworkPacket heartbeatPacket:
                                    break;
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void accept() {
        Socket socket;

        try {
            socket = serverSocket.accept();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        DataInputStream reader;
        DataOutputStream writer;

        try {
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        RemoteConnection connection = new RemoteConnection(reader, writer);

        // As the server, we expect the first packet to be an identity packet
        IdentityNetworkPacket identityPacket = (IdentityNetworkPacket) connection.receive();

        StatefulListener listener = new ServerTCPListener(connection, identityPacket.getIdentity());
        dispatcher.registerNewListener(listener);

        synchronized (connectionList) {
            connectionList.add(connection);
        }

        System.out.println("Accepted connection from " + identityPacket.getIdentity());
    }

}
