package ru.pasha.opencode.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.pasha.opencode.entity.User;

public interface UserRepo extends CrudRepository<User, Long> {

    User findById(int id);

    User findByUsername(String username);

    /**
     * Select users, who plays one or more game (if user didn't play a game his rating = 0)
     * @param pageable pagination parameter
     * @return page witn users
     */
    @Query(value = "select a.* from USERS a WHERE a.rating > 0 order by rating asc", nativeQuery = true)
    Page<User> getRating(Pageable pageable);
}
