package ru.pasha.opencode.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "answers")
@IdClass(AnswerId.class)
public class Answer  implements Serializable {
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;

    @Id
    @Column(name = "USER_ID")
    private int userId;

    @Id
    private int attempt;

    private int number;

    public Answer() {
    }

    public Answer(User user, int userId, int attempt, int number) {
        this.user = user;
        this.userId = userId;
        this.attempt = attempt;
        this.number = number;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return userId == answer.userId &&
                attempt == answer.attempt &&
                number == answer.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, attempt, number);
    }
}
