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

package it.polimi.ingsw.am07.action.server;

import it.polimi.ingsw.am07.action.ServerAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Player;

/**
 * Action to resume a game after the server has crashed.
 */
public class ResumeGameAction extends ServerAction {

    /**
     * The reference to the game model
     */
    private final Game game;

    /**
     * Whether all clients have reconnected and are ready to resume the game
     */
    private final boolean allClientsReady;

    /**
     * Constructor
     *
     * @param game the game model
     */
    public ResumeGameAction(Game game, boolean allClientsReady) {
        this.game = game;
        this.allClientsReady = allClientsReady;
    }

    /**
     * Execute the action
     *
     * @param gameModel the game model
     */
    @Override
    public void execute(Game gameModel) {
    }

    /**
     * Reflect the action on the client state
     *
     * @param state the client state
     */
    @Override
    public void reflect(ClientState state) {
        state.setGameModel(game);
        state.getGameModel().setSelfNickname(state.getNickname());

        if (allClientsReady) {
            // Find out whose turn it is
            String currentPlayer = game.getPlayers().get(game.getCurrentPlayerIndex()).getNickname();

            if (state.getNickname().equals(currentPlayer)) {
                // Find out if the player is in the middle of a turn
                int handSize = state.getGameModel().getSelf().getPlayableCards().size();

                if (handSize == Player.MAX_HAND_SIZE) { // Must place
                    state.setPlayerState(PlayerState.PLACING_CARD);
                } else { // Must pick
                    state.setPlayerState(PlayerState.PICKING_CARD);
                }
            } else {
                state.setPlayerState(PlayerState.SLEEPING);
            }
        } else {
            state.setPlayerState(PlayerState.SLEEPING);
        }

        state.notifyGameModelUpdate();
    }

}
