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
 * Represents a symbol in the CLI-rendered game field.
 *
 * @param symbol the symbol to render, as a char
 * @param color  the color of the symbol, as a CLIColor
 */
public record CLIGameSymbol(
        char symbol,
        CLIColor color
) implements CLIElement {

    /**
     * Constants for the different types of cards
     */
    public static final char CARD_GOLD = 'G';
    public static final char CARD_RES = 'R';
    public static final char CARD_STARTER = 'S';

    /**
     * Basic constructor for the CLIGameFieldSymbol, with the default color.
     *
     * @param symbol the symbol to render, as a char
     */
    public CLIGameSymbol(char symbol) {
        this(symbol, CLIColor.RESET);
    }

    /**
     * Constructor for the CLIGameFieldSymbol, taking a Symbol instead of a char.
     *
     * @param symbol the symbol to render, as a Symbol
     * @param color  the color of the symbol, as a CLIColor
     */
    public CLIGameSymbol(Symbol symbol, CLIColor color) {
        this(gameSymbolToChar(symbol), color);
    }

    /**
     * Maps a Symbol to a character
     *
     * @param symbol the symbol to map
     * @return the character mapped to the symbol
     */
    public static char gameSymbolToChar(Symbol symbol) {
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
            case CORNER -> 'C';
            default -> ' ';
        };
    }

    /**
     * Renders the symbol in the CLI, not resetting the color if it's the same as the current one.
     *
     * @param currentColor the current cursor color
     * @return the rendered symbol
     */
    public String render(CLIColor currentColor) {
        if (color.equals(currentColor)) {
            return String.valueOf(symbol);
        } else {
            return color.getCode() + symbol;
        }
    }

    /**
     * Renders the symbol in the CLI, resetting the color afterward.
     * #
     *
     * @return the rendered symbol
     */
    @Override
    public String render() {
        return color.getCode() + symbol + CLIColor.RESET.getCode();
    }

}
