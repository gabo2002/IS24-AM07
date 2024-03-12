package it.polimi.ingsw.am07.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixElementIteratorTest {

    @Test
    void testHasNext() {
        Matrix<Integer> matrix = new Matrix<>(3, 3, 0);
        MatrixElementIterator<Integer> iterator = new MatrixElementIterator<>(matrix);

        for (int i = 0; i < 9; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void testNext() {
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
    void testRemove() {
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