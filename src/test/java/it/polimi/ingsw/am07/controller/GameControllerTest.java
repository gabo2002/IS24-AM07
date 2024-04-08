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

package it.polimi.ingsw.am07.controller;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.player.PlayerPlaceCardAction;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.reactive.Dispatcher;
import it.polimi.ingsw.am07.reactive.LocalListener;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameControllerTest {

    @Test
    public void testAction() {
        GameCard card = GameResources.getInstance().getStarterCards().getFirst();

        Player player1 = new Player("a", Pawn.BLUE, card, null);
        Player player2 = new Player("b", Pawn.RED, card, null);

        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);

        Player player1_copy = new Player("a", Pawn.BLUE, card, null);
        Player player2_copy = new Player("b", Pawn.RED, card, null);

        List<Player> playerList_copy = new ArrayList<>();
        playerList_copy.add(player1_copy);
        playerList_copy.add(player2_copy);

        Player player1_copy2 = new Player("a", Pawn.BLUE, card, null);
        Player player2_copy2 = new Player("b", Pawn.RED, card, null);

        List<Player> playerList_copy2 = new ArrayList<>();
        playerList_copy2.add(player1_copy2);
        playerList_copy2.add(player2_copy2);

        Game game = new Game(playerList, null);
        Dispatcher controller = new GameController(game);

        Game localModel1 = new Game(playerList_copy, null);
        LocalListener localListener1 = new LocalListener(localModel1);

        Game localModel2 = new Game(playerList_copy2, null);
        LocalListener localListener2 = new LocalListener(localModel2);

        controller.registerNewListener(localListener1);
        controller.registerNewListener(localListener2);

        Action action = new PlayerPlaceCardAction(
                player1.getNickname(),
                player1.getStarterCard().front(),
                new GameFieldPosition(0, 0)
        );

        controller.execute(action);

        assertEquals(1, localModel1.getPlayers().getFirst().getPlacedCards().size());
        assertEquals(1, localModel2.getPlayers().getFirst().getPlacedCards().size());
    }

}