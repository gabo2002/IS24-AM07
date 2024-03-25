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

import static org.junit.jupiter.api.Assertions.*;

class MatrixElementIteratorTest {

    @Test
    void hasNext() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);
        MatrixElementIterator<Integer> iterator = new MatrixElementIterator<>(matrix);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void next() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);
        MatrixElementIterator<Integer> iterator = new MatrixElementIterator<>(matrix);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(0, iterator.next());
        }

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        for (int i = 0; i < 9; i++) {
            matrix.set(i % 3, i / 3, i);
        }

        iterator = new MatrixElementIterator<>(matrix);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        int i = 0;
        for (Integer el : matrix) {
            assertEquals(i, el);
            i++;
        }
    }

    @Test
    void remove() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, null);
        MatrixElementIterator<Integer> iterator = new MatrixElementIterator<>(matrix);

        for (int i = 0; i < 9; i++) {
            matrix.set(i % 3, i / 3, i);
        }

        for (int i = 0; i < 9; i++) {
            assertEquals(i, iterator.next());
            iterator.remove();
            assertNull(matrix.get(i % 3, i / 3));
        }
    }

}