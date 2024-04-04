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

package it.polimi.ingsw.am07.model.game.gamefield;

import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import it.polimi.ingsw.am07.utils.matrix.MatrixElementIterator;
import it.polimi.ingsw.am07.utils.matrix.MatrixSubMatrixIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameFieldPatternTest {

    static final Symbol[] allowedSymbols = {Symbol.EMPTY, Symbol.RED, Symbol.GREEN, Symbol.BLUE, Symbol.PURPLE};
    static final int max_iterations = 100;

    @Test
    void newGameFieldPatternTest() {
        //invalid Symbols
        Matrix<Symbol> pattern = new Matrix<>(3, 4, Symbol.EMPTY);
        pattern.set(0, 0, Symbol.RED);
        pattern.set(1, 1, Symbol.BLUE);
        pattern.set(3, 1, Symbol.SCROLL);
        assertThrows(IllegalArgumentException.class, () -> new GameFieldPattern(pattern));

        //edge Case: empty pattern
        Matrix<Symbol> pattern2 = new Matrix<>(3, 4, Symbol.EMPTY);
        assertDoesNotThrow(() -> new GameFieldPattern(pattern2));
    }

    @Test
    void getShapeTest() {
        /**
         * Testing pattern
         * R R
         * R B B
         *   B B
         *   B B
         *   B B
         */
        Matrix<Symbol> pattern = new Matrix<>(5, 4, Symbol.EMPTY);
        pattern.set(0, 0, Symbol.RED);
        pattern.set(1, 1, Symbol.BLUE);
        pattern.set(3, 1, Symbol.BLUE);
        GameFieldPattern gameFieldPattern = new GameFieldPattern(pattern);
        Matrix<Symbol> shape = gameFieldPattern.getShape();

        assertTrue(shape.containsShape(pattern)); //Of course the expanded shape contains the pattern
        assertEquals(Symbol.RED, shape.get(0, 0));
        assertEquals(Symbol.RED, shape.get(1, 0));
        assertEquals(Symbol.RED, shape.get(0, 1));
        assertEquals(Symbol.BLUE, shape.get(1, 1));
        assertEquals(Symbol.BLUE, shape.get(2, 1));
        assertEquals(Symbol.BLUE, shape.get(1, 2));
        assertEquals(Symbol.BLUE, shape.get(2, 2));
        assertEquals(Symbol.BLUE, shape.get(3, 1));
        assertEquals(Symbol.BLUE, shape.get(3, 2));
        assertEquals(Symbol.BLUE, shape.get(4, 1));
        assertEquals(Symbol.BLUE, shape.get(4, 2));

        /** Testing pattern
         * R R
         * R R R
         *   R R R
         *     R R
         */
        Matrix<Symbol> pattern1 = new Matrix<>(4, 4, Symbol.EMPTY);
        GameFieldPattern gameFieldPattern1 = new GameFieldPattern(pattern1);
        pattern1.set(0, 0, Symbol.RED);
        pattern1.set(1, 1, Symbol.RED);
        pattern1.set(2, 2, Symbol.RED);

        Matrix<Symbol> shape1 = gameFieldPattern1.getShape();
        assertTrue(shape1.containsShape(pattern1));

        assertEquals(Symbol.RED, shape1.get(0, 0));
        assertEquals(Symbol.RED, shape1.get(1, 0));
        assertEquals(Symbol.RED, shape1.get(0, 1));
        assertEquals(Symbol.RED, shape1.get(1, 1));
        assertEquals(Symbol.RED, shape1.get(2, 1));
        assertEquals(Symbol.RED, shape1.get(1, 2));
        assertEquals(Symbol.RED, shape1.get(2, 2));
        assertEquals(Symbol.RED, shape1.get(3, 2));
        assertEquals(Symbol.RED, shape1.get(2, 3));
        assertEquals(Symbol.RED, shape1.get(3, 3));
        assertEquals(Symbol.EMPTY, shape1.get(0, 2));

        //empty pattern -> should always match no matter the shape
        Matrix<Symbol> pattern2 = new Matrix<>(3, 3, Symbol.EMPTY);
        GameFieldPattern gameFieldPattern2 = new GameFieldPattern(pattern2);

        Matrix<Symbol> gameField = new Matrix<>(5, 5, Symbol.EMPTY);
        //fill the gameField with random symbols
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int random = (int) (Math.random() * 5);
                gameField.set(i, j, Symbol.values()[random]);
            }
        }

        MatrixSubMatrixIterator<Symbol> iterator = gameField.iterator(pattern2.getWidth(), pattern2.getHeight());
        //check if every submatrix of the gameField contains the pattern
        while (iterator.hasNext()) {
            Matrix<Symbol> subMatrix = iterator.next();
            assertTrue(subMatrix.containsShape(pattern2));
        }
    }

    @Test
    void randomMatrixShapeTest() {
        for (int i = 0; i < max_iterations; i++) {
            int rows = (int) (Math.random() * 5) + 1;
            int cols = (int) (Math.random() * 5) + 1;
            Matrix<Symbol> pattern = new Matrix<>(rows, cols, Symbol.EMPTY);
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < cols; k++) {
                    if ((j + k) % 2 != 0) {
                        pattern.set(j, k, Symbol.EMPTY);
                        continue;
                    }
                    int random = (int) (Math.random() * 5);
                    pattern.set(j, k, allowedSymbols[random]);
                }
            }
            GameFieldPattern gameFieldPattern = new GameFieldPattern(pattern);
            Matrix<Symbol> shape = gameFieldPattern.getShape();
            //check if at least 1 submatrix of the shape contains the pattern
            boolean found = false;

            MatrixSubMatrixIterator<Symbol> iterator = shape.iterator(pattern.getWidth(), pattern.getHeight());
            while (iterator.hasNext()) {
                Matrix<Symbol> subMatrix = iterator.next();
                if (subMatrix.containsShape(pattern)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void getCards() {
        Matrix<Symbol> pattern = new Matrix<>(3, 4, Symbol.EMPTY);
        pattern.set(0, 0, Symbol.RED);
        pattern.set(1, 1, Symbol.BLUE);
        pattern.set(3, 1, Symbol.BLUE);
        GameFieldPattern gameFieldPattern = new GameFieldPattern(pattern);
        // Iterate through the pattern and check if the cards are placed correctly
        MatrixElementIterator<Symbol> iterator = (MatrixElementIterator<Symbol>) gameFieldPattern.pattern().iterator();

        int cards = 0;
        while (iterator.hasNext()) {
            Symbol s = iterator.next();
            if (!Symbol.EMPTY.equals(s)) {
                cards++;
                switch (cards) {
                    case 1:
                        assertEquals(Symbol.RED, s);
                        break;
                    case 2:
                    case 3:
                        assertEquals(Symbol.BLUE, s);
                        break;
                    default:
                        fail("Too many cards in the pattern");
                }
            }
        }
        //random pattern
        for (int i = 0; i < max_iterations; i++) {
            int rows = (int) (Math.random() * 5) + 1;
            int cols = (int) (Math.random() * 5) + 1;
            int patternCards = 0;
            Matrix<Symbol> pattern1 = new Matrix<>(rows, cols, Symbol.EMPTY);
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < cols; k++) {
                    if ((j + k) % 2 != 0) {
                        pattern1.set(j, k, Symbol.EMPTY);
                        continue;
                    }
                    int random = (int) (Math.random() * 5);
                    Symbol s = allowedSymbols[random];
                    if (!Symbol.EMPTY.equals(s)) {
                        patternCards++;
                    }
                    pattern1.set(j, k, s);
                }
            }
            GameFieldPattern gameFieldPattern1 = new GameFieldPattern(pattern1);
            MatrixElementIterator<Symbol> iterator1 = (MatrixElementIterator<Symbol>) gameFieldPattern1.pattern().iterator();
            int cards1 = 0;
            while (iterator1.hasNext()) {
                Symbol s = iterator1.next();
                if (!Symbol.EMPTY.equals(s)) {
                    cards1++;
                }
            }
            assertEquals(cards1, patternCards);
        }
    }

}
