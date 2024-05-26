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

package it.polimi.ingsw.am07.model.game;

import it.polimi.ingsw.am07.exceptions.CardNotFoundException;
import it.polimi.ingsw.am07.exceptions.IllegalGameStateException;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.game.card.ObjectiveCard;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.lobby.LobbyPlayer;
import it.polimi.ingsw.am07.utils.assets.GameResources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game implements Serializable {

    public static int MAX_PLAYERS = 4;

    private final UUID id;

    private final List<Player> players;

    private final Deck deck;
    private final ObjectiveCard[] commonObjectives; //only 2 cards
    private String selfNickname;
    private int currentPlayerIndex;    //Reminder: randomly generated first

    private GameState gameState;

    /**
     * Constructor for the Game class, called by Lobby Class.
     * <strong>SHOULD NEVER BE CALLED IF NOT BY THE COMPANION FACTORY!</strong>
     *
     * @param players          the list of players in the game
     * @param commonObjectives the common objectives in the game
     */
    public Game(List<Player> players, ObjectiveCard[] commonObjectives) {
        this.commonObjectives = commonObjectives;
        this.players = players;
        this.selfNickname = null;

        id = UUID.randomUUID();
        currentPlayerIndex = 0;
        deck = new Deck.Factory().build();
        gameState = GameState.STARTING;
    }

    /**
     * Constructor for the Game class, which creates a shallow copy of the game object.
     *
     * @param other the game object to be copied
     */
    public Game(Game other) {
        this.id = other.id;
        this.players = new ArrayList<>(other.players);
        this.deck = new Deck.Factory()
                .goldCards(other.deck.availableGoldCards())
                .resourceCards(other.deck.availableResCards())
                .visibleGoldCards(other.deck.visibleGoldCards())
                .visibleResCards(other.deck.visibleResCards())
                .build();
        this.selfNickname = other.selfNickname;
        this.commonObjectives = other.commonObjectives;
        this.currentPlayerIndex = other.currentPlayerIndex;
        this.gameState = other.gameState;
    }

    /**
     * This method returns the unique identifier of the game.
     *
     * @return the game's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * This method returns the list of players in the game.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * This method returns the nickname of the current player.
     *
     * @return the nickname of the current player
     */
    public String getSelfNickname() {
        return selfNickname;
    }

    /**
     * This method sets the nickname of the current player.
     * <strong>SHOULD NEVER BE CALLED IF NOT BY THE COMPANION FACTORY!</strong>
     * <p>
     * This method is used to set the nickname of the player associated with the current client.
     * It is called by the companion factory class.
     * <strong>SHOULD NEVER BE CALLED IF NOT BY THE COMPANION FACTORY!</strong>
     * <p>
     */
    public void setSelfNickname(String selfNickname) {
        this.selfNickname = selfNickname;
    }

    /**
     * This method returns the common objectives in the game.
     *
     * @return an array of common objective cards
     */
    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * This method increments the current player index, effectively moving the turn to the next player.
     * It uses modulo operation to ensure that the player index wraps around to the first player after the last one.
     * Checks the current game state and updates it accordingly.
     */
    public void incrementTurn() {
        if (gameState == GameState.STARTING) {
            gameState = GameState.PLAYING;
        }

        // continue incrementing as long as it is not ended
        if (gameState != GameState.ENDED) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        if (gameState == GameState.ENDING && (currentPlayerIndex == 0 || getPlayingPlayer().getPlayerPawn().equals(Pawn.BLACK))) {
            gameState = GameState.ENDED;
        }

        if (gameState == GameState.PLAYING && (currentPlayerIndex == 0 || getPlayingPlayer().getPlayerPawn().equals(Pawn.BLACK))) {
            for (Player player : players) {
                if (player.getPlayerScore() >= 20) {
                    gameState = GameState.ENDING;
                    return;
                }
            }
        }
    }

    /**
     * This method returns the index of the current player in the game.
     *
     * @return the index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * This method picks a random resource card from the deck.
     *
     * @return a random resource card from the deck
     */
    public GameCard pickRandomResCard() {
        return deck.pickRandomResCard();
    }

    /**
     * This method picks a random gold card from the deck.
     *
     * @return a random gold card from the deck
     */

    public GameCard pickRandomGoldCard() {
        return deck.pickRandomGoldCard();
    }

    /**
     * This method removes a specified card from the deck.
     *
     * @param card the card to be removed from the deck
     * @throws CardNotFoundException if the specified card is not found in the deck
     */
    public void popCard(GameCard card) throws CardNotFoundException {
        deck.popCard(card);
    }

    /**
     * This method returns the size of the available resource cards in the deck.
     *
     * @return the size of the available resource cards
     */
    public int getAvailableResCardsSize() {
        return deck.availableResCards().size();
    }

    /**
     * This method returns the size of the available gold cards in the deck.
     *
     * @return the size of the available gold cards
     */
    public int getAvailableGoldCardsSize() {
        return deck.availableGoldCards().size();
    }

    /**
     * This method returns the visible resource cards from the deck.
     *
     * @return an array of visible resource cards
     */
    public GameCard[] getVisibleResCards() {
        return deck.visibleResCards();
    }

    /**
     * This method returns the visible gold cards from the deck.
     *
     * @return an array of visible gold cards
     */
    public GameCard[] getVisibleGoldCards() {
        return deck.visibleGoldCards();
    }

    /**
     * This method returns the current game state.
     *
     * @return the current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * This method returns the current deck state.
     *
     * @return the current deck state
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * This method returns the list of winning players.
     *
     * @return the list of winning players (usually 1, but ties are possible)
     * @throws IllegalGameStateException if the game has not ended yet
     */
    public List<Player> getWinners() throws IllegalGameStateException {
        if (gameState == GameState.ENDING) {

            // calculate contributing score of objective cards
            for (Player player : players) {
                player.evaluateObjectiveScore(player.getPlayerObjectiveCard());
                player.evaluateObjectiveScore(commonObjectives[0]);
                player.evaluateObjectiveScore(commonObjectives[1]);
            }

            int maxScore = players.stream()
                    .mapToInt(player -> player.getPlayerScore() + player.getPlayerObjectiveScore())
                    .max()
                    .orElseThrow(IllegalGameStateException::new);

            List<Player> winners = players.stream()
                    .filter(player -> player.getPlayerScore() + player.getPlayerObjectiveScore() == maxScore)
                    .collect(Collectors.toList());


            // case more than 1 winner
            if (winners.size() > 1) {

                int maxObjectiveScore = winners.stream()
                        .mapToInt(Player::getPlayerObjectiveScore)
                        .max()
                        .orElseThrow(IllegalGameStateException::new);

                return winners.stream()
                        .filter(player -> player.getPlayerObjectiveScore() == maxObjectiveScore)
                        .collect(Collectors.toList());

            }

            return winners;


        }

        throw new IllegalGameStateException("Game has not ended yet");
    }
    /**
     * This method returns the player associated with the current client.
     *
     * @return the player associated with the current client
     */
    /**
     * This method returns the player associated with the current client.
     *
     * @return the player associated with the current client, or null if not found
     */
    public Player getSelf() {
        for (Player player : players) {
            if (player.getNickname().equals(selfNickname)) {
                return player;
            }
        }
        return null; // Player not found
    }


    /**
     * This method returns the currently playing player.
     *
     * @return the currently playing player
     */
    public Player getPlayingPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * The companion factory class for the Game class.
     */
    public static class Factory {

        private Lobby lobby;

        /**
         * Constructor for the Factory class.
         */
        public Factory() {
            this.lobby = null;
        }

        /**
         * This method sets the lobby for the game.
         *
         * @param lobby
         * @return
         */
        public Factory fromLobby(Lobby lobby) {
            this.lobby = lobby;

            return this;
        }

        /**
         * This method builds the game object.
         *
         * @return the game object
         */
        public Game build() {
            // Initialize an empty list of players and get the shuffled objective cards
            List<Player> players = new ArrayList<>();
            List<ObjectiveCard> objectiveCards = GameResources.getInstance().getShuffledObjectiveCards();
            List<GameCard> starterCards = GameResources.getInstance().getShuffledStarterCards();

            // Pick the common objectives
            ObjectiveCard[] commonObjectives = new ObjectiveCard[2];
            commonObjectives[0] = objectiveCards.removeFirst();
            commonObjectives[1] = objectiveCards.removeFirst();

            // Create the game object
            Game game = new Game(players, commonObjectives);

            // Create a player for each lobbyPlayer in the lobby
            for (LobbyPlayer lobbyPlayer : lobby.getPlayers()) {
                ObjectiveCard[] availableObjectives = new ObjectiveCard[2];
                availableObjectives[0] = objectiveCards.removeFirst();
                availableObjectives[1] = objectiveCards.removeFirst();

                Player player = new Player(lobbyPlayer.getNickname(), lobbyPlayer.getPlayerPawn(), starterCards.removeFirst(), availableObjectives);

                players.add(player);
            }

            // Each player receives 2 resource cards and 1 gold card
            for (Player player : players) {
                player.addPlayableCard(game.pickRandomResCard());
                player.addPlayableCard(game.pickRandomResCard());
                player.addPlayableCard(game.pickRandomGoldCard());
            }

            // Shuffle the player list
            Collections.shuffle(players);

            return game;
        }

    }

}
