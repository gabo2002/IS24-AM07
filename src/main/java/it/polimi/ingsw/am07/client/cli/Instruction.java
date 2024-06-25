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
import it.polimi.ingsw.am07.action.chat.SendMessageAction;
import it.polimi.ingsw.am07.action.lobby.CreateLobbyAction;
import it.polimi.ingsw.am07.action.lobby.GameStartAction;
import it.polimi.ingsw.am07.action.lobby.PlayerJoinAction;
import it.polimi.ingsw.am07.action.player.PlayerInitialChoiceAction;
import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.action.player.PlayerPlaceCardAction;
import it.polimi.ingsw.am07.client.cli.input.SelectableMenu;
import it.polimi.ingsw.am07.client.cli.input.ThreadInputReader;
import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.CLIGameSymbol;
import it.polimi.ingsw.am07.client.cli.rendering.common.CLIPawnColor;
import it.polimi.ingsw.am07.client.cli.rendering.deck.CLIGameDeckRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.field.CLIGameFieldRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.lobby.CLILobbyRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.objectiveCardSelection.CLIObjectiveCardSelectionRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.playershand.CLIPlayableCardRepresentation;
import it.polimi.ingsw.am07.client.cli.rendering.starterCard.CLIStarterCardRepresentation;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.chat.ChatMessage;
import it.polimi.ingsw.am07.model.game.Pawn;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.lobby.LobbyPlayer;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.lambda.TriConsumer;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum Instruction {
    CREATE_LOBBY("create_lobby", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        String nickname = "";
        System.out.println("Insert your nickname:");
        try {
            nickname = scanner.getInput();
        } catch (InterruptedException e) {
            return;
        }
        clientState.setNickname(nickname);
        List<Pawn> pawns = new ArrayList<>(List.of(Pawn.values()));
        pawns.remove(Pawn.BLACK);
        System.out.println("Selection your PAWN Color: ");
        SelectableMenu<Pawn> pawnSelectableMenu = new SelectableMenu<>(pawns, scanner);
        try {
            pawnSelectableMenu.show();
        } catch (InterruptedException e) {
            return;
        }

        Action action = new CreateLobbyAction(nickname, clientState.getIdentity(), pawnSelectableMenu.getSelectedOption());
        dispatcher.execute(action);
    }),
    JOIN_LOBBY("join_lobby", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        if (clientState.getAvailableLobbies().isEmpty()) {
            System.out.println("No lobbies available, cannot execute the instruction.");
            return;
        }

        List<Lobby> lobbies = clientState.getAvailableLobbies();
        List<String> availableLobbies = clientState.getAvailableLobbies().stream().map(lobby -> new CLILobbyRepresentation(lobby).render()).toList();

        System.out.println("Choose the lobby you want to join:");
        SelectableMenu<String> menu = new SelectableMenu<>(availableLobbies, scanner);
        try {
            menu.show();
        } catch (InterruptedException e) {
            return;
        }
        Lobby lobby = lobbies.get(menu.getSelectedOptionIndex());
        //Selecting nickname
        System.out.println("Insert your nickname:");
        String nickname;
        try {
            nickname = scanner.getInput();
        } catch (InterruptedException e) {
            return;
        }

        clientState.setNickname(nickname);

        //Choosing the color
        List<Pawn> playerColors = new ArrayList<>(Arrays.stream(Pawn.values()).toList());
        playerColors.remove(Pawn.BLACK);

        for (LobbyPlayer player : lobby.getPlayers()) {
            System.out.println(player.getPlayerPawn());
            playerColors.remove(player.getPlayerPawn());
        }

        System.out.println("Insert the color you want to play with:");
        SelectableMenu<Pawn> colorMenu = new SelectableMenu<>(playerColors, scanner);
        try {
            colorMenu.show();
        } catch (InterruptedException e) {
            return;
        }
        Pawn color = colorMenu.getSelectedOption();

        //Sending join lobby packet
        Action action = new PlayerJoinAction(nickname, clientState.getIdentity(), lobby.getId(), color);
        dispatcher.execute(action);
    }),
    SELECT_CARD("select_card", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        // 1. Select Starter Card Side
        System.out.println(clientState.getGameModel().getSelf());
        GameCard card = clientState.getGameModel().getSelf().getStarterCard();

        //display the card
        System.out.println("Starter Card: ");
        CLIStarterCardRepresentation representation = new CLIStarterCardRepresentation(card);
        System.out.println(representation.render());

        List<String> options = List.of("Front", "Back");
        SelectableMenu<String> menu = new SelectableMenu<>(options, scanner);
        try {
            menu.show();
        } catch (InterruptedException e) {
            return;
        }
        int choice = menu.getSelectedOptionIndex();

        Side side = switch (choice) {
            case 0 -> card.front();
            case 1 -> card.back();
            default -> null;
        };

        // 2. Select Objective Card
        ObjectiveCard[] objectiveCards = clientState.getGameModel().getSelf().getAvailableObjectives();
        //display the cards
        System.out.println("Objective Cards: ");
        CLIObjectiveCardSelectionRepresentation objectiveCardRepresentation = new CLIObjectiveCardSelectionRepresentation(objectiveCards);
        System.out.println(objectiveCardRepresentation.render());

        //select the card
        SelectableMenu<String> objectiveCardMenu = new SelectableMenu<>(List.of("First", "Second"), scanner);
        try {
            objectiveCardMenu.show();
        } catch (InterruptedException e) {
            return;
        }
        int selectedCardIndex = objectiveCardMenu.getSelectedOptionIndex();

        // Sending the action to notify the server the selected cards
        Action action = new PlayerInitialChoiceAction(clientState.getGameModel().getSelfNickname(), clientState.getIdentity(), objectiveCards[selectedCardIndex], side);
        dispatcher.execute(action);
    }),
    PLACE_CARD("place_card", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        List<GameCard> cards = clientState.getGameModel().getSelf().getPlayableCards();
        List<Side> sides = new ArrayList<>();
        int selectedCardIndex, row = 0, column = 0;
        boolean validPosition = false;

        while (!validPosition) {

            for (GameCard card : cards) {
                sides.add(card.front());
                sides.add(card.back());
            }

            SelectableMenu<Side> menu = new SelectableMenu<>(sides.stream().toList(), scanner);
            try {
                menu.show();
            } catch (InterruptedException e) {
                return;
            }
            selectedCardIndex = menu.getSelectedOptionIndex();
            //choose the position where to place the card
            System.out.println("Insert the row where you want to place the card: ");
            try {
                row = scanner.getInt();
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Insert the column where you want to place the card: ");
            try {
                column = scanner.getInt();
            } catch (InterruptedException e) {
                return;
            }

            GameFieldPosition position = new GameFieldPosition(row, column);
            validPosition = clientState.getGameModel().getSelf().canBePlacedAt(sides.get(selectedCardIndex), position);

            if (!validPosition) {
                System.out.println("Invalid position, please try again.");
            } else {
                Action action = new PlayerPlaceCardAction(clientState.getGameModel().getSelfNickname(), clientState.getIdentity(), sides.get(selectedCardIndex), new GameFieldPosition(row, column));
                dispatcher.execute(action);
            }
        }
    }),
    PICK_CARD("pick_card", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        GameCard card = null;
        SelectableMenu<String> menu = null;
        int choice = 0;

        System.out.println("CARD SELECTION: ");
        menu = new SelectableMenu<>(List.of("Pick a Resource Card", "Pick a Gold Card"), scanner);
        try {
            menu.show();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        choice = menu.getSelectedOptionIndex();

        if (choice == 0) {
            System.out.println("RESOURCE CARD SELECTION: ");
            System.out.println("Insert 0 to pick a resource deck card \nInsert 1 to pick the first visible resource card \nInsert 2 to pick the second visible resource card");

            try {
                choice = scanner.getInt(0, 2);
            } catch (InterruptedException e) {
                return;
            }

            card = switch (choice) {
                case 0 -> clientState.getGameModel().pickRandomResCard();
                case 1 -> clientState.getGameModel().getVisibleResCards()[0];
                case 2 -> clientState.getGameModel().getVisibleResCards()[1];
                default -> card;
            };

        } else {
            System.out.println("GOLD CARD SELECTION: ");
            System.out.println("Insert 0 for gold deck card \nInsert 1 for the first visible gold card \nInsert 2 for the second visible gold card");

            try {
                choice = scanner.getInt(0, 2);
            } catch (InterruptedException e) {
                return;
            }

            card = switch (choice) {
                case 0 -> clientState.getGameModel().pickRandomGoldCard();
                case 1 -> clientState.getGameModel().getVisibleGoldCards()[0];
                case 2 -> clientState.getGameModel().getVisibleGoldCards()[1];
                default -> card;
            };
        }

        Action action = new PlayerPickCardAction(clientState.getGameModel().getSelfNickname(), clientState.getIdentity(), card);
        dispatcher.execute(action);
    }),
    SHOW_FIELD("show_field", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        //The user has to select a field to get
        List<Player> players = clientState.getGameModel().getPlayers();
        List<String> playerNicknames = players.stream().map(Player::getNickname).toList();
        SelectableMenu<String> menu = new SelectableMenu<>(playerNicknames, scanner);
        try {
            menu.show();
        } catch (InterruptedException e) {
            return;
        }
        //get the selected player
        String playerNickname = menu.getSelectedStringOption();
        Player player = players.stream().filter(p -> p.getNickname().equals(playerNickname)).findFirst().orElse(null);

        if (player == null) {
            System.out.println("Player not found");
            return;
        }

        //render the field of the selected player
        CLIGameFieldRepresentation render = new CLIGameFieldRepresentation(player.getPlayerGameField());
        System.out.println(render.render());
    }),

    SHOW_RESOURCES("show_resources", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        Player player = clientState.getGameModel().getSelf();
        System.out.println("Your Resources:");

        //Displaying the resources
        player.getPlayerResources().getResources().forEach((symbol, amount) -> {
            System.out.println(CLIGameSymbol.gameSymbolToChar(symbol) + " - " + amount);
        });
    }),

    SHOW_DECK("show_deck", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        CLIGameDeckRepresentation deckRepresentation = new CLIGameDeckRepresentation(clientState.getGameModel().getDeck());
        System.out.println(deckRepresentation.render());
    }),
    SHOW_OBJECTIVE_CARD("show_objective_card", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        ObjectiveCard[] objectiveCards = clientState.getGameModel().getSelf().getAvailableObjectives();
        //display the cards
        System.out.println("Objective Cards: ");
        CLIObjectiveCardSelectionRepresentation objectiveCardRepresentation = new CLIObjectiveCardSelectionRepresentation(objectiveCards);
        System.out.println(objectiveCardRepresentation.render());
    }),
    SHOW_PLAYERS_SCORE("show_players_score", (ClientState clientState, Controller dispatcher, ThreadInputReader reader) ->
    {
        List<Player> players = clientState.getGameModel().getPlayers();
        System.out.println("Players score:");

        //Sorting the players by score
        players.sort(Comparator.comparingInt(Player::getPlayerScore).reversed());

        for (Player player : players) {
            System.out.println(CLIPawnColor.pawnToColor(player.getPlayerPawn()) + player.getNickname() + " - " + player.getPlayerScore() + CLIColor.RESET.getCode());
        }
    }),
    SHOW_HAND("show_hand", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        List<GameCard> cards = clientState.getGameModel().getSelf().getPlayableCards();
        System.out.println("Your hand:");
        CLIPlayableCardRepresentation handRepresentation = new CLIPlayableCardRepresentation(cards);
        System.out.println(handRepresentation.render());
    }),
    SEND_MESSAGE("send_message", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        System.out.println("Insert the message you want to send:");
        String stringMessage;
        try {
            stringMessage = scanner.getInput();
        } catch (InterruptedException e) {
            return;
        }

        ChatMessage message = clientState.getGameModel().getSelf().getChat().sendBroadcastMessage(stringMessage);

        Action action = new SendMessageAction(clientState.getGameModel().getSelfNickname(), clientState.getIdentity(), message);
        dispatcher.execute(action);
    }),
    SHOW_CHAT("show_chat", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        //Retrieve the last 20 messages
        List<ChatMessage> messages = clientState.getGameModel().getSelf().getChat().getLastMessages(20);
        System.out.println("Chat:");
        for (ChatMessage message : messages) {
            //Formatting the timestamp
            DateFormat dateFormat = DateFormat.getTimeInstance();
            System.out.println("[" + dateFormat.format(message.timestamp()) + "] " + message.senderNickname() + ": " + message.message());
        }
    }),
    START_GAME("start_game", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        Action startGameAction = new GameStartAction(clientState.getLobbyModel().getFirstPlayer().getNickname(), clientState.getIdentity());
        dispatcher.execute(startGameAction);
    }),
    SHOW_LOBBY_PLAYER("show_lobby_player", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        List<LobbyPlayer> players = clientState.getLobbyModel().getPlayers();
        System.out.println("Players:");

        for (int i = 0; i < players.size(); i++) {
            //setting current Color
            System.out.print(CLIPawnColor.pawnToColor(players.get(i).getPlayerPawn()));
            System.out.println((i + 1) + " - " + players.get(i).getNickname());
            System.out.print(CLIColor.RESET.getCode());
        }

    }),
    QUIT("quit", (ClientState clientState, Controller dispatcher, ThreadInputReader scanner) ->
    {
        System.out.println("Quitting...");
        System.exit(0);
    });

    private final TriConsumer<ClientState, Controller, ThreadInputReader> action;

    Instruction(String instruction_line, TriConsumer<ClientState, Controller, ThreadInputReader> clientStateControllerScanner) {
        this.action = clientStateControllerScanner;
    }

    public void execute(ClientState state, Controller dispatcher, ThreadInputReader scanner) {
        action.accept(state, dispatcher, scanner);
    }
}
