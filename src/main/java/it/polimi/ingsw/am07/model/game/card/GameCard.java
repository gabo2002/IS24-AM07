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

package it.polimi.ingsw.am07.model.game.card;

import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFront;

import java.io.Serializable;

/**
 * Represents a game card, which has a front side and a back side.
 *
 * @param front the front side of the card
 * @param back  the back side of the card
 */
public record GameCard(
        SideFront front,
        SideBack back
) implements Serializable {

    /**
     * Constructs a new GameCard with the specified parameters.
     *
     * @param front the front side of the card
     * @param back  the back side of the card
     */
    public GameCard {
        if (front.id() != back.id()) {
            throw new IllegalArgumentException("The front and back sides of the card must have the same id");
        }
    }

    /**
     * Each card has a unique id, which is the same for the front and back sides.
     *
     * @return the id of the front card
     */
    public int id() {
        return front.id();
    }

    /**
     * Each card is uniquely identified by its id.
     * Thus, another object is equal to GameCard if it is a GameCard
     * and if it has the same id.
     *
     * @return true if the two cards have the same id, false otherwise
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameCard c)) {
            return false;
        }
        return c.id() == id();
    }

}
