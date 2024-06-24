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

package it.polimi.ingsw.am07.utils;

import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * A utility class to manage the identity of the client.
 */
public class IdentityManager {

    private static final AppLogger logger = new AppLogger(IdentityManager.class);

    /**
     * Get the constant identity of the client from the file "identity.txt".
     * If the identity file does not exist, a new identity is generated and saved.
     *
     * @return the identity
     */
    public static String getConstantIdentity() {
        Path identityPath = Paths.get("identity.txt");

        if (identityPath.toFile().exists()) {
            try {
                return Files.readString(identityPath);
            } catch (Exception e) {
                logger.error(e);
            }
        }

        UUID uuid = UUID.randomUUID();
        String identity = uuid.toString();

        try {
            Files.writeString(identityPath, identity);
        } catch (Exception e) {
            logger.error(e);
        }

        return identity;
    }

    /**
     * Clear the identity of the client by deleting the file "identity.txt".
     */
    public static void clearIdentity() {
        Path identityPath = Paths.get("identity.txt");

        try {
            Files.deleteIfExists(identityPath);
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
