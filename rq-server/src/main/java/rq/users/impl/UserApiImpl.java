package rq.users.impl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import rq.*;
import rq.users.UserApi;

import java.util.concurrent.CompletableFuture;

public class UserApiImpl extends AbstractApi implements UserApi {

    public UserApiImpl(ReDiQuadri storage) {
        super(storage);
    }
    @Override
    public CompletableFuture<Void> registerUser(User user) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().register(user);
                        return null;
                    } catch (ConflictException e) {
                        throw new ConflictResponse();
                    }catch (IllegalArgumentException e){
                        throw new BadRequestResponse();
                    }
                }
        );
    }
    @Override
    public CompletableFuture<User> getUserById(String userId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().getUserById(userId);
                    } catch (IllegalArgumentException e){
                        throw new BadRequestResponse();
                    } catch (MissingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
    @Override
    public CompletableFuture<Void> lostLife(String nickname, String lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().lostLife(nickname, lobbyId);
                        return null;
                    } catch (IllegalArgumentException e){
                        throw new BadRequestResponse();
                    } catch (MissingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
    @Override
    public CompletableFuture<Void> removeUserFromLobby(String nickname) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().removeUserFromLobby(nickname);
                        return null;
                    } catch (IllegalArgumentException e){
                        throw new BadRequestResponse();
                    } catch (MissingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Void> removeUserById(String nickname) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().removeUserById(nickname);
                        return null;
                    } catch (IllegalArgumentException e){
                        throw new BadRequestResponse();
                    } catch (MissingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );    }

    @Override
    public CompletableFuture<Void> setUserLastSignal(String nickname) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().setUserLastSignal(nickname);
                        return null;
                    } catch (IllegalArgumentException e){
                        throw new BadRequestResponse();
                    } catch (MissingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
