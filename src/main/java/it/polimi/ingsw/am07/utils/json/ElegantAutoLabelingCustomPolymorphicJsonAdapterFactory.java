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

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is a custom JsonAdapter.Factory that allows to serialize and deserialize polymorphic objects
 * It should be registered with a Moshi instance to allow it to serialize and deserialize polymorphic objects
 *
 * @param <T>
 */
public class ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<T> implements JsonAdapter.Factory {

    private final Class<T> baseClass;

    private final List<Type> subClasses;

    private final List<String> subClassNames;

    /**
     * Constructor
     *
     * @param baseClass The base class of the polymorphic objects
     */
    public ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory(Class<T> baseClass) {
        this.baseClass = baseClass;
        this.subClasses = new ArrayList<>();
        this.subClassNames = new ArrayList<>();
    }

    /**
     * Register a subclass of the base type to be serialized and deserialized
     *
     * @param subclass The subclass to register, must be a concrete class
     * @return this
     * @throws NullPointerException     if subclass is null
     * @throws IllegalArgumentException if subclass is an interface or an abstract class
     */
    public ElegantAutoLabelingCustomPolymorphicJsonAdapterFactory<T> registerSubclass(Class<? extends T> subclass) throws NullPointerException, IllegalArgumentException {
        if (subclass == null) {
            throw new NullPointerException("provided subclass is null");
        }

        if (subclass.isInterface()) {
            throw new IllegalArgumentException("provided subclass is an interface");
        }

        if (Modifier.isAbstract(subclass.getModifiers())) {
            throw new IllegalArgumentException("provided subclass is abstract");
        }

        subClasses.add(subclass);
        subClassNames.add(subclass.getSimpleName());

        return this;
    }

    /**
     * Create a JsonAdapter for the base class. Only Moshi should call this method.
     *
     * @param type  The type of the object to serialize or deserialize
     * @param set   The set of annotations on the field or method that this adapter was created for
     * @param moshi The Moshi instance
     * @return a JsonAdapter for the base class or null if the type is not the base class
     */
    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> set, Moshi moshi) {
        if (Types.getRawType(type) != baseClass) {
            return null;
        }

        if (!set.isEmpty()) {
            return null;
        }

        List<JsonAdapter<Object>> jsonAdapters = new ArrayList<>();

        for (Type subType : subClasses) {
            JsonAdapter<Object> jsonAdapter = moshi.adapter(subType);
            jsonAdapters.add(jsonAdapter);
        }

        return new ElegantAutoLabelingCustomPolymorphicJsonAdapter(subClassNames, subClasses, jsonAdapters);
    }

}
