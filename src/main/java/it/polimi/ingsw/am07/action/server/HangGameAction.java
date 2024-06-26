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

public class HangGameAction extends ServerAction {

    public HangGameAction(String identity) {
        super(identity);
    }

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

    @Override
    public void execute(Lobby lobbyModel) {
        System.out.println("Execute: connection lost");
    }

    @Override
    public void execute(Matchmaking matchmaking) {
        System.out.println("Execute: connection lost");
    }

    @Override
    public void reflect(ClientState state) {
        System.out.println("Reflect: connection lost");

        Game game = state.getGameModel();

        // If I'm the player who lost connection, I must set myself in the correct playerstate
        if (state.getIdentity().equals(getIdentity())) {
            state.setPlayerState(PlayerState.DISCONNECTED);
        } else if (game != null) {
            execute(game);

            if (!game.shouldFreezeGame()) {
                // Find out whose turn it is
                String currentPlayer = game.getPlayers().get(game.getCurrentPlayerIndex()).getNickname();

                if (state.getNickname().equals(currentPlayer)) {
                    // Find out if the player is in the middle of a turn
                    int handSize = state.getGameModel().getSelf().getPlayableCards().size();

                    if (handSize == Player.MAX_HAND_SIZE) { // Must place
                        state.setPlayerState(PlayerState.PLACING_CARD);
                    } else { // Must pick
                        state.setPlayerState(PlayerState.PICKING_CARD);
                    }
                } else {
                    state.setPlayerState(PlayerState.SLEEPING);
                }
            } else {
                state.setPlayerState(PlayerState.SLEEPING);
            }
        }

        state.notifyGameModelUpdate();
    }

}
