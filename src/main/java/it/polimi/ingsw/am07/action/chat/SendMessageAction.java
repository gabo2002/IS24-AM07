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

package it.polimi.ingsw.am07.action.chat;

import it.polimi.ingsw.am07.action.PlayerAction;
import it.polimi.ingsw.am07.chat.ChatMessage;
import it.polimi.ingsw.am07.model.game.Game;

/**
 * Action representing the sending of a message.
 * This action is executed by the server and reflects the message to all players.
 * This action is used to send a message to all players. The message is inserted in the chat of all players.
 */
public class SendMessageAction extends PlayerAction {

    private final ChatMessage message;

    public SendMessageAction(String playerNickname, String identity, ChatMessage message) {
        super(playerNickname, identity);
        this.message = message;
    }

    /**
     * Execute the action.
     *
     * @param gameModel the game model
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute(Game gameModel) {
        gameModel.getPlayers().stream().filter(player -> message.receiverNicknames().contains(player.getNickname())).forEach(player -> player.getChat().insertMessage(message));
        return true;
    }

    /**
     * Reflect the action.
     *
     * @param gameModel the game model
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean reflect(Game gameModel) {
        if (message.receiverNicknames().contains(gameModel.getSelf().getNickname()) || message.senderNickname().equals(gameModel.getSelf().getNickname()))
            gameModel.getSelf().getChat().insertMessage(message);

        return true;
    }
}
