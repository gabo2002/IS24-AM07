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
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPattern;

/**
 * Represents an objective card that requires a certain pattern to be completed.
 */
public final class PatternObjectiveCard extends ObjectiveCard {

    /**
     * The pattern required to satisfy the objective.
     */
    private final GameFieldPattern pattern;

    /**
     * Constructs a new PatternObjectiveCard with the specified parameters.
     *
     * @param associatedScore the score associated with the card
     * @param pattern         the pattern required to complete the card
     */
    public PatternObjectiveCard(int associatedScore, int id, GameFieldPattern pattern) {
        super(associatedScore, id);
        this.pattern = pattern;
    }

    /**
     * Calculates the score based on the matching patterns on the player's game field.
     *
     * @param playerResources The resources held by the player.
     * @param playerGameField The game field of the player.
     * @return The calculated score based on the matching patterns and associated score.
     */
    @Override
    public int calculateScore(ResourceHolder playerResources, GameField playerGameField) {
        int matchingPatterns = playerGameField.countMatches(pattern);

        return matchingPatterns * associatedScore;
    }

    /**
     * Returns the pattern required to satisfy the objective.
     *
     * @return The pattern required to satisfy the objective.
     */
    public GameFieldPattern getPattern() {
        return pattern;
    }

}
