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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.am07.exceptions.CardNotFoundException;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HangGameActionTest {

    private Game gameMock;
    private ClientState clientStateMock;
    private Player playerMock;
    private Deck deckMock;
    private GameCard cardMock;

    @BeforeEach
    void setUp() {
        gameMock = mock(Game.class);
        clientStateMock = mock(ClientState.class);
        playerMock = mock(Player.class);
        deckMock = mock(Deck.class);
        cardMock = mock(GameCard.class);
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_fullHand() {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock, cardMock)); // Full hand

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock, never()).addPlayableCard(any());
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_notFullHand_resourceCard() {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(cardMock);

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock).addPlayableCard(cardMock);
        verify(deckMock).popRandomResCard();
        verify(deckMock, never()).popRandomGoldCard();
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_notFullHand_goldCard() {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(null);
        when(deckMock.popRandomGoldCard()).thenReturn(cardMock);

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock).addPlayableCard(cardMock);
        verify(deckMock).popRandomResCard();
        verify(deckMock).popRandomGoldCard();
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_notFullHand_visibleCards() throws Exception {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(null);
        when(deckMock.popRandomGoldCard()).thenReturn(null);
        when(deckMock.visibleResCards()).thenReturn(new GameCard[]{cardMock, null});

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock).addPlayableCard(cardMock);
        verify(deckMock).popRandomResCard();
        verify(deckMock).popRandomGoldCard();
        verify(deckMock).popCard(cardMock);
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_notFullHand_visibleCards2() throws Exception {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(null);
        when(deckMock.popRandomGoldCard()).thenReturn(null);
        when(deckMock.visibleResCards()).thenReturn(new GameCard[]{null, cardMock});

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock).addPlayableCard(cardMock);
        verify(deckMock).popRandomResCard();
        verify(deckMock).popRandomGoldCard();
        verify(deckMock).popCard(cardMock);
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_notFullHand_visibleCards3() throws Exception {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(null);
        when(deckMock.popRandomGoldCard()).thenReturn(null);
        when(deckMock.visibleResCards()).thenReturn(new GameCard[]{null, null});
        when(deckMock.visibleGoldCards()).thenReturn(new GameCard[]{cardMock, null});

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock).addPlayableCard(cardMock);
        verify(deckMock).popRandomResCard();
        verify(deckMock).popRandomGoldCard();
        verify(deckMock).popCard(cardMock);
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_notFullHand_visibleCards4() throws Exception {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(null);
        when(deckMock.popRandomGoldCard()).thenReturn(null);
        when(deckMock.visibleResCards()).thenReturn(new GameCard[]{null, null});
        when(deckMock.visibleGoldCards()).thenReturn(new GameCard[]{null, cardMock});

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock).addPlayableCard(cardMock);
        verify(deckMock).popRandomResCard();
        verify(deckMock).popRandomGoldCard();
        verify(deckMock).popCard(cardMock);

        doThrow(CardNotFoundException.class).when(deckMock).popCard(cardMock);

        assertThrows(RuntimeException.class, () -> action.execute(gameMock));
    }

    @Test
    void execute_disconnectedPlayerIsPlaying_notFullHand_noCardsAvailable() {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(null);
        when(deckMock.popRandomGoldCard()).thenReturn(null);
        when(deckMock.visibleResCards()).thenReturn(new GameCard[]{null, null});
        when(deckMock.visibleGoldCards()).thenReturn(new GameCard[]{null, null});

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock, never()).addPlayableCard(any());
    }

    @Test
    void execute_disconnectedPlayerIsPlaying() {
        HangGameAction action = new HangGameAction("player1");
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(gameMock.getDeck()).thenReturn(deckMock);
        when(playerMock.getPlayableCards()).thenReturn(List.of(cardMock, cardMock));
        when(deckMock.popRandomResCard()).thenReturn(cardMock);

        action.execute(gameMock);

        verify(gameMock).incrementTurn();
        verify(playerMock).addPlayableCard(cardMock);

        action.execute((Lobby) null);
        action.execute((Matchmaking) null);
    }

    @Test
    void execute_disconnectedPlayerIsNotPlaying() {
        HangGameAction action = new HangGameAction("player1");
        Player otherPlayerMock = mock(Player.class);
        when(gameMock.addDisconnectedPlayer("player1")).thenReturn(playerMock);
        when(gameMock.getPlayingPlayer()).thenReturn(otherPlayerMock);

        action.execute(gameMock);

        verify(gameMock, never()).incrementTurn();
        verify(playerMock, never()).addPlayableCard(any());
    }

    @Test
    void reflect_nullgame() {
        HangGameAction action = new HangGameAction("player1");
        when(clientStateMock.getIdentity()).thenReturn("player2");
        when(clientStateMock.getGameModel()).thenReturn(null);

        action.reflect(clientStateMock);

        verify(clientStateMock, never()).setPlayerState(any());
        verify(clientStateMock, times(1)).notifyGameModelUpdate();
    }

    @Test
    void reflect_disconnectedPlayer() {
        HangGameAction action = new HangGameAction("player1");
        when(clientStateMock.getIdentity()).thenReturn("player1");
        when(clientStateMock.getGameModel()).thenReturn(gameMock);

        action.reflect(clientStateMock);

        verify(clientStateMock).setPlayerState(PlayerState.DISCONNECTED);
        verify(clientStateMock).notifyGameModelUpdate();
    }

    @Test
    void reflect_otherPlayer() {
        HangGameAction action = new HangGameAction("player1");
        when(clientStateMock.getIdentity()).thenReturn("player2");
        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(playerMock.getNickname()).thenReturn("player2");
        when(gameMock.getSelf()).thenReturn(playerMock);
        Map<GameFieldPosition, Side> placedCards = new HashMap<>();
        placedCards.put(new GameFieldPosition(0, 0), mock(SideFrontRes.class));
        when(playerMock.getPlacedCards()).thenReturn(placedCards);

        when(clientStateMock.getPlayerState()).thenReturn(PlayerState.SLEEPING);

        action.reflect(clientStateMock);

        verify(clientStateMock).refreshPlayerState();
        verify(clientStateMock).notifyGameModelUpdate();
    }

    @Test
    void reflect_gameEnded() {
        HangGameAction action = new HangGameAction("player1");
        when(clientStateMock.getIdentity()).thenReturn("player2");
        when(clientStateMock.getPlayerState()).thenReturn(PlayerState.GAME_ENDED);
        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(playerMock.getNickname()).thenReturn("player2");

        action.reflect(clientStateMock);

        verify(clientStateMock, never()).refreshPlayerState();
    }
}