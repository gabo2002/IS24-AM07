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

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Custom formatter for the logger.
 * It formats the log message with the calling class name.
 */
public class CustomFormatter extends SimpleFormatter {

    @Override
    public String format(LogRecord record) {
        // Get the calling class name
        String className = record.getLoggerName();

        // Format the log message
        String sb = "[" +
                className +
                "] " +
                record.getMessage() +
                "\n";

        return sb;
    }

}
