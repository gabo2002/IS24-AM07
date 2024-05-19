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

package it.polimi.ingsw.am07.utils.cli;

import java.io.IOException;
import java.util.Scanner;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class Input{

    public static int readInt(Scanner scanner) {
        while(!scanner.hasNextInt()) {
            System.err.println("Invalid input, please enter a valid integer number!");
            scanner.next();
        }

        try {
            return scanner.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }

    public static int readBinaryChoice(Scanner scanner) {
        boolean validValue = false;
        int returnValue = 0;

        while (!validValue) {
            returnValue = readInt(scanner);

            if(returnValue == 0 || returnValue == 1) {
                validValue = true;
            }
            else {
                System.err.println("Invalid input, please enter 0 or 1!");
            }
        }
        return returnValue;
    }

    public static int readInt(Scanner scanner, int min, int max) {
        int value = readInt(scanner);

        while(value < min || value > max) {
            System.err.println("Invalid input, please enter a number between " + min + " and " + max + "!");
            value = readInt(scanner);
        }

        return value;
    }
}
