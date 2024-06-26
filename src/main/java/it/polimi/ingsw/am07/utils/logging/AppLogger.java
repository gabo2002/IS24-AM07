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

package it.polimi.ingsw.am07.utils.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for logging.
 * This class is used to log messages to the console or to a file.
 * If the system property "cli" is set, the log messages will be written to a file.
 * IMPORTANT: Only one instance of this class is allowed per class.
 */
public class AppLogger {

    private final Logger LOGGER;

    /**
     * Constructs a new AppLogger for the specified class.
     *
     * @param clazz the class for which the logger is created
     */
    public AppLogger(Class<?> clazz) {
        LOGGER = Logger.getLogger(clazz.getName());

        synchronized (LOGGER) {
            if (LOGGER.getHandlers().length > 0) {
                return;
            }

            if (System.getProperty("cli") != null) {
                try {
                    FileHandler fileHandler = new FileHandler("log.txt", 0, 1, true);
                    fileHandler.setLevel(Level.ALL);
                    fileHandler.setFormatter(new CustomFormatter());
                    LOGGER.addHandler(fileHandler);
                } catch (IOException e) {
                    System.err.println("Error while creating log file! Logging will be disabled.");
                }
            } else {
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.ALL);
                consoleHandler.setFormatter(new CustomFormatter());
                LOGGER.addHandler(consoleHandler);
            }

            LOGGER.setLevel(Level.ALL);
            LOGGER.setUseParentHandlers(false);
        }
    }

    /**
     * Logs a debug message.
     * @param message the message to log
     */
    public void debug(String message) {
        LOGGER.log(Level.FINE, message);
    }

    /**
     * Logs an info message.
     * @param message the message to log
     */
    public void info(String message) {
        LOGGER.log(Level.INFO, message);
    }

    /**
     * Logs a warning message.
     * @param message the message to log
     */
    public void error(String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    /**
     * Logs an error message.
     * @param e the exception to log
     */
    public void error(Exception e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }

}
