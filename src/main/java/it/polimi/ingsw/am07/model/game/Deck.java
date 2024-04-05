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
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.utils.assets.GameResources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the game deck.
 *
 * @param availableResCards  the uncovered resource cards
 * @param availableGoldCards the uncovered gold cards
 * @param visibleResCards    the visible resource cards
 * @param visibleGoldCards   the visible gold cards
 */
public record Deck(
        List<GameCard> availableResCards,
        List<GameCard> availableGoldCards,
        GameCard[] visibleResCards,
        GameCard[] visibleGoldCards
) implements Serializable {

    public static int VISIBLE_CARDS_COUNT = 2;

    public Deck() {
        this(new ArrayList<>(), new ArrayList<>(), new GameCard[VISIBLE_CARDS_COUNT], new GameCard[VISIBLE_CARDS_COUNT]);
    }

    /**
     * This method is used to peek at the top resource card in the deck.
     * It first checks if the list of available resource cards is empty.
     * If it is, it returns null, indicating that there are no more resource cards in the deck.
     * If it is not, it returns the first card in the list, which is the top card of the deck.
     *
     * @return the top resource card in the deck, or null if the deck is empty
     */
    public GameCard peekTopResCard() {
        return availableResCards.isEmpty() ? null : availableResCards.getFirst();
    }

    /**
     * This method is used to peek at the top gold card in the deck.
     * It first checks if the list of available gold cards is empty.
     * If it is, it returns null, indicating that there are no more gold cards in the deck.
     * If it is not, it returns the first card in the list, which is the top card of the deck.
     *
     * @return the top gold card in the deck, or null if the deck is empty
     */
    public GameCard peekTopGoldCard() {
        return availableGoldCards.isEmpty() ? null : availableGoldCards.getFirst();
    }

    /**
     * Picks a random resource card from the deck and removes it.
     *
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
     *
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
     *
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
     *
     * @param card   the card to pop
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
     *
     * @param card       the card to pop
     * @param substitute the card to replace the popped card with
     * @param target     the array from which to pop the card
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

    public static class DeckFactory {
        /**
         * This method is used to create a new deck of cards for the game.
         * It first retrieves the instance of the GameResources class, which holds all the available cards.
         * Then, it creates two lists of cards: resourceCards and goldCards, by copying the respective lists from the GameResources instance.
         * After that, it shuffles these lists to ensure randomness.
         * It then creates two arrays to hold the visible resource and gold cards.
         * The first VISIBLE_CARDS_COUNT cards from the shuffled lists are then removed and placed into the respective visible cards arrays.
         * Finally, it returns a new Deck instance, passing the remaining cards in the lists and the visible cards arrays to the constructor.
         *
         * @return a new Deck instance with shuffled cards and the first few cards visible
         */
        public static Deck inflateNewDeck() {
            GameResources gameResources = GameResources.getInstance();

            List<GameCard> resourceCards = new ArrayList<>(gameResources.getResourceCards());
            List<GameCard> goldCards = new ArrayList<>(gameResources.getGoldCards());

            Collections.shuffle(resourceCards);
            Collections.shuffle(goldCards);

            GameCard[] visibleResCards = new GameCard[VISIBLE_CARDS_COUNT];
            GameCard[] visibleGoldCards = new GameCard[VISIBLE_CARDS_COUNT];

            for (int i = 0; i < VISIBLE_CARDS_COUNT; i++) {
                visibleResCards[i] = resourceCards.removeFirst();
                visibleGoldCards[i] = goldCards.removeFirst();
            }

            return new Deck(
                    resourceCards,
                    goldCards,
                    visibleResCards,
                    visibleGoldCards
            );
        }

    }

}
