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
import com.squareup.moshi.Types;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.PatternObjectiveCard;
import it.polimi.ingsw.am07.model.game.card.ResourceObjectiveCard;
import it.polimi.ingsw.am07.model.game.side.*;

import java.util.List;

/**
 * This class is a custom Moshi handler that can serialize and deserialize any game object
 *
 * @param <T>
 */
public final class GameDataJsonParser<T> {

    private final Class<T> typeClass;

    /**
     * Constructor
     *
     * @param typeClass The class of the object to serialize/deserialize
     */
    public GameDataJsonParser(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    /**
     * Initializes Moshi with the custom adapters required to serialize and deserialize the game objects
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

        return new Moshi.Builder()
                .add(sideFrontElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(sideElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .add(objectiveCardElegantAutoLabelingCustomPolymorphicJsonAdapterFactory)
                .build();
    }

    /**
     * Returns the JsonAdapter for generic object
     *
     * @return JsonAdapter
     */
    private JsonAdapter<T> getAdapter() {
        Moshi moshi = initializeMoshi();

        return moshi.adapter(typeClass);
    }

    /**
     * Returns the JsonAdapter for a list of generic objects
     *
     * @return JsonAdapter
     */
    private JsonAdapter<List<T>> getListAdapter() {
        Moshi moshi = initializeMoshi();

        return moshi.adapter(Types.newParameterizedType(List.class, typeClass));
    }

    /**
     * Serializes the object to JSON
     *
     * @param element The object to serialize
     * @return JSON string
     * @throws RuntimeException if something goes wrong with the serialization
     */
    public String toJson(T element) throws RuntimeException {
        JsonAdapter<T> adapter = getAdapter();

        try {
            return adapter.toJson(element);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes a list of objects to JSON
     *
     * @param list The list of objects to serialize
     * @return JSON string
     * @throws RuntimeException if something goes wrong with the serialization
     */
    public String listToJson(List<T> list) throws RuntimeException {
        JsonAdapter<List<T>> adapter = getListAdapter();

        try {
            return adapter.toJson(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes the object from JSON
     *
     * @param json JSON string
     * @return The deserialized object
     * @throws RuntimeException if something goes wrong with the deserialization
     */
    public T fromJson(String json) throws RuntimeException {
        JsonAdapter<T> adapter = getAdapter();

        try {
            return adapter.fromJson(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes a list of objects from JSON
     *
     * @param json JSON string
     * @return The deserialized list of objects
     * @throws RuntimeException if something goes wrong with the deserialization
     */
    public List<T> listFromJson(String json) throws RuntimeException {
        JsonAdapter<List<T>> adapter = getListAdapter();

        try {
            return adapter.fromJson(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
