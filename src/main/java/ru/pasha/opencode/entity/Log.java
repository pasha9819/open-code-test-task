package ru.pasha.opencode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "logs")
@IdClass(LogId.class)
public class Log implements Serializable {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;

    @Id
    @Column(name = "USER_ID")
    private int userId;
    @Id
    private int attempt;
    @Id
    @Column(name = "STEP_NUMBER")
    private int stepNumber;

    private int number;

    public Log() {
    }

    public Log(User user, int userId, int attempt, int step, int number) {
        this.user = user;
        this.userId = userId;
        this.attempt = attempt;
        this.stepNumber = step;
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

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
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
        Log log = (Log) o;
        return userId == log.userId &&
                attempt == log.attempt &&
                stepNumber == log.stepNumber &&
                number == log.number &&
                Objects.equals(user, log.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, userId, attempt, stepNumber, number);
    }
}
