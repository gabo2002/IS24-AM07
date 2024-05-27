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
import java.util.Date;
import java.util.List;

/**
 * Represents a chat message.
 *
 * @param senderNickname    the sender nickname -> it is the nickname of the player who sent the message
 * @param receiverNicknames the receiver nicknames -> it is the list of nicknames of the players who will receive the message
 * @param message           the message
 * @param timestamp         the timestamp of the message -> it is the time when the message was created
 */
public record ChatMessage(
        String senderNickname,
        List<String> receiverNicknames,
        String message,
        Date timestamp
) implements Serializable {
    public ChatMessage(String senderNickname, List<String> receiverNicknames, String message) {
        this(senderNickname, receiverNicknames, message, new Date());
    }
}
