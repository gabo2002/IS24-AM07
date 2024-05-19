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

package it.polimi.ingsw.am07.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PlayerChat implements Serializable {

    private final List<ChatMessage> messages;

    private final List<String> players;

    private final String nickname;

    public PlayerChat(List<String> players, String nickname) {
        this.players = players;
        this.nickname = nickname;
        messages = new ArrayList<>();
    }

    public ChatMessage sendBroadcastMessage(String message) {
        return new ChatMessage(nickname, players, message);
    }

    public ChatMessage sendPrivateMessage(String receiver, String message) {
        return new ChatMessage(nickname, List.of(receiver), message);
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public List<ChatMessage> getLastMessages(int n) {
        return messages.subList(Math.max(messages.size() - n, 0), messages.size());
    }

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String player) {
        players.add(player);
    }

    public void removePlayer(String player) {
        players.remove(player);
    }

    public void clearMessages() {
        messages.clear();
    }

    public void insertMessage(ChatMessage message) {
        // Sort messages by timestamp
        for(int i = messages.size() - 1 ; i >= 0; i--) {
            if(messages.get(i).timestamp().before(message.timestamp())) {
                messages.add(i + 1, message);
                return;
            }
        }
    }
}
