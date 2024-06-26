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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerChatTest {

    @Test
    void chatTest() {
        List<String> players = List.of("player1", "player2");
        PlayerChat chat = new PlayerChat(players, "nickname");

        // Validate that a broadcast message is sent to all players
        ChatMessage message = chat.sendBroadcastMessage("message");
        assertEquals(players, message.receiverNicknames());

        // Validate that a private message is sent to the correct player
        message = chat.sendBroadcastMessage("@player1 message");
        assertEquals(List.of("player1", "nickname"), message.receiverNicknames());

        // Validate that an inexistent player cannot receive a message
        assertThrows(IllegalArgumentException.class, () -> chat.sendPrivateMessage("player3", "message"));

        // Add a player to the chat
        chat.addPlayer("player3");

        // Validate that the player has been added
        assertEquals(3, chat.getPlayers().size());

        // Validate that a broadcast message is sent to all players
        message = chat.sendBroadcastMessage("message");
        assertEquals(List.of("player1", "player2", "player3"), message.receiverNicknames());

        // Validate that the chat has no messages
        assertEquals(0, chat.getMessages().size());

        // Insert a message
        chat.insertMessage(message);

        // Validate that the chat has one message
        assertEquals(1, chat.getMessages().size());
        assertEquals(1, chat.getLastMessages(1).size());

        // Insert another message
        chat.insertMessage(message);

        // Validate that the chat has two messages
        assertEquals(2, chat.getMessages().size());
        assertEquals(2, chat.getLastMessages(2).size());
        assertEquals(1, chat.getLastMessages(1).size());

        // Reset chat
        PlayerChat newChat = new PlayerChat(List.of("test"), "nickname");

        // Create three messages
        ChatMessage message1 = newChat.sendBroadcastMessage("message1");
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ChatMessage message2 = newChat.sendBroadcastMessage("message2");
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ChatMessage message3 = newChat.sendBroadcastMessage("message3");
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Insert them out of order
        newChat.insertMessage(message3);
        newChat.insertMessage(message1);
        newChat.insertMessage(message2);

        // Validate that the messages are in the correct order
        List<ChatMessage> messages = newChat.getMessages();
        assertEquals(message1, messages.get(0));
        assertEquals(message2, messages.get(1));
        assertEquals(message3, messages.get(2));
    }

}