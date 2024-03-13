package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.model.game.gamefield.GameField;
import it.polimi.ingsw.am07.model.game.gamefield.GameFieldPosition;
import it.polimi.ingsw.am07.model.game.side.Side;
import it.polimi.ingsw.am07.model.game.side.SideBack;
import it.polimi.ingsw.am07.model.game.side.SideFrontGold;
import it.polimi.ingsw.am07.model.game.side.SideFrontRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Player {

    private final String nickname;

    private final ResourceHolder playerResources;

    private int playerScore;

    private final Pawn playerPawn;

    private final GameField playerGameField;

    private final ArrayList<GameCard> playableCards;    //handCards?

    private ObjectiveCard playerObjectiveCard;

    /**
     * Constructs a new Player with the specified parameters.
     */
    public Player(String nickname, ResourceHolder playerResources, Pawn playerPawn, GameField playerGameField, ArrayList<GameCard> playableCards){
        this.nickname = nickname;
        this.playerResources = playerResources;
        this.playerPawn = playerPawn;
        this.playerGameField = playerGameField;
        this.playableCards = playableCards;
    }


    /**
     * Retrieves the nickname of the player.
     *
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Retrieves the score of the player.
     *
     * @return The score of the player.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Retrieves the pawn associated with the player.
     *
     * @return The pawn associated with the player.
     */
    public Pawn getPlayerPawn() {
        return playerPawn;
    }

    /**
     * Retrieves the objective card of the player.
     *
     * @return The objective card of the player.
     */
    public ObjectiveCard getPlayerObjectiveCard() {
        return playerObjectiveCard;
    }

    /**
     * Sets the objective card for the player.
     *
     * @param playerObjectiveCard The objective card to be set for the player.
     */
    public void setPlayerObjectiveCard(ObjectiveCard playerObjectiveCard) {
        this.playerObjectiveCard = playerObjectiveCard;
    }

    /**
     * Retrieves the resource holder associated with the player.
     *
     * @return The resource holder associated with the player.
     */
    public ResourceHolder getPlayerResources() {
        return playerResources;
    }

    /**
     * Checks if a card can be placed at the specified position on the game field.
     *
     * @param card The card to be placed.
     * @param pos  The position on the game field where the card is to be placed.
     * @return true if the card can be placed at the specified position, false otherwise.
     * @author Omar Chaabani
     */
    public boolean canBePlacedAt(Side card, GameFieldPosition pos) {

        switch (card){
            case SideFrontGold  sideFrontGold -> {
                return playerResources.contains(sideFrontGold.requirements()) && playerGameField.canBePlacedOnFieldAt(sideFrontGold, pos);
            }
            default -> {
                return playerGameField.canBePlacedOnFieldAt(card, pos);
            }
        }

    }

    /**
     * Places a card on the game field at the specified position.
     *
     * @param card The card to be placed.
     * @param pos  The position on the game field where the card is to be placed.
     */
    public void placeAt(Side card, GameFieldPosition pos) {
        playerGameField.placeOnFieldAt(card, pos);
    }

    /**
     * This class represents a game field where cards can be placed and manipulated.
     * It provides methods to check if a card can be placed at a specific position on the game field.
     *
     * @author Omar Chaabani
     */
    public HashMap<Side, GameFieldPosition> getPlacedCards() {
        return playerGameField.getPlacedCards();
    }

    /**
     * Retrieves the list of playable cards.
     *
     * @return An ArrayList containing the playable cards.
     * @author Omar Chaabani
     */
    public ArrayList<GameCard> getPlayableCards() {
        return playableCards;
    }

    /**
     * Adds a playable card to the list of playable cards.
     *
     * @param card The playable card to be added.
     * @author Omar Chaabani
     */
    public void addPlayableCard(GameCard card) {
        playableCards.add(card);
    }
}
