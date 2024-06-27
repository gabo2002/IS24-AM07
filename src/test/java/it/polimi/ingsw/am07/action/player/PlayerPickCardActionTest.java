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

import it.polimi.ingsw.am07.exceptions.CardNotFoundException;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.GameState;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.PatternObjectiveCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class PlayerPickCardActionTest {

    private Game gameMock;
    private ClientState clientStateMock;
    private Player playerMock;
    private GameCard cardMock;

    @BeforeEach
    void setUp() {
        gameMock = mock(Game.class);
        clientStateMock = mock(ClientState.class);
        playerMock = mock(Player.class);
        cardMock = mock(GameCard.class);
    }

    @Test
    void execute_success() throws Exception {
        PlayerPickCardAction action = new PlayerPickCardAction("player1", "id1", cardMock);
        when(playerMock.getNickname()).thenReturn("player1");
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);

        // mock void incrementTurn() method
        doNothing().when(gameMock).incrementTurn();
        doNothing().when(playerMock).addPlayableCard(cardMock);

        action.execute(gameMock);

        // Verify that popCard is called on the game model instead of addPlayableCard on the player
        verify(gameMock).popCard(cardMock);
        verify(gameMock).incrementTurn();
        assertTrue(action.isExecutedCorrectly());
    }

    @Test
    void execute_failure() throws Exception {
        PlayerPickCardAction action = new PlayerPickCardAction("player1", "id1", cardMock);
        when(playerMock.getNickname()).thenReturn("player1");
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);

        doThrow(new RuntimeException("Test error")).when(gameMock).popCard(cardMock);

        action.execute(gameMock);

        assertFalse(action.isExecutedCorrectly());
        assertEquals("Test error", action.getErrorMessage());
    }

    @Test
    void reflect_success() throws Exception {
        PlayerPickCardAction action = new PlayerPickCardAction("player1", "id1", cardMock);
        action.setExecutedCorrectly(true);

        // Setup a correct nextPlayer
        when(playerMock.getNickname()).thenReturn("player1");
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);

        // mock void incrementTurn() method
        doNothing().when(gameMock).incrementTurn();
        doNothing().when(playerMock).addPlayableCard(cardMock);

        action.execute(gameMock);

        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(clientStateMock.getNickname()).thenReturn("player2");
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(playerMock.getNickname()).thenReturn("player1");

        action.reflect(clientStateMock);

        verify(playerMock, times(2)).addPlayableCard(cardMock);
        verify(gameMock, times(2)).popCard(cardMock);
        verify(gameMock, times(2)).incrementTurn();
        verify(clientStateMock).notifyGameModelUpdate();

        ObjectiveCard mockObjective = mock(PatternObjectiveCard.class);

        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(clientStateMock.getGameModel().getGameState()).thenReturn(GameState.ENDED);
        when(clientStateMock.getGameModel().getCommonObjectives()).thenReturn(new ObjectiveCard[]{mockObjective, mockObjective});
        when(clientStateMock.getGameModel().getPlayers()).thenReturn(List.of(playerMock));
        when(playerMock.getPlayerObjectiveCard()).thenReturn(mockObjective);
        when(gameMock.getSelf()).thenReturn(playerMock);

        doNothing().when(playerMock).evaluateObjectiveScore(mockObjective);

        action.reflect(clientStateMock);
    }

    @Test
    void reflect_failure() throws CardNotFoundException {
        PlayerPickCardAction action = new PlayerPickCardAction("player1", "id1", cardMock);

        // Setup a correct nextPlayer
        when(playerMock.getNickname()).thenReturn("player1");
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);

        // mock void incrementTurn() method
        doNothing().when(gameMock).incrementTurn();
        doNothing().when(playerMock).addPlayableCard(cardMock);

        action.execute(gameMock);

        action.setExecutedCorrectly(false);

        action.setErrorMessage("Test error");

        action.reflect(clientStateMock);

        verify(clientStateMock).setClientStringErrorMessage("Test error");

        doThrow(CardNotFoundException.class).when(gameMock).popCard(cardMock);

        action.execute(gameMock);

        assertFalse(action.isExecutedCorrectly());

        action.setExecutedCorrectly(true);

        assertThrows(RuntimeException.class, () -> action.reflect(clientStateMock));
    }

}