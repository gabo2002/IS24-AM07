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

package it.polimi.ingsw.am07.action.chat;

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.chat.ChatMessage;
import it.polimi.ingsw.am07.model.chat.PlayerChat;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.utils.assets.GameResources;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendMessageActionTest {

    @Test
    void execute() {
        // Setup
        List<ObjectiveCard> objectives = GameResources.getInstance().getObjectiveCards();

        ObjectiveCard[] objCards = new ObjectiveCard[2];
        objCards[0] = objectives.getFirst();
        objCards[1] = objectives.getLast();

        Player player1 = new Player("player1", "player1", Pawn.BLUE, null, objCards);
        Player player2 = new Player("player2", "player2", Pawn.RED, null, objCards);
        Player player3 = new Player("player3", "player3", Pawn.GREEN, null, objCards);

        Game game = new Game(List.of(player1, player2, player3), objCards);

        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");

        PlayerChat playerChat = new PlayerChat(players, "player1");

        ChatMessage message = playerChat.sendBroadcastMessage("antonio");

        SendMessageAction action = new SendMessageAction("player1", "antonio", message);

        ClientState clientState = new ClientState((ClientState clientState1) -> {}, "player1");

        action.execute(game);

        Player player1Copy = new Player("player1", "player1", Pawn.BLUE, null, objCards);
        Player player2Copy = new Player("player2", "player2", Pawn.RED, null, objCards);
        Player player3Copy = new Player("player3", "player3", Pawn.GREEN, null, objCards);
        Game gameCopy = new Game(List.of(player1Copy, player2Copy, player3Copy), objCards);
        clientState.setGameModel(gameCopy);

        // Verify that the message is sent to all players
        assertEquals(1, player1.getChat().getMessages().size());
        assertEquals(1, player2.getChat().getMessages().size());
        assertEquals(1, player3.getChat().getMessages().size());

        assertEquals("player1", player1.getChat().getMessages().get(0).senderNickname());
        assertEquals("player1", player2.getChat().getMessages().get(0).senderNickname());
        assertEquals("player1", player3.getChat().getMessages().get(0).senderNickname());

        // Verify that the reflection does the same thing
        action.reflect(clientState);

        assertEquals(1, clientState.getGameModel().getPlayers().get(0).getChat().getMessages().size());
        assertEquals(1, clientState.getGameModel().getPlayers().get(1).getChat().getMessages().size());
        assertEquals(1, clientState.getGameModel().getPlayers().get(2).getChat().getMessages().size());
    }

}