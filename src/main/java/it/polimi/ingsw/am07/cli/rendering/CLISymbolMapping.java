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

package it.polimi.ingsw.am07.cli.rendering;

import it.polimi.ingsw.am07.model.game.Symbol;

/**
 * Maps a Symbol to a character
 */
public class CLISymbolMapping {

    /**
     * Constants for the different types of cards
     */
    public static char CARD_GOLD = 'G';
    public static char CARD_RES = 'R';
    public static char CARD_STARTER = 'S';

    /**
     * Maps a Symbol to a character
     *
     * @param symbol the symbol to map
     * @return the character mapped to the symbol
     */
    public static char toChar(Symbol symbol) {
        return switch (symbol) {
            case RED -> 'R';
            case GREEN -> 'G';
            case BLUE -> 'B';
            case PURPLE -> 'P';
            case SCROLL -> 'S';
            case FLASK -> 'I';
            case FEATHER -> 'F';
            case BLANK -> '█';
            case NONE -> 'X';
            default -> ' ';
        };
    }

}
