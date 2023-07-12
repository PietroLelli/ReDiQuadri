package rq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lobby {

    String id;
    Boolean available;
    List<User> users;

    public Lobby(String id) {
       this.id = id;
       this.available = true;
       this.users = new ArrayList<>();
    }

    public Lobby (Lobby lobby) {
        this.id = lobby.id;
        this.available = lobby.getAvailable();
        this.users = lobby.users;
    }

    public Lobby(String id, List<User> users) {
        this.id = id;
        this.available = true;
        this.users = users;
    }

    public Lobby(String id, Boolean available, List<User> users) {
        this.id = id;
        this.available = available;
        this.users = users;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "id='" + id + '\'' +
                ", avaiable=" + available +
                ", users=" + users +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(id, lobby.id) && Objects.equals(users, lobby.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users);
    }
}
