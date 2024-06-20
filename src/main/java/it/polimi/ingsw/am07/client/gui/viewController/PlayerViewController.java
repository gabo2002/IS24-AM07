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

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.chat.SendMessageAction;
import it.polimi.ingsw.am07.action.player.PlayerInitialChoiceAction;
import it.polimi.ingsw.am07.action.player.PlayerPlaceCardAction;
import it.polimi.ingsw.am07.chat.ChatMessage;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PlayerViewController {

    private static final int RECT_WIDTH = 150;
    private static final int RECT_HEIGHT = 100;
    private static final Double DELTA_X = RECT_WIDTH - (17.0 / 75.0 * RECT_WIDTH);
    private static final Double DELTA_Y = RECT_HEIGHT - (2.0 / 5.0 * RECT_HEIGHT);
    private static final int deafaultLayoutX = 100;
    private static final int deafaultLayoutY = 300;
    private final List<Rectangle> createdRectangles = new ArrayList<>();
    @FXML
    Rectangle defaultRectangle;
    private boolean areRectanglesVisible = false;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView card3;
    @FXML
    private ImageView card4;
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
    @FXML
    private Pane rightPane;
    private ClientState clientState;
    private Controller controller;

    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;
        Map<GameFieldPosition, Side> getPlacedCards = clientState.getGameModel().getSelf().getPlacedCards();
        showStartCardPopup();

        // Bind the label to reflect the player state changes
        updateView(clientState);
        render(getPlacedCards);
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

        enableDragAndDrop(defaultRectangle, 0, 0);
        //defaultRectangle.setOnMouseClicked(this::handleCardClick);
    }

    private void showStartCardPopup() {
        GameCard startCard = clientState.getGameModel().getSelf().getStarterCard();

        //ObjectiveCard[] objectiveCards = clientState.getGameModel().getSelf().getAvailableObjectives();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Choose Your Start Card");

        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(10));

        ImageView cardImageView = createImageView(startCard, "front");

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            String chosenSide = (String) cardImageView.getProperties().get("currentSide");
            Side cardSide = "front".equals(chosenSide) ? startCard.front() : startCard.back();
            Action action = new PlayerInitialChoiceAction(clientState.getGameModel().getSelfNickname(), clientState.getIdentity(), null, cardSide);
            controller.execute(action);
            popupStage.close();
        });

        popupContent.getChildren().addAll(cardImageView, confirmButton);
        Scene popupScene = new Scene(popupContent, 300, 400);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();

    }

    @FXML
    private void onDragDetected(ImageView imageView) {
        Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        db.setContent(content);
    }


    private void updateHandView(HBox handContainer, List<GameCard> cards) {
        handContainer.getChildren().clear();
        for (GameCard card : cards) {
            ImageView imageView = createImageView(card, "front");
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
        String item = "it/polimi/ingsw/am07/assets/" + side + "_" + id + ".png";
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
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(150.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    private ImageView createImageView(GameCard card, String initialSide) {
        Image initialImage = imgFrom(card.id(), initialSide);
        ImageView imageView = new ImageView(initialImage);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(150.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        imageView.getProperties().put("card", card);
        imageView.getProperties().put("currentSide", initialSide);

        imageView.setOnMouseClicked(event -> flipCard(imageView, card.id()));

        imageView.setOnDragDetected(event -> onDragDetected(imageView));

        return imageView;
    }


    private void flipCard(ImageView imageView, int id) {
        String currentSide = (String) imageView.getProperties().get("currentSide");
        String newSide = currentSide.equals("front") ? "back" : "front";
        Image newImage = imgFrom(id, newSide);
        imageView.setImage(newImage);
        imageView.getProperties().put("currentSide", newSide);
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

    private void handleCardClick(MouseEvent event) {
        if (!(event.getSource() instanceof ImageView)) {
            System.out.println((event.getSource()));
            return;
        }

        ImageView source = (ImageView) event.getSource();
        double x = source.getLayoutX();
        double y = source.getLayoutY();

        GameFieldPosition topLeft = new GameFieldPosition((int) ((x - DELTA_X) / (DELTA_X)), (int) ((y + DELTA_Y) / (DELTA_Y)));
        GameFieldPosition topRight = new GameFieldPosition((int) ((x + DELTA_X) / (DELTA_X)), (int) ((y + DELTA_Y) / (DELTA_Y)));
        GameFieldPosition bottomLeft = new GameFieldPosition((int) ((x - DELTA_X) / (DELTA_X)), (int) ((y - DELTA_Y) / (DELTA_Y)));
        GameFieldPosition bottomRight = new GameFieldPosition((int) ((x + DELTA_X) / (DELTA_X)), (int) ((y - DELTA_Y) / (DELTA_Y)));

        Side side = new SideBack(0, null, null, Symbol.GREEN);


        if (clientState.getGameModel().getSelf().canBePlacedAt(side, topLeft)) {
            createNewRectangle(x - DELTA_X, y + DELTA_Y);
        }

        if (clientState.getGameModel().getSelf().canBePlacedAt(side, topRight)) {
            createNewRectangle(x + DELTA_X, y + DELTA_Y);
        }

        if (clientState.getGameModel().getSelf().canBePlacedAt(side, bottomLeft)) {
            createNewRectangle(x - DELTA_X, y - DELTA_Y);
        }

        if (clientState.getGameModel().getSelf().canBePlacedAt(side, bottomRight)) {
            createNewRectangle(x + DELTA_X, y - DELTA_Y);
        }
    }

    private void createNewRectangle(double x, double y) {
        Rectangle newRect = new Rectangle(RECT_WIDTH, RECT_HEIGHT, Color.LIGHTGRAY);
        newRect.setStroke(Color.BLACK);
        newRect.setLayoutX(x);
        newRect.setLayoutY(y);

        createdRectangles.add(newRect);

        // Abilita il drag-and-drop sul nuovo rettangolo
        enableDragAndDrop(newRect, (int) (x / DELTA_X), (int) (y / DELTA_Y));

        // Aggiungi il nuovo rettangolo al Pane destro
        rightPane.getChildren().add(newRect);
    }

    private void enableDragAndDrop(Rectangle rect, int x, int y) {
        List<GameCard> cards = clientState.getGameModel().getSelf().getPlayableCards();
        List<Side> sides = new ArrayList<>();
        Map<GameFieldPosition, Side> getPlacedCards = clientState.getGameModel().getSelf().getPlacedCards();

        for (GameCard card : cards) {
            sides.add(card.front());
            sides.add(card.back());
        }
        rect.setOnDragOver(event -> {
            if (event.getGestureSource() != rightPane && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        rect.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                ImageView sourceImageView = (ImageView) event.getGestureSource();
                GameCard card = (GameCard) sourceImageView.getProperties().get("card");
                String side = (String) sourceImageView.getProperties().get("currentSide");
                ImageView imageView = new ImageView(db.getImage());
                imageView.setFitHeight(RECT_HEIGHT);
                imageView.setFitWidth(RECT_WIDTH);
                imageView.setLayoutX(rect.getLayoutX());
                imageView.setLayoutY(rect.getLayoutY());
                imageView.setOnMouseClicked(this::handleCardClick);
                rightPane.getChildren().add(imageView);
                success = true;

                Side sidePlacedCard;
                if (side == "front") {
                    sidePlacedCard = card.front();
                } else {
                    sidePlacedCard = card.back();
                }

                Action action = new PlayerPlaceCardAction(clientState.getGameModel().getSelfNickname(), clientState.getIdentity(), sidePlacedCard, new GameFieldPosition(x, y));
                controller.execute(action);
                render(getPlacedCards);
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

    private void render(Map<GameFieldPosition, Side> placedCards) {
        for (Map.Entry<GameFieldPosition, Side> entry : placedCards.entrySet()) {
            GameFieldPosition position = entry.getKey();
            Side side = entry.getValue();
            Image image;
            if (side instanceof SideBack) {
                image = imgFrom(side.id(), "back");
            } else {
                image = imgFrom(side.id(), "front");
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(RECT_HEIGHT);
            imageView.setFitWidth(RECT_WIDTH);
            imageView.setLayoutX(position.x() * DELTA_X);
            imageView.setLayoutY(position.y() * DELTA_Y);
            imageView.setOnMouseClicked(this::handleCardClick);
            rightPane.getChildren().add(imageView);

            for (Rectangle rect : createdRectangles) {
                rect.setVisible(false);
            }
        }
    }
}