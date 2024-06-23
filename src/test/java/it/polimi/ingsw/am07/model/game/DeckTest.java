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
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private GameCard craftCard(int id) {
        return new GameCard(new SideFrontRes(id, null, null, null), new SideBack(id, null, null, null));
    }

    private Deck constructDeck() {
        List<GameCard> resCards = new ArrayList<>();
        List<GameCard> goldCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            resCards.add(craftCard(i));
            goldCards.add(craftCard(i + 10));
        }
        GameCard[] visibleResCards = new GameCard[2];
        GameCard[] visibleGoldCards = new GameCard[2];
        for (int i = 0; i < 2; i++) {
            visibleResCards[i] = craftCard(i + 20);
            visibleGoldCards[i] = craftCard(i + 30);
        }
        return new Deck(resCards, goldCards, visibleResCards, visibleGoldCards);
    }

    @Test
    void popRandomResCard() {
        Deck deck = constructDeck();
        int size = deck.availableResCards().size();
        GameCard card = deck.popRandomResCard();

        assertEquals(size - 1, deck.availableResCards().size());
        assertFalse(deck.availableResCards().contains(card));
    }

    @Test
    void popRandomGoldCard() {
        Deck deck = constructDeck();
        int size = deck.availableGoldCards().size();
        GameCard card = deck.popRandomGoldCard();

        assertEquals(size - 1, deck.availableGoldCards().size());
        assertFalse(deck.availableGoldCards().contains(card));
    }

    @Test
    void popCard() {
        Deck deck_1 = constructDeck();
        Deck deck_2 = constructDeck();

        // Pick a random resource card
        GameCard resCard = deck_1.popRandomResCard();
        assertDoesNotThrow(() -> deck_2.popCard(resCard));
        assertEquals(deck_1.availableResCards().size(), deck_2.availableResCards().size());
        assertFalse(deck_2.availableResCards().contains(resCard));
        for (GameCard card : deck_1.availableResCards()) {
            assertTrue(deck_2.availableResCards().contains(card));
        }

        // Popping an already popped resource card should throw an exception
        assertThrows(CardNotFoundException.class, () -> deck_1.popCard(resCard));
        assertThrows(CardNotFoundException.class, () -> deck_2.popCard(resCard));

        // Pick a random gold card
        GameCard goldCard = deck_1.popRandomGoldCard();
        assertDoesNotThrow(() -> deck_2.popCard(goldCard));
        assertEquals(deck_1.availableGoldCards().size(), deck_2.availableGoldCards().size());
        assertFalse(deck_2.availableGoldCards().contains(goldCard));
        for (GameCard card : deck_1.availableGoldCards()) {
            assertTrue(deck_2.availableGoldCards().contains(card));
        }

        // Popping an already popped gold card should throw an exception
        assertThrows(CardNotFoundException.class, () -> deck_1.popCard(goldCard));
        assertThrows(CardNotFoundException.class, () -> deck_2.popCard(goldCard));

        // Pick a visible resource card and pop it, it should be replaced with a new card
        GameCard visibleResCard = deck_1.visibleResCards()[0];
        assertDoesNotThrow(() -> deck_1.popCard(visibleResCard));
        assertNotNull(deck_1.visibleResCards()[0]);
        assertNotNull(deck_2.visibleResCards()[1]);
        assertNotEquals(visibleResCard, deck_1.visibleResCards()[0]);
        assertNotEquals(visibleResCard, deck_2.visibleResCards()[1]);
        assertFalse(deck_1.availableResCards().contains(visibleResCard));
        assertFalse(deck_1.availableResCards().contains(deck_1.visibleResCards()[0]));
        assertFalse(deck_1.availableResCards().contains(deck_2.visibleResCards()[1]));

        // The deck in the previous state should have the new visible card as the first hidden card
        assertEquals(deck_1.visibleResCards()[0], deck_2.availableResCards().getFirst());
        assertEquals(deck_1.visibleResCards()[1], deck_2.visibleResCards()[1]);
        deck_2.popRandomResCard();
        for (GameCard card : deck_2.availableResCards()) {
            assertTrue(deck_1.availableResCards().contains(card));
        }

        // If there are no more hidden cards, popping a visible card should not replace it
        GameCard visibleGoldCard = deck_1.visibleGoldCards()[0];
        for (int i = deck_1.availableGoldCards().size(); i > 0; i--) {
            deck_1.popRandomGoldCard();
        }
        assertNull(deck_1.popRandomGoldCard());
        assertDoesNotThrow(() -> deck_1.popCard(visibleGoldCard));
        assertNull(deck_1.visibleGoldCards()[0]);
    }

    @Test
    void availableResCards() {
        Deck deck = constructDeck();
        assertEquals(10, deck.availableResCards().size());
    }

    @Test
    void availableGoldCards() {
        Deck deck = constructDeck();
        assertEquals(10, deck.availableGoldCards().size());
    }

    @Test
    void visibleResCards() {
        Deck deck = constructDeck();
        assertEquals(2, deck.visibleResCards().length);
    }

    @Test
    void visibleGoldCards() {
        Deck deck = constructDeck();
        assertEquals(2, deck.visibleGoldCards().length);
    }

    @Test
    void deckFactory() {
        final Deck base = new Deck.Factory().build();

        assertThrows(RuntimeException.class, () -> {
            new Deck.Factory()
                    .resourceCards(base.availableResCards())
                    .build();
        });

        assertThrows(RuntimeException.class, () -> {
            new Deck.Factory()
                    .goldCards(base.availableGoldCards())
                    .goldCards(base.availableResCards())
                    .build();
        });

        assertDoesNotThrow(() -> {
            new Deck.Factory()
                    .resourceCards(base.availableResCards())
                    .goldCards(base.availableGoldCards())
                    .visibleResCards(base.visibleResCards())
                    .visibleGoldCards(base.visibleGoldCards())
                    .build();
        });
    }

}