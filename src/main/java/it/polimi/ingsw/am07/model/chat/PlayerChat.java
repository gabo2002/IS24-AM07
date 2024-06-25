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

package it.polimi.ingsw.am07.model.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the chat state for a player.
 */
public final class PlayerChat implements Serializable {

    /**
     * The messages in the chat.
     */
    private final List<ChatMessage> messages;

    /**
     * The players connected to the chat.
     */
    private final List<String> players;

    /**
     * The nickname of the player.
     */
    private final String nickname;

    /**
     * Creates a new player chat.
     *
     * @param players  the players connected to the chat
     * @param nickname the nickname of the player
     */
    public PlayerChat(List<String> players, String nickname) {
        this.players = players;
        this.nickname = nickname;
        messages = new ArrayList<>();
    }

    /**
     * Sends a broadcast message to all players.
     *
     * @param message the message
     * @return the chat message
     */
    public ChatMessage sendBroadcastMessage(String message) {
        return new ChatMessage(nickname, players, message);
    }

    /**
     * Sends a private message to a specific player.
     *
     * @param receiver the receiver
     * @param message  the message
     * @return the chat message
     */
    public ChatMessage sendPrivateMessage(String receiver, String message) {
        return new ChatMessage(nickname, List.of(receiver), message);
    }

    /**
     * Gets the messages in the chat.
     *
     * @return the messages
     */
    public List<ChatMessage> getMessages() {
        return messages;
    }

    /**
     * Gets the last n messages in the chat.
     *
     * @param n the number of messages
     * @return the messages
     */
    public List<ChatMessage> getLastMessages(int n) {
        return messages.subList(Math.max(messages.size() - n, 0), messages.size());
    }

    /**
     * Gets the players connected to the chat.
     *
     * @return the players
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Adds a player to the chat.
     *
     * @param player the player to add
     */
    public void addPlayer(String player) {
        players.add(player);
    }

    /**
     * Adds a message to the chat.
     *
     * @param message the message to add
     */
    public void insertMessage(ChatMessage message) {
        if (messages.isEmpty()) {
            messages.add(message);
            return;
        }

        // Sort messages by timestamp
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).timestamp().before(message.timestamp())) {
                messages.add(i + 1, message);
                return;
            }
        }
    }

}
