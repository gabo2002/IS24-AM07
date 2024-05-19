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

package it.polimi.ingsw.am07.client.cli;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.lobby.CreateLobbyAction;
import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.action.player.PlayerPlaceCardAction;
import it.polimi.ingsw.am07.client.cli.rendering.deck.CLIGameDeckRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.field.CLIGameFieldRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.playershand.CLIPlayableCardRepresentation;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.cli.Input;
import it.polimi.ingsw.am07.utils.cli.SelectableMenu;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * CLI instruction loader.
 * This class is responsible for loading the instructions that the client can execute.
 */
public class CLIInstructionLoader {

    private final Map<Instruction, BiConsumer<ClientState, Controller>> instructionsMap;

    private final Scanner scanner;

    public CLIInstructionLoader(Scanner scanner) {
        instructionsMap = new HashMap<>(Instruction.values().length);
        this.scanner = scanner;
        loadInstructions();
    }

    /**
     * Load the instructions that the client can execute.
     * The instructions are loaded into a map, where the key is the instruction and the value is a BiConsumer that
     * takes the client state and the dispatcher as arguments.
     * The BiConsumer is responsible for executing the instruction.
     */
    private void loadInstructions() {
        instructionsMap.put(Instruction.PICK_CARD, (ClientState clientState, Controller dispatcher) ->
        {
            GameCard card = null;
            SelectableMenu menu = null;
            int choice = 0;

            System.out.println("CARD SELECTION: ");
            menu = new SelectableMenu(List.of("Pick a Resource Card", "Pick a Gold Card"), scanner);
            menu.show();
            choice = menu.getSelectedOptionIndex();

            if (choice == 0) {
                System.out.println("RESOURCE CARD SELECTION: ");
                System.out.println("Insert 0 to pick a resource deck card \nInsert 1 to pick the first visible resource card \nInsert 2 to pick the second visible resource card");
                choice = Input.readInt(scanner,0,2);

                card = switch (choice) {
                    case 0 -> clientState.getGameModel().pickRandomResCard();
                    case 1 -> clientState.getGameModel().getVisibleResCards()[0];
                    case 2 -> clientState.getGameModel().getVisibleResCards()[1];
                    default -> card;
                };

            } else {
                System.out.println("GOLD CARD SELECTION: ");
                System.out.println("Insert 0 for gold deck card \nInsert 1 for the first visible gold card \nInsert 2 for the second visible gold card");
                choice = Input.readInt(scanner,0,2);

                card = switch (choice) {
                    case 0 -> clientState.getGameModel().pickRandomGoldCard();
                    case 1 -> clientState.getGameModel().getVisibleGoldCards()[0];
                    case 2 -> clientState.getGameModel().getVisibleGoldCards()[1];
                    default -> card;
                };
            }

            Action action = new PlayerPickCardAction(clientState.getGameModel().getSelfNickname(), card);
            // TODO: vedere error handling nelle actions (da definire)
            dispatcher.execute(action);
        });

        instructionsMap.put(Instruction.SHOW_FIELD, (ClientState clientState, Controller dispatcher) ->
        {
            //The user has to select a field to get
            List<Player> players = clientState.getGameModel().getPlayers();
            SelectableMenu menu = new SelectableMenu(players.stream().map(Player::getNickname).toList(), scanner);
            menu.show();
            //get the selected player
            String playerNickname = menu.getSelectedOption();
            Player player = players.stream().filter(p -> p.getNickname().equals(playerNickname)).findFirst().orElse(null);

            //render the field of the selected player
            CLIGameFieldRepresentation render = new CLIGameFieldRepresentation(player.getPlayerGameField());
            System.out.println(render.render());
        });

        instructionsMap.put(Instruction.PLACE_CARD, (ClientState clientState, Controller dispatcher) ->
        {
            List<GameCard> cards = clientState.getGameModel().getSelf().getPlayableCards();
            List<Side> sides = new ArrayList<>();
            int selectedCardIndex, row = 0, column = 0;

            for (GameCard card : cards) {
                sides.add(card.front());
                sides.add(card.back());
            }
            //TODO: dinamically render the cards and the sides
            SelectableMenu menu = new SelectableMenu(sides.stream().map(Side::toString).toList(), scanner);
            menu.show();
            selectedCardIndex = menu.getSelectedOptionIndex();
            boolean validPosition = false;

            while (!validPosition) {
                //choose the position where to place the card
                System.out.println("Insert the row where you want to place the card: ");
                row = Input.readInt(scanner);
                System.out.println("Insert the column where you want to place the card: ");
                column = Input.readInt(scanner);

                GameFieldPosition position = new GameFieldPosition(row, column);
                validPosition = clientState.getGameModel().getSelf().canBePlacedAt(sides.get(selectedCardIndex), position);

                if (!validPosition) {
                    System.out.println("Invalid position, please try again.");
                }
            }

            Action action = new PlayerPlaceCardAction(clientState.getGameModel().getSelfNickname(), sides.get(selectedCardIndex), new GameFieldPosition(row, column));
            dispatcher.execute(action);
        });

        instructionsMap.put(Instruction.SHOW_DECK, (ClientState clientState, Controller dispatcher) ->
        {
            CLIGameDeckRepresentation deckRepresentation = new CLIGameDeckRepresentation(clientState.getGameModel().getDeck());
            System.out.println(deckRepresentation.render());
        });

        instructionsMap.put(Instruction.QUIT, (ClientState clientState, Controller dispatcher) ->
        {
            System.out.println("Quitting...");
            System.exit(0);
        });

        instructionsMap.put(Instruction.CREATE_LOBBY, (ClientState clientState, Controller dispatcher) ->
        {
            System.out.println("Insert your nickname:");
            String nickname = scanner.nextLine();

            Action action = new CreateLobbyAction(nickname);
            dispatcher.execute(action);
        });

        instructionsMap.put(Instruction.JOIN_LOBBY, (ClientState clientState, Controller dispatcher) ->
        {
            System.out.println("Insert the lobby code:");
            //TODO i should retrieve the lobby code from the user
        });

        instructionsMap.put(Instruction.SELECT_COLOR, (ClientState clientState, Controller dispatcher) ->
        {
            System.out.println("Insert the color you want to play with:");
            List<String> playerColors = new ArrayList<>(Arrays.stream(Pawn.values()).map(Pawn::toString).toList());
            playerColors.remove("BLACK");
            SelectableMenu menu = new SelectableMenu(playerColors, scanner);
            menu.show();

            int selectedColorIndex = menu.getSelectedOptionIndex();
            //TODO i should send the selected color to the server
        });

        instructionsMap.put(Instruction.SHOW_HAND, (ClientState clientState, Controller dispatcher) ->
        {
            List<GameCard> cards = clientState.getGameModel().getSelf().getPlayableCards();
            System.out.println("Your hand:");
            CLIPlayableCardRepresentation handRepresentation = new CLIPlayableCardRepresentation(cards);
            System.out.println(handRepresentation.render());
        });
    }

    /**
     * Get the instructions that the client can execute.
     * @return the instructions that the client can execute.
     */
    public Map<Instruction, BiConsumer<ClientState, Controller>> getInstruction() {
        return instructionsMap;
    }
}
