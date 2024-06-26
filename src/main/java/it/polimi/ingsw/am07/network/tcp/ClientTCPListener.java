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
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.reactive.ClientListener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

/**
 * Client listener for TCP.
 */
public class ClientTCPListener extends ClientListener {

    private final AppLogger LOGGER = new AppLogger(ClientTCPListener.class);

    private long lastHeartbeatTime = 0;

    /**
     * Constructor.
     *
     * @param clientState      the client state
     */
    public ClientTCPListener(ClientState clientState) {
        super(clientState);
    }

    /**
     * Notify an action.
     *
     * @param action the action to notify
     */
    @Override
    public synchronized void notify(Action action) {
        LOGGER.debug("Notifying action " + action.getIdentity() + " in " + Thread.currentThread().getName());

        action.reflect(clientState);
    }

    /**
     * Check the pulse.
     *
     * @return true if the remote server is alive, false otherwise
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
     * Get the client's identity.
     *
     * @return the client's identity
     */
    @Override
    public String getIdentity() {
        return clientState.getIdentity();
    }

}
