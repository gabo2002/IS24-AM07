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
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class GUI extends javafx.application.Application {

    private final Object lock = new Object();
    private ClientState state;
    private Stage stage;
    private boolean shouldRender = false;
    private ClientNetworkManager clientNetworkManager;

    public GUI() {
    }

    public void entrypoint() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        //generate identifier
        String identity = UUID.randomUUID().toString();

        ClientNetworkManager.Factory networkManager = new ClientNetworkManager.Factory();

        state = new ClientState(this::notifyRenderThread, identity);
        //initialize the stage
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/am07/views/network-view.fxml"));
        Parent root = loader.load();
        NetworkViewController networkViewController = loader.getController();
        networkViewController.init(state, identity, networkManager);

        Scene scene = new Scene(root, 1500, 1000);
        scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("/it/polimi/ingsw/am07/css/welcome.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> renderLoop()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void renderLoop() {
        boolean rerender;

        synchronized (lock) {
            rerender = shouldRender;
            shouldRender = false;
        }

        if (rerender) {
            Platform.runLater(() -> render(state));
        }
    }

    public void render(ClientState state) {
        //Switch case to render the correct view based on the state
        PlayerState playerState = state.getPlayerState();

        FXMLLoader loader;
        Scene scene = null;

        switch (playerState) {
            case SELECTING_LOBBY:
                loader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/am07/views/welcome-view.fxml"));
                try {
                    scene = new Scene(loader.load(), 1500, 1000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("/it/polimi/ingsw/am07/css/welcome.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
                break;
            case INSERTING_USERNAME:
                loader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/am07/views/username-view.fxml"));
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
                loader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/am07/views/lobby-view.fxml"));
                try {
                    scene = new Scene(loader.load(), 1500, 1000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("/it/polimi/ingsw/am07/css/welcome.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
                break;
            case ADMIN_WAITING_FOR_PLAYERS:
                loader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/am07/views/lobby-view.fxml"));
                try {
                    scene = new Scene(loader.load(), 1500, 1000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("/it/polimi/ingsw/am07/css/welcome.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + playerState);
        }
    }

    private void notifyRenderThread(ClientState state) {
        synchronized (lock) {
            shouldRender = true;
        }
    }

}
