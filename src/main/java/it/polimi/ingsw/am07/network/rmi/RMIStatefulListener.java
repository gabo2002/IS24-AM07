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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Thin wrapper over the StatefulListener interface but with knowledge of exceptions, to be used with RMI.
 */
public interface RMIStatefulListener extends Remote {

    /**
     * Notify an action.
     *
     * @param action the action to notify
     * @throws RemoteException if an error occurs
     */
    void notify(Action action) throws RemoteException;

    /**
     * Check if the connection is still alive.
     *
     * @return true if the connection is still alive, false otherwise
     * @throws RemoteException if an error occurs
     */
    boolean checkPulse() throws RemoteException;

    /**
     * Send a heartbeat to the server.
     *
     * @throws RemoteException if an error occurs
     */
    void heartbeat() throws RemoteException;

    /**
     * Get the identity of the listener.
     *
     * @return the identity of the listener
     * @throws RemoteException if an error occurs
     */
    String getIdentity() throws RemoteException;

}
