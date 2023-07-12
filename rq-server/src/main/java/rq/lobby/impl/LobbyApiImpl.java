package rq.lobby.impl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import rq.*;
import rq.lobby.LobbyApi;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LobbyApiImpl extends AbstractApi implements LobbyApi {

    public LobbyApiImpl(ReDiQuadri storage) {
        super(storage);
    }


    @Override
    public CompletableFuture<String> createLobby(Lobby lobby) {
        return CompletableFuture.supplyAsync(
            () -> {
                try {
                    storage().createLobby(lobby);
                    return lobby.getId();
                } catch (IllegalArgumentException e){
                    throw new BadRequestResponse();
                } catch (ConflictException e) {
                    throw new ConflictResponse();
                }
            }
        );
    }

    @Override
    public CompletableFuture<Collection<? extends Lobby>> getLobbiesAvailable() {
        return CompletableFuture.supplyAsync(
                () -> {
                    return (List<? extends Lobby>) storage().getLobbiesAvailable();
                }
        );    }

    @Override
    public CompletableFuture<Void> setLobbyUnavailable(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().setLobbyUnavailable(lobbyId);
                    } catch (ConflictException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
        );
    }

    @Override
    public CompletableFuture<Lobby> getLobby(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().getLobby(lobbyId);
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Void> addUserInLobby(String lobbyId, User user) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().addUserInLobby(lobbyId, user);
                        return null;
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    } catch (ConflictException e) {
                        throw new ConflictResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Void> addNumber(User user, int number) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().addNumber(user, number);
                        return null;
                    } catch (IllegalArgumentException e){
                        throw new BadRequestResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Collection<Integer>> getPuntateUsers(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    List<Integer> puntate = null;
                    try {
                        puntate = storage().getPuntateUsers(lobbyId);
                    } catch (ConflictException e) {
                        throw new RuntimeException(e);
                    }
                    return puntate;
                }
        );
    }

    @Override
    public CompletableFuture<String> getWinnerOfLobby(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().getWinnerOfLobby(lobbyId);
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Integer> getCorrectNumberOfLobby(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().getCorrectNumberOfLobby(lobbyId);
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }


    @Override
    public CompletableFuture<List<String>> getEliminatedOfLobby(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().getEliminatedOfLobby(lobbyId);
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Void> clearWinner(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().clearWinner(lobbyId);
                    } catch (IllegalArgumentException | MissingException e){
                        throw new BadRequestResponse();
                    }
                    return null;
                }
        );
    }

    @Override
    public CompletableFuture<Void> clearPuntateUsers(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().clearPuntateUsers(lobbyId);
                    } catch (MissingException e){
                        throw new BadRequestResponse();
                    }
                    return null;
                }
        );
    }

    @Override
    public CompletableFuture<Void> removeLobbyById(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().removeLobbyById(lobbyId);
                    } catch (MissingException e){
                        throw new BadRequestResponse();
                    }
                    return null;
                }
        );
    }

    @Override
    public CompletableFuture<Void> removeLosersFromLobby(String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    storage().removeLosersFromLobby(lobbyId);
                    return null;
                }
        );
    }

    @Override
    public CompletableFuture<Void> resetAllLobbies() {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().resetAllLobbies();
                    } catch (IllegalArgumentException | MissingException e){
                        throw new BadRequestResponse();
                    }
                    return null;
                }
        );
    }
}