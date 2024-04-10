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
import it.polimi.ingsw.am07.network.connection.RemoteConnection;
import it.polimi.ingsw.am07.network.packets.ActionNetworkPacket;
import it.polimi.ingsw.am07.reactive.StatefulListener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

/**
 * Server TCP listener.
 */
public class ServerTCPListener implements StatefulListener {

    public static final long HEARTBEAT_MAX_INTERVAL = 10000;
    private final AppLogger LOGGER = new AppLogger(ServerTCPListener.class);
    private final RemoteConnection remoteConnection;
    private final String identity;
    private long lastHeartbeatTime = 0;

    /**
     * Constructor.
     *
     * @param remoteConnection the remote connection
     * @param identity         the identity
     */
    public ServerTCPListener(RemoteConnection remoteConnection, String identity) {
        this.remoteConnection = remoteConnection;
        this.identity = identity;
    }

    /**
     * Notify an action.
     *
     * @param action the action to notify
     */
    @Override
    public void notify(Action action) {
        LOGGER.debug("Notifying action " + action.getIdentity() + " in " + Thread.currentThread().getName());

        remoteConnection.send(new ActionNetworkPacket(action));
    }

    /**
     * Check the pulse.
     *
     * @return true if the remote client is alive, false otherwise
     */
    @Override
    public boolean checkPulse() {
        return System.currentTimeMillis() - lastHeartbeatTime < HEARTBEAT_MAX_INTERVAL;
    }

    /**
     * Update the last heartbeat time.
     */
    @Override
    public void heartbeat() {
        lastHeartbeatTime = System.currentTimeMillis();
    }

    /**
     * Get the identity.
     *
     * @return the identity
     */
    @Override
    public String getIdentity() {
        return identity;
    }

}
