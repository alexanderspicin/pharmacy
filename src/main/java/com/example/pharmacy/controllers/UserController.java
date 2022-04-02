package com.example.pharmacy.controllers;


import com.example.pharmacy.DTO.UserDTO;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.payload.request.SignupRequest;
import com.example.pharmacy.service.EmailSender;
import com.example.pharmacy.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Objects;


@RestController()
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final EmailSender emailSender;

    public UserController(UserService userService, EmailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }


    @PostMapping("/new")
    public ResponseEntity<String> saveUser(@RequestBody SignupRequest signupRequest) {
        try {
            userService.save(signupRequest);
            emailSender.sendWelcomeEmail(signupRequest.getEmail(), signupRequest.getUsername());
            return new ResponseEntity("User created", HttpStatus.OK);
        } catch (DataIntegrityViolationException exception) {
            return new ResponseEntity("User with this username already created", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (MessagingException e) {
            /*log IT!!!!!*/
            return new ResponseEntity("User created", HttpStatus.OK);
        }
    }


    @GetMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> editUser(Principal principal, @RequestBody UserDTO userDTO) {
        if (principal == null || !Objects.equals(principal.getName(), userDTO.getUsername())) {
            return new ResponseEntity<>("Not authorize", HttpStatus.UNAUTHORIZED);
        }
        try {
            userService.updateProfile(userDTO);
        } catch (RuntimeException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Updated", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        try {
            User user = userService.getCurrentUser(principal);
            UserDTO userDTO = userService.userToUserDTO(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NullPointerException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/Role")
    public ResponseEntity<String> getUserRole(Principal principal) {
        try {
            User user = userService.getCurrentUser(principal);
            return new ResponseEntity<>(user.getRole().toString(), HttpStatus.OK);
        } catch (NullPointerException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(name = "id") Long id) {
        try {
            UserDTO userDTO = userService.loadUserById(id);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NullPointerException | UsernameNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable(name = "email") String email) {
        try {
            UserDTO userDTO = userService.loadUserByEmail(email);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NullPointerException | UsernameNotFoundException usernameNotFoundException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
