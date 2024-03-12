package it.polimi.ingsw.am07.utils;

import java.util.Iterator;

public class MatrixElementIterator <T> implements Iterator<T> {

    private final Matrix<T> matrix;

    private int currentX, currentY;

    public MatrixElementIterator(Matrix<T> matrix) {
        this.matrix = matrix;
        currentX = matrix.getMinX();
        currentY = matrix.getMinY();
    }

    @Override
    public boolean hasNext() {
        return currentX < matrix.getMaxX() || currentY < matrix.getMaxY();
    }

    @Override
    public T next() {
        T element = matrix.get(currentX, currentY);
        currentX++;
        if (currentX > matrix.getMaxX()) {
            currentX = matrix.getMinX();
            currentY++;
        }
        return element;
    }

    @Override
    public void remove() {
        matrix.clear(currentX, currentY);
    }

}
