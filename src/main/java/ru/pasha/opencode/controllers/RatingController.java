package ru.pasha.opencode.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pasha.opencode.entity.User;
import ru.pasha.opencode.repos.UserRepo;

import java.util.HashMap;
import java.util.Map;

import static ru.pasha.opencode.controllers.Commons.errorResponse;

@RestController
@RequestMapping("rating")
public class RatingController {
    private final UserRepo userRepo;

    public RatingController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Get rating list with all users, who play 1 game and more.
     * @param pageable pagination parameter
     * @return map with response status, total page count and rating list
     */
    @GetMapping
    public Map<String, Object> getRating(@PageableDefault Pageable pageable){
        Map<String, Object> map = new HashMap<>();
        if (pageable.getPageSize() != 10){
            return errorResponse("Указан неверный параметр количества элементов на странице " +
                    "(возможно, нужно убрать аргумент size в адресной строке)");
        }
        Page<User> userPage = userRepo.getRating(pageable);

        map.put("status", "ok");
        map.put("rating", userPage.getContent());
        map.put("ratingPageCount", userPage.getTotalPages());
        return map;
    }
}
