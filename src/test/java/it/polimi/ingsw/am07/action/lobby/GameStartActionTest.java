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

package it.polimi.ingsw.am07.action.lobby;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.reactive.MockListener;
import it.polimi.ingsw.am07.server.controller.LobbyController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStartActionTest {

    @Test
    void test() {
        Lobby lobby = new Lobby();
        LobbyController lobbyController = new LobbyController(lobby, (l) -> {
        });

        MockListener listener = new MockListener();

        lobbyController.registerNewListener(listener);
        assertEquals(1, listener.getCalled().size());

        lobby.addNewPlayer("player1");
        lobby.addNewPlayer("player2");
        lobby.addNewPlayer("player3");

        Action action = new GameStartAction("player1");
        lobbyController.execute(action);
        assertTrue(lobby.readyToStart());

        lobby.removePlayer("player3");
        assertFalse(lobby.readyToStart());

        action = new GameStartAction("player2");
        lobbyController.execute(action);
        assertFalse(lobby.readyToStart());

        action = new GameStartAction("player1");
        lobbyController.execute(action);
        assertTrue(lobby.readyToStart());

        lobby.removePlayer("player2");
        assertFalse(lobby.readyToStart());

        action = new GameStartAction("player1");
        lobbyController.execute(action);
        assertFalse(lobby.readyToStart());
    }

}