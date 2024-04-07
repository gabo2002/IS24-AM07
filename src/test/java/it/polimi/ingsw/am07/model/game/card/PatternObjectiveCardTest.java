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

package it.polimi.ingsw.am07.model.game.card;

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPattern;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFieldRepresentation;
import it.polimi.ingsw.am07.utils.json.GameDataJsonParser;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatternObjectiveCardTest {
    private final static int RANDOM_TEST_ITERATIONS = 100;

    @Test
    void calculateScoreEmptyField() {
        GameField field = new GameField();
        GameFieldPattern pattern = getLeftDiagonalPattern(Symbol.RED);
        PatternObjectiveCard card = new PatternObjectiveCard(5, pattern);
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));
        pattern = getRightDiagonalPattern(Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));

        for (int i = 0; i < 5; i++) {
            pattern = getLPattern(i, Symbol.RED, Symbol.BLUE);
            card = new PatternObjectiveCard(5, pattern);
            assertEquals(0, card.calculateScore(new ResourceHolder(), field));
        }
    }

    @Test
    void calculateSingleCardPattern() {
        Matrix<Symbol> patternMatrix = new Matrix<>(1,1, Symbol.EMPTY);
        patternMatrix.set(0, 0, Symbol.RED);
        ObjectiveCard card = new PatternObjectiveCard(1, new GameFieldPattern(patternMatrix));
        Matrix<Symbol> patternMatrix2 = new Matrix<>(1,1, Symbol.EMPTY);
        patternMatrix2.set(0, 0, Symbol.BLUE);
        ObjectiveCard card2 = new PatternObjectiveCard(1, new GameFieldPattern(patternMatrix2));

        //generate random field
        for (int i = 0; i < RANDOM_TEST_ITERATIONS; i++) {
            GameField field = new GameField();
            int elements = (int) (Math.random() * 10);
            int numberOfReds = (int) (Math.random() * elements);

            //random position
            List<GameFieldPosition> positions = getRandomPositionWithoutOverlap(elements);
            for (int j = 0; j < elements; j++) {
                Symbol color = j < numberOfReds ? Symbol.RED : Symbol.BLUE;
                Side side = getGenericSide(color);
                field.placeOnFieldAt(side, positions.get(j));
            }
            assertEquals(numberOfReds, card.calculateScore(new ResourceHolder(), field));
            assertEquals(elements - numberOfReds, card2.calculateScore(new ResourceHolder(), field));
        }
    }

    @Test
    void calculateScoreComplexPatterns() {
        /*
            R
           R B B
            B B
         */
        GameField field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -1, 0));
        GameFieldPattern pattern = getLPattern(3, Symbol.RED, Symbol.BLUE);
        PatternObjectiveCard card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        //A very complex pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(-1, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(-2, -2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(-3, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(-2, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, -2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(-1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(-2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 3, 0));
        pattern = getLPattern(3, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(15, card.calculateScore(new ResourceHolder(), field));
        pattern = getLeftDiagonalPattern(Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));
        pattern = getRightDiagonalPattern(Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));
    }

    static List<GameFieldPosition> getRandomPositionWithoutOverlap( int size) {
        int x = 0, y = 0;
        List<GameFieldPosition> positions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            boolean placed = false;
            while (!placed) {
                placed = true;
                x = (int) (Math.random() * 20);
                y = (int) (Math.random() * 20);

                if ((x+y) % 2 != 0) {
                    placed = false;
                }

                for(GameFieldPosition position : positions) {
                    if(position.x() == x && position.y() == y) {
                        placed = false;
                        break;
                    }
                }
            }
            positions.add(new GameFieldPosition(x, y, 0));
        }
        return positions;
    }

    static Side getGenericSide(Symbol symbol) {
        Matrix<Symbol> matrix = new Matrix<>(2, 2, Symbol.BLANK);

        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(matrix);

        return new SideBack(1, sideFieldRepresentation, new ResourceHolder(), symbol);
    }

    static GameFieldPattern getRightDiagonalPattern(Symbol color) {
        Matrix<Symbol> patternMatrix = new Matrix<>(2, 2, Symbol.EMPTY);
        patternMatrix.set(0, 2, color);
        patternMatrix.set(1, 1, color);
        patternMatrix.set(2, 0, color);
        return new GameFieldPattern(patternMatrix);
    }
    static GameFieldPattern getLeftDiagonalPattern(Symbol color) {
        Matrix<Symbol> patternMatrix = new Matrix<>(2, 2, Symbol.EMPTY);
        patternMatrix.set(0, 0, color);
        patternMatrix.set(1, 1, color);
        patternMatrix.set(2, 2, color);
        return new GameFieldPattern(patternMatrix);
    }

    static GameFieldPattern getLPattern(int type, Symbol shortSide, Symbol longSide) {
        Matrix<Symbol> patternMatrix = new Matrix<>(2, 2, Symbol.EMPTY);
        if (type == 0) {
            patternMatrix.set(0, 0, longSide);
            patternMatrix.set(0, 2, longSide);
            patternMatrix.set(1, 3, shortSide);
        } else if (type == 1) {
            patternMatrix.set(1, 0, longSide);
            patternMatrix.set(1, 2, longSide);
            patternMatrix.set(0, 3, shortSide);
        } else if (type == 2) {
            patternMatrix.set(1, 0, shortSide);
            patternMatrix.set(0, 1, longSide);
            patternMatrix.set(0, 3, longSide);
        } else if (type == 3) {
            patternMatrix.set(0, 0, shortSide);
            patternMatrix.set(1, 1, longSide);
            patternMatrix.set(1, 3, longSide);
        } else if (type == 4) {
            patternMatrix.set(0, 0, shortSide);
            patternMatrix.set(1, 1, longSide);
            patternMatrix.set(3, 1, longSide);
        }
        else {
            throw new IllegalArgumentException("Invalid L pattern type");
        }
        return new GameFieldPattern(patternMatrix);
    }

    @Test
    void calculateScore() {
        // Test 1: diagonal pattern, empty field
        GameField field = new GameField();
        GameFieldPattern pattern = getLeftDiagonalPattern(Symbol.RED);
        PatternObjectiveCard card = new PatternObjectiveCard(5, pattern);
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));

        // Test 2: diagonal pattern, field with the same pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(2, 2, 0));
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 3: diagonal pattern, field with a different pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 2, 0));
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));

        // Test 4: diagonal pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(3, 3, 0));
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 5: diagonal pattern, field with pattern overlap but two matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(3, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(4, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(5, 5, 0));
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 6: diagonal pattern, field with pattern overlap but two matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(3, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(4, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(5, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(6, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(7, 7, 0));
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 7: diagonal pattern, field with pattern overlap but three matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(3, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(4, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(5, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(6, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(7, 7, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(8, 8, 0));
        assertEquals(15, card.calculateScore(new ResourceHolder(), field));

        // Test 8: diagonal pattern, field with no overlap and two matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(4, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(5, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(6, 2, 0));
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 11: L pattern, empty field
        field = new GameField();
        pattern = getLPattern(0, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));

        // Test 12a: L pattern, field with the same pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(-1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 3, 0));
        pattern = getLPattern(0, Symbol.BLUE, Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 12b: L pattern, field with the same pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(2, -2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -3, 0));
        pattern = getLPattern(1, Symbol.BLUE, Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 12c: L pattern, field with the same pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(-1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(-2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(-1, 3, 0));
        pattern = getLPattern(2, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 12d: L pattern, field with the same pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 3, 0));
        pattern = getLPattern(3, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 13a: L pattern, field with a different pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 3, 0));
        pattern = getLPattern(0, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));

        // Test 13b: L pattern, field with a different pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(-1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 3, 0));
        pattern = getLPattern(0, Symbol.BLUE, Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));

        // Test 13c: L pattern, field with a different pattern
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 6, 0));
        pattern = getLPattern(0, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(0, card.calculateScore(new ResourceHolder(), field));

        // Test 14a: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 5, 0));
        pattern = getLPattern(0, Symbol.GREEN, Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 14b: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(-1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(-1, 5, 0));
        pattern = getLPattern(1, Symbol.PURPLE, Symbol.GREEN);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 14c: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, -2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -5, 0));
        pattern = getLPattern(2, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 14d: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 5, 0));
        pattern = getLPattern(3, Symbol.BLUE, Symbol.PURPLE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(5, card.calculateScore(new ResourceHolder(), field));

        // Test 15a: L pattern, field with pattern overlap but two matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 7, 0));
        pattern = getLPattern(0, Symbol.GREEN, Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 15b: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(-1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(-1, 7, 0));
        pattern = getLPattern(1, Symbol.PURPLE, Symbol.GREEN);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 15c: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(-1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(-1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 6, 0));
        pattern = getLPattern(2, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 15d: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(2, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 7, 0));
        pattern = getLPattern(3, Symbol.BLUE, Symbol.PURPLE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 16a: L pattern, field with pattern overlap but two matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 7, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 8, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 9, 0));
        pattern = getLPattern(0, Symbol.GREEN, Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 16b: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(-1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(-1, 7, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(0, 8, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 9, 0));
        pattern = getLPattern(1, Symbol.PURPLE, Symbol.GREEN);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 16c: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(-1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(-1, 7, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 8, 0));
        pattern = getLPattern(2, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 16d: L pattern, field with pattern overlap
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(2, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(2, 8, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 7, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.PURPLE), new GameFieldPosition(1, 9, 0));
        pattern = getLPattern(3, Symbol.BLUE, Symbol.PURPLE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));

        // Test 17: L pattern, field with pattern overlap but three matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 3, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 4, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 5, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 6, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 7, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 8, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 9, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(0, 10, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 11, 0));
        pattern = getLPattern(0, Symbol.GREEN, Symbol.RED);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(15, card.calculateScore(new ResourceHolder(), field));

        // Test 18: L pattern, field with no overlap and two matches
        field = new GameField();
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.GREEN), new GameFieldPosition(1, 1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(0, 2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(1, -1, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, 0, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.BLUE), new GameFieldPosition(2, -2, 0));
        field.placeOnFieldAt(getGenericSide(Symbol.RED), new GameFieldPosition(3, -3, 0));
        pattern = getLPattern(2, Symbol.RED, Symbol.BLUE);
        card = new PatternObjectiveCard(5, pattern);
        assertEquals(10, card.calculateScore(new ResourceHolder(), field));
    }

    @Test
    void ensureSerializability() {
        assertDoesNotThrow(() -> {
            ObjectiveCard card = new PatternObjectiveCard(5, getLeftDiagonalPattern(Symbol.RED));

            GameDataJsonParser<ObjectiveCard> parser = new GameDataJsonParser<>(ObjectiveCard.class);

            String json = parser.toJson(card);

            ObjectiveCard deserialized = parser.fromJson(json);
        });
    }

}