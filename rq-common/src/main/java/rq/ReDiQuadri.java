package rq;

import java.util.List;

public interface ReDiQuadri {

    void register(User user) throws ConflictException;
    User getUserById(String userId) throws MissingException;
    String createLobby(Lobby lobby) throws ConflictException;
    public Lobby getLobby(String lobbyId) throws MissingException;

    List<Lobby> getLobbiesAvailable();
    void setLobbyUnavailable(String lobbyId) throws ConflictException;

    void addUserInLobby(String lobbyId, User user) throws MissingException, ConflictException;

    void addNumber(User user, int number) throws IllegalArgumentException;

    List<Integer> getPuntateUsers (String lobbyId) throws ConflictException;
    String getWinnerOfLobby(String lobbyId) throws MissingException;
    Integer getCorrectNumberOfLobby(String lobbyId) throws MissingException;
    List<String> getEliminatedOfLobby(String lobbyId) throws MissingException;

    void clearWinner(String lobbyId) throws MissingException;
    void lostLife(String nickname, String lobbyId) throws MissingException;
    void setUserLastSignal(String nickname) throws MissingException;

    void removeUserFromLobby(String nickname) throws MissingException;
    void removeUserById(String nickname) throws MissingException;
    void removeLobbyById(String lobbyId) throws MissingException;

    void removeLosersFromLobby(String lobbyId);
    void resetAllLobbies() throws MissingException;
    void clearPuntateUsers(String lobbyId) throws MissingException;

}
