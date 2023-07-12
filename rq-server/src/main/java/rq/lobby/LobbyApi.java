package rq.lobby;

import rq.Lobby;
import rq.ReDiQuadri;
import rq.User;
import rq.lobby.impl.LobbyApiImpl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LobbyApi {

    CompletableFuture<String> createLobby(Lobby lobby);
    CompletableFuture<Collection<? extends Lobby>> getLobbiesAvailable();
    CompletableFuture<Void> setLobbyUnavailable(String lobbyId);
    CompletableFuture<Lobby> getLobby(String id);
    CompletableFuture<Void> addUserInLobby(String lobbyId, User user);
    CompletableFuture<Void> addNumber(User user, int number);
    CompletableFuture<Collection<Integer>> getPuntateUsers(String lobbyId);
    CompletableFuture<String> getWinnerOfLobby(String lobbyId);
    CompletableFuture<Integer> getCorrectNumberOfLobby(String lobbyId);
    CompletableFuture<List<String>> getEliminatedOfLobby(String lobbyId);
    CompletableFuture<Void> clearWinner(String lobbyId);
    //CompletableFuture<Void> resetLobby(String lobbyId);
    CompletableFuture<Void> clearPuntateUsers (String lobbyId);
    CompletableFuture<Void> removeLobbyById(String lobbyId);
    CompletableFuture<Void> removeLosersFromLobby(String lobbyId);


    CompletableFuture<Void> resetAllLobbies();

    static LobbyApi of(ReDiQuadri storage) {
        return new LobbyApiImpl(storage);
    }

}
