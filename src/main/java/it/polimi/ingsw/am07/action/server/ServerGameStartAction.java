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

/**
 * Action to notify clients that the game has started
 * Spawned by the server on migration from lobby to game
 */
public class ServerGameStartAction extends ServerAction {

    /**
     * The reference to the game model
     */
    private final Game game;

    /**
     * Constructor
     *
     * @param game the game model
     */
    public ServerGameStartAction(Game game) {
        this.game = game;
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
        state.setPlayerState(PlayerState.SELECTING_STARTER_CARD_SIDE);
        state.notifyGameModelUpdate();
    }

}
