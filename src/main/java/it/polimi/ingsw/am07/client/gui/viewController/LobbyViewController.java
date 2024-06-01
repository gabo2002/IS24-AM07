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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class LobbyViewController {


    @FXML
    private Button start_btn;

    private ClientState clientState;
    private Controller controller;

    @FXML
    private ListView players_list;


    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        // Bind the label to reflect the player state changes
        updateView(clientState);
        // clientState.onGameModelUpdate(this::updateView);
    }


    private void updateView(ClientState clientState) {
        // Logic to update the GUI based on the new clientState
        System.out.println("Lobby view, Client state updated: " + clientState);
        // welcomeText.setText("Current Player State: " + clientState.getPlayerState());
        // Additional GUI updates can go here

        start_btn.setDisable(true);

            for (LobbyPlayer player : clientState.getLobbyModel().getPlayers()) {
                players_list.getItems().add(player.getNickname());
            }

            if (players_list.getItems().size() >=2) {
                start_btn.setDisable(false);
            }

            if (clientState.getPlayerState() == PlayerState.ADMIN_WAITING_FOR_PLAYERS) {
                start_btn.setVisible(true);
            }
        }

    @FXML
    protected void onPlayerBtnClick(ActionEvent event){
        Action action = new GameStartAction(clientState.getNickname(), clientState.getIdentity());
        controller.execute(action);
    }

}
