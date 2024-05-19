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

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.cli.Input;
import it.polimi.ingsw.am07.utils.cli.SelectableMenu;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class CLI {

    private Scanner scanner;

    private Controller controller;

    private Map<Instruction, BiConsumer<ClientState, Controller>> instructionHandler;


    private static final List<Instruction> availableInstructionsPickingCard = List.of(Instruction.PICK_CARD,Instruction.SHOW_DECK,Instruction.SHOW_FIELD,Instruction.QUIT,Instruction.SHOW_HAND);
    private static final List<Instruction> availableInstructionsPlacingCard = List.of(Instruction.PLACE_CARD,Instruction.SHOW_FIELD,Instruction.QUIT,Instruction.SHOW_HAND,Instruction.SHOW_DECK);
    private static final List<Instruction> availableInstructionsSleeping = List.of(Instruction.QUIT,Instruction.SHOW_DECK,Instruction.SHOW_HAND,Instruction.SHOW_FIELD);

    /*
        * Entry point of the CLI client. this function will be executed when the client starts in the main method.
        * It initializes the network manager and the client state.
        * It also initializes the instructions that the client can execute.
     */
    public void entrypoint() {
        scanner = new Scanner(System.in);
        ClientState clientState = new ClientState(this::render);

        // Select your identity
        System.out.println("Insert your nickname:");
        String nickname = scanner.nextLine();

        // Choose network type
        System.out.println("Press 0 for RMI, 1 for TCP:");
        int choice = Input.readBinaryChoice(scanner);

        NetworkType networkType = choice == 0 ? NetworkType.RMI : NetworkType.TCP;
        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(12345)
                .withNetworkType(networkType)
                .withIdentity(nickname)
                .withState(clientState)
                .build();

        //Initialize instructions: every instruction has a lambda that will be executed when the instruction is called
        CLIInstructionLoader loader = new CLIInstructionLoader(scanner);
        instructionHandler = loader.getInstruction();

        controller = networkManager.getController();
    }


    /**
     * Render the client state, showing the current game state and a list of available instructions.
     * Insructions are shown as a menu and the user can select one of them.
     * @param clientState the client state to render
     */
    private void render(ClientState clientState) {
        PlayerState currentState = clientState.getPlayerState();

        switch(currentState) {
            case SELECTING_LOBBY:
                //renderLobbySelection(clientState);
                break;
            case PICKING_CARD:
                renderState(clientState, availableInstructionsPickingCard);
                break;
            case PLACING_CARD:
                renderState(clientState, availableInstructionsPlacingCard);
                break;
            case SLEEPING:
                renderState(clientState, availableInstructionsSleeping);
                break;
            default:
                System.out.println("Invalid state");
        }
    }

    private void renderState(ClientState clientState, List<Instruction> availableInstructions) {
        System.out.println("Select an action:");
        SelectableMenu<Instruction> menu = new SelectableMenu<>(availableInstructions, scanner);
        int selectedOption = menu.getSelectedOptionIndex();

        Instruction instruction = availableInstructions.get(selectedOption);
        instructionHandler.get(instruction).accept(clientState, controller);
    }
}
