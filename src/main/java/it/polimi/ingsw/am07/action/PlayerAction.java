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

import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Player;

/**
 * Abstract class for an action initiated by a player.
 */
public abstract class PlayerAction extends Action {

    protected String playerNickname;

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     */
    protected PlayerAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    /**
     * Get the corresponding player.
     *
     * @param gameModel the game model
     * @return the player corresponding to the nickname
     */
    protected Player getCorrespondingPlayer(Game gameModel) {
        for (Player player : gameModel.getPlayers()) {
            if (player.getNickname().equals(playerNickname)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Get the identity of the action.
     *
     * @return the identity of the action
     */
    @Override
    public String getIdentity() {
        return playerNickname;
    }

    /**
     * Set the identity of the action.
     */
    @Override
    public void setIdentity(String identity) {
        if (!playerNickname.equals(identity)) {
            throw new IllegalArgumentException("The identity of a PlayerAction cannot be changed");
        }
    }

}
