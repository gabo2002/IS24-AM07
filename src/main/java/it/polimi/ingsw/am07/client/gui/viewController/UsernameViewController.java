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
import it.polimi.ingsw.am07.action.lobby.CreateLobbyAction;
import it.polimi.ingsw.am07.action.lobby.PlayerJoinAction;
import it.polimi.ingsw.am07.action.lobby.ReconnectAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.lobby.LobbyPlayer;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller class for handling username input and lobby actions in the GUI.
 * Manages user interaction and updates based on client state changes.
 */
public class UsernameViewController {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField nicknameField;

    @FXML
    private ListView<String> pawn_list;

    private ObservableList<String> options;

    private ClientState clientState;
    private Controller controller;

    /**
     * Initializes the controller with the current client state and controller instance.
     * Binds the label to reflect the player state changes.
     *
     * @param clientState the current client state containing game information
     * @param controller  the controller instance for executing game actions
     */
    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;
        updateView(clientState);
    }

    /**
     * Updates the GUI view based on the provided client state.
     * Placeholder method for future GUI updates.
     *
     * @param clientState the updated client state to display
     */
    private void updateView(ClientState clientState) {
        // Placeholder for future GUI updates based on client state
    }

    /**
     * Handles the action event triggered by clicking the play button.
     * Performs different actions based on the current state and user input.
     *
     * @param event the action event triggered by clicking the play button
     */
    @FXML
    protected void onPlayBtnClick(ActionEvent event) {
        if (nicknameField.getText().isEmpty()) {
            welcomeText.setText("Insert a username");
            return;
        }

        if (clientState.getPlayerState() == PlayerState.INSERTING_USERNAME_FOR_RECONNECT) {
            // Reconnect action for a player with an existing session
            String nickname = nicknameField.getText();
            clientState.setNickname(nickname);
            Action action = new ReconnectAction(nickname, clientState.getIdentity());
            controller.execute(action);
            return;
        }

        if (clientState.getLobbyModel() != null) {
            // Player join action for an existing lobby
            Set<Pawn> availablePawns = Arrays.stream(Pawn.values())
                    .filter(pawn -> !pawn.equals(Pawn.BLACK))
                    .collect(Collectors.toSet());

            availablePawns.removeAll(
                    clientState.getLobbyModel().getPlayers().stream()
                            .map(LobbyPlayer::getPlayerPawn)
                            .collect(Collectors.toSet())
            );

            List<Pawn> pawns = new ArrayList<>(availablePawns);
            clientState.setNickname(nicknameField.getText());
            Action action = new PlayerJoinAction(nicknameField.getText(), clientState.getIdentity(), clientState.getLobbyModel().getId(), pawns.get(0));
            controller.execute(action);
            return;
        }

        // Create lobby action for a new lobby
        List<Pawn> availablePawns = Arrays.stream(Pawn.values())
                .filter(pawn -> !pawn.equals(Pawn.BLACK))
                .toList();

        Action action = new CreateLobbyAction(nicknameField.getText(), clientState.getIdentity(), availablePawns.get(0));
        controller.execute(action);
    }
}
