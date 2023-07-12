package rq;

import java.time.LocalTime;
import java.util.Objects;

public class User {
    public static final int LIVES = 5;
    private String nickname;
    private int lives;
    private LocalTime lastSignal;

    public User(String nickname) {
        this.nickname = nickname;
        this.lives = LIVES;
        this.lastSignal = LocalTime.now();
    }

    public User(String nickname, int lives) {
        this.nickname = nickname;
        this.lives = lives;
        this.lastSignal = LocalTime.now();
    }
    public User(User other) {
        this.nickname = other.nickname;
        this.lives= other.lives;
        this.lastSignal = LocalTime.now();
    }

    public User(String nickname, int lives, LocalTime lastSignal) {
        this.nickname = nickname;
        this.lives = lives;
        this.lastSignal = lastSignal;
    }


    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLives() {
        return lives;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }

    public LocalTime getLastSignal() {
        return lastSignal;
    }
    public void setLastSignal(LocalTime lastSignal) {
        this.lastSignal = lastSignal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return lives == user.lives && Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, lives);
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", lives=" + lives +
                '}';
    }
}
