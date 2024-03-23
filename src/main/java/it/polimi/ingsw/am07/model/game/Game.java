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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private final List<Player> players;

    private final Deck deck;

    private final String selfNickname;

    private final List<ObjectiveCard> availableObjectiveCards;

    private final ObjectiveCard[] commonObjectives; //only 2 cards

    private int currentPlayerIndex;    //Reminder: randomly generated first

    private GameState gameState;

    public Game(String selfNickname) {
        this.selfNickname = selfNickname;
        players = new ArrayList<>();
        deck = new Deck(
                new ArrayList<>(),
                new ArrayList<>(),
                new GameCard[2],
                new GameCard[2]
        );
        availableObjectiveCards = new ArrayList<>();
        commonObjectives = new ObjectiveCard[2];
        currentPlayerIndex = 0;
        gameState = GameState.STARTING;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getSelfNickname() {
        return selfNickname;
    }

    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    public void incrementTurn() {

    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public GameCard pickRandomResCard() {
        return deck.pickRandomResCard();
    }

    public GameCard pickRandomGoldCard() {
        return deck.pickRandomGoldCard();
    }

    public void popCard(GameCard card) throws CardNotFoundException {
        deck.popCard(card);
    }

    public int getAvailableResCardsSize() {
        return deck.availableResCards().size();
    }

    public int getAvailableGoldCardsSize() {
        return deck.availableGoldCards().size();
    }

    public GameCard[] getVisibleResCards() {
        return deck.visibleResCards();
    }

    public GameCard[] getVisibleGoldCards() {
        return deck.visibleGoldCards();
    }

    public GameState getGameState() {
        return gameState;
    }

}
