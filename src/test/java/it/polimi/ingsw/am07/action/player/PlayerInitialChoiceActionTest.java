package it.polimi.ingsw.am07.action.player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.am07.exceptions.IllegalGamePositionException;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.PatternObjectiveCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PlayerInitialChoiceActionTest {

    private Game gameMock;
    private ClientState clientStateMock;
    private Player playerMock;
    private ObjectiveCard objectiveCardMock;
    private Side sideMock;

    @BeforeEach
    void setUp() {
        gameMock = mock(Game.class);
        clientStateMock = mock(ClientState.class);
        playerMock = mock(Player.class);
        objectiveCardMock = mock(PatternObjectiveCard.class);
        sideMock = mock(SideFrontRes.class);
    }

    @Test
    void execute_success() throws Exception {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(playerMock.getPlayerObjectiveCard()).thenReturn(null);
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(playerMock.getNickname()).thenReturn("player1");

        doNothing().when(playerMock).setPlayerObjectiveCard(objectiveCardMock);
        doNothing().when(playerMock).placeAt(sideMock, new GameFieldPosition(0, 0));

        action.execute(gameMock);

        verify(playerMock).setPlayerObjectiveCard(objectiveCardMock);
        verify(playerMock).placeAt(sideMock, new GameFieldPosition(0, 0));
        assertTrue(action.isExecutedCorrectly());
    }

    @Test
    void execute_playerNotFound() {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(null);

        action.execute(gameMock);

        assertFalse(action.isExecutedCorrectly());
    }

    @Test
    void execute_alreadyplayed() {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(playerMock.getPlayerObjectiveCard()).thenReturn(mock(PatternObjectiveCard.class));
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(playerMock.getNickname()).thenReturn("player1");

        action.execute(gameMock);

        assertFalse(action.isExecutedCorrectly());
    }

    @Test
    void execute_objectiveCardAlreadyChosen() {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(playerMock.getPlayerObjectiveCard()).thenReturn(mock(PatternObjectiveCard.class));

        action.execute(gameMock);

        assertFalse(action.isExecutedCorrectly());
    }

    @Test
    void reflect_success() throws Exception {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        action.setExecutedCorrectly(true);
        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(clientStateMock.getIdentity()).thenReturn("id1");
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(playerMock.getPlayerObjectiveCard()).thenReturn(objectiveCardMock);
        when(clientStateMock.getNickname()).thenReturn("player1");
        when(playerMock.getNickname()).thenReturn("player1");

        doNothing().when(playerMock).placeAt(sideMock, new GameFieldPosition(0, 0));
        doNothing().when(playerMock).setPlayerObjectiveCard(objectiveCardMock);

        action.reflect(clientStateMock);

        verify(playerMock).placeAt(sideMock, new GameFieldPosition(0, 0));
        verify(playerMock).setPlayerObjectiveCard(objectiveCardMock);
        verify(clientStateMock).setPlayerState(PlayerState.WAITING_FOR_GAME_START);
        verify(clientStateMock).notifyGameModelUpdate();
    }

    @Test
    void reflect_gameCanStart() throws Exception {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        action.setExecutedCorrectly(true);
        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(clientStateMock.getIdentity()).thenReturn("id1");
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(clientStateMock.getNickname()).thenReturn("player1");
        when(playerMock.getNickname()).thenReturn("player1");

        when(playerMock.getPlayerObjectiveCard()).thenCallRealMethod();
        doCallRealMethod().when(playerMock).setPlayerObjectiveCard(any());

        doNothing().when(playerMock).placeAt(sideMock, new GameFieldPosition(0, 0));

        action.execute(gameMock);

        action.reflect(clientStateMock);

        verify(clientStateMock).setPlayerState(PlayerState.PLACING_CARD);

        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(playerMock.getNickname()).thenReturn("player1");
        doThrow(IllegalGamePositionException.class).when(playerMock).placeAt(any(), any());
        when(playerMock.getPlayerObjectiveCard()).thenReturn(null);
        assertThrows(RuntimeException.class, () -> action.execute(gameMock));
        action.setExecutedCorrectly(true);
        assertThrows(RuntimeException.class, () -> action.reflect(clientStateMock));
    }

    @Test
    void reflect_gameCanStart_2() throws Exception {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        action.setExecutedCorrectly(true);
        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);
        when(clientStateMock.getIdentity()).thenReturn("id1");
        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(clientStateMock.getNickname()).thenReturn("player1");
        when(playerMock.getNickname()).thenReturn("player1");

        when(playerMock.getPlayerObjectiveCard()).thenCallRealMethod();
        doCallRealMethod().when(playerMock).setPlayerObjectiveCard(any());

        doNothing().when(playerMock).placeAt(sideMock, new GameFieldPosition(0, 0));

        action.execute(gameMock);

        action.setExecutedCorrectly(false);

        clientStateMock.setPlayerState(PlayerState.PLACING_CARD);
        action.reflect(clientStateMock);

        verify(clientStateMock).setPlayerState(PlayerState.PLACING_CARD);

        when(gameMock.getPlayers()).thenReturn(List.of(playerMock));
        when(gameMock.getPlayingPlayer()).thenReturn(playerMock);
        when(playerMock.getNickname()).thenReturn("player1");
        doThrow(IllegalGamePositionException.class).when(playerMock).placeAt(any(), any());
        when(playerMock.getPlayerObjectiveCard()).thenReturn(null);
        assertThrows(RuntimeException.class, () -> action.execute(gameMock));
        action.setExecutedCorrectly(true);
        assertThrows(RuntimeException.class, () -> action.reflect(clientStateMock));
    }

    @Test
    void reflect_failure() throws Exception {
        PlayerInitialChoiceAction action = new PlayerInitialChoiceAction("player1", "id1", objectiveCardMock, sideMock);
        action.setExecutedCorrectly(false);

        when(clientStateMock.getGameModel()).thenReturn(gameMock);
        when(gameMock.getPlayerByNickname("player1")).thenReturn(playerMock);

        doThrow(new RuntimeException("Test error")).when(playerMock).placeAt(any(), any());

        assertThrows(RuntimeException.class, () -> action.reflect(clientStateMock));
    }

}