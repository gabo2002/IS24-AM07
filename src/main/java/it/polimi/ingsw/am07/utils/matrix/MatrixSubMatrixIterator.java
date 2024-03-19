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

package it.polimi.ingsw.am07.utils.matrix;

import java.util.Iterator;

/**
 * An iterator for the submatrices of a matrix.
 * The submatrices are of a fixed size, and are overlapping.
 * The iterator can be configured to iterate over the partially out-of-bounds submatrices as well.
 * @param <T> the type of the elements in the matrix
 * @author Roberto Alessandro Bertolini
 */
public class MatrixSubMatrixIterator <T> implements Iterator<Matrix<T>> {

    private final Matrix<T> matrix;

    private int currentX, currentY;

    private final int subMatrixSizeX;
    private final int subMatrixSizeY;

    private final boolean iterateOutOfBounds;

    /**
     * Create a new iterator for the given matrix, with the given submatrix size.
     * @param matrix                the matrix to iterate over
     * @param subMatrixSizeX        the width of the submatrices
     * @param subMatrixSizeY        the height of the submatrices
     * @param iterateOutOfBounds    whether to iterate over the partially out-of-bounds submatrices as well
     * @author Roberto Alessandro Bertolini
     */
    public MatrixSubMatrixIterator(Matrix<T> matrix, int subMatrixSizeX, int subMatrixSizeY, boolean iterateOutOfBounds) {
        this.matrix = matrix;
        this.subMatrixSizeX = subMatrixSizeX;
        this.subMatrixSizeY = subMatrixSizeY;
        this.iterateOutOfBounds = iterateOutOfBounds;

        if (iterateOutOfBounds) {
            // The first submatrix should have just the bottom-right corner set
            currentX = matrix.getMinX() - subMatrixSizeX + 1;
            currentY = matrix.getMinY() - subMatrixSizeY + 1;
        } else {
            // The first submatrix should have the top-left corner set
            currentX = matrix.getMinX();
            currentY = matrix.getMinY();
        }
    }


    /**
     * Check if there are more submatrices to iterate on.
     * @return true if there are more submatrices, false otherwise
     * @author Roberto Alessandro Bertolini
     */
    @Override
    public boolean hasNext() {
        if (iterateOutOfBounds) {
            return currentX <= matrix.getMaxX() && currentY <= matrix.getMaxY();
        } else {
            return (currentX + subMatrixSizeX - 1) <= matrix.getMaxX() && (currentY + subMatrixSizeY - 1) <= matrix.getMaxY();
        }
    }

    /**
     * Get the next submatrix in the iteration.
     * @return the next submatrix
     * @author Roberto Alessandro Bertolini
     */
    @Override
    public Matrix<T> next() {
        if (!hasNext()) {
            return null;
        }

        Matrix<T> subMatrix = matrix.getSubMatrix(currentX, currentY, subMatrixSizeX, subMatrixSizeY);

        currentX++;
        if (iterateOutOfBounds && currentX > matrix.getMaxX()) {
            currentX = matrix.getMinX() - subMatrixSizeX + 1;
            currentY++;
        } else if (!iterateOutOfBounds && (currentX + subMatrixSizeX - 1) > matrix.getMaxX()) {
            currentX = matrix.getMinX();
            currentY++;
        }

        return subMatrix;
    }

    /**
     * Clear the current submatrix from the matrix (does not actually remove it from the matrix).
     * @author Roberto Alessandro Bertolini
     */
    @Override
    public void remove() {
        for (int i = 0; i < subMatrixSizeX; i++) {
            for (int j = 0; j < subMatrixSizeY; j++) {
                matrix.clear(currentX + i, currentY + j);
            }
        }
    }

}
