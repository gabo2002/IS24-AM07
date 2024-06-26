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

package it.polimi.ingsw.am07.client.cli.rendering.common;

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.model.game.Pawn;
/**
 * The CLIPawnColor class provides utility methods for converting Pawn objects
 * to their corresponding color codes for CLI (Command Line Interface) rendering.
 */
public class CLIPawnColor {


    /**
     * Converts a Pawn object to its corresponding CLI color code.
     * This method maps each Pawn enum value to a specific color code defined
     * in the CLIColor enum. If the pawn color is not recognized, it defaults to white.
     *
     * @param pawn The Pawn object to be converted to a color code.
     * @return A String representing the ANSI color code corresponding to the pawn's color.
     *         Returns white color code if the pawn is null or not recognized.
     */
    public static String pawnToColor(Pawn pawn) {
        return switch (pawn) {
            case BLACK -> CLIColor.BLACK.getCode();
            case RED -> CLIColor.RED.getCode();
            case YELLOW -> CLIColor.YELLOW.getCode();
            case BLUE -> CLIColor.BLUE.getCode();
            case GREEN -> CLIColor.GREEN.getCode();
            default -> CLIColor.WHITE.getCode();
        };
    }
}
