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

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResumeGameActionTest {

    @Test
    void execute() {
        // Setup
        List<ObjectiveCard> objectives = GameResources.getInstance().getObjectiveCards();

        ObjectiveCard[] objCards = new ObjectiveCard[2];
        objCards[0] = objectives.getFirst();
        objCards[1] = objectives.getLast();

        Player player1 = new Player("player1", "player1", Pawn.BLUE, null, objCards);
        Player player2 = new Player("player2", "player2", Pawn.RED, null, objCards);

        Game game = new Game(List.of(player1, player2), objCards);

        ResumeGameAction resumeGameAction = new ResumeGameAction(game, false);

        // Verify the empty execute does not throw an exception
        assertDoesNotThrow(() -> resumeGameAction.execute(game));

        ClientState clientState = new ClientState((ClientState clientState1) -> {}, "player1");

        clientState.setNickname("player1");

        // Verify that the reflection sets the game
        resumeGameAction.reflect(clientState);

        assertEquals(game.getId(), clientState.getGameModel().getId());
        assertEquals(player1, clientState.getGameModel().getSelf());
        assertEquals(PlayerState.SLEEPING, clientState.getPlayerState());

        ClientState clientState2 = new ClientState((ClientState clientState1) -> {}, "player2");
        clientState2.setNickname("player2");

        ResumeGameAction resumeGameAction2 = new ResumeGameAction(game, true);

        // Verify the empty execute does not throw an exception
        assertDoesNotThrow(() -> resumeGameAction2.execute(game));

        // Verify that the reflection sets the correct states
        resumeGameAction2.reflect(clientState);
        resumeGameAction2.reflect(clientState2);

        assertEquals(PlayerState.PICKING_CARD, clientState.getPlayerState());
        assertEquals(PlayerState.SLEEPING, clientState2.getPlayerState());

        // Verify that the reflection sets the correct states when the player has a full hand
        List<GameCard> playableCards = GameResources.getInstance().getGoldCards();
        player1.addPlayableCard(playableCards.get(0));
        player1.addPlayableCard(playableCards.get(1));
        player1.addPlayableCard(playableCards.get(2));

        resumeGameAction2.reflect(clientState);

        assertEquals(PlayerState.PLACING_CARD, clientState.getPlayerState());
    }

}