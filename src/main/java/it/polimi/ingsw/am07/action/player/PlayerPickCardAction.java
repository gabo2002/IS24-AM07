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
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.card.GameCard;

/**
 * Action for a player to pick a card from the deck.
 */
public class PlayerPickCardAction extends PlayerAction {

    private final GameCard pickedCard;
    private String nextPlayer;

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     * @param pickedCard     the picked card
     */
    public PlayerPickCardAction(String playerNickname, String identity, GameCard pickedCard) {
        super(playerNickname, identity);
        this.pickedCard = pickedCard;
        nextPlayer = null;
    }

    /**
     * Execute the action.
     *
     * @param gameModel the game model
     * @return true if the action was successful, false otherwiseq
     */
    @Override
    public boolean execute(Game gameModel) {
        try {
            getCorrespondingPlayer(gameModel).addPlayableCard(pickedCard);
            gameModel.popCard(pickedCard);

            // I can increment the turn
            gameModel.incrementTurn();
            nextPlayer = gameModel.getPlayingPlayer().getNickname();
            executedCorrectly = true;
        } catch (Exception e) {
            executedCorrectly = false;
            super.setErrorMessage(e.getMessage());
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

    @Override
    public boolean reflect(ClientState state) {

        if (!executedCorrectly) {
            state.setClientStringErrorMessage(getErrorMessage());
            return true;
        }

        // Update the client state
        if (state.getNickname().equals(playerNickname)) {
            state.setPlayerState(PlayerState.SLEEPING);
            return false;
        } else if (nextPlayer.equals(state.getNickname())) {
            state.setPlayerState(PlayerState.PLACING_CARD);
            return false;
        }

        return false;
    }

}
