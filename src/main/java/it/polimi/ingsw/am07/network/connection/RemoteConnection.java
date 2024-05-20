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

package it.polimi.ingsw.am07.network.connection;

import it.polimi.ingsw.am07.network.packets.NetworkPacket;
import it.polimi.ingsw.am07.utils.json.NetworkJsonSerializer;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;

/**
 * Connection with a remote client.
 */
public class RemoteConnection implements Connection {

    private final AppLogger LOGGER = new AppLogger(RemoteConnection.class);

    private final DataOutputStream outputStream;
    private final DataInputStream inputStream;

    private final NetworkJsonSerializer serializer;

    /**
     * Constructor.
     *
     * @param inputStream  the input stream to read from
     * @param outputStream the output stream to write to
     */
    public RemoteConnection(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        this.serializer = NetworkJsonSerializer.getInstance();
    }

    /**
     * Send a packet.
     *
     * @param packet the packet to send
     */
    @Override
    public void send(NetworkPacket packet) {
        LOGGER.debug("Sending packet " + packet.getClass().getSimpleName() + " to " + this);

        String json = serializer.toJson(packet);

        try {
            outputStream.writeUTF(json);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * Block until a packet is received.
     *
     * @return the received packet
     */
    @Override
    public NetworkPacket receive() {
        LOGGER.debug("Receiving packet from " + this);

        try {
            String json = inputStream.readUTF();
            return serializer.fromJson(json);
        } catch (EOFException e) {
            LOGGER.error("Connection " + this + "Closed unexpectedly");
            return null;
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Check if there are packets available.
     *
     * @return a number higher than 0 if there are packets available
     */
    @Override
    public int available() {
        try {
            return inputStream.available();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        //return just the id
        return "RemoteConnection@" + Integer.toHexString(hashCode());
    }

}
