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

package it.polimi.ingsw.am07.model;

import it.polimi.ingsw.am07.model.game.Game;
import it.polimi.ingsw.am07.model.lobby.Lobby;

import java.util.List;
import java.util.function.Consumer;

public class ClientState {

    private final Consumer<ClientState> onGameModelUpdate;
    private String identity;
    private String nickname;
    private Game gameModel;
    private Lobby lobbyModel;
    private List<Lobby> availableLobbies;
    private PlayerState playerState;


    private String clientStringErrorMessage = null;

    public ClientState(Consumer<ClientState> onGameModelUpdate, String identity) {
        gameModel = null;
        lobbyModel = null;
        nickname = null;
        playerState = PlayerState.SELECTING_LOBBY;
        this.identity = identity;
        this.onGameModelUpdate = onGameModelUpdate;
    }

    public ClientState() {
        gameModel = null;
        lobbyModel = null;
        onGameModelUpdate = null;
        identity = null;
    }

    public Game getGameModel() {
        return gameModel;
    }

    public void setGameModel(Game gameModel) {
        this.gameModel = new Game(gameModel);
    }

    public Lobby getLobbyModel() {
        return lobbyModel;
    }

    public void setLobbyModel(Lobby lobbyModel) {
        this.lobbyModel = new Lobby(lobbyModel);
    }

    public void notifyGameModelUpdate() {
        if (onGameModelUpdate != null) {
            onGameModelUpdate.accept(this);
        }
    }

    public void setLobbies(List<Lobby> availableLobbies) {
        this.availableLobbies = availableLobbies;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
        notifyGameModelUpdate();
    }

    public List<Lobby> getAvailableLobbies() {
        return availableLobbies != null ? List.copyOf(availableLobbies) : List.of();
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getClientStringErrorMessage() {
        String returnMessage = clientStringErrorMessage;
        clientStringErrorMessage = null;
        return returnMessage;
    }

    public void setClientStringErrorMessage(String clientStringErrorMessage) {
        this.clientStringErrorMessage = clientStringErrorMessage;
    }

    @Override
    public String toString() {
        return "ClientState{" +
                "identity='" + identity + '\'' +
                ", gameModel=" + gameModel +
                ", lobbyModel=" + lobbyModel +
                ", availableLobbies=" + availableLobbies +
                ", playerState=" + playerState +
                ", onGameModelUpdate=" + onGameModelUpdate +
                '}';
    }

}
