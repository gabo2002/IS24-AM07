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
import it.polimi.ingsw.am07.client.gui.utils.CardImageLoader;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.chat.ChatMessage;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.Symbol;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.reactive.Controller;
import it.polimi.ingsw.am07.utils.matrix.Matrix;
import it.polimi.ingsw.am07.utils.logging.AppLogger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.util.*;


public class PlayerViewController {

    private static final int RECT_WIDTH = 150;
    private static final int RECT_HEIGHT = 100;
    private static final Double DELTA_X = RECT_WIDTH - (17.0 / 75.0 * RECT_WIDTH);
    private static final Double DELTA_Y = RECT_HEIGHT - (2.0 / 5.0 * RECT_HEIGHT);
    private final AppLogger LOGGER = new AppLogger(PlayerViewController.class);
    private final List<Rectangle> createdRectangles = new ArrayList<>();
    private static final int defaultX = 4698;
    private static final int defaultY = 2430;

    @FXML
    public HBox objectiveCardsContainer;
    @FXML
    public Button confirmDeck;
    @FXML
    public HBox errorContainer;

    @FXML
    private ListView<Parent> playerList;

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

    @FXML
    private Label green;

    @FXML
    private Label red;

    @FXML
    private Label blue;
    @FXML
    private Label feather;
    @FXML
    private Label scroll;
    @FXML
    private Label flask;
    @FXML
    private Label purple;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ScrollPane scrollLeft;

    @FXML
    private VBox contentBox;

    private GameCard selectedCard;

    private ClientState clientState;
    private Controller controller;

    /**
     * Initialize the view with the client state and the controller
     *
     * @param clientState the client state
     * @param controller  the controller
     */
    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        updateView(clientState);

        render(clientState.getGameModel().getSelf());
    }

    /**
     * Update the view with the new client state
     *
     * @param clientState a reference to the client state
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
                LOGGER.error(e);
            }
        }

        green.setText(String.valueOf(clientState.getGameModel().getSelf().getPlayerResources().countOf(Symbol.GREEN)));
        red.setText(String.valueOf(clientState.getGameModel().getSelf().getPlayerResources().countOf(Symbol.RED)));
        blue.setText(String.valueOf(clientState.getGameModel().getSelf().getPlayerResources().countOf(Symbol.BLUE)));
        feather.setText(String.valueOf(clientState.getGameModel().getSelf().getPlayerResources().countOf(Symbol.FEATHER)));
        flask.setText(String.valueOf(clientState.getGameModel().getSelf().getPlayerResources().countOf(Symbol.FLASK)));
        purple.setText(String.valueOf(clientState.getGameModel().getSelf().getPlayerResources().countOf(Symbol.PURPLE)));
        scroll.setText(String.valueOf(clientState.getGameModel().getSelf().getPlayerResources().countOf(Symbol.SCROLL)));

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
        GameCard resourceCard = deck.pickRandomResCard();
        GameCard[] resourceCards = deck.visibleResCards();
        GameCard goldCard = deck.pickRandomGoldCard();
        GameCard[] goldCards = deck.visibleGoldCards();

        updateDeckView(resourceDeckContainer, resourceCard, resourceCards);
        updateDeckView(goldDeckContainer, goldCard, goldCards);
        updateObjectivesView(objectiveCardsContainer, Arrays.stream(clientState.getGameModel().getCommonObjectives()).toList());

        List<GameCard> hand = clientState.getGameModel().getSelf().getPlayableCards();

        updateHandView(playerHand, hand);

        updateInfoMessage("It's your turn");

        if (clientState.getGameModel().getPlayingPlayer() != clientState.getGameModel().getSelf()) {
            updateInfoMessage("It's " + clientState.getGameModel().getPlayingPlayer().getNickname() + "'s turn");
        }


        Platform.runLater(() -> {
            contentBox.layout();
            scrollLeft.setVvalue(0.0);

            Platform.runLater(() -> scrollLeft.setVvalue(0.0));
        });
    }

    /**
     * Handle the click on a player in the list
     *
     * @param event
     */
    @FXML
    private void onPlayerClicked(MouseEvent event) {
        Parent selectedPlayer = playerList.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            Player player = (Player) selectedPlayer.getUserData();
            clearGameField();
            render(player);
            if (player.equals(clientState.getGameModel().getSelf()) && clientState.getPlayerState() == PlayerState.PLACING_CARD) {
                updateInfoMessage("It's your turn");
                errorContainer.setBackground(Background.fill(Color.rgb(249, 222, 201)));
                return;
            }

            if (player.equals(clientState.getGameModel().getSelf())) {
                updateInfoMessage("It's " + clientState.getGameModel().getPlayingPlayer().getNickname() + "'s turn");
                errorContainer.setBackground(Background.fill(Color.rgb(249, 222, 201)));
                return;
            }

            updateInfoMessage("You are watching " + player.getNickname());
            errorContainer.setBackground(Background.fill(Color.rgb(249, 222, 201)));
        }
    }

    /**
     * Handle the click on the confirm button
     *
     * @param event the event
     */
    @FXML
    private void onConfirmButtonClicked(ActionEvent event) {
        if (clientState.getPlayerState() != PlayerState.PICKING_CARD && clientState.getPlayerState() != PlayerState.PLACING_CARD) {
            updateInfoMessage("It's not your turn");
            errorContainer.setBackground(Background.fill(Color.rgb(240, 128, 128)));
            return;
        }

        if (clientState.getPlayerState() != PlayerState.PICKING_CARD) {
            updateInfoMessage("You have to place a card first");
            errorContainer.setBackground(Background.fill(Color.rgb(240, 128, 128)));
            return;
        }

        Action action = new PlayerPickCardAction(clientState.getNickname(), clientState.getIdentity(), selectedCard);
        controller.execute(action);
        updateInfoMessage("It's " + clientState.getGameModel().getPlayingPlayer().getNickname() + "'s turn");
    }

    /**
     * Handle the drag detected event
     *
     * @param imageView the image view for placing the card
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
     *
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
     *
     * @param deckContainer the container of the deck
     * @param backCard      the back card of the hidden deck
     * @param visibleCards  the visible cards of the deck
     */
    private void updateDeckView(HBox deckContainer, GameCard backCard, GameCard[] visibleCards) {
        deckContainer.getChildren().clear();
        if (backCard != null) {
            ImageView coverImageView = createDeckImageView(backCard, "back");
            deckContainer.getChildren().add(coverImageView);
        }

        for (GameCard card : visibleCards) {
            if (card == null) {
                continue;
            }

            ImageView imageView = createDeckImageView(card, "front");
            deckContainer.getChildren().add(imageView);
        }
    }

    private void updateObjectivesView(HBox objectiveCardsContainer, List<ObjectiveCard> cards) {
        objectiveCardsContainer.getChildren().clear();
        for (ObjectiveCard card : cards) {
            ImageView imageView = createImageView(card, "front");
            objectiveCardsContainer.getChildren().add(imageView);
        }
        ObjectiveCard myObjective = clientState.getGameModel().getSelf().getPlayerObjectiveCard();
        ImageView imageView = createImageView(myObjective, "front");
        objectiveCardsContainer.getChildren().add(imageView);
    }

    /**
     * Create an image view for the deck
     *
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
        createShadow(imageView, Color.BLACK);

        imageView.setOnMouseClicked(event -> {
            confirmDeck.setVisible(true);
            ImageView sourceImageView = (ImageView) event.getSource();
            selectedCard = (GameCard) sourceImageView.getProperties().get("card");
            for (ImageView iv : resourceDeckContainer.getChildren().stream().map(node -> (ImageView) node).toList()) {
                createShadow(iv, Color.BLACK);
            }
            for (ImageView iv : goldDeckContainer.getChildren().stream().map(node -> (ImageView) node).toList()) {
                createShadow(iv, Color.BLACK);
            }
            createShadow(sourceImageView, Color.GREEN);
        });

        return imageView;
    }

    /**
     * Create an image view for the card
     *
     * @param card
     * @param initialSide
     * @return
     */

    private ImageView createImageView(ObjectiveCard card, String initialSide) {
        Image initialImage = imgFrom(card.getId(), initialSide);
        ImageView imageView = new ImageView(initialImage);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(150);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        createShadow(imageView, Color.BLACK);

        imageView.getProperties().put("card", card);
        imageView.getProperties().put("currentSide", initialSide);

        return imageView;
    }


    private ImageView createImageView(GameCard card, String initialSide) {
        Image initialImage = imgFrom(card.id(), initialSide);
        ImageView imageView = new ImageView(initialImage);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(150.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        createShadow(imageView, Color.BLACK);

        imageView.getProperties().put("card", card);
        imageView.getProperties().put("currentSide", initialSide);

        imageView.setOnMouseClicked(event -> flipCard(imageView, card.id()));

        imageView.setOnDragDetected(event -> onDragDetected(imageView));

        return imageView;
    }

    /**
     * Load the image from the assets
     *
     * @param id
     * @param side
     * @return
     */
    private Image imgFrom(int id, String side) {
        ClassLoader cl = this.getClass().getClassLoader();
        return CardImageLoader.imgFrom(cl, id, side);
    }

    /**
     * Flip the card
     *
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
     *
     * @param event
     */
    private void handleCardClick(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();

        createdRectangles.forEach(rect -> rect.setVisible(false));

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

    /**
     * Create a new rectangle
     *
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
     *
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
            if (clientState.getPlayerState() == PlayerState.PICKING_CARD) {
                updateInfoMessage("Pick a card");
                errorContainer.setBackground(Background.fill(Color.rgb(240, 128, 128)));
                return;
            }
            if (clientState.getPlayerState() != PlayerState.PLACING_CARD) {
                updateInfoMessage("It's not your turn");
                errorContainer.setBackground(Background.fill(Color.rgb(240, 128, 128)));
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

                if(clientState.getClientStringErrorMessage() != null) {
                    updateInfoMessage("Not enough resources");
                    errorContainer.setBackground(Background.fill(Color.rgb(240, 128, 128)));
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

    /**
     * Render the player game filed
     *
     * @param player
     */
    private void render(Player player) {
        Map<GameFieldPosition, Side> placedCards = new HashMap<>(player.getPlacedCards());
        Matrix<Symbol> myMatrix = player.getPlayerGameField().getFieldMatrix();

        int minX = myMatrix.getMinX();
        int minY = myMatrix.getMinY();
        int maxX = myMatrix.getMaxX();
        int maxY = myMatrix.getMaxY();

        double posX = (maxX+minX)/2.0*DELTA_X + defaultX + (RECT_WIDTH-DELTA_X);
        double posY = (maxY+minY)/2.0*DELTA_Y + defaultY + (RECT_HEIGHT-DELTA_Y);

        scrollPane.setHvalue(posX/ 9396);
        scrollPane.setVvalue(posY/ 4860);

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
            createShadow(imageView, Color.BLACK);
            if (player.equals(clientState.getGameModel().getSelf())) {
                imageView.setOnMouseClicked(this::handleCardClick);
            }
            imageView.setViewOrder(-position.z());
            rightPane.getChildren().add(imageView);

            for (Rectangle rect : createdRectangles) {
                rect.setVisible(false);
            }
        }
    }

    /**
     * Create a shadow for the image view
     *
     * @param imageView
     */
    private void createShadow(ImageView imageView, Color color) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8);
        dropShadow.setColor(color);
        imageView.setEffect(dropShadow);
    }

    /**
     * Clear the game field
     */
    private void clearGameField() {
        rightPane.getChildren().clear();
    }

    /**
     * Update the info message
     *
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

        ChatMessage msg = clientState.getGameModel().getSelf().getChat().sendBroadcastMessage(message);

        SendMessageAction action = new SendMessageAction(clientState.getNickname(), clientState.getIdentity(), msg);

        controller.execute(action);
    }


}