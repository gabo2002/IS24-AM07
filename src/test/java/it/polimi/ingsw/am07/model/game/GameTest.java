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

package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.exceptions.IllegalPlacementException;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.*;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.lobby.LobbyPlayer;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void validateSerializability() {
        Player player = new Player("player1", Pawn.BLACK, null, null);

        List<Player> players = new ArrayList<>();
        players.add(player);

        Game game = new Game(players, null);

        assertDoesNotThrow(() -> {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(game);

            oos.close();
            baos.close();
        });
    }

    @Test
    void incrementTurnTest() {

        // setting up the players
        Player player1 = new Player("player1", Pawn.BLACK, null, null);
        Player player2 = new Player("player2", Pawn.RED, null, null);
        Player player3 = new Player("player3", Pawn.GREEN, null, null);
        Player player4 = new Player("player4", Pawn.BLUE, null, null);


        // adding the players
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        // setting up game
        Game game = new Game(players, null);

        // crafting a custom card with 20 points to skip
        // crafting starter card
        Matrix<Symbol> corners = new Matrix<>(2, 2, Symbol.STARTER);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);
        ResourceHolder resources = new ResourceHolder(sideFieldRepresentation);

        SideFront starter = new SideFrontStarter(0, sideFieldRepresentation, resources);

        SideFront sideFront = new SideFrontGold(
                1, sideFieldRepresentation, resources, 20, Symbol.NONE, null, Symbol.RED);

        // checking a whole turn by everybody
        assertEquals(GameState.STARTING, game.getGameState());
        assertEquals(0, game.getCurrentPlayerIndex());

        assertDoesNotThrow(() ->  player1.placeAt(starter, new GameFieldPosition(0, 0)));
        assertEquals(0, player1.getPlayerScore());

        game.incrementTurn();

        assertDoesNotThrow(() -> player2.placeAt(starter, new GameFieldPosition(0, 0)));

        assertEquals(GameState.PLAYING, game.getGameState());
        assertEquals(1, game.getCurrentPlayerIndex());

        game.incrementTurn();

        assertDoesNotThrow(() -> player3.placeAt(starter, new GameFieldPosition(0, 0)));
        assertEquals(GameState.PLAYING, game.getGameState());
        assertEquals(2, game.getCurrentPlayerIndex());

        game.incrementTurn();

        assertDoesNotThrow(() -> player4.placeAt(starter, new GameFieldPosition(0, 0)));
        assertEquals(GameState.PLAYING, game.getGameState());
        assertEquals(3, game.getCurrentPlayerIndex());

        game.incrementTurn();

        // player 1
        player1.addPlayableCard(new GameCard(sideFront, new SideBack(1, null, null, null)));
        assertDoesNotThrow(() -> player1.placeAt(sideFront, new GameFieldPosition(1, 1, 1)));
        assertEquals(20, player1.getPlayerScore());

        game.incrementTurn();

        // player 2
        assertEquals(GameState.PLAYING, game.getGameState());
        assertEquals(1, game.getCurrentPlayerIndex());

        player2.addPlayableCard(new GameCard(sideFront, new SideBack(1, null, null, null)));
        assertDoesNotThrow(() -> player2.placeAt(sideFront, new GameFieldPosition(1, 1, 1)));
        assertEquals(20, player2.getPlayerScore());

        game.incrementTurn();

        // player 3
        assertEquals(GameState.PLAYING, game.getGameState());

        player3.addPlayableCard(new GameCard(sideFront, new SideBack(1, null, null, null)));
        assertDoesNotThrow(() -> player3.placeAt(sideFront, new GameFieldPosition(1, 1, 1)));
        assertEquals(20, player3.getPlayerScore());


        game.incrementTurn();

        // player 4
        player4.addPlayableCard(new GameCard(sideFront, new SideBack(1, null, null, null)));
        assertDoesNotThrow(() -> player4.placeAt(sideFront, new GameFieldPosition(1, 1, 1)));
        assertEquals(20, player4.getPlayerScore());

        game.incrementTurn();

        // back to player 1
        // I have all players at 20 points now should be ending
        assertEquals(GameState.ENDING, game.getGameState());

        game.incrementTurn();

        // player 2
        assertEquals(GameState.ENDING, game.getGameState());
        game.incrementTurn();

        // player 3
        assertEquals(GameState.ENDING, game.getGameState());
        game.incrementTurn();

        // player 4
        assertEquals(3, game.getCurrentPlayerIndex());
        assertEquals(GameState.ENDING, game.getGameState());
        game.incrementTurn();

        // now back to player1 but the game is ended
        assertEquals(0, game.getCurrentPlayerIndex());
        assertEquals(GameState.ENDED, game.getGameState());

        // it should remain at player1 since the game is ended
        game.incrementTurn();
        assertEquals(0, game.getCurrentPlayerIndex());
        assertEquals(GameState.ENDED, game.getGameState());


    }

    @Test
    void pickRandomResCardTest() {
        Game game = new Game(new ArrayList<>(), null);
        int size = game.getAvailableResCardsSize();

        game.pickRandomResCard();

        assertEquals(size, game.getAvailableResCardsSize());
    }

    @Test
    void pickRandomGoldCardTest() {
        Game game = new Game(new ArrayList<>(), null);
        int size = game.getAvailableGoldCardsSize();

        game.pickRandomGoldCard();

        assertEquals(size, game.getAvailableGoldCardsSize());
    }

    @Test
    void popCardTest() {
        Game game = new Game(new ArrayList<>(), null);
        int size = game.getAvailableResCardsSize();
        GameCard gameCard = new GameCard(new SideFrontRes(1, null, null, null), new SideBack(1, null, null, null));

        assertDoesNotThrow(() -> game.popCard(gameCard));

        assertEquals(size - 1, game.getAvailableResCardsSize());
    }

    @Test
    void factoryTest() {
        Lobby lobby = new Lobby();
        LobbyPlayer a = lobby.addNewPlayer("player1", "player1", Pawn.YELLOW);

        Game game = new Game.Factory()
                .fromLobby(lobby)
                .build();

        assertEquals(1, game.getPlayers().size());
    }

}