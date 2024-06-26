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
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.logging.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for displaying the game's winner and ranking information in the GUI.
 * Manages the UI updates based on the client state containing game results.
 */
public class WinnerViewController {

    private final AppLogger LOGGER = new AppLogger(WinnerViewController.class);

    @FXML
    private Label result_message;

    @FXML
    private ListView<Parent> ranking_list;

    /**
     * Initializes the controller with the current client state and sets up initial UI state.
     *
     * @param clientState the current client state containing game results
     * @param controller  the controller instance for executing game actions
     */
    public void init(ClientState clientState, Controller controller) {
        updateView(clientState);
    }

    /**
     * Updates the UI view based on the provided client state.
     * Populates the ranking list with player information and displays the result message.
     *
     * @param clientState the updated client state containing game results
     */
    private void updateView(ClientState clientState) {
        List<Player> players = clientState.getGameModel().getPlayers(); // Get all players from game model
        ranking_list.getItems().clear(); // Clear the list view before updating

        // Populate the ranking list with player information
        for (Player player : players) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am07/views/player-box.fxml"));
                Parent player_box = fxmlLoader.load();

                PlayerBoxController playerBoxController = fxmlLoader.getController();
                playerBoxController.setPlayer_name_box(player.getNickname());
                playerBoxController.setScore_label("Score: " + (player.getPlayerScore() + player.getPlayerObjectiveScore()));

                ranking_list.getItems().add(player_box);
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }

        // Display result message based on whether the client is among the winners
        try {
            if (clientState.getGameModel().getWinners().contains(clientState.getGameModel().getSelf())) {
                result_message.setText("You won!");
            }
        } catch (Exception e) {
            System.out.println("Could not find winners");
        }
    }

}
