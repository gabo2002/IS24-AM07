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

package it.polimi.ingsw.am07.network;

import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.controller.GameController;
import it.polimi.ingsw.am07.model.game.*;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFieldRepresentation;
import it.polimi.ingsw.am07.model.game.side.SideFrontStarter;
import it.polimi.ingsw.am07.network.rmi.ServerRMINetworkManager;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RMINetworkTest {

    private static final int PORT = 23456;

    private final List<AssertionFailedError> exceptions = new ArrayList<>();

    @Test
    void testConnection() {
        Thread serverThread = new Thread(this::serverThreadFunction);
        Thread client1Thread = new Thread(this::client1ThreadFunction);
        Thread client2Thread = new Thread(this::client2ThreadFunction);

        serverThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client1Thread.start();
        client2Thread.start();

        try {
            serverThread.join();
            client1Thread.join();
            client2Thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (exceptions) {
            for (AssertionFailedError e : exceptions) {
                throw e;
            }
        }
    }

    private void serverThreadFunction() {
        Player player1 = new Player("Player1", Pawn.BLUE, null, null);
        Player player2 = new Player("Player2", Pawn.RED, null, null);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        Game game = new Game(players, null);

        GameController controller = new GameController(game);

        ServerNetworkManager server = new ServerRMINetworkManager(PORT, controller);

        server.start();

        synchronized (controller) {
            try {
                controller.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            assertEquals(1, player1.getPlayableCards().size());
        } catch (AssertionFailedError e) {
            synchronized (exceptions) {
                exceptions.add(e);
            }
        }
    }

    private void client1ThreadFunction() {
        Player player1 = new Player("Player1", Pawn.BLUE, null, null);
        Player player2 = new Player("Player2", Pawn.RED, null, null);

        GameCard card = new GameCard(
                new SideFrontStarter(1, new SideFieldRepresentation(new Matrix<>(2, 2)), new ResourceHolder()),
                new SideBack(1, new SideFieldRepresentation(new Matrix<>(2, 2)), new ResourceHolder(), Symbol.BLUE)
        );

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        Game game = new Game(players, null);

        ClientNetworkManager client = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(PORT)
                .withIdentity("Player1")
                .withGameModel(game)
                .withNetworkType(NetworkType.RMI)
                .build();

        Controller controller = client.getController();

        controller.execute(new PlayerPickCardAction("Player1", card));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(1, player1.getPlayableCards().size());
        } catch (AssertionFailedError e) {
            synchronized (exceptions) {
                exceptions.add(e);
            }
        }
    }

    private void client2ThreadFunction() {
        Player player1 = new Player("Player1", Pawn.BLUE, null, null);
        Player player2 = new Player("Player2", Pawn.RED, null, null);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        Game game = new Game(players, null);

        ClientNetworkManager client = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(PORT)
                .withIdentity("Player2")
                .withGameModel(game)
                .withNetworkType(NetworkType.RMI)
                .build();

        Controller controller = client.getController();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(1, player1.getPlayableCards().size());
        } catch (AssertionFailedError e) {
            synchronized (exceptions) {
                exceptions.add(e);
            }
        }
    }

}
