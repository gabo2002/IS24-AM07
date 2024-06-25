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

package it.polimi.ingsw.am07.action;

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;

/**
 * Interface for an action that just prints to STDOUT on execution and reflection.
 */
public class DebuggingAction extends Action {

    /**
     * Execute the action.
     *
     * @param gameModel the game model
     */
    @Override
    public void execute(Game gameModel) {
        System.out.println("GameAction executed in " + Thread.currentThread().getName());
    }

    /**
     * Execute the action.
     *
     * @param lobbyModel the lobby model
     */
    @Override
    public void execute(Lobby lobbyModel) {
        System.out.println("LobbyAction executed in " + Thread.currentThread().getName());
    }

    /**
     * Execute the action.
     *
     * @param matchmaking the matchmaking
     */
    @Override
    public void execute(Matchmaking matchmaking) {
        System.out.println("MatchmakingAction executed in " + Thread.currentThread().getName());
    }

    /**
     * Reflect the action.
     *
     * @param clientState the client state
     */
    @Override
    public void reflect(ClientState clientState) {
        System.out.println("LobbyAction reflected in " + Thread.currentThread().getName());
    }

}
