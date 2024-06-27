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

package it.polimi.ingsw.am07.action.server;

import it.polimi.ingsw.am07.action.ServerAction;
import it.polimi.ingsw.am07.model.ClientState;
import it.polimi.ingsw.am07.model.PlayerState;
import it.polimi.ingsw.am07.model.game.Deck;
import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.game.Player;
import it.polimi.ingsw.am07.model.game.card.GameCard;
import it.polimi.ingsw.am07.model.lobby.Lobby;
import it.polimi.ingsw.am07.model.matchmaking.Matchmaking;

/**
 * Action to hang a game when a player loses connection.
 */
public class HangGameAction extends ServerAction {
    /**
     * Constructor.
     * @param identity the player's identity
     */
    public HangGameAction(String identity) {
        super(identity);
    }

    /**
     * Execute the action on the game.
     * @param gameModel the game model
     */
    @Override
    public void execute(Game gameModel) {
        Player disconnectedPlayer = gameModel.addDisconnectedPlayer(getIdentity());

        if (gameModel.getPlayingPlayer().equals(disconnectedPlayer)) {
            // If the disconnected player is the one playing, we need to skip his turn
            gameModel.incrementTurn();

            // If the player had placed a card, we need to add a new one to his hand
            if (disconnectedPlayer.getPlayableCards().size() != Player.MAX_HAND_SIZE) {
                GameCard card;
                Deck deck = gameModel.getDeck();

                // Let's pick a random card from the deck
                card = deck.popRandomResCard();

                if (card == null) {
                    // The resource deck might be empty
                    card = deck.popRandomGoldCard();
                }

                if (card == null) {
                    // Check the visible cards to see if we can pick one of them
                    if (deck.visibleResCards()[0] != null) {
                        card = deck.visibleResCards()[0];
                    } else if (deck.visibleResCards()[1] != null) {
                        card = deck.visibleResCards()[1];
                    } else if (deck.visibleGoldCards()[0] != null) {
                        card = deck.visibleGoldCards()[0];
                    } else if (deck.visibleGoldCards()[1] != null) {
                        card = deck.visibleGoldCards()[1];
                    }

                    if (card != null) {
                        try {
                            deck.popCard(card);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                if (card != null) {
                    disconnectedPlayer.addPlayableCard(card);
                }
            }
        }
    }

    /**
     * Execute the action on the lobby.
     * @param lobbyModel the lobby model
     */
    @Override
    public void execute(Lobby lobbyModel) {
        System.out.println("Execute: connection lost");
    }

    /**
     * Execute the action on the matchmaking.
     * @param matchmaking the matchmaking model
     */
    @Override
    public void execute(Matchmaking matchmaking) {
        System.out.println("Execute: connection lost");
    }

    /**
     * Reflect the action on the client state.
     * @param state the client state
     */
    @Override
    public void reflect(ClientState state) {
        System.out.println("Reflect: connection lost");

        Game game = state.getGameModel();

        // If I'm the player who lost connection, I must set myself in the correct playerstate
        if (state.getIdentity().equals(getIdentity())) {
            state.setPlayerState(PlayerState.DISCONNECTED);
        } else if (game != null) {
            execute(game);

            if (state.getPlayerState().equals(PlayerState.GAME_ENDED)) {
                // The game has ended, no need to update the player state
                return;
            }

            Player self = game.getSelf();
            if (self.getPlacedCards().isEmpty()) {
                // The player has not yet picked a starter card. This means that the game has just started
                state.setPlayerState(PlayerState.SELECTING_STARTER_CARD_SIDE);
            } else {
                state.refreshPlayerState();
            }
        }

        state.notifyGameModelUpdate();
    }

}
