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

package it.polimi.ingsw.am07.client.cli.rendering;

import it.polimi.ingsw.am07.model.game.Symbol;

/**
 * Enum that contains the ANSI escape codes for the colors used in the CLI.
 */
public enum CLIColor {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m");

    private final String code;

    /**
     * Constructor for the CLIColor enum.
     *
     * @param code the ANSI escape code
     */
    CLIColor(String code) {
        this.code = code;
    }

    /**
     * Returns the CLI color for the symbol.
     *
     * @param symbol any Symbol
     * @return the color for the symbol
     */
    public static CLIColor fromSymbol(Symbol symbol) {
        return switch (symbol) {
            case Symbol.RED -> RED;
            case Symbol.GREEN -> GREEN;
            case Symbol.BLUE -> BLUE;
            case Symbol.PURPLE -> PURPLE;
            case Symbol.STARTER, Symbol.FEATHER, Symbol.SCROLL, Symbol.FLASK, Symbol.CORNER -> YELLOW;
            default -> throw new RuntimeException("Invalid symbol");
        };
    }

    /**
     * Getter for the ANSI escape code.
     *
     * @return the ANSI escape code
     */
    public String getCode() {
        return code;
    }

}
