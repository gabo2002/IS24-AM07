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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.am07.exceptions.IllegalPlacementException;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PlayerPlaceCardActionTest {

    private Game gameMock;
    private ClientState clientStateMock;
    private Player playerMock;
    private Side sideMock;
    private GameFieldPosition positionMock;

    @BeforeEach
    void setUp() {
        gameMock = mock(Game.class);
        clientStateMock = mock(ClientState.class);
        playerMock = mock(Player.class);
        sideMock = mock(SideFrontRes.class);
        positionMock = mock(GameFieldPosition.class);
    }

    @Test
    void execute_success() throws Exception {
        PlayerPlaceCardAction action = new PlayerPlaceCardAction("player1", "id1", sideMock, positionMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(playerMock.getNickname()).thenReturn("player1");

        doNothing().when(playerMock).placeAt(sideMock, positionMock);

        action.execute(gameMock);

        verify(playerMock).placeAt(sideMock, positionMock);
        assertTrue(action.isExecutedCorrectly());
    }

    @Test
    void execute_failure() throws Exception {
        PlayerPlaceCardAction action = new PlayerPlaceCardAction("player1", "id1", sideMock, positionMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(playerMock.getNickname()).thenReturn("player1");
        doThrow(new IllegalPlacementException("Test error")).when(playerMock).placeAt(sideMock, positionMock);

        action.execute(gameMock);

        assertFalse(action.isExecutedCorrectly());
        assertEquals("Test error", action.getErrorMessage());
    }

    @Test
    void reflect_success() throws Exception {
        PlayerPlaceCardAction action = new PlayerPlaceCardAction("player1", "id1", sideMock, positionMock);
        action.setExecutedCorrectly(true);
        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(clientStateMock.getNickname()).thenReturn("player1");

        doNothing().when(playerMock).placeAt(sideMock, positionMock);

        action.reflect(clientStateMock);

        verify(clientStateMock).setPlayerState(PlayerState.PICKING_CARD);
        verify(playerMock).placeAt(sideMock, positionMock);
        verify(clientStateMock).notifyGameModelUpdate();
    }

    @Test
    void reflect_failure() {
        PlayerPlaceCardAction action = new PlayerPlaceCardAction("player1", "id1", sideMock, positionMock);
        action.setExecutedCorrectly(false);
        action.setErrorMessage("Test error");

        action.reflect(clientStateMock);

        verify(clientStateMock).setClientStringErrorMessage("Test error");
    }

    @Test
    void reflect_throwsException() throws Exception {
        PlayerPlaceCardAction action = new PlayerPlaceCardAction("player1", "id1", sideMock, positionMock);
        action.setExecutedCorrectly(true);
        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(clientStateMock.getNickname()).thenReturn("player1");

        doThrow(new RuntimeException("Test error")).when(playerMock).placeAt(sideMock, positionMock);

        assertThrows(RuntimeException.class, () -> action.reflect(clientStateMock));
    }
}