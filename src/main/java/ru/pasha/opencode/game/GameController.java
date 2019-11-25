package ru.pasha.opencode.game;

import ru.pasha.opencode.entity.Answer;
import ru.pasha.opencode.entity.Log;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    /**
     * Counts the number of bulls and cows in <code>number</code>.
     * @param answer answer for current game
     * @param number the number that the user entered
     * @param step current step number
     * @return object with step number, user's number, cows and bull in user's number
     */
    public static GameStep check(Answer answer, int number, int step){
        String ans = String.valueOf(answer.getNumber());
        String[] digits = String.valueOf(number).split("");
        int cows = 0, bulls = 0;

        for (int i = 0; i < digits.length; i++){
            if (digits[i].charAt(0) == ans.charAt(i)){
                bulls++;
            }else if (ans.contains(digits[i])){
                cows++;
            }
        }

        return new GameStep(step, number, bulls, cows);
    }

    /**
     * Convert log list to game step list.
     * @param logs logs of game
     * @param answer answer for game
     * @return game step list
     */
    public static List<GameStep> convertToGameStepList(List<Log> logs, Answer answer){
        List<GameStep> stepList = new ArrayList<>();
        for (Log log : logs){
            stepList.add(check(answer, log.getNumber(), log.getStepNumber()));
        }
        return stepList;
    }
}
