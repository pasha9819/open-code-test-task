package ru.pasha.opencode.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

public class AnswerId implements Serializable {

    @Column(name = "USER_ID")
    private int userId;

    private int attempt;

    public AnswerId() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerId answerId = (AnswerId) o;
        return userId == answerId.userId &&
                attempt == answerId.attempt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, attempt);
    }
}
