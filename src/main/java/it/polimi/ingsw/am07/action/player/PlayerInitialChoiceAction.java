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
import it.polimi.ingsw.am07.utils.logging.AppLogger;

/**
 * The PlayerInitialChoiceAction class represents the action of a player choosing an objective card and a starter side.
 */
public class PlayerInitialChoiceAction extends PlayerAction {

    /**
     * The logger.
     */
    private static final AppLogger logger = new AppLogger(PlayerInitialChoiceAction.class);
    /**
     * The chosen objective card.
     */
    private final ObjectiveCard selectedCard;

    /**
     * The chosen side of the starter card.
     */
    private final Side starterSide;
    /**
     * True if the clients should be notified that the game can start.
     */
    private boolean gameCanStart = false;

    /**
     * Constructor.
     *
     * @param playerNickname the player nickname
     * @param identity       the identity
     * @param selectedCard   the selected card
     * @param starterSide    the starter side
     */
    public PlayerInitialChoiceAction(String playerNickname, String identity, ObjectiveCard selectedCard, Side starterSide) {
        super(playerNickname, identity);
        this.selectedCard = selectedCard;
        this.starterSide = starterSide;
    }

    /**
     * Execute the action.
     *
     * @param gameModel the game model
     */
    @Override
    public void execute(Game gameModel) {
        //Getting the player
        Player correspondingPlayer = getCorrespondingPlayer(gameModel);

        //If the player is not in the game, the action is not valid
        if (correspondingPlayer == null) {
            executedCorrectly = false;
            logger.error("Player not found");
            return;
        }

        //If the player has already chosen an objective card, the action is not valid
        if (correspondingPlayer.getPlayerObjectiveCard() != null) {
            executedCorrectly = false;
            logger.error("Player has already chosen an objective card");
            return;
        }

        //Setting the player's objective card and starter side
        correspondingPlayer.setPlayerObjectiveCard(selectedCard);
        try {
            correspondingPlayer.setStarterCardSide(starterSide);
        } catch (IllegalPlacementException e) {
            executedCorrectly = false;
            return;
        }

        // If all players have chosen their objective card and starter side, the game can start
        if (gameModel.getPlayers().stream().allMatch(player -> player.getPlayerObjectiveCard() != null)) {
            gameCanStart = true;
        }

        executedCorrectly = true;
    }

    /**
     * Reflect the action.
     *
     * @param state the client state
     */
    @Override
    public void reflect(ClientState state) {
        //If the action was executed correctly and the player is the one who sent the action, the player is waiting for the game to start
        boolean amITheFirstPlayer = state.getGameModel().getPlayingPlayer().getNickname().equals(state.getNickname());

        if (gameCanStart && executedCorrectly) {
            state.setPlayerState(amITheFirstPlayer ? PlayerState.PLACING_CARD : PlayerState.SLEEPING);
            return;
        }

        if (executedCorrectly && state.getIdentity().equals(getIdentity())) {
            state.setPlayerState(PlayerState.WAITING_FOR_GAME_START);
        }
    }

    @Override
    public String toString() {
        return "PlayerInitialChoiceAction{" +
                "selectedCard=" + selectedCard +
                ", starterSide=" + starterSide +
                '}';
    }
}
