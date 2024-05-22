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

import it.polimi.ingsw.am07.chat.PlayerChat;
import it.polimi.ingsw.am07.exceptions.IllegalGamePositionException;
import it.polimi.ingsw.am07.exceptions.IllegalPlacementException;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a player in the game.
 * It provides methods to place a card on the game field and to check if a card can be placed at a specific position on the game field.
 * It also provides methods to retrieve the list of playable cards and the objective card of the player.
 * Additionally, it stores the nickname, the score, the pawn, the resource holder, and the game field associated with the player.
 */
public class Player implements Serializable {

    private final String nickname;

    private final ResourceHolder playerResources;
    private final Pawn playerPawn;
    private final GameField playerGameField;
    private final List<GameCard> playableCards;    //handCards?
    private final GameCard starterCard;
    private final ObjectiveCard[] availableObjectives;
    private int playerScore;
    private int playerObjectiveScore;
    private ObjectiveCard playerObjectiveCard;

    private PlayerChat chat;

    /**
     * Constructs a new Player with the specified parameters.
     */
    public Player(String nickname, Pawn playerPawn, GameCard starterCard, ObjectiveCard[] availableObjectives) {
        this.nickname = nickname;
        this.playerPawn = playerPawn;
        this.availableObjectives = availableObjectives;
        this.starterCard = starterCard;

        this.playerResources = new ResourceHolder();
        this.playerGameField = new GameField();
        this.playableCards = new ArrayList<>();
        this.playerObjectiveCard = null;

        this.chat = new PlayerChat(new ArrayList<>(), nickname);
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
     * Sets the objective card for the player, if it has not been set yet.
     *
     * @param playerObjectiveCard The objective card to be set for the player.
     */
    public void setPlayerObjectiveCard(ObjectiveCard playerObjectiveCard) {
        if (this.playerObjectiveCard == null) {
            this.playerObjectiveCard = playerObjectiveCard;
        }
    }

    /**
     * Retrieves the available objective cards for the player.
     *
     * @return The available objective cards for the player.
     */
    public ObjectiveCard[] getAvailableObjectives() {
        return availableObjectives;
    }

    /**
     * Retrieves the starter card of the player.
     *
     * @return The starter card of the player.
     */
    public GameCard getStarterCard() {
        return starterCard;
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
        return pos.isValid()
                && playerGameField.canBePlacedOnFieldAt(card, pos)
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
    public void placeAt(Side card, GameFieldPosition pos) throws IllegalPlacementException, IllegalGamePositionException {
        if (!pos.isValid()) {
            throw new IllegalGamePositionException("The provided position is not valid");
        }

        if (!canBePlacedAt(card, pos)) {
            throw new IllegalPlacementException("The provided position is not valid");
        }

        final ResourceHolder diff = playerGameField.placeOnFieldAt(card, pos);

        playerResources.add(diff);

        final int coveredCorners = playerGameField.countCoveredCorners(pos);

        // the associated score of a gold card includes the resources added by the card itself
        playerScore += card.calculateAssociatedScore(playerResources, coveredCorners);
    }

    /**
     * This class represents a game field where cards can be placed and manipulated.
     * It provides methods to check if a card can be placed at a specific position on the game field.
     *
     * @author Omar Chaabani
     */
    public Map<GameFieldPosition, Side> getPlacedCards() {
        return playerGameField.getPlacedCards();
    }

    /**
     * Retrieves the list of playable cards.
     *
     * @return An ArrayList containing the playable cards.
     * @author Omar Chaabani
     */
    public List<GameCard> getPlayableCards() {
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

    /**
     * Returns the chat associated with the player.
     *
     * @return The chat associated with the player.
     */
    public PlayerChat getChat() {
        return chat;
    }

    /**
     * Evalutes the score the player has achieved for the provided objective card.
     *
     * @param objective The objective card for which the score is to be evaluated.
     */
    public void evaluateObjectiveScore(ObjectiveCard objective) {
        this.playerObjectiveScore += objective.calculateScore(new ResourceHolder(playerResources), playerGameField);
    }

    public int getPlayerObjectiveScore() {
        return playerObjectiveScore;
    }

    public GameField getPlayerGameField() {
        return playerGameField;
    }
}
