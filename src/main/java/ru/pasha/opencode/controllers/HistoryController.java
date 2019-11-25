package ru.pasha.opencode.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pasha.opencode.entity.Log;
import ru.pasha.opencode.entity.User;
import ru.pasha.opencode.game.GameController;
import ru.pasha.opencode.game.GameStep;
import ru.pasha.opencode.repos.AnswerRepo;
import ru.pasha.opencode.repos.LogRepo;
import ru.pasha.opencode.repos.UserRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.pasha.opencode.controllers.Commons.errorResponse;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final LogRepo logRepo;
    private final AnswerRepo answerRepo;
    private final UserRepo userRepo;

    @Autowired
    public HistoryController(LogRepo logRepo, AnswerRepo answerRepo, UserRepo userRepo){
        this.logRepo = logRepo;
        this.answerRepo = answerRepo;
        this.userRepo = userRepo;
    }

    /**
     * Get history about all user attempts.
     * @param pageable param for pagination
     * @return list with logs about user games
     */
    @GetMapping
    public Map<String, Object> myHistory(@PageableDefault(sort = "attempt") Pageable pageable){
        Map<String, Object> map = new HashMap<>();
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // user из SecurityContextHolder имеет необновленные данные,
        // например, количество игр не меняется при завершении игры
        u = userRepo.findById(u.getId());
        if (pageable.getPageSize() != 10){
            return errorResponse("Указан неверный параметр количества элементов на странице " +
                    "(возможно, нужно убрать аргумент size в адресной строке)");
        }
        Page<Log> history = logRepo.findAttemptHistoryByUser(u.getId(), u.getGameCount(), pageable);
        map.put("status", "ok");
        map.put("logs", history.getContent());
        map.put("historyPageCount", history.getTotalPages());
        return map;
    }

    /**
     * Get log of a concrete user attempt.
     * @param attempt string value of attempt
     * @return log with info about concrete attempt
     */
    @GetMapping("/{attempt}")
    public Map<String, Object> myHistory(@PathVariable String attempt){
        Map<String, Object> map = new HashMap<>();
        int attemptInt;
        try{
            attemptInt = Integer.parseInt(attempt);
        }catch (NumberFormatException e){
            return errorResponse("Передано не число");
        }
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Log> logs = logRepo.findByUserAndAttemptOrderByStepNumberAsc(u, attemptInt);
        List<GameStep> stepList;
        stepList = GameController.convertToGameStepList(logs, answerRepo.findByUserAndAttempt(u, attemptInt));
        map.put("status", "ok");
        map.put("gameLog", stepList);
        return map;
    }
}
