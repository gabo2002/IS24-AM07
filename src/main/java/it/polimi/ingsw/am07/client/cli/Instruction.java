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

package it.polimi.ingsw.am07.client.cli;

public enum Instruction {

    // instruction_line = command you want to execute
    // params = number of params of the command

    CREATE_LOBBY("create_lobby"),
    JOIN_LOBBY("join_lobby"),
    SELECT_CARD("select_card"),
    PLACE_CARD("place_card"),
    PICK_CARD("pick_card"),
    SHOW_FIELD("show_field"),
    SHOW_DECK("show_deck"),
    SHOW_HAND("show_hand"),
    SEND_MESSAGE("send_message"),
    SHOW_CHAT("show_chat"),
    START_GAME("start_game"),
    SHOW_LOBBY_PLAYER("show_lobby_player"),
    QUIT("quit");

    // linkare con le azioni
    Instruction(String instruction_line) {
    }
}
