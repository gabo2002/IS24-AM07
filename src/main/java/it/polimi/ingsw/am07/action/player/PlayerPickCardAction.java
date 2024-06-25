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
import it.polimi.ingsw.am07.model.game.GameState;
import it.polimi.ingsw.am07.model.game.card.GameCard;

/**
 * Action for a player to pick a card from the deck.
 */
public class PlayerPickCardAction extends PlayerAction {

    /**
     * The picked card.
     */
    private final GameCard pickedCard;

    /**
     * The next player's nickname.
     */
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
     */
    @Override
    public void execute(Game gameModel) {
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
        }
    }

    /**
     * Reflect the action on the client state.
     *
     * @param state the client state
     */
    @Override
    public void reflect(ClientState state) {
        if (!executedCorrectly) {
            state.setClientStringErrorMessage(getErrorMessage());
            return;
        }

        try {
            state.getGameModel().getPlayerByNickname(playerNickname).addPlayableCard(pickedCard);
            state.getGameModel().popCard(pickedCard);
            state.getGameModel().incrementTurn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Update the client state
        if (state.getGameModel().getGameState() == GameState.ENDED) {
            state.setPlayerState(PlayerState.GAME_ENDED);
        } else if (state.getNickname().equals(playerNickname)) {
            state.setPlayerState(PlayerState.SLEEPING);
        } else if (nextPlayer.equals(state.getNickname())) {
            state.setPlayerState(PlayerState.PLACING_CARD);
        }

        state.notifyGameModelUpdate();
    }

}
