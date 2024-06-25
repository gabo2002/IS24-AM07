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

package it.polimi.ingsw.am07.client.cli.input;

import java.util.List;

/**
 * A class that represents a selectable menu. Used in the CLI to show a list of options and let the user select one.
 * The user can navigate through the options using the arrow keys and select an option pressing enter.
 * This class is blocking, so the program will wait until the user selects an option.
 *
 * @author Gabriele Corti
 */
public class SelectableMenu<T> {
    private final List<T> options;
    private final ThreadInputReader scanner;
    private int selectedOption;

    /**
     * Create a new SelectableMenu with the given options.
     * This class can be recycled to show multiple menus.
     *
     * @param options the options of the menu.
     * @author Gabriele Corti
     */
    public SelectableMenu(List<T> options, ThreadInputReader scanner) {
        this.options = options;
        this.scanner = scanner;
        selectedOption = 0;
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
    public String getSelectedStringOption() {
        if (options.isEmpty())
            return "";
        return options.get(selectedOption).toString();
    }

    public T getSelectedOption() {
        if (options.isEmpty())
            return null;
        return options.get(selectedOption);
    }

    /**
     * Show the menu on the terminal. The user can select an option by pressing the corresponding number.
     * This method will block until the user selects an option. The selected option can be retrieved using the getSelectedOption method.
     * This method is non-interactive, so the user can't navigate through the options using the arrow keys
     *
     * @author Gabriele Corti
     */
    public void show() throws InterruptedException {

        if (options.isEmpty()) {
            return;
        }

        System.out.println("Select an option:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println(i + ". " + options.get(i));
        }

        selectedOption = scanner.getInt(0, options.size() - 1);
    }
}
