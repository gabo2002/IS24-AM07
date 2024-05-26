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

package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.model.game.side.SideFieldRepresentation;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the resources that a player can have in his possession.
 * Additionally, it is used to represent the resources that a card side provides,
 * or the resources required for a side to be placed on the game field.
 */
public class ResourceHolder implements Serializable {

    private final Map<Symbol, Integer> resources;

    /**
     * Default constructor, initializes an empty resource holder.
     */
    public ResourceHolder() {
        resources = new EnumMap<>(Symbol.class);
    }

    /**
     * Constructor that initializes a resource holder with the resources provided by a card side.
     *
     * @param fieldRepresentation the card side representation.
     */
    public ResourceHolder(SideFieldRepresentation fieldRepresentation) {
        this();

        for (int i = 0; i < SideFieldRepresentation.SIDE_SIZE; i++) {
            for (int j = 0; j < SideFieldRepresentation.SIDE_SIZE; j++) {
                incrementResource(fieldRepresentation.corners().get(i, j));
            }
        }
    }

    /**
     * Constructor that initializes a resource holder with the resources provided by a card side and additional resources.
     *
     * @param fieldRepresentation the card side representation.
     * @param additionalSymbols   the additional resources that a side might have, such as the center symbols.
     */
    public ResourceHolder(SideFieldRepresentation fieldRepresentation, List<Symbol> additionalSymbols) {
        this(fieldRepresentation);

        for (Symbol symbol : additionalSymbols) {
            incrementResource(symbol);
        }
    }

    /**
     * Constructor that initializes a resource holder with the resources provided by another resource holder.
     * The new resource holder is a copy of the provided one.
     *
     * @param resourceHolder the resource holder to copy.
     */
    public ResourceHolder(ResourceHolder resourceHolder) {
        this();
        add(resourceHolder);
    }

    /**
     * Increments the count of a resource in the resource holder.
     * If the provided symbol is not a resource, the method does nothing.
     *
     * @param symbol the resource to increment.
     */
    public void incrementResource(Symbol symbol) {
        if (!symbol.isResource()) {
            return;
        }

        if (resources.containsKey(symbol)) {
            resources.put(symbol, resources.get(symbol) + 1);
        } else {
            resources.put(symbol, 1);
        }
    }

    /**
     * Decrements the count of a resource in the resource holder.
     * If the provided symbol is not a resource, the method does nothing.
     *
     * @param symbol the resource to decrement.
     */
    public void decrementResource(Symbol symbol) {
        if (!symbol.isResource()) {
            return;
        }

        if (resources.containsKey(symbol)) {
            resources.put(symbol, resources.get(symbol) - 1);
        } else {
            resources.put(symbol, -1);
        }
    }

    /**
     * Subtracts the resources of another resource holder from this resource holder.
     *
     * @param other the resource holder to subtract.
     * @throws IllegalArgumentException if the resource holder does not contain enough resources to subtract.
     */
    public void subtract(ResourceHolder other) throws IllegalArgumentException {
        if (!contains(other)) {
            throw new IllegalArgumentException("The substraction results in negative amounts of resources.");
        }
        for (Map.Entry<Symbol, Integer> entry : other.resources.entrySet()) {
            resources.put(entry.getKey(), resources.get(entry.getKey()) - entry.getValue());
        }
    }

    /**
     * Checks if the resource holder has at least the resources of another resource holder.
     * To put it in other words, subtracting the other holder would result in non-negative amounts.
     *
     * @param other the resource holder to check.
     * @return true if the resource holder contains the resources of the other resource holder, false otherwise.
     */
    public boolean contains(ResourceHolder other) {
        for (Map.Entry<Symbol, Integer> entry : other.resources.entrySet()) {
            if (resources.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the resources of another resource holder to this resource holder.
     *
     * @param other the resource holder to add.
     */
    public void add(ResourceHolder other) {
        for (Map.Entry<Symbol, Integer> entry : other.resources.entrySet()) {
            if (resources.containsKey(entry.getKey())) {
                resources.put(entry.getKey(), resources.get(entry.getKey()) + entry.getValue());
            } else {
                resources.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Checks if two resource holders hold the same amount of resources.
     *
     * @param o the object to compare
     * @return true if the resource holders hold the same amount of resources, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceHolder that = (ResourceHolder) o;
        return contains(that) && that.contains(this);
    }

    /**
     * Returns the count of a resource in the resource holder.
     *
     * @param symbol the resource to count.
     * @return the count of the resource.
     */
    public int countOf(Symbol symbol) {
        return resources.getOrDefault(symbol, 0);
    }

    /**
     * Returns the resources of the resource holder.
     * The returned map is a copy of the internal map.
     * Modifying the returned map does not affect the resource holder.
     * To modify the resource holder, use the provided methods.
     * @return the resources of the resource holder.
     */
    public Map<Symbol, Integer> getResources() {
        return new EnumMap<>(resources);
    }

}
