package it.polimi.ingsw.am07.utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A matrix of generic type T.
 * It is implemented as a list of elements, with a fixed number of rows and columns.
 * The matrix can be extended in any direction, and the elements are initialized to a default value.
 * @param <T> the type of the elements in the matrix
 * @author Roberto Alessandro Bertolini
 */
public class Matrix<T> implements Iterable<T> {

    private final ArrayList<T> data;

    private final T emptyValue;

    private int centerX, centerY;

    private int minX, minY, maxX, maxY;

    private int sizeX, sizeY;

    /**
     * Create a new matrix with the given number of rows and columns.
     * @param rows          the number of rows
     * @param columns       the number of columns
     * @param emptyValue    the default value for every element of the matrix (can be null)
     * @author Roberto Alessandro Bertolini
     */
    public Matrix(int rows, int columns, T emptyValue) {
        data = new ArrayList<>(rows * columns);
        for (int i = 0; i < rows * columns; i++) {
            data.add(emptyValue);
        }
        this.emptyValue = emptyValue;
        centerX = 0;
        centerY = 0;
        minX = 0;
        minY = 0;
        sizeX = rows;
        sizeY = columns;
        maxX = rows - 1;
        maxY = columns - 1;
    }

    /**
     * Create a new matrix with the given number of rows and columns. The default value for the elements is null.
     * @param rows      the number of rows
     * @param columns   the number of columns
     * @author Roberto Alessandro Bertolini
     */
    public Matrix(int rows, int columns) {
        this(rows, columns, null);
    }

    /**
     * Get a specific element
     * @param x the row index
     * @param y the column index
     * @return the element at the position (x, y), or the default value if the position is out of bounds
     * @author Roberto Alessandro Bertolini
     */
    public T get(int x, int y) {
        if (x > maxX || x < minX || y > maxY || y < minY) {
            return emptyValue;
        }
        int new_x = x + centerX;
        int new_y = y + centerY;
        return data.get(new_x + new_y * sizeX);
    }

    /**
     * Get a sub-matrix of the current matrix, starting from the given position and with the given number of rows and columns.
     * @param x         the row index of the starting position
     * @param y         the column index of the starting position
     * @param rows      the number of rows of the sub-matrix
     * @param columns   the number of columns of the sub-matrix
     * @return          the sub-matrix of size (rows, columns) starting from the position (x, y)
     * @throws IllegalArgumentException if the number of rows or columns is invalid
     * @author Roberto Alessandro Bertolini
     */
    public Matrix<T> getSubMatrix(int x, int y, int rows, int columns) throws IllegalArgumentException {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("The number of rows and columns must be positive");
        }

        Matrix<T> subMatrix = new Matrix<>(rows, columns, emptyValue);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                subMatrix.set(i, j, get(x + i, y + j));
            }
        }
        return subMatrix;
    }

    /**
     * Set a specific element
     * @param x the row index
     * @param y the column index
     * @param value the new value for the element (can be null)
     * @author Roberto Alessandro Bertolini
     */
    public void set(int x, int y, T value) {
        if (value == emptyValue && (x > maxX || x < minX || y > maxY || y < minY)) {
            // No need to add an empty value to the matrix when out of bounds
            return;
        }
        extendTo(x, y);
        int new_x = x + centerX;
        int new_y = y + centerY;
        data.set(new_x + new_y * sizeX, value);
    }

    /**
     * Clear a specific element, setting it to the default value
     * @param x the row index
     * @param y the column index
     * @author Roberto Alessandro Bertolini
     */
    public void clear(int x, int y) {
        if (x > maxX || x < minX || y > maxY || y < minY) {
            return;
        }
        set(x, y, emptyValue);
    }

    /**
     * Check if two different matrices are equal, element by element
     * @param other      the other matrix to compare
     * @param wildcard   a value to ignore when comparing the elements
     * @return           true if the two matrices are equal, false otherwise
     * @author Roberto Alessandro Bertolini
     */
    public boolean match(Matrix<T> other, T wildcard) {
        if (other.getWidth() != getWidth() || other.getHeight() != getHeight()) {
            return false;
        }
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                T value = get(i, j);
                T otherValue = other.get(i, j);
                if (value == null) {
                    return otherValue == null || otherValue.equals(wildcard);
                }
                if (otherValue != wildcard && !value.equals(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if two different matrices are equal, element by element
     * @param other the other matrix to compare
     * @return      true if the two matrices are equal, false otherwise
     * @author Roberto Alessandro Bertolini
     */
    public boolean match(Matrix<T> other) {
        if (other.getWidth() != getWidth() || other.getHeight() != getHeight()) {
            return false;
        }
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                T value = get(i, j);
                T otherValue = other.get(i, j);
                if (value == null) {
                    return otherValue == null;
                }
                if (!value.equals(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the number of rows of the matrix
     * @return the number of rows
     * @author Roberto Alessandro Bertolini
     */
    public int getWidth() {
        return sizeX;
    }

    /**
     * Get the number of columns of the matrix
     * @return the number of columns
     * @author Roberto Alessandro Bertolini
     */
    public int getHeight() {
        return sizeY;
    }

    /**
     * Get the minimum row index of the matrix
     * @return the minimum row index
     * @author Roberto Alessandro Bertolini
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Get the maximum row index of the matrix
     * @return the maximum row index
     * @author Roberto Alessandro Bertolini
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Get the minimum column index of the matrix
     * @return the minimum column index
     * @author Roberto Alessandro Bertolini
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Get the maximum column index of the matrix
     * @return the maximum column index
     * @author Roberto Alessandro Bertolini
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Get the total size of the matrix
     * @return the total size of the matrix
     * @author Roberto Alessandro Bertolini
     */
    public int getSize() {
        return sizeX * sizeY;
    }

    private void extendTo(int x, int y) {
        if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
            return;
        }

        while (x < minX) {
            for (int i = 0; i < sizeY; i++) {
                data.add(i * (sizeX + 1), emptyValue);
            }
            minX--;
            centerX++;
            sizeX++;
        }
        while (x > maxX) {
            for (int i = 1; i <= sizeY; i++) {
                data.add(i * (sizeX + 1) - 1, emptyValue);
            }
            maxX++;
            sizeX++;
        }

        while (y < minY) {
            for (int i = 0; i < sizeX; i++) {
                data.add(i, emptyValue);
            }
            minY--;
            centerY++;
            sizeY++;
        }
        while (y > maxY) {
            for (int i = 0; i < sizeX; i++) {
                data.add(emptyValue);
            }
            maxY++;
            sizeY++;
        }
    }

    /**
     * Get an iterator for the elements of the matrix
     * @return an iterator for the elements of the matrix
     * @see MatrixElementIterator
     * @author Roberto Alessandro Bertolini
     */
    @Override
    public Iterator<T> iterator() {
        return new MatrixElementIterator<>(this);
    }

    /**
     * Get an iterator for the sub-matrices of the matrix.
     * @param rows      the number of rows of the sub-matrices
     * @param columns   the number of columns of the sub-matrices
     * @return          an iterator for the sub-matrices of the matrix
     * @see MatrixSubMatrixIterator
     * @author Roberto Alessandro Bertolini
     */
    public Iterator<Matrix<T>> iterator(int rows, int columns) {
        return new MatrixSubMatrixIterator<>(this, rows, columns, false);
    }

}
