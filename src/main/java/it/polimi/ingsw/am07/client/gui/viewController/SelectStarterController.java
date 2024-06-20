package it.polimi.ingsw.am07.client.gui.viewController;

import it.polimi.ingsw.am07.action.Action;
import it.polimi.ingsw.am07.action.player.PlayerInitialChoiceAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.reactive.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.InputStream;

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

    public void init(ClientState clientState, Controller controller) {
        this.clientState = clientState;
        this.controller = controller;

        updateview(clientState);
    }

    private void updateview(ClientState clientState) {
        GameCard starterCard = clientState.getGameModel().getSelf().getStarterCard();
        ObjectiveCard[] objectiveCards = clientState.getGameModel().getSelf().getAvailableObjectives();
        updateObjectiveCards(objectiveCards);
        updateStarterCard(starterCard);
    }

    private void updateObjectiveCards(ObjectiveCard[] objectiveCards) {
        Image img = imgFrom(objectiveCards[0].getId(), "front");
        objective1.setImage(img);
        img = imgFrom(objectiveCards[1].getId(), "front");
        objective2.setImage(img);
    }

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

    private void updateStarterCard(GameCard starterCard) {
        Image img = imgFrom(starterCard.id(), "front");
        starterImg.getProperties().put("currentSide", "front");
        starterImg.setImage(img);
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

    @FXML
    private void flipCard(MouseEvent event) {
        String currentSide = (String) starterImg.getProperties().get("currentSide");
        String newSide = currentSide.equals("front") ? "back" : "front";
        int id = getStarterCardId();
        Image newImage = imgFrom(id, newSide);
        starterImg.setImage(newImage);
        starterImg.getProperties().put("currentSide", newSide);
    }

    private int getStarterCardId() {
        return clientState.getGameModel().getSelf().getStarterCard().id();
    }

    @FXML
    private void onConfirmBtnClick(ActionEvent event) {
        Side side;

        if(starterImg.getProperties().get("currentSide").equals("back")) {
            side = clientState.getGameModel().getSelf().getStarterCard().back();
        }else {
            side = clientState.getGameModel().getSelf().getStarterCard().front();
        }

        Action action = new PlayerInitialChoiceAction(clientState.getNickname(), clientState.getIdentity(), selectedObjective, side);
        controller.execute(action);

        waiting_text.setVisible(true);
    }

    private void clearObjectiveCardBorders() {
        rect1.setVisible(false);
        rect2.setVisible(false);
    }
}
