package rq;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class LocalReDiQuadri implements ReDiQuadri{

    public static final int MAX_SIZE_LOBBY = 5;
    public static final int INITIAL_LIVES = 5;
    static List<User> users = new ArrayList<>(); //mettere 3 vite quando finisce la partita
    static List<Lobby> lobbies = new ArrayList<>(); //remove lobby quando termina la partita
    static Map<String, Map<String, Integer>> puntateUsers = new HashMap<>();     //Map<LobbyId, Map< userId, puntata>>
    Map<String, Map<String, Integer>> winnersForLobby = new HashMap<>();     //Map<LobbyId, Map<userId, risultatocorretto >>
    static Map<String, List<String>> loserForLobby = new HashMap<>(); //remove lobby quando termina la partita

    @Override
    public synchronized void register(User user) throws ConflictException {
        var copy = new User(user); // defensive copy
        if (copy.getNickname() == null || copy.getNickname().isBlank()) {
            throw new IllegalArgumentException("Invalid username: " + copy.getNickname());
        }
        for(User u : users) {
            if(u.getNickname().equals(copy.getNickname())) {
                throw new ConflictException("Nome gia presente");
            }
        }
       users.add(user);
    }

    @Override
    public synchronized User getUserById(String userId) {
        for(User u : users) {
            if(u.getNickname().equals(userId)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public synchronized String createLobby(Lobby lobby) throws ConflictException {
        var copy = new Lobby(lobby); // defensive copy
        for(Lobby lob : lobbies) {
            if(copy.id.equals(lob.id)) {
                throw new ConflictException("Nome lobby già presente");
            }
        }
        lobbies.add(copy);
        puntateUsers.put(lobby.id, new HashMap<>());
        winnersForLobby.put(lobby.id, new HashMap<>());

        return lobby.id;
    }

    @Override
    public synchronized Lobby getLobby(String id) throws MissingException {
        Lobby lobby = null;
        for(Lobby l : lobbies) {
            if(l.id.equals(id)) {
                lobby = l;
            }
        }
        if (lobby == null) {
            throw new MissingException("No such a lobby: " + id);
        }
        return lobby;
    }

    @Override
    public synchronized List<Lobby> getLobbiesAvailable() {
        List<Lobby> lobbiesAvailable = new ArrayList<>();
        for(Lobby l : lobbies) {
            if(l.getAvailable()) {
                lobbiesAvailable.add(l);
            }
        }
        return lobbiesAvailable;
    }

    @Override
    public synchronized void setLobbyUnavailable(String lobbyId) {
        for(Lobby l : lobbies) {
            if(l.id.equals(lobbyId)) {
                l.setAvailable(false);
            }
        }
    }

    @Override
    public synchronized void addUserInLobby(String lobbyId, User user) throws MissingException {
        boolean lobbyIdExist = false;
        if(!users.contains(user)){
            users.add(user);
        }
        for(Lobby lob : lobbies) {
            if (lobbyId.equals(lob.id)) {
                lobbyIdExist = true;
                break;
            }
        }
        if (!lobbyIdExist) {
            throw new MissingException("La lobby non esiste");
        }
        Lobby lobby = getLobby(lobbyId);
        user.setLives(INITIAL_LIVES);
        lobby.users.add(user);
        //reset vite per quando inizia la nuova partita
        for(User u : users) {
            if(u.getNickname().equals(user.getNickname())) {
                u.setLives(INITIAL_LIVES);
            }
        }
        if(lobby.users.size() == MAX_SIZE_LOBBY){
            lobby.available = false;
        }
    }

    private synchronized String getLobbyIdByUser(User user) {
        for(Lobby l : lobbies) {
            if(l.users.contains(user)){
                return l.id;
            }
        }
        return null;
    }

    private synchronized int calculateResult(String lobbyId) {
        int result = 0;
        Map<String, Integer> map = puntateUsers.get(lobbyId);

        for(String nickname : map.keySet()) {
            result += map.get(nickname);
        }
        result = result/map.size();
        result = (int) (result * 0.8);

        return result;
    }

    private synchronized String lobbyWinner(String lobbyId, int correctNumber) {
        String winner = null;
        int diff = 100;
        for (String user : puntateUsers.get(lobbyId).keySet()) {
            int val = Math.abs(correctNumber - puntateUsers.get(lobbyId).get(user));
            if(val < diff) {
                winner = user;
                diff = val;
            }
        }
        //loserForLobby.remove(lobbyId);
        return winner;
    }

    @Override
    public synchronized void addNumber(User user, int number) {
        String lobbyId = getLobbyIdByUser(user);
        if(number < 0 || number > 100) {
            throw new IllegalArgumentException("Devi inserire un numero compreso tra 0 e 100");
        }
        puntateUsers.get(lobbyId).put(user.getNickname(), number);
        for(Lobby l : lobbies) {
            if (l.id.equals(lobbyId) && puntateUsers.get(lobbyId).size() == l.users.size()) {
                int result = calculateResult(lobbyId);
                Map<String, Integer> userGame = new HashMap<>();
                userGame.put(lobbyWinner(lobbyId, result), result);
                winnersForLobby.put(lobbyId, userGame);
            }
        }
    }
    @Override
    public synchronized List<Integer> getPuntateUsers(String lobbyId) {
        return new ArrayList<>(puntateUsers.get(lobbyId).values());
    }
    @Override
    public synchronized String getWinnerOfLobby(String lobbyId) {
        String winner;
        if(winnersForLobby.get(lobbyId).keySet().stream().findFirst().isPresent()) {
            winner = winnersForLobby.get(lobbyId).keySet().stream().findFirst().get();
            return winner;
        }
        return "";
    }
    @Override
    public synchronized Integer getCorrectNumberOfLobby(String lobbyId) {
        int correctNumber;
        if(winnersForLobby.get(lobbyId).values().stream().findFirst().isPresent()) {
            correctNumber = winnersForLobby.get(lobbyId).values().stream().findFirst().get();
            return correctNumber;
        }
        return null;
    }
    @Override
    public synchronized List<String> getEliminatedOfLobby(String lobbyId) {
        if(loserForLobby.containsKey(lobbyId)) {
            return loserForLobby.get(lobbyId);
        }
        return new ArrayList<>();
    }
    @Override
    public synchronized void clearWinner(String lobbyId) {
        winnersForLobby.get(lobbyId).clear();
    }
    @Override
    public synchronized void lostLife(String nickname, String lobbyId) {
        for(Lobby l : lobbies) {
            if(l.id.equals(lobbyId)){
                for(User u : l.users) {
                    if(u.getNickname().equals(nickname)) {
                        int lives = u.getLives();
                        lives -= 1;
                        u.setLives(lives);
                        for(User user : users) {
                            if(user.getNickname().equals(nickname)) {
                                user.setLives(lives);
                            }
                        }
                        if(lives == 0) {
                            if(!loserForLobby.containsKey(lobbyId)) {
                                List<String> lobbyLosers = new ArrayList<>();
                                lobbyLosers.add(nickname);
                                loserForLobby.put(lobbyId, lobbyLosers);
                            } else {
                                loserForLobby.get(lobbyId).add(nickname);
                            }
                        }
                    }
                }
            }
        }
        puntateUsers.get(lobbyId).clear(); //svuota puntate di quella lobby per il round successivo
    }
    @Override
    public synchronized void setUserLastSignal(String nickname) {
        for(Lobby lobby : lobbies) {
            for (User user : lobby.users) {
                if (user.getNickname().equals(nickname)) {
                    user.setLastSignal(LocalTime.now());
                }
            }
        }
    }
    @Override
    public synchronized void removeUserFromLobby(String nickname) {
        for(Lobby l : lobbies) {
            if(l.id.equals(getLobbyIdByUser(getUserById(nickname)))) {
                l.users.remove(getUserById(nickname));
                //puntateUsers.get(l.id).clear();//pulisce puntate se un giocatore non scrive input
                if(!loserForLobby.containsKey(l.id)) {
                    List<String> lobbyLosers = new ArrayList<>();
                    lobbyLosers.add(nickname);
                    loserForLobby.put(l.id, lobbyLosers);
                } else {
                    loserForLobby.get(l.id).add(nickname);
                }
            }
        }
    }

    @Override
    public synchronized void removeUserById(String nickname) {
        users.removeIf(user -> user.getNickname().equals(nickname));
    }

    @Override
    public synchronized void removeLobbyById(String lobbyId) {
        lobbies.removeIf(lobby -> lobby.getId().equals(lobbyId));
    }

    @Override
    public synchronized void removeLosersFromLobby(String lobbyId) {
        loserForLobby.remove(lobbyId);
        puntateUsers.get(lobbyId).clear(); //svuota puntate di quella lobby per il round successivo
    }

    @Override
    public synchronized void resetAllLobbies() {
        lobbies.clear();
        users.clear();
        puntateUsers.clear();
        winnersForLobby.clear();
        loserForLobby.clear();
    }

    @Override
    public synchronized void clearPuntateUsers(String lobbyId) {
        if(puntateUsers.containsKey(lobbyId)) {
            winnersForLobby.get(lobbyId).clear();
            puntateUsers.get(lobbyId).clear(); //svuota puntate di quella lobby per il round successivo
        }
    }

    static class CheckUserIsAlive extends TimerTask{
        @Override
        public synchronized void run() {
            LocalTime timeNow = LocalTime.now();
            List<String> toRemove = new ArrayList<>();
            for(Lobby lobby : lobbies) {
                for(User user : lobby.users) {
                    if(Math.abs(Duration.between(timeNow, user.getLastSignal()).toSeconds()) > 1) {

                        toRemove.add(user.getNickname());

                        //puntateUsers.get(lobby.getId()).clear();
                        if(!loserForLobby.containsKey(lobby.getId())) {
                            List<String> lobbyLosers = new ArrayList<>();
                            lobbyLosers.add(user.getNickname());
                            loserForLobby.put(lobby.getId(), lobbyLosers);
                        } else {
                            loserForLobby.get(lobby.getId()).add(user.getNickname());
                        }
                    }
                }
                if(toRemove.size()>0) {
                    lobby.users.removeIf(u -> toRemove.contains(u.getNickname()));
                    users.removeIf(u -> toRemove.contains(u.getNickname()));

                    if(lobby.users.size()==0){
                        lobbies.removeIf(l -> l.id.equals(lobby.getId()));
                    }
                }
            }
        }
    }
}
