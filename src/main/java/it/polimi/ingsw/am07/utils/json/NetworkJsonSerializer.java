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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.DebuggingAction;
import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.action.player.PlayerPlaceCardAction;
import it.polimi.ingsw.am07.model.game.side.*;
import it.polimi.ingsw.am07.network.packets.ActionNetworkPacket;
import it.polimi.ingsw.am07.network.packets.HeartbeatNetworkPacket;
import it.polimi.ingsw.am07.network.packets.IdentityNetworkPacket;
import it.polimi.ingsw.am07.network.packets.NetworkPacket;

public class NetworkJsonSerializer {

    private static NetworkJsonSerializer instance;

    private final Moshi moshi;
    private final JsonAdapter<NetworkPacket> adapter;

    private NetworkJsonSerializer() {
        moshi = initializeMoshi();

        adapter = moshi.adapter(NetworkPacket.class);
    }

    private static Moshi initializeMoshi() {
        ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<SideFront> sideFrontElegantAutoLabelingCustomPolymorphicJsonAdapterFactory = new ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<>(SideFront.class)
                .registerSubclass(SideFrontGold.class)
                .registerSubclass(SideFrontRes.class)
                .registerSubclass(SideFrontStarter.class);

        ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<Side> sideElegantAutoLabelingCustomPolymorphicJsonAdapterFactory = new ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<>(Side.class)
                .registerSubclass(SideFrontGold.class)
                .registerSubclass(SideFrontRes.class)
                .registerSubclass(SideFrontStarter.class)
                .registerSubclass(SideBack.class);

        ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<NetworkPacket> networkPacketElegantAutoLabelingCustomPolymorphicJsonAdapterFactory = new ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<>(NetworkPacket.class)
                .registerSubclass(ActionNetworkPacket.class)
                .registerSubclass(HeartbeatNetworkPacket.class)
                .registerSubclass(IdentityNetworkPacket.class);

        ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<Action> actionElegantAutoLabelingCustomPolymorphicJsonAdapterFactory = new ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<>(Action.class)
                .registerSubclass(PlayerPickCardAction.class)
                .registerSubclass(PlayerPlaceCardAction.class)
                .registerSubclass(DebuggingAction.class);

        return new Moshi.Builder()
                .add(networkPacketElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(actionElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(sideFrontElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(sideElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .build();
    }

    public static NetworkJsonSerializer getInstance() {
        if (instance == null) {
            instance = new NetworkJsonSerializer();
        }
        return instance;
    }

    public String toJson(NetworkPacket packet) {
        return moshi.adapter(NetworkPacket.class).toJson(packet);
    }

    public NetworkPacket fromJson(String json) {
        try {
            return adapter.fromJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
