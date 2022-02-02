package com.example.pharmacy.controllers;


import com.example.pharmacy.payload.request.SignupRequest;
import com.example.pharmacy.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String auth(HttpServletRequest request) {
        String log_in = request.getAuthType();;
        return log_in;
    }

    @PostMapping("/new")
    public String saveUser(@RequestBody SignupRequest signupRequest, Model model) {
        try {
            if (userService.save(signupRequest)) {
                return "redirect:/";
            } else {
                model.addAttribute("user", signupRequest);
                return "user";
            }
        } catch (DataIntegrityViolationException exception) {
            System.out.println(exception.getMessage());
            return "User with this username or email already exist";
        } catch (RuntimeException e) {
            return e.getMessage();
        }

    }

    @RequestMapping("/login.html")
    public String login() {
        return "src/main/templates/login.html";
    }

}
