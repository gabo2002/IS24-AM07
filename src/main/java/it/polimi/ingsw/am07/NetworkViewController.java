package it.polimi.ingsw.am07;

import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.network.ClientNetworkManager;
import it.polimi.ingsw.am07.network.NetworkType;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.assets.AssetsRegistry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class NetworkViewController {

    private Controller controller;
    private ClientState clientState;
    private String identity;

    @FXML
    protected void onRMIBtnClicked(ActionEvent event) {
        initializeClientState(NetworkType.RMI);
        loadScene(event);
    }

    @FXML
    protected void onTCPBtnClicked(ActionEvent event) {
        initializeClientState(NetworkType.TCP);
        loadScene(event);
    }

    private void initializeClientState(NetworkType networkType) {
        identity = UUID.randomUUID().toString();
        clientState = new ClientState(this::updateView, identity);

        ClientNetworkManager networkManager = new ClientNetworkManager.Factory()
                .withHostname("localhost")
                .withPort(networkType == NetworkType.RMI ? AssetsRegistry.getInstance().getGameResourceDefinition().rmiPort() : AssetsRegistry.getInstance().getGameResourceDefinition().tcpPort())
                .withNetworkType(networkType)
                .withIdentity(identity)
                .withState(clientState)
                .build();

        controller = networkManager.getController();
    }

    private void loadScene(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/welcome-view.fxml"));
            Parent root = fxmlLoader.load();

            // Obtain the controller for the new view
            WelcomeViewController view_controller = fxmlLoader.getController();
            view_controller.init(clientState, controller);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root, 1500, 1000);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateView(ClientState clientState) {
        // Logic to update the initial view based on clientState
        System.out.println("Network view, Client state updated: " + clientState);
    }
}
