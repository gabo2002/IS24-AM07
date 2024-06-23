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

package it.polimi.ingsw.am07.reactive;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for a dispatcher that can execute an action and notify listeners.
 */
public abstract class Dispatcher implements Controller {

    protected final List<Listener> listeners;

    /**
     * Constructor.
     *
     * @param defaultListenersSize the default size of the listeners list
     */
    protected Dispatcher(int defaultListenersSize) {
        listeners = new ArrayList<>(defaultListenersSize);
    }

    /**
     * Constructor.
     */
    protected Dispatcher() {
        listeners = new ArrayList<>();
    }

    /**
     * Register a new listener.
     *
     * @param listener the listener to register
     */
    public abstract void registerNewListener(Listener listener);

    /**
     * Remove a listener.
     *
     * @param listener the listener to remove
     */
    public abstract void removeListener(Listener listener);

    /**
     * Inherit listeners from another dispatcher.
     */
    public void inheritListeners(Dispatcher dispatcher) {
        listeners.addAll(dispatcher.listeners);
    }

    /**
     * Get the number of listeners.
     */
    public int getListenersCount() {
        return listeners.size();
    }

}
