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

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.assets.AssetsRegistry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NetworkViewController {

    private Controller controller;
    private ClientState clientState;
    private String identity;

    @FXML
    protected void onRMIBtnClicked(ActionEvent event) {
        initializeClientState(NetworkType.RMI);
        loadScene(event);
    }

    @FXML
    protected void onTCPBtnClicked(ActionEvent event) {
        initializeClientState(NetworkType.TCP);
        loadScene(event);
    }

    private void initializeClientState(NetworkType networkType) {
        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(networkType == NetworkType.RMI ? AssetsRegistry.getInstance().getGameResourceDefinition().rmiPort() : AssetsRegistry.getInstance().getGameResourceDefinition().tcpPort())
                .withNetworkType(networkType)
                .withIdentity(identity)
                .withState(clientState)
                .build();

        controller = networkManager.getController();
    }

    private void loadScene(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am07/views/welcome-view.fxml"));
            Parent root = fxmlLoader.load();

            // Obtain the controller for the new view
            WelcomeViewController view_controller = fxmlLoader.getController();
            view_controller.init(clientState, controller);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root, 1500, 1000);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(ClientState state, String identity) {
        this.clientState = state;
        this.identity = identity;
    }
}
