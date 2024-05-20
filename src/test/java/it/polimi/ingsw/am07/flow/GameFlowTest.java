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

package it.polimi.ingsw.am07.flow;

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import it.polimi.ingsw.am07.server.Server;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class GameFlowTest {

    @Test
    void testGameFlow() {
        Server server = new Server(12345, 23456);

        new Thread(server::entrypoint).start();
        new Thread(this::client1).start();
        new Thread(this::client2).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void client1() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientState clientState = new ClientState((ClientState state) -> {
            System.out.println("Received state update");
            if(state.getLobbyModel() != null)
                System.out.println("Player count: " + state.getLobbyModel().getPlayerCount());
        },"client1");

        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(12345)
                .withNetworkType(NetworkType.TCP)
                .withIdentity("client1")
                .withState(clientState)
                .build();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void client2() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientState clientState = new ClientState((ClientState state) -> {
            System.out.println("Received state update");
            System.out.println("Player count: " + state.getLobbyModel().getPlayerCount());
        },"client2");

        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(23456)
                .withNetworkType(NetworkType.RMI)
                .withIdentity("client2")
                .withState(clientState)
                .build();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
