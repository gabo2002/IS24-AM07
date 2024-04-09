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

import it.polimi.ingsw.am07.action.DebuggingAction;
import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.controller.GameController;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.network.rmi.ClientRMINetworkManager;
import it.polimi.ingsw.am07.network.rmi.ServerRMINetworkManager;
import it.polimi.ingsw.am07.reactive.Listener;
import it.polimi.ingsw.am07.reactive.StatefulListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RMINetworkTest {

    @Test
    void testConnection() {
        Player p = new Player("provola", Pawn.BLUE, null, null);
        Game game = new Game(new ArrayList<>(List.of(p)), null);
        GameController gameController = new GameController(game);

        ClientNetworkManager client = new ClientRMINetworkManager("provola");
        ServerNetworkManager server = new ServerRMINetworkManager(gameController);

        server.start();

        Thread b = new Thread(() -> {
            StatefulListener listener = server.accept();
            assertEquals("provola", listener.getIdentity());
            gameController.execute(new DebuggingAction());
            gameController.execute(new PlayerPickCardAction(p.getNickname(), game.pickRandomResCard()));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        b.start();

        Player p_copy = new Player("provola", Pawn.BLUE, null, null);
        Game game_copy = new Game(new ArrayList<>(List.of(p_copy)), null);

        Thread a = new Thread(() -> {
            client.connect();

            client.inflateListener(game_copy);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        a.start();

        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(1, p.getPlayableCards().size());
        assertEquals(1, p_copy.getPlayableCards().size());
    }

}
