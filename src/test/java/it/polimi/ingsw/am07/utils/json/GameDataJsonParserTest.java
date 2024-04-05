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

package it.polimi.ingsw.am07.utils.json;

import it.polimi.ingsw.am07.Application;
import it.polimi.ingsw.am07.model.game.*;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.side.*;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameDataJsonParserTest {

    @Test
    void resourceHolderSerialization() {
        GameDataJsonParser<ResourceHolder> parser = new GameDataJsonParser<>(ResourceHolder.class);

        ResourceHolder resourceHolder = new ResourceHolder();
        String json = parser.toJson(resourceHolder);
        ResourceHolder deserializedResourceHolder = parser.fromJson(json);
        assertEquals(resourceHolder, deserializedResourceHolder);

        resourceHolder = new ResourceHolder();
        resourceHolder.incrementResource(Symbol.PURPLE);
        resourceHolder.incrementResource(Symbol.BLUE);
        json = parser.toJson(resourceHolder);
        deserializedResourceHolder = parser.fromJson(json);
        assertEquals(resourceHolder, deserializedResourceHolder);
    }

    @Test
    void sideFieldRepresentationSerialization() {
        GameDataJsonParser<SideFieldRepresentation> parser = new GameDataJsonParser<>(SideFieldRepresentation.class);

        Matrix<Symbol> corners = new Matrix<>(SideFieldRepresentation.SIDE_SIZE, SideFieldRepresentation.SIDE_SIZE, Symbol.EMPTY);
        corners.set(0, 0, Symbol.PURPLE);
        corners.set(0, 1, Symbol.BLUE);
        corners.set(1, 0, Symbol.RED);
        corners.set(1, 1, Symbol.GREEN);
        SideFieldRepresentation sideFieldRepresentation = new SideFieldRepresentation(corners);

        String json = parser.toJson(sideFieldRepresentation);

        SideFieldRepresentation deserializedSideFieldRepresentation = parser.fromJson(json);

        assertTrue(sideFieldRepresentation.corners().match(deserializedSideFieldRepresentation.corners()));
    }

    @Test
    void cardListSerialization() {
        List<GameCard> cards = new ArrayList<>();

        Matrix<Symbol> corners = new Matrix<>(SideFieldRepresentation.SIDE_SIZE, SideFieldRepresentation.SIDE_SIZE, Symbol.EMPTY);
        corners.set(0, 0, Symbol.PURPLE);
        corners.set(0, 1, Symbol.BLUE);
        corners.set(1, 0, Symbol.RED);
        corners.set(1, 1, Symbol.GREEN);
        SideFieldRepresentation sideFieldRepresentation1 = new SideFieldRepresentation(corners);

        SideFrontGold sideFrontGold = new SideFrontGold(
                1, sideFieldRepresentation1, new ResourceHolder(), 1, Symbol.BLANK, new ResourceHolder(sideFieldRepresentation1), Symbol.BLUE
        );
        SideFrontStarter sideFrontStarter = new SideFrontStarter(2, sideFieldRepresentation1, new ResourceHolder(sideFieldRepresentation1));

        SideBack sideBack1 = new SideBack(1, sideFieldRepresentation1, new ResourceHolder(sideFieldRepresentation1), Symbol.PURPLE);
        SideBack sideBack2 = new SideBack(2, sideFieldRepresentation1, new ResourceHolder(sideFieldRepresentation1), Symbol.RED);

        corners = new Matrix<>(SideFieldRepresentation.SIDE_SIZE, SideFieldRepresentation.SIDE_SIZE, Symbol.EMPTY);
        corners.set(0, 0, Symbol.BLANK);
        corners.set(0, 1, Symbol.BLANK);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(1, 1, Symbol.GREEN);
        SideFieldRepresentation sideFieldRepresentation2 = new SideFieldRepresentation(corners);

        SideFrontGold sideFrontGold2 = new SideFrontGold(
                5, sideFieldRepresentation2, new ResourceHolder(sideFieldRepresentation2), 1, Symbol.FEATHER, new ResourceHolder(), Symbol.FLASK
        );
        SideFrontRes sideFrontRes = new SideFrontRes(6, sideFieldRepresentation2, new ResourceHolder(sideFieldRepresentation2), Symbol.FEATHER);

        SideBack sideBack3 = new SideBack(5, sideFieldRepresentation2, new ResourceHolder(), Symbol.FLASK);
        SideBack sideBack4 = new SideBack(6, sideFieldRepresentation2, new ResourceHolder(sideFieldRepresentation2), Symbol.FEATHER);

        cards.add(new GameCard(sideFrontGold, sideBack1));
        cards.add(new GameCard(sideFrontStarter, sideBack2));
        cards.add(new GameCard(sideFrontGold2, sideBack3));
        cards.add(new GameCard(sideFrontRes, sideBack4));

        GameDataJsonParser<GameCard> parser = new GameDataJsonParser<>(GameCard.class);

        String json = parser.listToJson(cards);

        List<GameCard> deserializedCards = parser.listFromJson(json);

        for (int i = 0; i < cards.size(); i++) {
            GameCard card = cards.get(i);
            GameCard deserializedCard = deserializedCards.get(i);

            assertEquals(card.front().id(), deserializedCard.front().id());
            assertEquals(card.front().resources(), deserializedCard.front().resources());
            assertEquals(card.front().color(), deserializedCard.front().color());
            assertTrue(card.front().fieldRepresentation().corners().match(deserializedCard.front().fieldRepresentation().corners()));

            assertEquals(card.back().id(), deserializedCard.back().id());
            assertEquals(card.back().resources(), deserializedCard.back().resources());
            assertEquals(card.back().color(), deserializedCard.back().color());
            assertTrue(card.back().fieldRepresentation().corners().match(deserializedCard.back().fieldRepresentation().corners()));
        }

        String newJson = parser.listToJson(deserializedCards);

        assertEquals(json, newJson);
    }

    @Test
    void sideListSerialization() {
        List<Side> sides = new ArrayList<>();

        Matrix<Symbol> corners = new Matrix<>(SideFieldRepresentation.SIDE_SIZE, SideFieldRepresentation.SIDE_SIZE, Symbol.EMPTY);
        corners.set(0, 0, Symbol.PURPLE);
        corners.set(0, 1, Symbol.BLUE);
        corners.set(1, 0, Symbol.RED);
        corners.set(1, 1, Symbol.GREEN);
        SideFieldRepresentation sideFieldRepresentation1 = new SideFieldRepresentation(corners);

        SideFrontGold sideFrontGold = new SideFrontGold(
                1, sideFieldRepresentation1, new ResourceHolder(), 1, Symbol.BLANK, new ResourceHolder(sideFieldRepresentation1), Symbol.BLUE
        );
        SideFrontStarter sideFrontStarter = new SideFrontStarter(2, sideFieldRepresentation1, new ResourceHolder(sideFieldRepresentation1));

        SideBack sideBack1 = new SideBack(3, sideFieldRepresentation1, new ResourceHolder(sideFieldRepresentation1), Symbol.PURPLE);
        SideBack sideBack2 = new SideBack(4, sideFieldRepresentation1, new ResourceHolder(sideFieldRepresentation1), Symbol.RED);

        corners = new Matrix<>(SideFieldRepresentation.SIDE_SIZE, SideFieldRepresentation.SIDE_SIZE, Symbol.EMPTY);
        corners.set(0, 0, Symbol.BLANK);
        corners.set(0, 1, Symbol.BLANK);
        corners.set(1, 0, Symbol.BLUE);
        corners.set(1, 1, Symbol.GREEN);
        SideFieldRepresentation sideFieldRepresentation2 = new SideFieldRepresentation(corners);

        SideFrontGold sideFrontGold2 = new SideFrontGold(
                5, sideFieldRepresentation2, new ResourceHolder(sideFieldRepresentation2), 1, Symbol.FEATHER, new ResourceHolder(), Symbol.FLASK
        );
        SideFrontRes sideFrontRes = new SideFrontRes(6, sideFieldRepresentation2, new ResourceHolder(sideFieldRepresentation2), Symbol.FEATHER);

        SideBack sideBack3 = new SideBack(7, sideFieldRepresentation2, new ResourceHolder(), Symbol.FLASK);
        SideBack sideBack4 = new SideBack(8, sideFieldRepresentation2, new ResourceHolder(sideFieldRepresentation2), Symbol.FEATHER);

        sides.add(sideFrontGold);
        sides.add(sideFrontStarter);
        sides.add(sideBack1);
        sides.add(sideBack2);
        sides.add(sideFrontGold2);
        sides.add(sideFrontRes);
        sides.add(sideBack3);
        sides.add(sideBack4);

        GameDataJsonParser<Side> parser = new GameDataJsonParser<>(Side.class);

        String json = parser.listToJson(sides);

        List<Side> deserializedSides = parser.listFromJson(json);

        for (int i = 0; i < sides.size(); i++) {
            Side side = sides.get(i);
            Side deserializedSide = deserializedSides.get(i);

            assertEquals(side.id(), deserializedSide.id());
            assertEquals(side.resources(), deserializedSide.resources());
            assertEquals(side.color(), deserializedSide.color());
            assertTrue(side.fieldRepresentation().corners().match(deserializedSide.fieldRepresentation().corners()));

            // Verify that the deserialized side is an instance of the correct subclass
            assertTrue(side.getClass().isInstance(deserializedSide));
        }

        String newJson = parser.listToJson(deserializedSides);

        assertEquals(json, newJson);
    }

    @Test
    void validateGameSerializability() {
        assertDoesNotThrow(() -> {
            Player player = new Player(
                    "test_0",
                    new ResourceHolder(),
                    Pawn.BLACK,
                    new GameField(),
                    new ArrayList<>()
            );

            List<Player> players = new ArrayList<>();
            players.add(player);

            Game game = new Game(players);

            GameDataJsonParser<Game> parser = new GameDataJsonParser<>(Game.class);

            String json = parser.toJson(game);

            Game deserializedGame = parser.fromJson(json);
        });
    }

    @Test
    void validateCardAsset() {
        String json = "";

        try (InputStream inputStream = Application.class.getResourceAsStream("cards.json")) {
            if (inputStream == null) {
                fail("Failed to load card asset");
            }
            json = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            fail("Failed to read card asset");
        }

        GameDataJsonParser<GameCard> parser = new GameDataJsonParser<>(GameCard.class);

        String finalJson = json;
        assertDoesNotThrow(() -> parser.listFromJson(finalJson));
    }

}