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

package it.polimi.ingsw.am07.client.gui.viewController;

import it.polimi.ingsw.am07.action.chat.SendMessageAction;
import it.polimi.ingsw.am07.chat.ChatMessage;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayerViewController {

    @FXML
    private ListView<String> playerList;

    @FXML
    private Label scoreLabel;

    @FXML
    private TextField chatInput;

    @FXML
    private TextArea chatArea;

    @FXML
    private HBox resourceDeckContainer;

    @FXML
    private HBox goldDeckContainer;

    @FXML
    private HBox playerHand;

    private ClientState clientState;
    private Controller controller;

    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        // Bind the label to reflect the player state changes
        updateView(clientState);
        // clientState.onGameModelUpdate(this::updateView);
    }

    public void updateView(ClientState clientState) {
        this.clientState = clientState;
        System.out.println("Player view, Client state updated: " + clientState);

        List<String> players = clientState.getGameModel().getPlayers().stream().map(Player::getNickname).toList();
        playerList.getItems().clear();
        playerList.getItems().addAll(players);

        if (clientState.getGameModel().getSelf() != null) {
            scoreLabel.setText("Score: " + clientState.getGameModel().getSelf().getPlayerScore());
        }

        // Retrieve the last 20 messages
        List<ChatMessage> messages = clientState.getGameModel().getSelf().getChat().getMessages();
        List<String> messageRepresentation = new ArrayList<>(messages.size());
        for (ChatMessage message : messages) {
            DateFormat dateFormat = DateFormat.getTimeInstance();
            messageRepresentation.add("[" + dateFormat.format(message.timestamp()) + "] " + message.senderNickname() + ": " + message.message());
        }
        chatArea.clear();
        chatArea.setText(String.join("\n", messageRepresentation));

        Deck deck = clientState.getGameModel().getDeck();
        List<GameCard> resourceCards = deck.availableResCards();
        List<GameCard> goldCards = deck.availableGoldCards();

        updateDeckView(resourceDeckContainer, resourceCards);
        updateDeckView(goldDeckContainer, goldCards);

        List<GameCard> hand = clientState.getGameModel().getSelf().getPlayableCards();

        updateHandView(playerHand, hand);
    }

    private void updateHandView(HBox handContainer, List<GameCard> cards) {
        handContainer.getChildren().clear();
        for (GameCard card : cards) {
            ImageView imageView = createImageView(card.id(), "front");
            handContainer.getChildren().add(imageView);
        }
    }

    private void updateDeckView(HBox deckContainer, List<GameCard> cards) {
        deckContainer.getChildren().clear();
        Image coverImage = imgFrom(cards.getFirst().id(), "back");
        if (!cards.isEmpty()) {
            ImageView coverImageView = createImageView(coverImage);
            deckContainer.getChildren().add(coverImageView);

            for (int i = 1; i < Math.min(3, cards.size()); i++) {
                Image cardImage = imgFrom(cards.get(i).id(), "front");
                ImageView imageView = createImageView(cardImage);
                deckContainer.getChildren().add(imageView);
            }
        }
    }

    private Image imgFrom(int id, String side) {
        ClassLoader cl = this.getClass().getClassLoader();
        String item = "it/polimi/ingsw/am07/assets/"+side+"_" + id + ".png";
        Image img = null;
        try (InputStream is = cl.getResourceAsStream(item)) {
            if (is != null) {
                img = new Image(is);
            } else {
                System.err.println("Unable to load image: " + item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(200.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        return imageView;
    }
    private ImageView createImageView(int id, String initialSide) {
        Image initialImage = imgFrom(id, initialSide);
        ImageView imageView = new ImageView(initialImage);
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(200.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        imageView.getProperties().put("currentSide", initialSide);

        imageView.setOnMouseClicked(event -> {
            String currentSide = (String) imageView.getProperties().get("currentSide");
            String newSide = currentSide.equals("front") ? "back" : "front";
            Image newImage = imgFrom(id, newSide);
            imageView.setImage(newImage);
            imageView.getProperties().put("currentSide", newSide);
        });

        return imageView;
    }

    @FXML
    protected void sendChatMessage() {
        String message = chatInput.getText();
        List<String> recipients = playerList.getSelectionModel().getSelectedItems().stream().map(
                nickname -> nickname.toString()
        ).toList();

        if (recipients.isEmpty()) {
            recipients = clientState.getGameModel().getPlayers().stream().map(
                    Player::getNickname
            ).toList();
        }

        ChatMessage msg = new ChatMessage(clientState.getGameModel().getSelf().getNickname(), recipients, message);

        SendMessageAction action = new SendMessageAction(clientState.getNickname(), clientState.getIdentity(), msg);

        controller.execute(action);
    }

}
