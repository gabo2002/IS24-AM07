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
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.cli.Input;
import it.polimi.ingsw.am07.utils.cli.SelectableMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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


    }

    /**
     * Get the instructions that the client can execute.
     * @return the instructions that the client can execute.
     */
    public Map<Instruction, BiConsumer<ClientState, Controller>> getInstruction() {
        return instructionsMap;
    }
}
