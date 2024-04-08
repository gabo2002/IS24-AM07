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

/**
 * Action to place a card on the game field.
 */
public class PlayerPlaceCardAction extends PlayerAction {

    private final Side placedSide;
    private final GameFieldPosition position;

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     * @param side the side
     * @param position the position
     */
    public PlayerPlaceCardAction(String playerNickname, Side side, GameFieldPosition position) {
        super(playerNickname);

        this.placedSide = side;
        this.position = position;
    }

    /**
     * Execute the action.
     *
     * @param gameModel the game model
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute(Game gameModel) {
        try {
            getCorrespondingPlayer(gameModel).placeAt(placedSide, position);
        } catch (Exception e) {
            return false;
        }

        return false;
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
