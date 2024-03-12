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

package it.polimi.ingsw.am07.utils;

import java.util.Iterator;

/**
 * An iterator for the elements of a matrix, in row-major order, one by one.
 * @param <T>
 * @author Roberto Alessandro Bertolini
 */
public class MatrixElementIterator <T> implements Iterator<T> {

    private final Matrix<T> matrix;

    private int currentX, currentY;

    /**
     * Create a new iterator for the given matrix.
     * @param matrix the matrix to iterate over
     * @author Roberto Alessandro Bertolini
     */
    public MatrixElementIterator(Matrix<T> matrix) {
        this.matrix = matrix;
        currentX = matrix.getMinX();
        currentY = matrix.getMinY();
    }

    /**
     * Check if there are more elements to iterate on.
     * @return true if there are more elements, false otherwise
     * @author Roberto Alessandro Bertolini
     */
    @Override
    public boolean hasNext() {
        return currentX <= matrix.getMaxX() && currentY <= matrix.getMaxY();
    }

    /**
     * Get the next element in the iteration.
     * @return the next element
     * @author Roberto Alessandro Bertolini
     */
    @Override
    public T next() {
        if (!hasNext()) {
            return null;
        }

        T element = matrix.get(currentX, currentY);

        currentX++;
        if (currentX > matrix.getMaxX()) {
            currentX = matrix.getMinX();
            currentY++;
        }
        return element;
    }

    /**
     * Clear the current element from the matrix (does not actually remove it from the matrix).
     * @author Roberto Alessandro Bertolini
     */
    @Override
    public void remove() {
        int x = currentX - 1;
        int y = currentY;
        if (x < matrix.getMinX()) {
            x = matrix.getMaxX();
            y--;
        }

        matrix.clear(x, y);
    }

}
