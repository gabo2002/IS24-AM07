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
import it.polimi.ingsw.am07.utils.logging.AppLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Controller class for handling the welcome view and lobby selection in the GUI.
 * Manages user interaction and updates based on client state changes.
 */
public class WelcomeViewController {

    private final AppLogger LOGGER = new AppLogger(WelcomeViewController.class);

    private ClientState clientState;

    @FXML
    private ListView<Parent> lobby_list;

    /**
     * Initializes the controller with the current client state and sets up initial UI state.
     *
     * @param clientState the current client state containing lobby information
     * @param controller  the controller instance for executing game actions
     */
    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        updateView(clientState);
    }

    /**
     * Initializes the ListView to handle item selection events.
     * Called automatically after FXML loading is complete.
     */
    @FXML
    public void initialize() {
        lobby_list.setOnMouseClicked(this::onListItemClick);
    }

    /**
     * Handles the event of clicking on an item in the lobby list.
     * Retrieves the selected lobby and updates the client state accordingly.
     *
     * @param event the mouse event triggered by clicking on a lobby item
     */
    private void onListItemClick(MouseEvent event) {
        Parent selectedItem = lobby_list.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Lobby lobby = (Lobby) selectedItem.getUserData();
            clientState.setLobbyModel(lobby);
            clientState.setPlayerState(PlayerState.INSERTING_USERNAME);
        }
    }

    /**
     * Updates the GUI view based on the provided client state.
     * Generates UI elements dynamically for each available lobby.
     *
     * @param clientState the updated client state containing lobby information
     */
    private void updateView(ClientState clientState) {
        LOGGER.info("Lobby available: " + clientState.getAvailableLobbies().size());
        if (!clientState.getAvailableLobbies().isEmpty()) {
            for (Lobby lobby : clientState.getAvailableLobbies()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am07/views/lobby-box.fxml"));
                    Parent lobby_box = fxmlLoader.load();

                    lobby_box.setUserData(lobby);

                    LobbyBoxController lobbyBoxController = fxmlLoader.getController();
                    lobbyBoxController.setLobby_name_box("Lobby of " + lobby.getFirstPlayer().getNickname());
                    lobbyBoxController.setN_players("Players: " + lobby.getPlayers().size());

                    lobby_list.getItems().add(lobby_box);

                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
        LOGGER.info("Welcome view, Client state updated: " + clientState);
    }

    /**
     * Handles the event of clicking on the "Join Lobby" button.
     * Sets the player state to inserting username for lobby join.
     *
     * @param event the action event triggered by clicking on the "Join Lobby" button
     */
    @FXML
    protected void onLobbyBtnClick(ActionEvent event) {
        clientState.setPlayerState(PlayerState.INSERTING_USERNAME);
    }

    /**
     * Handles the event of clicking on the "Reconnect" button.
     * Sets the player state to inserting username for reconnecting to a session.
     *
     * @param event the action event triggered by clicking on the "Reconnect" button
     */
    @FXML
    protected void onReconnectBtnClick(ActionEvent event) {
        clientState.setPlayerState(PlayerState.INSERTING_USERNAME_FOR_RECONNECT);
    }
}
