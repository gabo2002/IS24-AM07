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

import it.polimi.ingsw.am07.exceptions.CardNotFoundException;
import it.polimi.ingsw.am07.exceptions.IllegalGameStateException;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game implements Serializable {

    private final UUID id;

    private final List<Player> players;

    private final Deck deck;

    private final String selfNickname;

    private final List<ObjectiveCard> availableObjectiveCards;

    private final ObjectiveCard[] commonObjectives; //only 2 cards

    private int currentPlayerIndex;    //Reminder: randomly generated first

    private GameState gameState;

    /**
     * Default constructor for the Game class.
     * It initializes the game with a random UUID, an empty list of players, a new deck, and the nickname of the current player.
     *
     *
     * @param selfNickname the nickname of the current player
     */
    public Game(String selfNickname) {
        id = UUID.randomUUID();
        players = new ArrayList<>();
        deck = new Deck.Factory().build();
        availableObjectiveCards = new ArrayList<>();
        commonObjectives = new ObjectiveCard[2];
        currentPlayerIndex = 0;
        gameState = GameState.STARTING;

        this.selfNickname = selfNickname;
    }

    /**
     * Constructor for the Game class, called by Lobby Class.
     * <strong>SERVER SIDE ONLY!</strong>
     * @param players the list of players in the game
     */
    public Game(List<Player> players) {
        id = UUID.randomUUID();
        this.players = players;
        deck = new Deck.Factory().build();
        availableObjectiveCards = new ArrayList<>();
        commonObjectives = new ObjectiveCard[2];
        currentPlayerIndex = 0;
        gameState = GameState.STARTING;
        this.selfNickname = null;
    }

    /**
     * This method returns the unique identifier of the game.
     *
     * @return the game's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * This method returns the list of players in the game.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * This method returns the nickname of the current player.
     *
     * @return the nickname of the current player
     */
    public String getSelfNickname() {
        return selfNickname;
    }

    /**
     * This method returns the common objectives in the game.
     *
     * @return an array of common objective cards
     */
    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * This method increments the current player index, effectively moving the turn to the next player.
     * It uses modulo operation to ensure that the player index wraps around to the first player after the last one.
     */
    public void incrementTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * This method returns the index of the current player in the game.
     *
     * @return the index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * This method picks a random resource card from the deck.
     *
     * @return a random resource card from the deck
     */
    public GameCard pickRandomResCard() {
        return deck.pickRandomResCard();
    }

    /**
     * This method picks a random gold card from the deck.
     *
     * @return a random gold card from the deck
     */

    public GameCard pickRandomGoldCard() {
        return deck.pickRandomGoldCard();
    }

    /**
     * This method removes a specified card from the deck.
     *
     * @param card the card to be removed from the deck
     * @throws CardNotFoundException if the specified card is not found in the deck
     */
    public void popCard(GameCard card) throws CardNotFoundException {
        deck.popCard(card);
    }

    /**
     * This method returns the size of the available resource cards in the deck.
     *
     * @return the size of the available resource cards
     */
    public int getAvailableResCardsSize() {
        return deck.availableResCards().size();
    }

    /**
     * This method returns the size of the available gold cards in the deck.
     *
     * @return the size of the available gold cards
     */
    public int getAvailableGoldCardsSize() {
        return deck.availableGoldCards().size();
    }

    /**
     * This method returns the visible resource cards from the deck.
     *
     * @return an array of visible resource cards
     */
    public GameCard[] getVisibleResCards() {
        return deck.visibleResCards();
    }

    /**
     * This method returns the visible gold cards from the deck.
     *
     * @return an array of visible gold cards
     */
    public GameCard[] getVisibleGoldCards() {
        return deck.visibleGoldCards();
    }

    /**
     * This method returns the current game state.
     *
     * @return the current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * This method returns the list of winning players.
     *
     * @return the list of winning players (usually 1, but ties are possible)
     * @throws IllegalGameStateException if the game has not ended yet
     */
    public List<Player> getWinners() throws IllegalGameStateException {
        return null;
    }

    /**
     * This method returns the player associated with the current client.
     *
     * @return the player associated with the current client
     */
    public Player getSelf() {
        return null;
    }

    /**
     * This method returns the currently playing player.
     *
     * @return the currently playing player
     */
    public Player getPlayingPlayer() {
        return players.get(currentPlayerIndex);
    }

}
