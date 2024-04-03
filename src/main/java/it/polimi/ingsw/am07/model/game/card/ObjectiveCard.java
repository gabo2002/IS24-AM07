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

import it.polimi.ingsw.am07.model.game.ResourceHolder;
import it.polimi.ingsw.am07.model.game.gamefield.GameField;

/**
 * Represents an objective card that requires a certain amount of resources to be completed.
 */
public abstract class ObjectiveCard {

    protected final int associatedScore;

    protected ObjectiveCard(int associatedScore) {
        this.associatedScore = associatedScore;
    }

    /**
     * Calculates the score associated with the card, based on the player's resources and game field.
     *
     * @param playerResources the resources of the player
     * @param playerGameField the game field of the player
     * @return the score associated with the card
     */
    public abstract int calculateScore(ResourceHolder playerResources, GameField playerGameField);

}
