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

package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.exceptions.IllegalPlacementException;
import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a player in the game.
 * It provides methods to place a card on the game field and to check if a card can be placed at a specific position on the game field.
 * It also provides methods to retrieve the list of playable cards and the objective card of the player.
 * Additionally, it stores the nickname, the score, the pawn, the resource holder, and the game field associated with the player.
 */
public class Player {

    private final String nickname;

    private final ResourceHolder playerResources;

    private int playerScore;

    private final Pawn playerPawn;

    private final GameField playerGameField;

    private final ArrayList<GameCard> playableCards;    //handCards?

    private ObjectiveCard playerObjectiveCard;

    /**
     * Constructs a new Player with the specified parameters.
     */
    public Player(String nickname, ResourceHolder playerResources, Pawn playerPawn, GameField playerGameField, ArrayList<GameCard> playableCards){
        this.nickname = nickname;
        this.playerResources = playerResources;
        this.playerPawn = playerPawn;
        this.playerGameField = playerGameField;
        this.playableCards = playableCards;
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
     * Retrieves the score of the player.
     *
     * @return The score of the player.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Retrieves the pawn associated with the player.
     *
     * @return The pawn associated with the player.
     */
    public Pawn getPlayerPawn() {
        return playerPawn;
    }

    /**
     * Retrieves the objective card of the player.
     *
     * @return The objective card of the player.
     */
    public ObjectiveCard getPlayerObjectiveCard() {
        return playerObjectiveCard;
    }

    /**
     * Sets the objective card for the player.
     *
     * @param playerObjectiveCard The objective card to be set for the player.
     */
    public void setPlayerObjectiveCard(ObjectiveCard playerObjectiveCard) {
        this.playerObjectiveCard = playerObjectiveCard;
    }

    /**
     * Retrieves the resource holder associated with the player.
     *
     * @return The resource holder associated with the player.
     */
    public ResourceHolder getPlayerResources() {
        return playerResources;
    }

    /**
     * Checks if a card can be placed at the specified position on the game field.
     *
     * @param card The card to be placed.
     * @param pos  The position on the game field where the card is to be placed.
     * @return true if the card can be placed at the specified position, false otherwise.
     * @author Omar Chaabani
     */
    public boolean canBePlacedAt(Side card, GameFieldPosition pos) {
        return playerGameField.canBePlacedOnFieldAt(card, pos)
                && card.requirements()
                        .map(playerResources::contains)
                        .orElse(true);
    }

    /**
     * Places a card on the game field at the specified position.
     *
     * @param card The card to be placed.
     * @param pos  The position on the game field where the card is to be placed.
     * @throws IllegalPlacementException if the position is not valid
     */
    public void placeAt(Side card, GameFieldPosition pos) throws IllegalPlacementException {
        if (!canBePlacedAt(card, pos))
            throw new IllegalPlacementException("The provided position is not valid");

        final ResourceHolder diff = playerGameField.placeOnFieldAt(card, pos);

        final int coveredCorners = playerGameField.countCoveredCorners(pos);

        // Cards whose score multiplier is based on the player's resources
        // should be calculated before the resource difference is applied
        playerScore += card.calculateAssociatedScore(playerResources, coveredCorners);

        playerResources.subtract(diff);
    }

    /**
     * This class represents a game field where cards can be placed and manipulated.
     * It provides methods to check if a card can be placed at a specific position on the game field.
     *
     * @author Omar Chaabani
     */
    public HashMap<Side, GameFieldPosition> getPlacedCards() {
        return playerGameField.getPlacedCards();
    }

    /**
     * Retrieves the list of playable cards.
     *
     * @return An ArrayList containing the playable cards.
     * @author Omar Chaabani
     */
    public ArrayList<GameCard> getPlayableCards() {
        return playableCards;
    }

    /**
     * Adds a playable card to the list of playable cards.
     *
     * @param card The playable card to be added.
     * @author Omar Chaabani
     */
    public void addPlayableCard(GameCard card) {
        playableCards.add(card);
    }

}
