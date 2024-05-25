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

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.lobby.CreateLobbyAction;
import it.polimi.ingsw.am07.action.lobby.PlayerJoinAction;
import it.polimi.ingsw.am07.client.cli.Instruction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.server.Server;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class LobbyFlowTest {

    @Test
    void testLobbyFlow() {
        Server server = new Server(54321, 4321);

        new Thread(server::entrypoint).start();
        new Thread(this::client1).start();
        new Thread(this::client2).start();

        try {
            Thread.sleep(10000);
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
                .withPort(54321)
                .withNetworkType(NetworkType.TCP)
                .withIdentity("client1")
                .withState(clientState)
                .build();

        Controller controller = networkManager.getController();
        Action createLobby = new CreateLobbyAction("client1","client1", Pawn.RED);
        controller.execute(createLobby);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void client2() {
        List<Lobby> availableLobbies = new ArrayList<>();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientState clientState = new ClientState((ClientState state) -> {
            System.out.println("Received state update");
            if(!state.getAvailableLobbies().isEmpty()) {
                System.out.println("Lobby count: " + state.getAvailableLobbies().size());
                availableLobbies.addAll(state.getAvailableLobbies());
            }
        },"client2");

        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(54321)
                .withNetworkType(NetworkType.TCP)
                .withIdentity("client2")
                .withState(clientState)
                .build();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Controller controller = networkManager.getController();

        if(availableLobbies.isEmpty())
            return;
        Action joinLobby = new PlayerJoinAction("client2","client2",availableLobbies.get(0).getId());
        controller.execute(joinLobby);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
