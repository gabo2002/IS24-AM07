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
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerGameStartActionTest {

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

        ClientState clientState = new ClientState((ClientState cs) -> {}, "player1");
        clientState.setNickname("player1");

        ServerGameStartAction serverGameStartAction = new ServerGameStartAction(game);

        assertDoesNotThrow(() -> serverGameStartAction.execute(game));

        serverGameStartAction.reflect(clientState);

        assertEquals(game.getId(), clientState.getGameModel().getId());
        assertEquals(player1, clientState.getGameModel().getSelf());
        assertEquals(PlayerState.SELECTING_STARTER_CARD_SIDE, clientState.getPlayerState());
    }

}