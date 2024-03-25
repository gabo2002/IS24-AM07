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

class MatrixTest {

    private static Matrix<Integer> getIntegerMatrix() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);

        for (int i = 0; i < 9; i++) {
            matrix.set(i / 3, i % 3, i + 1);
        }

        return matrix;
    }

    @Test
    void getAndSet() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);

        for (int i = 0; i < 9; i++) {
            matrix.set(i / 3, i % 3, i);
        }

        for (int i = 0; i < 9; i++) {
            assertEquals(i, matrix.get(i / 3, i % 3));
        }

        assertEquals(3, matrix.getWidth());
        assertEquals(3, matrix.getHeight());
        assertEquals(3 * 3, matrix.getSize());
    }

    @Test
    void positiveExtension() {
        Matrix<Integer> matrix = getIntegerMatrix();

        assertEquals(3, matrix.getWidth());
        assertEquals(3, matrix.getHeight());
        assertEquals(3 * 3, matrix.getSize());

        // Insert new item in position (3, 3)
        matrix.set(3, 3, 16);

        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, matrix.get(i / 3, i % 3));
        }

        assertEquals(16, matrix.get(3, 3));
        assertEquals(0, matrix.get(3, 0));
        assertEquals(0, matrix.get(3, 1));
        assertEquals(0, matrix.get(3, 2));
        assertEquals(0, matrix.get(0, 3));
        assertEquals(0, matrix.get(1, 3));
        assertEquals(0, matrix.get(2, 3));

        assertEquals(4, matrix.getWidth());
        assertEquals(4, matrix.getHeight());
        assertEquals(4 * 4, matrix.getSize());

        // Insert new item in position (0, 3)
        matrix.set(0, 3, 255);

        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, matrix.get(i / 3, i % 3));
        }

        assertEquals(16, matrix.get(3, 3));
        assertEquals(0, matrix.get(3, 0));
        assertEquals(0, matrix.get(3, 1));
        assertEquals(0, matrix.get(3, 2));
        assertEquals(255, matrix.get(0, 3));
        assertEquals(0, matrix.get(1, 3));
        assertEquals(0, matrix.get(2, 3));

        assertEquals(4, matrix.getWidth());
        assertEquals(4, matrix.getHeight());
        assertEquals(4 * 4, matrix.getSize());

        // Insert new item in position (0, 300)
        matrix.set(0, 300, 1024);

        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, matrix.get(i / 3, i % 3));
        }

        assertEquals(16, matrix.get(3, 3));
        assertEquals(0, matrix.get(3, 0));
        assertEquals(0, matrix.get(3, 1));
        assertEquals(0, matrix.get(3, 2));
        assertEquals(255, matrix.get(0, 3));
        assertEquals(0, matrix.get(1, 3));
        assertEquals(0, matrix.get(2, 3));
        assertEquals(1024, matrix.get(0, 300));

        assertEquals(4, matrix.getWidth());
        assertEquals(301, matrix.getHeight());
        assertEquals(4 * 301, matrix.getSize());

        for (int i = 0; i < 4; i++) {
            for (int j = 4; j < 512; j++) {
                if (i == 0 && j == 300) {
                    continue;
                }
                assertEquals(0, matrix.get(i, j));
            }
        }
    }

    @Test
    void booleanAnd() {
        Matrix<Integer> matrix_1 = getIntegerMatrix();

        // A matrix anded with itself should not change
        matrix_1.logicAnd(matrix_1);
        assertTrue(matrix_1.match(getIntegerMatrix()));

        // A matrix anded with 0s should become 0s
        matrix_1 = getIntegerMatrix();
        Matrix<Integer> matrix_2 = new Matrix<>(3, 3, 0);
        matrix_1.logicAnd(matrix_2);
        assertTrue(matrix_2.match(matrix_1));

        // Test a generic case
        matrix_1 = getIntegerMatrix();
        matrix_1.set(0, 0, 3);
        matrix_1.set(0, 1, 3);
        matrix_1.set(1, 0, 3);
        matrix_1.set(1, 1, 3);
        matrix_1.set(2, 0, 0);
        matrix_1.set(2, 1, 0);
        matrix_1.set(0, 2, 0);
        matrix_1.set(1, 2, 0);
        matrix_1.set(2, 2, 0);

        matrix_2 = getIntegerMatrix();
        matrix_2.set(0, 0, 0);
        matrix_2.set(0, 1, 0);
        matrix_2.set(1, 0, 0);
        matrix_2.set(1, 1, 3);
        matrix_2.set(2, 0, 0);
        matrix_2.set(2, 1, 3);
        matrix_2.set(0, 2, 0);
        matrix_2.set(1, 2, 3);
        matrix_2.set(2, 2, 3);

        matrix_1.logicAnd(matrix_2);

        Matrix<Integer> result = new Matrix<>(3, 3, 0);
        result.set(1, 1, 3);

        assertTrue(result.match(matrix_1));
    }

    @Test
    void negativeExtension() {
        Matrix<Integer> matrix = getIntegerMatrix();

        // Insert new in position (-1, -1)
        matrix.set(-1, -1, 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, matrix.get(i / 3, i % 3));
        }

        assertEquals(10, matrix.get(-1, -1));
        assertEquals(0, matrix.get(-1, 0));
        assertEquals(0, matrix.get(-1, 1));
        assertEquals(0, matrix.get(-1, 2));
        assertEquals(0, matrix.get(0, -1));
        assertEquals(0, matrix.get(1, -1));
        assertEquals(0, matrix.get(2, -1));

        assertEquals(4, matrix.getWidth());
        assertEquals(4, matrix.getHeight());
        assertEquals(4 * 4, matrix.getSize());

        // Insert new in position (-8, -8)
        matrix.set(-8, -8, 15);

        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, matrix.get(i / 3, i % 3));
        }

        assertEquals(10, matrix.get(-1, -1));
        assertEquals(0, matrix.get(-1, 0));
        assertEquals(0, matrix.get(-1, 1));
        assertEquals(0, matrix.get(-1, 2));
        assertEquals(0, matrix.get(0, -1));
        assertEquals(0, matrix.get(1, -1));
        assertEquals(0, matrix.get(2, -1));
        assertEquals(15, matrix.get(-8, -8));

        assertEquals(11, matrix.getWidth());
        assertEquals(11, matrix.getHeight());
        assertEquals(11 * 11, matrix.getSize());

        //Insert new in position (15, 15)
        matrix.set(15, 15, 1024);

        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, matrix.get(i / 3, i % 3));
        }

        assertEquals(10, matrix.get(-1, -1));
        assertEquals(0, matrix.get(-1, 0));
        assertEquals(0, matrix.get(-1, 1));
        assertEquals(0, matrix.get(-1, 2));
        assertEquals(0, matrix.get(0, -1));
        assertEquals(0, matrix.get(1, -1));
        assertEquals(0, matrix.get(2, -1));
        assertEquals(15, matrix.get(-8, -8));
        assertEquals(1024, matrix.get(15, 15));

        assertEquals(24, matrix.getWidth());
        assertEquals(24, matrix.getHeight());
        assertEquals(24 * 24, matrix.getSize());
    }

    @Test
    void simpleConstructor() {
        Matrix<Integer> matrix = new Matrix<>(3, 3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertNull(matrix.get(i, j));
            }
        }
    }

    @Test
    void getSubMatrix() {
        Matrix<Integer> matrix = getIntegerMatrix();

        // Get a submatrix of size 2x2 starting from (0, 0)
        Matrix<Integer> subMatrix = matrix.getSubMatrix(0, 0, 2, 2);

        assertEquals(2, subMatrix.getWidth());
        assertEquals(2, subMatrix.getHeight());
        assertEquals(2 * 2, subMatrix.getSize());

        assertEquals(1, subMatrix.get(0, 0));
        assertEquals(2, subMatrix.get(0, 1));
        assertEquals(4, subMatrix.get(1, 0));
        assertEquals(5, subMatrix.get(1, 1));

        // Try to get a non-existent submatrix of size 0x2
        assertThrows(IllegalArgumentException.class, () -> matrix.getSubMatrix(0, 0, 0, 2));

        // Get a submatrix of size 2x2 starting from (1, 1)
        subMatrix = matrix.getSubMatrix(1, 1, 2, 2);

        assertEquals(2, subMatrix.getWidth());
        assertEquals(2, subMatrix.getHeight());
        assertEquals(2 * 2, subMatrix.getSize());

        assertEquals(5, subMatrix.get(0, 0));
        assertEquals(6, subMatrix.get(0, 1));
        assertEquals(8, subMatrix.get(1, 0));
        assertEquals(9, subMatrix.get(1, 1));

        // Get a submatrix of size 5x5 starting from (-1, -1)
        subMatrix = matrix.getSubMatrix(-1, -1, 5, 5);

        assertEquals(5, subMatrix.getWidth());
        assertEquals(5, subMatrix.getHeight());
        assertEquals(5 * 5, subMatrix.getSize());

        assertEquals(0, subMatrix.get(0, 0));
        assertEquals(0, subMatrix.get(0, 1));
        assertEquals(0, subMatrix.get(0, 2));
        assertEquals(0, subMatrix.get(0, 3));
        assertEquals(0, subMatrix.get(0, 4));
        assertEquals(0, subMatrix.get(1, 0));
        assertEquals(1, subMatrix.get(1, 1));
        assertEquals(2, subMatrix.get(1, 2));
        assertEquals(3, subMatrix.get(1, 3));
        assertEquals(0, subMatrix.get(1, 4));
        assertEquals(0, subMatrix.get(2, 0));
        assertEquals(4, subMatrix.get(2, 1));
        assertEquals(5, subMatrix.get(2, 2));
        assertEquals(6, subMatrix.get(2, 3));
        assertEquals(0, subMatrix.get(2, 4));
        assertEquals(0, subMatrix.get(3, 0));
        assertEquals(7, subMatrix.get(3, 1));
        assertEquals(8, subMatrix.get(3, 2));
        assertEquals(9, subMatrix.get(3, 3));
        assertEquals(0, subMatrix.get(3, 4));
        assertEquals(0, subMatrix.get(4, 0));
        assertEquals(0, subMatrix.get(4, 1));
        assertEquals(0, subMatrix.get(4, 2));
        assertEquals(0, subMatrix.get(4, 3));
        assertEquals(0, subMatrix.get(4, 4));
    }

    @Test
    void clear() {
        Matrix<Integer> matrix = getIntegerMatrix();

        matrix.clear(0, 0);
        matrix.clear(0, 1);
        matrix.clear(0, 2);
        matrix.clear(1, 0);
        matrix.clear(1, 1);
        matrix.clear(1, 2);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(0, matrix.get(i, j));
            }
        }

        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));

        matrix = new Matrix<>(3, 3, null);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix.set(i, 3, i * 3 + j);
            }
        }

        matrix.clear(0, 3);

        assertNull(matrix.get(0, 3));
    }

    @Test
    void match() {
        Matrix<Integer> matrix_1 = getIntegerMatrix();

        // A matrix should match itself
        assertTrue(matrix_1.match(matrix_1, null));
        assertTrue(matrix_1.match(matrix_1));

        // A matrix should not match a different matrix
        Matrix<Integer> matrix_2 = new Matrix<>(3, 3, 0);
        assertFalse(matrix_1.match(matrix_2, null));
        assertFalse(matrix_1.match(matrix_2));

        // A matrix should match a matrix of only wildcards, when specifying a wildcard
        Matrix<Integer> matrix_3 = new Matrix<>(3, 3, 1);
        assertTrue(matrix_1.match(matrix_3, 1));
        assertFalse(matrix_1.match(matrix_3));

        // A matrix should not match a matrix with different size
        Matrix<Integer> matrix_4 = new Matrix<>(2, 2, 0);
        assertFalse(matrix_1.match(matrix_4, null));
        assertFalse(matrix_1.match(matrix_4));

        // A matrix should match a matrix with the same elements or a wildcard
        Matrix<Integer> matrix_5 = getIntegerMatrix();
        matrix_5.set(0, 0, -1);
        assertTrue(matrix_1.match(matrix_5, -1));
        assertFalse(matrix_1.match(matrix_5));
    }

    @Test
    void dimensions() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);

        assertEquals(3, matrix.getWidth());
        assertEquals(3, matrix.getHeight());
        assertEquals(3 * 3, matrix.getSize());
        assertEquals(0, matrix.getMinX());
        assertEquals(0, matrix.getMinY());
        assertEquals(2, matrix.getMaxX());
        assertEquals(2, matrix.getMaxY());

        // Insert new item in position (3, 3)
        matrix.set(3, 3, 16);

        assertEquals(4, matrix.getWidth());
        assertEquals(4, matrix.getHeight());
        assertEquals(4 * 4, matrix.getSize());
        assertEquals(0, matrix.getMinX());
        assertEquals(0, matrix.getMinY());
        assertEquals(3, matrix.getMaxX());
        assertEquals(3, matrix.getMaxY());

        // Insert new item in position (0, 3)
        matrix.set(0, 3, 255);

        assertEquals(4, matrix.getWidth());
        assertEquals(4, matrix.getHeight());
        assertEquals(4 * 4, matrix.getSize());
        assertEquals(0, matrix.getMinX());
        assertEquals(0, matrix.getMinY());
        assertEquals(3, matrix.getMaxX());
        assertEquals(3, matrix.getMaxY());

        // Insert new item in position (-20, -20)
        matrix.set(-20, -20, 1024);

        assertEquals(24, matrix.getWidth());
        assertEquals(24, matrix.getHeight());
        assertEquals(24 * 24, matrix.getSize());
        assertEquals(-20, matrix.getMinX());
        assertEquals(-20, matrix.getMinY());
        assertEquals(3, matrix.getMaxX());
        assertEquals(3, matrix.getMaxY());
    }

    @Test
    void matchWithNullValues() {
        Matrix<Integer> matrix = new Matrix<>(3, 3);

        matrix.set(0, 1, 1);
        matrix.set(1, 0, 1);

        Matrix<Integer> compareMatrix = new Matrix<>(3, 3);

        compareMatrix.set(0, 1, 2);
        compareMatrix.set(1, 0, 2);

        assertFalse(matrix.match(compareMatrix));
    }

    @Test
    void copy() {
        Matrix<Integer> matrix = getIntegerMatrix();
        Matrix<Integer> copy = matrix.copy();

        assertTrue(matrix.match(copy));
        assertTrue(copy.match(matrix));
        assertEquals(matrix.getWidth(), copy.getWidth());
        assertEquals(matrix.getHeight(), copy.getHeight());
        assertEquals(matrix.getSize(), copy.getSize());
        assertNotSame(matrix, copy);
    }

    @Test
    void patternMatching() {

        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);
        Matrix<Integer> pattern = new Matrix<>(3, 3, 0);

        // Test a generic case, the matrix is an empty 3x3 matrix with a value in the center
        matrix.set(0, 0, 0);
        matrix.set(0, 1, 0);
        matrix.set(0, 2, 0);
        matrix.set(1, 0, 0);
        matrix.set(1, 1, 1);
        matrix.set(1, 2, 0);
        matrix.set(2, 0, 0);
        matrix.set(2, 1, 0);
        matrix.set(2, 2, 0);

        // The pattern is an empty 3x3 matrix with a value in the center
        pattern.set(0, 0, 0);
        pattern.set(0, 1, 0);
        pattern.set(0, 2, 0);
        pattern.set(1, 0, 0);
        pattern.set(1, 1, 15);
        pattern.set(1, 2, 0);
        pattern.set(2, 0, 0);
        pattern.set(2, 1, 0);
        pattern.set(2, 2, 0);

        assertTrue(matrix.containsShape(pattern)); // The pattern is in the center of the matrix

        pattern.set(0, 0, 19);

        assertFalse(matrix.containsShape(pattern)); // Trying to match a pattern that is not in the matrix

        pattern.set(2, 2, 69);
        matrix.set(2, 2, -10);
        matrix.set(0, 0, 43);

        assertTrue(matrix.containsShape(pattern)); // The pattern is a diagonal in the matrix

        pattern = new Matrix<>(2, 2, 0);

        assertFalse(matrix.containsShape(pattern)); // The pattern is bigger than the matrix

        //Trying an empty pattern
        matrix = new Matrix<>(3, 3, 0);
        pattern = new Matrix<>(3, 3, 0);

        assertTrue(matrix.containsShape(pattern)); // The pattern is empty, so it is in the matrix

        //Trying with null values as emptyValue
        matrix = new Matrix<>(2, 2, null);
        pattern = new Matrix<>(2, 2, null);

        assertTrue(matrix.containsShape(pattern)); // The pattern is empty, so it is in the matrix

        matrix.set(0, 0, 1);
        matrix.set(0, 1, 1);
        matrix.set(1, 0, 1);
        pattern.set(0, 0, 12);
        pattern.set(0, 1, 12);
        pattern.set(1, 0, 12);
        pattern.set(1, 1, null);

        assertTrue(matrix.containsShape(pattern)); // The pattern is in the matrix

        //Trying with null values but emptyValue is not null
        matrix = new Matrix<>(2, 2, 0);
        pattern = new Matrix<>(2, 2, 0);

        matrix.set(0, 0, null);
        pattern.set(0, 0, 12);
        assertTrue(matrix.containsShape(pattern)); // The pattern is in the matrix

        matrix = new Matrix<Integer>(4, 4, 0);
        pattern = new Matrix<>(4, 4, 0);


        for (int i = 0; i < 4; i++) {
            matrix.set(0, i, 1);
            matrix.set(1, i, 1);
        }

        matrix.set(2, 0, 0);
        matrix.set(2, 1, 1);
        matrix.set(2, 2, 1);
        matrix.set(2, 3, 1);
        matrix.set(3, 0, 0);
        matrix.set(3, 1, 0);
        matrix.set(3, 2, 1);
        matrix.set(3, 3, 1);
        pattern.set(0, 0, 1);
        pattern.set(0, 1, 1);
        pattern.set(0, 2, 0);
        pattern.set(0, 3, 0);
        pattern.set(1, 0, 1);
        pattern.set(1, 1, 1);
        pattern.set(1, 2, 1);
        pattern.set(1, 3, 0);
        pattern.set(2, 0, 0);
        pattern.set(2, 1, 1);
        pattern.set(2, 2, 1);
        pattern.set(2, 3, 1);
        pattern.set(3, 0, 0);
        pattern.set(3, 1, 0);
        pattern.set(3, 2, 1);
        pattern.set(3, 3, 1);

        assertTrue(matrix.containsShape(pattern)); // The pattern is in the matrix
    }

}