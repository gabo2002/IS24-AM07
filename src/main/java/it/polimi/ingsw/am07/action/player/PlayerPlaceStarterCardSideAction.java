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

package it.polimi.ingsw.am07.action.player;

import it.polimi.ingsw.am07.action.PlayerAction;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;

public class PlayerPlaceStarterCardSideAction extends PlayerAction {
    private final Side chosenSide;
    private final GameFieldPosition startPos = new GameFieldPosition(0, 0, 0);

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     */
    protected PlayerPlaceStarterCardSideAction(String playerNickname, String identity, Side chosenSide) {
        super(playerNickname, identity);
        this.chosenSide = chosenSide;
    }

    public boolean execute(Game gameModel) {
        try {
            getCorrespondingPlayer(gameModel).placeAt(chosenSide, startPos);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Reflect the action.
     *
     * @param gameModel the game model
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean reflect(Game gameModel) {
        return execute(gameModel);
    }
}
