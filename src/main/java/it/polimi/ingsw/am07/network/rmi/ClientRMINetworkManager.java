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

import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.reactive.StatefulListener;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMINetworkManager implements ClientNetworkManager {

    private final Registry registry;
    private final String identity;
    private RMIDispatcher dispatcher;
    private RMIStatefulListener listener;

    public ClientRMINetworkManager(String identity) {
        Registry tempRegistry = null;

        try {
            tempRegistry = LocateRegistry.getRegistry("127.0.0.1", 10999);
        } catch (Exception e) {
            e.printStackTrace();
        }

        registry = tempRegistry;
        this.identity = identity;
    }

    @Override
    public void connect() {
        try {
            dispatcher = (RMIDispatcher) registry.lookup("dispatcher");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        dispatcher = null;
    }

    @Override
    public Listener inflateListener(Game game) {
        StatefulListener listener = new RMIListener(game, identity);
        RMIStatefulListener rmiListener;

        try {
            rmiListener = new ClientRMIStatefulListener(listener);

            dispatcher.registerNewListener(rmiListener);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return listener;
    }

}
