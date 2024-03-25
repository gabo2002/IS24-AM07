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

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class MatrixSubMatrixIteratorTest {
    Matrix<Integer> getIntegerMatrix() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);

        for (int i = 0; i < 9; i++) {
            matrix.set(i % 3, i / 3, i);
        }

        return matrix;
    }

    @Test
    void hasNext() {
        // Test for in-bounds submatrix
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);

        MatrixSubMatrixIterator<Integer> iterator = new MatrixSubMatrixIterator<>(matrix, 1, 1, false);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new MatrixSubMatrixIterator<>(matrix, 2, 2, false);

        for (int i = 0; i < 4; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new MatrixSubMatrixIterator<>(matrix, 3, 3, false);

        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new MatrixSubMatrixIterator<>(matrix, 4, 4, false);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        // Test for out-of-bounds submatrix
        iterator = new MatrixSubMatrixIterator<>(matrix, 1, 1, true);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new MatrixSubMatrixIterator<>(matrix, 2, 2, true);

        for (int i = 0; i < 16; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new MatrixSubMatrixIterator<>(matrix, 3, 3, true);

        for (int i = 0; i < 25; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new MatrixSubMatrixIterator<>(matrix, 4, 4, true);

        for (int i = 0; i < 36; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    void next() {
        // Test for in-bounds submatrix
        Matrix<Integer> matrix = new Matrix<>(3, 3, null);

        for (int i = 0; i < 9; i++) {
            matrix.set(i % 3, i / 3, i);
        }

        MatrixSubMatrixIterator<Integer> iterator = new MatrixSubMatrixIterator<>(matrix, 1, 1, false);

        for (int i = 0; i < 9; i++) {
            assertTrue(new Matrix<>(1, 1, i).match(iterator.next()));
        }

        iterator = new MatrixSubMatrixIterator<>(matrix, 2, 2, false);

        Matrix<Integer> subMatrix = new Matrix<>(2, 2, 0);
        subMatrix.set(0, 0, 0);
        subMatrix.set(1, 0, 1);
        subMatrix.set(0, 1, 3);
        subMatrix.set(1, 1, 4);
        assertTrue(subMatrix.match(iterator.next()));

        subMatrix.set(0, 0, 1);
        subMatrix.set(1, 0, 2);
        subMatrix.set(0, 1, 4);
        subMatrix.set(1, 1, 5);
        assertTrue(subMatrix.match(iterator.next()));

        subMatrix.set(0, 0, 3);
        subMatrix.set(1, 0, 4);
        subMatrix.set(0, 1, 6);
        subMatrix.set(1, 1, 7);
        assertTrue(subMatrix.match(iterator.next()));

        subMatrix.set(0, 0, 4);
        subMatrix.set(1, 0, 5);
        subMatrix.set(0, 1, 7);
        subMatrix.set(1, 1, 8);
        assertTrue(subMatrix.match(iterator.next()));

        iterator = new MatrixSubMatrixIterator<>(matrix, 3, 3, false);

        assertTrue(matrix.match(iterator.next()));

        iterator = new MatrixSubMatrixIterator<>(matrix, 4, 4, false);

        assertNull(iterator.next());

        // Test for out-of-bounds submatrix
        iterator = new MatrixSubMatrixIterator<>(matrix, 1, 1, true);

        for (int i = 0; i < 9; i++) {
            assertTrue(new Matrix<>(1, 1, i).match(iterator.next()));
        }

        iterator = new MatrixSubMatrixIterator<>(matrix, 2, 2, true);

        subMatrix = new Matrix<>(2, 2, 0);
        subMatrix.set(0, 0, null);
        subMatrix.set(1, 0, null);
        subMatrix.set(0, 1, null);
        subMatrix.set(1, 1, 0);

        assertTrue(subMatrix.match(iterator.next()));

        for (int i = 0; i < (16 - 2); i++) {
            iterator.next();
        }

        subMatrix.set(0, 0, 8);
        subMatrix.set(1, 0, null);
        subMatrix.set(0, 1, null);
        subMatrix.set(1, 1, null);

        assertTrue(subMatrix.match(iterator.next()));

        iterator = new MatrixSubMatrixIterator<>(matrix, 3, 3, true);

        subMatrix = new Matrix<>(3, 3, null);
        subMatrix.set(0, 0, 4);
        subMatrix.set(1, 0, 5);
        subMatrix.set(0, 1, 7);
        subMatrix.set(1, 1, 8);

        for (int i = 0; i < 18; i++) {
            iterator.next();
        }

        assertTrue(subMatrix.match(iterator.next()));

        int i = 0;
        for (Iterator<Matrix<Integer>> it = matrix.iterator(1, 1); it.hasNext(); ) {
            assertTrue(new Matrix<>(1, 1, i).match(it.next()));
            i++;
        }
    }

    @Test
    void remove() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, null);

        for (int i = 0; i < 9; i++) {
            matrix.set(i % 3, i / 3, i);
        }

        MatrixSubMatrixIterator<Integer> iterator = new MatrixSubMatrixIterator<>(matrix, 3, 3, true);

        for (int i = 0; i < 18; i++) {
            iterator.next();
        }
        iterator.remove();

        assertEquals(0, matrix.get(0, 0));
        assertEquals(1, matrix.get(1, 0));
        assertEquals(2, matrix.get(2, 0));
        assertNull(matrix.get(1, 1));
        assertNull(matrix.get(2, 1));
        assertNull(matrix.get(0, 1));
        assertNull(matrix.get(0, 2));
        assertNull(matrix.get(1, 2));
        assertNull(matrix.get(2, 2));
    }

    @Test
    void removeWithMask() {
        Matrix<Integer> matrix = getIntegerMatrix();

        MatrixSubMatrixIterator<Integer> iterator = new MatrixSubMatrixIterator<>(matrix, 2, 2, false);

        Matrix<Integer> mask = new Matrix<>(2, 2, 0);
        mask.set(0, 0, 1);
        mask.set(1, 0, 1);
        mask.set(0, 1, 1);
        Matrix<Integer> subMatrix = iterator.next();
        iterator.remove(mask);

        assertEquals(0, matrix.get(0, 0));
        assertEquals(0, matrix.get(1, 0));
        assertEquals(0, matrix.get(0, 1));
        assertEquals(4, matrix.get(1, 1));
        assertEquals(7, matrix.get(1, 2));
        assertEquals(8, matrix.get(2, 2));
        assertEquals(2, matrix.get(2, 0));
        assertEquals(5, matrix.get(2, 1));

        subMatrix = iterator.next();
        iterator.remove(mask);

        assertEquals(0, matrix.get(1, 1));
        assertEquals(0, matrix.get(2, 0));

        //Trying with an empty mask
        mask = new Matrix<>(2, 2, 0);
        matrix = getIntegerMatrix();

        iterator = new MatrixSubMatrixIterator<>(matrix, 2, 2, false);

        while (iterator.hasNext()) {
            subMatrix = iterator.next();
            iterator.remove(mask);
        }

        for (int i = 0; i < 9; i++) {
            assertEquals(i, matrix.get(i % 3, i / 3));
        }

        mask = new Matrix<Integer>(2, 2, 0);
        mask.set(0, 1, 1);
        mask.set(1, 0, 1);

        matrix = getIntegerMatrix();

        iterator = new MatrixSubMatrixIterator<>(matrix, 2, 2, false);
        subMatrix = iterator.next();
        iterator.remove(mask);

        assertEquals(0, matrix.get(0, 0));
        assertEquals(0, matrix.get(1, 0));
        assertEquals(0, matrix.get(0, 1));
        assertEquals(4, matrix.get(1, 1));
    }

}