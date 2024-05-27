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
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;

public class WelcomeViewController {

    @FXML
    private Button create_lobby_btn;

    private ClientState clientState;
    private Controller controller;

    @FXML
    private ListView<Parent> lobby_list;

    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        // Bind the label to reflect the player state changes
        updateView(clientState);
        // clientState.onGameModelUpdate(this::updateView);
    }

    private void updateView(ClientState clientState) {
        // Logic to update the GUI based on the new clientState
        if (!clientState.getAvailableLobbies().isEmpty()) {
            System.out.println("Lobby available: " + clientState.getAvailableLobbies().size());

            for (Lobby lobby : clientState.getAvailableLobbies()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am07/views/lobby-box.fxml"));
                    Parent lobby_box = fxmlLoader.load();

                    LobbyBoxController lobbyBoxController = fxmlLoader.getController();
                    lobbyBoxController.setLobby_name_box("Lobby");
                    lobbyBoxController.setN_players(lobby.getPlayers().size());

                    lobby_list.getItems().add(lobby_box);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Welcome view, Client state updated: " + clientState);
        // Additional GUI updates can go here
    }

    @FXML
    protected void onLobbyBtnClick(ActionEvent event) {
        clientState.setPlayerState(PlayerState.INSERTING_USERNAME);
        loadScene(event);
    }

    private void loadScene(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am07/views/username-view.fxml"));

        // Obtain the controller for the new view
        UsernameViewController view_controller = fxmlLoader.getController();
        view_controller.init(clientState, controller);

    }

}
