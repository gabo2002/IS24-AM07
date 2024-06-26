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

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.lobby.GameStartAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.lobby.LobbyPlayer;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * The {@code LobbyViewController} class manages the lobby view in the GUI.
 * It updates the view to reflect the current state of the lobby and handles
 * user interactions such as starting the game or quitting the application.
 */
public class LobbyViewController {

    @FXML
    private Button start_btn;

    @FXML
    private Label lobby_name;

    @FXML
    private ListView<String> players_list;

    private ClientState clientState;
    private Controller controller;

    /**
     * Initializes the LobbyViewController with the given {@code ClientState} and {@code Controller}.
     * This method binds the lobby view to reflect the player state changes.
     *
     * @param clientState the current state of the client, containing information about the lobby and players
     * @param controller the controller used to execute actions and handle the game logic
     */
    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        // Bind the label to reflect the player state changes
        updateView(clientState);
        // clientState.onGameModelUpdate(this::updateView);
    }

    /**
     * Updates the GUI to reflect the new state of the client.
     * This method is called whenever the client state changes to refresh the lobby view.
     *
     * @param clientState the updated state of the client
     */
    private void updateView(ClientState clientState) {
        // Log the state update for debugging purposes
        System.out.println("Lobby view, Client state updated: " + clientState);

        // Initially enable the start button
        start_btn.setDisable(false);

        // Clear the current player list and add updated player names
        players_list.getItems().clear();
        for (LobbyPlayer player : clientState.getLobbyModel().getPlayers()) {
            players_list.getItems().add(player.getNickname());
        }

        // Disable the start button if there are fewer than 2 players in the lobby
        if (players_list.getItems().size() < 2) {
            start_btn.setDisable(true);
        }

        // Make the start button visible if the current player is the admin waiting for players
        if (clientState.getPlayerState() == PlayerState.ADMIN_WAITING_FOR_PLAYERS) {
            start_btn.setVisible(true);
        }

        // Update the lobby name label with the first player's nickname
        lobby_name.setText("Lobby of " + clientState.getLobbyModel().getFirstPlayer().getNickname());
    }

    /**
     * Handles the event when the start game button is clicked.
     * This method creates a {@code GameStartAction} and executes it via the controller to start the game.
     *
     * @param event the action event triggered by clicking the start button
     */
    @FXML
    protected void onPlayerBtnClick(ActionEvent event) {
        // Create a new GameStartAction with the nickname of the first player and the client's identity
        Action startGameAction = new GameStartAction(clientState.getLobbyModel().getFirstPlayer().getNickname(), clientState.getIdentity());

        // Set the client's nickname to the first player's nickname
        clientState.setNickname(clientState.getLobbyModel().getFirstPlayer().getNickname());

        // Execute the action to start the game
        controller.execute(startGameAction);
    }

    /**
     * Handles the event when the quit button is clicked.
     * This method exits the application gracefully.
     *
     * @param event the action event triggered by clicking the quit button
     */
    @FXML
    protected void onQuitBtnClick(ActionEvent event) {
        // Exit the JavaFX application
        Platform.exit();

        // Terminate the Java virtual machine
        System.exit(0);
    }
}
