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

package it.polimi.ingsw.am07.client.gui.utils;

import it.polimi.ingsw.am07.utils.logging.AppLogger;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class to load card images.
 * It loads images from the resources folder.
 */
public class CardImageLoader {

    private static final AppLogger LOGGER = new AppLogger(CardImageLoader.class);

    /**
     * Loads an image from the resources folder.
     *
     * @param cl   the class loader
     * @param id   the id of the card
     * @param side the side of the card
     * @return the image
     */
    public static Image imgFrom(ClassLoader cl, int id, String side) {
        String item = "it/polimi/ingsw/am07/assets/" + side + "_" + id + ".png";
        Image img = null;
        try (InputStream is = cl.getResourceAsStream(item)) {
            if (is != null) {
                img = new Image(is);
            } else {
                System.err.println("Unable to load image: " + item);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return img;
    }

}
