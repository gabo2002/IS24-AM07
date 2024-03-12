package it.polimi.ingsw.am07.utils;

import java.util.ArrayList;

public class Matrix<T> {

    private final ArrayList<T> data;

    private final T emptyValue;

    private int centerX, centerY;

    private int minX, minY, maxX, maxY;

    private int sizeX, sizeY;

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
        maxX = rows;
        maxY = columns;
    }

    public Matrix(int rows, int columns) {
        this(rows, columns, null);
    }

    public T get(int x, int y) {
        if (x >= maxX || x < minX || y >= maxY || y < minY) {
            return emptyValue;
        }
        int new_x = x + centerX;
        int new_y = y + centerY;
        return data.get(new_x + new_y * sizeX);
    }

    public void set(int x, int y, T value) {
        extendTo(x, y);
        int new_x = x + centerX;
        int new_y = y + centerY;
        data.set(new_x + new_y * sizeX, value);
    }

    public void clear(int x, int y) {
        if (x >= maxX || x < minX || y >= maxY || y < minY) {
            return;
        }
        set(x, y, emptyValue);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getSize() {
        return sizeX * sizeY;
    }

    private void extendTo(int x, int y) {
        if (x > minX && x < maxX && y > minY && y < maxY) {
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
        while (x >= maxX) {
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
        while (y >= maxY) {
            for (int i = 0; i < sizeX; i++) {
                data.add(emptyValue);
            }
            maxY++;
            sizeY++;
        }
    }

}
