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

package it.polimi.ingsw.am07.network.rmi;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.reactive.StatefulListener;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

/**
 * Remote listener for RMI, which calls the wrapped RMI listener.
 */
public class RMIRemoteListener implements StatefulListener {

    private final AppLogger LOGGER = new AppLogger(RMIRemoteListener.class);

    private final RMIStatefulListener rmiListener;

    /**
     * Constructor.
     *
     * @param rmiListener the RMI listener
     */
    public RMIRemoteListener(RMIStatefulListener rmiListener) {
        this.rmiListener = rmiListener;
    }

    /**
     * Notify an action.
     *
     * @param action the action to notify
     */
    @Override
    public void notify(Action action) {
        try {
            rmiListener.notify(action);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * Check the pulse.
     *
     * @return true if the remote client is alive, false otherwise
     */
    @Override
    public boolean checkPulse() {
        try {
            return rmiListener.checkPulse();
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        }
    }

    /**
     * Update the last heartbeat time.
     */
    @Override
    public void heartbeat() {
        try {
            rmiListener.heartbeat();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * Get the client's identity.
     *
     * @return the client's identity
     */
    @Override
    public String getIdentity() {
        try {
            return rmiListener.getIdentity();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Get the client's state.
     *
     * @return the client's state
     */
    @Override
    public ClientState getClientState() {
        return null;
    }
}
