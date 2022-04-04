package com.example.pharmacy.controllers;


import com.amazonaws.services.dynamodbv2.xspec.S;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.pharmacy.DTO.UserDTO;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.payload.request.SignupRequest;
import com.example.pharmacy.service.EmailSender;
import com.example.pharmacy.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


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


    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.findUserByUsername(username);
                List<String> roles =Collections.singletonList(user.getRole().toString());
                String accessToken = JWT.create().withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",roles)
                        .sign(algorithm);
        /*response.setHeader("accessToken", accessToken);
        response.setHeader("refreshToken", refreshToken);*/
                Map<String,String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(UNAUTHORIZED.value());
                Map<String,String> error = new HashMap<>();
                error.put("errorMessage", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
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
