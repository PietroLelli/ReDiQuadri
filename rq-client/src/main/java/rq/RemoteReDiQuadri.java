package rq;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class RemoteReDiQuadri extends AbstractHttpClientStub implements ReDiQuadri {

    public RemoteReDiQuadri(URI host) {
        super(host, "rediquadri", "0.1.0");
    }

    public RemoteReDiQuadri(String host, int port) {
        this(URI.create("http://" + host + ":" + port));
    }

    private CompletableFuture<?> registerAsync(User user) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users"))
                .header("Accept", "application/json")
                .POST(body(user))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void register(User user) throws ConflictException {
        try {
            registerAsync(user).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, ConflictException.class);
        }
    }

    private CompletableFuture<User> getUserByIdAsync(String userId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/"+userId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(User.class));
    }
    @Override
    public User getUserById(String userId) throws MissingException {
        try {
            return getUserByIdAsync(userId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<String> createLobbyAsync(Lobby lobby) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .POST(body(lobby))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }
    @Override
    public String createLobby(Lobby lobby) throws ConflictException {
        try {
            return createLobbyAsync(lobby).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, ConflictException.class);
        }
    }

    private CompletableFuture<Lobby> getLobbyAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/"+lobbyId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Lobby.class));
    }

    @Override
    public Lobby getLobby(String lobbyId) throws MissingException {
        try {
            return getLobbyAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    protected CompletableFuture<List<Lobby>> getLobbiesAvailablesAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeMany(Lobby.class));
    }

    @Override
    public List<Lobby> getLobbiesAvailable() {
        return getLobbiesAvailablesAsync().join();
    }

    private CompletableFuture<?> setLobbyUnavailableAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/setUnavailable"))
                .header("Accept", "application/json")
                .PUT(body(lobbyId))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }
    @Override
    public void setLobbyUnavailable(String lobbyId) throws ConflictException {
        try {
            setLobbyUnavailableAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, ConflictException.class);
        }
    }



    private CompletableFuture<?> addUserInLobbyAsync(String lobbyId, User user) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/addUser/"+lobbyId))
                .header("Accept", "application/json")
                .POST(body(user))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void addUserInLobby(String lobbyId, User user) throws MissingException {
        try {
            addUserInLobbyAsync(lobbyId, user).join();
        }
        catch (CompletionException e) {
            if(e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            }
        }
    }

    private CompletableFuture<?> addNumberAsync(User user, int number) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/game/"+number))
                .header("Accept", "application/json")
                .POST(body(user))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public void addNumber(User user, int number) throws IllegalArgumentException {
        try {
            addNumberAsync(user, number).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, IllegalArgumentException.class);
        }
    }

    private CompletableFuture<List<Integer>> getPuntateUsersAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/puntate/"+lobbyId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeMany(Integer.class));
    }

    @Override
    public List<Integer> getPuntateUsers(String lobbyId) throws ConflictException {
        try {
            return getPuntateUsersAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, ConflictException.class);
        }
    }



    private CompletableFuture<String> getWinnerOfLobbyAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/game/"+lobbyId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public String getWinnerOfLobby(String lobbyId) throws MissingException {
        try {
            return getWinnerOfLobbyAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<Integer> getCorrectNumberOfLobbyAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/correctNumber/"+lobbyId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Integer.class));
    }
    @Override
    public Integer getCorrectNumberOfLobby(String lobbyId) throws MissingException {
        try {
            return getCorrectNumberOfLobbyAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<List<String>> getEliminatedOfLobbyAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/loser/"+lobbyId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeMany(String.class));
    }
    @Override
    public List<String> getEliminatedOfLobby(String lobbyId) throws MissingException {
        try {
            return getEliminatedOfLobbyAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }



    private CompletableFuture<?> clearWinnerAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .PUT(body(lobbyId))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }
    @Override
    public void clearWinner(String lobbyId) throws MissingException {
        try {
            clearWinnerAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<?> lostLifeAsync(String nickname, String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/"+lobbyId))
                .header("Accept", "application/json")
                .PUT(body(nickname))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void lostLife(String nickname, String lobbyId) throws MissingException {
        try {
            lostLifeAsync(nickname, lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }
    private CompletableFuture<?> setUserLastSignalAsync(String nickname) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/lastSignal/"+nickname))
                .header("Accept", "application/json")
                .PUT(body(nickname))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }
    @Override
    public void setUserLastSignal(String nickname) throws MissingException {
        try {
            setUserLastSignalAsync(nickname).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }




    private CompletableFuture<?> removeUserFromLobbyAsync(String nickname) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/"+nickname))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void removeUserFromLobby(String nickname) throws MissingException {
        try {
            removeUserFromLobbyAsync(nickname).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<?> removeUserByIdAsync(String nickname) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/remove/"+nickname))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }
    @Override
    public void removeUserById(String nickname) throws MissingException {
        try {
            removeUserByIdAsync(nickname).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<?> removeLobbyByIdAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/remove/"+lobbyId))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void removeLobbyById(String lobbyId) throws MissingException {
        try {
            removeLobbyByIdAsync(lobbyId).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<?> removeLosersFromLobbyAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/removeLosers/"+lobbyId))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void removeLosersFromLobby(String lobbyId) {
        removeLosersFromLobbyAsync(lobbyId).join();

    }

    private CompletableFuture<?> resetAllLobbiesAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/reset/all"))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }
    @Override
    public void resetAllLobbies() throws MissingException {
        try {
            resetAllLobbiesAsync().join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<?> clearPuntateUsersAsync(String lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/clearPuntate/lobby/"+lobbyId))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }
    @Override
    public void clearPuntateUsers(String lobbyId) throws MissingException {
        try {
            clearPuntateUsersAsync(lobbyId).join();
        }
        catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }
}
