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

package it.polimi.ingsw.am07.model.lobby;

import it.polimi.ingsw.am07.model.game.Pawn;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a player in the lobby, which only has a nickname and a pawn.
 */
public class LobbyPlayer implements Serializable {

    private final String nickname;
    private final String identifier;
    private Pawn playerPawn;

    /**
     * Constructs a new LobbyPlayer object with the given nickname.
     *
     * @param nickname
     */
    public LobbyPlayer(String nickname) {
        this.nickname = nickname;
        this.playerPawn = null;
        this.identifier = null;
    }

    public LobbyPlayer(String nickname, String identifier, Pawn playerPawn) {
        this.nickname = nickname;
        this.playerPawn = playerPawn;
        this.identifier = identifier;
    }

    /**
     * Retrieves the nickname of the player.
     *
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Retrieves the pawn of the player.
     *
     * @return The pawn of the player.
     */
    public Pawn getPlayerPawn() {
        return playerPawn;
    }

    /**
     * Sets the pawn of the player.
     *
     * @param playerPawn The pawn of the player.
     */
    public void setPlayerPawn(Pawn playerPawn) {
        this.playerPawn = playerPawn;
    }

    /**
     * Retrieves the identifier of the player.
     *
     * @return The identifier of the player.
     */
    public String getIdentity() {
        return identifier;
    }

    /**
     * Verifies if two instances of LobbyPlayer are equal.
     *
     * @param o The object to compare.
     * @return true if the two instances are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyPlayer that = (LobbyPlayer) o;
        return Objects.equals(nickname, that.nickname);
    }

    /**
     * Generates the hash code of the LobbyPlayer instance, which is based on the nickname.
     *
     * @return The hash code of the LobbyPlayer instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

}
