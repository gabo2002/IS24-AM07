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

import it.polimi.ingsw.am07.model.game.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyPlayerTest {

    @Test
    void getNickname() {
        String nickname = "nickname";

        LobbyPlayer lobbyPlayer = new LobbyPlayer(nickname);

        assertEquals(nickname, lobbyPlayer.getNickname());
    }

    @Test
    void getSetPlayerPawn() {
        LobbyPlayer lobbyPlayer = new LobbyPlayer("nickname");

        assertNull(lobbyPlayer.getPlayerPawn());

        Pawn pawn = Pawn.BLUE;

        lobbyPlayer.setPlayerPawn(pawn);

        assertEquals(pawn, lobbyPlayer.getPlayerPawn());
    }

    @Test
    void testEquals() {
        LobbyPlayer lobbyPlayer1 = new LobbyPlayer("nickname");
        LobbyPlayer lobbyPlayer2 = new LobbyPlayer("nickname");

        assertEquals(lobbyPlayer1, lobbyPlayer2);

        lobbyPlayer1.setPlayerPawn(Pawn.BLUE);

        assertEquals(lobbyPlayer1, lobbyPlayer2);

        LobbyPlayer lobbyPlayer3 = new LobbyPlayer("nickname2");

        assertNotEquals(lobbyPlayer1, lobbyPlayer3);

        assertNotEquals(lobbyPlayer1, null);
        assertNotEquals(lobbyPlayer1, new Object());
    }

    @Test
    void hashTest() {
        // Two lobby players with the same nickname should have the same hash code
        LobbyPlayer lobbyPlayer1 = new LobbyPlayer("nickname");
        LobbyPlayer lobbyPlayer2 = new LobbyPlayer("nickname");

        assertEquals(lobbyPlayer1.hashCode(), lobbyPlayer2.hashCode());

        // Two lobby players with different nicknames should have different hash codes
        LobbyPlayer lobbyPlayer3 = new LobbyPlayer("nickname2");

        assertNotEquals(lobbyPlayer1.hashCode(), lobbyPlayer3.hashCode());
    }

}