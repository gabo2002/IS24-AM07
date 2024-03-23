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

import java.util.List;

/**
 * Represents the game deck.
 * @param availableResCards the uncovered resource cards
 * @param availableGoldCards the uncovered gold cards
 * @param visibleResCards the visible resource cards
 * @param visibleGoldCards the visible gold cards
 */
public record Deck(
        List<GameCard> availableResCards,
        List<GameCard> availableGoldCards,
        GameCard[] visibleResCards,
        GameCard[] visibleGoldCards
) {

    /**
     * Picks a random resource card from the deck and removes it.
     * @return the picked card
     */
    public GameCard pickRandomResCard() {
        if (availableResCards.isEmpty()) {
            return null;
        }

        return availableResCards.removeFirst();
    }

    /**
     * Picks a random gold card from the deck and removes it.
     * @return the picked card
     */
    public GameCard pickRandomGoldCard() {
        if (availableGoldCards.isEmpty()) {
            return null;
        }

        return availableGoldCards.removeFirst();
    }

    /**
     * Pops a card from the deck and, if needed, replaces it with a substitute card.
     * @param card the card to pop
     * @throws CardNotFoundException if the card is not found in the deck
     */
    public void popCard(GameCard card) throws CardNotFoundException {
        if (popHiddenCard(card, availableResCards)) {
            return;
        }

        if (popHiddenCard(card, availableGoldCards)) {
            return;
        }

        if (popVisibleCard(card, pickRandomResCard(), visibleResCards)) {
            return;
        }

        if (popVisibleCard(card, pickRandomGoldCard(), visibleGoldCards)) {
            return;
        }

        throw new CardNotFoundException("Card not found in the deck");
    }

    /**
     * Pops a card from the deck
     * @param card the card to pop
     * @param target the list from which to pop the card
     * @return true if the card was found and popped, false otherwise
     */
    private boolean popHiddenCard(GameCard card, List<GameCard> target) {
        for (GameCard c : target) {
            if (c.equals(card)) {
                target.remove(c);

                return true;
            }
        }

        return false;
    }

    /**
     * Pops a visible card from the deck
     * @param card the card to pop
     * @param substitute the card to replace the popped card with
     * @param target the array from which to pop the card
     * @return true if the card was found and popped, false otherwise
     */
    private boolean popVisibleCard(GameCard card, GameCard substitute, GameCard[] target) {
        for (int i = 0; i < target.length; i++) {
            if (target[i].equals(card)) {
                target[i] = substitute;

                return true;
            }
        }

        return false;
    }

}
