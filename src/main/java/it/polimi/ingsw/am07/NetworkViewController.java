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

package it.polimi.ingsw.am07;

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NetworkViewController {

    @FXML
    protected void onRMIBtnClicked(ActionEvent event) {

        ClientState clientState = new ClientState();

        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(12345)
                .withNetworkType(NetworkType.RMI)
                .withIdentity("client1")
                .withState(clientState)
                .build();

        loadScene(event);

    }

    @FXML
    protected void onTCPBtnClicked(ActionEvent event) {

        ClientState clientState = new ClientState();

        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(12345)
                .withNetworkType(NetworkType.TCP)
                .withIdentity("client1")
                .withState(clientState)
                .build();

        loadScene(event);
    }


    @FXML
    protected void loadScene(ActionEvent event) {
        try {
            // Carica la nuova scena
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/username-view.fxml"));
            Parent root = fxmlLoader.load();

            // Ottieni la finestra corrente tramite l'evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Imposta la nuova scena sulla finestra corrente
            Scene scene = new Scene(root, 1500, 1000);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
