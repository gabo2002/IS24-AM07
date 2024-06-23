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

package it.polimi.ingsw.am07.model;

import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ClientStateTest {

    @Test
    void gameModelInsertionTest() {
        ClientState clientState = new ClientState(null, "");
        Game game = new Game(List.of(), null);
        game.setSelfNickname("test");
        assertEquals("test", game.getSelfNickname());

        clientState.setGameModel(game);
        assertEquals(game.getGameState(), clientState.getGameModel().getGameState());
        assertNull(clientState.getGameModel().getSelfNickname());

        clientState.setNickname("provola");
        assertNull(clientState.getGameModel().getSelfNickname());

        clientState.setGameModel(game);
        assertEquals(game.getGameState(), clientState.getGameModel().getGameState());
        assertEquals("test", game.getSelfNickname());
        assertEquals("provola", clientState.getGameModel().getSelfNickname());

        clientState.setNickname("ciao");
        assertEquals("ciao", clientState.getNickname());
        assertEquals("test", game.getSelfNickname());
        assertEquals("provola", clientState.getGameModel().getSelfNickname());
    }

    @Test
    void notifyTest() {
        ClientState clientState = new ClientState(null, "");

        assertDoesNotThrow(clientState::notifyGameModelUpdate);

        boolean[] called = {false};

        Consumer<ClientState> consumer = (c) -> {
            called[0] = true;
        };

        clientState = new ClientState(consumer, "");

        clientState.notifyGameModelUpdate();

        assertTrue(called[0]);
    }

    @Test
    void playerStateTest() {
        ClientState clientState = new ClientState(null, "");

        assertEquals(PlayerState.SELECTING_LOBBY, clientState.getPlayerState());

        clientState.setPlayerState(PlayerState.PICKING_CARD);
        assertEquals(PlayerState.PICKING_CARD, clientState.getPlayerState());
    }

    @Test
    void propertyTest() {
        ClientState clientState = new ClientState(null, "test");
        assertEquals("test", clientState.getIdentity());
        clientState = new ClientState(null, null);
        assertNull(clientState.getIdentity());

        assertNull(clientState.getClientStringErrorMessage());
        clientState.setClientStringErrorMessage("test");
        assertEquals("test", clientState.getClientStringErrorMessage());
        assertNull(clientState.getClientStringErrorMessage());

        List<Lobby> lobbies = List.of(new Lobby());
        assertNotNull(clientState.getAvailableLobbies());
        assertEquals(0, clientState.getAvailableLobbies().size());
        clientState.setLobbies(lobbies);
        assertEquals(lobbies, clientState.getAvailableLobbies());
    }

}