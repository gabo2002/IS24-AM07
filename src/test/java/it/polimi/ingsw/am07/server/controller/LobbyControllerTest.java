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

package it.polimi.ingsw.am07.server.controller;

import it.polimi.ingsw.am07.action.DebuggingAction;
import it.polimi.ingsw.am07.action.lobby.GameStartAction;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.reactive.MockListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyControllerTest {

    private boolean called = false;

    @Test
    void execute() {
        Lobby lobby = new Lobby();
        LobbyController lobbyController = new LobbyController(lobby, (l) -> {
            assertEquals(3, lobby.getPlayerCount());
            called = true;
        });

        MockListener mockListener = new MockListener();

        lobbyController.registerNewListener(mockListener);

        assertEquals(1, mockListener.getCalled().size());

        lobbyController.execute(new DebuggingAction());

        assertEquals(2, mockListener.getCalled().size());

        lobbyController.execute(new DebuggingAction());

        assertEquals(3, mockListener.getCalled().size());

        MockListener mockListener2 = new MockListener();

        lobbyController.registerNewListener(mockListener2);

        assertEquals(4, mockListener.getCalled().size());
        assertEquals(1, mockListener2.getCalled().size());

        lobbyController.execute(new DebuggingAction());

        assertEquals(5, mockListener.getCalled().size());
        assertEquals(2, mockListener2.getCalled().size());

        lobbyController.removeListener(mockListener);

        assertEquals(5, mockListener.getCalled().size());
        assertEquals(3, mockListener2.getCalled().size());

        assertDoesNotThrow(() -> {
            lobbyController.removeListener(mockListener);
        });

        assertEquals(5, mockListener.getCalled().size());
        assertEquals(4, mockListener2.getCalled().size());

        lobby.addNewPlayer("player1");
        lobby.addNewPlayer("player2");
        lobby.addNewPlayer("player3");

        lobbyController.execute(new GameStartAction("player1"));

        assertEquals(5, mockListener.getCalled().size());

        assertTrue(called);
    }

}