package ru.pasha.opencode.entity;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

public class LogId implements Serializable {
    @Column(name = "USER_ID")
    private int userId;

    private int attempt;

    @Column(name = "STEP_NUMBER")
    private int stepNumber;

    public LogId() {
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

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogId logId = (LogId) o;
        return userId == logId.userId &&
                attempt == logId.attempt &&
                stepNumber == logId.stepNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, attempt, stepNumber);
    }
}
