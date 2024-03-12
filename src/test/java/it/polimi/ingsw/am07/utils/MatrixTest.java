package it.polimi.ingsw.am07.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
    @Test
    void testGetAndSet() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, 3);
        matrix.set(1, 0, 4);
        matrix.set(1, 1, 5);
        matrix.set(1, 2, 6);
        matrix.set(2, 0, 7);
        matrix.set(2, 1, 8);
        matrix.set(2, 2, 9);
        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));

        assertEquals(3, matrix.getSizeX());
        assertEquals(3, matrix.getSizeY());
        assertEquals(3 * 3, matrix.getSize());
    }

    private static Matrix<Integer> getIntegerMatrix() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, 3);
        matrix.set(1, 0, 4);
        matrix.set(1, 1, 5);
        matrix.set(1, 2, 6);
        matrix.set(2, 0, 7);
        matrix.set(2, 1, 8);
        matrix.set(2, 2, 9);
        return matrix;
    }

    @Test
    void testPositiveExtension() {
        Matrix<Integer> matrix = getIntegerMatrix();

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));

        assertEquals(3, matrix.getSizeX());
        assertEquals(3, matrix.getSizeY());
        assertEquals(3 * 3, matrix.getSize());

        // Insert new item in position (3, 3)
        matrix.set(3, 3, 16);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));
        assertEquals(16, matrix.get(3, 3));
        assertEquals(0, matrix.get(3, 0));
        assertEquals(0, matrix.get(3, 1));
        assertEquals(0, matrix.get(3, 2));
        assertEquals(0, matrix.get(0, 3));
        assertEquals(0, matrix.get(1, 3));
        assertEquals(0, matrix.get(2, 3));

        assertEquals(4, matrix.getSizeX());
        assertEquals(4, matrix.getSizeY());
        assertEquals(4 * 4, matrix.getSize());

        // Insert new item in position (0, 3)
        matrix.set(0, 3, 255);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));
        assertEquals(16, matrix.get(3, 3));
        assertEquals(0, matrix.get(3, 0));
        assertEquals(0, matrix.get(3, 1));
        assertEquals(0, matrix.get(3, 2));
        assertEquals(255, matrix.get(0, 3));
        assertEquals(0, matrix.get(1, 3));
        assertEquals(0, matrix.get(2, 3));

        assertEquals(4, matrix.getSizeX());
        assertEquals(4, matrix.getSizeY());
        assertEquals(4 * 4, matrix.getSize());

        //Insert new item in position (0, 300)
        matrix.set(0, 300, 1024);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));
        assertEquals(16, matrix.get(3, 3));
        assertEquals(0, matrix.get(3, 0));
        assertEquals(0, matrix.get(3, 1));
        assertEquals(0, matrix.get(3, 2));
        assertEquals(255, matrix.get(0, 3));
        assertEquals(0, matrix.get(1, 3));
        assertEquals(0, matrix.get(2, 3));
        assertEquals(1024, matrix.get(0, 300));

        assertEquals(4, matrix.getSizeX());
        assertEquals(301, matrix.getSizeY());
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

    @org.junit.jupiter.api.Test
    void testNegativeExtension() {
        Matrix<Integer> matrix = getIntegerMatrix();
        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));

        // Insert new in position (-1, -1)
        matrix.set(-1, -1, 10);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));
        assertEquals(10, matrix.get(-1, -1));
        assertEquals(0, matrix.get(-1, 0));
        assertEquals(0, matrix.get(-1, 1));
        assertEquals(0, matrix.get(-1, 2));
        assertEquals(0, matrix.get(0, -1));
        assertEquals(0, matrix.get(1, -1));
        assertEquals(0, matrix.get(2, -1));

        assertEquals(4, matrix.getSizeX());
        assertEquals(4, matrix.getSizeY());
        assertEquals(4 * 4, matrix.getSize());

        // Insert new in position (-8, -8)
        matrix.set(-8, -8, 15);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));
        assertEquals(10, matrix.get(-1, -1));
        assertEquals(0, matrix.get(-1, 0));
        assertEquals(0, matrix.get(-1, 1));
        assertEquals(0, matrix.get(-1, 2));
        assertEquals(0, matrix.get(0, -1));
        assertEquals(0, matrix.get(1, -1));
        assertEquals(0, matrix.get(2, -1));
        assertEquals(15, matrix.get(-8, -8));

        assertEquals(11, matrix.getSizeX());
        assertEquals(11, matrix.getSizeY());
        assertEquals(11 * 11, matrix.getSize());

        //Insert new in position (15, 15)
        matrix.set(15, 15, 1024);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(9, matrix.get(2, 2));
        assertEquals(10, matrix.get(-1, -1));
        assertEquals(0, matrix.get(-1, 0));
        assertEquals(0, matrix.get(-1, 1));
        assertEquals(0, matrix.get(-1, 2));
        assertEquals(0, matrix.get(0, -1));
        assertEquals(0, matrix.get(1, -1));
        assertEquals(0, matrix.get(2, -1));
        assertEquals(15, matrix.get(-8, -8));
        assertEquals(1024, matrix.get(15, 15));

        assertEquals(24, matrix.getSizeX());
        assertEquals(24, matrix.getSizeY());
        assertEquals(24 * 24, matrix.getSize());
    }

}