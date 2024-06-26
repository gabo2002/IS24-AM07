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

    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        // Bind the label to reflect the player state changes
        updateView(clientState);
        // clientState.onGameModelUpdate(this::updateView);
    }


    private void updateView(ClientState clientState) {
        // Logic to update the GUI based on the new clientState
        System.out.println("Username view, Client state updated: " + clientState);
        // welcomeText.setText("Current Player State: " + clientState.getPlayerState());
        // Additional GUI updates can go here
    }

    @FXML
    protected void onPlayBtnClick(ActionEvent event) {
        // welcomeText.setText("Welcome " + nicknameField.getText());

        if (nicknameField.getText().isEmpty()) {
            welcomeText.setText("Insert a username");
            return;
        }

        if (clientState.getPlayerState() == PlayerState.INSERTING_USERNAME_FOR_RECONNECT) {
            String nickname = nicknameField.getText();
            clientState.setNickname(nickname);
            Action action = new ReconnectAction(nickname, clientState.getIdentity());
            controller.execute(action);
            return;
        }

        if (clientState.getLobbyModel() != null) {

            Set<Pawn> availablePawns = Arrays.stream(Pawn.values())
                    .filter(t -> !t.equals(Pawn.BLACK))
                    .collect(Collectors.toSet());

            availablePawns.removeAll(
                    clientState.getLobbyModel().getPlayers().stream()
                            .map(LobbyPlayer::getPlayerPawn)
                            .collect(Collectors.toSet())
            );

            List<Pawn> pawns = new ArrayList<>(availablePawns);

            clientState.setNickname(nicknameField.getText());
            Action action = new PlayerJoinAction(nicknameField.getText(), clientState.getIdentity(), clientState.getLobbyModel().getId(), pawns.getFirst());
            controller.execute(action);

            //clientState.setPlayerState(PlayerState.WAITING_FOR_PLAYERS);
            return;
        }

        List<Pawn> availablePawns = Arrays.stream(Pawn.values())
                .filter(t -> !t.equals(Pawn.BLACK))
                .toList();

        Action action = new CreateLobbyAction(nicknameField.getText(), clientState.getIdentity(), availablePawns.getFirst());
        controller.execute(action);

        //clientState.setPlayerState(PlayerState.ADMIN_WAITING_FOR_PLAYERS);

        //loadScene(event);

    }
}
