package com.example.pharmacy.controllers;


import com.example.pharmacy.DTO.UserDTO;
import com.example.pharmacy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user";
    }


    @PostMapping("/new")
    public String saveUser(UserDTO userDTO, Model model) {
        if (userService.save(userDTO)) {
            return "redirect:/";
        } else {
            model.addAttribute("user", userDTO);
            return "user";
        }
    }
}
