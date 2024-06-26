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

package it.polimi.ingsw.am07.utils.lambda;

/**
 * Represents a function that accepts four arguments and produces a result.
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @param <W> the type of the fourth argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {

    /**
     * Applies this function to the given arguments.
     * @param t the first argument
     * @param u the second argument
     * @param v the third argument
     * @param w the fourth argument
     * @return the function result
     */
    R apply(T t, U u, V v, W w);

    default <X> QuadFunction<T, U, V, W, X> andThen(java.util.function.Function<? super R, ? extends X> after) {
        if (after == null) {
            throw new NullPointerException();
        }
        return (t, u, v, w) -> after.apply(apply(t, u, v, w));
    }

}

