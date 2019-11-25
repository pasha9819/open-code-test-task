package ru.pasha.opencode.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pasha.opencode.entity.User;

@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * If the user is authenticated redirect him to main-page,
     * else - show login page.
     * @return view name
     */
    @GetMapping
    public String login(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof User){
            return "redirect:/";
        }
        return "login.html";
    }
}
