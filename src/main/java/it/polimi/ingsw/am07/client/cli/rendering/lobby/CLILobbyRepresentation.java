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

package it.polimi.ingsw.am07.client.cli.rendering.lobby;

import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIElement;
import it.polimi.ingsw.am07.client.cli.rendering.common.CLIPawnColor;
import it.polimi.ingsw.am07.model.lobby.Lobby;

public class CLILobbyRepresentation implements CLIElement {

    private static final char LEFT_ANGLE = '/';
    private static final char RIGHT_ANGLE = '\\';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';
    private final Lobby lobby;

    public CLILobbyRepresentation(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public String render() {
        StringBuilder bufferedRender = new StringBuilder();

        bufferedRender.append("\n");
        //get the longest name
        int width = 4 + lobby.getPlayers().stream().mapToInt(player -> player.getNickname().length()).max().orElse(0);

        //draw the borders
        bufferedRender.append(LEFT_ANGLE);
        for (int x = 1; x < width - 1; x++) {
            bufferedRender.append(HORIZONTAL);
        }
        bufferedRender.append(RIGHT_ANGLE + "\n");

        // First row is empty
        bufferedRender.append(VERTICAL);
        for (int x = 0; x < width - 2; x++) {
            bufferedRender.append(' ');
        }
        bufferedRender.append(VERTICAL);
        bufferedRender.append('\n');

        // Draw the players
        for (int y = 0; y < lobby.getPlayerCount(); y++) {
            bufferedRender.append(VERTICAL);
            bufferedRender.append(" ");
            //Setting the pawn color
            if (lobby.getPlayers().get(y).getPlayerPawn() != null) {
                bufferedRender.append(CLIPawnColor.pawnToColor(lobby.getPlayers().get(y).getPlayerPawn()));
            }
            bufferedRender.append(lobby.getPlayers().get(y).getNickname());
            //reset the color
            bufferedRender.append(CLIColor.RESET.getCode());
            for (int x = 0; x < width - 3 - lobby.getPlayers().get(y).getNickname().length(); x++) {
                bufferedRender.append(' ');
            }
            bufferedRender.append(VERTICAL);
            bufferedRender.append('\n');
        }

        // Last row is empty
        bufferedRender.append(VERTICAL);
        for (int x = 0; x < width - 2; x++) {
            bufferedRender.append(' ');
        }
        bufferedRender.append(VERTICAL);
        bufferedRender.append('\n');

        bufferedRender.append(RIGHT_ANGLE);
        for (int x = 1; x < width - 1; x++) {
            bufferedRender.append(HORIZONTAL);
        }
        bufferedRender.append(LEFT_ANGLE + "\n");

        return bufferedRender.toString();
    }
}
