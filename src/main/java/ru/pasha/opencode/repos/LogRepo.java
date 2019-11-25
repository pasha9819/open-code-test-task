package ru.pasha.opencode.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.pasha.opencode.entity.Log;
import ru.pasha.opencode.entity.LogId;
import ru.pasha.opencode.entity.User;

import java.util.List;

public interface LogRepo extends CrudRepository<Log, LogId> {

    List<Log> findByUserAndAttemptOrderByStepNumberAsc(User user, int attempt);

    // информация о последних ходах пользователя в завершенных играх
    @Query(value = "select a.* from LOGS a " +
            "where a.USER_ID=:userId " +
            "and a.ATTEMPT <= :gamesCount " +
            "and a.STEP_NUMBER = " +
            "(select max(STEP_NUMBER) from LOGS b where b.ATTEMPT = a.ATTEMPT and b.USER_ID = a.USER_ID) " +
            "order by a.ATTEMPT desc", nativeQuery = true)
    Page<Log> findAttemptHistoryByUser(int userId, int gamesCount, Pageable pageable);
}
