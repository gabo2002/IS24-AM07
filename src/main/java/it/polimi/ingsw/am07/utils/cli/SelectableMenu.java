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

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * A class that represents a selectable menu. Used in the CLI to show a list of options and let the user select one.
 * The user can navigate through the options using the arrow keys and select an option pressing enter.
 * This class is blocking, so the program will wait until the user selects an option.
 *
 * @author Gabriele Corti
 */
public class SelectableMenu {
    private final List<String> options;

    private int selectedOption;
    private Terminal terminal;

    private Scanner scanner;

    private NonBlockingReader reader;

    /**
     * Create a new SelectableMenu with the given options.
     * This class can be recycled to show multiple menus.
     *
     * @param options the options of the menu.
     * @author Gabriele Corti
     */
    public SelectableMenu(List<String> options,Scanner scanner) {
        this.options = options;
        this.scanner = scanner;
        selectedOption = 0;
    }

    /**
     * Initialize the terminal and the reader.
     * This method is called when the show method is called.
     *
     * @author Gabriele Corti
     */
    private void init() {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
            terminal.enterRawMode();
            reader = terminal.reader();
        } catch (IOException e) {
            terminal = null;
            reader = null;
            System.err.println("Impossible to create the terminal.");
        }
    }

    /**
     * Get the index of the selected option.
     *
     * @return the index of the selected option.
     * @author Gabriele Corti
     */
    public int getSelectedOptionIndex() {
        return selectedOption;
    }

    /**
     * Get the selected option.
     *
     * @return the selected option.
     * @author Gabriele Corti
     */
    public String getSelectedOption() {
        return options.get(selectedOption);
    }


    public void show() {
        if ( System.getProperty("org.jline.terminal.dumb") != null) {   //if the terminal is not interactive
            showNonInteractiveMenu();
        } else {
            showInteractiveMenu();
        }
    }

    /**
     * Show the menu on the terminal. The user can select an option by pressing the corresponding number.
     * This method will block until the user selects an option. The selected option can be retrieved using the getSelectedOption method.
     * This method is non-interactive, so the user can't navigate through the options using the arrow keys.
     * IMPORTANT: This method will be executed only if the terminal is not interactive, like in an IDE.
     * @author Gabriele Corti
     */
    private void showNonInteractiveMenu() {
        int key = 0;

        if (options.isEmpty()) {
            return;
        }

        while(true) {
            System.out.println("Select an option:");
            for (int i = 0; i < options.size(); i++) {
                System.out.println(i + ". " + options.get(i));
            }

            key = Input.readInt(scanner);

            if (key >= 0 && key < options.size()) {
                selectedOption = key;
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Show the menu on the terminal. The user can navigate through the options using the arrow keys and select an option pressing enter.
     * This method will block until the user selects an option. The selected option can be retrieved using the getSelectedOption method.
     *
     * @author Gabriele Corti
     */
    private void showInteractiveMenu() {
        int key = 0;
        init();

        if (options.isEmpty() || reader == null) {
            return;
        }

        while (true) {
            clearScreen();
            printMenu();

            try {
                key = reader.read();
            } catch (IOException e) {
                System.out.println("Error reading the key");
                break;
            }

            //arrow down
            if (key == 66) {
                selectedOption = (selectedOption + 1) % options.size();
            }
            //arrow up
            if (key == 65) {
                selectedOption = (selectedOption - 1 + options.size()) % options.size();
            }
            //enter
            if (key == 10 || key == 13) {
                break;
            }
        }
        //close the terminal
        try {
            reader.close();
            terminal.close();
        } catch (IOException e) {
            System.err.println("Impossible to close the terminal.");
        }
    }

    /**
     * Print the menu on the terminal. According to the selected option, the arrow will be printed before the option.
     *
     * @author Gabriele Corti
     */
    private void printMenu() {
        for (int i = 0; i < options.size(); i++) {
            if (i == selectedOption)
                System.out.println("-> " + options.get(i));
            else
                System.out.println("   " + options.get(i));
        }
    }

    /**
     * Clear the screen.
     *
     * @author Gabriele Corti
     */
    private void clearScreen() {
        terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
        terminal.flush();
    }
}
