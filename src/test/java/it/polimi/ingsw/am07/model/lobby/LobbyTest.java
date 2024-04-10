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

package it.polimi.ingsw.am07.model.lobby;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @Test
    void getPlayers() {
        assertNotNull(new Lobby().getPlayers());
    }

    @Test
    void getPlayerCount() {
        Lobby lobby = new Lobby();

        assertEquals(0, lobby.getPlayerCount());

        lobby.addNewPlayer("player1");
        assertEquals(1, lobby.getPlayerCount());

        lobby.addNewPlayer("player2");
        assertEquals(2, lobby.getPlayerCount());

        lobby.addNewPlayer("player3");
        assertEquals(3, lobby.getPlayerCount());
    }

    @Test
    void addNewPlayer() {
        Lobby lobby = new Lobby();

        assertDoesNotThrow(() -> lobby.addNewPlayer("player1"));

        assertEquals(1, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.addNewPlayer("player2"));

        assertEquals(2, lobby.getPlayerCount());

        assertThrows(IllegalArgumentException.class, () -> lobby.addNewPlayer("player1"));

        assertEquals(2, lobby.getPlayerCount());

        LobbyPlayer test = lobby.addNewPlayer("player3");

        assertEquals(3, lobby.getPlayerCount());
        assertEquals("player3", test.getNickname());
        assertEquals(test, lobby.getPlayers().get(2));

        assertDoesNotThrow(() -> lobby.addNewPlayer("player4"));
        assertEquals(4, lobby.getPlayerCount());
        assertTrue(lobby.isFull());

        assertThrows(IllegalStateException.class, () -> lobby.addNewPlayer("player5"));
        assertEquals(4, lobby.getPlayerCount());
        assertTrue(lobby.isFull());
    }

    @Test
    void removePlayer() {
        Lobby lobby = new Lobby();

        LobbyPlayer player1 = lobby.addNewPlayer("player1");
        LobbyPlayer player2 = lobby.addNewPlayer("player2");
        LobbyPlayer player3 = lobby.addNewPlayer("player3");
        LobbyPlayer player4 = lobby.addNewPlayer("player4");

        assertEquals(4, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.removePlayer("player1"));
        assertEquals(3, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.addNewPlayer("player1"));
        assertEquals(4, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.removePlayer("player1"));
        assertEquals(3, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.removePlayer("player2"));
        assertEquals(2, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.removePlayer("player3"));
        assertEquals(1, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.removePlayer("player4"));
        assertEquals(0, lobby.getPlayerCount());

        assertDoesNotThrow(() -> lobby.removePlayer("player1"));
        assertEquals(0, lobby.getPlayerCount());
    }

}