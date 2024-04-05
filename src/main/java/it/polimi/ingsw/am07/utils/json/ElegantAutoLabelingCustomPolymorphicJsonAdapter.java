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
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This class is a custom JsonAdapter that allows to serialize and deserialize polymorphic objects
 * It automatically adds a label to the serialized object to identify the subclass
 * The label is the first field of the serialized object
 */
public class ElegantAutoLabelingCustomPolymorphicJsonAdapter extends JsonAdapter<Object> {

    private static final String CLASS_LABEL = "class";

    private static final JsonReader.Options LABEL_OPTION = JsonReader.Options.of(CLASS_LABEL);

    private final List<Type> subTypes;

    private final List<String> subTypeNames;

    private final List<JsonAdapter<Object>> adapters;

    private final JsonReader.Options subTypeOptions;

    /**
     * Constructor
     *
     * @param subTypeNames List of subclass names
     * @param subTypes     List of subclass types
     * @param adapters     List of JsonAdapters for the subclasses
     */
    public ElegantAutoLabelingCustomPolymorphicJsonAdapter(List<String> subTypeNames, List<Type> subTypes, List<JsonAdapter<Object>> adapters) {
        this.subTypes = subTypes;
        this.adapters = adapters;
        this.subTypeNames = subTypeNames;
        this.subTypeOptions = JsonReader.Options.of(subTypeNames.toArray(new String[0]));
    }

    /**
     * This method deserializes the object and reads the label to identify the subclass
     *
     * @param jsonReader JsonReader
     * @return deserialized object
     * @throws IOException      if something goes wrong with the JSON reader
     * @throws RuntimeException if the subclass is not valid or not recognized
     */
    @Override
    public Object fromJson(JsonReader jsonReader) throws IOException, RuntimeException {
        try (JsonReader peeked = jsonReader.peekJson()) {
            int subTypeIndex = findSubTypeIndex(peeked);

            JsonAdapter<Object> adapter = adapters.get(subTypeIndex);

            return adapter.fromJson(jsonReader);
        }
    }

    /**
     * This method serializes the object and adds a label to identify the subclass
     *
     * @param jsonWriter JsonWriter
     * @param o          Object to serialize
     * @throws IOException      if something goes wrong with the JSON writer
     * @throws RuntimeException if the subclass is not valid or not recognized
     */
    @Override
    public void toJson(JsonWriter jsonWriter, Object o) throws IOException, RuntimeException {
        if (o == null) {
            jsonWriter.beginObject();
            jsonWriter.endObject();

            return;
        }

        int subTypeIndex = subTypes.indexOf(o.getClass());

        if (subTypeIndex == -1) {
            throw new RuntimeException("Unknown subclass: " + o.getClass().getName());
        }

        jsonWriter.beginObject();
        jsonWriter.name(CLASS_LABEL).value(subTypeNames.get(subTypeIndex));
        int flattenToken = jsonWriter.beginFlatten();
        adapters.get(subTypeIndex).toJson(jsonWriter, o);
        jsonWriter.endFlatten(flattenToken);
        jsonWriter.endObject();
    }

    /**
     * This method finds the index of the subclass in the list of subclasses
     *
     * @param jsonReader JsonReader
     * @return index of the subclass
     * @throws IOException      if the JSON file is not well formatted
     * @throws RuntimeException if the subclass is not valid
     */
    private int findSubTypeIndex(JsonReader jsonReader) throws IOException, RuntimeException {
        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            if (jsonReader.selectName(LABEL_OPTION) != -1) {
                int index = jsonReader.selectString(subTypeOptions);

                if (index == -1) {
                    throw new RuntimeException("Unknown subclass label: " + jsonReader.nextString());
                }

                return index;
            } else {
                jsonReader.skipName();
                jsonReader.skipValue();
            }
        }

        return 0;
    }

}
