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

import it.polimi.ingsw.am07.action.lobby.ReconnectAction;
import it.polimi.ingsw.am07.client.cli.input.SelectableMenu;
import it.polimi.ingsw.am07.client.cli.input.ThreadInputReader;
import it.polimi.ingsw.am07.client.cli.rendering.CLIColor;
import it.polimi.ingsw.am07.client.cli.rendering.common.CLIPawnColor;
import it.polimi.ingsw.am07.exceptions.IllegalGameStateException;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.IdentityManager;
import it.polimi.ingsw.am07.utils.assets.AssetsRegistry;
import it.polimi.ingsw.am07.utils.logging.AppLogger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CLI {

    private static final List<Instruction> availableInstructionsPickingCard = List.of(
            Instruction.SEND_MESSAGE,
            Instruction.SHOW_CHAT,
            Instruction.SHOW_PLAYERS_SCORE,
            Instruction.SHOW_OBJECTIVE_CARD,
            Instruction.SHOW_RESOURCES,
            Instruction.PICK_CARD,
            Instruction.SHOW_DECK,
            Instruction.SHOW_FIELD,
            Instruction.SHOW_HAND,
            Instruction.QUIT
    );
    private static final List<Instruction> availableInstructionsPlacingCard = List.of(
            Instruction.SEND_MESSAGE,
            Instruction.SHOW_CHAT,
            Instruction.SHOW_PLAYERS_SCORE,
            Instruction.SHOW_OBJECTIVE_CARD,
            Instruction.SHOW_RESOURCES,
            Instruction.PLACE_CARD,
            Instruction.SHOW_FIELD,
            Instruction.SHOW_HAND,
            Instruction.SHOW_DECK,
            Instruction.QUIT
    );
    private static final List<Instruction> availableInstructionsSleeping = List.of(
            Instruction.SEND_MESSAGE,
            Instruction.SHOW_CHAT,
            Instruction.SHOW_PLAYERS_SCORE,
            Instruction.SHOW_OBJECTIVE_CARD,
            Instruction.SHOW_RESOURCES,
            Instruction.SHOW_DECK,
            Instruction.SHOW_HAND,
            Instruction.SHOW_FIELD,
            Instruction.QUIT
    );
    private static final List<Instruction> availableInstructionsLobby = List.of(
            Instruction.JOIN_LOBBY,
            Instruction.CREATE_LOBBY,
            Instruction.RECONNECT,
            Instruction.QUIT
    );
    private static final List<Instruction> availableInstructionsWaitingForPlayers = List.of(
            Instruction.SHOW_LOBBY_PLAYER,
            Instruction.QUIT
    );
    private static final List<Instruction> availableInstructionAdminWaitingForPlayers = List.of(
            Instruction.START_GAME,
            Instruction.SHOW_LOBBY_PLAYER,
            Instruction.QUIT
    );
    private static final List<Instruction> availableInstructionsSelectingStarterCardSide = List.of(
            Instruction.SEND_MESSAGE,
            Instruction.SHOW_CHAT,
            Instruction.SHOW_PLAYERS_SCORE,
            Instruction.SELECT_CARD,
            Instruction.SHOW_HAND,
            Instruction.SHOW_FIELD,
            Instruction.QUIT
    );
    private final AppLogger LOGGER = new AppLogger(CLI.class);
    private final ExecutorService renderExecutor;
    private ThreadInputReader reader;
    private Controller controller;
    private Future<?> currentRenderTask;

    private ClientNetworkManager networkManager;

    public CLI() {
        System.setProperty("cli", "true");
        renderExecutor = Executors.newSingleThreadExecutor();
        currentRenderTask = null;

        networkManager = null;
    }

    /**
     * Entry point of the CLI client. this function will be executed when the client starts in the main method.
     * It initializes the network manager and the client state.
     * It also initializes the instructions that the client can execute.
     */
    public void entrypoint() {
        reader = new ThreadInputReader();

        //generate Identity
        String identity = IdentityManager.getConstantIdentity();
        ClientState clientState = new ClientState(this::threadRender, identity);

        // Choose network type
        System.out.println("Press 0 for RMI, 1 for TCP:");
        int choice;
        try {
            choice = reader.getInt(0, 1);
        } catch (InterruptedException e) {
            choice = 0;
        }

        //Hostname selection
        System.out.println("Enter the hostname:");
        String hostname;
        try {
            hostname = reader.getInput();
        } catch (InterruptedException e) {
            hostname = "";
        }

        if (hostname.isEmpty()) {
            hostname = "localhost";
        }

        NetworkType networkType = choice == 0 ? NetworkType.RMI : NetworkType.TCP;
        int port = choice == 0 ? AssetsRegistry.getInstance().getGameResourceDefinition().rmiPort() : AssetsRegistry.getInstance().getGameResourceDefinition().tcpPort();

        networkManager = new ClientNetworkManager.Factory()
                .withHostname(hostname)
                .withPort(port)
                .withNetworkType(networkType)
                .withIdentity(identity)
                .withState(clientState)
                .build();

        controller = networkManager.getController();
    }


    /**
     * Render the client state, showing the current game state and a list of available instructions.
     * Insructions are shown as a menu and the user can select one of them.
     *
     * @param clientState the client state to render
     */
    private void render(ClientState clientState) {
        PlayerState currentState = clientState.getPlayerState();

        String message = clientState.getClientStringErrorMessage();

        if (message != null) {
            System.out.println(message);
        }

        switch (currentState) {
            case ADMIN_WAITING_FOR_PLAYERS:
                renderState(clientState, availableInstructionAdminWaitingForPlayers);
                break;
            case WAITING_FOR_PLAYERS:
                renderState(clientState, availableInstructionsWaitingForPlayers);
                break;
            case SELECTING_LOBBY:
                renderState(clientState, availableInstructionsLobby);
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
            case SELECTING_STARTER_CARD_SIDE:
                renderState(clientState, availableInstructionsSelectingStarterCardSide);
                break;
            case WAITING_FOR_GAME_START:
                System.out.println("Waiting for game to start");
                renderState(clientState, List.of(Instruction.QUIT));
                break;
            case GAME_ENDED:
                System.out.println("Game ended");
                try {
                    List<Player> winners = clientState.getGameModel().getWinners();
                    System.out.println("Winners:");
                    for (Player winner : winners) {
                        System.out.println(CLIPawnColor.pawnToColor(winner.getPlayerPawn()) + winner.getNickname() + CLIColor.RESET.getCode());
                    }
                } catch (IllegalGameStateException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
                break;
            case DISCONNECTED:
                System.out.println("Disconnected from server. Attempting reconnection");
                networkManager.reconnect(clientState);
                controller = networkManager.getController();

                if (controller == null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        LOGGER.error(e);
                    }
                } else {
                    clientState.setPlayerState(PlayerState.SLEEPING);
                    controller.execute(new ReconnectAction(clientState.getNickname(), clientState.getIdentity()));
                }

                break;
            default:
                System.out.println("Invalid state");
                System.out.println("Current state: " + currentState);
        }

        clientState.notifyGameModelUpdate();
    }

    private void renderState(ClientState clientState, List<Instruction> availableInstructions) {
        SelectableMenu<Instruction> menu = new SelectableMenu<>(availableInstructions, reader);
        try {
            menu.show();
        } catch (InterruptedException e) {
            //clear the screen
            System.out.print("\033[H\033[2J");
            return; //I have killed the render thread
        }
        int selectedOption = menu.getSelectedOptionIndex();

        //clear the screen
        System.out.print("\033[H\033[2J");

        Instruction instruction = availableInstructions.get(selectedOption);
        instruction.execute(clientState, controller, reader);
    }

    private void threadRender(ClientState clientState) {
        // Cancel the current render task if it is still running
        try {
            if (currentRenderTask != null) {
                currentRenderTask.cancel(true);
                if (!currentRenderTask.isCancelled()) {
                    LOGGER.error("Failed to cancel the current render task");
                }
                currentRenderTask = null;
            }
            currentRenderTask = renderExecutor.submit(() -> render(clientState));
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
}
