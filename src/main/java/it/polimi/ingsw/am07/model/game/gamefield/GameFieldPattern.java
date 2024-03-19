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

import java.util.List;

/**
 * Represents a pattern of symbols that can be placed on the game field.
 * This object is used by ObjectiveCards to check how many patterns are present in the Gamefield.
 * @param pattern the pattern of symbols
 * @author Gabriele Corti
 */
public record GameFieldPattern(
    Matrix<Symbol> pattern
) {

    /**
     * Create a new GameFieldPattern with the given pattern.
     * @param pattern the pattern of symbols
     * @throws IllegalArgumentException if the pattern contains invalid symbols - only NONE, RED, GREEN, BLUE and PURPLE are allowed
     * @author Gabriele Corti
     */
    public GameFieldPattern(Matrix<Symbol> pattern) {
        final List<Symbol> allowedSymbols = List.of(Symbol.EMPTY, Symbol.RED,Symbol.GREEN,Symbol.BLUE,Symbol.PURPLE);

        for(Symbol s : pattern) {
            if (!allowedSymbols.contains(s)) {
                throw new IllegalArgumentException("Invalid symbol in pattern");
            }
        }
        this.pattern = pattern;
    }
}
