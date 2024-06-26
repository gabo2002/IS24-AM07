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

package it.polimi.ingsw.am07.client.gui.viewController;

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

/**
 * The {@code DisconnectedViewController} class manages the state of the application
 * when the client is disconnected. It attempts to reconnect after a delay.
 * <p>
 * It utilizes a separate thread to wait for a predefined amount of time before
 * notifying the {@link ClientState} to attempt reconnection.
 */
public class DisconnectedViewController {

    /**
     * Logger instance for this class.
     */
    private final AppLogger LOGGER = new AppLogger(DisconnectedViewController.class);

    /**
     * Initializes the view controller by starting a thread that attempts to reconnect
     * after a 5-second delay.
     *
     * @param clientState the current state of the client which will be notified for reconnection.
     */
    public void init(ClientState clientState) {
        new Thread(() -> {
            try {
                // Wait for 5 seconds before attempting to reconnect
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Log the error if the thread is interrupted during sleep
                LOGGER.error(e);
            }

            // Notify the client state to attempt a reconnection
            clientState.notifyGameModelUpdate();
        }).start();
    }
}
