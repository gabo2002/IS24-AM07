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
import it.polimi.ingsw.am07.exceptions.IllegalPlacementException;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.side.Side;

public class PlayerInitialChoiceAction extends PlayerAction {

    private ObjectiveCard selectedCard;
    private Side starterSide;

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     * @param identity      the identity
     * @param selectedCard  the selected card
     *  @param starterSide the starter side
     */
    public PlayerInitialChoiceAction(String playerNickname, String identity, ObjectiveCard selectedCard, Side starterSide) {
        super(playerNickname, identity);
        this.selectedCard = selectedCard;
        this.starterSide = starterSide;
    }

    @Override
    public boolean execute(Game gameModel) {
        //Getting the player
        Player correspondingPlayer = gameModel.getPlayers().stream().filter(player -> player.getNickname().equals(playerNickname)).findFirst().orElse(null);

        //If the player is not in the game, the action is not valid
        if (correspondingPlayer == null) {
            executedCorrectly = false;
            System.out.println("Player not found");
            return true;
        }
        //If the player has already chosen an objective card, the action is not valid
        if (correspondingPlayer.getPlayerObjectiveCard() != null) {
            executedCorrectly = false;
            return true;
        }

        //Setting the player's objective card and starter side
        correspondingPlayer.setPlayerObjectiveCard(selectedCard);
        try {
            correspondingPlayer.setStarterCardSide(starterSide);
        } catch (IllegalPlacementException e) {
            executedCorrectly = false;
            return true;
        }
        executedCorrectly = true;
        return false;
    }

    @Override
    public boolean reflect(ClientState state) {
        //getting the player
        Player correspondingPlayer = state.getGameModel().getPlayers().stream().filter(player -> player.getNickname().equals(playerNickname)).findFirst().orElse(null);

        if (correspondingPlayer == null) {
            return true;
        }

        if (executedCorrectly && state.getIdentity().equals(getIdentity())) {
            state.setPlayerState(PlayerState.WAITING_FOR_GAME_START);
        }

        return false;
    }

    @Override
    public String toString() {
        return "PlayerInitialChoiceAction{" +
                "selectedCard=" + selectedCard +
                ", starterSide=" + starterSide +
                '}';
    }
}
