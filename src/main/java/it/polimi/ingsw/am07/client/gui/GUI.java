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

package it.polimi.ingsw.am07.client.gui;

import it.polimi.ingsw.am07.Application;
import it.polimi.ingsw.am07.client.gui.viewController.NetworkViewController;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GUI extends javafx.application.Application {

    private Future<?> currentRenderTask;

    private final ExecutorService renderExecutor;
    private ClientState state;

    private Stage stage;

    public GUI() {
        renderExecutor = Executors.newSingleThreadExecutor();
    }
    public void entrypoint() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        //generate identifier
        String identity = UUID.randomUUID().toString();
        state = new ClientState(this::renderThread, identity);
        //initialize the stage
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/am07/views/network-view.fxml"));
        Parent root = loader.load();
        NetworkViewController controller = loader.getController();
        controller.init(state, identity);

        Scene scene = new Scene(root, 1500, 1000);
        scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("/it/polimi/ingsw/am07/css/welcome.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void render(ClientState state) {
        //Switch case to render the correct view based on the state
        PlayerState playerState = state.getPlayerState();

        switch(playerState) {
            case SELECTING_LOBBY:
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/am07/views/lobby-view.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(loader.load(), 1500, 1000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("/it/polimi/ingsw/am07/css/welcome.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
                break;
            case WAITING_FOR_PLAYERS:
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + playerState);

        }
    }

    private void renderThread(ClientState state) {
        // Cancel the current render task if it is still running
        if (currentRenderTask != null) {
            currentRenderTask.cancel(true);
            if (!currentRenderTask.isCancelled())
                throw new RuntimeException("Render task could not be cancelled");
            currentRenderTask = null;
        }
        currentRenderTask = renderExecutor.submit(() -> render(state));
    }

}
