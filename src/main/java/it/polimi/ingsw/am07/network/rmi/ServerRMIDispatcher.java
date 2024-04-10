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
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.StatefulListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RMI dispatcher for the server.
 */
public class ServerRMIDispatcher extends UnicastRemoteObject implements RMIDispatcher {

    public final Dispatcher dispatcher;
    private final Map<RMIStatefulListener, StatefulListener> listeners;

    /**
     * Constructor.
     *
     * @param dispatcher the dispatcher
     * @throws RemoteException if an error occurs
     */
    public ServerRMIDispatcher(Dispatcher dispatcher) throws RemoteException {
        super();

        this.dispatcher = dispatcher;
        listeners = new HashMap<>();
    }

    /**
     * Execute an action.
     *
     * @param action the action to execute
     * @throws RemoteException if an error occurs
     */
    @Override
    public synchronized void execute(Action action) throws RemoteException {
        dispatcher.execute(action);
    }

    /**
     * Register a new listener.
     *
     * @param listener the listener to register (the wrapper over StatefulListener)
     * @throws RemoteException if an error occurs
     */
    @Override
    public synchronized void registerNewListener(RMIStatefulListener listener) throws RemoteException {
        RMIRemoteListener remoteListener = new RMIRemoteListener(listener);

        listeners.put(listener, remoteListener);

        dispatcher.registerNewListener(remoteListener);
    }

    /**
     * Remove a listener.
     *
     * @param listener the listener to remove
     * @throws RemoteException if an error occurs
     */
    @Override
    public synchronized void removeListener(RMIStatefulListener listener) throws RemoteException {
        if (listeners.containsKey(listener)) {
            dispatcher.removeListener(listeners.get(listener));
            listeners.remove(listener);
        }
    }

}
