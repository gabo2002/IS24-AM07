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
import it.polimi.ingsw.am07.reactive.StatefulListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI stateful listener for the client, used to receive actions from the server.
 */
public class ClientRMIStatefulListener extends UnicastRemoteObject implements RMIStatefulListener {

    private final StatefulListener listener;

    /**
     * Constructor.
     *
     * @param listener the listener to wrap
     * @throws RemoteException if an error occurs
     */
    public ClientRMIStatefulListener(StatefulListener listener) throws RemoteException {
        super();

        this.listener = listener;
    }

    /**
     * Notify an action.
     *
     * @param action the action
     * @throws RemoteException if an error occurs
     */
    @Override
    public synchronized void notify(Action action) throws RemoteException {
        listener.notify(action);
    }

    /**
     * Check the pulse.
     *
     * @return true if the client is alive, false otherwise
     * @throws RemoteException if an error occurs
     */
    @Override
    public synchronized boolean checkPulse() throws RemoteException {
        return listener.checkPulse();
    }

    /**
     * Send a keep-alive signal.
     *
     * @throws RemoteException if an error occurs
     */
    @Override
    public synchronized void heartbeat() throws RemoteException {
        listener.heartbeat();
    }

    /**
     * Get the identity of the client.
     *
     * @return the identity
     * @throws RemoteException if an error occurs
     */
    @Override
    public synchronized String getIdentity() throws RemoteException {
        return listener.getIdentity();
    }

}
