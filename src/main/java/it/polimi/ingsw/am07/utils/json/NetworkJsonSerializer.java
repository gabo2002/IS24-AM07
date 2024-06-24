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
import dev.zacsweers.moshix.records.RecordsJsonAdapterFactory;
import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.DebuggingAction;
import it.polimi.ingsw.am07.action.chat.SendMessageAction;
import it.polimi.ingsw.am07.action.error.ErrorAction;
import it.polimi.ingsw.am07.action.lobby.CreateLobbyAction;
import it.polimi.ingsw.am07.action.lobby.GameStartAction;
import it.polimi.ingsw.am07.action.lobby.PlayerJoinAction;
import it.polimi.ingsw.am07.action.lobby.ReconnectAction;
import it.polimi.ingsw.am07.action.player.PlayerInitialChoiceAction;
import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.action.player.PlayerPlaceCardAction;
import it.polimi.ingsw.am07.action.server.LobbyListAction;
import it.polimi.ingsw.am07.action.server.LobbyStateSyncAction;
import it.polimi.ingsw.am07.action.server.ResumeGameAction;
import it.polimi.ingsw.am07.action.server.ServerGameStartAction;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.PatternObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.ResourceObjectiveCard;
import it.polimi.ingsw.am07.model.game.side.*;
import it.polimi.ingsw.am07.network.packets.ActionNetworkPacket;
import it.polimi.ingsw.am07.network.packets.HeartbeatNetworkPacket;
import it.polimi.ingsw.am07.network.packets.IdentityNetworkPacket;
import it.polimi.ingsw.am07.network.packets.NetworkPacket;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.io.IOException;

/**
 * This class is a custom Moshi handler that can serialize and deserialize any network packet.
 */
public class NetworkJsonSerializer {

    private static NetworkJsonSerializer instance;
    private final AppLogger LOGGER = new AppLogger(NetworkJsonSerializer.class);
    private final Moshi moshi;
    private final JsonAdapter<NetworkPacket> adapter;

    private NetworkJsonSerializer() {
        moshi = initializeMoshi();

        adapter = moshi.adapter(NetworkPacket.class);
    }

    /**
     * Initializes Moshi with the custom adapters required to serialize and deserialize the network packets
     *
     * @return Moshi
     */
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

        ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<ObjectiveCard> objectiveCardElegantAutoLabelingCustomPolymorphicJsonAdapterFactory = new ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<>(ObjectiveCard.class)
                .registerSubclass(PatternObjectiveCard.class)
                .registerSubclass(ResourceObjectiveCard.class);

        ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<NetworkPacket> networkPacketElegantAutoLabelingCustomPolymorphicJsonAdapterFactory = new ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<>(NetworkPacket.class)
                .registerSubclass(ActionNetworkPacket.class)
                .registerSubclass(HeartbeatNetworkPacket.class)
                .registerSubclass(IdentityNetworkPacket.class);

        ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<Action> actionElegantAutoLabelingCustomPolymorphicJsonAdapterFactory = new ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<>(Action.class)
                .registerSubclass(GameStartAction.class)
                .registerSubclass(PlayerPickCardAction.class)
                .registerSubclass(PlayerPlaceCardAction.class)
                .registerSubclass(LobbyStateSyncAction.class)
                .registerSubclass(CreateLobbyAction.class)
                .registerSubclass(PlayerJoinAction.class)
                .registerSubclass(LobbyListAction.class)
                .registerSubclass(ErrorAction.class)
                .registerSubclass(SendMessageAction.class)
                .registerSubclass(PlayerInitialChoiceAction.class)
                .registerSubclass(ServerGameStartAction.class)
                .registerSubclass(ResumeGameAction.class)
                .registerSubclass(ReconnectAction.class)
                .registerSubclass(DebuggingAction.class);

        UUIDJsonAdapter uuidJsonAdapter = new UUIDJsonAdapter();
        DateJSONAdapter dateJSONAdapter = new DateJSONAdapter();
        GameFieldPositionCardMapAdapter gameFieldPositionCardMapAdapter = new GameFieldPositionCardMapAdapter();

        return new Moshi.Builder()
                .add(dateJSONAdapter)
                .add(networkPacketElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(actionElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(sideFrontElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(sideElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(objectiveCardElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(uuidJsonAdapter)
                .add(gameFieldPositionCardMapAdapter)
                .add(new RecordsJsonAdapterFactory())
                .build();
    }

    /**
     * Get the singleton instance of the class
     *
     * @return the singleton instance of the class
     */
    public static NetworkJsonSerializer getInstance() {
        if (instance == null) {
            instance = new NetworkJsonSerializer();
        }
        return instance;
    }

    /**
     * Serialize a network packet to a JSON string
     *
     * @param packet the network packet to serialize
     * @return the JSON string
     */
    public String toJson(NetworkPacket packet) {
        return moshi.adapter(NetworkPacket.class).toJson(packet);
    }

    /**
     * Deserialize a JSON string to a network packet
     *
     * @param json the JSON string to deserialize
     * @return the network packet
     */
    public NetworkPacket fromJson(String json) {
        try {
            return adapter.fromJson(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
