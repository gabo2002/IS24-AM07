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

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The {@code LobbyBoxController} class is responsible for controlling the lobby box view in the GUI.
 * It manages the display of the lobby name and the number of players in the lobby.
 */
public class LobbyBoxController {

    /**
     * The label displaying the lobby name.
     */
    @FXML
    private Label lobby_name_box;

    /**
     * The label displaying the number of players in the lobby.
     */
    @FXML
    private Label n_players;

    /**
     * Sets the text of the {@code lobby_name_box} label to the specified name.
     *
     * @param name the name to set for the lobby.
     */
    @FXML
    protected void setLobby_name_box(String name) {
        lobby_name_box.setText(name);
    }

    /**
     * Sets the text of the {@code n_players} label to the specified text.
     *
     * @param text the text to set for the number of players.
     */
    @FXML
    protected void setN_players(String text) {
        n_players.setText(text);
    }
}
