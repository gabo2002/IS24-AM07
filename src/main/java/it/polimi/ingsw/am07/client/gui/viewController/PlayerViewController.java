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
import it.polimi.ingsw.am07.action.player.PlayerPickCardAction;
import it.polimi.ingsw.am07.action.player.PlayerPlaceCardAction;
import it.polimi.ingsw.am07.chat.ChatMessage;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class PlayerViewController {

    private static final int RECT_WIDTH = 150;
    private static final int RECT_HEIGHT = 100;
    private static final Double DELTA_X = RECT_WIDTH-(17.0/75.0*RECT_WIDTH);
    private static final Double DELTA_Y = RECT_HEIGHT-(2.0/5.0*RECT_HEIGHT);
    private final List<Rectangle> createdRectangles = new ArrayList<>();

    @FXML
    private ScrollPane scrollPane;

    @FXML
    public Button confirmDeck;

    @FXML
    private ListView<Parent> playerList;

    @FXML
    private ListView<String> itemsList;

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

    @FXML
    private Text infoMessage;

    private GameCard selectedCard;

    private ClientState clientState;
    private Controller controller;

    /**
     * Initialize the view with the client state and the controller
     * @param clientState
     * @param controller
     */
    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        updateView(clientState);

        render(clientState.getGameModel().getSelf());
    }

    /**
     * Update the view with the new client state
     * @param clientState
     */
    public void updateView(ClientState clientState) {
        this.clientState = clientState;
        System.out.println("Player view, Client state updated: " + clientState);

        List<Player> players = clientState.getGameModel().getPlayers().stream().toList();
        playerList.getItems().clear();
        for (Player player : players) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am07/views/player-box.fxml"));
                Parent player_box = fxmlLoader.load();

                player_box.setUserData(player);

                PlayerBoxController playerBoxController = fxmlLoader.getController();
                playerBoxController.setPlayer_name_box(player.getNickname());
                playerBoxController.setScore_label("Score: " + player.getPlayerScore());

                playerList.getItems().add(player_box);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            clientState.getGameModel().getSelf().getPlayerResources().getResources().keySet().forEach(symbol -> {
                itemsList.getItems().add(symbol.toString() + ": " + clientState.getGameModel().getSelf().getPlayerResources().countOf(symbol));
            });
        }catch (IllegalArgumentException e) {
            System.out.println("No resources in the player's inventory");
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

        updateInfoMessage("Sei in: " + clientState.getPlayerState());
    }

    /**
     * Handle the click on a player in the list
     * @param event
     */
    @FXML
    private void onPlayerClicked(MouseEvent event){
        Parent selectedPlayer = playerList.getSelectionModel().getSelectedItem();
        Player player = (Player) selectedPlayer.getUserData();
        clearGameField();
        render(player);
        if(player.equals(clientState.getGameModel().getSelf())) {
            updateInfoMessage("Sei in "+ clientState.getPlayerState());
            return;
        }
        updateInfoMessage("Stai guardando "+ player.getNickname());
    }

    /**
     * Handle the click on the confirm button
     * @param event
     */
    @FXML
    private void onConfirmButtonClicked(ActionEvent event) {
        if(clientState.getPlayerState() != PlayerState.PICKING_CARD) {
            updateInfoMessage("Non puoi pescare");
            return;
        }
        Action action = new PlayerPickCardAction(clientState.getNickname(), clientState.getIdentity(), selectedCard);
        controller.execute(action);
        updateInfoMessage("Sei in: " + clientState.getPlayerState());
    }

    /**
     * Handle the drag detected event
     * @param imageView
     */
    @FXML
    private void onDragDetected(ImageView imageView) {
        Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        db.setContent(content);
    }

    /**
     * Update the hand view with the new cards
     * @param handContainer
     * @param cards
     */
    private void updateHandView(HBox handContainer, List<GameCard> cards) {
        handContainer.getChildren().clear();
        for (GameCard card : cards) {
            ImageView imageView = createImageView(card, "front");
            handContainer.getChildren().add(imageView);
        }
    }

    /**
     * Update the deck view with the new cards
     * @param deckContainer
     * @param cards
     */
    private void updateDeckView(HBox deckContainer, List<GameCard> cards) {
        deckContainer.getChildren().clear();
        if (!cards.isEmpty()) {
            ImageView coverImageView = createDeckImageView(cards.getFirst(), "back");
            deckContainer.getChildren().add(coverImageView);

            for (int i = 1; i < Math.min(3, cards.size()); i++) {
                ImageView imageView = createDeckImageView(cards.get(i), "front");
                deckContainer.getChildren().add(imageView);
            }
        }
    }

    /**
     * Create an image view for the deck
     * @param card
     * @param initialSide
     * @return
     */
    private ImageView createDeckImageView(GameCard card, String initialSide) {
        Image image = imgFrom(card.id(), initialSide);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(150.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.getProperties().put("card", card);
        imageView.setViewOrder(1);
        createShadow(imageView);

        imageView.setOnMouseClicked(event -> {
            confirmDeck.setVisible(true);
            ImageView sourceImageView = (ImageView) event.getSource();
            selectedCard = (GameCard) sourceImageView.getProperties().get("card");
        });

        return imageView;
    }

    /**
     * Create an image view for the card
     * @param card
     * @param initialSide
     * @return
     */
    private ImageView createImageView(GameCard card, String initialSide) {
        Image initialImage = imgFrom(card.id(), initialSide);
        ImageView imageView = new ImageView(initialImage);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(150.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        createShadow(imageView);

        imageView.getProperties().put("card", card);
        imageView.getProperties().put("currentSide", initialSide);

        imageView.setOnMouseClicked(event -> flipCard(imageView, card.id()));

        imageView.setOnDragDetected(event -> onDragDetected(imageView));

        return imageView;
    }

    /**
     * Load the image from the assets
     * @param id
     * @param side
     * @return
     */
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

    /**
     * Flip the card
     * @param imageView
     * @param id
     */
    private void flipCard(ImageView imageView, int id) {
        String currentSide = (String) imageView.getProperties().get("currentSide");
        String newSide = currentSide.equals("front") ? "back" : "front";
        Image newImage = imgFrom(id, newSide);
        imageView.setImage(newImage);
        imageView.getProperties().put("currentSide", newSide);
    }

    /**
     * Handle the click on the card
     * @param event
     */
    private void handleCardClick(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();

        if(source.getProperties().get("RectVisibility").equals(true)) {
            source.getProperties().put("RectVisibility", false);

            Stream<Rectangle> nearRectangles = createdRectangles.stream()
                    .filter(rect -> rect.getLayoutX() == source.getLayoutX() + DELTA_X && rect.getLayoutY() == source.getLayoutY() - DELTA_Y || rect.getLayoutX() == source.getLayoutX() - DELTA_X && rect.getLayoutY() == source.getLayoutY() - DELTA_Y || rect.getLayoutX() == source.getLayoutX() + DELTA_X && rect.getLayoutY() == source.getLayoutY() + DELTA_Y || rect.getLayoutX() == source.getLayoutX() - DELTA_X && rect.getLayoutY() == source.getLayoutY() + DELTA_Y);

            nearRectangles.forEach(rect -> rect.setVisible(false));

        }else {
            source.getProperties().put("RectVisibility", true);
            double x = source.getLayoutX();
            double y = source.getLayoutY();

            GameFieldPosition topLeft = new GameFieldPosition((int) ((x - DELTA_X)/(DELTA_X)), (int) ((y + DELTA_Y)/(DELTA_Y)));
            GameFieldPosition topRight = new GameFieldPosition((int) ((x+ DELTA_X)/(DELTA_X)), (int) ((y  + DELTA_Y)/(DELTA_Y)));
            GameFieldPosition bottomLeft = new GameFieldPosition((int) ((x- DELTA_X)/(DELTA_X)), (int) ((y - DELTA_Y)/(DELTA_Y)));
            GameFieldPosition bottomRight = new GameFieldPosition((int) ((x + DELTA_X)/(DELTA_X)), (int) ((y - DELTA_Y)/(DELTA_Y)));

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
    }

    /**
     * Create a new rectangle
     * @param x
     * @param y
     */
    private void createNewRectangle(double x, double y) {
        Rectangle newRect = new Rectangle(RECT_WIDTH, RECT_HEIGHT, Color.LIGHTGRAY);
        newRect.setStroke(Color.BLACK);
        newRect.setLayoutX(x);
        newRect.setLayoutY(y);
        newRect.setViewOrder(1);

        createdRectangles.add(newRect);
        if (clientState.getPlayerState() != PlayerState.PLACING_CARD) {
            newRect.setVisible(false);
        }
        enableDragAndDrop(newRect, (int) (x / DELTA_X), (int) (y / DELTA_Y));

        rightPane.getChildren().add(newRect);

    }

    /**
     * Enable the drag and drop
     * @param rect
     * @param x
     * @param y
     */
    private void enableDragAndDrop(Rectangle rect, int x, int y) {
        List<GameCard> cards = clientState.getGameModel().getSelf().getPlayableCards();
        List<Side> sides = new ArrayList<>();

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
            if(clientState.getPlayerState() == PlayerState.PICKING_CARD) {
                updateInfoMessage("Pick a card");
                return;
            }
            if(clientState.getPlayerState() != PlayerState.PLACING_CARD) {
                updateInfoMessage("It's not your turn");
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                ImageView sourceImageView = (ImageView) event.getGestureSource();
                GameCard card = (GameCard) sourceImageView.getProperties().get("card");
                String side = (String) sourceImageView.getProperties().get("currentSide");
                success = true;

                Side sidePlacedCard;
                if (side == "front") {
                    sidePlacedCard = card.front();
                } else {
                    sidePlacedCard = card.back();
                }

                Action action = new PlayerPlaceCardAction(clientState.getGameModel().getSelfNickname(), clientState.getIdentity(), sidePlacedCard, new GameFieldPosition(x, y));
                controller.execute(action);
                render(clientState.getGameModel().getSelf());
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

    /**
     * Render the player game filed
     * @param player
     */
    private void render (Player player) {
        Map<GameFieldPosition, Side> placedCards = player.getPlacedCards();
        for (Map.Entry<GameFieldPosition, Side> entry : placedCards.entrySet()) {
            GameFieldPosition position = entry.getKey();
            Side side = entry.getValue();
            Image image;
            if (side instanceof SideBack) {
                image = imgFrom(side.id(), "back");
            }else {
                image = imgFrom(side.id(), "front");
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(RECT_HEIGHT);
            imageView.setFitWidth(RECT_WIDTH);
            imageView.setLayoutX(position.x() * DELTA_X);
            imageView.setLayoutY(position.y() * DELTA_Y);
            imageView.getProperties().put("RectVisibility", false);
            createShadow(imageView);
            if(player.equals(clientState.getGameModel().getSelf())) {
                imageView.setOnMouseClicked(this::handleCardClick);
            }
            imageView.setViewOrder(-position.z());
            System.out.println("Rendering card at position: " + position.x() + " " + position.y() + position.z());
            rightPane.getChildren().add(imageView);

            for(Rectangle rect : createdRectangles) {
                rect.setVisible(false);
            }
        }
    }

    /**
     * Create a shadow for the image view
     * @param imageView
     */
    private void createShadow(ImageView imageView) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8);
        dropShadow.setColor(Color.BLACK);
        imageView.setEffect(dropShadow);
    }

    /**
     * Clear the game field
     */
    private void clearGameField(){
        rightPane.getChildren().clear();
    }

    /**
     * Update the info message
     * @param message
     */
    private void updateInfoMessage(String message) {
        infoMessage.setText(message);
    }

    /**
     * Send the chat message
     */
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