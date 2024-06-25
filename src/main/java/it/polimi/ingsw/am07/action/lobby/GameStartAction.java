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

package it.polimi.ingsw.am07.action.lobby;

import it.polimi.ingsw.am07.action.PlayerAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.lobby.Lobby;

/**
 * This action can be sent by the lobby creator to start the game.
 */
public class GameStartAction extends PlayerAction {

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     */
    public GameStartAction(String playerNickname, String identity) {
        super(playerNickname, identity);
    }

    /**
     * Execute the action. Only the first player is able to start the lobby
     *
     * @param lobbyModel the lobby model
     */
    @Override
    public void execute(Lobby lobbyModel) {
        if (!getIdentity().equals(lobbyModel.getFirstPlayer().getIdentity())) {
            executedCorrectly = false;
            super.setErrorMessage("Only the first player can start the game");
            return;
        }

        try {
            lobbyModel.startGame();
            executedCorrectly = true;
        } catch (IllegalStateException e) {
            executedCorrectly = false;
            super.setErrorMessage(e.getMessage());
        }
    }

    /**
     * Reflect the action.
     *
     * @param state the ClientState
     */
    @Override
    public void reflect(ClientState state) {
        if (state.getIdentity().equals(getIdentity()) && !executedCorrectly) {
            state.setClientStringErrorMessage(getErrorMessage());
            state.notifyGameModelUpdate();
        }
    }

}
