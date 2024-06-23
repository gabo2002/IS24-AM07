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

/**
 * This class represents the state of the client.
 */
public class ClientState {

    /**
     * The callback function that is called when the game model is updated.
     */
    private final Consumer<ClientState> onGameModelUpdate;

    /**
     * The identity of the client.
     */
    private String identity;

    /**
     * The nickname of the client.
     */
    private String nickname;

    /**
     * The local game model.
     */
    private Game gameModel;

    /**
     * The local lobby model.
     */
    private Lobby lobbyModel;

    /**
     * The list of available lobbies.
     */
    private List<Lobby> availableLobbies;

    /**
     * The state of the player.
     */
    private PlayerState playerState;

    /**
     * The error message to be displayed to the client.
     */
    private String clientStringErrorMessage = null;

    /**
     * Constructs a new ClientState object with the given callback function and identity.
     *
     * @param onGameModelUpdate The callback function that is called when the game model is updated.
     * @param identity          The identity of the client.
     */
    public ClientState(Consumer<ClientState> onGameModelUpdate, String identity) {
        gameModel = null;
        lobbyModel = null;
        nickname = null;
        playerState = PlayerState.SELECTING_LOBBY;
        this.identity = identity;
        this.onGameModelUpdate = onGameModelUpdate;
    }

    /**
     * Retrieves the local game model.
     *
     * @return The local game model.
     */
    public Game getGameModel() {
        return gameModel;
    }

    /**
     * Sets the local game model.
     *
     * @param gameModel The local game model.
     */
    public void setGameModel(Game gameModel) {
        this.gameModel = new Game(gameModel);
        this.gameModel.setSelfNickname(nickname);
    }

    /**
     * Retrieves the local lobby model.
     *
     * @return The local lobby model.
     */
    public Lobby getLobbyModel() {
        return lobbyModel;
    }

    /**
     * Sets the local lobby model.
     *
     * @param lobbyModel The local lobby model.
     */
    public void setLobbyModel(Lobby lobbyModel) {
        this.lobbyModel = new Lobby(lobbyModel);
    }

    /**
     * Notifies the client that the game model has been updated.
     */
    public void notifyGameModelUpdate() {
        if (onGameModelUpdate != null) {
            onGameModelUpdate.accept(this);
        }
    }

    /**
     * Sets the list of available lobbies.
     *
     * @param availableLobbies The list of available lobbies.
     */
    public void setLobbies(List<Lobby> availableLobbies) {
        this.availableLobbies = availableLobbies;
    }

    /**
     * Retrieves the state of the player.
     *
     * @return The state of the player.
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * Sets the state of the player.
     *
     * @param playerState The state of the player.
     */
    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
        notifyGameModelUpdate();
    }

    /**
     * Retrieves the list of available lobbies.
     *
     * @return The list of available lobbies.
     */
    public List<Lobby> getAvailableLobbies() {
        return availableLobbies != null ? List.copyOf(availableLobbies) : List.of();
    }

    /**
     * Retrieves the identity of the client.
     *
     * @return The identity of the client.
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Sets the identity of the client.
     *
     * @param identity The identity of the client.
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     * Retrieves the nickname of the client.
     *
     * @return The nickname of the client.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the client.
     *
     * @param nickname The nickname of the client.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Retrieves the error message to be displayed to the client.
     *
     * @return The error message to be displayed to the client.
     */
    public String getClientStringErrorMessage() {
        String returnMessage = clientStringErrorMessage;
        clientStringErrorMessage = null;
        return returnMessage;
    }

    /**
     * Sets the error message to be displayed to the client.
     *
     * @param clientStringErrorMessage The error message to be displayed to the client.
     */
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
                ", nickname=" + nickname +
                ", onGameModelUpdate=" + onGameModelUpdate +
                '}';
    }

}
