package rq.users;


import rq.ReDiQuadri;
import rq.User;
import rq.users.impl.UserApiImpl;

import java.util.concurrent.CompletableFuture;

public interface UserApi {

    CompletableFuture<Void> registerUser(User user);
    CompletableFuture<User> getUserById(String userId);
    CompletableFuture<Void> lostLife(String nickname, String lobbyId);
    CompletableFuture<Void> removeUserFromLobby(String nickname);
    CompletableFuture<Void> removeUserById(String nickname);

    CompletableFuture<Void> setUserLastSignal(String nickname);

    static UserApi of(ReDiQuadri storage) {
        return new UserApiImpl(storage);
    }

}
