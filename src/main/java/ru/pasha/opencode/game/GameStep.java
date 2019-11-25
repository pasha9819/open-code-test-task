package ru.pasha.opencode.game;

public class GameStep {
    private int stepNumber;
    private int number;
    private int bulls;
    private int cows;

    public GameStep() {
    }

    public GameStep(int step, int number, int bulls, int cows) {
        this.stepNumber = step;
        this.number = number;
        this.bulls = bulls;
        this.cows = cows;
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

    public int getBulls() {
        return bulls;
    }

    public void setBulls(int bulls) {
        this.bulls = bulls;
    }

    public int getCows() {
        return cows;
    }

    public void setCows(int cows) {
        this.cows = cows;
    }
}
