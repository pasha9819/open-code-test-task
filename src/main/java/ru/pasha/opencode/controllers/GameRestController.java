package ru.pasha.opencode.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pasha.opencode.entity.Answer;
import ru.pasha.opencode.entity.Log;
import ru.pasha.opencode.entity.User;
import ru.pasha.opencode.game.GameBean;
import ru.pasha.opencode.game.GameController;
import ru.pasha.opencode.game.GameStep;
import ru.pasha.opencode.repos.AnswerRepo;
import ru.pasha.opencode.repos.LogRepo;
import ru.pasha.opencode.repos.UserRepo;

import java.util.*;

import static ru.pasha.opencode.controllers.Commons.errorResponse;

@RestController
@RequestMapping("/game")
public class GameRestController {

    private final LogRepo logRepo;
    private final AnswerRepo answerRepo;
    private final UserRepo userRepo;
    private final GameBean gameBean;

    @Autowired
    public GameRestController(GameBean gameBean, LogRepo logRepo, AnswerRepo answerRepo, UserRepo userRepo) {
        this.logRepo = logRepo;
        this.answerRepo = answerRepo;
        this.gameBean = gameBean;
        this.userRepo = userRepo;
    }

    /**
     *
     * <p>This method return map with game-status(old or new)
     * and game data (attempt number for new game or user steps for opened game.</p>
     * First, we give user-info from Spring Security Context, then we update
     * update user-info (take it from DB).
     * <p>If user start a new game this method generate answer for game
     * and write it to DB.</p>
     * <p>If user continue an old game, method get log of this game from DB</p>
     * @return response-map
     */
    @GetMapping("/start")
    public Object start(){
        Map<String, Object> response = new HashMap<>();
        User u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        u = userRepo.findById(u.getId());
        Answer lastAttemptAnswer = getLastAttemptAnswer(u);
        int lastAttempt = lastAttemptAnswer == null ? 0 : lastAttemptAnswer.getAttempt();

        if (lastAttempt == u.getGameCount()){
            int digit = generateNumber();
            lastAttempt++;

            Answer answer = new Answer(u, u.getId(), lastAttempt, digit);
            gameBean.setLastAnswer(answer);
            gameBean.setLastAttemptLogs(null);
            answerRepo.save(answer);

            response.put("game", "new");
            response.put("attempt", String.valueOf(lastAttempt));
        }else {
            List<Log> logs = getLastAttemptLogs(u, lastAttempt);
            List<GameStep> stepList = GameController.convertToGameStepList(logs, lastAttemptAnswer);
            response.put("game", "old");
            response.put("steps", stepList);
        }
        return response;
    }

    /**
     * Validate <code>userNumber</code> and return:
     * <p>error message - if <code>userNumber</code> is invalid</p>
     * <p>bulls and cows count - if <code>userNumber</code> is valid</p>
     * <p>win info - if user win</p>
     * @param userNumber - digit, which user enter at current step
     * @return response-map with bulls and cows information or error message or win information
     */
    @GetMapping("/step")
    public Object step(@RequestParam String userNumber){
        Map<String, Object> response = new HashMap<>();
        User u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        u = userRepo.findById(u.getId());
        int number;

        if (userNumber.length() != 4){
            return errorResponse("Введите число из 4 цифр");
        }
        try{
            number = Integer.parseInt(userNumber);
        }catch (NumberFormatException e){
            return errorResponse("Передано не число");
        }
        if (userNumber.charAt(0) == '0'){
            return errorResponse("Число не должно начинаться с 0");
        }
        if (!isStringContainsUniqueDigits(userNumber)){
            return errorResponse("Число не должно содержать повторяющиеся цифры");
        }

        Answer lastAttemptAnswer = getLastAttemptAnswer(u);
        if (lastAttemptAnswer == null || lastAttemptAnswer.getAttempt() == u.getGameCount()){
            return errorResponse("Вы не начали игру. Нажмите \'Играть\' на странице главного меню");
        }

        List<Log> logs = getLastAttemptLogs(u, lastAttemptAnswer.getAttempt());
        int step = logs.size() + 1;

        GameStep currentStep = GameController.check(lastAttemptAnswer, number, step);

        Log newLog = new Log(u, u.getId(), lastAttemptAnswer.getAttempt(), step, number);
        logRepo.save(newLog);
        gameBean.getLastAttemptLogs().add(newLog);

        if (currentStep.getBulls() == 4){
            gameOver(u, currentStep);
            response.put("status", "win");
            return response;
        }
        response.put("status", "ok");
        response.put("step", currentStep);
        return response;
    }

    /**
     * Generate random integer value with unique digits.
     * @return random int
     */
    private int generateNumber(){
        // array with digits
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arr.add(i);
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // use random.nextInt(...) + 1, because first digit must not be 0
        int index = random.nextInt(arr.size() - 1) + 1;
        sb.append(arr.remove(index));
        // generate second and other digits and remove them from array
        for (int i = 0; i < 3; i++) {
            index = random.nextInt(arr.size());
            sb.append(arr.remove(index));
        }
        // return int value
        return Integer.parseInt(sb.toString());
    }

    /**
     * Check string and return true if all digits in string are unique.
     * @param digits - string with digits
     * @return true - if all digits unique, false - otherwise
     */
    private boolean isStringContainsUniqueDigits(String digits){
        boolean[] temp = new boolean[10];
        for (int i = 0; i < digits.length(); i++) {
            // convert digit[i] to int
            int j = digits.charAt(i) - '0';
            // temp[j] == true means: digit has already been met before
            if (temp[j]){
                return false;
            }else {
                temp[j] = true;
            }
        }
        return true;
    }

    /**
     * Get answer for user's last game
     * If answer contains in bean - return answer from bean,
     * else - get answer from DB.
     * @param user user, who play the game now
     * @return answer for user's last game
     */
    private Answer getLastAttemptAnswer(User user){
        Answer lastAttemptAnswer = gameBean.getLastAnswer();
        if (lastAttemptAnswer == null) {
            lastAttemptAnswer = answerRepo.findFirstByUserOrderByAttemptDesc(user);
            gameBean.setLastAnswer(lastAttemptAnswer);
        }
        return lastAttemptAnswer;
    }

    /**
     * Get logs for user's last game
     * @param user user, who play the game now
     * @param lastAttempt last attempt number
     * @return logs for user's last game
     */
    private List<Log> getLastAttemptLogs(User user, int lastAttempt){
        List<Log> logs = gameBean.getLastAttemptLogs();
        if (logs == null){
            logs = logRepo.findByUserAndAttemptOrderByStepNumberAsc(user, lastAttempt);
            gameBean.setLastAttemptLogs(logs);
        }
        return logs;
    }

    /**
     * Update user rating and gameCount in DB,
     * delete answer and logs from session bean.
     * @param user user, who win the game
     * @param currentStep information about last user step before win
     */
    private void gameOver(User user, GameStep currentStep){
        int games = user.getGameCount();
        double newRating = (games * user.getRating() + currentStep.getStepNumber()) / (games + 1);
        user.setGameCount(games + 1);
        user.setRating(newRating);
        userRepo.save(user);
        gameBean.setLastAttemptLogs(null);
        gameBean.setLastAnswer(null);
    }
}
