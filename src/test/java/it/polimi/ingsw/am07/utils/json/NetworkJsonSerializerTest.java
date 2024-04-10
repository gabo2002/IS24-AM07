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

package it.polimi.ingsw.am07.utils.json;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.server.GameStateSyncAction;
import it.polimi.ingsw.am07.action.server.LobbyStateSyncAction;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.network.packets.ActionNetworkPacket;
import it.polimi.ingsw.am07.network.packets.HeartbeatNetworkPacket;
import it.polimi.ingsw.am07.network.packets.IdentityNetworkPacket;
import it.polimi.ingsw.am07.network.packets.NetworkPacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkJsonSerializerTest {

    @Test
    void validateIdentitySerializability() {
        NetworkJsonSerializer networkJsonSerializer = NetworkJsonSerializer.getInstance();

        IdentityNetworkPacket identity = new IdentityNetworkPacket("test");

        assertDoesNotThrow(() -> {
            String serialized = networkJsonSerializer.toJson(identity);

            IdentityNetworkPacket deserialized = (IdentityNetworkPacket) networkJsonSerializer.fromJson(serialized);

            assertEquals(identity.getIdentity(), deserialized.getIdentity());
        });
    }

    @Test
    void validateHeartbeatSerializability() {
        NetworkJsonSerializer networkJsonSerializer = NetworkJsonSerializer.getInstance();

        HeartbeatNetworkPacket heartbeat = new HeartbeatNetworkPacket();

        assertDoesNotThrow(() -> {
            String serialized = networkJsonSerializer.toJson(heartbeat);
            NetworkPacket deserialized = networkJsonSerializer.fromJson(serialized);
        });
    }

    @Test
    void validateActionSerializability() {
        NetworkJsonSerializer networkJsonSerializer = NetworkJsonSerializer.getInstance();

        Lobby lobby = new Lobby();
        lobby.addNewPlayer("test1");
        lobby.addNewPlayer("test2");
        lobby.addNewPlayer("test3");
        Game game = new Game.Factory().fromLobby(lobby).build();

        assertDoesNotThrow(() -> {
            Action action = new GameStateSyncAction(game);
            NetworkPacket packet = new ActionNetworkPacket(action);
            String serialized = networkJsonSerializer.toJson(packet);
            assertNotNull(serialized);
            NetworkPacket deserialized = networkJsonSerializer.fromJson(serialized);
            assertNotNull(deserialized);
        });

        assertDoesNotThrow(() -> {
            Action action = new LobbyStateSyncAction(lobby);
            NetworkPacket packet = new ActionNetworkPacket(action);
            String serialized = networkJsonSerializer.toJson(packet);
            assertNotNull(serialized);
            NetworkPacket deserialized = networkJsonSerializer.fromJson(serialized);
            assertNotNull(deserialized);
        });
    }

}