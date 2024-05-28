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

package it.polimi.ingsw.am07.client.gui.viewController;

import it.polimi.ingsw.am07.client.gui.NetworkInitializer;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class NetworkViewController {

    private ClientState clientState;
    private ClientNetworkManager.Factory clientNetworkManagerFactory;
    private String identity;
    private NetworkInitializer networkInitializer;

    @FXML
    protected void onRMIBtnClicked(ActionEvent event) {
        networkInitializer.initializeClientState(NetworkType.RMI);
    }

    @FXML
    protected void onTCPBtnClicked(ActionEvent event) {
        networkInitializer.initializeClientState(NetworkType.TCP);
    }

    public void init(ClientState state, String identity, ClientNetworkManager.Factory clientNetworkManagerFactory, NetworkInitializer networkInitializer) {
        this.clientState = state;
        this.identity = identity;
        this.clientNetworkManagerFactory = clientNetworkManagerFactory;
        this.networkInitializer = networkInitializer;
    }
}

