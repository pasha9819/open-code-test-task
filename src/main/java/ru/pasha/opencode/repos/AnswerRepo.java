package ru.pasha.opencode.repos;

import org.springframework.data.repository.CrudRepository;
import ru.pasha.opencode.entity.Answer;
import ru.pasha.opencode.entity.AnswerId;
import ru.pasha.opencode.entity.User;

public interface AnswerRepo extends CrudRepository<Answer, AnswerId> {

    // ответ для последней игры
    Answer findFirstByUserOrderByAttemptDesc(User user);

    Answer findByUserAndAttempt(User user, int attempt);
}
