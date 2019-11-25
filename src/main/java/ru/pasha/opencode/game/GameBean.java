package ru.pasha.opencode.game;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.pasha.opencode.entity.Answer;
import ru.pasha.opencode.entity.Log;

import java.io.Serializable;
import java.util.List;

/**
 * Bean with information about last user game.
 */
@Component
@SessionScope
public class GameBean implements Serializable {
    private Answer lastAnswer;
    private List<Log> lastAttemptLogs;

    public GameBean() {
    }

    /**
     * Get answer for user's last game
     * @return answer for user's last game
     */
    public Answer getLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(Answer lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    /**
     * Get logs for user's last game
     * @return logs for user's last game
     */
    public List<Log> getLastAttemptLogs() {
        return lastAttemptLogs;
    }

    public void setLastAttemptLogs(List<Log> lastAttemptLogs) {
        this.lastAttemptLogs = lastAttemptLogs;
    }
}
