package ru.pasha.opencode.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.pasha.opencode.entity.User;
import ru.pasha.opencode.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Check user information and try add him to DM.
     * @param passwordConfirm password confirmation value
     * @param user user who wants to register
     * @param bindingResult result of user validation
     * @return map with response status or registration errors
     */
    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> registration(
            @RequestParam String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult){
        Map<String, String> map = new HashMap<>();
        if (bindingResult.hasErrors()){
            for(FieldError f : bindingResult.getFieldErrors()){
                map.put(f.getField() + "Error", f.getDefaultMessage());
            }
        }
        if (passwordConfirm.isEmpty()){
            map.put("passwordConfirmError", "Введите пароль еще раз");
        }
        if (!user.getUsername().matches("^[A-Za-z]+$")){
            map.put("usernameError", "Допустимы только латинские буквы");
        }
        if (!user.getPassword().equals(passwordConfirm)) {
            map.put("passwordConfirmError", "Пароли не совпадают");
        }
        if (map.isEmpty()){
            if (userService.addUser(user)) {
                map.put("status", "ok");
                return map;
            } else {
                map.put("usernameError", "Этот логин уже занят");
            }
        }
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        map.put("passwordConfirm", passwordConfirm);

        return map;
    }

    @GetMapping
    public String registration(){
        return "registration.html";
    }
}
