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
import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import it.polimi.ingsw.am07.reactive.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class CLI {

    private Scanner scanner;

    private Controller controller;

    private HashMap<Instruction, BiConsumer<ClientState, Controller>> instructionHandler;


    // TODO: da aggiungere le istruzioni
    private static final List<Instruction> availableInstructionsPickingCard = List.of();
    private static final List<Instruction> availableInstructionsPlacingCard = List.of();
    private static final List<Instruction> availableInstructionsSleeping = List.of();

    public void entrypoint() {
        // faccio scegliere tra tcp/rmi

        System.out.println("Premi 0 per RMI\n Premi 1 per Socket");

        scanner = new Scanner(System.in);

        int choice = scanner.nextInt();

        ClientState clientState = new ClientState(this::renderPickingCard);

        if( choice == 0) {
            // RMI
        } else {
            // SOCKET
        }

        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(12345)
                .withNetworkType(NetworkType.TCP)
                .withIdentity("client1")
                .withState(clientState)
                .build();

        initializeInstructions();

        controller = networkManager.getController();

    }

    public void initializeInstructions() {
        this.instructionHandler = new HashMap<>();

        this.instructionHandler.put(Instruction.PICK_CARD, (ClientState clientState, Controller dispatcher) ->
                {

                    GameCard card = null;

                    System.out.println("Quale carta vuoi scegliere: ");
                    System.out.println("Inserisci 0 per risorsa \nInserisci 1 per oro");
                    int choice = scanner.nextInt();

                    if( choice == 0) {
                        System.out.println("Inserisci 0 per deck risorsa \nInserisci 1 per carta 1\nInserisci 2 per carta 2");
                        choice = scanner.nextInt();

                        card = switch (choice) {
                            case 0 -> clientState.getGameModel().pickRandomResCard();
                            case 1 -> clientState.getGameModel().getVisibleResCards()[0];
                            case 2 -> clientState.getGameModel().getVisibleResCards()[1];
                            default -> card;
                        };

                    } else {
                        System.out.println("Inserisci 0 per deck oro \nInserisci 1 per carta 1\nInserisci 2 per carta 2");
                        choice = scanner.nextInt();

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
    }

    // same for every action
    public void renderPickingCard(ClientState clientState) {

        // TODO: lambda for action associated with instruction

        // TODO: gabo fa il menu ;)

        // esempio
        instructionHandler.get(Instruction.PICK_CARD);

    }

}
