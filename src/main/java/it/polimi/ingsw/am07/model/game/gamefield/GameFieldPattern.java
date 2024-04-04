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

import java.util.List;

/**
 * Represents a pattern of symbols that can be placed on the game field.
 * This object is used by ObjectiveCards to check how many patterns are present in the Gamefield.
 *
 * @param pattern the pattern of symbols
 * @author Gabriele Corti
 */
public record GameFieldPattern(
        Matrix<Symbol> pattern
) {

    /**
     * Create a new GameFieldPattern with the given pattern.
     *
     * @param pattern The pattern of symbols, which must consist only of center positions.
     *                IMPORTANT: Only CENTER POSITIONS ARE ALLOWED. Every symbol that is not EMPTY must be in a center position and correspond to a card.
     * @throws IllegalArgumentException if the pattern contains invalid symbols. Only NONE, RED, GREEN, BLUE, and PURPLE are allowed.
     * @author Gabriele Corti
     */
    public GameFieldPattern {
        final List<Symbol> allowedSymbols = List.of(Symbol.EMPTY, Symbol.RED, Symbol.GREEN, Symbol.BLUE, Symbol.PURPLE);

        for (Symbol s : pattern) {
            if (!allowedSymbols.contains(s)) {
                throw new IllegalArgumentException("Invalid symbol in pattern");
            }
        }
    }

    /**
     * Get the shape of the pattern, expanding the center positions to cover the entire card.
     * This method is used to obtain a matrix that represents the shape of the pattern, with the center positions expanded to a 2x2 matrix (corresponding to each corner) to be used for comparison with the game field.
     *
     * @return a matrix corresponding to the shape of the pattern, with the center positions expanded to cover the entire card
     * @author Gabriele Corti
     */
    public Matrix<Symbol> getShape() {
        Matrix<Symbol> matrix = pattern.copy();
        MatrixElementIterator<Symbol> iterator = (MatrixElementIterator<Symbol>) pattern.iterator();

        while (iterator.hasNext()) {
            Symbol s = iterator.next();

            if (!Symbol.EMPTY.equals(s)) {
                fillShape(matrix, s, iterator.getCurrentX(), iterator.getCurrentY());
            }
        }

        return matrix;
    }

    /**
     * Fills the shape of the pattern with the given symbol, expanding the center positions, corresponding to (x,y) coordinates to the whole card.
     *
     * @param shape the shape matrix to fill
     * @param s     the symbol to fill the shape with
     * @param x     the x coordinate of the top left corner of the card
     * @param y     the y coordinate of the top left corner of the card
     * @author Gabriele Corti
     */
    private void fillShape(Matrix<Symbol> shape, Symbol s, int x, int y) {
        shape.set(x, y, s);
        shape.set(x, y + 1, s);
        shape.set(x + 1, y, s);
        shape.set(x + 1, y + 1, s);
    }

}
