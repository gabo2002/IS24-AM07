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
import it.polimi.ingsw.am07.action.player.PlayerInitialChoiceAction;
import it.polimi.ingsw.am07.client.gui.utils.CardImageLoader;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Controller class for handling the selection of starter card and objective cards in the GUI.
 * Manages user interaction and updates based on game state changes.
 */
public class SelectStarterController {

    private ClientState clientState;
    private Controller controller;

    @FXML
    private ImageView starterImg;

    @FXML
    private ImageView objective1;

    @FXML
    private ImageView objective2;

    @FXML
    private Button rect1;

    @FXML
    private Button rect2;

    @FXML
    private Label waiting_text;

    private ObjectiveCard selectedObjective = null;

    /**
     * Initializes the controller with the current client state and controller instance.
     * Sets the initial selected objective card and updates the view.
     *
     * @param clientState the current client state containing game information
     * @param controller  the controller instance for executing game actions
     */
    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;
        this.selectedObjective = clientState.getGameModel().getSelf().getAvailableObjectives()[0];
        this.rect1.setVisible(true);
        updateview(clientState);
    }

    /**
     * Updates the view with the provided client state.
     * Updates the objective cards and starter card displayed in the GUI.
     *
     * @param clientState the updated client state to display
     */
    private void updateview(ClientState clientState) {
        GameCard starterCard = clientState.getGameModel().getSelf().getStarterCard();
        ObjectiveCard[] objectiveCards = clientState.getGameModel().getSelf().getAvailableObjectives();
        updateObjectiveCards(objectiveCards);
        updateStarterCard(starterCard);
    }

    /**
     * Updates the displayed objective cards with the provided objective cards.
     *
     * @param objectiveCards the array of objective cards to display
     */
    private void updateObjectiveCards(ObjectiveCard[] objectiveCards) {
        Image img = imgFrom(objectiveCards[0].getId(), "front");
        objective1.setImage(img);
        img = imgFrom(objectiveCards[1].getId(), "front");
        objective2.setImage(img);
        createShadow(objective1);
        createShadow(objective2);
    }

    /**
     * Handles the click event on objective cards.
     * Updates the selected objective card and highlights the corresponding rectangle.
     *
     * @param event the mouse event triggered by clicking on an objective card
     */
    @FXML
    private void onObjectiveClick(MouseEvent event) {
        clearObjectiveCardBorders();
        if (event.getSource().equals(objective1)) {
            selectedObjective = clientState.getGameModel().getSelf().getAvailableObjectives()[0];
            rect1.setVisible(true);
        } else {
            selectedObjective = clientState.getGameModel().getSelf().getAvailableObjectives()[1];
            rect2.setVisible(true);
        }
    }

    /**
     * Updates the displayed starter card with the provided starter card.
     *
     * @param starterCard the starter card to display
     */
    private void updateStarterCard(GameCard starterCard) {
        Image img = imgFrom(starterCard.id(), "front");
        starterImg.getProperties().put("currentSide", "front");
        starterImg.setImage(img);
        createShadow(starterImg);
    }

    /**
     * Flips the starter card when clicked, toggling between front and back views.
     *
     * @param event the mouse event triggered by clicking on the starter card
     */
    @FXML
    private void flipCard(MouseEvent event) {
        String currentSide = (String) starterImg.getProperties().get("currentSide");
        String newSide = currentSide.equals("front") ? "back" : "front";
        int id = getStarterCardId();
        Image newImage = imgFrom(id, newSide);
        starterImg.setImage(newImage);
        starterImg.getProperties().put("currentSide", newSide);
    }

    /**
     * Retrieves the ID of the current starter card.
     *
     * @return the ID of the current starter card
     */
    private int getStarterCardId() {
        return clientState.getGameModel().getSelf().getStarterCard().id();
    }

    /**
     * Handles the confirm button click event.
     * Executes the player's initial choice action with the selected objective and starter card side.
     *
     * @param event the action event triggered by clicking on the confirm button
     */
    @FXML
    private void onConfirmBtnClick(ActionEvent event) {
        Side side;
        if (starterImg.getProperties().get("currentSide").equals("back")) {
            side = clientState.getGameModel().getSelf().getStarterCard().back();
        } else {
            side = clientState.getGameModel().getSelf().getStarterCard().front();
        }
        Action action = new PlayerInitialChoiceAction(clientState.getNickname(), clientState.getIdentity(), selectedObjective, side);
        controller.execute(action);
        waiting_text.setVisible(true);
    }

    /**
     * Clears the selection border on the objective cards.
     */
    private void clearObjectiveCardBorders() {
        rect1.setVisible(false);
        rect2.setVisible(false);
    }

    /**
     * Creates a drop shadow effect for the provided ImageView.
     *
     * @param imageView the ImageView to apply the drop shadow effect
     */
    private void createShadow(ImageView imageView) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8);
        dropShadow.setColor(Color.BLACK);
        imageView.setEffect(dropShadow);
    }

    /**
     * Loads an Image from resources based on the provided card ID and side.
     *
     * @param id   the ID of the card
     * @param side the side of the card ("front" or "back")
     * @return the loaded Image object
     */
    private Image imgFrom(int id, String side) {
        ClassLoader cl = this.getClass().getClassLoader();
        return CardImageLoader.imgFrom(cl, id, side);
    }
}
